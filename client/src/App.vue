<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import router from './router'
import Main from './components/Main'
import Login from './components/Login'
import register from './components/register'
import Profile from './components/profile.vue'
export default {
  name: 'App',
  components: {
    Main,
    Login,
    register,
    Profile
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
