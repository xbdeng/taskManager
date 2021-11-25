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
                  <i class="el-icon-success"
                     :id="this.tempTaskForm.status === 0 ? 'origin' : (this.tempTaskForm.status === 1 ? 'active' : 'defined') "
                     v-on:mouseover="changeToActive($event)"
                     v-on:mouseout="changeToOrigin($event)"
                     @click="postTaskFinished($event)"
                     style="font-size: 20px"></i>
                </el-tooltip>
              </el-col>
              <el-col :span="15">
              <el-popover placement="top" width="200" trigger="click" title="修改任务名" ref="taskNameVisible">
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
                      <el-link>
                          <span class="title">{{ tempTaskForm.taskName }}</span>
                      </el-link>
                    </el-tooltip>
                </el-popover>
                </el-col>
            </el-row>
          </el-header>
          <!-- 主页面显示任务的描述信息 -->
          <el-main>
            <!-- 显示任务标签，可以动态添加，删除 -->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">任务标签: </span>
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
            <!-- 显示任务地点-->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">任务地点: </span>
              </el-col>
            <el-col :span="18" align="middle">
              <el-popover placement="bottom" width="200" trigger="click" title="修改任务的地点" ref="taskLocationVisible">
                  <el-row>
                      <el-col>
                          <el-input placeholder="请输入修改后的任务地点..." v-model="editedlocation"></el-input>
                      </el-col>
                  </el-row>
                  <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editlocation">确定</el-button>
                      </el-col>
                  </el-row>
                    <el-tooltip content="点击可修改任务的地点" slot="reference">
                      <el-link>
                          {{ tempTaskForm.location === '' ||  tempTaskForm.location == null? '未设定地点' : tempTaskForm.location}}
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 显示任务优先级 -->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">任务优先级: </span>
              </el-col>
              <!-- 修改任务优先级的接口 -->
              <el-col :span="5">
                <el-popover placement="top" width="200" trigger="click" title="修改任务优先级" ref="taskPriorityVisible">
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
                    <el-tooltip content="点击可修改任务优先级" slot="reference">
                      <el-link type='primary'>
<!--                        <i class="el-icon-edit"></i>-->
                        <el-col :span="5">
                          <el-tooltip effect="dark" content="优先级：很高" placement="top-end" v-if="tempTaskForm.privilege === 3">
                            <i class="el-icon-s-flag" style="color:red;font-size: 20px" v-if="tempTaskForm.privilege === 3"></i>
                          </el-tooltip>
                          <el-tooltip effect="dark" content="优先级：高" placement="top-end" v-if="tempTaskForm.privilege === 2">
                            <i class="el-icon-s-flag" style="color:#FFA500;font-size: 20px" v-if="tempTaskForm.privilege === 2"></i>
                          </el-tooltip>
                          <el-tooltip effect="dark" content="优先级：中" placement="top-end" v-if="tempTaskForm.privilege === 1">
                            <i class="el-icon-s-flag" style="color:#00BFFF;font-size: 20px" v-if="tempTaskForm.privilege === 1"></i>
                          </el-tooltip>
                          <el-tooltip effect="dark" content="优先级：低" placement="top-end" v-if="tempTaskForm.privilege === 0">
                            <i class="el-icon-s-flag" style="color:#7CFC00;font-size: 20px" v-if="tempTaskForm.privilege === 0"></i>
                          </el-tooltip>
                        </el-col>
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 显示任务的开始时间 -->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">开始时间：</span>
              </el-col>
              <el-col :span="18">
                  <el-popover placement="top" width="200" trigger="click" title="修改任务开始时间" ref="taskStartVisible">
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
                      <el-link>
