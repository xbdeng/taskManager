<template>
  <!-- 这个页面用于显示通讯录 -->
  <div id="addressBook">
    <el-container>
      <!-- 头部栏，用于显示Friends和添加好友按钮 -->
      <el-header class='address_header'>
        <el-row type="flex" align="middle">
          <el-col :span='9'>
            <span class='friends_title'>Friends</span>
          </el-col>
          <el-col :span="5" :offset="18">
            <el-popover placement="bottom" width="200" trigger="click" title="添加好友">
              <el-row>
                <el-col>
                  <el-input placeholder="请输入用户名" v-model="addFriend"></el-input>
                </el-col>
              </el-row>
              <el-row>
                <el-col :offset="8">
                  <el-button type='primary' plain size="small" @click="AddFriendRequest">确定</el-button>
                </el-col>
              </el-row>
              <el-tooltip content="点击可修改组的描述信息" slot="reference">
                <!-- TODO:点击添加好友 -->
                <el-button type='primary' icon='el-icon-plus' circle></el-button>
              </el-tooltip>
            </el-popover>

          </el-col>
        </el-row>
      </el-header>
      <!-- 通讯录主页面 -->
      <el-container>
        <!-- 侧边栏，用于显示所有好友 -->
        <el-aside class='address_aside'>
          <el-menu>
            <el-menu-item v-for="(friend, index) in Friends" :key="index" @click="changeSelectedFriend(index)">
              {{ friend.username }}
              <el-button type="danger" icon="el-icon-delete" circle @click="removeFriend"></el-button>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <!-- 主页面，用于显示好友信息 -->
        <el-main class='address_main'>
          <FriendShow :friend="Friends[this.selectedFriend]"></FriendShow>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import FriendShow from '../sub-components/FriendShow.vue'
import axios from "axios";
//设置axios请求的baseURL
import process from "_shelljs@0.7.8@shelljs";

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
      addFriend: ''
    }
  },
  methods: {
    //点击某个好友，获取这个好友对象在Friends中的数组下标
    changeSelectedFriend(index) {
      this.selectedFriend = index
    },
    //发送加好友请求
    AddFriendRequest(friendName) {
      const that = this
      axios.post(
          '/message/sendrequest',
          {
            usernameTo: friendName,
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
            that.$forceUpdate()
          },
          function (err) {
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

.address_aside {
  /* background: darkorange; */
  background-size: 100% 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
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

</style>
