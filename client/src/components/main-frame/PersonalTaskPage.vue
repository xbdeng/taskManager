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
                <!-- 默认展开"今天" -->
                <el-menu :default-openeds="['today']">
                    <el-submenu index='today'>
                        <template slot="title">
                            <span slot="title">今天</span>
                        </template>
                        <TaskTree :taskData="this.todayTaskData" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                    </el-submenu>
                    <el-submenu index='week'>
                        <template slot="title">
                            <span slot="title">一周内</span>
                        </template>
                        <TaskTree :taskData="this.weekTaskData" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                    </el-submenu>
                    <el-submenu index='later'>
                        <template slot="title">
                            <span slot="title">稍后</span>
                        </template>
                        <TaskTree :taskData="this.laterTaskData" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
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
                <TaskShow :singleTaskData="getTask(chosenTaskId)" :drawer="drawer" v-on:editTask='editTask'></TaskShow>
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
  watch:{
      // 一旦要显示“任务”对应的数据，向后端发送请求
      'taskShow':{

      },
    //   一旦要显示“计划内任务”对应的数据，向后端发送请求
    // 同上
      'planedTaskShow': {

      }
  },
  data() {
        
        return {
            // 用户点击的任务的key
            chosenTaskId:'-1',
            // 是否显示"任务"对应的数据
            taskShow:true,
            // 是否显示"计划内任务"对应的数据
            planedTaskShow:false,
            // 0 显示今天，1 显示一周内，2显示稍后
            Specifier:0,
            // 任务信息显示与编辑界面
            drawer:false,
            // 今天的任务数据
            todayTaskData:[],
            // 一周内的任务数据
            weekTaskData:[],
            // “稍后”的任务数据
            laterTaskData:[],
        }
  },
  methods: {
      // 用户在侧边栏中点击今天后，显示今天的任务数据
    showTask() {
        this.taskShow = true
        this.planedTaskShow = false
    },
    // 同上
    showPlanedTask() {
        this.taskShow = false
        this.planedTaskShow = true
    },
    // 响应TaskTree中传上来的id
    chooseTasks(id) {
          this.chosenTaskId = id;
          this.drawer = true
    },
    // 根据任务数组taskList和id找到对应的任务对象并返回
    getTaskById(taskList, id) {
        if (id === '-1') return {
            taskName:'Please choose your task'
        }
        if (parseInt(id[0]) >= taskList.length) return {
            taskName:'Please choose your task'
        }
        if (id.length == 1) return taskList[parseInt(id)];
        return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
    },
    // 通过用户选中的任务的id，结合Specifier,确定显示哪一个任务的信息
    getTask(id)
    {
        switch(this.Specifier) {
            // 选择“今天”的数据
            case 0:
                return this.getTaskById(this.todayTaskData, id)
            // 选择“一周内”的数据
            case 1:
                return this.getTaskById(this.weekTaskData, id)
            // 选择“稍后”的数据
            case 2:
                return this.getTaskById(this.laterTaskData, id)
            default:
                break;
        }
        return {};
    },
    // 关闭展示任务信息的抽屉
    handleClose() {
        this.drawer = false
    },
    // 进行任务修改,task是子组件TaskShow提交上来的修改后的任务数据
    editTask(task) {
        // TODO:向后端请求修改任务
        
    }
}

}
</script>

<style scoped>

</style>
