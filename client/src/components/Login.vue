<template>

  <div id="login">
    <el-row type="flex" justify="center" id="login_container_pos">
      <el-col :span="12">
        <LoginContainer></LoginContainer>
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
  }

}
</script>

<style scoped>

#login {
  background: url("../assets/login_background.gif");
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

#login_container_pos {
  position: relative;
  top: 10%;
}

</style>
