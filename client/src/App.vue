<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import router from './router'
import Main from './components/main-frame/Main'
import Login from './components/login-register/Login'
import register from './components/login-register/register'
import Profile from './components/profile/profile.vue'
import tree from './components/main-frame/TreeTask.vue'
import ws from "./components/sub-components/WebSocket";

export default {
  name: 'App',
  components: {
    Main,
    Login,
    register,
    Profile,
    tree
  },
  router,
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: auto;
}
/*scroll bar*/
::-webkit-scrollbar {
  width: 10px;
  height: 1px;
}
::-webkit-scrollbar-thumb { //滑块部分
  border-radius: 10px;
  background-color: rgba(175, 175, 175, 0.26);
}
::-webkit-scrollbar-track { //轨道部分
box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
  background: rgb(16, 49, 84);
  border-radius: 5px;
}
</style>
