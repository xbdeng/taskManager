<template>
<!-- 这个页面是一个模板页面，用来向用户显示某个组的信息 -->
  <div>
    <template>
      <el-container>
          <!-- 头部栏显示组名称，并留有接口供用户修改 -->
          <el-header>
            <el-row :gutter="10" type="flex" align="middle">
              <el-col :span="3">
                <span class="title">{{ singleTeamData.teamName }}</span>
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
                      {{ singleTeamData.createDate }}
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
                  <!-- 设置管理员的接口 -->
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
                  <el-col>{{ singleTeamData.description }}</el-col>
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
                      <el-button type="primary" @click="postEdit">确定</el-button>
                  </el-col>
                  <el-col>
                      <el-button type="danger">退出该组</el-button>
                  </el-col>
                  <el-col>
                      <el-button type="warning">解散该组</el-button>
                  </el-col>
              </el-row>
          </el-main>
      </el-container>
    </template>
  </div>
</template>
 
<script>

export default {

  name: 'TeamShow',
  // 这个组件接收的参数是一个组对象
  props: ['singleTeamData'],
  data() {
      // 生成普通成员的穿梭框信息
      const generateTransferData = _ => {
          const data = []
          for(let i in this.singleTeamData.members) {
              data.push({
                  key:this.singleTeamData.members[i],
                  value:this.singleTeamData.members[i]
              });
          }
          return data
      };
    // 生成好友列表信息，显示在穿梭框中
    const generateFriendData = _ => {
        const data = []
          for(let i in this.singleTeamData.members) {
              data.push({
                  key:this.singleTeamData.members[i],
                  value:this.singleTeamData.members[i]
              });
          }
          return data
    }
      return {
          memberList:[],
          transferData:generateTransferData(),
          friends:generateFriendData(),
          editedAdmins:[],
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
          creator.name = this.singleTeamData.creator
          creator.role = 'creator'
          members.push(creator)
          // 管理员
          for(let i in this.singleTeamData.admins) {
              let admin = new Object()
              admin.name = this.singleTeamData.admins[i]
              admin.role = 'admin'
              members.push(admin)
          }
            // 普通成员
          for(let i in this.singleTeamData.members) {
              let member = new Object()
              member.name = this.singleTeamData.members[i]
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
          this.tempTeamInfo.members.splice(this.teapTeamInfo.indexOf(name), 1)
      },
    //   将修改后的editedTeamName添加到暂存区
      editTeamName() {
          let value = this.editedTeamName
          if(value != null) {
              this.tempTeamInfo.teamName = value
          }
          this.editedTeamName = null
      },
    //   将修改后的editedAdmins添加到暂存区
      editAdmins() {
          let value = this.editedAdmins
          if(value != null) {
              this.tempTeamInfo.admins.concat(value)
          }
          this.editedAdmins = null
      },
    //   将邀请的成员添加到暂存区
      editInvitedMembers() {
          let value = this.invitedMembers
          if(value != null) {
              this.tempTeamInfo.members.concat(value)
          }
          this.invitedMembers = null
      },
    //   将修改后的任务描述信息添加到暂存区
      editDescription() {
          let value = this.editedTeamDescription
          if(value != null) {
              this.tempTeamInfo.description = value
          }
          this.editedTeamDescription = null
      },
    postEdit() {
        // 向后端提交修改后的组的信息
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