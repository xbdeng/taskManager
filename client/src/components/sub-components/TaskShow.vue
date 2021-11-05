<template>
  <div>
    <template>
      <el-container>
          <el-header>
              {{ singleTaskData.taskName }}
          </el-header>
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
  props: ['singleTaskData'],
  /*
  Single Task Data:
    Task Name:
    Task Description
   */
  
}
</script>