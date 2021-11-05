<template>
  <div id="profile">
    <el-row  type="flex"  justify="center" id="profile_container_pos">
      <el-col :span="24">
        <profileContainer></profileContainer>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import profileContainer from './profileContainer'

export default {
  name: "Profile",
  components:{
    profileContainer,
  },
  props:['username']

}
</script>

<style scoped>

#profile{
  /*background: url("../assets/login_background.gif");*/
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

#profile_container_pos{
  position: relative;
}

</style>
