<template>
  <div>
    <template v-for="task in this.taskData">
      <el-submenu :key="task.taskName" :index="task.taskName" v-if="task.subTasks">
          <template slot="title">
              <i class="el-icon-apple"></i>
              <span slot="title">{{ task.taskName }}</span>
          </template>
          <TaskTree :taskData="task.subTasks"></TaskTree>
      </el-submenu>
      <el-menu-item :key="task.taskName" :index="task.taskName" v-else>
          <i class="el-icon-apple"></i>
          <span slot="title">{{ task.taskName }}</span>
      </el-menu-item>
    </template>
  </div>
</template>
 
<script>

export default {

  name: 'TaskTree',
  props: ['taskData'],
  
}
</script>