<template>
  <!-- 这个页面用于显示通讯录 -->
  <div id="addressBook">
    <el-container>
      <!-- 头部栏，用于显示Friends和添加好友按钮 -->
      <el-header class='address_header'>
        <el-row type="flex" align="middle">
          <el-col :span='9' align="left" :offset="1">
            <span class='friends_title'>Friends</span>
          </el-col>
          <el-col :span="5" :offset="8">
            <!--            <el-popover placement="bottom" width="200" trigger="click" title="添加好友">-->
            <!--              <el-row>-->
            <!--                <el-col>-->
            <!--                  <el-input placeholder="请输入用户名" v-model="addFriend"></el-input>-->
            <!--                </el-col>-->
            <!--              </el-row>-->
            <!--              <el-row>-->
            <!--                <el-col :offset="8">-->
            <!--                  <el-button type='primary' plain size="small" @click="AddFriendRequest">确定</el-button>-->
            <!--                </el-col>-->
            <!--              </el-row>-->
            <!--              <el-tooltip content="点击可添加好友" slot="reference">-->
            <!--                &lt;!&ndash; TODO:点击添加好友 &ndash;&gt;-->
            <!--                <el-button type='primary' icon='el-icon-plus' circle></el-button>-->
            <!--              </el-tooltip>-->
            <!--            </el-popover>-->
            <el-row :gutter="10">
              <el-col :span="18">
                <el-input v-model="addFriend" placeholder="请输入用户名"></el-input>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" plain @click="SearchFriendM">添加好友</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-header>
      <!-- 通讯录主页面 -->
      <el-container>
        <!-- 侧边栏，用于显示所有好友 -->
<!--        <el-aside class='address_aside'>-->
        <el-main class='address_main'>

          <el-form id="card-back" style="text-transform: uppercase">
            <el-form-item >
              <el-row>
                <el-col :span="4">
                  username
                </el-col>
                <el-col :span="4">
                  firstName
                </el-col>
                <el-col :span="4">
                  lastName
                </el-col>
                <el-col :span="4">
                  email
                </el-col>
                <el-col :span="8">
                  Action
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>

          <el-menu id="card-back">
            <el-menu-item v-for="(friend, index) in Friends" :key="index" style="border-radius: 10px; width: 100%; font-weight: bold; font-size: 15px" id="card-text">
              <el-row>
                <el-col :span="4">
                  {{ friend.username }}
                </el-col>
                <el-col :span="4">
                  {{friend.firstName}}
                </el-col>
                <el-col :span="4">
                  {{friend.lastName}}
                </el-col>
                <el-col :span="4">
                  {{friend.email}}
                </el-col>
                <el-col :span="8">
                  <el-button type="danger" icon="el-icon-delete" @click="removeFriend(friend.username)">Delete</el-button>
                </el-col>
              </el-row>
            </el-menu-item>
          </el-menu>
        </el-main>
<!--        </el-aside>-->
        <!-- 主页面，用于显示好友信息 -->
<!--        <el-main class='address_main'>-->
<!--          <FriendShow :friend="Friends[this.selectedFriend]"-->
<!--                      v-if="Friends[this.selectedFriend] !== undefined"></FriendShow>-->
<!--        </el-main>-->

        <el-drawer
            title="Team Search Page"
            :visible.sync="searchFriend"
            direction="rtl"
            :before-close="handleFriendClose"
            :modal-append-to-body='false'
            size="25%">
          <div>
            <el-form>
              <el-form-item v-for="(data, index) in searchFriendList" :key="index">
                <el-card class="box-card">
                  <el-form-item>
                    <el-row :gutter="20">
                      <el-col :span="15">
                        {{ data.username }}
                      </el-col>
                      <el-col :span="9">
                        <el-button type="primary" plain @click="AddFriendRequest(data.username)">申请添加</el-button>
                      </el-col>
                    </el-row>
                  </el-form-item>
                  <el-form-item>
                    first Name:{{ data.firstName }}
                    <br>
                    Last Name:{{ data.lastName }}
                    <br>
                    email : {{data.email}}
                    <br>
                    phone : {{data.phone}}
                  </el-form-item>
                </el-card>
              </el-form-item>
            </el-form>
          </div>
        </el-drawer>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import FriendShow from '../sub-components/FriendShow.vue'
