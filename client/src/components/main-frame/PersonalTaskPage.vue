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
              <el-container>
                <el-main>
                  <div class="test-div">
                <el-menu :default-openeds="['today']">
                  <DragTaskTree
                  :taskData="this.taskData"
                  v-on:taskIdChanged="chooseTasks($event)"
                  v-on:postTaskDataAgain="postTaskDataAgain($event)"
                  v-show="this.taskShow"></DragTaskTree>

                  <el-submenu index='today' v-show="planedTaskShow">
                    <template slot="title">
                      <span slot="title" @click="setSpecifier(0)">今天</span>
                    </template>
                    <DragTaskTree
                    :taskData="this.todayTaskData"
                    v-on:taskIdChanged="chooseTasks($event)"
                    v-on:postTaskDataAgain="postTaskDataAgain($event)"></DragTaskTree>
                  </el-submenu>
                  <el-submenu index='week' v-show="planedTaskShow">
                    <template slot="title">
                      <span slot="title" @click="setSpecifier(1)">一周内</span>
                    </template>
                    <DragTaskTree
                    :taskData="this.weekTaskData"
                    v-on:taskIdChanged="chooseTasks($event)"
                    v-on:postTaskDataAgain="postTaskDataAgain($event)"></DragTaskTree>
                  </el-submenu>
                  <el-submenu index='later' v-show="planedTaskShow">
                    <template slot="title">
                      <span slot="title" @click="setSpecifier(2)">稍后</span>
                    </template>
                    <DragTaskTree
                    :taskData="this.laterTaskData"
                    v-on:taskIdChanged="chooseTasks($event)"
                    v-on:postTaskDataAgain="postTaskDataAgain($event)"></DragTaskTree>
                  </el-submenu>
                  <el-input v-model="addedTaskName" placeholder="请输入要添加的任务的名称" @keyup.enter.native="addTask"></el-input>
                </el-menu>
                  </div>
                </el-main>
              </el-container>
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
                :tagArray="this.tagArray"
                :myTeamInfo="this.myTeamInfo"
                v-on:closeTreeDrawer="handleTreeClose"
                v-on:postTreeTaskAgain="postPersonalTaskAgain"></TreeTask>
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
import dragTreeTable from "drag-tree-table";
import DragTaskTree from "../sub-components/DragTaskTree";
axios.defaults.baseURL = process.env.API_ROOT
export default {
  name: "PersonalTaskPage",
  components: {
    TaskTree,
    TaskShow,
    TreeTask,
    dragTreeTable,
    DragTaskTree
  },
  props:['username','taskData','todayTaskData','weekTaskData','laterTaskData', 'transData'],
  data() {
    return {
        chosenTaskId:'-1',
        //任务
        taskShow:true,
        planedTaskShow:false,
        Specifier:0,
        drawer:false,
        addedTaskName:'',
        treeDrawer:false,
        treeData:null,
        tagArray:[],
        myTeamInfo:[],
        colprop:null
    }
  },
  watch:{
    //监听，一旦树形图打开，自动请求标签和用户创建或管理的组
    'treeDrawer':function() {
      this.postTags()
      this.postMyTeams()
    }
  },
  mounted() {
    this.colprop = [
      {
        type: "selection",
        title: "<a>Task Name</a>",
        field: "name",
        width: 200,
        align: "left",
        titleAlign: "left",
      },
      {
        title: "Task ID",
        type: "id",
        width: 100,
        align: "center"
      },
      {
        title: "Action",
        type: "action",
        flex: 1,
        align: "center",
      },
    ];
  },
  methods: {
    //list是拖拽后形成的新的任务列表
    onTreeDataChange(list) {
      console.log(this.findNewEdge(this.transData,list))
    },
    getEdgeSet(father) {
      if(father.lists.length === 0) {
        return []
      }
      let edgeSet = []
      for(let i in father.lists) {
        let child = father.lists[i]
        edgeSet.push(father.id + '-' + child.id)
        let edges = this.getEdgeSet(child)
        if(!(edges.length === 0)) {
          edgeSet.push.apply(edgeSet, edges)
        }
      }
      return edgeSet
    },
    getForestEdgeSet(list) {
      let edgeSet = []
      for (let i in list) {
        edgeSet.push.apply(edgeSet, this.getEdgeSet(list[i]))
      }
      return edgeSet
    },
    findNewEdge(oldList, newList) {
      let edges1 = this.getForestEdgeSet(oldList)
      let edges2 = this.getForestEdgeSet(newList)
      for(let i in edges1) {
        let oldEdge = edges1[i]
        let index = edges2.indexOf(oldEdge)
        if(index > -1) {
          edges2.splice(index, 1)
        }
      }
      return edges2
    },
    add(row) {
      console.log(row)
      console.log('add!')
    },
    edit(row) {
      console.log('edit!')
    },
    onDel(item) {
      console.log('del!')
    },
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
    getTask(id) {
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
      if(that.addedTaskName === '' || that.addedTaskName === null) {
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
              Authorization:window.sessionStorage.getItem('token')
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
              that.$emit('postPersonalTaskAgain',{})
            }else if(response.data.code === 201){
              that.$message.error('你们已添加过好友')
            }
            else {
              that.$message.error('添加任务失败')
            }
            let newToken = response.headers.authorization
            if(newToken != null) window.sessionStorage.setItem('token', newToken)
          },
          function(err) {
            that.$message.error('响应失败，添加任务失败')
          }
      )
    },
    emitTreeData(task) {
      console.log(task)
      this.treeData = task
      this.treeDrawer = true
    },
    postPersonalTaskAgain() {
      this.drawer = false
      this.treeDrawer = false
      this.$emit('postPersonalTaskAgain', {})
    },
    //请求用户的标签数据
    postTags() {
      let that = this
      axios.post(
          '/user/selecttags',
          {
            username: this.username
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '获取标签成功',
                type: 'success'
              })
              that.tagArray = []
              for (let i in response.data.data) {
                let content = response.data.data[i]
                let obj = {
                  label: content.tagName,
                  value: content.tagName
                }

                that.tagArray.push(obj)
              }
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('获取Tags数据失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应错误,获取Tags数据失败')
          }
      )
    },
    // 请求用户创建或管理的组
    postMyTeams() {
      let that = this;
      axios.post(
          '/user/myteams/admin',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            //alert(response.data.msg)
            if (response.data.code === 200) {
              that.$message({
                message: '请求用户创建或管理的组成功',
                type: 'success'
              })
              that.myTeamInfo = response.data.data
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('请求用户创建或管理的组失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应错误,请求用户创建或管理的组失败')
          }
      );
    },
    postTaskDataAgain() {
      this.$emit('postPersonalTaskAgain', {})
    }

  }

}
</script>

<style scoped>
.test-div{
  overflow-y:auto;
  height: 90%;
}
</style>
