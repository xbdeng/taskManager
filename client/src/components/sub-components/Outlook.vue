<template>
  <div>
<!--        <div>{{ googleUser }}</div>-->
<!--        <el-form>-->
<!--          <el-row>-->
<!--    &lt;!&ndash;        <el-form-item v-for="(data, index) in this.list" :key="index" class="remove_bottom">&ndash;&gt;-->
<!--            <el-row v-for="x in parseInt((this.listtmp.length / 4).toString())" :key="x">-->
<!--              <el-col :span="6" v-for="y in 4" :key="y">-->
<!--                <div>-->
<!--                  <el-card class="box-card">-->
<!--                    <div slot="header" class="clearfix">-->
<!--                      <span>{{ this.listtmp[4 * x + y].taskName }}}</span>-->
<!--                      <el-button style="float: right; padding: 3px 0" type="text">添加</el-button>-->
<!--                    </div>-->
<!--                    <div>-->
<!--                      Description:{{ this.listtmp[4 * x + y].description }}-->
<!--                      <br>-->
<!--                      Location: {{ this.listtmp[4 * x + y].location }}-->
<!--                      <br>-->
<!--                      CreateDate:{{ this.listtmp[4 * x + y].createDate }}-->
<!--                      <br>-->
<!--                      DueDate:{{ this.listtmp[4 * x + y].dueDate }}-->
<!--                    </div>-->
<!--                  </el-card>-->
<!--                </div>-->
<!--              </el-col>-->
<!--            </el-row>-->
<!--            <el-row >-->
<!--              <el-col :span="6" v-for="(j,y) in this.listtmp.length % 4" :key="y">-->
<!--                <div>-->
<!--                  <el-card class="box-card">-->
<!--                    <div slot="header" class="clearfix">-->
<!--                      <span>{{ this.listtmp[4 * this.listtmp.length + y].taskName }}}</span>-->
<!--                      <el-button style="float: right; padding: 3px 0" type="text">添加</el-button>-->
<!--                    </div>-->
<!--                    <div>-->
<!--                      Description:{{ this.listtmp[4 * this.listtmp.length + y].description }}-->
<!--                      <br>-->
<!--                      Location: {{ this.listtmp[4 * this.listtmp.length + y].location }}-->
<!--                      <br>-->
<!--                      CreateDate:{{ this.listtmp[4 * this.listtmp.length + y].createDate }}-->
<!--                      <br>-->
<!--                      DueDate:{{ this.listtmp[4 * this.listtmp.length + y].dueDate }}-->
<!--                    </div>-->
<!--                  </el-card>-->
<!--                </div>-->
<!--              </el-col>-->
<!--            </el-row>-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--          </el-row>-->
<!--        </el-form>-->
    <el-form>
      <el-form-item v-for="(data, index) in this.listtmp" :key="index" class="remove_bottom">
        <div>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ data.taskName }}</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddTask(data.taskName, data.description, data.location, data.createDate, data.dueDate, data.remindDate)">添加</el-button>
            </div>
            <div>
              Description:{{ data.description }}
              <br>
              Location: {{ data.location }}
              <br>
              CreateDate:{{ data.createDate }}
              <br>
              DueDate:{{ data.dueDate }}
            </div>
          </el-card>
        </div>
      </el-form-item>
    </el-form>
    <el-button type="primary" plain @click="handleclick">select end</el-button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      googleUser: "Microsoft登陆中...",
      listtmp: [],
      username: ''
    };
  },
  mounted() {
    // TODO :  Login with github profile (need api)
    // alert(1)
    let code = this.$route.query.code;
    // console.log(code)
    const that = this
    axios.post(
        'http://localhost:8081/api/task/getmicrosofttask',
        {
          code: code
        },
        {
          headers: {
            Authorization: window.sessionStorage.getItem('token')
          }
        }
    ).then(
        function (response) {
          if (response.data.code === 200) {
            that.listtmp = response.data.data
          }
          // console.log(response)
          that.username = response.data.msg
          let newToken = response.headers.authorization
          if (newToken != null) window.sessionStorage.setItem('token', newToken)
        },
        function (err) {
        }
    )
  },
  methods:{
    handleclick(event){
      this.$router.push({name: 'Main', params: {username: this.username }})
    },
    handleAddTask(taskName, description, location, createDate, dueDate, remindDate){
      const that = this
      axios.post(
          '/task/addtask',
          {
            taskName: taskName,
            createDate: createDate,
            dueDate: dueDate,
            description: description,
            location: location,
            remindDate: remindDate,
            type: 0
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response){
            if(response.data.code === 200){
              that.$message({
                message: 'add success',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if (newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err){

          }
      )
    }
  }
};
</script>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 480px;
}
</style>
