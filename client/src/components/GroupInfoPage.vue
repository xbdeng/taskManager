<template>
    <div class='groupInfo'>
        <el-container>
            <!-- Group Overview -->
            <el-aside>
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <el-submenu  v-for='(group, groupIndex) in groupInfo' :key="groupIndex" index= toString(groupIndex) >
                        <template slot="title">
                            <i class="el-icon-apple"></i>
                            <span slot="title" @click="showSelectedGroup(groupIndex)">{{ group.groupName }}</span>
                        </template>
                        <el-menu-item v-for='(member, memberIndex) in group.groupMembers' :key="memberIndex">
                            {{ member.memberName }}
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>

            <!-- Different Group Tasks in the selected Group-->
            <el-aside>
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <el-submenu v-for='(task, taskIndex) in groupInfo[this.selectedGroup].groupTasks' :key="taskIndex" index="1">
                        <template slot="title">
                            <i class="el-icon-apple"></i>
                            <span slot="title"  @click="showSelectedTask(taskIndex)">{{ task.taskName }}</span>
                        </template>
                    </el-submenu>
                </el-menu>
            </el-aside>

            <!-- The detailed Info of the given tasks-->
            <el-main>
                <el-container>
                    <el-header>头部栏</el-header>
                    <el-main>
                        {{ this.groupInfo[selectedGroup].groupTasks[selectedTask].taskName }}
                    </el-main>
                </el-container>
            </el-main>
        </el-container>
    </div>
</template>

<script>

export default {

  name: "GroupInfoPage",
  components: {
    
  },
  data() {
        
        return {
            selectedGroup:0,
            selectedTask:0,
            groupInfo:[
            {
                groupName:'菜鸡学生组',
                groupMembers:[
                    {
                        memberName:'八云紫',
                        memberRole:'Master'
                    },
                    {
                        memberName:'八云蓝',
                        memberRole:'Manager'
                    },
                    {
                        memberName:'橙',
                        memberRole:'none'
                    }
                        
                    
                ],
                groupTasks:[
                    {
                        taskName:'Database'
                    },
                    {
                        taskName:'Computer Architecture'
                    }
                ]
            },
            {
                groupName:'菜鸡教师组',
                groupMembers:[
                    {
                        memberName:'古明地觉',
                        memberRole:'Master'
                    },
                    {
                        memberName:'古明地恋',
                        memberRole:'Manager'
                    },
                    {
                        memberName:'Olen',
                        memberRole:'none'
                    },
                    {
                        memberName:'灵乌路空',
                        memberRole:'none'
                    }  
                    
                ],
                groupTasks:[
                    {
                        taskName:'AI Project'
                    },
                    {
                        taskName:'OS Project'
                    }
                ]
            },
        ]
    }
  },
  methods: {

    showSelectedGroup(groupId) {
        this.selectedGroup = groupId
    },
    showSelectedTask(taskId) {
        this.selectedTask = taskId
    }
}

}
</script>

<style scoped>

</style>
