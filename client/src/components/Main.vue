<template>

  <div>
    <p>用户名：<input type="text" v-model="form.username"></input></p>
    <p>密码：<input type="password" v-model="form.password"></input></p>
    <button @click="">提交</button>
    <p>{{form.msg}}</p>

  </div>
</template>

<script>
import axios from 'axios'
export default {


  name: "Main",
  data(){
    return{
      form:{
        username:'',
        password:'',
        msg:'',
      }
    }
  },
  onSubmit(){
    this.$axios({
      url:'https://localhost:8080',
      method:'post',
      params:{
        username: this.username,
        password: this.password
      }
    }).then(response =>{this.form.msg=response.data})
  }
}
</script>

<style scoped>

</style>