<!--                        <i class="el-icon-edit"></i>-->
                          {{ this.convertTime(tempTaskForm.createDate) }}
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 显示任务的结束时间 -->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">结束时间: </span>
              </el-col>
              <el-col :span="17">
                  <el-popover placement="top" width="200" trigger="click" title="修改任务结束时间" ref="taskEndVisible">
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
                      <el-link>
                          {{ this.convertTime(tempTaskForm.dueDate) }}
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!--任务的提醒时间-->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">提醒时间: </span>
              </el-col>
              <el-col :span="17">
                  <el-popover placement="top" width="200" trigger="click" title="修改任务提醒时间" ref="taskRemindVisible">
                    <el-row type="flex" justify="start">
                        <el-col>
                            <el-date-picker v-model="editedRemindDate" type="datetime" placeholder="请选择任务的提醒时间"></el-date-picker>
                        </el-col>
                    </el-row>
                    <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editRemindDate">确定</el-button>
                      </el-col>
                    </el-row>
                    <el-tooltip content="点击可修改任务的提醒时间" slot="reference">
                      <el-link>
                          {{ this.convertTime(tempTaskForm.remindDate) }}
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-col>
            </el-row>
            <!-- 如果是组队任务，显示这个任务覆盖的组员 -->
            <el-row v-show="(tempTaskForm.type===1 || tempTaskForm.type==='1') && !(this.singleTeamData == null)" type="flex" justify="start" align="middle">
              <!-- 显示组内成员的组件 -->
                  <el-col :span="8">
                      <span style="font-weight:bold;font-size: 18px">任务成员: </span>
                  </el-col>
                  <!-- 下拉菜单 -->
                  <el-col :span="8">
                    <el-dropdown trigger="click">
                        <el-button @click="getMemberList">
                            点击查看任务成员<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item
                                v-for="member in tempTaskForm.members"
                                :key="member.name"
                                @mouseover.native="whenMouseIn(member.name)"
                                @mouseout.native="whenMouseOut"
                                @click.native="deleteMember(member.name)">
                                {{ member.name }}
                              <i class="el-icon-remove" v-if="member.name===mousein"></i>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                  </el-col>
                  <!-- 增加成员的接口 -->
                  <el-col :span="3">
                      <el-popover placement="top" width="900" trigger="click" title="邀请成员">
                        <el-row>
                            <el-col>
