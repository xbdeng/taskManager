<template>
<!-- 这个页面是任务搜索页面 -->
  <div id="searchTask">
    <el-container>
        <!-- 头部栏，用于显示搜索框 -->
        <el-header class='search_header'>
            <el-row type="flex" align="middle" :gutter="20">
                <el-input placeholder="请输入要查询的任务名称" prefix-icon="el-icon-search" v-model="searchTaskName"></el-input>
                <el-button type='primary' @click="searchRequest">搜索</el-button>
            </el-row>
        </el-header>
        <el-container>
            <!-- 侧边栏，用于递归显示找到的任务数据 -->
            <el-aside class="search_aside">
                <el-menu>
                    <TaskTree :taskData="searchedResult" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                </el-menu>
            </el-aside>
            <!-- 显示任务深故居 -->
            <el-main>
                <TaskShow :singleTaskData="getTaskById(searchedResult, chosenTaskId)"></TaskShow>
            </el-main>
        </el-container>
    </el-container>
  </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import axios from 'axios'
export default {

  name: "SearchTaskPage",
  components: {
      TaskTree,
      TaskShow
  },
  data() {
      return {
          // 用户选中的任务的key
          chosenTaskId:'-1',
          // 用户输入的查询任务的名称
          searchTaskName:'',
          // 搜索结果
          searchedResult:[
              {
                  taskName:'OOAD',
                  taskDescriptions:'An OOAD Project',
                  subTasks:[
                      {
                          taskName:'Front end',
                          taskDescriptions:'Front end is hard'
                      },
                      {
                          taskName:'back end',
                          taskDescriptions:'Back end is hard'
                      }
                  ]
              },
              {
                  taskName:'Complier',
                  taskDescriptions:'A Complier Project',
                  subTasks:[
                      {
                          taskName:'Lexcial Analysis',
                          taskDescriptions:'hard',
                          subTasks:[
                              {
                                  taskName:'regular expression',
                                  taskDescriptions:'abab',

                              }
                          ]
                      },
                      {
                          taskName:'Syntax Analysis',
                          taskDescriptions:'easy',
                          subTasks:[
                              {
                                  taskName:'Context Free Grammar',
                                  taskDescriptions:'hard'
                              }
                          ]
                      }
                  ]
              },
          ]
      }
  },
  methods: {
      // 用用户输入的任务名称像后端发送请求
      searchRequest(event) {
          const that = this
          axios.post(
              'http://localhost:8081',
              {
                  searchTaskName:this.searchTaskName
              }
          ).then(
              function(response) {
                  alert('搜索信息提交成功')
                  // Clear content in el-input
                  that.searchTaskName = ''
                  that.searchedResult = response.data.searchedResult
              },
              function(err) {
                  alert('搜索失败')
                  that.searchTaskName = ''
              }
          )
      },
      chooseTasks(id) {
          this.chosenTaskId = id;
      },
      getTaskById(taskList, id) {
          if (id === '-1') return {
              taskName:'Please choose your task'
        }
          if (id.length == 1) return taskList[parseInt(id)];

          return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
      }
  }

}
</script>

<style scoped>
.search_aside {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

</style>
