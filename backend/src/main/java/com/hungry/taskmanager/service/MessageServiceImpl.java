package com.hungry.taskmanager.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.component.WebSocketServer;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Message;
import com.hungry.taskmanager.entity.MessageType;
import com.hungry.taskmanager.entity.Team;
import com.hungry.taskmanager.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService{

    @Resource
    WebSocketServer server;

    @Resource
    MessageMapper messageMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    TeamMapper teamMapper;

    @Resource
    TeamService teamService;

    @Resource
    TeamUserMapper teamUserMapper;

    @Resource
    ContactMapper contactMapper;

    @Resource
    UserService userService;

    @Resource
    MessageTypeMapper messageTypeMapper;


    @Override
    public int sendRequest(InvitationDTO invitation) throws Exception {
        // get incoming type value
        Integer type = invitation.getType();
        // configuration for sending messages
        List<Message> messages = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        WebSocketMessageDTO wsm = new WebSocketMessageDTO();
        // configuration foor type query
        QueryWrapper<MessageType> typeWrapper = new QueryWrapper<MessageType>().select("mt_id");
        String typeName = "";
        switch(type){
            case(0):{
                // get sender receiver id
                BigInteger sender = userMapper.getIdByName(invitation.getFrom());
                List<User> receivers = userMapper.selectList(new QueryWrapper<User>().in("username", invitation.getUsernameTo()));
                // configure type name
                typeName = "g_invitation";
                // check whther there are receivers
                if (receivers.size() == 0) return 202;
                // insert message into database
                for(User receiver: receivers){
                    // check whether user is already in team
                    if (teamService.isInTeam(receiver.getUserId(), invitation.getTeamId())) return 201;
                    Message message = new Message().setSender(sender).setReceiver(receiver.getUserId()).setContent(invitation.getTeamId().toString());
                    messages.add(message);
                    // insert username into username list
                    usernames.add(receiver.getUsername());
                }
                Team team = teamMapper.selectById(invitation.getTeamId());
                // set wsm information
                wsm.setFrom(invitation.getFrom()).setGroupName(team.getTeamName()).setType(type);
                break;
            }
            case(1):{
                Message message = new Message();
                // get sender receiver id
                BigInteger sender = userMapper.getIdByName(invitation.getFrom());
                BigInteger receiver = userMapper.getIdByName(invitation.getUsernameTo().get(0));
                // check whether user is a friend
                if (userService.hasAFriend(sender, receiver)) return 201;
                // insert message into database
                message.setSender(sender).setReceiver(receiver);
                typeName = "f_invitation";
                messages.add(message);
                // set wsm information
                wsm.setFrom(invitation.getFrom()).setType(type);
                // add username to username list
                usernames.add(invitation.getUsernameTo().get(0));
                break;
            }
            case(2):{
                Message message = new Message();
                BigInteger sender = userMapper.getIdByName(invitation.getFrom());
                // get all group administrator and creator username
                List<User> ac = teamMapper.getTeamCreatorAndAdministrators(invitation.getTeamId());
                // check whether user is already in team
                if (teamService.isInTeam(sender, invitation.getTeamId())) return 201;
                // get team
                Team team = teamMapper.selectById(invitation.getTeamId());
                // configure type name
                typeName = "g_application";
                for (User user: ac){
                    BigInteger receiver = user.getUserId();
                    String username = user.getUsername();
                    message.setSender(sender).setReceiver(receiver).setContent(invitation.getTeamId().toString());
                    messages.add(message);
                    // add username to username list
                    usernames.add(username);
                }
                // set wsm information
                wsm.setFrom(invitation.getFrom()).setType(invitation.getType()).setGroupName(team.getTeamName());
                break;
            }
            default:{
                throw new Exception("invitation type mismatch");
            }
        }
        typeWrapper = typeWrapper.eq("type_name", typeName);
        List<Object> typeIds = messageTypeMapper.selectObjs(typeWrapper);
        if (typeIds.size() != 1) return 203;
        Long typeId  = (Long)typeIds.get(0);
        for (Message message : messages){
            message.setType(BigInteger.valueOf(typeId));
        }
        sendMessages(wsm, usernames, messages);
        return 200;
    }

    @Override
    public void confirm(ConfirmDTO confirmDTO){
        // update message status in message table
        messageMapper.update(null, new UpdateWrapper<Message>().eq("message_id", confirmDTO.getMessageId()).set("is_read", 1));
        // get message
        Message message = messageMapper.selectById(confirmDTO.getMessageId());
        // configure params for sendMessages()
        List<Message> backMessages = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        WebSocketMessageDTO wsm = new WebSocketMessageDTO();
        // varialbe for getting typeid
        String backTypeName = "";
        QueryWrapper<MessageType> typeWrapper = new QueryWrapper<MessageType>().select("mt_id");
        // type for backMessages
        // choose operation according to message type
        Integer operation = confirmDTO.getOperation();
        if (operation != null && (operation == 0 || operation == 1)){
            BigInteger typeId = message.getType();
            String typeName = messageTypeMapper.selectById(typeId).getTypeName();
            switch(typeName){
                case("g_invitation"):{
                    // get ac name
                    User operator = userMapper.selectById(message.getSender());
                    // configure param for add memeber
                    ArrayList<String> username = new ArrayList<>();
                    username.add(confirmDTO.getFrom());
                    // get TeamId
                    BigInteger teamId = BigInteger.valueOf(Long.parseLong(message.getContent()));
                    // add memebers
                    OppoTeamMemberDTO otm = new OppoTeamMemberDTO().setTeamId(teamId).setUserName(username);
                    teamService.addMember(otm, operator.getUsername());
                    Message backMessage = new Message().setSender(message.getReceiver()).setReceiver(operator.getUserId()).setContent(teamId.toString());
                    backMessages.add(backMessage);
                    usernames.add(operator.getUsername());
                    // set wsm information
                    Team team = teamMapper.selectById(teamId);
                    wsm.setFrom(confirmDTO.getFrom()).setGroupName(team.getTeamName());
                    // set type according to operation
                    if (operation == 0){
                        backTypeName = "g_invitation_accept";
                        wsm.setType(6);
                    }else{
                        backTypeName = "g_invitation_reject";
                        wsm.setType(7);
                    }
                    break;
                }
                case("f_invitation"):{
                    userService.addFriend(message.getSender(), message.getReceiver());
                    // construct message
                    Message backMessage = new Message().setReceiver(message.getSender()).setSender(message.getReceiver());
                    // add backMessage to backMessages
                    backMessages.add(backMessage);
                    // add username to username list
                    String backReceiver = userMapper.getUsernameById(message.getSender());
                    usernames.add(backReceiver);
                    // set wsm information
                    wsm.setFrom(confirmDTO.getFrom());
                    if (operation == 0){
                        backTypeName = "f_invitation_accept";
                        wsm.setType(4);
                    }else{
                        backTypeName = "f_invitation_reject";
                        wsm.setType(5);
                    }
                    break;
                }
                case("g_application"):{
                    // get member name
                    User member = userMapper.selectById(message.getSender());
                    // add memeber into the group
                    List<String> username = new ArrayList<>();
                    username.add(member.getUsername());
                    BigInteger teamId = BigInteger.valueOf(Long.parseLong(message.getContent()));
                    OppoTeamMemberDTO otm = new OppoTeamMemberDTO().setTeamId(teamId).setUserName(username);
                    teamService.addMember(otm, confirmDTO.getFrom());
                    // find all creator and administrator
                    List<User> acs = teamMapper.getTeamCreatorAndAdministrators(teamId);
                    for(User ac: acs){
                        Message backMessage = new Message().setReceiver(message.getSender()).setSender(message.getReceiver()).setContent(teamId.toString());
                        // add backMessage to backMessages
                        backMessages.add(backMessage);
                    }
                    // add username to username list
                    usernames.add(member.getUsername());
                    // set wsm
                    String teamName = teamMapper.getTeamNameById(teamId);
                    // set type
                    wsm.setFrom(confirmDTO.getFrom()).setGroupName(teamName);
                    if (operation == 0){
                        backTypeName = "g_application_accept";
                        wsm.setType(8);
                    }else{
                        backTypeName = "g_application_reject";
                        wsm.setType(9);
                    }

                }
            }
            typeWrapper = typeWrapper.eq("type_name", backTypeName);
            List<Object> typeIds = messageTypeMapper.selectObjs(typeWrapper);
            BigInteger backTypeId  = BigInteger.valueOf((Long)typeIds.get(0));
            for (Message backMessage : backMessages){
                backMessage.setType(backTypeId);
            }
            sendMessages(wsm, usernames, backMessages);
        }
    }

    @Override
    public List<MessageDTO> getMessages(String username){
        BigInteger userId = userMapper.getIdByName(username);
        List<Message> messages = messageMapper.selectList(new QueryWrapper<Message>().eq("receiver", userId));
        List<MessageDTO> data = new ArrayList<>();
        for (Message message : messages){
            MessageDTO mDTO = new MessageDTO();
            mDTO.setUsernameFrom(userMapper.getUsernameById(message.getSender())).setMessageId(message.getMessageId());
            // assign type status according to message condition
            switch(message.getType().intValue()){
                case(1):{
                    mDTO.setType(0);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    break;
                }
                case(2):{
                    mDTO.setType(1);
                    break;
                }
                case(3):{
                    mDTO.setType(2);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    break;
                }
                case(4):{
                    mDTO.setType(3);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    message.setIsRead(1);
                    break;
                }
                case(5):{
                    mDTO.setType(4);
                    message.setIsRead(1);
                    break;
                }
                case(6):{
                    mDTO.setType(5);
                    message.setIsRead(1);
                    break;
                }
                case(7):{
                    mDTO.setType(6);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    message.setIsRead(1);
                    break;
                }
                case(8):{
                    mDTO.setType(7);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    message.setIsRead(1);
                    break;
                }
                case(9):{
                    mDTO.setType(8);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    message.setIsRead(1);
                    break;
                }
                case(10):{
                    mDTO.setType(9);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    message.setIsRead(1);
                    break;
                }
            }
            if (message.getIsRead() == 1){
                mDTO.setStatus("read");
            }else if (message.getIsRead() == 0){
                switch(mDTO.getType()){
                    case(0):{
                        if (teamService.isInTeam(userId, BigInteger.valueOf(Long.parseLong(message.getContent())))){
                            mDTO.setStatus("handled");
                        }else{
                            mDTO.setStatus("unhandled");
                        }
                        break;
                    }
                    case(1):{
                        if (userService.hasAFriend(userId, message.getSender())){
                            mDTO.setStatus("handled");
                        }else{
                            mDTO.setStatus("unhandled");
                        }
                        break;
                    }
                    case(2):{
                        if (teamService.isInTeam(message.getSender(), BigInteger.valueOf(Long.parseLong(message.getContent())))){
                            mDTO.setStatus("handled");
                        }else{
                            mDTO.setStatus("unhandled");
                        }
                        break;
                    }
                }
            }
            data.add(mDTO);
        }
        return data;
    }

    public void sendMessages(WebSocketMessageDTO wsm, List<String> names, List<Message> messages){
        // broadcast
        server.broadcast(wsm.toJsonString(), names);
        // insert into database
        for (Message message : messages){
            messageMapper.insert(message);
        }
    }




}
