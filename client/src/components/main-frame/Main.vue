<template>
  <div class='mainFrame'>
     <el-container>
       <el-aside class = 'mainFrameAside' width='64px'>
         <el-menu default-active="6" class="main-frame-menu"  :collapse="true" >
           <!-- Profile -->
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

          <!-- Add Tasks or Groups -->
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-plus"></i>
              <span slot="title">添加任务/组别</span>
            </template>
            <el-menu-item-group>
              <span slot="title">添加...</span>
              <el-menu-item index="2-1" @click='addTask'>任务</el-menu-item>
              <el-menu-item index="2-2" @click='addGroup'>组别</el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <!-- Show Personal Task -->
          <el-menu-item index="3" @click='showPersonalTask'>
            <i class="el-icon-user"></i>
            <span slot="title">个人任务</span>
          </el-menu-item>

          <!-- Show Group Task -->
          <el-menu-item index="4" @click="showGroupInfo">
            <i class="el-icon-platform-eleme"></i>
            <span slot="title">组队任务</span>
          </el-menu-item>

          <!-- Address Book -->
          <el-menu-item index="5" @click="showAddressBook">
            <i class="el-icon-s-management"></i>
            <span slot="title">通讯录</span>
          </el-menu-item>

          <!-- View Tasks in Calendar View mode -->
          <el-menu-item index="6" @click="showCalendar">
            <i class="el-icon-date"></i>
            <span slot="title">日历视图</span>
          </el-menu-item>

          <!-- Search Tasks according to taskName -->
          <el-menu-item index="7" @click="showSearchTask">
            <i class="el-icon-search"></i>
            <span slot="title">任务搜索</span>
          </el-menu-item>
        </el-menu>
       </el-aside>
       <el-main class='main'>
         <!-- Add Tasks -->
         <el-dialog :visible.sync='addTaskShow'  width='700px' :modal-append-to-body='false'>
            <AddTaskForm  v-on:taskFormData='addTask'></AddTaskForm>
          </el-dialog>
          <!-- Add Groups -->
          <el-dialog :visible.sync='addGroupShow' width='1000px' height='1000px' :modal-append-to-body='false'>
            <AddGroupForm v-on:groupFormData='addGroup'></AddGroupForm>
          </el-dialog>
          <!-- Personal Task -->
          <PersonalTaskPage v-show="personalTaskShow"></PersonalTaskPage>
          <!-- Group Info -->
          <GroupInfoPage v-show="groupInfoShow"></GroupInfoPage>
          <!-- Address Book -->
          <AddressBookPage v-show="addressBookShow"></AddressBookPage>
          <!-- Calendar View -->
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
          <!-- Search Task View -->
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
      ]
    }
  },
  methods:{
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
    addTask() {
      this.personalTaskShow = false
      this.groupInfoShow = false
      this.addTaskShow = true
      this.addGroupShow = false
      this.addressBookShow = false
      this.calendarShow = false
      this.searchTaskShow = false
    },
    addGroup() {
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
