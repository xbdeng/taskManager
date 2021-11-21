<template>
  <!-- 这个页面是个人主页面 -->
  <div class='mainFrame'>
    <el-container>
      <!-- 头部栏 -->
      <el-header class="mainFrameHeader">
        <el-row type="flex" align="middle" justify="start">
          <!-- 标题 -->
          <el-col :span="20">
            <span class="taskManager">Task Manager</span>
          </el-col>
          <!-- 打招呼 -->
          <el-col :span="10" :offset="19">
            <span>你好, {{ username }}</span>
          </el-col>
          <!-- 头像 -->
          <el-col :span="2" :offset="1">
            <el-avatar
                src="https://tse3-mm.cn.bing.net/th/id/OIP-C.1w4B8x7dI4cjN3LITLC7uwHaHZ?w=213&h=212&c=7&r=0&o=5&dpr=2&pid=1.7"></el-avatar>
          </el-col>
        </el-row>
      </el-header>
      <el-container>
        <!-- 侧边栏 -->
        <el-aside class='mainFrameAside' width='64px'>
          <el-menu default-active="6" class="main-frame-menu" :collapse="true">
            <!-- 个人面板弹框 -->
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-user-solid"></i>
                <span slot="title">个人面板</span>
              </template>
              <el-menu-item-group>
                <span slot="title"></span>
                <el-menu-item index="1-1" @click="toProfile">个人主页</el-menu-item>
                <el-menu-item index="1-2">页面设置</el-menu-item>
                <el-menu-item index="1-3">数据同步</el-menu-item>
                <el-menu-item index="1-4" @click="logOut">账号登出</el-menu-item>
              </el-menu-item-group>
            </el-submenu>

            <!-- 添加任务或者组别 -->
            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-plus"></i>
                <span slot="title">新建任务/组别</span>
              </template>
              <el-menu-item-group>
                <span slot="title">新建...</span>
                <el-menu-item index="2-1" @click='showTask'>任务</el-menu-item>
                <el-menu-item index="2-2" @click='showGroup'>组别</el-menu-item>
              </el-menu-item-group>
            </el-submenu>

            <!-- 展示个人任务 -->
            <el-menu-item index="3" @click='showPersonalTask'>
              <i class="el-icon-user"></i>
              <span slot="title">个人任务</span>
            </el-menu-item>

            <!-- 展示组别任务 -->
            <el-menu-item index="4" @click="showTeamInfo">
              <i class="el-icon-platform-eleme"></i>
              <span slot="title">组队任务</span>
            </el-menu-item>

            <!-- 通讯录 -->
            <el-menu-item index="5" @click="showAddressBook">
              <i class="el-icon-s-management"></i>
              <span slot="title">通讯录</span>
            </el-menu-item>

            <!-- 以日历视图展示任务 -->
            <el-menu-item index="6" @click="showCalendar">
              <i class="el-icon-date"></i>
              <span slot="title">日历视图</span>
            </el-menu-item>

            <!-- 搜索任务 -->
            <el-menu-item index="7" @click="showSearchTask">
              <i class="el-icon-search"></i>
              <span slot="title">任务过滤器</span>
            </el-menu-item>


            <!-- 消息推送 -->
            <el-menu-item index="8" @click="showMessage">
              <el-badge :value="12" class="item">
                <i class="el-icon-message"></i>
                <span slot="title">消息通知</span>
              </el-badge>
            </el-menu-item>
          </el-menu>

        </el-aside>
        <el-main class='main'>
          <!-- 添加task的表单 -->
          <el-dialog :visible.sync='addTaskShow' width='700px' :modal-append-to-body='false' @close='showCalendar'>
            <AddTaskForm
                :username='username'
                :tagArray='this.tagArray'
                :myTeamInfo='this.myTeamInfo'
                v-on:toCalendar='toCalendar($event)'></AddTaskForm>
          </el-dialog>
          <!-- 添加组别的表单 -->
          <el-dialog :visible.sync='addTeamShow' width='1000px' height='1000px' :modal-append-to-body='false'
                     @close='showCalendar'>
            <AddTeamForm
                v-on:toCalendar='toCalendar($event)'></AddTeamForm>
          </el-dialog>
          <!-- 个人任务页面 -->
          <PersonalTaskPage
              v-show="personalTaskShow"
              :taskData="this.taskData"
              :todayTaskData="this.todayTaskData"
              :weekTaskData="this.weekTaskData"
              :laterTaskData="this.laterTaskData"
              v-on:closeTaskDrawer="closeTaskDrawer($event)"></PersonalTaskPage>
          <!-- 组队任务页面 -->
          <TeamInfoPage v-show="teamInfoShow"
                        :teamInfo="this.teamInfo"
                        :username="this.username"
                        :Friends="this.Friends"
                        v-on:postTeamInfoAgain="postTeamInfoAgain($event)"
                        v-on:postMyTeamAgain="postMyTeamAgain($event)"></TeamInfoPage>
          <!-- 通讯录 -->
          <AddressBookPage v-show="addressBookShow" :Friends="this.Friends.length === 0 ? this.sampleFriends : this.Friends"></AddressBookPage>
          <!-- 日历视图 -->
          <div class='demo-app' v-show="calendarShow">
            <div class='demo-app-sidebar'>
              <div class='demo-app-sidebar-section'>
                <h2 class="demo-h2">Instructions</h2>
                <ul class="demo-ul">
                  <li class="demo-li">Select dates and you will be prompted to create a new event</li>
                  <li class="demo-li">Drag, drop, and resize events</li>
                  <li class="demo-li">Click an event to delete it</li>
                </ul>
              </div>
              <div class='demo-app-sidebar-section'>
                <label>
                  <input
                      type='checkbox'
                      :checked='calendarOptions.weekends'
                      @change='handleWeekendsToggle'
                  />
                  toggle weekends
                </label>
              </div>
              <div class='demo-app-sidebar-section'>
                <h2 class="demo-h2">All Events ({{ currentEvents.length }})</h2>
                <ul class="demo-ul">
                  <li class="demo-li" v-for='event in currentEvents' :key='event.id'>
                    <b class="demo-b">{{ event.startStr }}</b>
                    <i>{{ event.title }}</i>
                  </li>
                </ul>
              </div>
            </div>
            <div class='demo-app-main'>
              <FullCalendar ref="fullCalendar"
                            class='demo-app-calendar'
                            :options='calendarOptions'>
                <template v-slot:eventContent='arg'>
                  <b class="demo-b">{{ arg.timeText }}</b>
                  <i>{{ arg.event.title }}</i>
                </template>
              </FullCalendar>
            </div>
          </div>
          <!--弹出任务详细信息-->
          <el-dialog
              title="提示"
              :visible.sync="CalendarDialog"
              width="30%"
              :modal-append-to-body='false'
              center>
            <span>{{ this.CalendarClickTask }}</span>
            <span slot="footer" class="dialog-footer">
              <el-button @click="CalendarDialog = false">取 消</el-button>
              <el-button type="primary" @click="CalendarDialog = false">确 定</el-button>
            </span>
          </el-dialog>

          <!--弹出消息推送-->
          <el-drawer
              :title="this.username + ' --- Your notifications'"
              :visible.sync="MessageShow"
              direction="rtl"
              :before-close="handleMessageClose"
              :append-to-body='true'
              size="400px">
            <MessagePage></MessagePage>
          </el-drawer>

          <!-- 任务搜索 -->
          <SearchTaskPage v-show="searchTaskShow"></SearchTaskPage>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import PersonalTaskPage from './PersonalTaskPage.vue'
