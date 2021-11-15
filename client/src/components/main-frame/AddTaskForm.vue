<template>
    <div class='addTaskForm'>
        <el-container>
            <el-header>
                <h1>添加任务</h1>
            </el-header>

            <el-main>
                <el-form label-width="150px" ref='taskForm' :model='taskForm' :rules="rules" status-icon>
                    <!--Task Name-->
                    <el-form-item label='任务名称:' prop='taskName'>
                        <el-row>
                            <el-col :span='14'>
                                <el-input placeholder='请输入任务名...' clearable v-model='taskForm.taskName'></el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--Task Tags-->
                    <el-form-item label='任务标签:' prop='taskTags'>
                        <el-row>
                            <el-col :span='14' >
                                <el-select placeholder='请选择任务标签' multiple v-model='taskForm.taskTags'>
                                    <el-option v-for='item in tagArray' :label='item.label' :value='item.value' :key="item.value"></el-option>
                                </el-select>
                                <!-- 文字提示：提示用户可以自定义任务标签 -->
                                <el-popover placement="top" width="200" trigger="click" title="自定义任务标签">
                                    <el-row>
                                        <el-col>
                                            <el-input placeholder="请输入自定义的任务标签..." clearable v-model="addedTag"></el-input>
                                        </el-col>
                                    </el-row>
                                    <el-row>
                                        <el-col :offset="8">
                                            <el-button type='primary' plain size="small" @click="addTag">确定</el-button>
                                        </el-col>
                                    </el-row>
                                    <el-button type='primary' icon='el-icon-plus' circle slot="reference"></el-button>
                                </el-popover>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--Task DDL-->
                    <el-form-item label='任务截止时间:' prop='taskDDL'>
                        <el-row>
                            <el-col :span='8'>
                                <el-date-picker type='date' placeholder="请选择任务的截止时间" v-model="taskForm.taskDDL"></el-date-picker>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--Task Priority-->
                    <el-form-item label='任务优先级：' prop='taskPriority'>
                        <el-row>
                            <el-col :span='11'>
                                <el-select placeholder='请选择任务优先级' v-model='taskForm.taskPriority'>
                                    <el-option v-for='item in priorityArray' :label='item.label' :value='item.value' :key="item.value"></el-option>
                                </el-select>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--Task Type-->
                    <el-form-item label='任务类型:' prop='taskType'>
                        <el-row>
                            <el-col :span='10'>
                                <el-radio-group v-model='taskForm.taskType' >
                                    <el-radio label='personal'>个人任务</el-radio>
                                    <el-radio label='group'>组队任务</el-radio>
                                </el-radio-group>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!-- 如果选择了组队任务，会多出一个多选框，选择给哪个队伍分配任务 -->
                    <el-form-item label='分配组别:' prop='taskGroups' v-if="taskForm.taskType === 'group'">
                        <el-col :span='10'>
                            <el-select placeholder='请选择任务分配的组别' multiple v-model='taskForm.taskGroups'>
                                <el-option v-for="(group, gIndex) in groupInfo" :key="gIndex" :label="group.groupName" :value="group.groupName">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-form-item>
                    <!--Task Start Time-->
                    <el-form-item label='开始时间:' prop='taskStartTime'>
                        <el-row>
                            <el-col :span='8'>
                                <el-date-picker type='date' placeholder="请选择任务的开始时间" v-model="taskForm.taskStartTime"></el-date-picker>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!-- Task Description -->
                    <el-form-item label='任务描述:' prop='taskDescription'>
                        <el-row>
                            <el-col :span='14'>
                                <el-input placeholder='请输入任务的描述信息' clearable v-model='taskForm.taskDescription' type='textarea'></el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-form> 
            </el-main>

            <el-footer>
                <el-button type='primary' @click="submitForm('taskForm')" >添加任务</el-button>
                <el-button type="danger">取消</el-button>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
export default {
  name: 'AddTaskForm',
  data () {
    
    var checkTaskName = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task Name should not be empty!'));
        }
        callback()
    };

    var checkTaskTags = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task Tags should not be empty!'));
        }
        callback()
    };

    var checkTaskDDL = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task DDL should not be empty!'));
        }
        callback()
    };

    var checkTaskPriority = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task Priority should not be empty!'));
        }
        callback()
    };

    var checkTaskType = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task Type should not be empty!'));
        }
        callback()
    };

    var checkTaskStartTime = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task Start Time should not be empty!'));
        }
        callback()
    };

    var checkTaskDescription = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('Task Description should not be empty!'));
        }
        callback()
    };

    return {
        addedTag:'',
        taskForm:{
            taskName:'',
            taskTags: '',
            taskDDL:'',
            taskPriority:'',
            taskType: '',
            taskGroups:'',
            taskStartTime:'',
            taskDescription:''
        },
        tagArray:[
            {label:'学习', value:'study'},
            {label:'工作', value: 'work'}
        ],
        priorityArray:[
            {label:'高', value:'high_priority'},
            {label:'中', value:'medium_priority'},
            {label:'低', value:'low_priority'}
        ],
        groupInfo:[
            {
                groupName:'嘻嘻嘻嘻嘻',
            },
            {
                groupName:'OOAD摸鱼划水'
            }
        ],
        rules:{
            taskName:[{validator:checkTaskName, trigger:'blur'}],
            taskTags:[{validator:checkTaskTags, trigger:'blur'}],
            taskDDL:[{validator:checkTaskDDL, trigger:'blur'}],
            taskPriority:[{validator:checkTaskPriority, trigger:'blur'}],
            taskType:[{validator:checkTaskType, trigger:'blur'}],
            taskStartTime:[{validator:checkTaskStartTime, trigger:'blur'}],
            taskDescription:[{validator:checkTaskDescription, trigger:'blur'}],
        }
    }
  },
  methods:{
    //   用户自定义标签
      addTag() {
        //   判断要添加的标签是否已经存在
          let flag = false
          for(let i in this.tagArray) {
              let item = this.tagArray[i]
              if(item.label === this.addedTag) {
                  flag = true
                  break
              }
          }
          if(flag) {
              this.$message.error('添加失败，已有该标签')
              return ;
          }
          this.tagArray.push(
              {
                  label:this.addedTag,
                  value:this.addedTag
              }
          )
          this.$message(
              {
                  message:'添加成功!',
                  type:'success'
              }
          )
      },
      submitForm(formName) {

          this.$refs[formName].validate((valid)=>{
              
              if(valid) {
                  this.$emit('taskFormData',{
                      taskName:this.taskForm.taskName,
                      taskTags:this.taskForm.taskTags,
                      taskDDL:this.taskForm.taskDDL,
                      taskPriority:this.taskForm.taskPriority,
                      taskType:this.taskForm.taskType,
                      taskGroups:this.taskForm.taskGroups,
                      taskStartTime:this.taskForm.taskStartTime,
                      taskDescription:this.taskForm.taskDescription
                  })
                  for(let key in this.taskForm) {
                      this.taskForm[key] = ''
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
