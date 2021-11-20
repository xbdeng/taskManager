<template>
  <!--用vue3dtree组件构建树-->
  <div class="tree-diagram">
    <el-container>
      <el-aside>
        <el-menu>
          <TaskTree
              :taskData="taskData"
              :taskLevel="''"
              :chosenTask="chosenTaskId"
              v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
        </el-menu>
      </el-aside>
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
                <el-tooltip content="点击返回主页面" >
                  <el-button type="primary" icon="el-icon-back" circle @click="toMain"></el-button>
                </el-tooltip>
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
                ></tree>
          </el-main>
        </el-container>
      </el-main>
    </el-container>

  </div>
</template>

<script>
import {tree} from 'vued3tree'
import TaskTree from '../sub-components/TaskTree'

export default {
  components: {
    tree,
    TaskTree
  },
  props:['username'],
  data() {
    return {

      taskData: [
        {
          taskName: 'ooad',
          subTasks: [
            {
              taskName: 'frontend'
            },
            {
              taskName: 'backend',
              subTasks: [
                {
                  taskName: 'springboot'
                }
              ]
            }
          ]
        },
        {
          taskName: 'compiler',
          subTasks: [
            {
              taskName: 'lexical analysis',
              subTasks: [
                {
                  taskName: 'regex',
                },
                {
                  taskName: 'flex',
                },
                {
                  taskName: 'bison'
                }
              ]
            },
            {
              taskName: 'syntax analysis',
              subTasks: [
                {
                  taskName: 'context free grammar'
                },
                {
                  taskName: 'bison programming',
                  subTasks: [
                    {
                      taskName: 'C programming'
                    }
                  ]
                }
              ]
            }
          ]
        }
      ],
      tree: null,
      chosenTaskId: '-1',
      layoutType: 'horizontal',
      linkLayout:'bezier',

    }
  },
  methods: {
    getId(node){
      return node.id
    },
    setTree() {
      this.tree = this.taskToTree(this.getTaskById(this.taskData, this.chosenTaskId))
      console.log(this.tree)
    },
    //根据任务数据生成树的数据
    taskToTree(task) {
      let tree = {}
      tree.name = task.taskName
      if (task.subTasks == null || task.subTasks.length === 0) {
        return tree
      }
      tree.children = []
      for (let i in task.subTasks) {
        tree.children.push(this.taskToTree(task.subTasks[i]))
      }
      return tree
    },
    chooseTasks(id) {
      this.chosenTaskId = id;
      this.setTree()
    },
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
    setLayOutType(str) {
      this.layoutType = str
    },
    setLinkLayOut(str) {
      this.linkLayout = str
    },
    toMain() {
      this.$router.push({name:'Main', params:{username:this.username}})
    }
  }
}
</script>

<style scoped>
.tree-main{
  padding: 0;
}
.treeclass {
  height: 870px;
}
>>> .treeclass .nodetree text {
  font:18px Consolas !important;
  color:red !important;
}
</style>
