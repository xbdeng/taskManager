<template>
<!-- 这个页面是一个模板页面，用来向用户显示任务的信息 -->
  <div>
    <template>
      <el-container>
        <!-- 头部栏显示任务名 -->
          <el-header>
            <el-row :gutter="10" type="flex" align="middle">
              <el-col :span="3">
                <el-tooltip content="完成任务：在已完成和未完成任务之间切换" placement="top-end">
                  <i class="el-icon-success" id="origin" v-on:mouseover="changeToActive($event)" v-on:mouseout="changeToOrigin($event)" @click="postTaskFinished($event)"></i>
                </el-tooltip>
              </el-col>

              <el-col :span="3">
                <span class="title">{{ singleTaskData.taskName }}</span>
              </el-col>

              <el-popover placement="top" width="200" trigger="click" title="修改任务名">
                  <el-row>
                      <el-col>
                          <el-input placeholder="请输入修改后的任务名..." clearable></el-input>
                      </el-col>
                  </el-row>
                  <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small">确定</el-button>
                      </el-col>
                  </el-row>
                    <el-tooltip content="点击可修改任务名称" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>

            </el-row>
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

  data() {
    return {

    }
  },
  methods:{
    changeToActive($event) {
      if(document.getElementById('origin') != null)
        document.getElementById('origin').id = 'active'
    },
    changeToOrigin($event) {
      if(document.getElementById('active') != null)
        document.getElementById('active').id = 'origin'
    },
    postTaskFinished($event) {
      if(document.getElementById('active') != null) {
        document.getElementById('active').id = 'defined'
      } 
      else if(document.getElementById('defined') != null) {
        document.getElementById('defined').id = 'origin'
      }
      // TODO:调用后端修改任务接口，显示该任务已经完成
    }
  }
  
}
</script>

<style scoped>
.title {
  font-weight: bold;
}
#active {
  color:rgb(63, 205, 50)
}
#origin {
  color:grey
}
#defined {
  color:rgb(63, 205, 50)
}
</style>