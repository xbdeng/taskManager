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
                </el-menu>
            </el-main>
            <!-- 显示任务信息 -->
            <el-drawer 
            title="查看或编辑任务"
            :visible.sync="drawer"
            direction="rtl"
            :before-close="handleClose"
            :modal-append-to-body='false'
            size='50%'
            >
                <TaskShow :singleTaskData="getTask(chosenTaskId)" v-on:closeTaskDrawer='closeDrawer($event)'></TaskShow>
            </el-drawer>
        </el-container>
    </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import axios from 'axios'
export default {
  name: "PersonalTaskPage",
  components: {
    TaskTree,
    TaskShow
  },
  props:['taskData','todayTaskData','weekTaskData','laterTaskData'],
  data() {
    return {
        chosenTaskId:'-1',
        taskShow:true,
        planedTaskShow:false,
        Specifier:0,
        drawer:false,
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
    setSpecifier(num) {
        this.Specifier = num
    },
    closeTaskDrawer() {
        this.drawer = false
    }
}

}
</script>

<style scoped>

</style>