import TeamInfoPage from './TeamInfoPage.vue'
import AddTaskForm from './AddTaskForm.vue'
import AddTeamForm from './AddTeamForm.vue'
import AddressBookPage from './AddressBookPage.vue'
import SearchTaskPage from './SearchTaskPage.vue'
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import MessagePage from "./MessagePage";
import axios from 'axios'
import process from "_shelljs@0.7.8@shelljs";
axios.defaults.baseURL = process.env.API_ROOT
export default {
  name: "Main",
  components: {
    MessagePage,
    AddTaskForm,
    AddTeamForm,
    PersonalTaskPage,
    TeamInfoPage,
    AddressBookPage,
    SearchTaskPage,
    FullCalendar
  },
  props: ['username'],
  watch: {
    'addTaskShow': function () {
      if (this.addTaskShow) {
        this.postTags()
        this.postMyTeams()
      }
    },
    'addTeamShow': function () {
    },
    'personalTaskShow': function () {
      if (this.personalTaskShow) {
        this.postTaskData()
        this.postTodayTaskData()
        this.postWeekTaskData()
        this.postLaterTaskData()
      }
    },
    'teamInfoShow': function () {
      if (this.teamInfoShow) {
        this.postTeamInfo()
        this.postAddressBook()
      }
    },
    'addressBookShow': function () {
      if (this.addressBookShow) {
        this.postAddressBook()
      }
    },
    // TODO:监听，如果显示日历界面，获取日历数据,放入calendarTasks中
    calendarShow(newValue, oldValue) {
      if (newValue === true) {
        this.showCalendarData();
      }
    }
  },
  // 在载入页面前先获取日历数据
  mounted() {
    this.showCalendarData();
    this.connWebSocket();
  },
  beforeDestroy() {
    // 监听窗口关闭事件,vue生命周期销毁之前关闭socket当窗口关闭时，防止连接还没断开就关闭窗口。
    this.onbeforeunload();
  },
  data() {

    return {
      // 是否展示"个人任务"界面
      personalTaskShow: false,
      // 是否展示“组队任务”界面
      teamInfoShow: false,
      // 是否展示“添加任务”界面
      addTaskShow: false,
      // 是否展示“添加组别”界面
      addTeamShow: false,
      // 是否展示“通讯录”界面
      addressBookShow: false,
      // 是否展示“日历”界面
      calendarShow: true,
      // 是否展示“任务过滤器”界面
      searchTaskShow: false,
      // 获取当前时间，用于在日历中特殊显示
      MessageShow: false,
      calendarValue: new Date(),
      // 用于在日历上显示 +
      calendarDateOn: ' ',
      CalendarDialog: false,
      CalendarClickTask: null,
      taskForm: {
        taskName: '',
        tags: '',
        dueDate: '',
        privilege: '',
        type: '',
        subtasks: [],
        members: [],
        createDate: '',
        description: '',
        status: 0,
      },
      teamInfo: [{
        admins: [],
        createTime: '',
        creator: '',
        description: '',
        members: [],
        teamId: 0,
        teamName: '',
        teamTasks: []
      }],
      calendarOptions: {
        plugins: [
          dayGridPlugin,
          timeGridPlugin,
          interactionPlugin // needed for dateClick
        ],
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        initialView: 'dayGridMonth',
        events: this.calendarTasks, // alternatively, use the `events` setting to fetch from a feed
        editable: true,
        selectable: true,
        selectMirror: true,
        dayMaxEvents: true,
        weekends: true,
        select: this.handleDateSelect,
        eventClick: this.handleEventClick,
        eventsSet: this.handleEvents,
        eventChange: this.handleEventChange
        /* you can update a remote database when these fire:
        eventAdd:
        eventChange:
        eventRemove:
        */
      },
      currentEvents: [],
      // AddTaskForm
      tagArray: [],
      myTeamInfo: [],
      // PersonalTaskPage
      taskData: [],
      todayTaskData: [],
      weekTaskData: [],
      laterTaskData: [],
      //  AddressBook
      Friends: [{
        email: '',
        firstname: '',
        lastname: '',
        phone: '',
        userId: '',
        username: 'a'
      }],
      sampleFriends:[{
        email: '',
        firstname: '',
        lastname: '',
        phone: '',
        userId: '',
        username: ''
      }]
    }
  },
  methods: {
    toProfile(event) {
      this.$router.push({name: 'Profile', params: {username: this.username}});
    },
    toCalendar() {
      this.showCalendar()
    },
    showPersonalTask() {
      this.personalTaskShow = true
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showTeamInfo() {
      this.personalTaskShow = false
      this.teamInfoShow = true
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showTask() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = true
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showGroup() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = true
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showCalendar() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = true
      this.searchTaskShow = false
    },
    showAddressBook() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = true
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showSearchTask() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = true
    },
    showMessage() {
      this.MessageShow = true;
    },
    handleMessageClose(done) {
      done();
    },
    // 选择是否显示周末
    handleWeekendsToggle() {
      this.calendarOptions.weekends = !this.calendarOptions.weekends // update a property
    },
    // 显示添加任务
    handleDateSelect(selectInfo) {
      // let title = prompt('Please enter a new title for your event')
      let calendarApi = selectInfo.view.calendar

      calendarApi.unselect() // clear date selection
      //
      // if (title) {
      //   calendarApi.addEvent({
      //     id: createEventId(),
      //     title,
      //     start: selectInfo.startStr,
      //     end: selectInfo.endStr,
      //     allDay: selectInfo.allDay
      //   })
      // }
      this.showTask()
    },
    handleEventChange(event, delta, revertFunc) {
      // console.log(event)
      let taskId = parseInt(event.event.id);
      let start = event.event.start;
      let end = event.event.end;
      this.changeCalendarData(taskId, start, end);
      // this.$message({
      //   message: 'update success',
      //   type: 'success'
      // })
    },
    // 日历点击任务时出现详细信息
    handleEventClick(clickInfo) {
      // if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
      //   clickInfo.event.remove()
      // }
      // console.log(clickInfo.event.id)
      this.getTaskDetails(clickInfo.event.id)
      this.CalendarDialog = true
    },
    // 任务详细信息
    getTaskDetails(taskId) {
      for (let x in this.calendarTasks) {
        if (this.calendarTasks[x].taskId.toString() === taskId) {
          this.CalendarClickTask = this.calendarTasks[x];
          // console.log(this.CalendarClickTask)
          return;
        }
      }
    },
    changeCalendarData(taskId, start, end) {
      const that = this;
      axios.post(
          '/task/edittask',
          {
            taskId: taskId,
            createDate: start,
            dueDate: end
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            // console.log(response)
            if (response.data.code === 200) {
              that.$message({
                message: 'update success',
                type: 'success'
              });
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message({
                message: 'fetch error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            that.showCalendarData()
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    // axios get tasks
    showCalendarData() {
      const that = this;
      // console.log(window.sessionStorage.getItem('token'));
      axios.post(
          '/task/query',
          {
            // fliter
            scheduledTask: 0
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: '获取日历信息',
                type: 'success'
              });
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message({
                message: 'fetch error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            // console.log(response.data.data)
            that.calendarTasks = []
            let tmplist = []
            for (let x in response.data.data) {
              tmplist.push({
                id: response.data.data[x].taskId,
                title: response.data.data[x].taskName,
                start: response.data.data[x].createDate,
                end: response.data.data[x].dueDate
              })
              that.calendarTasks.push(response.data.data[x])
            }
            that.calendarOptions.events = tmplist;
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },

    handleEvents(events) {
      this.currentEvents = events
    },
    // AddTaskForm
    postTags() {
      let that = this
      axios.post(
          '/user/selecttags',
          {
            username: this.username
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '获取标签成功',
                type: 'success'
              })
              that.tagArray = []
              for (let i in response.data.data) {
                let content = response.data.data[i]
                let obj = {
                  label: content.tagName,
                  value: content.tagName
                }

                that.tagArray.push(obj)
              }
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取Tags数据失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应错误,获取Tags数据失败')
          }
      )
    },
    postMyTeams() {
      let that = this;
      axios.post(
          '/user/myteams/admin',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '请求用户创建或管理的组成功',
                type: 'success'
              })
              that.myTeamInfo = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求用户创建或管理的组失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应错误,请求用户创建或管理的组失败')
          }
      );
    },
    // PersonalTaskPage
    postTaskData() {
      let that = this
      axios.post(
          '/task/query',
          {
            createDate: null,
            dueDate: null,
            privilege: null,
            status: null,
            tag: null,
            taskName: null,
            teamName: null,
            type: null,
            scheduledTask: 0
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '请求“任务”数据成功',
                type: 'success'
              })
              that.taskData = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“任务”数据失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“任务”数据失败')
          }
      )
    },
    postTodayTaskData() {
      let that = this
      axios.post(
          '/task/query',
          {
            createDate: null,
            dueDate: null,
            privilege: null,
            status: null,
            tag: null,
            taskName: null,
            teamName: null,
            type: null,
            scheduledTask: 1,
            currentDate: new Date().toISOString(),
            timeRange: 0
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '请求“今天任务”数据成功',
                type: 'success'
              })
              that.todaytaskData = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“今天任务”数据失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“今天任务”数据失败')
          }
      )
    },
    postWeekTaskData() {
      let that = this
      axios.post(
          '/task/query',
          {
            createDate: null,
            dueDate: null,
            privilege: null,
            status: null,
            tag: null,
            taskName: null,
            teamName: null,
            type: null,
            scheduledTask: 1,
            currentDate: new Date().toISOString(),
            timeRange: 1
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '请求“一周内”数据成功',
                type: 'success'
              })
              that.weekTaskData = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“一周内”数据失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“一周内”数据失败')
          }
      )
    },
    postLaterTaskData() {
      let that = this
      axios.post(
          '/task/query',
          {
            createDate: null,
            dueDate: null,
            privilege: null,
            status: null,
            tag: null,
            taskName: null,
            teamName: null,
            type: null,
            currentDate: new Date().toISOString(),
            timeRange: 2
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '请求“稍后”数据成功',
                type: 'success'
              })
              that.laterTaskData = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“稍后”数据失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“稍后”数据失败')
          }
      )
    },
    // TeamInfoShow
    postTeamInfo() {
      let that = this
      axios.post(
          '/user/myteams',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '获取组队任务数据成功',
                type: 'success'
              })
              that.teamInfo = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取组队任务失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，获取组队任务失败')
          }
      )
    },
    // addressBookShow
    postAddressBook() {
      let that = this
      axios.post(
          '/user/addressbook',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '获取通讯录数据成功',
                type: 'success'
              })
              that.Friends = response.data.data
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取通讯录数据失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败,获取通讯录数据出错')
          }
      )
    },
    // 编辑或删除任务完，重新请求任务
    closeTaskDrawer() {
      this.postTaskData()
      this.postTodayTaskData()
      this.postWeekTaskData()
      this.postLaterTaskData()
    },
    postMyTeamAgain() {
      this.postMyTeams()
    },
    logOut() {
      const that = this
      axios.post(
          '/user/logout',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: '登出成功',
                type: 'success'
              })
              that.$router.push({name: 'Login'})
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('登出失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，登出失败')
          }
      )
    },
    postTeamInfoAgain() {
      this.postTeamInfo()
    },


    // websocket
    connWebSocket() {
      // let userInfo = JSON.parse(sessionStorage.getItem("userInfos"));
      // let userId = userInfo.userId;
      // WebSocket
      if ("WebSocket" in window) {
        this.websocket = new WebSocket(
            "ws://localhost:8081/messagepush/" + this.username //userId 传此id主要后端java用来保存session信息，用于给特定的人发送消息，广播类消息可以不用此参数
        );
        //初始化socket
        this.initWebSocket();
      } else {
        alert("浏览器不支持websocket");
      }
    },

    initWebSocket() {
      // 连接错误
      this.websocket.onerror = this.setErrorMessage;

      // 连接成功
      this.websocket.onopen = this.setOnopenMessage;

      // 收到消息的回调
      this.websocket.onmessage = this.setOnmessageMessage;

      // 连接关闭的回调
      this.websocket.onclose = this.setOncloseMessage;

      // 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = this.onbeforeunload;
    },
    setErrorMessage() {
      console.log(
          "WebSocket连接发生错误   状态码：" + this.websocket.readyState
      );
      this.reconnect();
    },
    setOnopenMessage() {
      console.log("WebSocket连接成功    状态码：" + this.websocket.readyState);
      this.start()
    },
    setOnmessageMessage(result) {
      this.reset();
      console.log("服务端返回：" + result.data);
      let msgMap = JSON.parse(result.data);
      let id = msgMap.id;
      let title = msgMap.title;
      let type = msgMap.type;
      // 根据服务器推送的消息做自己的业务处理

      this.$notify({
        title: "你有一条新信息",
        type: "info",
        duration: 0,
        dangerouslyUseHTMLString: true,
        message:
            '<div style="height:100px;width:100px">' +
            title,
        position: "bottom-right"
      });
    },
    setOncloseMessage() {
      console.log("WebSocket连接关闭    状态码：" + this.websocket.readyState);
      this.reconnect();
    },
    onbeforeunload() {
      this.closeWebSocket();
    },
    closeWebSocket() {
      this.websocket.close();
    },
