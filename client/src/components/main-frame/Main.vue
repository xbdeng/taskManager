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
              <el-menu-item index="2-1" @click='addTask'>任务</el-menu-item>
              <el-menu-item index="2-2" @click='addGroup'>组别</el-menu-item>
            </el-menu-item-group>
          </el-submenu>

          <!-- 展示个人任务 -->
          <el-menu-item index="3" @click='showPersonalTask'>
            <i class="el-icon-user"></i>
            <span slot="title">个人任务</span>
          </el-menu-item>

          <!-- 展示组别任务 -->
          <el-menu-item index="4" @click="showGroupInfo">
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
            <span slot="title">任务搜索</span>
          </el-menu-item>
        </el-menu>
       </el-aside>
       <el-main class='main'>
         <!-- 添加task的表单 -->
         <el-dialog :visible.sync='addTaskShow'  width='700px' :modal-append-to-body='false'>
            <AddTaskForm  v-on:taskFormData='addTask'></AddTaskForm>
          </el-dialog>
          <!-- 添加组别的表单 -->
          <el-dialog :visible.sync='addGroupShow' width='1000px' height='1000px' :modal-append-to-body='false'>
            <AddGroupForm v-on:groupFormData='addGroup'></AddGroupForm>
          </el-dialog>
          <!-- 个人任务页面 -->
          <PersonalTaskPage v-show="personalTaskShow"></PersonalTaskPage>
          <!-- 组队任务页面 -->
          <GroupInfoPage v-show="groupInfoShow"></GroupInfoPage>
          <!-- 通讯录 -->
          <AddressBookPage v-show="addressBookShow"></AddressBookPage>
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
