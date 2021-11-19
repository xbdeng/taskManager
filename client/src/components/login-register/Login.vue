<template>

  <div id="login">
    <el-row type="flex" justify="center" id="login_container_pos">
      <el-col :span="12">
          <el-collapse-transition>
            <div v-show="login" class="animate__animated animate__zoomIn">
              <LoginContainer></LoginContainer>
            </div>
          </el-collapse-transition>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import LoginContainer from './LoginContainer'

export default {

  name: "Login",
  components: {
    LoginContainer,
  },
  data() {
    return {
      login:true
    }
  },
  methods:{
    setLogin() {
      this.login = !this.login
    }
  }

}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Reggae+One&display=swap');
#login {
  background: url("../../assets/login.png");
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