import axios from "axios";
//设置axios请求的baseURL
import process from "_shelljs@0.7.8@shelljs";
import websocket from "../sub-components/WebSocket";
import {storeRequest} from "../sub-components/cache";

axios.defaults.baseURL = process.env.API_ROOT
export default {
  name: "AddressBookPage",
  components: {
    FriendShow
  },
  //父组件：Main, 向该组件传递参数Friends，所有的好友数据
  props: ['Friends'],
  data() {
    return {
      //鼠标点击，选中的好友对象在Friends数组中的下标
      selectedFriend: 0,
      //添加好友时，在搜索框中输入的用户名
      addFriend: '',
      searchFriend: false,
      searchFriendList: []
    }
  },
  methods: {
    //点击某个好友，获取这个好友对象在Friends中的数组下标
    changeSelectedFriend(index) {
      this.selectedFriend = index
    },
    //发送加好友请求
    AddFriendRequest(username) {
      if (websocket.getStatus() === "连接已关闭" || websocket.getStatus() === "未连接") {
        storeRequest('/message/sendrequest', {usernameTo: [this.addFriend], type: 1})
        this.$message({
          message: 'Offline request, request cache',
          type: 'error'
        })
        return;
      }
      const that = this
      axios.post(
          '/message/sendrequest',
          {
            usernameTo: [username],
            type: 1
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
                message: 'send friend request success',
                type: 'success'
              })
            } else if (response.data.code === 201) {
              that.$message({
                message: 'Friend is in list!',
                type: 'error'
              })
            }
            let newToken = response.headers.authorization
            if (newToken != null) window.sessionStorage.setItem('token', newToken)
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    removeFriend(friendName) {
      if (websocket.getStatus() === "连接已关闭" || websocket.getStatus() === "未连接") {
        storeRequest('/api/user/removefriend', {username: friendName})
        this.$message({
          message: 'Offline request, request cache',
          type: 'error'
        })
        return
      }
      const that = this
      axios.post(
          'http://localhost:8081/api/user/removefriend',
          {
            username: friendName
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
                message: 'remove friend success',
                type: 'success'
              })
            }
            let newToken = response.headers.authorization
            if (newToken != null) window.sessionStorage.setItem('token', newToken)
            that.$emit("updateAddressBook")
            that.$forceUpdate()
          },
          function (err) {
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    handleFriendClose() {
      this.searchFriend = false
    },
    SearchFriendM(){
      if(this.addFriend === ''){
        this.$message({
          message: 'name cannot be null',
          type: 'error'
        })
        return
      }
      const that = this
      axios.post(
          '/user/searchfriend',
          {username: that.addFriend},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response){
            if(response.data.code === 200){
              that.$message({
                message: 'Search Friend success',
                type: 'success'
              })
              that.searchFriendList = response.data.data
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
              that.searchFriend = true
            }
            else {
              that.$message({
                message: 'unknown error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (res){
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    }
  }
}
</script>

<style scoped>

.address_header {
  /* background: aquamarine; */
  background-size: 100% 100%;
  padding: 0%;
  /*box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)*/
}

.address_aside {
  /* background: darkorange; */
  height: 100%;
  background-size: 100% 100%;
  /*box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)*/
}

.address_main {
  /* background: salmon; */
  background-size: 100% 100%;
}

.friends_title {
  font-size: 50px;
}

.find_friends_button {
  position: relative;
  top: 50%;
  transform: translateY(-50%);
}

.el-menu-item.is-active，.el-submenu .el-menu-item.is-active {
  background-color: red !important;
}

.el-menu-item.is-active:hover .el-submenu .el-menu-item:hover {
  background-color: rgb(179, 13, 13) !important;
}

.el-menu-item:hover {
  background-color: #5a9def;;
/ / border-right: 10 px solid #000;
}

#card-front,
#card-back {
  background-color: #498ee4;
  border-radius: 6px;
  padding: 20px 30px 0;
  box-sizing: border-box;
  font-size: 10px;
  letter-spacing: 1px;
  font-weight: 300;
  color: white;
  font-family: lato, 'helvetica-light',serif;
  position: relative;
}

#card-text {
  border: none;
  outline: none;
  /*background-color: #5a9def;*/
  padding: 0 10px;
  margin: 0 0 25px;
  color: white;
  font-size: 10px;
  box-sizing: border-box;
  border-radius: 4px;
  font-family: lato, 'helvetica-light', 'sans-serif';
  letter-spacing: .7px;
}
</style>
