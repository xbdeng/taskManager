<template>
<!-- 这个页面用来显示个人任务数据 -->
    <div class='personalTask'>
        <el-container>
            <!-- 侧边栏，显示 “任务” 或者是 “计划内任务” -->
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

            <!-- 显示今天,一周内，稍后任务的侧边栏 -->
            <el-main>
                <!-- 默认展开今天的任务数据 -->
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
export default {

  name: "PersonalTaskPage",
  components: {
    TaskTree,
    TaskShow
  },
  data() {
        
        return {
            // 用户点击的任务的key
            chosenTaskId:'-1',
            // 是否显示"任务"对应的数据
            taskShow:true,
            // 是否显示"计划内任务"对应的数据
            planedTaskShow:false,
            // 显示“今天”，“一周内”，还是“稍后”的数据
            // 0 显示今天，1 显示一周内，2显示稍后
            Specifier:0,
            // 任务信息显示与编辑界面
            drawer:false,
            // 经过任务过滤器筛选后，后端返回的今天的任务数据
            todayTaskData:[
                {
                    taskName:'任务一',
                    taskTags:['学习','工作'],
                    taskStartTime:'',
                    taskDDL:'结束时间',
                    taskPriority:0,
                    taskType:0,
                    subTasks:[],
                    taskStartTime:'开始时间',
                    taskDescriptions:'任务一的描述信息',
                    hasFinished:true,
                    hasExpired:false

                },
                {
                    taskName:'任务二',
                    taskTags:['学习','工作'],
                    taskStartTime:'',
                    taskDDL:'结束时间',
                    taskPriority:1,
                    taskType:0,
                    subTasks:[],
                    taskStartTime:'开始时间',
                    taskDescriptions:'任务二的描述信息',
                    hasFinished:false,
                    hasExpired:true
                },
                {
                    taskName:'任务三',
                    taskTags:['学习','工作'],
                    taskStartTime:'',
                    taskDDL:'',
                    taskPriority:2,
                    taskType:1,
                    members:[{name:111},{name:222},{name:333}],
                    subTasks:[],
                    taskDescriptions:'任务三的描述信息',
                    hasFinished:false,
                    hasExpired:false
                },
                {
                    taskName:'任务四',
                    taskTags:['学习','工作'],
                    taskStartTime:'',
                    taskDDL:'',
                    taskPriority:3,
                    taskType:0,
                    subTasks:[],
                    taskDescriptions:'任务四的描述信息',
                    hasFinished:false,
                    hasExpired:false
                }
            ],
            // 经过任务过滤器筛选后，后端返回的一周内的任务数据
            weekTaskData:[
            ],
            // 经过任务过滤器过滤后，后端返回的“稍后”的任务数据
            laterTaskData:[
                {
                    taskName:'OOAD',
                    taskTags:['学习','工作'],
                    taskStartTime:'',
                    taskDDL:'',
                    taskPriority:3,
                    taskType:0,
                    subTasks:[],
                    taskDescriptions:'OOAD的描述信息',
                    hasFinished:false,
                    hasExpired:false
                }
            ],
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
    // 向后端请求今天的任务数据
    postTodayTask() {
        // 如果最左边侧边栏选择的是“任务”
        if(this.taskShow) {
            // TODO:
        }
        // 如果最左边侧边栏选择的是“计划内任务”
        else if(this.planedTaskShow) {
            // TODO:
        }
    },
    // 向后端请求一周内的任务数据
    postWeekTask() {
        // 如果最左边侧边栏选择的是“任务”
        if(this.taskShow) {
            // TODO:
        }
        // 如果最左边侧边栏选择的是“计划内任务”
        else if(this.planedTaskShow) {
            // TODO:
        }
    },
    // 向后端请求稍后时间的任务数据
    postLaterTask() {
        // 如果最左边侧边栏选择的是“任务”
        if(this.taskShow) {
            // TODO:
        }
        // 如果最左边侧边栏选择的是“计划内任务”
        else if(this.planedTaskShow) {
            // TODO:
        }
    },
    // 通过用户选中的任务的id，结合Specifier,确定显示哪一个人物的信息
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
    handleClose() {
        this.drawer = false
    },
    // 进行任务修改
    editTask(task) {
        
    }
}

}
</script>

<style scoped>

</style>
