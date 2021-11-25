<template>
  <!--用vue3dtree组件构建树-->
  <div class="tree-diagram">
    <el-container>
      <el-main>
        <el-container>
          <el-header class="header">
            <el-row class="icon-row">
              <el-col class="icon-col" :offset="9">
                <el-popover
                  placement="bottom"
                  width="200"
                  trigger="hover">
                  <el-menu>
                    <el-submenu index="1">
                      <template slot="title">摆放方向</template>
                      <el-menu-item index="1-1" @click="setLayOutType('horizontal')">水平</el-menu-item>
                      <el-menu-item index="1-2" @click="setLayOutType('vertical')">竖直</el-menu-item>
                      <el-menu-item index="1-3" @click="setLayOutType('circular')">圆形</el-menu-item>
                    </el-submenu>
                    <el-submenu index="2">
                      <template slot="title">曲线样式</template>
                      <el-menu-item index="2-1" @click="setLinkLayOut('orthogonal')">直角曲线</el-menu-item>
                      <el-menu-item index="2-2" @click="setLinkLayOut('bezier')">贝塞尔曲线</el-menu-item>
                    </el-submenu>
                  </el-menu>
                  <el-button type="primary" size="mini" icon="el-icon-setting" slot="reference">显示设置</el-button>
                </el-popover>
<!--                <el-tooltip content="点击返回" >-->
<!--                  <el-button type="primary" icon="el-icon-back" circle @click="closeTreeDrawer" size="mini"></el-button>-->
<!--                </el-tooltip>-->
              </el-col>
            </el-row>
          </el-header>
          <el-main class="tree-main">
            <tree
                :data="tree"
                nodeText="name"
                :identifier="getId"
                :linkLayout="this.linkLayout"
                :layoutType="this.layoutType"
                @clickedText="onClick"
                @expand="onExpand"
                @retract="onRetract"
                @clickedNode="onClickNode">
              <template #popUp="{data,node}">
                <div>
                  <el-button @click="Addclick(data, node)" type="primary" icon="el-icon-plus" circle></el-button>
                  <el-button @click="Detclick(data, node)" type="danger" icon="el-icon-minus" circle></el-button>
                </div>
              </template>
            </tree>
          </el-main>
        </el-container>
      </el-main>
    </el-container>

    <el-dialog :visible.sync="dialogVisible" :before-close="handleClose" :modal-append-to-body="false" :modal="false">
      <add-task-form
          :fatherTaskId="this.fatherTaskId"
          :tagArray="this.tagArray"
          :myTeamInfo="this.myTeamInfo"
      v-on:postTree="closeTreeDrawer"></add-task-form>
    </el-dialog>

  </div>
</template>

<script>
import {tree,popUpOnHoverText} from 'vued3tree'
import AddTaskForm from "./AddTaskForm";
import axios from 'axios'
import process from "_shelljs@0.7.8@shelljs";
axios.defaults.baseURL = process.env.API_ROOT
export default {
  components: {
    AddTaskForm,
    tree,
    popUpOnHoverText
  },
  props:['TData', 'tagArray','myTeamInfo'],
  watch:{
    'TData':function() {
      this.taskData = this.TData
      this.tree = this.taskToTree(this.taskData)
    }
  },
  data() {
    return {
      taskData: this.TData,
      tree: this.taskToTree(this.TData),
      layoutType: 'vertical',
      linkLayout:'bezier',
      dialogVisible: false,
      fatherTaskId:null,
    }
  },
  methods: {
    getId(node){
      return node.id
    },
    //根据任务数据生成树的数据
    taskToTree(task) {
      let tree = {}
      tree.name = task.taskId + ': ' + task.taskName
      if (task.subTasks == null || task.subTasks.length === 0) {
        tree.children = []
        return tree
      }
      tree.children = []
      for (let i in task.subTasks) {
        tree.children.push(this.taskToTree(task.subTasks[i]))
      }
      return tree
    },
    setLayOutType(str) {
      this.layoutType = str
    },
    setLinkLayOut(str) {
      this.linkLayout = str
    },
    closeTreeDrawer() {
      this.$emit('closeTreeDrawer',{})
    },
    onClick (evt) {
      this.onEvent('clickedText', evt)
    },
    onClickNode (evt) {
      this.onEvent('clickedNode', evt)
    },
    onExpand (evt) {
      this.onEvent('onExpand', evt)
    },
    onRetract (evt) {
      this.onEvent('onRetract', evt)
    },
    onEvent (eventName, data) {
      console.log(eventName)
      console.log(data)
      // this.events.push({eventName, data: data.data})
    },
    Addclick(data, node){
      this.dialogVisible = true;
      console.log(data)
      console.log(node)
      this.fatherTaskId = parseInt(data.name.split(':')[0])
    },
    Detclick(data, node){
      const that = this
      console.log(data)
      console.log(node)
      let deletedTaskId = parseInt(data.name.split(':')[0])
      axios({
        method:'POST',
        url:'/task/deletetask',
        params:{
          id:deletedTaskId
        },
        headers:{
          Authorization:window.sessionStorage.getItem('token')
        }
      }).then(
          function(response) {
            if(response.data.code === 200) {
              that.$message({
                message:'删除任务成功'
              })
              that.$emit('postTreeTaskAgain',{})
            } else {
              that.$message.error('删除任务失败')
            }
          },
          function(err) {
            that.$message.error('响应失败，删除任务失败')
          }
      )
    },

    handleClose(done){
      this.dialogVisible = false
      done()
    },
    postTree() {
      this.$emit('postTree')
    }
  }
}
</script>

<style scoped>
.tree-main{
  padding: 0;
}
.treeclass {
  height: 710px;
}
>>> .treeclass .nodetree text {
  font:18px Consolas !important;
  color:red !important;
}
</style>
