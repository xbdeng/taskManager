<template>
    <div class='addTeamForm'>
        <el-container>
            <el-header>
                <h1>新建组别</h1>
            </el-header>
            <el-main>
                <el-form label-width="150px" ref='teamForm' :model='teamForm' :rules="rules" status-icon>
                    <!--输入组别名称-->
                    <el-form-item label='组别名称:' prop='teamName'>
                        <el-col :span='14'>
                            <el-input placeholder='请输入组别名称...' clearable v-model='teamForm.teamName'></el-input>
                        </el-col>
                    </el-form-item>

                    <!-- 输入组内成员 :穿梭框bug-->

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

            <el-footer>
                <el-button type='primary' @click="submitForm('teamForm')" >新建</el-button>
                <el-button type="danger" @click="toCalendar">取消</el-button>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
export default {
  name: 'AddTeamForm',
  data () {
    // 验证队名
    var checkTeamName = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('队名不能为空！'));
        }
        callback()
    };
    // 验证描述信息
    var checkDescription = (rule, value, callback)=>{
        if(value === '') {
            return callback(new Error('描述信息不能为空！'));
        }
        callback()
    };
    return {
        // 新建组的表单信息
        teamForm:{
            teamName:'',
            description:''
        },
        // 表单项验证规则
        rules:{
            teamName:[{validator:checkTeamName, trigger:'blur'}],
            description:[{validator:checkDescription, trigger:'blur'}]
        },
    }
  },
  methods:{
      toCalendar() {
        this.$emit('toCalendar',{});
      },
    //   点击“新建”，提交表单
      submitForm(formName) {

          this.$refs[formName].validate((valid)=>{
              if(valid) {
                //   向父组件传值
                  this.$emit('teamFormData',this.teamForm)
                //   清空表单
                  for(let key in this.teamForm) {
                      this.teamForm[key] = ''
                  }
                //   提示提交成功
                this.$message({
                    message:'提交成功',
                    type:'success'
                })
                // 跳转到日历界面
                this.toCalendar()
              } else {
                  alert('error submit !!')
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
