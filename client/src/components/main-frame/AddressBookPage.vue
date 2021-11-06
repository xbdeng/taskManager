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
                    <el-button type='primary' icon='el-icon-plus' circle ></el-button>
                </el-col>
            </el-row>
        </el-header>
        <!-- 通讯录主页面 -->
        <el-container>
            <!-- 侧边栏，用于显示所有好友 -->
            <el-aside class='address_aside'>
                <el-menu >
                    <el-menu-item v-for="(friend, index) in Friends" :key="index" @click="changeSelectedFriend(index)">
                        {{ friend.name }}
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
export default {

  name: "AddressBookPage",
  components: {
      FriendShow
  },
  data() {
      return {
          selectedFriend:0,
          Friends:[
              {
                  name:'Lee',
                  email:'11913003@mail.sustech.edu.cn',
                  telephone:'19861251006',
                  personalSign:'Eclipse first, the rest nowhere'
              },
              {
                  name:'Mike',
                  email:'chlijiaao@163.com',
                  telephone:'10086',
                  personalSign:"躺平不香吗?"
              },
              {
                  name:'Smith',
                  email:'2381446488@qq.com',
                  telephone:'120',
                  personalSign:"我是个傻逼"
              },
              {
                  name:'Jack',
                  email:'ababa@mail.sustech.edu.cn',
                  telephone:'119',
                  personalSign:"哥只是个传说"
              }
          ]
      }
  },
  methods:{
      changeSelectedFriend(index) {
          this.selectedFriend = index
      }
  }

}
</script>

<style scoped>

.address_header{
    /* background: aquamarine; */
    background-size: 100% 100%;
    padding: 0%;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.address_aside{
    /* background: darkorange; */
    background-size: 100% 100%;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}
.address_main{
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
