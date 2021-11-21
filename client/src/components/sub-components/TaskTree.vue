<template>
<!-- 这个页面是一个模板页面，用来将给定的任务数组递归地显示在菜单栏中 -->
  <div>
    <!-- 遍历任务数组中的每一个任务，获得任务对象task和下标tIndex -->
    <template v-for="(task, tIndex) in this.taskData">
      <!-- 这个是一个子菜单栏
        :index = "taskLevel + String(tIndex)": "taskLevel + String(tIndex)"是唯一标识一个任务的key, 其中,taskLevel是这个组件的参数，
          表示它的父亲任务的key，String(tIndex)表示在父亲任务的subTasks中的下标。
        v-if="task.subTasks": 表示如果有子任务的话，会递归调用这个模板。
        -->
      <el-submenu :key="task.taskName" :index="taskLevel + String(tIndex)" v-if="task.subTasks">
          <template slot="title">
            <!-- 此处绑定changeTaskId，表示当点击某个任务时，会向父组件传递这个任务的key，一直传递到SearchTaskPage,GroupInfoPage或者是PersonalTaskPage -->
              <span slot="title" @click="changeTaskId(taskLevel + String(tIndex))">
                <el-tooltip effect="dark" content="优先级：很高" placement="top-end">
                  <i class="el-icon-s-flag" style="color:red" v-if="task.privilege === 3"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="优先级：高" placement="top-end">
                  <i class="el-icon-s-flag" style="color:#FFA500" v-if="task.privilege === 2"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="优先级：中" placement="top-end">
                  <i class="el-icon-s-flag" style="color:#00BFFF" v-if="task.privilege === 1"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="优先级：低" placement="top-end">
                  <i class="el-icon-s-flag" style="color:#7CFC00" v-if="task.privilege === 0"></i>
                </el-tooltip>
                {{ task.taskName }}
                <el-tooltip effect="dark" content="任务已完成，辛苦啦" placement="top-end">
                  <i class="el-icon-success" style="color:#7CFC00" v-if="task.status === 1"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="该任务未能在DDL之前完成" placement="top-end">
                  <i class="el-icon-error" style="color:red" v-if="task.status === 2"></i>
                </el-tooltip>
              </span>
          </template>
          <!-- 这里递归调用模板，传递的参数是本层的key:taskLevel + String(tIndex) -->
          <TaskTree :taskData="task.subTasks" :taskLevel="taskLevel + String(tIndex)" :chosenTask="chosenTask" v-on:taskIdChanged='chooseTasks($event)'></TaskTree>
      </el-submenu>
      <!-- 如果任务没有子任务，就不用el-submenu递归显示，直接用el-menu-item显示 -->
      <el-menu-item :key="task.taskName" :index="taskLevel + String(tIndex)" v-else>
          <span slot="title" @click="changeTaskId(taskLevel + String(tIndex))">
            <el-tooltip effect="dark" content="优先级：很高" placement="top-end" v-if="task.privilege === 3">
              <i class="el-icon-s-flag" style="color:red" v-if="task.privilege === 3"></i>
            </el-tooltip>
            <el-tooltip effect="dark" content="优先级：高" placement="top-end" v-if="task.privilege === 2">
              <i class="el-icon-s-flag" style="color:#FFA500" v-if="task.privilege === 2"></i>
            </el-tooltip>
            <el-tooltip effect="dark" content="优先级：中" placement="top-end" v-if="task.privilege === 1">
              <i class="el-icon-s-flag" style="color:#00BFFF" v-if="task.privilege === 1"></i>
            </el-tooltip>
            <el-tooltip effect="dark" content="优先级：低" placement="top-end" v-if="task.privilege === 0">
              <i class="el-icon-s-flag" style="color:#7CFC00" v-if="task.privilege === 0"></i>
            </el-tooltip>
            {{ task.taskName }}
          </span>
      </el-menu-item>
    </template>
  </div>
</template>
 
<script>

export default {

  name: 'TaskTree',
  /*
    taskData: task数组
    taskLevel: 父亲任务的key
    chosenTask: 当前选中的任务的key，用于向上传递
   */
  props: ['taskData', 'taskLevel', 'chosenTask'],
  
  methods: {
    // 点击某个任务后，调用this.$emit函数发出taskIdChanged信号，并向父组件传递选中的任务id
    changeTaskId(id) {
      this.$emit('taskIdChanged', id)
    },
    // 接收子组件taskIdChanged信号的响应函数，将父组件传过来的id继续向上面发送
    chooseTasks(id) {
      this.$emit('taskIdChanged', id)
    }
  }
}
</script>