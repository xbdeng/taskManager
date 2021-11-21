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

export default {
  name: 'App',
  components: {
    Main,
    Login,
    register,
    Profile,
    tree
  },
  router
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
</style>
