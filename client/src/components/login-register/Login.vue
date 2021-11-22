<template>

  <div id="login">

    <el-row type="flex" justify="center" id="login_container_pos">
      <el-col :span="24">
<!--        登录窗口-->
        <div v-show="login" class="animate__animated animate__zoomIn">
          <LoginContainer></LoginContainer>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
//登录窗口
import LoginContainer from './LoginContainer'
export default {

  name: "Login",
  components: {
    LoginContainer,
  },
  data() {
    return {
      // 登录窗口是否显示
      login:true
    }
  },
  methods:{
    //控制登录窗口显示，暂时用不到
    setLogin() {
      this.login = !this.login
    }
  }

}
</script>

<style scoped>
#login {
  /*登录页面背景图*/
  background: url("../../assets/login.png");
  /*登录页面占满*/
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

#login_container_pos {
  position: relative;
  top: 10%;
}

#title{
    color: #fff;
    text-shadow:  0px 0px 10px black;
}

</style>
