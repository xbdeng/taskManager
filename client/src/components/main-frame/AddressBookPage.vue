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
                                <el-input placeholder="请输入用户名"></el-input>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :offset="8">
                                <el-button type='primary' plain size="small">确定</el-button>
                            </el-col>
                        </el-row>
                        <el-tooltip content="点击可修改组的描述信息" slot="reference">
                            <el-button type='primary' icon='el-icon-plus' circle ></el-button>
                        </el-tooltip>
                    </el-popover>
                    
                </el-col>
            </el-row>
        </el-header>
        <!-- 通讯录主页面 -->
        <el-container>
            <!-- 侧边栏，用于显示所有好友 -->
            <el-aside class='address_aside'>
                <el-menu >
                    <el-menu-item v-for="(friend, index) in friends" :key="index" @click="changeSelectedFriend(index)">
                        {{ friend.username }}
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <!-- 主页面，用于显示好友信息 -->
            <el-main class='address_main'>
                <FriendShow :friend="friends[this.selectedFriend]"></FriendShow>
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
  props:['friends'],
  data() {
      return {
          selectedFriend:0,
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
