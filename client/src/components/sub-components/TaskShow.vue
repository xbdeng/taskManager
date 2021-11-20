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
                <span class="title">{{ tempTaskForm.taskName }}</span>
              </el-col>

              <el-popover placement="top" width="200" trigger="click" title="修改任务名">
                  <el-row>
                      <el-col>
                          <el-input placeholder="请输入修改后的任务名..." clearable v-model="editedTaskName"></el-input>
                      </el-col>
                  </el-row>
                  <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editTaskName">确定</el-button>
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
            <!-- 显示任务标签，可以动态添加，删除 -->
            <el-row type="flex" justify="start">
              <el-col :span="3">
                <span style="font-weight:bold">任务标签: </span>
              </el-col>
              <el-col :span="17">
                <el-tag :key="tag"
                  v-for="tag in tempTaskForm.tags" 
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
            <!-- 显示任务优先级 -->
            <el-row type="flex" align="middle">
              <el-col :span="4">
                <span style="font-weight:bold">任务优先级：</span>
              </el-col>
              <el-col :span="5">
                <el-tooltip effect="dark" content="优先级：很高" placement="top-end" v-if="tempTaskForm.privilege === 3">
                  <i class="el-icon-s-flag" style="color:red" v-if="tempTaskForm.privilege === 3"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="优先级：高" placement="top-end" v-if="tempTaskForm.privilege === 2">
                  <i class="el-icon-s-flag" style="color:#FFA500" v-if="tempTaskForm.privilege === 2"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="优先级：中" placement="top-end" v-if="tempTaskForm.privilege === 1">
                  <i class="el-icon-s-flag" style="color:#00BFFF" v-if="tempTaskForm.privilege === 1"></i>
                </el-tooltip>
                <el-tooltip effect="dark" content="优先级：低" placement="top-end" v-if="tempTaskForm.privilege === 0">
                  <i class="el-icon-s-flag" style="color:#7CFC00" v-if="tempTaskForm.privilege === 0"></i>
                </el-tooltip>
              </el-col>
              <!-- 修改任务优先级的接口 -->
              <el-col :span="5">
                <el-popover placement="top" width="200" trigger="click" title="修改任务优先级">
                  <el-row>
                      <el-col>
                          <el-rate :texts="this.texts" show-text :max="4" v-model="editedPriority"></el-rate>
                      </el-col>
                  </el-row>
                  <el-row>
                    <el-col :offset="8">
                        <el-button type='primary' plain size="small" @click="editPriority">确定</el-button>
                    </el-col>
                  </el-row>
                    <el-tooltip content="点击可修改任务名称" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 显示任务的开始时间 -->
            <el-row>
              <el-col :span="4">
                <span style="font-weight:bold">任务开始时间：</span>
              </el-col>
              <el-col :span="15">
                {{ tempTaskForm.createDate }}
              </el-col>
              <el-col :span="3">
                  <el-popover placement="top" width="200" trigger="click" title="修改任务开始时间">
                    <el-row type="flex" justify="start">
                        <el-col>
                            <el-date-picker v-model="editedStartTime" type="datetime" placeholder="请选择任务的开始时间"></el-date-picker>
                        </el-col>
                    </el-row>
                    <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editStartTime">确定</el-button>
                      </el-col>
                    </el-row>
                    <el-tooltip content="点击可修改任务的开始时间" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 显示任务的结束时间 -->
            <el-row>
              <el-col :span="4">
                <span style="font-weight:bold">任务结束时间：</span>
              </el-col>
              <el-col :span="15">
                {{ tempTaskForm.dueDate }}
              </el-col>
              <el-col :span="3">
                  <el-popover placement="top" width="200" trigger="click" title="修改任务结束时间">
                    <el-row type="flex" justify="start">
                        <el-col>
                            <el-date-picker v-model="editedDDL" type="datetime" placeholder="请选择任务的结束时间"></el-date-picker>
                        </el-col>
                    </el-row>
                    <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editDDL">确定</el-button>
                      </el-col>
                    </el-row>
                    <el-tooltip content="点击可修改任务的结束时间" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 如果是组队任务，显示这个任务覆盖的组员 -->
            <el-row v-show="tempTaskForm.type===1 || tempTaskForm.type==='1'">
              <!-- 显示组内成员的组件 -->
                  <el-col :span="4">
                      <span style="font-weight:bold">任务成员：</span>
                  </el-col>
                  <!-- 下拉菜单 -->
                  <el-col :span="7">
                    <el-dropdown trigger="click">
                        <el-button>
                            点击查看任务成员<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="member in tempTaskForm.members" :key="member.name">
                                {{ member.name }}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                  </el-col>
                  <!-- 增加成员的接口 -->
                  <el-col :span="3">
                      <el-popover placement="top" width="900" trigger="click" title="邀请成员">
                        <el-row>
                            <el-col>
                              <!-- 穿梭框左边显示:我的好友 -->
                                <el-transfer :data="this.Friends" filterable :button-texts="['取消邀请','邀请进组']" v-model="invitedMembers" :titles="['我的好友','邀请名单']"></el-transfer>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :offset="8">
                                <el-button type='primary' @click="editInvitedMembers">确定</el-button>
                            </el-col>
                        </el-row>
                            <el-tooltip content="点击可邀请成员进组" slot="reference">
                            <el-link type='primary'>
                              <!-- 点击请求好友数据 -->
                                <i class="el-icon-plus"></i>
                            </el-link>
                            </el-tooltip>
                        </el-popover>
                  </el-col>
            </el-row>
            <!-- 显示任务描述信息 -->
            <el-row type="flex" justify="start">
              <el-col :span="4">
                <span style="font-weight:bold">任务描述：</span>
              </el-col>
              <el-col :span="20">
                {{ tempTaskForm.description }}
              </el-col>
              <el-popover placement="bottom" width="200" trigger="click" title="修改任务的描述信息">
                  <el-row>
                      <el-col>
                          <el-input placeholder="请输入修改后的描述信息..." type="textarea" :rows="4"></el-input>
                      </el-col>
                  </el-row>
                  <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editDescription">确定</el-button>
                      </el-col>
                  </el-row>
                    <el-tooltip content="点击可修改任务的描述信息" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>
            </el-row>
            <el-row>
              <el-button type="primary" @click="emitTreeData">查看树形图</el-button>
            </el-row>
            <!-- 输入框，可以输入子任务的名称，输入后摁下回车可以创建子任务，之后可以通过本界面进行修改 -->
            <el-row type="flex" justify="start">
              <el-col>
                <el-input v-model="subTaskName" placeholder="请输入子任务的名称" @keyup.enter.native="addSubTask"></el-input>
              </el-col>
            </el-row>
            <!-- 两个按钮，一个是修改完成，一个是删除任务 -->
            <el-row type="flex" justify="start">
              <el-col>
                <el-button type="primary" round @click="postEdit">确认修改</el-button>
              </el-col>
              <el-col>
                <el-button type="danger" round @click="closeDrawer">删除任务</el-button>
              </el-col>
            </el-row>
          </el-main>
      </el-container>
    </template>
  </div>
