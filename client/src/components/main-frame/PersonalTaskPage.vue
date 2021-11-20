<template>
<!-- 这个页面用来显示个人任务数据 -->
    <div class='personalTask'>
        <el-container>
            <!-- 侧边栏，显示 “任务” , “计划内任务” -->
            <el-aside>
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <!-- 显示侧边栏中对应的"任务"的数据 -->
                    <el-menu-item index="1" @click="showTask">
                        <template slot="title">
                            <span slot="title">任务</span>
                        </template>
                    </el-menu-item>
                    <!-- 显示侧边栏中对应的“计划内任务”的数据 -->
                    <el-menu-item index="2" @click="showPlanedTask">
                        <template slot="title">
                            <span slot="title">计划内任务</span>
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <!-- 显示今天,一周内，稍后,任务的侧边栏 -->
            <el-main>
                <el-menu :default-openeds="['today']" >
                    <TaskTree
                    v-show="taskShow"
                    :taskData="this.taskData" 
                    :taskLevel="''" 
                    :chosenTask="chosenTaskId" 
                    v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                    <el-submenu index='today' v-show="planedTaskShow">
                        <template slot="title">
                            <span slot="title" @click="setSpecifier(0)">今天</span>
                        </template>
                        <TaskTree 
                        :taskData="this.todayTaskData"
                        :taskLevel="''"
                        :chosenTask="chosenTaskId"
                        v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                    </el-submenu>
                    <el-submenu index='week' v-show="planedTaskShow">
                        <template slot="title">
                            <span slot="title" @click="setSpecifier(1)">一周内</span>
                        </template>
                        <TaskTree 
                        :taskData="this.weekTaskData" 
                        :taskLevel="''" 
                        :chosenTask="chosenTaskId"
                        v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                    </el-submenu>
                    <el-submenu index='later' v-show="planedTaskShow">
                        <template slot="title">
                            <span slot="title" @click="setSpecifier(2)">稍后</span>
                        </template>
                        <TaskTree 
                        :taskData="this.laterTaskData" 
                        :taskLevel="''" 
                        :chosenTask="chosenTaskId" 
                        v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                        </el-submenu>
                    <el-input v-model="addedTaskName" placeholder="请输入要添加的任务的名称" @keyup.enter.native="addTask"></el-input>
                </el-menu>
            </el-main>
            <!-- 显示任务信息 -->
            <el-drawer 
            title="查看或编辑任务"
            :visible.sync="drawer"
            direction="rtl"
            :before-close="handleClose"
            :modal-append-to-body='false'
            size='30%'>
                <TaskShow
                    :singleTaskData="getTask(chosenTaskId)"
                    v-on:closeTaskDrawer='closeTaskDrawer($event)'
                    v-on:emitTreeData="emitTreeData($event)"></TaskShow>
            </el-drawer>

            <el-drawer
            title="查看任务树形图"
            :visible.sync="treeDrawer"
            direction="ltr"
            :before-close="handleTreeClose"
            :modal-append-to-body='false'
            size='50%'>
                <TreeTask
                :TData="this.treeData"
                v-on:closeTreeDrawer="handleTreeClose"></TreeTask>
            </el-drawer>
        </el-container>
    </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import TreeTask from './TreeTask'
import axios from 'axios'
import process from "_shelljs@0.7.8@shelljs";
axios.defaults.baseURL = process.env.API_ROOT
export default {
  name: "PersonalTaskPage",
  components: {
    TaskTree,
    TaskShow,
    TreeTask
  },
  props:['taskData','todayTaskData','weekTaskData','laterTaskData'],
  data() {
    return {
        chosenTaskId:'-1',
        taskShow:true,
        planedTaskShow:false,
        Specifier:0,
        drawer:false,
        addedTaskName:'',
        treeDrawer:false,
        treeData:null,
    }
  },
  methods: {
    showTask() {
        this.taskShow = true
        this.planedTaskShow = false
    },
    showPlanedTask() {
        this.taskShow = false
        this.planedTaskShow = true
    },
    chooseTasks(id) {
          this.chosenTaskId = id;
          this.drawer = true
    },
    getTaskById(taskList, id) {
        if (id === '-1') return {
            taskName:'Please choose your task'
        }
        if (parseInt(id[0]) >= taskList.length) return {
            taskName:'Please choose your task'
        }
        if (id.length === 1) return taskList[parseInt(id)];
        return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
    },
    getTask(id)
    {
        switch(this.Specifier) {
            case 0:
                return this.getTaskById(this.taskShow ? this.taskData : this.todayTaskData, id)
            case 1:
                return this.getTaskById(this.taskShow ? this.taskData : this.weekTaskData, id)
            case 2:
                return this.getTaskById(this.taskShow ? this.taskData : this.laterTaskData,  id)
            default:
                break;
        }
        return {};
    },
    handleClose() {
        this.drawer = false
    },
    handleTreeClose() {
      this.treeDrawer = false
    },
    setSpecifier(num) {
        this.Specifier = num
    },
    closeTaskDrawer() {
        this.$emit('closeTaskDrawer', {})
        this.drawer = false
    },
    addTask() {
      const that = this
      if(this.addedTaskName === '') {
        that.$message.error('要添加的任务名不能为空')
        return ;
      }
      axios.post(
          '/task/addtask',
          {
            createDate:null,
            description:null,
            dueDate:null,
            //fatherTask
            //members
            privilege:0,
            status:0,
            // subtasks
            tags:null,
            taskName:that.addedTaskName,
            teamId:null,
            type:0,
            // username
          },
          {
            headers:{
              Authorization:window.localStorage.getItem('token')
            }
          }
      ).then(
          function(response) {
            if(response.data.code === 200) {
              that.$message({
                message:'添加任务成功',
                type:'success'
              })
              that.addedTaskName = null
              let newToken = response.headers.authorization
              if(newToken != null) window.localStorage.setItem('token', newToken)
            } else {
              that.$message.error('添加任务失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.localStorage.setItem('token', newToken)
            }
          },
          function(err) {
            that.$message.error('响应失败，添加任务失败')
          }
      )
    },
    emitTreeData(task) {
      this.treeDrawer = true
      this.treeData = task
    }

  }

}
</script>

<style scoped>

</style>
