<template>
  <!-- 这个页面是一个模板页面，用来向用户显示某个组的信息 -->
  <div>
    <template>
      <el-container>
        <!-- 头部栏显示组名称，并留有接口供用户修改 -->
        <el-header>
          <el-row :gutter="10" type="flex" align="middle">
            <el-col :span="4">
            <el-popover placement="top" width="200" trigger="click" title="修改组名" ref="teamNameVisible">
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
                <el-link>
                  <span class="title">{{ this.singleTeamData.teamName }}</span>
                </el-link>
              </el-tooltip>
            </el-popover>
              </el-col>
          </el-row>
        </el-header>
        <el-main>
          <el-row :gutter="40">
            <el-col :span="18">
              <!-- 显示创建时间，没有接口修改 -->
              <el-row type="flex" justify="start" align="middle">
                <el-col :span="7">
                  <span style="font-weight:bold;font-size: 18px">创建时间: </span>
                </el-col>
                <el-col :span="17">
                  {{ this.convertTime(this.singleTeamData.creatTime) }}
                </el-col>
              </el-row>
              <!-- 显示组内成员，留接口修改 -->
              <el-row type="flex" justify="start" align="middle">
                <!-- 显示组内成员的组件 -->
                <el-col :span="8">
                  <span style="font-weight:bold;font-size: 18px">组内成员：</span>
                </el-col>
                <!-- 下拉菜单 -->
                <el-col :span="7">
                  <el-dropdown trigger="click">
                    <el-button @click="getMemberList">
                      点击查看组内成员<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <!-- 普通成员 -->
                      <el-dropdown-item v-for="member in memberList" :key="member.name"
                                        @mouseover.native="whenMouseIn(member.name)" @mouseout.native="whenMouseOut"
                                        @click.native="deleteMember(member.name)">
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
                <el-col :span="4">
                  <el-popover placement="top" width="900" trigger="click" title="设置管理员">
                    <el-row>
                      <el-col>
                        <el-transfer :data="transferData" filterable :button-texts="['取消管理员权限','设置成为管理员']"
                                     v-model="editedAdmins" :titles="['普通成员','管理员']"></el-transfer>
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
                <el-col :span="3">
                  <el-popover placement="top" width="900" trigger="click" title="删除管理员">
                    <el-row>
                      <el-col>
                        <el-transfer :data="adminTransferData" filterable :button-texts="['重新设置成管理员','取消管理员权限']"
                                     v-model="removedAdmins" :titles="['管理员','取消管理员名单']"></el-transfer>
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
                <el-col :span="4">
                  <el-popover placement="top" width="900" trigger="click" title="邀请成员">
                    <el-row>
                      <el-col>
<!--                        好友中不在这个群里的人-->
                        <el-transfer :data="this.friends" filterable :button-texts="['取消邀请','邀请进组']"
                                     v-model="invitedMembers" :titles="['我的好友','邀请名单']"></el-transfer>
                      </el-col>
                    </el-row>
                    <el-row>
                      <el-col :offset="8">
                        <el-button type='primary' @click="editInvitedMembers">确定</el-button>
