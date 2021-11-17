mo<template>
    <div class='addTaskForm'>
        <el-container>
            <!-- 表单标题 -->
            <el-header>
                <h1>新建任务</h1>
            </el-header>
            <!-- 表单项 -->
            <el-main>
                <el-form label-width="150px" ref='taskForm' :model='taskForm' :rules="rules" status-icon>
                    <!--任务名-->
                    <el-form-item label='任务名称:' prop='taskName'>
                        <el-row>
                            <el-col :span='14'>
                                <el-input placeholder='请输入任务名...' clearable v-model='taskForm.taskName'></el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--任务标签-->
                    <el-form-item label='任务标签:' prop='tags'>
                        <el-row>
                            <el-col :span='14' >
                                <el-select placeholder='请选择任务标签' multiple v-model='taskForm.tags'>
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
                    <el-form-item label='任务截止时间:' prop='dueDate'>
                        <el-row>
                            <el-col :span='8'>
                                <el-date-picker type='datetime' placeholder="请选择任务的截止时间" v-model="taskForm.dueDate"></el-date-picker>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--Task Priority-->
                    <el-form-item label='任务优先级：' prop='privilege'>
                        <el-row>
                            <el-col :span='11'>
                                <el-select placeholder='请选择任务优先级' v-model='taskForm.privilege'>
                                    <el-option v-for='item in priorityArray' :label='item.label' :value='item.value' :key="item.value"></el-option>
                                </el-select>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!--Task Type-->
                    <el-form-item label='任务类型:' prop='type'>
                        <el-row>
                            <el-col :span='10'>
                                <el-radio-group v-model='taskForm.type' >
                                    <el-radio label='0'>个人任务</el-radio>
                                    <el-radio label='1' @click="postMyTeams">组队任务</el-radio>
                                </el-radio-group>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!-- 如果选择了组队任务，会多出一个多选框，选择给哪个队伍分配任务 -->
                    <el-form-item label='分配组别:' prop='teams' v-if="taskForm.type === 'team'">
                        <el-col :span='10'>
                            <el-select placeholder='请选择任务分配的组别' multiple v-model='taskForm.teams'>
                                <el-option v-for="(team, tIndex) in myTeamInfo" :key="tIndex" :label="team.teamName" :value="team.teamName">
                                </el-option>
                            </el-select>
                        </el-col>
                    </el-form-item>
                    <!--Task Start Time-->
                    <el-form-item label='开始时间:' prop='createDate'>
                        <el-row>
                            <el-col :span='8'>
                                <el-date-picker type='datetime' placeholder="请选择任务的开始时间" v-model="taskForm.createDate"></el-date-picker>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!-- Task Description -->
                    <el-form-item label='任务描述:' prop='description'>
                        <el-row>
                            <el-col :span='14'>
                                <el-input placeholder='请输入任务的描述信息' clearable v-model='taskForm.description' type='textarea'></el-input>
                            </el-col>
                        </el-row>
                    </el-form-item>
                </el-form>
            </el-main>

            <el-footer>
                <!-- 点击添加任务，提交到父组件 -->
                <el-button type='primary' @click="submitForm('taskForm')" >新建</el-button>
                <!-- 点击取消，跳转到日历界面 -->
                <el-button type="danger" @click="toCalendar">取消</el-button>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
import axios from 'axios'
const token = window.sessionStorage.getItem('token')
export default {
  name: 'AddTaskForm',
  props:['username'],
  data () {
    // 任务名验证
    var checkTaskName = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务名不能为空！'));
        }
        callback()
    };
    // 标签验证
    var checkTaskTags = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务标签不能为空！'));
        }
        callback()
    };
    // 截止时间验证
    var checkTaskDDL = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务截止时间不能为空！'));
        }
        callback()
    };
    // 优先级验证
    var checkTaskPriority = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务优先级不能为空！'));
        }
        callback()
    };
    // 任务类型验证
    var checkTaskType = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务类型不能为空！'));
        }
        callback()
    };
    // 开始时间验证
    var checkTaskStartTime = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务开始时间不能为空！'));
        }
        callback()
    };
    // 任务描述验证
    var checkTaskDescription = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务描述信息不能为空！'));
        }
        callback()
    };

    return {
        // 添加的标签，是一个string类型的数组
        addedTag:'',
        // 用户输入的表单内容
        taskForm:{
            // 任务名
            taskName:'',
            // 标签
            tags: '',
            // 截止时间
            dueDate:'',
            // 优先级
            privilege:'',
            // 任务类型,0个人任务，1组
            type: '',
            // 如果任务类型是组队任务，这个任务所覆盖的组，是一个字符串数组
            teams:'',
            // 任务开始时间
            createDate:'',
            // 任务描述
            description:''
        },
        // 默认标签
        tagArray:[
            {label:'学习', value:'study'},
            {label:'工作', value: 'work'}
        ],
        // 默认优先级
        priorityArray:[
            {label:'极高', value:3},
            {label:'高',   value:2},
            {label:'中',   value:1},
            {label:'低',   value:0}
        ],
        // 用户创建，或者管理的组,需要向后端获取
        myTeamInfo:'',
        // 表单的验证规则
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
    //   跳转到日历界面
    toCalendar() {
        this.$emit('toCalendar',{});
    },
    //   添加用户自定义标签
      addTag() {
        //   判断要添加的标签是否已经存在,如果已经存在，报错
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
        //   向默认标签中添加该标签
          this.tagArray.push(
              {
                  label:this.addedTag,
                  value:this.addedTag
              }
          )
        // TODO:向后端发送添加后的tag

        //   提示成功信息
          this.$message(
              {
                  message:'添加成功!',
                  type:'success'
              }
          )
      },
    //   点击提交按钮，提交添加任务的表单
      submitForm(formName) {
          let that = this
          // this.taskForm.createDate = new Date(this.taskForm.createDate)
          // alert(this.taskForm.createDate)
          // this.taskForm.dueDate = new Date(this.taskForm.dueDate)
          this.$refs[formName].validate((valid)=>{

              if(valid) {
                //   向父组件<Main>传值
                  this.$emit('taskFormData',that.taskForm)

                axios.post(
                    'http://localhost:8081/api/task/addtask',
                    this.taskForm,
                    {
                      headers:{
                        Authorization:window.localStorage.getItem('token')
                      }
                    }
                ).then(
                    function (response) {
                      that.$message(
                          {
                            message: '创建任务成功',
                            type: 'success'
                          }
                      );
                    },
                    function (err) {
                      that.$message.error('创建任务失败')
                    }
                )
                //   提交后清空表单
                  for(let key in this.taskForm) {
                      this.taskForm[key] = ''
                  }
                // 提交成功后跳转到日历界面
                this.toCalendar()
              } else {
                //   提示错误提交信息
                  alert('error submit !!')
                  return false
              }
          });
      },
    // 向后端请求用户创建/管理的所有组
      postMyTeams() {
          let that = this;
          axios.post(
              'http://localhost:8081/api/user/myteams/admin',
          ).then(
              function(response) {
                  that.myTeamInfo = response.data
              },
              function(err) {
                  that.$message.error('请求用户创建或管理的组失败')
              }
          );
      }

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