// heartCheck
    reconnect() {//重新连接
      let that = this;
      if (that.lockReconnect) {
        return;
      }
      that.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      that.timeoutnum && clearTimeout(that.timeoutnum);
      that.timeoutnum = setTimeout(function () {
        //新连接
        that.initWebSocket();
        that.lockReconnect = false;
      }, 5000);
    },
    reset() {//重置心跳
      let that = this;
      //清除时间
      clearTimeout(that.timeoutObj);
      clearTimeout(that.serverTimeoutObj);
      //重启心跳
      that.start();
    },
    start() {//开启心跳
      let self = this;
      self.timeoutObj && clearTimeout(self.timeoutObj);
      self.serverTimeoutObj && clearTimeout(self.serverTimeoutObj);
      self.timeoutObj = setTimeout(function () {
        //这里发送一个心跳，后端收到后，返回一个心跳消息，
        if (self.websocket.readyState === 1) {//如果连接正常
          self.websocket.send("{heartCheck: 1}");
        } else {//否则重连
          self.reconnect();
        }
        self.serverTimeoutObj = setTimeout(function () {
          //超时关闭
          self.websocket.close();
        }, self.timeout);
      }, self.timeout)
    },
  },
}
</script>

<style scoped>
.mainFrame {
  width: 100%;
  height: 100%;
  position: fixed;
  padding: 0%;
  margin: 0%;
}

.taskManager {
  font-weight: bold;
  font-size: 20px;
}

.mainFrameAside {
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.main {
  padding: 0%;
}

.main-frame-menu {
  height: 900px;
}

.mainFrameHeader {
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.demo-h2 {
  margin: 0;
  font-size: 16px;
}

.demo-ul {
  margin: 0;
  padding: 0 0 0 1.5em;
}

.demo-li {
  margin: 1.5em 0;
  padding: 0;
}

.demo-b { /* used for event dates/times */
  margin-right: 3px;
}

.demo-app {
  display: flex;
  min-height: 100%;
  font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
  font-size: 14px;
}

.demo-app-sidebar {
  width: 300px;
  line-height: 1.5;
  background: #eaf9ff;
  border-right: 1px solid #d3e2e8;
}

.demo-app-sidebar-section {
  padding: 2em;
}

.demo-app-main {
  flex-grow: 1;
  padding: 3em;
}

.fc { /* the calendar root */
  max-width: 1100px;
  margin: 0 auto;
}

.item {
  margin-top: 10px;
  margin-right: 40px;
}

</style>
