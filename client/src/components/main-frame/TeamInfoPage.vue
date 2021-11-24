<template>
  <!-- 这个页面用于显示组队任务 -->
  <div class='teamInfo'>
    <el-container>
      <!-- 侧边栏，用于显示所有组的信息 -->
      <el-aside style="height: 100vh">
        <el-button icon="el-icon-search" circle @click="searchTeam = true"></el-button>
        <el-menu class="main-frame-menu" :default-openeds="['1']">
          <!-- 显示所有的组 -->
          <el-submenu index="1">
            <template slot="title">
              <i class="iconfont el-icon-githubteam"></i>
              <span slot="title" class="teamNameCSS">我加入的组</span>
            </template>
            <el-menu-item-group>
              <el-menu-item
                  v-for='(team, teamIndex) in this.teamInfo'
                  :key=team.teamId
                  :index=team.teamName
                  @click.native="showSelectedTeam2(teamIndex)">
                <span @click="showSelectedTeam(teamIndex)" class="teamNameCSS">{{ team.teamName }}</span>
              </el-menu-item>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 侧边栏，用于显示这个组的所有任务 -->
      <el-main>
          <DragTaskTree
              v-show="!(generateTaskTreeData() === [])"
              :taskData="generateTaskTreeData()"
              v-on:taskIdChanged="chooseTasks($event)"
              v-on:postTaskDataAgain="postTeamAgain($event)"></DragTaskTree>
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
          size='40%'>
        <TaskShow
            :singleTaskData="teamInfo.length === 0 ? this.taskSample :getTaskById(teamInfo[this.selectedTeam].teamTasks, chosenTaskId)"
            :singleTeamData="this.teamInfo === 0 ? this.teamSample: teamInfo[selectedTeam]"
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

      <el-drawer
          title="Team Search Page"
          :visible.sync="searchTeam"
          direction="rtl"
          :before-close="handleTeamClose"
          :modal-append-to-body='false'
          size="25%">
        <div>
          <el-form>
            <el-form-item>
              <el-row>
                <el-col :span="15">
                  <el-input v-model="teamSearchName"></el-input>
                </el-col>
                <el-col :span="9">
                  <el-button type="success" @click="searchTeamB">搜索群组消息</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item v-for="(data, index) in searchTeamList" :key="index">
              <el-card class="box-card">
                <el-form-item>
                  <el-row :gutter="20">
                    <el-col :span="15">
                      {{data.teamName}}
                    </el-col>
                    <el-col :span="9">
                      <el-button type="primary" plain @click="requestTeam(data.teamId)">申请加入</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  Creator Name:{{data.creatorName}}
                  <br>
                  Description:{{data.description}}
                </el-form-item>
              </el-card>
            </el-form-item>
          </el-form>
        </div>
      </el-drawer>

    </el-container>
  </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import TeamShow from '../sub-components/TeamShow.vue'
import TreeTask from './TreeTask'
import DragTaskTree from "../sub-components/DragTaskTree";
import axios from "axios";

export default {

  name: "TeamInfoPage",
  components: {
    TaskTree,
    TaskShow,
    TeamShow,
    TreeTask,
    DragTaskTree
  },
  props: ['teamInfo', 'username', 'Friends'],
  data() {
    return {
      // 唯一标识任务的key
      chosenTaskId: '-1',
      // 用户点击的组的编号
      selectedTeam: 0,
      taskInfoDrawer: false,
      teamInfoDrawer: false,
      treeDrawer: false,
      treeData: null,
      taskSample: {
        createDate: '',
        description: '',
        dueDate: '',
        members: [],
        privilege: 0,
        status: 0,
        subTasks: [],
        tags: [],
        taskName: '',
        type: 0
      },
      teamSample: {
        teamName: '',
        createDate: '',
        members: [],
        description: ''
      },
      searchTeam: false,
      teamSearchName: '',
      searchTeamList: []
    }
  },
  methods: {
    // 用于根据用户的点击，显示特定的组别
    showSelectedTeam(teamId) {
      this.teamInfoDrawer = true
      this.selectedTeam = teamId
    },
    showSelectedTeam2(teamId) {
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
        taskName: 'Please choose your task'
      }
      if (parseInt(id[0]) >= taskList.length) return {
        taskName: 'Please choose your task'
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
      this.$emit('postTeamInfoAgain', {})
    },
    emitTreeData(task) {
      this.treeDrawer = true
      this.treeData = task
      console.log(task)
    },
    generateTaskTreeData() {
      if (this.teamInfo.length !== 0)
        return this.teamInfo[this.selectedTeam].teamTasks
      else return []
    },
    postTreeTaskAgain() {
      this.$emit('postTeamInfoAgain', {})
    },
    handleTeamClose() {
      this.searchTeam = false
    },
    searchTeamB(event) {
      const that = this
      axios.post(
          '/team/queryteam',
          {
            teamName: that.teamSearchName
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            // console.log(response)
            if (response.data.code === 200) {
              that.$message({
                message: 'search success',
                type: 'success'
              })
              that.searchTeamList = response.data.data
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message({
                message: 'search error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    requestTeam(TeamId) {
      const that = this
      axios.post(
          'http://localhost:8081/api/message/sendrequest',
          {
            type: 2,
            teamId: TeamId
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'request success',
                type: 'success'
              })
            }else if(response.data.code === 201){
              that.$message({
                message: 'request failed, you are already a member of it',
                type: 'error'
              })
            }else {
              that.$message({
                message: 'request failed, unknown error',
                type: 'error'
              })
            }
            let newToken = response.headers.authorization
            if (newToken != null) window.sessionStorage.setItem('token', newToken)
          },
          function (err){
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    postTeamAgain() {
      this.$emit('postTeamInfoAgain', {})
    }
  }

}
</script>

<style scoped>

.box-card {
  width: 100%;
  height: 200px;
}
.teamNameCSS{
  font-family: Arial, Helvetica, sans-serif;
  font-weight: bold;
  font-size:15px;
}
</style>
