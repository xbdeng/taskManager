<template>
<!-- 这个页面用于显示组队任务 -->
    <div class='teamInfo'>
        <el-container>
            <!-- 侧边栏，用于显示所有组的信息 -->
            <el-aside>
                <el-menu  class="main-frame-menu">
                    <!-- 显示所有的组 -->
                    <el-menu-item  v-for='(team, teamIndex) in teamInfo' :key="teamIndex" :index= team.teamName >
                        <template slot="title">
                            <span slot="title" @click="showSelectedTeam(teamIndex)">{{ team.teamName }}</span>
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <!-- 侧边栏，用于显示这个组的所有任务 -->
            <el-main>
                <el-menu>
                    <TaskTree :taskData="teamInfo[this.selectedTeam].teamTasks" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                </el-menu>
            </el-main>

            <!-- 用于显示组内任务的抽屉组件 -->
            <el-drawer 
            title="查看或编辑组内信息"
            :visible.sync="teamInfoDrawer"
            direction="ltr"
            :before-close="handleTeamInfoClose"
            :modal-append-to-body='false'
            size='50%'>
                <TeamShow :singleTeamData="teamInfo[selectedTeam]" :drawer="teamInfoDrawer"></TeamShow>
            </el-drawer>
            <!-- 用于显示任务信息 -->
            <el-drawer 
            title="查看或编辑任务"
            :visible.sync="taskInfoDrawer"
            direction="rtl"
            :before-close="handleTaskInfoClose"
            :modal-append-to-body='false'
            size='50%'>
                <TaskShow :singleTaskData="getTaskById(teamInfo[this.selectedTeam].teamTasks, chosenTaskId)" v-on:editTeam='editTeam'></TaskShow>
            </el-drawer>

            <!-- 用于显示组的信息 -->

        </el-container>
    </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import TeamShow from '../sub-components/TeamShow.vue'
export default {

  name: "TeamInfoPage",
  components: {
    TaskTree,
    TaskShow,
    TeamShow
  },
  props:['teamInfo'],
  data() {

    return {
        // 唯一标识任务的key
        chosenTaskId:'-1',
        // 用户点击的组的编号
        selectedTeam:0,
        taskInfoDrawer:false,
        teamInfoDrawer:false
    }
  },
  methods: {
    // 用于根据用户的点击，显示特定的组别
    showSelectedTeam(teamId) {
        this.teamInfoDrawer = true
        this.selectedTeam = teamId
    },
    // 接收子组件传递的任务的key
    chooseTasks(id) {
          this.taskInfoDrawer = true
          this.chosenTaskId = id;
    },
    // 根据任务数组和子组件的key找到任务对象并返回
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
    handleTaskInfoClose() {
        this.taskInfoDrawer = false
    },
    handleTeamInfoClose() {
        this.teamInfoDrawer = false
    },
    editTeam(newTeam) {
        // TODO:调用后端接口，实现修改组的信息
    }
}

}
</script>

<style scoped>

</style>
