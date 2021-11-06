<template>
<!-- 这个页面是一个模板页面，用来向用户显示任务的信息 -->
  <div>
    <template>
      <el-container>
        <!-- 头部栏显示任务名 -->
          <el-header>
              {{ singleTaskData.taskName }}
          </el-header>
          <!-- 主页面显示任务的描述信息 -->
          <el-main>
              {{ singleTaskData.taskDescriptions }}
          </el-main>
      </el-container>
    </template>
  </div>
</template>
 
<script>

export default {

  name: 'TaskShow',
  // 这个组件接收的参数是一个任务对象
  props: ['singleTaskData'],
  
}
</script>