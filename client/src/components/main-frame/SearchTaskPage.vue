<template>
<!-- 这个页面是任务搜索页面 -->
  <div id="searchTask">
    <el-container>
        <!-- 头部栏，用于显示搜索框 -->
        <el-header class='search_header'>
            <el-row type="flex" align="middle" :gutter="20">
                <el-button type="success" icon="el-icon-search" circle @click="openFliter"></el-button>
            </el-row>
        </el-header>
        <el-main>
            <!-- 侧边栏，用于递归显示找到的任务数据 -->
            <el-menu>
                <TaskTree :taskData="searchedResult" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
            </el-menu>
        </el-main>
        <el-drawer 
            title="任务过滤器"
            :visible.sync="fliterDrawer"
            direction="ttb"
            :before-close="handleFliterClose"
            :modal-append-to-body='false'
            size='60%'
            >
            <el-form label-width="150px" ref='fliterForm' :model='fliterForm' status-icon>
                <!-- 任务名称 -->
                <el-form-item label="任务名称:" prop="taskName">
                    <el-row>
                        <el-col :span='14'>
                            <el-input placeholder='请输入任务名...' clearable v-model='fliterForm.taskName'></el-input>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- 任务标签 -->
                <el-form-item label="任务标签:" prop="taskTags">
                    <el-row type="flex" justify="start">
                        <el-col :span="17">
                            <el-tag :key="tag"
                            v-for="tag in fliterForm.taskTags" 
                            closable 
                            :disable-transitions="false" 
                            @close= "deleteTag(tag)">
                                {{ tag }}
                            </el-tag>
                            <el-input class="input-new-tag" v-if="inputVisible" size="small" v-model="addedTag" ref="saveTagInput"
                            @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
                            </el-input>
                            <el-button v-else class="button-new-tag" size="small" @click="showInput">+添加新标签</el-button>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- 任务优先级 -->
                <el-form-item label="任务优先级:" prop="taskPriority">
                    <el-row>
                      <el-col>
                          <el-rate :texts="this.texts" show-text :max="4" v-model="fliterForm.taskPriority"></el-rate>
                      </el-col>
                  </el-row>
                </el-form-item>
                <!-- 任务开始时间 -->
                <el-form-item label="任务开始时间" prop="taskStartTime">
                    <el-row>
                        <el-col>
                            <el-date-picker v-model="fliterForm.taskStartTime" type="datetime" placeholder="请选择任务的开始时间"></el-date-picker>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- 任务结束时间 -->
                <el-form-item label="任务结束时间:" prop="taskDDL">
                    <el-row>
                        <el-col>
                            <el-date-picker v-model="fliterForm.taskDDL" type="datetime" placeholder="请选择任务的结束时间"></el-date-picker>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
            <el-row>
                <el-col :span="5">
                    <el-button type='primary'>确定</el-button>
                </el-col>
                <el-col :span="5">
                    <el-button type='danger'>取消</el-button>
                </el-col>
            </el-row>
        </el-drawer>
        <el-drawer 
            title="查看或编辑任务"
            :visible.sync="taskInfoDrawer"
            direction="rtl"
            :before-close="handleTaskInfoClose"
            :modal-append-to-body='false'
            size='50%'
            >
            <TaskShow :singleTaskData="getTaskById(searchedResult, chosenTaskId)"></TaskShow>
        </el-drawer>
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
          ],
          // 过滤器表单
          fliterForm:{
              taskName:'',
              taskStartTime:'',
              taskDDL:'',
              taskTags:[],
              taskPriority:0
          },
          addedTag:'',
          inputVisible:false,
          // 是否显示过滤器的抽屉
          fliterDrawer:false,
          // 是否显示任务信息的抽屉
          taskInfoDrawer:false,
          texts:['低','中','高','很高']
      }
  },
  methods: {
      // 向后端发送请求
      searchRequest(event) {
          const that = this
          axios.post(
              'http://localhost:8081',
              {
                  searchTaskName:this.searchTaskName
              }
          ).then(
              function(response) {
                  that.$message(
                      {
                          message:'查询成功',
                          type:'success'
                      }
                  )
                  // Clear content in el-input
                  that.searchTaskName = ''
                  that.searchedResult = response.data.searchedResult
              },
              function(err) {
                  that.$message.error('查询失败')
                  that.searchTaskName = ''
              }
          )
      },
      chooseTasks(id) {
          this.taskInfoDrawer = true
          this.chosenTaskId = id;
      },
      getTaskById(taskList, id) {
          if (id === '-1') return {
              taskName:'Please choose your task'
        }
          if (id.length == 1) return taskList[parseInt(id)];

          return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
      },
      // 打开过滤器抽屉
      openFliter() {
          this.fliterDrawer = true
      },
      handleTaskInfoClose() {
          this.taskInfoDrawer = false
      },
      handleFliterClose() {
          this.fliterDrawer = false
      },
      handleInputConfirm() {
          let inputValue = this.addedTag;
            if(inputValue) {
                for(let i in this.fliterForm.taskTags) {
                if(this.fliterForm.taskTags[i] === inputValue) {
                    this.$message.error('添加失败，已有该标签')
                    this.inputVisible = false;
                    this.addedTag = '';
                    return 
                }
                }
                this.fliterForm.taskTags.push(inputValue);
            }
            this.inputVisible = false;
            this.addedTag = '';
      },
      deleteTag(tag) {
        this.fliterForm.taskTags.splice(this.fliterForm.taskTags.indexOf(tag), 1);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
            this.$refs.saveTagInput.$refs.input.focus();
        });
      },

  }

}
</script>

<style scoped>
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
