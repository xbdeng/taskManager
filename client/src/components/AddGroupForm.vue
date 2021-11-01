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

                    <!--Task Tags-->
                    <el-form-item label='组别类型:' prop='groupTypes'>
                        <el-col :span='16'>
                            <el-select placeholder='请选择组别类型' v-model='groupForm.groupTypes'>
                                <el-option v-for='item in groupTypeArray' :label='item.label' :value='item.value' :key="item.value"></el-option>
                            </el-select>
                        </el-col>
                    </el-form-item>

                    <!-- Group Members -->
                    <el-form-item label='组内成员:' prop="groupMembers">
                        <el-transfer
                            filterable
                            :filter-method="memberFilterMethod"
                            filter-placeholder="请输入成员名称的拼音"
                            v-model="memberValue"
                            :data="memberData">
                        </el-transfer>
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

    var checkGroupTypes = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Group Types should not be empty!'));
        }
        callback()
    };

    const generateMembers = _ => {
        const memberData = []
        const members = ['博丽灵梦','雾雨魔理沙','东风谷早苗','十六夜咲夜','魂魄妖梦','射命丸文']
        const pinyin = ['bolilingmeng','wuyumolisha','dongfengguzaomiao','shiliuyexiaoye','hunpoyaomeng','shemingwanwen']
        members.forEach((member, index) => {
            memberData.push({
                label:member,
                key:index,
                pinyin:pinyin[index]
            })
        })
        return memberData
    }

    return {
        
        groupForm:{
            groupName:'',
            groupTypes: '',
            groupMembers:''
        },
        groupTypeArray:[
            {label:'课程小组', value:'course'},
            {label:'创新实践小组', value: 'innovation_project'}
        ],

        rules:{
            groupName:[{validator:checkGroupName, trigger:'blur'}],
            groupTypes:[{validator:checkGroupTypes, trigger:'blur'}],

        },
        memberData:generateMembers(),
        memberValue:[],
        memberFilterMethod(query, item) {
            return item.pinyin.indexOf(query) > -1
        }
    }
  },
  methods:{
      

      submitForm(formName) {

          this.$refs[formName].validate((valid)=>{
              if(valid) {
                  this.$emit('groupFormData',{
                      groupName:this.groupForm.groupName,
                      groupTypes:this.groupForm.groupTypes,
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
