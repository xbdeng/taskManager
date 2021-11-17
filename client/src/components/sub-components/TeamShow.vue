<template>
<!-- 这个页面是一个模板页面，用来向用户显示某个组的信息 -->
  <div>
    <template>
      <el-container>
          <!-- 头部栏显示组名称，并留有接口供用户修改 -->
          <el-header>
            <el-row :gutter="10" type="flex" align="middle">
              <el-col :span="3">
                <span class="title">{{ this.tempTeamInfo.teamName }}</span>
              </el-col>

              <el-popover placement="top" width="200" trigger="click" title="修改组名">
                  <el-row>
                      <el-col>
                          <el-input placeholder="请输入修改后的组名..." clearable v-model="editedTeamName"></el-input>
                      </el-col>
                  </el-row>
                  <el-row>
                      <el-col :offset="8">
                          <el-button type='primary' plain size="small" @click="editTeamName">确定</el-button>
                      </el-col>
                  </el-row>
                    <el-tooltip content="点击可修改组的名称" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>
            </el-row>
          </el-header>
          <el-main>
              <!-- 显示创建时间，没有接口修改 -->
              <el-row type="flex" justify="start">
                  <el-col :span="4">
                      <span style="font-weight:bold">创建时间：</span>
                  </el-col>
                  <el-col>
                      {{ this.tempTeamInfo.createDate }}
                  </el-col>
              </el-row>
            <!-- 显示组内成员，留接口修改 -->
              <el-row type="flex" justify="start">
                  <!-- 显示组内成员的组件 -->
                  <el-col :span="4">
                      <span style="font-weight:bold">组内成员：</span>
                  </el-col>
                  <!-- 下拉菜单 -->
                  <el-col :span="7">
                    <el-dropdown trigger="click">
                        <el-button @click="getMemberList">
                            点击查看组内成员<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <!-- 普通成员 -->
                            <el-dropdown-item v-for="member in memberList" :key="member.name" @mouseover.native="whenMouseIn(member.name)" @mouseout.native="whenMouseOut" @click.native="deleteMember(member.name)">
                                <i class="el-icon-user-solid" v-if="member.role==='member'"></i>
                                <i class="el-icon-user-solid" v-if="member.role==='admin'" style="color:#00BFFF"></i>
                                <i class="el-icon-user-solid" v-if="member.role==='creator'" style="color:red"></i>
                                {{ member.name }}
                                <i class="el-icon-remove" v-if="member.name===mousein"></i>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                  </el-col>
                  <!-- 添加管理员的接口 -->
                    <el-col :span="7">
                        <el-popover placement="top" width="900" trigger="click" title="设置管理员">
                            <el-row>
                                <el-col>
                                    <el-transfer :data="transferData" filterable :button-texts="['取消管理员权限','设置成为管理员']" v-model="editedAdmins" :titles="['普通成员','管理员']"></el-transfer>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :offset="8">
                                    <el-button type='primary' @click="editAdmins">确定</el-button>
                                    <!-- TODO:点击取消后的动作 -->
                                    <el-button type="danger">取消</el-button>
                                </el-col>
                            </el-row>
                                <el-tooltip content="点击可设置管理员" slot="reference">
                                <el-link type='primary'>
                                    <i class="el-icon-edit"></i>
                                </el-link>
                                </el-tooltip>
                        </el-popover>
                    </el-col>
                    <!-- 取消管理员的接口 -->
                    <el-col :span="7">
                        <el-popover placement="top" width="900" trigger="click" title="删除管理员">
                            <el-row>
                                <el-col>
                                    <el-transfer :data="adminTransferData" filterable :button-texts="['重新设置成管理员','取消管理员权限']" v-model="removedAdmins" :titles="['管理员','取消管理员名单']"></el-transfer>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :offset="8">
                                    <el-button type='primary' @click="removeAdmins">确定</el-button>
                                    <!-- TODO:点击取消后的动作 -->
                                    <el-button type="danger">取消</el-button>
                                </el-col>
                            </el-row>
                                <el-tooltip content="点击可撤销管理员" slot="reference">
                                <el-link type='primary'>
                                    <i class="el-icon-edit"></i>
                                </el-link>
                                </el-tooltip>
                        </el-popover>
                    </el-col>
                  <!-- 添加成员的接口 -->
                  <el-col :span="3">
                      <el-popover placement="top" width="900" trigger="click" title="邀请成员">
                        <el-row>
                            <el-col>
                                <el-transfer :data="friends" filterable :button-texts="['取消邀请','邀请进组']" v-model="invitedMembers" :titles="['我的好友','邀请名单']"></el-transfer>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :offset="8">
                                <el-button type='primary' @click="editInvitedMembers">确定</el-button>
                                <!-- TODO:点击取消后的动作 -->
                                <el-button type="danger">取消</el-button>
                            </el-col>
                        </el-row>
                            <el-tooltip content="点击可邀请成员进组" slot="reference">
                            <el-link type='primary'>
                                <i class="el-icon-plus"></i>
                            </el-link>
                            </el-tooltip>
                        </el-popover>
                  </el-col>
              </el-row>
              <!-- 显示任务描述信息 -->
              <el-row type="flex" justify="start">
                  <el-col :span="6">
                      <span style="font-weight:bold">任务描述信息：</span>
                  </el-col>
                  <el-col>{{ tempTeamInfo.description }}</el-col>
                  <el-popover placement="bottom" width="200" trigger="click" title="修改组的描述信息">
                    <el-row>
                        <el-col>
                            <el-input placeholder="请输入修改后的描述信息..." type="textarea" :rows="4" v-model="editedTeamDescription"></el-input>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :offset="8">
                            <el-button type='primary' plain size="small" @click="editDescription">确定</el-button>
                        </el-col>
                    </el-row>
                    <el-tooltip content="点击可修改组的描述信息" slot="reference">
                      <el-link type='primary'>
                        <i class="el-icon-edit"></i>
                      </el-link>
                    </el-tooltip>
                </el-popover>
              </el-row>
              <!-- 确定， 退出该组，解散该组 -->
              <!-- TODO:点击按钮后执行的动作 -->
              <el-row type="flex" justify="start">
                  <el-col>
                      <el-button type="primary" @click="closeTeamDrawer">确定</el-button>
                  </el-col>
                  <el-col>
                      <el-button type="danger" @click="removeMe">退出该组</el-button>
                  </el-col>
                  <el-col>
                      <el-button type="warning" @click="dismissTeam">解散该组</el-button>
                  </el-col>
              </el-row>
          </el-main>
      </el-container>
    </template>
  </div>
