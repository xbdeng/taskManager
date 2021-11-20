<template>
  <div>
    <el-form>
      <el-form-item v-for="(data, index) in Message" :key="index" class="remove_bottom">
        <el-card class="box-card-x" shadow="hover">
          <div>
            <el-row :gutter="20">
              <el-col :span="5">
                <i class="el-icon-s-check"></i>
              </el-col>
              <el-col :span="10">
                <span>{{ data.header }}</span>
              </el-col>
              <el-col :span="9">
                <el-button type="success" icon="el-icon-check" circle></el-button>
                <el-button type="danger" icon="el-icon-close" circle></el-button>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="center">
                {{ data.msg }}
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "MessagePage",
  data() {
    return {
      Message: [{
        header: 'Friends request',
        msg: 'Bill want to be friend with you'
      },
        {
          header: 'Add team request',
          msg: 'Alisa want to invite you into a team'
        }]
    }
  }
}
</script>

<style scoped>
.box-card-x {
  width: 400px;
  padding: 5px;
}
.remove_bottom{
  margin-bottom: 5px;
}
</style>