</template>
 
<script>
import axios from 'axios'
import process from "_shelljs@0.7.8@shelljs";
axios.defaults.baseURL = process.env.API_ROOT
export default {

  name: 'TaskShow',
  props: ['singleTaskData','Friends'],
  watch:{
    'singleTaskData':function() {
      this.tempTaskForm = JSON.parse(JSON.stringify(this.singleTaskData))
    }
  },
  data() {
    return {
      inputVisible:false,
      addedTag:null,
      texts:['低','中','高','很高'],
      editedPriority:null,
      editedStartTime:null,
      editedDDL:null,
      editedStatus:0,
      subTaskName:null,
      invitedMembers:[],
      editedTaskName:null,
      editedDescription:null,
      tempTaskForm:JSON.parse(JSON.stringify(this.singleTaskData))
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
        this.editedStatus = 1
      } 
      else if(document.getElementById('defined') != null) {
        document.getElementById('defined').id = 'origin'
        this.editedStatus = 0
      }
    },
    deleteTag(tag) {
      this.tempTaskForm.tags.splice(this.tempTaskForm.tags.indexOf(tag), 1);
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    handleInputConfirm() {
      let inputValue = this.addedTag;
      if(inputValue != null) {
        for(let i in this.tempTaskForm.tags) {
          if(this.tempTaskForm.tags[i] === inputValue) {
            this.$message.error('添加失败，已有该标签')
            this.inputVisible = false;
            this.addedTag = null;
            return 
          }
        }
        this.tempTaskForm.tags.push(inputValue);
      }
      this.inputVisible = false;
      this.addedTag = null;
    },
    editTaskName() {
      let value = this.editedTaskName
      if(value != null) {
        this.tempTaskForm.taskName = value
        this.editedTaskName = null
      }
      else {
        this.$message.error('修改失败，修改后的任务名不能为空')
      }
    },
    editPriority() {
      let value = this.editedPriority
      if(value != null) {
        this.tempTaskForm.privilege = this.editedPriority - 1
      }
      this.editedPriority = null
    },
    editStartTime() {
      let value = this.editedStartTime
      if(value != null) {
        this.tempTaskForm.createDate = value
      }
      this.editedStartTime = null
    },
    editDDL() {
      let value = this.editedDDL
      if(value != null) {
        this.tempTaskForm.dueDate = value
      }
      this.editedDDL = null
    },
    editInvitedMembers() {
      let value = this.invitedMembers
      if(value != null) {
        this.tempTaskForm.members.concat(value)
      }
      this.invitedMembers = null
    },
    editDescription() {
      let value = this.editedDescription
      if(value != null) {
        this.tempTaskForm.description = this.editedDescription
      }
      this.editedDescription = null
    },
    addSubTask() {
      let value = this.subTaskName
      if(value != null) {
        if(this.tempTaskForm.subTasks == null) {
          this.tempTaskForm.subTasks = []
        }
        this.tempTaskForm.subTasks.push({
          taskName:value
        })
      }
      this.subTaskName = null
    },
    postEdit() {
      let that = this
      axios({
        method:'POST',
        url:'/task/edittask',
        params:{
          'taskId':that.tempTaskForm.taskId
        },
        data:{
          createDate:that.tempTaskForm.createDate,
          description:that.tempTaskForm.description,
          dueDate:that.tempTaskForm.dueDate,
          //fatherTask
          members:that.tempTaskForm.members,
          privilege:that.tempTaskForm.privilege,
          status:that.tempTaskForm.status,
          subTasks:that.tempTaskForm.subTasks,
          tags:that.tempTaskForm.tags,
          taskName:that.tempTaskForm.taskName,
          type:that.tempTaskForm.type
        },
        headers:{
          Authorization:window.localStorage.getItem('token')
        }
      }).then(
        function(response) {
          //alert(response.data.msg)
          if(response.data.code === 200) {
            that.$message({
              message:'修改任务成功',
              type:'success'
            })
            let newToken = response.headers.authorization
            if(newToken != null) window.localStorage.setItem('token', newToken)
          } else {
            that.$message.error('修改任务失败')
            let newToken = response.headers.authorization
            if(newToken != null) window.localStorage.setItem('token', newToken)
          }
          that.$emit('closeTaskDrawer',{})
        },
        function(err) {
          this.$message.error('响应失败,修改任务失败')
        }
      )

    },
    closeDrawer() {
      let that = this
      axios({
        method:'POST',
        url:'/task/deletetask',
        params:{'id':that.tempTaskForm.taskId},
        headers:{
          Authorization:window.localStorage.getItem('token')
        }
      }).then(
          function(response) {
            //alert(response.data.msg)
            if(response.data.code === 200) {
              that.$message({
                message:'删除任务成功',
                type:'success'
              })
              this.$emit('closeTaskDrawer',{})
              let newToken = response.headers.authorization
              if(newToken != null) window.localStorage.setItem('token', newToken)
            } else {
              that.$message.error('删除任务失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.localStorage.setItem('token', newToken)
            }
          },
          function(err) {
            that.$message.error('响应失败，删除任务失败')
          }
      )
    },
    emitTreeData() {
      this.$emit('emitTreeData',this.singleTaskData)
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
.el-tag + .el-tag {
  margin-left: 10px;
}
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

.el-row {
    margin-bottom: 50px;
}

.el-dropdown {
vertical-align: top;
}
.el-dropdown + .el-dropdown {
margin-left: 15px;
}
.el-icon-arrow-down {
font-size: 12px;
}
</style>