<template>
  <div id="register">
    <el-row  type="flex"  justify="center" id="register_container_pos">
      <el-col :span="12">
        <registerContainer></registerContainer>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import registerContainer from "./registerContainer";

export default {
  name: "register",

  components:{
    registerContainer
  }
}
</script>

<style scoped>


#register{
  background-color: #cac6c6;
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}

#register_container_pos{
  position: relative;
  top:10%;
}

</style>
