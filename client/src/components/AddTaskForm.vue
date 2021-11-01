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
                        <el-col :span='14'>
                            <el-input placeholder='请输入任务名...' clearable v-model='taskForm.taskName'></el-input>
                        </el-col>
                    </el-form-item>
                    <!--Task Tags-->
                    <el-form-item label='任务标签:' prop='taskTags'>
                        <el-col :span='16'>
                            <el-select placeholder='请选择任务标签' v-model='taskForm.taskTags'>
                                <el-option v-for='item in tagArray' :label='item.label' :value='item.value' :key="item.value"></el-option>
                            </el-select>
                        </el-col>
                    </el-form-item>
                    <!--Task DDL-->
                    <el-form-item label='任务截止时间:' prop='taskDDL'>
                        <el-col :span='8'>
                            <el-date-picker type='date' placeholder="请选择任务的截止时间" v-model="taskForm.taskDDL"></el-date-picker>
                        </el-col>
                    </el-form-item>
                    <!--Task Priority-->
                    <el-form-item label='任务优先级：' prop='taskPriority'>
                        <el-col :span='16'>
                            <el-select placeholder='请选择任务优先级' v-model='taskForm.taskPriority'>
                                <el-option v-for='item in priorityArray' :label='item.label' :value='item.value' :key="item.value"></el-option>
                            </el-select>
                        </el-col>
                    </el-form-item>
                    <!--Task Type-->
                    <el-form-item label='任务类型:' prop='taskType'>
                        <el-col :span='16'>
                            <el-radio-group v-model='taskForm.taskType' >
                                <el-radio label='personal'>个人任务</el-radio>
                                <el-radio label='group'>组队任务</el-radio>
                            </el-radio-group>
                        </el-col>
                    </el-form-item>
                    <!--Task Start Time-->
                    <el-form-item label='开始时间:' prop='taskStartTime'>
                        <el-col :span='8'>
                            <el-date-picker type='date' placeholder="请选择任务的开始时间" v-model="taskForm.taskStartTime"></el-date-picker>
                        </el-col>
                    </el-form-item>
                    <!-- Task Description -->
                    <el-form-item label='任务描述:' prop='taskDescription'>
                        <el-col :span='14'>
                            <el-input placeholder='请输入任务的描述信息' clearable v-model='taskForm.taskDescription' type='textarea'></el-input>
                        </el-col>
                    </el-form-item>
                </el-form> 
            </el-main>

            <el-footer>
                <el-button type='primary' @click="submitForm('taskForm')" >添加任务</el-button>
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
        
        taskForm:{
            taskName:'',
            taskTags: '',
            taskDDL:'',
            taskPriority:'',
            taskType: '',
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
      

      submitForm(formName) {

          this.$refs[formName].validate((valid)=>{
              if(valid) {
                  this.$emit('taskFormData',{
                      taskName:this.taskForm.taskName,
                      taskTags:this.taskForm.taskTags,
                      taskDDL:this.taskForm.taskDDL,
                      taskPriority:this.taskForm.taskPriority,
                      taskType:this.taskForm.taskType,
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
