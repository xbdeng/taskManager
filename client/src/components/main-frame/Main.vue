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
                <el-menu-item index="1-3" @click="handleOutlook">数据同步</el-menu-item>
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
              <el-badge :value="showMessageNote" class="item">
                <i class="el-icon-message"></i>
                <span slot="title">消息通知</span>
              </el-badge>
            </el-menu-item>

            <el-menu-item index="9">
              <i class="el-icon-circle-check" v-if="!this.offline"></i>
              <i class="el-icon-warning-outline" v-if="this.offline"></i>
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
              :username="this.username"
              :taskData="this.taskData"
              :todayTaskData="this.todayTaskData"
              :weekTaskData="this.weekTaskData"
              :laterTaskData="this.laterTaskData"
              :transData="this.transData"
              v-on:closeTaskDrawer="closeTaskDrawer($event)"
              v-on:postPersonalTaskAgain="postPersonalTaskAgain($event)"></PersonalTaskPage>
          <!-- 组队任务页面 -->
          <TeamInfoPage v-show="teamInfoShow"
                        :teamInfo="this.teamInfo"
                        :username="this.username"
                        :Friends="this.Friends"
                        v-on:postTeamInfoAgain="postTeamInfoAgain($event)"
                        v-on:postMyTeamAgain="postMyTeamAgain($event)"
                        v-on:postPersonalTaskAgain='postPersonalTaskAgain($event)'></TeamInfoPage>
          <!-- 通讯录 -->
          <AddressBookPage v-show="addressBookShow"
                           :Friends="this.Friends" v-on:updateAddressBook="postAddressBook"></AddressBookPage>
          <!-- 日历视图 -->
          <div class='demo-app' v-show="calendarShow">
            <div class='demo-app-sidebar'>
              <div class='demo-app-sidebar-section'>
                <h2 class="demo-h2">Instructions</h2>
                <ul class="demo-ul">
                  <li class="demo-li">Select dates and you will be prompted to create a new event</li>
                  <li class="demo-li">Drag, drop, and resize events</li>
                  <li class="demo-li">Click an event to get detailed information</li>
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
              title="任务详细信息"
              :visible.sync="CalendarDialog"
              width="30%"
              :modal-append-to-body='false'
              center>
            <!--            <span>{{ this.CalendarClickTask }}</span>-->
            <el-form v-if="this.CalendarClickTask !== null">
              <el-form-item label="任务名称">
                <span>{{ this.CalendarClickTask.taskName }}</span>
              </el-form-item>
              <el-form-item label="描述">
                <span>{{ this.CalendarClickTask.description }}</span>
              </el-form-item>
              <el-form-item label="开始时间">
                <span>{{ this.CalendarClickTask.createDate }}</span>
              </el-form-item>
              <el-form-item label="截止时间">
                <span>{{ this.CalendarClickTask.dueDate }}</span>
              </el-form-item>
              <el-form-item label="标识">
                <span v-for="i in this.CalendarClickTask.tags"> {{ i }} </span>
                <!--                <span>{{ this.CalendarClickTask.tags }}</span>-->
              </el-form-item>
              <el-form-item>

              </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
              <el-button @click="CalendarDialog = false">取 消</el-button>
              <el-button type="primary" @click="CalendarDialog = false">确 定</el-button>
            </span>
          </el-dialog>

          <!--弹出消息推送-->
          <el-drawer
              :visible.sync="MessageShow"
              direction="rtl"
              :before-close="handleMessageClose"
              :append-to-body='true'
              size="440px">
            <div slot="title"><i class="el-icon-message-solid" style="font-size: 30px"></i><span
                style="font-size: 30px; text-align: center">Your Notification</span></div>
            <MessagePage :message-show="MessageShow"></MessagePage>
          </el-drawer>

          <!-- 任务搜索 -->
          <SearchTaskPage
              v-show="searchTaskShow"
              v-on:postPersonalTaskAgain='postPersonalTaskAgain($event)'></SearchTaskPage>
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
import websocket, {heartCheck} from "../sub-components/WebSocket";
import Push from 'push.js'
import {removeRequest} from "../sub-components/cache";
import {Notification} from "_element-ui@2.15.6@element-ui";

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
    //监听：是否显示添加任务的表单
    //显示的话，请求用户标签数据，用户创建或管理的组
    'addTaskShow': function () {
      if (this.addTaskShow) {
        this.postTags()
        this.postMyTeams()
      }
    },
    //监听：是否显示“个人任务”
    //如果显示，请求“任务”，计划内任务的“今天”，“一周内”，“稍后”
    'personalTaskShow': function () {
      if (this.personalTaskShow) {
        this.postTaskData()
        this.postTodayTaskData()
        this.postWeekTaskData()
        this.postLaterTaskData()
      }
    },
    //监听，是否显示“组队任务”
    //如果显示，请求:用户加入的组，以及用户的所有好友
    'teamInfoShow': function () {
      if (this.teamInfoShow) {
        this.postTeamInfo()
        this.postAddressBook()
      }
    },
    //监听：是否显示：“通讯录”
    //如果显示，请求：用户的好友数据
    'addressBookShow': function () {
      if (this.addressBookShow) {
        this.postAddressBook()
      }
    },
    calendarShow(newValue, oldValue) {
      if (newValue === true) {
        this.showCalendarData();
      }
    }
  },
  // 在载入页面前先获取日历数据
  mounted() {
    this.showCalendarData();
    // websocket.setReconnectVar(true)
    // websocket.Init(this.username);
    // this.eventMsg();
    this.Initwebscoket()
  },
  created() {
    Push.Permission.request();
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
      //用户加入的所有组
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
      // 传递给子组件的标签数据
      tagArray: [],
      //传递给子组件的用户创建或管理的组的数据
      myTeamInfo: [],
      // "任务"
      taskData: [],
      //"今天"
      todayTaskData: [],
      //"一周内"
      weekTaskData: [],
      //"稍后"
      laterTaskData: [],
      //通讯录数据
      Friends: [{
        email: '',
        firstname: '',
        lastname: '',
        phone: '',
        userId: '',
        username: 'a'
      }],
      timeout: 5000,
      //如果Friends为空，传递给子组件时会报错，防止报错定义一个sampleFriends
      sampleFriends: [{
        sampleFriends: [{
          email: '',
          firstname: '',
          lastname: '',
          phone: '',
          userId: '',
          username: ''
        }]
      }],
      transData: [],
      showMessageNote: '',
      offline: false
    }
  },
  methods: {
    Initwebscoket() {
      let that = this
      websocket.setInit(that.username)
      websocket.getWebSocket().onerror = function (e) {
        console.log("数据传输发生错误");
        if (websocket.getBroken() === false) {
          Notification({
            title: '警告',
            message: '数据传输发生错误',
            type: 'warning'
          });
        }
        that.offline = true
        websocket.setBroken(true)
        that.reconnect(that.username)
      }
      websocket.getWebSocket().onopen = function () {
        console.log("连接成功")
        Notification({
          title: '成功',
          message: '连接成功',
          type: 'success'
        });
        that.offline = false
        websocket.setBroken(false)
        heartCheck.start();
      }

      websocket.getWebSocket().onclose = function () {
        console.log("连接已关闭")
        if (websocket.getBroken() === false) {
          Notification({
            title: '警告',
            message: '连接已关闭',
            type: 'warning'
          });
        }
        that.offline = true
        websocket.setBroken(true)
        if (websocket.getReconnectVar() === true) {
          that.reconnect(that.username);
        }
      }


      websocket.getWebSocket().onmessage = function (res) {
        that.offline = false
        //处理接收的时间逻辑
        // console.log(res)
        heartCheck.start()
        websocket.setBroken(false)
        let tmp = JSON.parse(res.data)
        // console.log(res)
        removeRequest()
        if (tmp.heartCheck === 1) {
          return
        } else {
          if (tmp.type === 0) {
            that.$notify.info({
              title: tmp.from,
              message: '想要与你一起组队成为' + tmp.groupName
            });
            that.showMessageNote = 'new'
            that.pushMessage(tmp.from, '想要与你一起组队成为' + tmp.groupName);
          }
          if (tmp.type === 1) {
            that.$notify.info({
              title: tmp.from,
              message: '想要与你成为好友'
            });
            that.showMessageNote = 'new'
            that.pushMessage(tmp.from, '想要与你成为好友');
          }
          if (tmp.type === 2) {
            that.$notify.info({
              title: tmp.from,
              message: '邀请你加入' + tmp.groupName
            });
            that.showMessageNote = 'new'
            that.pushMessage(tmp.from, '邀请你加入' + tmp.groupName);
          }
        }
      }
    },
    reconnect(sname) {
      const that = this
      if (websocket.getlockReconnect()) {
        return;
      }
      websocket.setlockReconnect(true)
      //没连接上会一直重连，设置延迟避免请求过多
      websocket.gettt() && clearTimeout(websocket.gettt());
      websocket.settt(setTimeout(function () {
        websocket.getWebSocket().close()
        console.log("执行断线重连...")
        // ws = new WebSocket(url + sname);
        that.Initwebscoket()
        websocket.setlockReconnect(false)
      }, 5000))
    },
    // eventMsg() {
    //   let that = this;
    //     if ("WebSocket" in window) {
    //       ws = new WebSocket(url + clientId);
    //     }
    //
    //   websocket.getWebSocket().onerror = function (e) {
    //     console.log("数据传输发生错误");
    //     if (broken === false) {
    //       Notification({
    //         title: '警告',
    //         message: '数据传输发生错误',
    //         type: 'warning'
    //       });
    //     }
    //     websocket.setBroken(true)
    //     reconnect(clientId)
    //   },
    //
    //   websocket.getWebSocket().onopen = function () {
    //     console.log("连接成功")
    //     Notification({
    //       title: '成功',
    //       message: '连接成功',
    //       type: 'success'
    //     });
    //     broken = false
    //     heartCheck.start();
    //   },
    //
    //   websocket.getWebSocket().onclose = function () {
    //     console.log("连接已关闭")
    //     if (broken === false) {
    //       Notification({
    //         title: '警告',
    //         message: '连接已关闭',
    //         type: 'warning'
    //       });
    //     }
    //     broken = true
    //     if (reconnectvar === true) {
    //       reconnect(clientId);
    //     }
    //   }
    //
    //
    //   websocket.getWebSocket().onmessage = function (res) {
    //     //处理接收的时间逻辑
    //     // console.log(res)
    //     heartCheck.start()
    //     websocket.setBroken(false)
    //     let tmp = JSON.parse(res.data)
    //     // console.log(res)
    //     removeRequest()
    //     if (tmp.heartCheck === 1) {
    //       return
    //     } else {
    //       if (tmp.type === 0) {
    //         that.$notify.info({
    //           title: tmp.from,
    //           message: '想要与你一起组队成为' + tmp.groupName
    //         });
    //         that.pushMessage(tmp.from, '想要与你一起组队成为' + tmp.groupName);
    //       }
    //       if (tmp.type === 1) {
    //         that.$notify.info({
    //           title: tmp.from,
    //           message: '想要与你成为好友'
    //         });
    //         that.pushMessage(tmp.from, '想要与你成为好友');
    //       }
    //       if (tmp.type === 2) {
    //         that.$notify.info({
    //           title: tmp.from,
    //           message: '邀请你加入' + tmp.groupName
    //         });
    //         that.pushMessage(tmp.from, '邀请你加入' + tmp.groupName);
    //       }
    //     }
    //   }
    // },

    //跳转到个人主页
    toProfile(event) {
      this.$router.push({name: 'Profile', params: {username: this.username}});
    },
    //接收子组件跳转回日历界面的请求
    toCalendar() {
      this.showCalendar()
    },
    // 控制页面显示
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
      this.showMessageNote = '';
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message({
                message: 'fetch error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message({
                message: 'fetch error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
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
    // 请求用户所有的标签
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取Tags数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应错误,获取Tags数据失败')
          }
      )
    },
    //请求用户创建或者管理的组
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求用户创建或管理的组失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应错误,请求用户创建或管理的组失败')
          }
      );
    },
    // 请求“任务”数据
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
              // that.transferData(that.taskData)
              that.transData = that.transferData(that.taskData)
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“任务”数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“任务”数据失败')
          }
      )
    },
    transferData(task) {
      let transferList = []
      for (let i in task) {
        let tmp = {
          id: task[i].taskId,
          parent_id: task[i].fatherTask,
          order: 0,
          name: task[i].taskName,
          lists: this.transferData(task[i].subTasks),
        }
        transferList.push(tmp)
      }
      return transferList
    },
    //请求“今天”
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“今天任务”数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“今天任务”数据失败')
          }
      )
    },
    //“一周内”
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“一周内”数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“一周内”数据失败')
          }
      )
    },
    //"稍后"
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
            scheduledTask: 1,
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求“稍后”数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，请求“稍后”数据失败')
          }
      )
    },
    // 请求用户加入的组的数据
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取组队任务失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，获取组队任务失败')
          }
      )
    },
    // 请求用户的好友数据
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
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取通讯录数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败,获取通讯录数据出错')
          }
      )
    },
    // 重新请求“任务”，“今天”，“一周内”，“稍后”
    closeTaskDrawer() {
      this.postTaskData()
      this.postTodayTaskData()
      this.postWeekTaskData()
      this.postLaterTaskData()
    },
    //重新请求：用户创建或管理的组
    postMyTeamAgain() {
      this.postMyTeams()
    },
    //重新请求"任务"
    postPersonalTaskAgain() {
      this.postTaskData()
      this.postTodayTaskData()
      this.postWeekTaskData()
      this.postLaterTaskData()
    },
    //登出操作
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

              websocket.setReconnectVar(false)
              websocket.getWebSocket().close()
              that.$router.push({name: 'login'})
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)

            } else {
              that.$message.error('登出失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，登出失败')
          }
      )
    },
    //重新请求：用户加入的组
    postTeamInfoAgain() {
      this.postTeamInfo()
    },
    pushMessage(header, message) {
      Push.create(header, {
        body: message,
        requireInteraction: true,
        //icon: '/icon.png',
        timeout: 60000,
      });
    },
    handleOutlook(event) {
      //github登录授权页面
      let oauth_uri = 'https://login.microsoftonline.com/common/oauth2/v2.0/authorize'
      //github中获取
      let client_id = '70f26831-fd49-4b56-a707-3a1ba1ae66d6'
      //授权回调地址
      let redirect_uri = 'http://localhost:8080/outlook/login'
      let scope = 'https://graph.microsoft.com/Calendars.ReadWrite'
      window.location.href = `${oauth_uri}?client_id=${client_id}&redirect_uri=${redirect_uri}&scope=${scope}&response_type=code`
    }
  }
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
  height: 100vh;
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
