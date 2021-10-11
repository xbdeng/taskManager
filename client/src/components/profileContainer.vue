<template>
  <div class="profile-container">
    <el-container>
      <el-header>
        <el-row>
          <el-col :span="1">
            <el-button type="primary" @click="drawer = true" icon="el-icon-more" circle>
            </el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-container>
        <el-drawer
            title="侧边栏"
            :modal-append-to-body=false
            :direction="direction"
            :visible.sync="drawer"
            :with-header="false"
            :size="size">
<!--          logo 20vh-->
          <el-aside width="15vw">
            <div class="up-icon">
              <img src="../assets/task-icon.jpg" class="up-icon">
            </div>
            <div class="aside-page">
              <!--          <el-radio-group v-model="isCollapse" style="margin-bottom: 20px;">-->
              <!--            <el-radio-button :label="false">展开</el-radio-button>-->
              <!--            <el-radio-button :label="true">收起</el-radio-button>-->
              <!--          </el-radio-group>-->
              <el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
                       :collapse="isCollapse" @select="handleSelect">
<!--                <el-submenu index="1">-->
<!--                  <template slot="title">-->
<!--                    <i class="el-icon-location"></i>-->
<!--                    <span slot="title">-->
<!--&lt;!&ndash;                      <router-link to="/profile/Info">导航一</router-link>&ndash;&gt;-->
<!--&lt;!&ndash;                      ？？？&ndash;&gt;-->
<!--                      导航一-->
<!--                    </span>-->
<!--                  </template>-->
                  <el-menu-item-group>
<!--                    <span slot="title">分组一</span>-->
                    <el-menu-item index="/profile/Info">个人信息</el-menu-item>
                    <el-menu-item index="/profile/ChangePasswd">修改信息</el-menu-item>
                  </el-menu-item-group>
<!--                  <el-menu-item-group title="分组2">-->
<!--                    <el-menu-item index="1-3">选项3</el-menu-item>-->
<!--                  </el-menu-item-group>-->
<!--                  <el-submenu index="1-4">-->
<!--                    <span slot="title">选项4</span>-->
<!--                    <el-menu-item index="1-4-1">选项1</el-menu-item>-->
<!--                  </el-submenu>-->
<!--                </el-submenu>-->
<!--                <el-menu-item index="2">-->
<!--                  <i class="el-icon-menu"></i>-->
<!--                  <span slot="title">导航二</span>-->
<!--                </el-menu-item>-->
<!--                <el-menu-item index="3" disabled>-->
<!--                  <i class="el-icon-document"></i>-->
<!--                  <span slot="title">导航三</span>-->
<!--                </el-menu-item>-->
<!--                <el-menu-item index="4">-->
<!--                  <i class="el-icon-setting"></i>-->
<!--                  <span slot="title">导航四</span>-->
<!--                </el-menu-item>-->
              </el-menu>
            </div>
            <div class="aside-down">
              The nearest Task:
              HomeWork
            </div>
          </el-aside>
        </el-drawer>
        <el-container>
          <el-main>
            <div class="main-page">
              <router-view></router-view>
            </div>
          </el-main>
          <!--          <el-footer>Footer</el-footer>-->
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "profileContainer",
  data() {
    return {
      isCollapse: false,
      drawer: false,
      direction: 'ltr',
      size: '15vw',
      fit: 'fill',
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    handleSelect(key, keyPath){
        if("/profile/Info" === key && this.$route.path !== '/profile/Info'){
          this.$router.push('/profile/Info');
        }else if("/profile/ChangePasswd" === key && this.$route.path !== '/profile/ChangePasswd'){
          this.$router.push('/profile/ChangePasswd');
        }
    }
  }
}
</script>

<style scoped>

.main-page {
  min-height: 100vh;
  min-width: 100vh;
}

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.profile-container {
  width: 100%;
  height: 100%;
  background-color: transparent;
  position: absolute;
}

.el-header, .el-footer {
  background-color: #409EFF;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  /*background-color: #D3DCE6;*/
  color: #333;
  text-align: center;
  line-height: 200px;
  height: 100vh;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.aside-page {
  height: 70vh;
  width: 15vw;
}

.aside-down {
  height: 15vh;
  width: 15vw;
}

.el-menu-vertical-demo[data-v-514e58e8]:not(.el-menu--collapse) {
  width: 14.9vw;
  height: 70vh;
}

.up-icon{
  height: 10vh;
  width: 15vw;
  object-fit: contain;
}

a {
  text-decoraction: none;
  color: #050505;
}
.router-link-active {
  text-decoration: none;
}

</style>