</template>
 
<script>
import axios from 'axios'
export default {
  name: 'TeamShow',
  // 这个组件接收的参数是一个组对象
  props: ['singleTeamData','username','Friends'],
  data() {
      // 生成普通成员的穿梭框信息
      const generateTransferData = _ => {
          const data = []
          for(let i in this.tempTeamInfo.members) {
              data.push({
                  key:this.tempTeamInfo.members[i],
                  value:this.tempTeamInfo.members[i]
              });
          }
          return data
      };
    // 生成好友列表信息，显示在穿梭框中
    const generateFriendData = _ => {
        const data = []
          for(let i in this.Friends) {
              data.push({
                  key:this.Friends[i],
                  value:this.Friends[i]
              });
          }
          return data
    };
    // 生成管理员列表，显示在穿梭框中
     const generateAdminData = _ => {
        const data = []
          for(let i in this.tempTeamInfo.admins) {
              data.push({
                  key:this.tempTeamInfo.admins[i],
                  value:this.tempTeamInfo.admins[i]
              });
          }
          return data
    };
      return {
          memberList:[],
          transferData:generateTransferData(),
          adminTransferData:generateAdminData(),
          friends:generateFriendData(),
          editedAdmins:[],
          removedAdmins:[],
          invitedMembers:[],
          mousein:null,
          editedTeamName:null,
          editedTeamDescription:null,
          tempTeamInfo:JSON.parse(JSON.stringify(this.singleTeamData))
      }
  },
  methods:{
      getMemberList() {
          let members = [];
          // 创建者
          let creator = new Object();
          creator.name = this.tempTeamInfo.creator
          creator.role = 'creator'
          members.push(creator)
          // 管理员
          for(let i in this.tempTeamInfo.admins) {
              let admin = new Object()
              admin.name = this.tempTeamInfo.admins[i]
              admin.role = 'admin'
              members.push(admin)
          }
            // 普通成员
          for(let i in this.tempTeamInfo.members) {
              let member = new Object()
              member.name = this.tempTeamInfo.members[i]
              member.role = 'member'
              members.push(member)
          }
          
          this.memberList = members
      },
    // 鼠标移进，显示鼠标所在的成员的名称
      whenMouseIn(name) {
          this.mousein = name
      },
    //   鼠标移出，成员置空
      whenMouseOut() {
          this.mousein = null
      },
    //   点击成员中的某人，在暂存区中删除这个成员
      deleteMember(name) {
          this.tempTeamInfo.members.splice(this.tempTeamInfo.indexOf(name), 1)
          let that = this
          let userName = []
          userName.push(name)
          axios.post(
              'http://localhost:8081/api/team/removemember',
              {
                  teamId:that.tempTeamInfo.teamId,
                  userName:userName
              },
              {
                headers:{
                    Authorization:window.localStorage.getItem('token')
                }
              }
          ).then(
              function(response) {
                  alert(response.msg)
              },
              function(err) {
                  that.$message.error()
              }
          )
      },
    //   将修改后的editedTeamName添加到暂存区
      editTeamName() {
          let value = this.editedTeamName
          if(value != null) {
              this.tempTeamInfo.teamName = value
          }
          this.editedTeamName = null
      },
      editAdmins() {
          let value = this.editedAdmins
          let that = this
          if(value != null) {
              axios.post(
                  'http://localhost:8081/api/team/setadmin',
                  {
                      teamId:that.tempTeamInfo.teamId,
                      userName:value
                  },
                  {
                    headers:{
                        Authorization:window.localStorage.getItem('token')
                    }
                  }
              ).then(
                  function(response) {
                      alert(response.data.msg)
                  },
                  function(err) {
                      that.$message.error('响应失败，添加管理员失败')
                  }
              )
          }
          this.editedAdmins = null
      },
      editInvitedMembers() {
          let that = this
          let value = this.invitedMembers
          if(value != null) {
              axios.post(
                  'http://localhost:8081/api/team/addmember',
                  {
                      teamId:that.tempTeamInfo.teamId,
                      userName:value
                  },
                  {
                    headers:{
                        Authorization:window.localStorage.getItem('token')
                    }
                  }
              ).then(
                  function(response) {
                      alert(response.data.msg)
                  },
                  function(err) {
                      that.$message.error('响应失败,邀请成员失败')
                  }
              )
          }
          this.invitedMembers = null
      },
      editDescription() {
          let value = this.editedTeamDescription
          if(value != null) {
              this.tempTeamInfo.description = value
          }
          this.editedTeamDescription = null
      },
      dismissTeam() {
          let that = this
          axios.post(
              'http://localhost:8081/api/team/dismiss',
              {
                  teamId:that.tempTeamInfo.teamId
              },
              {
                headers:{
                    Authorization:window.localStorage.getItem('token')
                }
              }
          ).then(
              function(response) {
                  alert(response.data.msg)
              },
              function(err) {
                  that.$message.error('响应失败,解散该组失败')
              }
          )
      },
      removeMe() {
          let that = this
          axios.post(
              'http://localhost:8081/api/team/removemember',
              {
                  teamId:that.tempTeamInfo.teamId,
                  userName:that.username
              },
              {
                headers:{
                    Authorization:window.localStorage.getItem('token')
                }
              }
          ).then(
              function(response) {
                  alert(response.data.msg)
              },
              function(err) {
                  that.$message.error('响应失败，退出组失败')
              }
          )
      },
      removeAdmins() {
          let value = this.removedAdmins
          if(value != null) {
              let that = this
              axios.post(
                  'http://localhost:8081/api/team/removeadmin',
                  {
                      teamId:that.tempTeamInfo.teamId,
                      userName:value
                  },
                  {
                    headers:{
                        Authorization:window.localStorage.getItem('token')
                    }
                  }
              ).then(
                  function(response) {
                      alert(response.data.msg)
                  },
                  function(err) {
                      that.$message.error('响应失败，撤销管理员失败')
                  }
              )
          }
          this.removedAdmins = null
      },
      closeTeamDrawer() {
          this.$emit('closeTeamDrawer',{})
      }
    
  }
  
}
</script>

<style scoped>
.title {
  font-weight: bold;
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

.zZindex {

}
</style>