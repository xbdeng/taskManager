<template>
  <div>
    <el-form ref="form">
    <el-form-item class="text_left" >
      <el-row :gutter="20">
        <el-col :span="8">
          <div>
            Passwd
            <br>
            <el-input placeholder="123456"></el-input>
          </div>
        </el-col>

        <el-col :span="8">
          <div>
            ?
            <br>
            <el-input placeholder="?"></el-input>
          </div>
        </el-col>

        <el-col :span="8">
          <div>
            ?
            <br>
            <el-input placeholder="?"></el-input>
          </div>
        </el-col>

      </el-row>
    </el-form-item>
  </el-form>
  </div>
</template>

<script>
export default {
  name: "ChangePasswd"
}
</script>

<style scoped>

</style>
