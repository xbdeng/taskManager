<template>
  <div>
    <div>{{ githubUser }}</div>
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
  mounted() {
    let code = this.$route.query.code;
    console.log(code)
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
