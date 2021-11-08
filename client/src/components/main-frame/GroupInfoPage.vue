<template>
<!-- 这个页面用于显示组队任务 -->
    <div class='groupInfo'>
        <el-container>
            <!-- 侧边栏，用于显示所有组的信息 -->
            <el-aside>
                <el-menu  class="main-frame-menu">
                    <!-- 子菜单，用于显示所有的组 -->
                    <el-submenu  v-for='(group, groupIndex) in groupInfo' :key="groupIndex" :index= group.groupName >
                        <template slot="title">
                            <i class="el-icon-apple"></i>
                            <span slot="title" @click="showSelectedGroup(groupIndex)">{{ group.groupName }}</span>
                        </template>
                        <!-- 根据组的下标获取member，遍历memberList并显示成员 -->
                        <el-menu-item v-for='(member, memberIndex) in group.groupMembers' :key="memberIndex">
                            {{ member.memberName }}
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-aside>

            <!-- 侧边栏，用于显示组别任务 -->
            <el-aside>
                <el-menu>
                    <TaskTree :taskData="groupInfo[this.selectedGroup].groupTasks" :taskLevel="''" :chosenTask="chosenTaskId" v-on:taskIdChanged="chooseTasks($event)"></TaskTree>
                </el-menu>
            </el-aside>

            <!-- 用于显示任务信息 -->
            <el-main>
                <TaskShow :singleTaskData="getTaskById(groupInfo[this.selectedGroup].groupTasks, chosenTaskId)"></TaskShow>
            </el-main>
        </el-container>
    </div>
</template>

<script>
import TaskTree from '../sub-components/TaskTree.vue'
import TaskShow from '../sub-components/TaskShow.vue'
export default {

  name: "GroupInfoPage",
  components: {
    TaskTree,
    TaskShow
  },
  props:['groupInfo'],
  data() {

    return {
        // 唯一标识任务的key
        chosenTaskId:'-1',
        // 用户点击的组的编号
        selectedGroup:0       
    }
  },
  methods: {
    // 用于根据用户的点击，显示特定的组别
    showSelectedGroup(groupId) {
        this.selectedGroup = groupId
    },
    // 接收子组件传递的任务的key
    chooseTasks(id) {
          this.chosenTaskId = id;
    },
    // 根据任务数组和子组件的key找到任务对象并返回
    getTaskById(taskList, id) {
        if (id === '-1') return {
            taskName:'Please choose your task'
        }
        if (parseInt(id[0]) >= taskList.length) return {
            taskName:'Please choose your task'
        }
        if (id.length == 1) return taskList[parseInt(id)];
        return this.getTaskById(taskList[parseInt(id[0])].subTasks, id.substr(1));
    }
}

}
</script>

<style scoped>

</style>
