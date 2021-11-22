<template>
<!-- 这个页面用于显示组队任务 -->
    <div class='teamInfo'>
        <el-container>
            <!-- 侧边栏，用于显示所有组的信息 -->
            <el-aside>
                <el-menu  class="main-frame-menu">
                    <!-- 显示所有的组 -->
                    <el-menu-item  v-for='(team, teamIndex) in this.teamInfo' :key=team.teamId :index= team.teamName >
                            <span @click="showSelectedTeam(teamIndex)">{{ team.teamName }}</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <!-- 侧边栏，用于显示这个组的所有任务 -->
            <el-main>
                <el-menu>
                    <TaskTree 
                    :taskData="generateTaskTreeData()"
                    :taskLevel="''"
                    :chosenTask="chosenTaskId"
                    v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
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
                <TeamShow
                :singleTeamData="this.teamInfo === 0 ? this.teamSample: teamInfo[selectedTeam]"
                :username="this.username"
                :Friends="this.Friends"
                v-on:closeTeamDrawer="closeTeamDrawer($event)"
                v-on:postTeamInfoAgain="postTeamInfoAgain($event)"></TeamShow>
            </el-drawer>
            <!-- 用于显示任务信息 -->
            <el-drawer
            title="查看或编辑任务"
            :visible.sync="taskInfoDrawer"
            direction="rtl"
            :before-close="handleTaskInfoClose"
            :modal-append-to-body='false'
            size='50%'>
                <TaskShow
                :singleTaskData="teamInfo.length === 0 ? this.taskSample :getTaskById(teamInfo[this.selectedTeam].teamTasks, chosenTaskId)"
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
                v-on:closeTreeDrawer="handleTreeClose"
                v-on:postTreeTaskAgain="postTreeTaskAgain"></TreeTask>
            </el-drawer>
            <!-- 用于显示组的信息 -->

        </el-container>
    </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import TeamShow from '../sub-components/TeamShow.vue'
import TreeTask from './TreeTask'
export default {

  name: "TeamInfoPage",
  components: {
    TaskTree,
    TaskShow,
    TeamShow,
    TreeTask
  },
  props:['teamInfo','username','Friends'],
  data() {
    return {
        // 唯一标识任务的key
        chosenTaskId:'-1',
        // 用户点击的组的编号
        selectedTeam:0,
        taskInfoDrawer:false,
        teamInfoDrawer:false,
        treeDrawer:false,
        treeData:null,
        taskSample:{
          createDate:'',
          description:'',
          dueDate:'',
          members:[],
          privilege:0,
          status:0,
          subTasks:[],
          tags:[],
          taskName:'',
          type:0
        },
        teamSample:{
          teamName:'',
          createDate:'',
          members:[],
          description:''
        }
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
        if (id.length === 1) return taskList[parseInt(id)];
        return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
    },
    handleTaskInfoClose() {
        this.taskInfoDrawer = false
    },
    handleTeamInfoClose() {
        this.teamInfoDrawer = false
    },
    handleTreeClose() {
      this.treeDrawer = false
    },
    closeTaskDrawer() {
        this.$emit('postTeamInfoAgain', {})
        this.taskInfoDrawer = false
    },
    closeTeamDrawer() {
        this.teamInfoDrawer = false
    },
    postTeamInfoAgain() {
      this.selectedTeam = 0
      this.$emit('postTeamInfoAgain',{})
    },
    emitTreeData(task) {
      this.treeDrawer = true
      this.treeData = task
      console.log(task)
    },
    generateTaskTreeData() {
      if(this.teamInfo.length !== 0)
        return this.teamInfo[this.selectedTeam].teamTasks
      else return []
    },
    postTreeTaskAgain() {
      this.$emit('postTeamInfoAgain',{})
    }
}

}
</script>

<style scoped>

</style>
