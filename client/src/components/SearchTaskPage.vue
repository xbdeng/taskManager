<template>

  <div id="searchTask">
    <el-container>
        <el-header class='search_header'>
            <el-row type="flex" align="middle">
                <el-input placeholder="请输入要查询的任务名称" prefix-icon="el-icon-search"></el-input>
            </el-row>
        </el-header>
        <el-container>
            <el-aside class="search_aside">
                <el-menu
                default-active="2"
                class="el-menu-vertical-demo">
                    <el-submenu index="1">
                        <template slot="title">
                        <i class="el-icon-location"></i>
                        <span>导航一</span>
                        </template>
                        <el-menu-item-group>
                        <template slot="title">分组一</template>
                        <el-menu-item index="1-1">选项1</el-menu-item>
                        <el-menu-item index="1-2">选项2</el-menu-item>
                        </el-menu-item-group>
                        <el-menu-item-group title="分组2">
                        <el-menu-item index="1-3">选项3</el-menu-item>
                        </el-menu-item-group>
                        <el-submenu index="1-4">
                        <template slot="title">选项4</template>
                        <el-menu-item index="1-4-1">选项1</el-menu-item>
                        </el-submenu>
                    </el-submenu>
                    <el-menu-item index="2">
                        <i class="el-icon-menu"></i>
                        <span slot="title">导航二</span>
                    </el-menu-item>
                    <el-menu-item index="3" disabled>
                        <i class="el-icon-document"></i>
                        <span slot="title">导航三</span>
                    </el-menu-item>
                    <el-menu-item index="4">
                        <i class="el-icon-setting"></i>
                        <span slot="title">导航四</span>
                    </el-menu-item>
                </el-menu>
            </el-aside>
            <el-main>
                在这里显示任务名和任务描述
            </el-main>
        </el-container>
    </el-container>
  </div>
</template>

<script>

export default {

  name: "SearchTaskPage",
  components: {

  },
  data() {
      return {

      }
  }

}
</script>

<style scoped>
.search_header{
    
}
.search_aside {
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
}

</style>
