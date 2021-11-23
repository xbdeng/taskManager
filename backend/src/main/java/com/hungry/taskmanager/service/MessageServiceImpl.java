package com.hungry.taskmanager.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.component.WebSocketServer;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.Message;
import com.hungry.taskmanager.entity.Team;
import com.hungry.taskmanager.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.websocket.Session;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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


    @Override
    public int sendRequest(InvitationDTO invitation) throws Exception {
        Integer type = invitation.getType();

        switch(type){
            case(0):{
                // get sender receiver id
                BigInteger sender = userMapper.getIdByName(invitation.getFrom());
                List<User> receivers = userMapper.selectList(new QueryWrapper<User>().in("username", invitation.getUsernameTo()));
                List<Message> messages = new ArrayList<>();
                // insert message into database
                for(User receiver: receivers){
                    // check whether user is already in team
                    if (teamService.isInTeam(receiver.getUserId(), invitation.getTeamId())) return 201;
                    Message message = new Message().setSender(sender).setReceiver(receiver.getUserId()).setType("g_invitation").setContent(invitation.getTeamId().toString());
                    messageMapper.insert(message);
                }
                if (receivers.size() == 0) return 202;
                Team team = teamMapper.selectById(invitation.getTeamId());
                WebSocketMessageDTO wsm = new WebSocketMessageDTO().setFrom(invitation.getFrom()).setGroupName(team.getTeamName()).setType(type);
                String text = JSON.toJSONString(wsm);
                server.broadcast(text, invitation.getUsernameTo());
                break;
            }
            case(1):{
                Message message = new Message();
                // get sender receiver id
                BigInteger sender = userMapper.getIdByName(invitation.getFrom());
                BigInteger receiver = userMapper.getIdByName(invitation.getUsernameTo().get(0));
                if(receiver == null) return 202;
                // check whether user is a friend
                if (userService.hasAFriend(sender, receiver)) return 201;
                // insert message into database
                message.setSender(sender).setReceiver(receiver).setType("f_invitation");
                messageMapper.insert(message);
                // send invitation to the other user
                WebSocketMessageDTO wsm = new WebSocketMessageDTO().setFrom(invitation.getFrom()).setType(type);
                String text = JSON.toJSONString(wsm);
                Session session = WebSocketServer.sessionMap.get(invitation.getUsernameTo().get(0));
                server.send(text, session);
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
                List<String> usernames = new ArrayList<>();
                // insert message into database
                for (User user: ac){
                    BigInteger receiver = user.getUserId();
                    String username = user.getUsername();
                    message.setSender(sender).setReceiver(receiver).setType("g_application").setContent(invitation.getTeamId().toString());
                    messageMapper.insert(message);
                    usernames.add(username);
                }
                // broadcast
                WebSocketMessageDTO wsm = new WebSocketMessageDTO().setFrom(invitation.getFrom()).setType(invitation.getType()).setGroupName(team.getTeamName());
                String text = JSON.toJSONString(wsm);
                server.broadcast(text, usernames);
                break;
            }
            default:{
                throw new Exception("invitation type mismatch");
            }
        }
        return 200;
    }

    @Override
    public void confirm(ConfirmDTO confirmDTO){
        // update message status in message table
        messageMapper.update(null, new UpdateWrapper<Message>().eq("message_id", confirmDTO.getMessageId()).set("is_read", 1));
        // get message
        Message message = messageMapper.selectById(confirmDTO.getMessageId());
        // choose operation according to message type
        Integer operation = confirmDTO.getOperation();
        if (operation != null && operation == 0){
            String type = message.getType();
            switch(type){
                case("g_invitation"):{
                    // get ac name
                    User ac = userMapper.selectById(message.getSender());
                    List<String> usernames = new ArrayList<>();
                    usernames.add(confirmDTO.getFrom());
                    // insert relationship
                    OppoTeamMemberDTO otm = new OppoTeamMemberDTO().setTeamId(BigInteger.valueOf(Long.parseLong(message.getContent())))
                            .setUserName(usernames);
                    teamService.addMember(otm, ac.getUsername());
                    break;
                }
                case("f_invitation"):{
                    userService.addFriend(message.getSender(), message.getReceiver());
                    break;
                }
                case("g_application"):{
                    // get member name
                    User member = userMapper.selectById(message.getSender());
                    List<String> usernames = new ArrayList<>();
                    usernames.add(member.getUsername());
                    OppoTeamMemberDTO otm = new OppoTeamMemberDTO().setTeamId(BigInteger.valueOf(Long.parseLong(message.getContent())))
                            .setUserName(usernames);
                    teamService.addMember(otm, confirmDTO.getFrom());
                }
            }
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
            switch(message.getType()){
                case("g_invitation"):{
                    mDTO.setType(0);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
                    break;
                }
                case("f_invitation"):{
                    mDTO.setType(1);
                    break;
                }
                case("g_application"):{
                    mDTO.setType(2);
                    String teamName = teamMapper.getTeamNameById(BigInteger.valueOf(Long.parseLong(message.getContent())));
                    mDTO.setMessage(teamName);
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




}
