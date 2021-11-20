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
            <el-menu>
                <TaskTree 
                :taskData="searchedResult" 
                :taskLevel="''" 
                :chosenTask="chosenTaskId" 
                v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
            </el-menu>
        </el-main>
        <el-drawer 
            title="任务过滤器"
            :visible.sync="fliterDrawer"
            direction="ltr"
            :before-close="handleFliterClose"
            :modal-append-to-body='false'
            size='60%'
            >
            <el-form label-width="150px" ref='fliterForm' :model='fliterForm' :rules="rules" status-icon>
                <!-- taskName -->
                <el-form-item label="任务名称:" prop="taskName">
                    <el-row>
                        <el-col :span='14'>
                            <el-input placeholder='请输入任务名...' clearable v-model='fliterForm.taskName'></el-input>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- tags -->
                <el-form-item label="任务标签:" prop="tags">
                    <el-row type="flex" justify="start">
                        <el-col :span="17">
                            <el-tag :key="tag"
                            v-for="tag in fliterForm.tags" 
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
                <!-- privilege -->
                <el-form-item label="任务优先级:" prop="privilege">
                    <el-row>
                      <el-col>
                          <el-rate :texts="this.texts" show-text :max="4" v-model="fliterForm.privilege"></el-rate>
                      </el-col>
                  </el-row>
                </el-form-item>
                <!-- createDate -->
                <el-form-item label="任务开始时间" prop="createDate">
                    <el-row>
                        <el-col>
                            <el-date-picker v-model="fliterForm.createDate" type="datetime" placeholder="请选择任务的开始时间"></el-date-picker>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- dueDate -->
                <el-form-item label="任务结束时间:" prop="dueDate">
                    <el-row>
                        <el-col>
                            <el-date-picker v-model="fliterForm.dueDate" type="datetime" placeholder="请选择任务的结束时间"></el-date-picker>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- status -->
                <el-form-item label="任务状态:" prop="status">
                    <el-row>
                        <el-col>
                            <el-checkbox v-model="this.notFinished">未完成</el-checkbox>
                            <el-checkbox v-model="this.finished">已完成</el-checkbox>
                            <el-checkbox v-model="this.expired">已过期</el-checkbox>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- type -->
                <el-form-item label="任务类型:" prop="type">
                    <el-row>
                        <el-col>
                            <el-radio label="0" v-model="fliterForm.type">个人任务</el-radio>
                            <el-radio label="1" v-model="fliterForm.type">组队任务</el-radio>
                        </el-col>
                    </el-row>
                </el-form-item>
                <!-- teamName -->
                <el-form-item label="队伍名称:" prop="teamName" v-show="fliterForm.type==='1'">
                    <el-row>
                        <el-col :span='14'>
                            <el-input placeholder='请输入队伍名称...' clearable v-model='fliterForm.teamName'></el-input>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>
            <el-row>
                <el-col :span="5">
                    <el-button type='primary' @click="searchRequest">确定</el-button>
                </el-col>
                <el-col :span="5">
                    <el-button type='danger' @click="handleFliterClose">取消</el-button>
                </el-col>
            </el-row>
        </el-drawer>
        <el-drawer 
            title="查看或编辑任务"
            :visible.sync="taskInfoDrawer"
            direction="rtl"
            :before-close="handleTaskInfoClose"
            :modal-append-to-body='false'
            size='30%'
            >
            <TaskShow
                :singleTaskData="this.showedTask"
                v-on:closeTaskDrawer='handleTaskInfoClose($event)'
                v-on:emitTreeData="emitTreeData($event)"></TaskShow>
        </el-drawer>

        <el-drawer
              title="查看任务树形图"
              :visible.sync="treeDrawer"
              direction="ltr"
              :before-close="handleTreeClose"
              :modal-append-to-body='false'
              size='50%'>
                  <TreeTask
                  :TData="this.treeData"
                  v-on:closeTreeDrawer="handleTreeClose"></TreeTask>
              </el-drawer>
    </el-container>
  </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