<!--                              TODO-->
                                <el-transfer :data="this.list1" filterable :button-texts="['取消添加','添加任务成员']" v-model="invitedMembers" :titles="['组内成员','分配名单']"></el-transfer>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :offset="8">
                                <el-button type='primary' @click="editInvitedMembers">确定</el-button>
                            </el-col>
                        </el-row>
                            <el-tooltip content="点击可添加成员" slot="reference">
                            <el-link type='primary'>
                              <!-- 点击请求好友数据 -->
                                <i class="el-icon-plus"></i>
                            </el-link>
                            </el-tooltip>
                        </el-popover>
                  </el-col>
            </el-row>
            <!-- 显示任务描述信息 -->
            <el-row type="flex" justify="start" align="middle">
              <el-col :span="8">
                <span style="font-weight:bold;font-size: 18px">任务描述：</span>
              </el-col>
              <el-col :span="15">
                <el-popover placement="bottom" width="200" trigger="click" title="修改任务的描述信息" ref="taskDescriptionVisible">
                    <el-row>
                        <el-col>
                            <el-input placeholder="请输入修改后的描述信息..." type="textarea" :rows="4" v-model="editedDescription"></el-input>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :offset="8">
                            <el-button type='primary' plain size="small" @click="editDescription">确定</el-button>
                        </el-col>
                    </el-row>
                      <el-tooltip content="点击可修改任务的描述信息" slot="reference">
                        <el-link>
                            {{ (tempTaskForm.description == null || tempTaskForm.description === '') ? '描述信息为空' : tempTaskForm.description}}
                        </el-link>
                      </el-tooltip>
                  </el-popover>
              </el-col>
            </el-row>
            <!--查看树形图-->
            <el-row>
              <el-button type="primary" @click="emitTreeData" icon="iconfont el-icon-githubDirectory-tree">查看树形图</el-button>
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
  props: ['singleTaskData','Friends', 'singleTeamData'],
  watch:{
    'singleTaskData':function() {
      this.tempTaskForm = JSON.parse(JSON.stringify(this.singleTaskData))
    },
  },
  data() {
    const generateTransferData = _ => {
      const data = []
      if(this.singleTeamData == null) return []
      data.push({
          key: this.singleTeamData.creator,
          label: this.singleTeamData.creator
        });
      for(let i in this.singleTeamData.admins) {
        data.push({
          key: this.singleTeamData.admins[i],
          label: this.singleTeamData.admins[i]
        });
      }
      for (let i in this.singleTeamData.members) {
        data.push({
          key: this.singleTeamData.members[i],
          label: this.singleTeamData.members[i]
        });
      }
      return data
    };
    // 生成某个任务组员的穿梭框信息
    //TODO:后端实现了任务的组员记得改过来
    // const generateMemberData = _ => {
    //   const data = []
    //   for (let i in this.tempTaskForm.members) {
    //     data.push({
    //       key: this.tempTaskForm.members[i],
    //       label: this.tempTaskForm.members[i]
    //     });
    //   }
    //   return data
    // };
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
      editedDescription:'',
      editedlocation:null,
      editedRemindDate:null,
      // 用于存储要新添加的任务，是字符串类型的数组
      subTasksList:[],
      memberList:[],
      tempTaskForm:JSON.parse(JSON.stringify(this.singleTaskData)),
      list1:generateTransferData(),
      mousein:null,
      //TODO:理由同上
      // list2:generateMemberData(),

    }
  },
  methods:{
    changeToActive($event) {
      if(document.getElementById('origin') != null)
        this.tempTaskForm.status = 1
    },
    changeToOrigin($event) {
      if(document.getElementById('active') != null)
        this.tempTaskForm.status = 0
    },
    postTaskFinished($event) {
      if(document.getElementById('active') != null) {
        this.tempTaskForm.status = 2
        const that = this
        axios.post(
            '/task/edit/status',
            {
              dueDate:that.tempTaskForm.dueDate,
              status:1,
              taskId:that.tempTaskForm.taskId
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  message:'修改成功',
                  type:'success'
                })
                that.$emit('update',{})
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
      else if(document.getElementById('defined') != null) {
        this.tempTaskForm.status = 0
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
        const that = this
        axios.post(
            '/task/edit/taskname',
            {
              taskId:that.tempTaskForm.taskId,
              taskName:that.tempTaskForm.taskName
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$emit('update',{})
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
                that.$refs.taskNameVisible.doClose();
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
        this.editedTaskName = null
      }
      else {
        this.$message.error('修改失败，修改后的任务名不能为空')
      }
    },
    editlocation() {
      let value = this.editedlocation
      if(value != null) {
        this.tempTaskForm.location = value
        const that = this
        axios.post(
            '/task/edit/location',
            {
              taskId:that.tempTaskForm.taskId,
              location:that.tempTaskForm.location
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$emit('update',{})
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
                that.$refs.taskLocationVisible.doClose()
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
      this.editedlocation = null
    },
    editPriority() {
      let value = this.editedPriority
      if(value != null) {
        this.tempTaskForm.privilege = this.editedPriority - 1
        const that = this
        axios.post(
            '/task/edit/privilege',
            {
              privilege:that.tempTaskForm.privilege,
              taskId:that.tempTaskForm.taskId
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$forceUpdate()
                that.$emit('update',{})
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
                that.$refs.taskPriorityVisible.doClose()
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
      this.editedPriority = null
    },
    editStartTime() {
      let value = this.editedStartTime
      if(value != null) {
        this.tempTaskForm.createDate = value
        const that = this
        that.taskStartVisible = false
        axios.post(
            '/task/edit/startdate',
            {
              dateTime:that.tempTaskForm.createDate,
              taskId:that.tempTaskForm.taskId
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$forceUpdate()
                that.$emit('update',{})
                that.$refs.taskStartVisible.doClose()
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
                that.editedStartTime = null
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
    },
    editDDL() {
      let value = this.editedDDL
      if(value != null) {
        this.tempTaskForm.dueDate = value
        const that = this
        axios.post(
            '/task/edit/duedate',
            {
              dateTime:that.tempTaskForm.dueDate,
              taskId:that.tempTaskForm.taskId
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$forceUpdate()
                that.$emit('update',{})
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
                that.$emit('closeIfNecessary',{})
                that.$refs.taskEndVisible.doClose()
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
    },
    editInvitedMembers() {
      let value = this.invitedMembers
      if(value != null) {
        const that = this
        axios.post(
            '/task/assigntask',
            {
              taskId:that.singleTaskData.taskId,
              usernames:value
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  message:'分配任务成功',
                  type:'success'
                })
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              } else {
                that.$message.error('分配任务失败')
              }
            },
            function(err) {
              that.$message.error('响应失败，分配任务失败')
            }
        )
      }
      this.invitedMembers = null
    },
    editDescription() {
      let value = this.editedDescription
      if(value != null) {
        this.tempTaskForm.description = this.editedDescription
        const that = this
        axios.post(
            '/task/edit/description',
            {
              description:that.tempTaskForm.description,
              taskId:that.tempTaskForm.taskId
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$forceUpdate()
                that.$emit('update',{})
                that.$refs.taskDescriptionVisible.doClose()
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
      this.editedDescription = null
    },
    editRemindDate() {
      let value = this.editedRemindDate
      if(value != null) {
        this.tempTaskForm.remindDate = value
        const that = this
        axios.post(
            '/task/edit/reminddate',
            {
              dateTime:that.tempTaskForm.remindDate,
              taskId:that.tempTaskForm.taskId
            },
            {
              headers:{
                Authorization:window.sessionStorage.getItem('token')
              }
            }
        ).then(
            function(response) {
              if(response.data.code === 200) {
                that.$message({
                  type:'success',
                  message:'修改成功'
                })
                that.$forceUpdate()
                that.$emit('update',{})
                that.$refs.taskRemindVisible.doClose()
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
                that.editedStartTime = null
              } else {
                that.$message.error('修改失败')
                let newToken = response.headers.authorization
                if(newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function(err) {
              that.$message.error('响应失败，修改失败')
            }
        )
      }
    },
    addSubTask() {
      const that = this
      let value = this.subTaskName
      if(value != null && !(value === '')) {
        this.subTasksList.push(value)
      } else {
        that.$message.error('要添加的子任务名不能为空')
      }
      this.subTaskName = null
    },
    postEdit() {
      let that = this
      axios({
        method:'POST',
        url:'/task/edittask',
        data:{
          createDate:that.tempTaskForm.createDate,
          description:that.tempTaskForm.description,
          dueDate:that.tempTaskForm.dueDate,
          //fatherTask
          members:that.tempTaskForm.members,
          privilege:that.tempTaskForm.privilege,
          status:that.tempTaskForm.status === 2 ? that.tempTaskForm.status - 1 : that.tempTaskForm.status,
          subTasks:that.subTasksList,
          tags:that.tempTaskForm.tags,
          taskName:that.tempTaskForm.taskName,
          type:that.tempTaskForm.type,
          taskId:that.tempTaskForm.taskId,
          location:that.tempTaskForm.location
        },
        headers:{
          Authorization:window.sessionStorage.getItem('token')
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
            if(newToken != null) window.sessionStorage.setItem('token', newToken)
          //  重置数据
            that.clearTaskForm()
          } else {
            that.$message.error('修改任务失败')
            let newToken = response.headers.authorization
            if(newToken != null) window.sessionStorage.setItem('token', newToken)
          }
          that.$emit('closeTaskDrawer',{})
        },
        function(err) {
          that.$message.error('响应失败,修改任务失败')
        }
      )

    },
    closeDrawer() {
      let that = this
      axios({
        method:'POST',
        url:'/task/deletetask',
        params:{id:that.tempTaskForm.taskId},
        headers:{
          Authorization:window.sessionStorage.getItem('token')
        }
      }).then(
          function(response) {
            //alert(response.data.msg)
            if(response.data.code === 200) {
              that.$message({
                message:'删除任务成功',
                type:'success'
              })
              that.$emit('closeTaskDrawer',{})
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            //  重置数据
              that.clearTaskForm()
            } else {
              that.$message.error('删除任务失败')
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function(err) {
            that.$message.error('响应失败，删除任务失败')
          }
      )
    },
    emitTreeData() {
      this.$emit('emitTreeData',this.singleTaskData)
    },
    // 用于提交或者删除表单后，清空表单
    clearTaskForm() {
      //清空就是重新加载表单
      this.tempTaskForm = JSON.parse(JSON.stringify(this.singleTaskData))
      this.subTasksList = []
    },
  //  用于将标准日期字符串转换成用户方便阅读的时间格式
    convertTime(time) {
      if (time === null) return '未设定'
      let date = new Date(time)
      return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日' + date.getHours() + '时' + date.getMinutes() + '分' + date.getSeconds() + '秒'
    },
    // 从list1中踢出去list2的元素返回的元素
    CU(list1, list2) {
      for(let i in list2) {
        let item = list2[i]
        let index = list1.indexOf(item)
        if(index > -1) {
          list1.splice(index, 1)
        }
      }
      return list1
    },
    getMemberList() {
      let members = [];
      // 创建者
      let creator = {};
      creator.name = this.singleTeamData.creator
      creator.role = 'creator'
      members.push(creator)
      // 管理员
      for (let i in this.singleTeamData.admins) {
        let admin = {}
        admin.name = this.singleTeamData.admins[i]
        admin.role = 'admin'
        members.push(admin)
      }
      // 普通成员
      for (let i in this.singleTeamData.members) {
        let member = {}
        member.name = this.singleTeamData.members[i]
        member.role = 'member'
        members.push(member)
      }

      this.memberList = members
    },
    whenMouseIn(name) {
      this.mousein = name
    },
    whenMouseOut() {
      this.mousein = null
    },
    deleteMember(name) {
      let that = this
      let userName = []
      userName.push(name)
      let teamId = this.singleTeamData.teamId
      //TODO:后端取消分配的接口
      axios.post(
          '/team/removemember',
          {
            teamId: teamId,
            //这个是数组，看情况改
            userName: userName
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: '取消分配成功',
                type: 'success'
              })
              that.$emit('postTeamInfoAgain', {})
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('取消分配失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，取消分配失败')
          }
      )
    },
  }
  
}
</script>

<style scoped>
.title {
  font-weight: bold;
  font-size: 30px;
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