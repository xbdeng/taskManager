<template>
    <div class='personalTask'>
        <el-container>
            <!-- Asider for choose to show today's task or recent seven days' task -->
            <el-aside>
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <!-- Today -->
                    <el-menu-item index="1" @click="showToday">
                        <template slot="title">
                            <i class="el-icon-apple"></i>
                            <span slot="title">今天</span>
                        </template>
                    </el-menu-item>

                    <!-- Recent Seven Days -->
                    <el-menu-item index="2" @click="showRecentSevenDays">
                        <template slot="title">
                            <i class="el-icon-watermelon"></i>
                            <span slot="title">最近七天</span>
                        </template>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <!-- Asider for show Today's Task Item-->
            <el-aside v-show="today">
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    
                    <el-submenu v-for="(task, taskIndex) in todayTasks" :key='taskIndex' index='1'>
                        <template slot="title">
                            <i class="el-icon-edit-outline"></i>
                            <span slot="title">{{ task.taskName }}</span>
                        </template>
                    </el-submenu>
                </el-menu>

            </el-aside>
            <!-- Asider for show Recent seven days' Task Item-->
            <el-aside v-show="recentSevenDays">
                <el-menu default-active="1" class="main-frame-menu"  :collapse="false">
                    <el-submenu v-for="(task, taskIndex) in recentSevenDaysTasks" :key='taskIndex' index='1'>
                        <template slot="title">
                            <i class="el-icon-edit-outline"></i>
                            <span slot="title">{{ task.taskName }}</span>
                        </template>
                    </el-submenu>
                </el-menu>
            </el-aside>

            <el-main>
                <el-container>
                    <el-header>头部栏</el-header>
                    <el-main>
                        显示任务名称和任务描述
                    </el-main>
                </el-container>
            </el-main>
        </el-container>
    </div>
</template>

<script>

export default {

  name: "PersonalTaskPage",
  components: {
    
  },
  data() {
        
        return {
            // Whether choose to show today's task or recent seven days'tasks
            today:false,
            recentSevenDays:false,
            todayTasks:[
                {
                    taskName:'OOAD'
                },
                {
                    taskName:'Principle of Compiler'
                }
            ],
            recentSevenDaysTasks:[
                {
                    taskName:'OOAD'
                },
                {
                    taskName:'Principle of Complier'
                },
                {
                    taskName:'AI Project'
                }
            ]
        }
  },
  methods: {
    showToday() {
        this.today = true
        this.recentSevenDays = false
    },
    showRecentSevenDays() {
        this.today = false
        this.recentSevenDays = true
    }
}

}
</script>

<style scoped>

</style>
