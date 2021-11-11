<template>
<!-- 这个页面是个人主页面 -->
  <div class='mainFrame'>
     <el-container>
       <!-- 侧边栏 -->
       <el-aside class = 'mainFrameAside' width='64px'>
         <el-menu default-active="6" class="main-frame-menu"  :collapse="true" >
           <!-- 个人面板弹框 -->
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-user-solid"></i>
              <span slot="title">个人面板</span>`
            </template>
            <el-menu-item-group>
              <span slot="title"></span>
              <el-menu-item index="1-1" @click="toProfile">个人主页</el-menu-item>
              <el-menu-item index="1-2" >任务统计</el-menu-item>
              <el-menu-item index="1-3" >页面设置</el-menu-item>
              <el-menu-item index="1-4" >数据同步</el-menu-item>
              <el-menu-item index="1-5" >账号登出</el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <!-- 添加任务或者组别 -->
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-plus"></i>
              <span slot="title">添加任务/组别</span>
            </template>
            <el-menu-item-group>
              <span slot="title">添加...</span>
              <el-menu-item index="2-1" @click='showTask'>任务</el-menu-item>
              <el-menu-item index="2-2" @click='showGroup'>组别</el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <!-- 展示个人任务 -->
          <el-menu-item index="3" @click='postPersonalTasks'>
            <i class="el-icon-user"></i>
            <span slot="title">个人任务</span>
          </el-menu-item>

          <!-- 展示组别任务 -->
          <el-menu-item index="4" @click="postGroupInfo">
            <i class="el-icon-platform-eleme"></i>
            <span slot="title">组队任务</span>
          </el-menu-item>

          <!-- 通讯录 -->
          <el-menu-item index="5" @click="postAddressBook">
            <i class="el-icon-s-management"></i>
            <span slot="title">通讯录</span>
          </el-menu-item>

          <!-- 以日历视图展示任务 -->
          <el-menu-item index="6" @click="postCalendar">
            <i class="el-icon-date"></i>
            <span slot="title">日历视图</span>
          </el-menu-item>

          <!-- 搜索任务 -->
          <el-menu-item index="7" @click="showSearchTask">
            <i class="el-icon-search"></i>
            <span slot="title">任务搜索</span>
          </el-menu-item>
        </el-menu>
       </el-aside>
       <el-main class='main'>
         <!-- 添加task的表单 -->
         <el-dialog :visible.sync='addTaskShow'  width='700px' :modal-append-to-body='false'>
            <AddTaskForm  v-on:taskFormData='addTask($event)'></AddTaskForm>
          </el-dialog>
          <!-- 添加组别的表单 -->
          <el-dialog :visible.sync='addGroupShow' width='1000px' height='1000px' :modal-append-to-body='false'>
            <AddGroupForm v-on:groupFormData='addGroup($event)'></AddGroupForm>
          </el-dialog>
          <!-- 个人任务页面 -->
          <PersonalTaskPage v-show="personalTaskShow" :todayTasks="this.todayTasks" :recentSevenDaysTasks="this.recentSevenDaysTasks"></PersonalTaskPage>
          <!-- 组队任务页面 -->
          <GroupInfoPage v-show="groupInfoShow" :groupInfo="this.groupInfo"></GroupInfoPage>
          <!-- 通讯录 -->
          <AddressBookPage v-show="addressBookShow" :friends="this.friends"></AddressBookPage>
          <!-- 日历视图 -->
          <el-calendar v-show='calendarShow'>
            <template slot="dateCell" slot-scope="{data}">
                {{ data.day.split('-').slice(1).join('-') }}
                  <div v-for="(task,index) in formCalendarData(data)" :key="index">
                    <el-popover placement="top-start" trigger="hover" :title="task[0]" :content="task[1]">
                      <el-link slot='reference'>{{ task[0] }}</el-link>
                    </el-popover>
                  </div>
            </template>
          </el-calendar>
          <!-- 任务搜索 -->
          <SearchTaskPage v-show="searchTaskShow"></SearchTaskPage>
       </el-main>
     </el-container>
  </div>
</template>

<script>
import PersonalTaskPage from './PersonalTaskPage.vue'
import GroupInfoPage from './GroupInfoPage.vue'
import AddTaskForm from './AddTaskForm.vue'
import AddGroupForm from './AddGroupForm.vue'
import AddressBookPage from './AddressBookPage.vue'
import SearchTaskPage from './SearchTaskPage.vue'
import axios from 'axios'
export default {
  name: "Main",
  components:{AddTaskForm, AddGroupForm, PersonalTaskPage, GroupInfoPage, AddressBookPage, SearchTaskPage},
  props: ['username'],
  data() {

    return {
      personalTaskShow:false,
      groupInfoShow:false,
      addTaskShow:false,
      addGroupShow:false,
      addressBookShow:false,
      calendarShow:true,
      searchTaskShow:false,
      calendarValue:new Date(),
      taskForm:{
          taskName:'',
          taskTags: '',
          taskDDL:'',
          taskPriority:'',
          taskType: '',
          taskStartTime:'',
          taskDescription:''
      },
      // 后端返回的日历数据
      calendarTasks:[
        {
          taskName:'OOAD',
          taskDDL:'11-05',
          taskDescription:'An OOAD Project'
        },
        {
          taskName:'AI Project',
          taskDDL:'11-27',
          taskDescription:'An AI Project'
        },
        {
          taskName:'Complier',
          taskDDL:'11-30',
          taskDescription:'Design a Complier'
        },
        {
          taskName:'Computer Network',
          taskDDL:'11-30',
          taskDescription:'Midterm Exam'
        },
        {
          taskName:'PAT',
          taskDDL:'11-30',
          taskDescription:'PAT Context'
        },
        {
          taskName:'CCF',
          taskDDL:'11-30',
          taskDescription:'CCF Context'
        }
      ],
      // 后端返回的组队任务数据
      groupInfo:[
            {
                groupName:'菜鸡学生组',
                groupMembers:[
                    {
                        memberName:'八云紫',
                        memberRole:'Master'
                    },
                    {
                        memberName:'八云蓝',
                        memberRole:'Manager'
                    },
                    {
                        memberName:'橙',
                        memberRole:'none'
                    }


                ],
                groupTasks:[
                    {
                        taskName:'Database',
                        taskDescriptions:'Very busy'
                    },
                    {
                        taskName:'Computer Architecture',
                        taskDescriptions:'Easy',
                        subTasks:[
                            {
                                taskName:'ALU',
                                taskDescriptions:'ababaab',
                            },
                            {
                                taskName:'Controller',
                                taskDescriptions:'hhh'
                            }
                        ]
                    }
                ]
            },
            {
                groupName:'菜鸡教师组',
                groupMembers:[
                    {
                        memberName:'古明地觉',
                        memberRole:'Master'
                    },
                    {
                        memberName:'古明地恋',
                        memberRole:'Manager'
                    },
                    {
                        memberName:'Olen',
                        memberRole:'none'
                    },
                    {
                        memberName:'灵乌路空',
                        memberRole:'none'
                    }

                ],
                groupTasks:[
                    {
                        taskName:'AI Project',
                        taskDescriptions:'aaaaaaaaaa'
                    },
                    {
                        taskName:'OS Project',
                        taskDescriptions:'A little hard',
                        subTasks:[
                            {
                                taskName:'fork function',
                                taskDescriptions:'easiest'
                            }
                        ]
                    }
                ]
            },
      ],
      // 后端返回的通讯录数据
      friends:[
              {
                  username:'Lee',
                  email:'11913003@mail.sustech.edu.cn',
                  telephone:'19861251006',
                  personalSign:'Eclipse first, the rest nowhere'
              },
              {
                  username:'Mike',
                  email:'chlijiaao@163.com',
                  telephone:'10086',
                  personalSign:"躺平不香吗?"
              },
              {
                  username:'Smith',
                  email:'2381446488@qq.com',
                  telephone:'120',
                  personalSign:"我是个傻逼"
              },
              {
                  username:'Jack',
                  email:'ababa@mail.sustech.edu.cn',
                  telephone:'119',
                  personalSign:"哥只是个传说"
              }
      ],
      // 后端返回的今日任务数据
      todayTasks:[
        {
            taskName:'OOAD',
            taskDescriptions:'nnnnnn',
            subTasks:[
                {
                    taskName:'jjj',
                    taskDescriptions:'6666'
                }
            ]
        },
        {
            taskName:'Principle of Compiler',
            taskDescriptions:'82371'
        }
      ],
      // 后端返回的最近七天任务数据
      recentSevenDaysTasks:[
          {
              taskName:'OOAD',
              taskDescriptions:'0000'
          },
          {
              taskName:'Principle of Complier',
              taskDescriptions:'1111'
          },
          {
              taskName:'AI Project',
              taskDescriptions:'999'
          }
      ]

    }
  },
  methods:{
    // 跳转到个人主页
    toProfile(event) {
      this.$router.push({name: 'Profile',params:{username:this.username}});
    },
    showPersonalTask() {
      this.personalTaskShow = true
      this.groupInfoShow = false
      this.addTaskShow = false
      this.addGroupShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showGroupInfo() {
      this.personalTaskShow = false
      this.groupInfoShow = true
      this.addTaskShow = false
      this.addGroupShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showTask() {
      this.personalTaskShow = false
      this.groupInfoShow = false
      this.addTaskShow = true
      this.addGroupShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false

    },
    showGroup() {
      this.personalTaskShow = false
      this.groupInfoShow = false
      this.addTaskShow = false
      this.addGroupShow = true
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showCalendar() {
      this.personalTaskShow = false
      this.groupInfoShow = false
      this.addTaskShow = false
      this.addGroupShow = false
      this.addressBookShow = false
      this.calendarShow = true
      this.searchTaskShow = false
    },
    showAddressBook() {
      this.personalTaskShow = false
      this.groupInfoShow = false
      this.addTaskShow = false
      this.addGroupShow = false
      this.addressBookShow = true
      this.calendarShow = false
      this.searchTaskShow = false
    },
    showSearchTask() {
      this.personalTaskShow = false
      this.groupInfoShow = false
      this.addTaskShow = false
      this.addGroupShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = true
    },
    // 将任务名称展示在日历中
    formCalendarData(data) {
      let currentDate = data.day.split('-').slice(1).join('-')
      let calendarData = []
      this.calendarTasks.forEach(
        function (task) {
          if (currentDate === task.taskDDL) {
            let list = []
            list.push(task.taskName)
            list.push(task.taskDescription)
            calendarData.push(list)
          }
        }
      )
      return calendarData
    },
    addTask(newTask) {
      const that = this
      // 向后端发送创建的任务数据
      axios.post(
        'http://localhost:8081/',
        {
          taskName:newTask.taskName,
          taskTags:newTask.taskTags,
          taskDDL:newTask.taskDDL,
          taskPriority:newTask.taskPriority,
          taskType:newTask.taskType,
          taskGroups:newTask.taskGroups,
          taskStartTime:newTask.taskStartTime,
          taskDescription:newTask.taskDescription
        }
      ).then(
        function(response) {
          that.$message(
            {
              message:'创建任务成功',
              type:'success'
            }

          );
        },
        function(err) {
          that.$message.error('创建任务失败')
        }
      )
    },
    addGroup(newGroup) {
      const that = this
      // 向后端发送创建的组别数据
      axios.post(
        'http://localhost:8081/',
        {
          groupName:newGroup.groupName,
          groupMembers:newGroup.groupMembers
        }
      ).then(
        function(response) {
          that.$message({
            message:'创建组别成功',
            type:'success'
          })
        },
        function(err) {
          that.$message.error('创建组别失败')
        }
      )
    },
    // 向后端请求个人任务信息
    postPersonalTasks() {
      const that = this
      alert('请求个人任务信息')
      this.showPersonalTask()
      axios.post(
        'http://localhost:8081/api/user/personaltasks',
        {
          username:this.username
        }
      ).then(
        function(response) {
          that.todayTasks = response.data.todayTasks
          that.recentSevenDaysTasks = response.data.recentSevenDaysTasks
        },
        function(err) {
          that.$message.error('请求个人任务失败')
        }
      )
    },
    // 向后端请求组队任务信息
    postGroupInfo() {
      const that = this
      alert('请求组队任务信息')
      this.showGroupInfo()
      axios.post(
        'http://localhost:8081/api/user/mygroups',
        {
          username:this.username
        }
      ).then(
        function(response) {
          that.groupInfo = response.data
        },
        function(err) {
          that.$message.error('获取组队任务信息失败')
        }
      )
    },
    // 向后端请求通讯录数据
    postAddressBook() {
      const that = this
      alert('请求通讯录数据')
      this.showAddressBook()
      axios.post(
        'http://localhost:8081/api/user/addressbook',
        {
          username:this.username
        }
      ).then(
        function(response) {
          that.friends = response.data
        },
        function(err) {
          that.$message.error('获取通讯录数据失败')
        }
      )
    },
    // 向后端请求日历数据
    postCalendar() {
      const that = this
      alert('请求日历数据')
      this.showCalendar()
      axios.post(
        'http://localhost:8081/api/user/calendar',
        {
          username:this.username
        }
      ).then(
        function(response) {
          that.calendarTasks = response.data
        },
        function(err) {
          that.$message.error('获取日历数据失败')
        }
      )
    }
  }
}
</script>

<style scoped>

.mainFrame{
  width: 100%;
  height:100%;
  position:fixed;
  padding: 0%;
  margin: 0%;
}

.main{
  padding: 0%;
}
.main-frame-menu{
  height: 900px;
}

</style>
