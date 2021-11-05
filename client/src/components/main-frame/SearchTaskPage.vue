<template>

  <div id="searchTask">
    <el-container>
        <el-header class='search_header'>
            <el-row type="flex" align="middle" :gutter="20">
                <el-input placeholder="请输入要查询的任务名称" prefix-icon="el-icon-search" v-model="searchTaskName"></el-input>
                <el-button type='primary'>搜索</el-button>
            </el-row>
        </el-header>
        <el-container>
            <el-aside class="search_aside">
                <el-menu>
                    <TaskTree :taskData="searchedResult"></TaskTree>
                </el-menu>
            </el-aside>
            <el-main>
                <TaskShow :singleTaskData="searchedResult[0]"></TaskShow>
            </el-main>
        </el-container>
    </el-container>
  </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
export default {

  name: "SearchTaskPage",
  components: {
      TaskTree,
    TaskShow
  },
  data() {
      return {
          //   User input for search Tasks
          searchTaskName:'',
          // Searched Result
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
                          taskDescriptions:'easy'
                      }
                  ]
              },
          ]
      }
  }

}
</script>

<style scoped>
.search_aside {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

</style>
