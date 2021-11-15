<template>
    <div class='addGroupForm'>
        <el-container>
            <el-header>
                <h1>添加组别</h1>
            </el-header>
            <el-main>
                <el-form label-width="150px" ref='groupForm' :model='groupForm' :rules="rules" status-icon>
                    <!--输入组别名称-->
                    <el-form-item label='组别名称:' prop='groupName'>
                        <el-col :span='14'>
                            <el-input placeholder='请输入组别名称...' clearable v-model='groupForm.groupName'></el-input>
                        </el-col>
                    </el-form-item>

                    <!-- 输入组内成员 :穿梭框bug-->

                    <!-- 输入组的描述信息 -->
                    <el-form-item label='任务描述:' prop='taskDescription'>
                        <el-row>
                            <el-col :span='14'>
                                <el-input placeholder='请输入任务的描述信息' clearabletype='textarea' :rows="4"></el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-form> 
            </el-main>

            <el-footer>
                <el-button type='primary' @click="submitForm('groupForm')" >添加组别</el-button>
                <el-button type="danger">取消</el-button>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
export default {
  name: 'AddGroupForm',
  data () {
    
    var checkGroupName = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Group Name should not be empty!'));
        }
        callback()
    };


    return {
        groupForm:{
            groupName:'',
            groupMembers:''
        },
        rules:{
            groupName:[{validator:checkGroupName, trigger:'blur'}],
        },
        friends:[
            {
                friendName:'博丽灵梦'
            },
            {
                friendName:'Trump'
            },
            {
                friendName:'dxbdl'
            },
            {
                friendName:'糖糖'
            }
        ]
    }
  },
  methods:{
      

      submitForm(formName) {

          this.$refs[formName].validate((valid)=>{
              if(valid) {
                  this.$emit('groupFormData',{
                      groupName:this.groupForm.groupName,
                      groupMemvers:this.groupForm.groupMembers,
                  })
                  for(let key in this.groupForm) {
                      this.groupForm[key] = ''
                  }

              } else {
                  console.log('error submit !!')
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