<!--                        &lt;!&ndash; TODO:点击取消后的动作 &ndash;&gt;-->
<!--                        <el-button type="danger">取消</el-button>-->
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
              <el-row type="flex" justify="start" align="middle">
                <el-col :span="8">
                  <span style="font-weight:bold;font-size: 18px">队伍描述信息：</span>
                </el-col>
                <el-col>
                <el-popover placement="bottom" width="200" trigger="click" title="修改组的描述信息" ref="teamDescriptionVisible">
                  <el-row>
                    <el-col>
                      <el-input placeholder="请输入修改后的描述信息..." type="textarea" :rows="4"
                                v-model="editedTeamDescription"></el-input>
                    </el-col>
                  </el-row>
                  <el-row>
                    <el-col :offset="8">
                      <el-button type='primary' plain size="small" @click="editDescription">确定</el-button>
                    </el-col>
                  </el-row>

                  <el-tooltip content="点击可修改组的描述信息" slot="reference">
                    <el-link>
                      {{ singleTeamData.description }}
                    </el-link>
                  </el-tooltip>
                </el-popover>
                </el-col>
              </el-row>
              <el-row type="flex" justify="start" align="middle" :gutter="20">
                  <el-col>
                    <el-card>
                      <div>
                        <el-progress type="circle" :percentage="25"></el-progress>
                      </div>
                      <div>
                        team ,,,
                      </div>
                    </el-card>
                  </el-col>
                  <el-col>
                    <el-card>
                      <div>
                        <el-progress :text-inside="true" :stroke-width="26" :percentage="70"></el-progress>
                      </div>
                      <div>
                        done task
                      </div>
                    </el-card>
                  </el-col>
              </el-row>
              <el-row type="flex" justify="start" align="middle">
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
  name: 'TeamShow',
  // 这个组件接收的参数是一个组对象
  props: ['singleTeamData', 'username', 'Friends'],

  data() {
    // 生成普通成员的穿梭框信息
    const generateTransferData = _ => {
      const data = []
      for (let i in this.singleTeamData.members) {
        data.push({
          key: this.singleTeamData.members[i],
          label: this.singleTeamData.members[i]
        });
      }
      return data
    };
    // 生成好友列表信息，显示在穿梭框中
    const generateFriendData = _ => {
      const data = []
      for (let i in this.Friends) {
        let username = this.Friends[i].username
        if(!(username === this.singleTeamData.creator) && !(this.singleTeamData.admins.indexOf(username) > -1) && !(this.singleTeamData.members.indexOf(username) > -1))
        data.push({
          key: this.Friends[i].username,
          label: this.Friends[i].username
        });
      }
      return data
    };
    // 生成管理员列表，显示在穿梭框中
    const generateAdminData = _ => {
      const data = []
      for (let i in this.singleTeamData.admins) {
        data.push({
          key: this.singleTeamData.admins[i],
          label: this.singleTeamData.admins[i]
        });
      }
      return data
    };
    return {
      memberList: [],
      transferData: generateTransferData(),
      adminTransferData: generateAdminData(),
      friends: generateFriendData(),
      editedAdmins: [],
      removedAdmins: [],
      invitedMembers: [],
      mousein: null,
      editedTeamName: null,
      editedTeamDescription: null,
    }
  },
  methods: {
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
      axios.post(
          '/team/removemember',
          {
            teamId: teamId,
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
                message: '删除组员成功',
                type: 'success'
              })
              that.$emit('postTeamInfoAgain', {})
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('删除组员失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，删除组员失败')
          }
      )
    },
    editTeamName() {
      const that = this
      let teamId = this.singleTeamData.teamId
      axios.post(
    '/team/editteam',
        {
          teamName: that.editedTeamName,
          teamId: teamId,
          description:that.singleTeamData.description
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
                message: '修改组名成功',
                type: 'success'
              })
              that.editedTeamName = null
              that.$refs.teamNameVisible.doClose()
              that.$emit('postTeamInfoAgain', {})
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('修改组名失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，修改组名失败')
          }
      )
    },
    editAdmins() {
      let value = this.editedAdmins
      const that = this
      let teamId = this.singleTeamData.teamId
      if (value != null) {
        axios.post(
            '/team/setadmin',
            {
              teamId: teamId,
              userName: value
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
                  message: '添加管理员成功',
                  type: 'success'
                })
                that.editedAdmins = []
                that.$emit('postTeamInfoAgain', {})
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
              } else {
                that.$message.error('添加管理员失败')
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function (err) {
              that.$message.error('响应失败，添加管理员失败')
            }
        )
      }
      this.editedAdmins = null
    },
    editInvitedMembers() {
      const that = this
      let value = this.invitedMembers
      let teamId = this.singleTeamData.teamId
      if (value != null) {
        axios.post(
            '/message/sendrequest',
            {
              teamId: teamId,
              usernameTo: value,
              type: 0
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
                  message: '发送邀请成功',
                  type: 'success'
                })
                that.$emit('postTeamInfoAgain', {})
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
              } else {
                if (response.data.code === 201)
                  that.$message.error('发送邀请失败')
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function (err) {
              that.$message.error('响应失败,发送邀请失败')
            }
        )
      }
    },
    editDescription() {
      const that = this
      let teamId = this.singleTeamData.teamId
      axios.post(
          'team/editteam',
          {
            description: that.editedTeamDescription,
            teamId: teamId,
            teamName:that.singleTeamData.teamName
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
                message: '修改队伍描述信息成功',
                type: 'success'
              })
              that.editedDescription = null
              that.$emit('postTeamInfoAgain', {})
              that.$refs.teamDescriptionVisible.doClose()
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('修改队伍描述信息失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，修改队伍描述信息失败')
          }
      )
    },
    dismissTeam() {
      let that = this
      let teamId = this.singleTeamData.teamId
      axios.post(
          '/team/dismiss',
          {
            teamId: teamId
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
                message: '解散组成功',
                type: 'success'
              })
              that.closeTeamDrawer()
              that.$emit('postTeamInfoAgain', {})
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('解散组失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败,解散该组失败')
          }
      )
    },
    removeMe() {
      let that = this
      let teamId = this.singleTeamData.teamId
      axios.post(
          '/team/withdraw',
          {
            teamId: teamId,
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
                message: '退出组成功',
                type: 'success'
              })
              that.closeTeamDrawer()
              that.$emit('postTeamInfoAgain', {})
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            } else {
              that.$message.error('退出组失败')
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message.error('响应失败，退出组失败')
          }
      )
    },
    removeAdmins() {
      const that = this
      let value = this.removedAdmins
      let teamId = this.singleTeamData.teamId
      if (value != null) {
        axios.post(
            '/team/removeadmin',
            {
              teamId: teamId,
              userName: value
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
                  message: '撤销管理员成功',
                  type: 'success'
                })
                that.removedAdmins = null
                that.$emit('postTeamInfoAgain', {})
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
              } else {
                that.$message.error('撤销管理员失败')
                let newToken = response.headers.authorization
                if (newToken != null) window.sessionStorage.setItem('token', newToken)
              }
            },
            function (err) {
              that.$message.error('响应失败，撤销管理员失败')
            }
        )
      }

    },
    closeTeamDrawer() {
      this.$emit('closeTeamDrawer', {})
    },
    convertTime(time) {
      if (time === null) return '未设定'
      let date = new Date(time)
      return date.getFullYear() + '年' + (date.getMonth() + 1) + '月' + date.getDate() + '日' + date.getHours() + '时' + date.getMinutes() + '分' + date.getSeconds() + '秒'
    },
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
  }

}
</script>

<style scoped>
.title {
  font-weight: bold;
  font-size:30px;
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
