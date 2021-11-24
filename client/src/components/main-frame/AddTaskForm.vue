<template>
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
                  <!--任务地点-->
                    <el-form-item label="任务地点:" prop="position">
                      <el-row>
                        <el-col :span="14">
                          <el-input placeholder='请输入任务地点...' clearable v-model='taskForm.position'></el-input>
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
                                    <el-radio label='1'>组队任务</el-radio>
                                </el-radio-group>
                            </el-col>
                        </el-row>
                    </el-form-item>
                    <!-- 如果选择了组队任务，会多出一个多选框，选择给哪个队伍分配任务 -->
                    <el-form-item label='分配组别:' prop='teams' v-if="taskForm.type === '1'">
                        <el-col :span='10'>
                            <!-- 单选 -->
                            <el-select placeholder='请选择任务分配的组别' v-model='taskForm.teamId'>
                                <el-option v-for="team in this.myTeamInfo" :key="team.teamName" :label="team.teamName" :value="team.teamId">
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
  name: 'AddTaskForm',
  //username:Main中传递
  //tagArray:Main中请求用户的标签信息，传递到子组件
  //myTeamInfo:Main中请求用户创建或管理的组
  //fatherTaskId:TreeTask中，点击节点添加子任务时，传递的父任务的id
  props:['username','tagArray','myTeamInfo','fatherTaskId', 'startDate','endDate','show'],
  data () {
    //任务名验证：不能为空
    var checkTaskName = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务名不能为空！'));
        }
        callback()
    };
    //任务类型验证：不能为空
    var checkTaskType = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('任务类型不能为空！'));
        }
        callback()
    }
    //组队任务分配的组：不能为空
    var checkTeams = (rule, value, callback)=>{
        if (value === '') {
            return callback(new Error('组队任务分配的组别不能为空！'));
        }
        callback()
    };
    return {
        // 用户添加的自定义的标签
        addedTag:'',
        //添加任务的表单
        taskForm:{
            taskName:'',
            tags: [],
            dueDate:this.endDate === null ? '' : this.endDate,
            privilege:0,
            type: '',
            teamId:'',
            createDate:this.startDate === null ? '' : this.startDate,
            description:'',
            position:''
        },
        //默认的优先级选项
        priorityArray:[
            {label:'极高', value:3},
            {label:'高',   value:2},
            {label:'中',   value:1},
            {label:'低',   value:0}
        ],
        //表单验证规则
        rules:{
            taskName:[{validator:checkTaskName, trigger:'blur'}],
            // dueDate:[{validator:checkTaskDDL, trigger:'blur'}],
            // privilege:[{validator:checkTaskPriority, trigger:'blur'}],
            type:[{validator:checkTaskType, trigger:'blur'}],
            // createDate:[{validator:checkTaskStartTime, trigger:'blur'}],
            // description:[{validator:checkTaskDescription, trigger:'blur'}],
            teams:[{validator:checkTeams, trigger:'blur'}],

        }
    }
  },
  watch:{
    show:{
      immediate:true,
      handler:function(){
        this.$set(this.taskForm,'createDate', this.startDate)
        this.$set(this.taskForm,'dueDate', this.endDate)
        console.log(this.dueDate)
        this.$forceUpdate()
      },
      deep: true
    }
  },
  methods:{
    //用户点击取消，跳转回日历界面
    toCalendar() {
        //将添加任务表单清空
        for(let i in this.taskForm) {
            this.taskForm[i] = null
        }
        //向父组件发送跳转日历界面的消息
        this.$emit('toCalendar',{});
    },
    // 用户输入自定义标签内容后，点击添加按钮，执行添加自定义标签
    addTag() {
        //遍历已有的标签，看是否重复，如果重复不能添加
        let flag = false
        let that = this
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
        //向后端发送添加标签请求
        axios.post(
            '/user/addtag',
            {
                tagName:that.addedTag
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
                        message:'添加标签成功',
                        type:'success'
                    })
                  // 添加成功，直接向表单中添加这个标签项
                    that.tagArray.push(
                        {
                            label:that.addedTag,
                            value:that.addedTag
                        }
                    )
                  // 将输入框绑定的数据清空
                  that.addedTag = null
                  let newToken = response.headers.authorization
                  if(newToken != null) window.sessionStorage.setItem('token', newToken)
                } else {
                    that.$message.error('添加标签失败')
                    let newToken = response.headers.authorization
                    if(newToken != null) window.sessionStorage.setItem('token', newToken)
                }
            },
            function(err) {
                that.$message.error('响应失败，添加标签失败')
            }
        )
    },
    //提交添加任务的表单
    submitForm(formName) {
        let that = this
        // 如果是在树形结构中调用AddTaskForm，参数中fatherTaskId不为null，这时要多执行一步添加子任务的步骤
        let fTaskId = this.fatherTaskId
        //子任务的id
        var cTaskId = null
        this.$refs[formName].validate((valid)=>{
            if(valid) {
              axios({
                method:'POST',
                url:'/task/addtask',
                headers:{
                  Authorization:window.sessionStorage.getItem('token')
                },
                data:{
                    createDate:that.taskForm.createDate,
                    description:that.taskForm.description,
                    dueDate:that.taskForm.dueDate,
                    //fatherTask
                    //members
                    privilege:that.taskForm.privilege,
                    status:0,
                    // subtasks
                    tags:that.taskForm.tags,
                    taskName:that.taskForm.taskName,
                    teamId:that.taskForm.teamId,
                    type:parseInt(that.taskForm.type),
                    // username
                }
              }).then(
                function (response) {
                    if(response.data.code === 200) {
                        that.$message({
                            message:'新建任务成功',
                            type:'success'
                        })
                        //清空所有表单项
                        for(let key in that.taskForm) {
                            that.taskForm[key] = ''
                        }
                        //获取添加该任务后，该任务的id
                        cTaskId = response.data.data
                        let newToken = response.headers.authorization
                        if(newToken != null) window.sessionStorage.setItem('token', newToken)
                        //添加子任务
                        const there = that
                        if(fTaskId != null) {
                          //请求添加父子任务
                          axios.post(
                              '/task/addsubtask',
                              {
                                fatherTask:fTaskId,
                                subTask:cTaskId
                              },
                              {
                                headers:{
                                  Authorization:window.sessionStorage.getItem('token')
                                }
                              }

                          ).then(
                              function(response) {
                                if(response.data.code === 200) {
                                  there.$message({
                                    message:'添加子任务成功',
                                    type:'success'
                                  })
                                  that.$emit('postTree',{})
                                } else {
                                  there.$message.error('添加子任务失败')
                                }
                              },
                              function(err) {
                                there.$message.error('响应失败，添加子任务失败')
                              }
                          )
                        }
                        //添加完任务，直接跳转到日历界面
                        that.toCalendar()
                    } else {
                        that.$message.error('新建任务失败')
                        let newToken = response.headers.authorization
                        if(newToken != null) window.sessionStorage.setItem('token', newToken)
                    }
                },
                function (err) {
                    that.$message.error('响应错误，新建任务失败')
                }
            )

            } else {
                that.$message.error('表单验证未通过')
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