import TreeTask from './TreeTask'
import axios from 'axios'
export default {

  name: "SearchTaskPage",
  components: {
      TaskTree,
      TaskShow,
      TreeTask
  },
  data() {
      var checkDueDate = (rule, value, callback)=> {
          if(new Date(value) <= new Date(this.fliterForm.createDate)) {
              return callback(new Error('截止时间不能在开始时间前'))
          }
          callback()
      };
      return {
          chosenTaskId:'-1',
          searchedResult:[],
          fliterForm:{
              taskName:null,
              createDate:null,
              dueDate:null,
              tags:null,
              privilege:null,
              status:null,
              teamName:null,
              type:null,

          },
          finished:false,
          notFinished:false,
          expired:false,
          addedTag:null,
          inputVisible:false,
          showedTask:null,
          fliterDrawer:false,
          taskInfoDrawer:false,
          treeDrawer:false,
          treeData:null,
          texts:['低','中','高','很高'],
          rules:{
              dueDate:[{validator:checkDueDate, trigger:'blur'}]
          }
      }
  },
  methods: {
      searchRequest() {
          const that = this
          axios.post(
              'http://localhost:8081/api/task/query',
              {
                createDate:that.fliterForm.createDate,
                dueDate:that.fliterForm.dueDate,
                privilege:that.fliterForm.privilege === 0 ? null : that.fliterForm.privilege - 1,
                status:that.generateStatusList(),
                tags:that.fliterForm.tags,
                taskName:that.fliterForm.taskName,
                teamName:that.fliterForm.teamName,
                type:that.fliterForm.type
              },
              {
                headers:{
                    Authorization:window.localStorage.getItem('token')
                }
              }
          ).then(
              function(response) {
                  //alert(response.data.msg)
                  if(response.data.code === 200) {
                      that.searchedResult = response.data.data
                      that.$message({
                          message:'查询成功',
                          type:'success'
                      })
                      that.clear()
                  } else {
                      that.$message.error('查询失败')
                  }
              },
              function(err) {
                  that.$message.error('响应失败,查询失败')
              }
          )
      },
      chooseTasks(id) {
          this.chosenTaskId = id;
          this.showedTask = this.getTaskById(this.searchedResult, id)
          console.log(this.showedTask.taskName)
          this.taskInfoDrawer = true
      },
      getTaskById(taskList, id) {
          if (id === '-1') return {
              taskName:'Please choose your task'
        }
          if (id.length === 1) return taskList[parseInt(id)];

          return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
      },
      openFliter() {
          this.fliterDrawer = true
      },
      handleTaskInfoClose() {
          this.taskInfoDrawer = false
      },
      handleFliterClose() {
          this.fliterDrawer = false
          this.clear()
      },
      handleInputConfirm() {
          let inputValue = this.addedTag;
            if(inputValue) {
                for(let i in this.fliterForm.tags) {
                if(this.fliterForm.tags[i] === inputValue) {
                    this.$message.error('添加失败，已有该标签')
                    this.inputVisible = false;
                    this.addedTag = '';
                    return 
                }
            }
                this.fliterForm.tags.push(inputValue);
            } else {
                this.$message.error('添加的标签不能为空')
            }
            this.inputVisible = false;
            this.addedTag = '';
      },
      deleteTag(tag) {
        this.fliterForm.tags.splice(this.fliterForm.tags.indexOf(tag), 1);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
            this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      generateStatusList() {
          let array = []
          if(this.finished) {
              array.push(0)
          }
          if(this.notFinished) {
              array.push(1)
          }
          if(this.expired) {
              array.push(2)
          }
          if(array.length === 0) return null
          return array
      },
      clear() {
          for(let i in this.fliterForm) {
              this.fliterForm[i] = null
          }
          this.finished = false
          this.notFinished = false
          this.expired = false
      },
      handleTreeClose() {
        this.treeDrawer = false
      },
      emitTreeData(task) {
        this.treeDrawer = true
        this.treeData = task
      }

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
