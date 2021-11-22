<template>
    <div class='addTeamForm'>
        <el-container>
<!--          表单标题：新建组别-->
            <el-header>
                <h1>新建组别</h1>
            </el-header>
<!--          表单项-->
            <el-main>
                <el-form label-width="150px" ref='teamForm' :model='teamForm' :rules="rules" status-icon>
                    <!--输入组别名称-->
                    <el-form-item label='组别名称:' prop='teamName'>
                        <el-col :span='14'>
                            <el-input placeholder='请输入组别名称...' clearable v-model='teamForm.teamName'></el-input>
                        </el-col>
                    </el-form-item>
                    <!-- 输入组的描述信息 -->
                    <el-form-item label='描述信息:' prop='description'>
                        <el-row>
                            <el-col :span='14'>
                                <el-input placeholder='请输入队伍的描述信息' clearable type='textarea' :rows="4" v-model="teamForm.description"></el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-form> 
            </el-main>
<!--          表单尾部，按钮-->
            <el-footer>
                <el-button type='primary' @click="submitForm('teamForm')" >新建</el-button>
                <el-button type="danger" @click="this.toCalendar">取消</el-button>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
import axios from 'axios'
import process from "_shelljs@0.7.8@shelljs";
axios.defaults.baseURL = process.env.API_ROOT
export default {
  name: 'AddTeamForm',
  data () {
    //验证：队伍名不能为空
    var checkTeamName = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('队名不能为空！'));
        }
        callback()
    };
    //验证：队伍表述信息不能为空
    var checkDescription = (rule, value, callback)=>{
        if(value === '') {
            return callback(new Error('描述信息不能为空！'));
        }
        callback()
    };
    return {
        //提交的表单
        teamForm:{
            teamName:'',
            description:''
        },
        //验证规则
        rules:{
            teamName:[{validator:checkTeamName, trigger:'blur'}],
            description:[{validator:checkDescription, trigger:'blur'}]
        },
    }
  },
  methods:{
      //提交表单后，跳转到日历界面
      toCalendar() {
          //清空表单
          for(let i in this.teamForm) {
              this.teamForm[i] = null
          }
        //  向父组件发送跳转信息
        this.$emit('toCalendar',{});
      },
      submitForm(formName) {
          let that = this
          this.$refs[formName].validate((valid)=>{
              if(valid) {
                axios.post(
                    '/team/createteam',
                    {
                        //creatorName
                        description:that.teamForm.description,
                        //memberName
                        teamName:that.teamForm.teamName
                    },
                    {
                        headers:{
                            Authorization:window.sessionStorage.getItem('token')
                        }
                    }
                ).then(
                    function(response) {
                        if(response.data.code === 200) {
                            that.$message({
                                message:'新建组成功',
                                type:'success'
                            })
                            //跳转到日历界面
                            that.toCalendar()
                            let newToken = response.headers.authorization
                            if(newToken != null) window.sessionStorage.setItem('token', newToken)
                        } else {
                            that.$message.error('新建组失败')
                            let newToken = response.headers.authorization
                            if(newToken != null) window.sessionStorage.setItem('token', newToken)
                        }
                    },
                    function(err) {
                        that.$message.error('响应错误，添加组失败')
                    }
                )
              } else {
                  that.$message.error('表单验证失败')
                  return false
              }
          });
      },


  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
