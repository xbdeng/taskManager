<template>
  <div id="register">
    <vue-canvas-nest :config="config" :el="'#register'"></vue-canvas-nest>
    <el-row  type="flex"  justify="center" id="register_container_pos">
      <el-col :span="12">
        <registerContainer></registerContainer>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import registerContainer from "./registerContainer";
import vueCanvasNest from "vue-canvas-nest";

export default {
  name: "register",

  components:{
    registerContainer,
    vueCanvasNest
  },
  data(){
    return{
      config: {
        color: "255, 255, 255",
        opacity: 1,
        zIndex: 5,
        count: 99,
      }
    }
  }
}
</script>

<style scoped>


#register{
  background-color: #040d21;
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
