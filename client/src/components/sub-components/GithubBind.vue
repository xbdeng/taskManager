<template>
  <div>
    <div>{{ githubUser }}</div>
    <div>wwwwwwww</div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      githubUser: "github登陆中..."
    };
  },
  name: "GithubBind",
  mounted() {
    // TODO : bind with github profile (need api)
    let code = this.$route.query.code;
    const that = this
    axios.post(
        'http://localhost:8081/api/github/bind',
        {
          code:code
        }
    ).then(
        function (response) {
          if(response.data.code === 200){
            that.$message({
              message: 'github bind success',
              type: 'success'
            })
            console.log(response)
            let token = response.data.data
            window.localStorage.setItem('token', token)
            let tmpusername = response.data.data.username
            that.loginForm.username = ''
            that.loginForm.password = ''
            that.logining = false
            this.$router.push({name: 'Profile', params: {username: this.username}});
          }
        },
        function (err) {
          that.$message({
            message: 'github bind failed, please personal info page',
            type: 'error'
          })
          this.$router.push({name: 'Profile', params: {username: this.username}});
        },
    )
    // console.log(code)
    // cannot do it on client
    // const that = this
    // axios.post(
    //     'https://github.com/login/oauth/access_token',
    //     {
    //       client_secret: 'f152c15e30334c9b2357d7fd37075ba6e6adceb4',
    //       client_id: 'Iv1.187f346cb4978b94',
    //       code: code
    //     }
    // ).then(
    //     function (response) {
    //       console.log(response)
    //     },
    //     function (err) {
    //       console.log(err)
    //     }
    // )
  }
};
</script>

<style>

</style>
