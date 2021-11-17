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
                <el-menu-item index="1-4">账号登出</el-menu-item>
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
          </el-menu>
        </el-aside>
        <el-main class='main'>
          <!-- 添加task的表单 -->
          <el-dialog :visible.sync='addTaskShow' width='700px' :modal-append-to-body='false' @close='showCalendar'>
            <AddTaskForm v-on:taskFormData='this.addTask($event)' :username='username'
                         v-on:toCalendar='toCalendar($event)'></AddTaskForm>
          </el-dialog>
          <!-- 添加组别的表单 -->
          <el-dialog :visible.sync='addTeamShow' width='1000px' height='1000px' :modal-append-to-body='false'
                     @close='showCalendar'>
            <AddTeamForm v-on:teamFormData='addTeam($event)' v-on:toCalendar='toCalendar($event)'></AddTeamForm>
          </el-dialog>
          <!-- 个人任务页面 -->
          <PersonalTaskPage v-show="personalTaskShow"></PersonalTaskPage>
          <!-- 组队任务页面 -->
          <TeamInfoPage v-show="teamInfoShow" :teamInfo="this.teamInfo"></TeamInfoPage>
          <!-- 通讯录 -->
          <AddressBookPage v-show="addressBookShow" :Friends="this.Friends"></AddressBookPage>
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
              <FullCalendar
                  class='demo-app-calendar'
                  :options='calendarOptions'
              >
                <template v-slot:eventContent='arg'>
                  <b class="demo-b">{{ arg.timeText }}</b>
                  <i>{{ arg.event.title }}</i>
                </template>
              </FullCalendar>
            </div>
          </div>
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
import axios from 'axios'
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import {INITIAL_EVENTS, createEventId} from './event-utils'

export default {
  name: "Main",
  components: {
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
    // TODO:监听，如果显示组队任务界面，获取该成员加入的所有组放入teamInfo中
    'teamInfoShow': {},
    // TODO:监听，如果显示通讯录界面，获取该成员的所有好友放入Friends中
    'addressBookShow': {},
    // TODO:监听，如果显示日历界面，获取日历数据,放入calendarTasks中
    'calendarShow': {}
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
      calendarValue: new Date(),
      // 用于在日历上显示 +
      calendarDateOn: ' ',
      taskForm: {
        // 任务名
        taskName: '',
        // 标签
        tags: '',
        // 截止时间
        dueDate: '',
        // 优先级
        privilege: '',
        // 任务类型：0是个人，1是组队
        type: '',
        // 子任务，对象类型数组
        subtasks: [],
        // 如果是组队任务，涉及的成员，对象类型的数组
        members: [],
        // 任务开始时间
        createDate: '',
        // 任务描述信息
        description: '',
        // 任务状态，0是未完成，1是已完成，2是已过期
        status: 0,
      },
      // // 后端返回的日历数据
      // calendarTasks: [],
      // 后端返回的组队任务数据
      teamInfo: [],
      // 后端返回的通讯录数据
      Friends: [],
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
        initialEvents: INITIAL_EVENTS, // alternatively, use the `events` setting to fetch from a feed
        editable: true,
        selectable: true,
        selectMirror: true,
        dayMaxEvents: true,
        weekends: true,
        select: this.handleDateSelect,
        eventClick: this.handleEventClick,
        eventsSet: this.handleEvents
        /* you can update a remote database when these fire:
        eventAdd:
        eventChange:
        eventRemove:
        */
      },
      currentEvents: []
    }
  },
  methods: {
    // 跳转到个人主页
    toProfile(event) {
      this.$router.push({name: 'Profile', params: {username: this.username}});
    },
    // 跳转到日历界面
    toCalendar() {
      this.showCalendar()
    },
    // 个人任务
    showPersonalTask() {
      this.personalTaskShow = true
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    // 组队任务
    showTeamInfo() {
      this.personalTaskShow = false
      this.teamInfoShow = true
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    // 添加任务
    showTask() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = true
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    // 添加组
    showGroup() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = true
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    // 日历
    showCalendar() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = true
      this.searchTaskShow = false
    },
    // 通讯录
    showAddressBook() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = true
      this.calendarShow = false
      this.searchTaskShow = false
    },
    // 任务过滤器
    showSearchTask() {
      this.personalTaskShow = false
      this.teamInfoShow = false
      this.addTaskShow = false
      this.addTeamShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = true
    },
    handleWeekendsToggle() {
      this.calendarOptions.weekends = !this.calendarOptions.weekends // update a property
    },

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

    handleEventClick(clickInfo) {
      if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
        clickInfo.event.remove()
      }
    },

    handleEvents(events) {
      this.currentEvents = events
    }
  },
  // 响应添加任务的表单，调用接口添加任务
  addTask(newTask) {
    const that = this
    alert(11)
    // 向后端发送创建的任务数据
    axios.post(
        'http://localhost:8081/api/task/addtask',
        newTask
    ).then(
        function (response) {
          that.$message(
              {
                message: '创建任务成功',
                type: 'success'
              }
          );
        },
        function (err) {
          that.$message.error('创建任务失败')
        }
    )
  },
  // 响应添加组的表单，调用接口添加组
  addTeam(newTeam) {
    const that = this
    // 向后端发送创建的组别数据
    axios.post(
        'http://localhost:8081/api/team/createTeam',
        newTeam
    ).then(
        function (response) {
          that.$message({
            message: '创建组别成功',
            type: 'success'
          })
        },
        function (err) {
          that.$message.error('创建组别失败')
        }
    )
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

</style>
