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
                <el-tooltip content="点击返回" >
                  <el-button type="primary" icon="el-icon-back" circle @click="closeTreeDrawer" size="mini"></el-button>
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
                :layoutType="this.layoutType">
              <template #behavior="{on, actions}">
                <popUpOnHoverText v-bind="{on, actions}"/>
              </template>
            </tree>
          </el-main>
        </el-container>
      </el-main>
    </el-container>

  </div>
</template>

<script>
import {tree,popUpOnHoverText} from 'vued3tree'

export default {
  components: {
    tree,
    popUpOnHoverText
  },
  props:['TData'],
  watch:{
    'Data':function() {
      this.taskData = this.TData
      this.tree = this.taskToTree(this.taskData)
    }
  },
  data() {
    return {
      taskData: this.TData,
      tree: this.taskToTree(this.TData),
      layoutType: 'horizontal',
      linkLayout:'bezier',
    }
  },
  methods: {
    getId(node){
      return node.id
    },
    //根据任务数据生成树的数据
    taskToTree(task) {
      let tree = {}
      tree.name = task.taskName
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
