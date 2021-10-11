<template>
  <div>
    <el-container>
      <el-header class="head">
        <el-row class="navigator">
          <el-col :span="6">
            <div class="title">Task Manager</div>
          </el-col>
          <el-col :span=4 :offset="14">
            <div class="hello">Hi,{{username}}</div>
          </el-col>
        </el-row>
      </el-header>
      <el-aside class="aside">
        <el-menu>
          <el-menu-item>Personal Information</el-menu-item>
          <el-menu-item>Log out</el-menu-item>
          <el-menu-item>Preference</el-menu-item>
        </el-menu>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Main",
  props: ['username']
}
</script>

<style scoped>
.head{
  background: #0099ff;
}
.navigator{
  top:10px;
}
.title{
  font-size: 30px;
  color:white;
}
.hello{
  font-size: 20px;
  color:white;
}
.pos{
  top:10px;
}
</style>
