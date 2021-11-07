<template>
    <div class='addGroupForm'>
        <el-container>
            <el-header>
                <h1>添加组别</h1>
            </el-header>
            <el-main>
                <el-form label-width="150px" ref='groupForm' :model='groupForm' :rules="rules" status-icon>
                    <!--Group Name-->
                    <el-form-item label='组别名称:' prop='groupName'>
                        <el-col :span='14'>
                            <el-input placeholder='请输入组别名称...' clearable v-model='groupForm.groupName'></el-input>
                        </el-col>
                    </el-form-item>

                    <!-- Group Members -->
                    <el-form-item label='组内成员:' prop="groupMembers">
                        <el-col :span="6">
                            <el-select placeholder='请选择组内成员' multiple v-model='groupForm.groupMembers'>
                                <el-option v-for="(friend, fIndex) in friends" :key="fIndex" :label="friend.friendName" :value="friend.friendName">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-form-item>
                </el-form> 
            </el-main>

            <el-footer>
                <el-button type='primary' @click="submitForm('groupForm')" >添加组别</el-button>
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
