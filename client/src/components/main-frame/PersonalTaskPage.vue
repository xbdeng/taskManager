<template>
<!-- 这个页面用来显示个人任务数据 -->
    <div class='personalTask'>
        <el-container>
            <!-- 侧边栏，显示 “今天” 或者是 “最近七天” -->
            <el-aside>
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <!-- 今天 -->
                    <el-menu-item index="1" @click="showToday">
                        <template slot="title">
                            <i class="el-icon-apple"></i>
                            <span slot="title">今天</span>
                        </template>
                    </el-menu-item>

                    <!-- 最近七天 -->
                    <el-menu-item index="2" @click="showRecentSevenDays">
                        <template slot="title">
                            <i class="el-icon-watermelon"></i>
                            <span slot="title">最近七天</span>
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>

            <!-- 显示今天任务的侧边栏 -->
            <el-aside v-show="today">
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <TaskTree :taskData="todayTasks" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                </el-menu>
            </el-aside>


            <!-- 显示最近七天任务的侧边栏 -->
            <el-aside v-show="recentSevenDays">
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <TaskTree :taskData="recentSevenDaysTasks" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                </el-menu>
            </el-aside>

            <!-- 显示任务信息 -->
            <el-main>
                <TaskShow :singleTaskData="today ? getTaskById(todayTasks, chosenTaskId) : getTaskById(recentSevenDaysTasks, chosenTaskId)"></TaskShow>
            </el-main>
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
  props:['todayTasks', 'recentSevenDaysTasks'],
  data() {
        
        return {
            // 用户点击的任务的key
            chosenTaskId:'-1',
            // 是否显示今天的数据
            today:false,
            // 是否显示最近七天的数据
            recentSevenDays:false,
        }
  },
  methods: {
      // 用户在侧边栏中点击今天后，显示今天的任务数据
    showToday() {
        this.today = true
        this.recentSevenDays = false
    },
    // 同上
    showRecentSevenDays() {
        this.today = false
        this.recentSevenDays = true
    },
    // 响应TaskTree中传上来的id
    chooseTasks(id) {
          this.chosenTaskId = id;
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
    }
}

}
</script>

<style scoped>

</style>
