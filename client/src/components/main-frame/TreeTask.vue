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
        <tree :data="tree" nodeText="name" layoutType="vertical" class="tree" :zoomable=true></tree>
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
    }
  },
  methods: {
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
    }
}
</script>

<style scoped>
.tree{
  height: 850px;
}
</style>