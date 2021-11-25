package com.hungry.taskmanager.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hungry.taskmanager.dao.*;
import com.hungry.taskmanager.dto.*;
import com.hungry.taskmanager.entity.*;
import com.hungry.taskmanager.entity.relation_entity.Contact;
import com.hungry.taskmanager.entity.relation_entity.TeamUser;
import com.hungry.taskmanager.entity.relation_entity.UserTask;
import com.hungry.taskmanager.utils.ICal4jUtil;
import com.hungry.taskmanager.utils.RedisUtil;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private ContactMapper contactMapper;

    @Resource
    private TeamUserMapper teamUserMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private TaskService taskService;

    @Resource
    private MailService mailService;

    @Resource
    private UserTaskMapper userTaskMapper;

    @Resource
    private TaskMapper taskMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userMapper.findPermsByRoleId(id);
    }

    @Override
    public User getUserByPass(String username, String password) {
        return userMapper.getUserByPass(username, password);
    }

    @Override
    public List<UserDTO> getAddressBook(String username) {
        BigInteger userId = userMapper.getIdByName(username);
        List<BigInteger> friendsId = contactMapper.selectList(new QueryWrapper<Contact>().eq("person", userId)).stream().map(Contact::getFriend).collect(Collectors.toList());
        if (friendsId.isEmpty()) {
            return new ArrayList<UserDTO>();
        }
        return userMapper.selectList(new QueryWrapper<User>().in("user_id", friendsId)).stream().map(User::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public List<TeamDTO> getTeams(String username) {
        BigInteger userId = userMapper.getIdByName(username);
        //获取全部的teamsId
        List<BigInteger> teamsId = teamUserMapper.selectList(new QueryWrapper<TeamUser>().eq("user_id", userId)).stream().map(TeamUser::getTeamId).collect(Collectors.toList());
        //获取全部的teams
        //获取全部的teams
        if (teamsId.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return getTeamDTOsByTeamIds(teamsId, userId);//todo
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<TeamDTO> getAdminTeams(String username) {
        BigInteger userId = userMapper.getIdByName(username);
        //获取管理的的teamsId
        List<String> t = new ArrayList<>(2);
        t.add("admin");
        t.add("creator");
        List<BigInteger> teamsId = teamUserMapper.selectList(new QueryWrapper<TeamUser>().eq("user_id", userId).in("identity", t)).stream().map(TeamUser::getTeamId).collect(Collectors.toList());

        //获取全部的teams
        if (teamsId.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return getTeamDTOsByTeamIds(teamsId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private List<TeamDTO> getTeamDTOsByTeamIds(List<BigInteger> teamIds, BigInteger userId) throws Exception {
        // get all teams and put them into maps
        List<Team> teams = teamMapper.selectList(new QueryWrapper<Team>().in("team_id", teamIds));
        HashMap<BigInteger, TeamDTO> teamMap = new HashMap<>();
        List<TeamDTO> returnList = new ArrayList<>();
        for (Team team : teams) {
            TeamDTO newTeam = new TeamDTO().setTeamId(team.getTeamId()).setTeamName(team.getTeamName())
                    .setDescription(team.getDescription()).setCreatTime(team.getCreateTime()).setAdmins(new ArrayList<>()).setMembers(new ArrayList<>());
            teamMap.put(team.getTeamId(), newTeam);
            returnList.add(newTeam);
        }
        // get user relationship
        List<HashMap<String, Object>> teamUserMaps = teamMapper.getTeamUserRelationships(teams);
        for (HashMap<String, Object> map : teamUserMaps) {
            String identity = (String) map.get("identity");
            if (identity != null) {
                String username = (String) map.get("username");
                BigInteger teamId = BigInteger.valueOf((Long) map.get("team_id"));
                if ("admin".equals(identity))
                    teamMap.get(teamId).getAdmins().add(username);
                if ("member".equals(identity))
                    teamMap.get(teamId).getMembers().add(username);
                if ("creator".equals(identity))
                    teamMap.get(teamId).setCreator(username);
            }
        }
        // get all related task
        for (TeamDTO team : teamMap.values()) {
            List<Task>tasks = taskService.queryTask(new QueryTaskDTO().setTeamId(team.getTeamId()));
            for(Task task:tasks){
                BigInteger taskId = task.getTaskId();
                List<BigInteger> userIds =  userTaskMapper.selectList(new QueryWrapper<UserTask>().eq("task_id",taskId)).stream().map(UserTask::getUserId).collect(Collectors.toList());
                List<String> users = userMapper.selectList(new QueryWrapper<User>().in("user_id",userIds).select("username")).stream().map(User::getUsername).collect(Collectors.toList());
                task.setMembers(users);
            }
            team.setTeamTasks(tasks);
        }
        return returnList;

    }

    @Override
    public UserDTO getProfile(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username)).toUserDTO();
    }

    @Override
    public List<Role> findRolesByUserName(String username) {
        return userMapper.findRolesByUserName(username);
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }


    @Override
    public Result register(RegisterInfoDTO registerInfoDTO) {
        userMapper.register(registerInfoDTO);
        return Result.succ("注册成功");
    }

    @Override
    public Result addTag(TagDTO params) {
        BigInteger uId = userMapper.getIdByName(params.getUsername());
        Tag tag = new Tag().setTagName(params.getTagName()).setUserId(uId);
        tagMapper.insert(tag);
        return Result.succ("添加成功");
    }

    public List<Tag> selectTags(TagDTO params) {
        BigInteger uId = userMapper.getIdByName(params.getUsername());
        QueryWrapper<Tag> wrapper = new QueryWrapper<Tag>().eq("user_id", uId);
        return tagMapper.selectList(wrapper);
    }

    @Override
    public void editUser(EditUserDTO params) {
        User user = new User();
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>().eq("username", params.getUsername());
        if (params.getPassword() != null) wrapper.set("password", params.getPassword());
        if (params.getFirstName() != null) wrapper.set("first_name", params.getFirstName());
        if (params.getLastName() != null) wrapper.set("last_name", params.getLastName());
        if (params.getEmail() != null) wrapper.set("email", params.getEmail());
        if (params.getPhone() != null) wrapper.set("phone", params.getPhone());
        userMapper.update(user, wrapper);
    }

    @Override
    public void bindGithub(String username, String githubName) {
        User user = new User();
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>().eq("username", username);
        wrapper.set("github_name", githubName);
        userMapper.update(user, wrapper);
    }

    @Override
    public void unbindGithub(String username) {
        User user = new User();
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>().eq("username", username);
        wrapper.set("github_name", null);
        userMapper.update(user, wrapper);
    }

    @Override
    public User getUserByGithub(String githubName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("github_name", githubName);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void addFriend(BigInteger idA, BigInteger idB) {
        Contact contactA = new Contact().setPerson(idA).setFriend(idB);
        Contact contactB = new Contact().setPerson(idB).setFriend(idA);
        contactMapper.insert(contactA);
        contactMapper.insert(contactB);
    }

    @Override
    public boolean hasAFriend(BigInteger person, BigInteger friend) {
        Long count = contactMapper.selectCount(new QueryWrapper<Contact>().eq("person", person).eq("friend", friend));
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeFriend(String username, String friendName) {
        BigInteger userId = userMapper.getIdByName(username);
        BigInteger friendId = userMapper.getIdByName(friendName);
        contactMapper.delete(new QueryWrapper<Contact>().eq("person", userId).eq("friend", friendId));
        contactMapper.delete(new QueryWrapper<Contact>().eq("friend", userId).eq("person", friendId));
    }

    @Override
    public void uploadImage(String username, String url) {
        userMapper.update(null, new UpdateWrapper<User>().eq("username", username).set("img", url));
    }

    @Override
    public String getImage(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        return user.getImg();
    }

    @Override
    public Result<List<UserDTO>> searchFriend(String name) {
        List<UserDTO> userDTOS = userMapper.selectList(new QueryWrapper<User>().like("username", name)).stream().map(User::toUserDTO).collect(Collectors.toList());
        return new Result<List<UserDTO>>(200, "查询成功", userDTOS);
    }

    @Override
    public Result<PersonalStatisticsDTO> personalStatistics(String username) {
        PersonalStatisticsDTO personalStatisticsDTO = new PersonalStatisticsDTO();

        BigInteger userId = userMapper.getIdByName(username);
        List<BigInteger> taskIds = userTaskMapper.selectList(new QueryWrapper<UserTask>().eq("user_id", userId).select("task_id")).stream().map(UserTask::getTaskId).collect(Collectors.toList());
        List<Task> tasks = taskMapper.selectList(new QueryWrapper<Task>().in("task_id", taskIds));

        personalStatisticsDTO.setTotalTaskNumber(tasks.size());

        int totalTaskNumber = tasks.size();
        int totalFinishTaskNumber = 0;
        int totalOverdueTaskNumber = 0;

        int totalTeamTaskNumber = 0;
        int totalFinishTeamTaskNumber = 0;
        int totalOverdueTeamTaskNumber = 0;

        Map<Integer, Integer> year_count_finish = new HashMap<>();
        Map<year_month, Integer> month_count_finish = new HashMap<>();
        Map<year_month_day, Integer> day_count_finish = new HashMap<>();

        Map<Integer, Integer> year_count_start = new HashMap<>();
        Map<year_month, Integer> month_count_start = new HashMap<>();
        Map<year_month_day, Integer> day_count_start = new HashMap<>();

        Map<Integer, Integer> year_count_due = new HashMap<>();
        Map<year_month, Integer> month_count_due = new HashMap<>();
        Map<year_month_day, Integer> day_count_due = new HashMap<>();

        for (Task task : tasks) {
            //全部任务
            if (task.getStatus() == 1) {
                totalFinishTaskNumber++;
            } else if (task.getStatus() == 2) {
                totalOverdueTaskNumber++;
            }

            //组队任务
            if (task.getType().intValue() == 1) {
                totalTeamTaskNumber++;
                if (task.getStatus() == 1) {
                    totalFinishTeamTaskNumber++;
                } else if (task.getStatus() == 2) {
                    totalOverdueTeamTaskNumber++;
                }
            }

            //已完成的任务 完成时间
            if (task.getStatus() == 1) {
                //按年分
                int year = task.getFinishDate().getYear();
                if (year_count_finish.get(year) == null) {
                    year_count_finish.put(year, 1);
                } else {
                    int t = year_count_finish.get(year);
                    year_count_finish.replace(year, t + 1);
                }

                //按月分
                int month = task.getFinishDate().getMonthValue();
                year_month ym = new year_month(year, month);
                if (month_count_finish.get(ym) == null) {
                    month_count_finish.put(ym, 1);
                } else {
                    int t = month_count_finish.get(ym);
                    month_count_finish.replace(ym, t + 1);
                }

                //按日分
                int day = task.getFinishDate().getDayOfMonth();
                year_month_day ymd = new year_month_day(year, month, day);
                if (day_count_finish.get(ymd) == null) {
                    day_count_finish.put(ymd, 1);
                } else {
                    int t = day_count_finish.get(ymd);
                    day_count_finish.replace(ymd, t + 1);
                }
            }

            //任务开始的时间
            if (task.getCreateDate() != null) {
                //按年分
                int year = task.getCreateDate().getYear();
                if (year_count_start.get(year) == null) {
                    year_count_start.put(year, 1);
                } else {
                    int t = year_count_start.get(year);
                    year_count_start.replace(year, t + 1);
                }

                //按月分
                int month = task.getCreateDate().getMonthValue();
                year_month ym = new year_month(year, month);
                if (month_count_start.get(ym) == null) {
                    month_count_start.put(ym, 1);
                } else {
                    int t = month_count_start.get(ym);
                    month_count_start.replace(ym, t + 1);
                }

                //按日分
                int day = task.getCreateDate().getDayOfMonth();
                year_month_day ymd = new year_month_day(year, month, day);
                if (day_count_start.get(ymd) == null) {
                    day_count_start.put(ymd, 1);
                } else {
                    int t = day_count_start.get(ymd);
                    day_count_start.replace(ymd, t + 1);
                }
            }

            //任务截止时间
            if (task.getDueDate() != null) {
                //按年分
                int year = task.getDueDate().getYear();
                if (year_count_due.get(year) == null) {
                    year_count_due.put(year, 1);
                } else {
                    int t = year_count_due.get(year);
                    year_count_due.replace(year, t + 1);
                }

                //按月分
                int month = task.getDueDate().getMonthValue();
                year_month ym = new year_month(year, month);
                if (month_count_due.get(ym) == null) {
                    month_count_due.put(ym, 1);
                } else {
                    int t = month_count_due.get(ym);
                    month_count_due.replace(ym, t + 1);
                }

                //按日分
                int day = task.getDueDate().getDayOfMonth();
                year_month_day ymd = new year_month_day(year, month, day);
                if (day_count_due.get(ymd) == null) {
                    day_count_due.put(ymd, 1);
                } else {
                    int t = day_count_due.get(ymd);
                    day_count_due.replace(ymd, t + 1);
                }
            }

        }

        personalStatisticsDTO.setTotalTaskNumber(totalTaskNumber)
                .setTotalFinishTaskNumber(totalFinishTaskNumber)
                .setTotalOverdueTaskNumber(totalOverdueTaskNumber)
                .setTotalTeamTaskNumber(totalTeamTaskNumber)
                .setTotalFinishTeamTaskNumber(totalFinishTeamTaskNumber)
                .setTotalOverdueTeamTaskNumber(totalOverdueTeamTaskNumber);

        List<Year_count> finishTaskNumberByYears = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : year_count_finish.entrySet()) {
            int year = entry.getKey();
            int count = entry.getValue();
            finishTaskNumberByYears.add(new Year_count(year, count));
        }
        personalStatisticsDTO.setFinishTaskNumberByYears(finishTaskNumberByYears);


        List<Month_count> finishTaskNumbersByMonths = new ArrayList<>();
        for (Map.Entry<year_month, Integer> entry : month_count_finish.entrySet()) {
            int year = entry.getKey().year;
            int month = entry.getKey().month;
            int count = entry.getValue();
            finishTaskNumbersByMonths.add(new Month_count(year, month, count));
        }
        personalStatisticsDTO.setFinishTaskNumbersByMonths(finishTaskNumbersByMonths);

        List<Day_count> finishTaskNumbersByDays = new ArrayList<>();
        for (Map.Entry<year_month_day, Integer> entry : day_count_finish.entrySet()) {
            int year = entry.getKey().year;
            int month = entry.getKey().month;
            int day = entry.getKey().day;
            int count = entry.getValue();
            finishTaskNumbersByDays.add(new Day_count(year, month, day, count));
        }
        personalStatisticsDTO.setFinishTaskNumbersByDays(finishTaskNumbersByDays);


        List<Year_count> startTaskNumberByYears = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : year_count_start.entrySet()) {
            int year = entry.getKey();
            int count = entry.getValue();
            startTaskNumberByYears.add(new Year_count(year, count));
        }
        personalStatisticsDTO.setStartTaskNumberByYears(startTaskNumberByYears);


        List<Month_count> startTaskNumbersByMonths = new ArrayList<>();
        for (Map.Entry<year_month, Integer> entry : month_count_start.entrySet()) {
            int year = entry.getKey().year;
            int month = entry.getKey().month;
            int count = entry.getValue();
            startTaskNumbersByMonths.add(new Month_count(year, month, count));
        }
        personalStatisticsDTO.setStartTaskNumbersByMonths(startTaskNumbersByMonths);

        List<Day_count> startTaskNumbersByDays = new ArrayList<>();
        for (Map.Entry<year_month_day, Integer> entry : day_count_start.entrySet()) {
            int year = entry.getKey().year;
            int month = entry.getKey().month;
            int day = entry.getKey().day;
            int count = entry.getValue();
            startTaskNumbersByDays.add(new Day_count(year, month, day, count));
        }
        personalStatisticsDTO.setStartTaskNumbersByDays(startTaskNumbersByDays);


        List<Year_count> dueTaskNumberByYears = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : year_count_due.entrySet()) {
            int year = entry.getKey();
            int count = entry.getValue();
            dueTaskNumberByYears.add(new Year_count(year, count));
        }
        personalStatisticsDTO.setDueTaskNumberByYears(dueTaskNumberByYears);


        List<Month_count> dueTaskNumbersByMonths = new ArrayList<>();
        for (Map.Entry<year_month, Integer> entry : month_count_due.entrySet()) {
            int year = entry.getKey().year;
            int month = entry.getKey().month;
            int count = entry.getValue();
            dueTaskNumbersByMonths.add(new Month_count(year, month, count));
        }
        personalStatisticsDTO.setDueTaskNumbersByMonths(dueTaskNumbersByMonths);

        List<Day_count> dueTaskNumbersByDays = new ArrayList<>();
        for (Map.Entry<year_month_day, Integer> entry : day_count_due.entrySet()) {
            int year = entry.getKey().year;
            int month = entry.getKey().month;
            int day = entry.getKey().day;
            int count = entry.getValue();
            dueTaskNumbersByDays.add(new Day_count(year, month, day, count));
        }
        personalStatisticsDTO.setDueTaskNumbersByDays(dueTaskNumbersByDays);

        return new Result<>(200, "统计成功", personalStatisticsDTO);
    }

    @Override
    public Result verifyEmail(String username) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        String email = user.getEmail();
        mailService.sendVerifyEmail(email, username);
        return Result.succ("验证码发送成功");
    }

    @Override
    public boolean verifyCode(String username, String code) {
        if (redisUtil.hasKey(username + "verifycode")) {
            if(redisUtil.get(username + "verifycode").equals(code)){
                UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("username",username).set("email_verify",1);
                userMapper.update(null,updateWrapper);
                return true;
            }
        }
        return false;
    }

    @Override
    public Result unbindEmail(String username) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username",username).set("email_verify",0).set("email",null);
        userMapper.update(null,updateWrapper);
        return Result.succ("解绑成功");
    }

    @Override
    public Result exportTask(String username) throws IOException, MessagingException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
        if(user.getEmailVerify()!=1){
            return Result.fail(201,"邮箱无效，无法发送",null);
        }
        BigInteger userId = user.getUserId();
        String email = user.getEmail();
        List<BigInteger> taskIds = userTaskMapper.selectList(new QueryWrapper<UserTask>().eq("user_id",userId).select("task_id")).stream().map(UserTask::getTaskId).collect(Collectors.toList());
        List<Task> tasks = taskMapper.selectList(new QueryWrapper<Task>().in("task_id",taskIds));
        Calendar calendar = ICal4jUtil.TasksToCalendar((ArrayList<Task>) tasks);
        mailService.sendCalendar(username,email,calendar);

        return Result.succ("导出成功");
    }
}

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
class year_month {
    int year;
    int month;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        year_month that = (year_month) o;
        return year == that.year && month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }
}

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
class year_month_day {
    int year;
    int month;
    int day;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        year_month_day that = (year_month_day) o;
        return year == that.year && month == that.month && day == that.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }
}