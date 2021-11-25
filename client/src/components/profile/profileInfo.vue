<template>
  <div>
    <el-container>
      <el-aside width="20vw">
        <div>
          <el-form ref="form" v-loading="loading">
            <el-form-item>
              <p class="text_font text_left">个人头像</p>
            </el-form-item>

            <el-form-item>
              <div class="avatardiv">
                <img :src=this.image_uri alt="" class="up-icon">
              </div>
            </el-form-item>

            <!--            <el-form-item>-->
            <!--              <el-button>上传<i class="el-icon-upload el-icon&#45;&#45;right"></i></el-button>-->
            <!--            </el-form-item>-->
            <el-form-item>
              <!-- TODO : token maybe && url api needed-->
              <el-upload
                  action="http://localhost:8081/api/user/uploadimage"
                  :show-file-list="false"
                  :accept="'image/*'"
                  :headers="{Authorization: this.token_up}"
                  :on-success="handleSuccess"
                  :on-error="handleError"
                  :before-upload="handleBeforeUpload"
                  :on-progress="handleProgress">
                <el-button @click="handletoken">上传<i class="el-icon-upload el-icon--right"></i></el-button>
                <!--              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
              </el-upload>
            </el-form-item>

            <el-form-item>
              <p class="text_font">png 或 jpg 格式，最大 2MB</p>
            </el-form-item>

          </el-form>

          <el-form ref="form">
            <el-form-item>
              <p class="text_font text_left">详细</p>
            </el-form-item>

            <el-form-item class="text_left">
              <el-row align="middle">
                <el-col :span="5" class="text-left">
                  <i class="el-icon-user-solid icon_size_center"></i>
                </el-col>
                <el-col :span="19" class="text-left">
                  <span>您是以下群组的成员：</span>
                  <br>
                  <span>HomeWork</span>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item class="text_left">
              <el-row align="middle">
                <el-col :span="5" class="text-left">
                  <i class="el-icon-s-data icon_size_center"></i>
                </el-col>
                <el-col :span="19" class="text-left">
                  <span>您已完成：</span>
                  <br>
                  <el-progress :percentage="50"></el-progress>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </div>
      </el-aside>

      <el-main>
        <el-form ref="form">
          <el-form-item class="text_left">
            <el-row :gutter="20">
              <el-col :span="8">
                <div>
                  username
                  <br>
                  <el-input placeholder="Put your user name here" v-model="username" :disabled="true"></el-input>
                </div>
              </el-col>

              <el-col :span="8">
                <div>
                  email
                  <br>
                  <el-row>
                    <el-col :span="18">
                      <el-input placeholder="Put your email address here" v-model="email" @change="handleEmail" :disabled="emailbind"></el-input>
                    </el-col>
                    <el-col :span="6">
                      <el-button type="primary" plain @click="bindEmail" v-if="!emailbind">验证邮箱</el-button>
                      <el-button type="primary" plain @click="unbindEmail" v-if="emailbind">解绑邮箱</el-button>
                    </el-col>
                  </el-row>
                </div>
              </el-col>

              <el-col :span="8">
                <div>
                  phone
                  <br>
                  <el-input placeholder="Put your phone number here" v-model="phone" @change="handlePhone"></el-input>
                </div>
              </el-col>

            </el-row>
          </el-form-item>

          <el-form-item class="text_left">
            <el-row :gutter="20">
              <el-col :span="8">
                <div>
                  firstName
                  <br>
                  <el-input placeholder="d" v-model="firstName" @change="handleFirstNmae"></el-input>
                </div>
              </el-col>

              <el-col :span="8">
                <div>
                  lastName
                  <br>
                  <el-input placeholder="xb" v-model="lastName" @change="handleLastName"></el-input>
                </div>
              </el-col>

              <el-col :span="8">
                <!--          <div>-->
                <!--            ?-->
                <!--            <br>-->
                <!--            <el-input placeholder="?"></el-input>-->
                <!--          </div>-->
                <el-button @click="handleGithub" v-show="githubbind === null">bind to github</el-button>
                <div v-show="githubbind">
                  You are now bind with github profile
                  <el-button @click="handleGithubUntie" v-show="githubbind !== null">Untie to github from {{this.githubbind}}</el-button>
                </div>
              </el-col>

            </el-row>
          </el-form-item>

<!--          <el-form-item>-->
<!--            <el-row>-->

<!--              <el-col :span="8">-->
<!--                &lt;!&ndash;          <div>&ndash;&gt;-->
<!--                &lt;!&ndash;            ?&ndash;&gt;-->
<!--                &lt;!&ndash;            <br>&ndash;&gt;-->
<!--                &lt;!&ndash;            <el-input placeholder="?"></el-input>&ndash;&gt;-->
<!--                &lt;!&ndash;          </div>&ndash;&gt;-->
<!--                <el-button @click="handleGithub" v-show="googlebind === null">bind to github</el-button>-->
<!--                <div v-show="githubbind">-->
<!--                  You are now bind with google profile-->
<!--                  <el-button @click="handleGithubUntie" v-show="googlebind !== null">Untie to github from {{this.githubbind}}</el-button>-->
<!--                </div>-->
<!--              </el-col>-->

<!--            </el-row>-->
<!--          </el-form-item>-->

        </el-form>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import axios from "axios";
import process from "_shelljs@0.7.8@shelljs";

axios.defaults.baseURL = process.env.API_ROOT
export default {
  name: "profileInfo",
  data() {
    return {
      username: '',
      email: '',
      passwd: '',
      firstName: '',
      lastName: '',
      phone: '',
      userId: -1,
      loading: false,
      githubbind: '',
      googlebind: '',
      token_up:'',
      image_uri:'',
      emailbind: false
    }
  },
  mounted() {
    this.showInfo()
    this.getImage()
  },
  methods: {
    getImage(){
      const that = this
      axios.post(
        '/user/getimage',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response){
            that.image_uri = response.data.data
            that.image_uri = "http://localhost:8081" + that.image_uri
            let newToken = response.headers.authorization
            if(newToken != null) window.sessionStorage.setItem('token', newToken)
          },
          function (err){

          }
      )
    },
    showInfo() {
      const that = this;
      axios.post(
          '/user/profile',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'fetch success',
                type: 'success'
              });
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            else{
              that.$message({
                message: 'fetch error',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            that.username = response.data.data.username
            that.phone = response.data.data.phone
            that.email = response.data.data.email
            that.firstName = response.data.data.firstName
            that.lastName = response.data.data.lastName
            that.userId = response.data.data.userId
            that.githubbind = response.data.data.githubName
            that.googlebind = response.data.data.googleName
            if(response.data.data.emailVerify === 1){
              that.emailbind = true
            }
            // console.log(response)
          }
      )
    },
    // upload image
    /*----- 以下为常用处理代码 ------*/
    handleSuccess(response, file, fileList) {
      this.$notify({
        title: '成功',
        message: 'upload success',
        type: 'success'
      });
      this.loading = false;
      this.getImage()
      this.$forceUpdate()
    },
    handleError() {
      this.$notify.error({
        title: '错误',
        message: 'upload failed, check your internet'
      });
      this.loading = false;
    },
    handleBeforeUpload(file) {
      const isImage = file.type.includes("image");
      if (!isImage) {
        this.$message.error("上传文件类型必须是图片!");
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error("上传图片大小不能超过 2MB!");
      }
      this.loading = false;
      return isImage && isLt2M;
    },
    handleProgress(event, file, fileList) {
      this.loading = true;  //  上传时执行loading事件
    },
    handleEmail(val) {
      // console.log(val)
      const that = this
      axios.post(
          'http://localhost:8081/api/user/edituser',
          {
            email: val
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'email change success',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            else {
              that.$message({
                message: 'email change failed',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message({
              message: 'server failed',
              type: 'error'
            })
          }
      )
    },
    handlePhone(val){
      // console.log(val)
      const that = this
      axios.post(
          'http://localhost:8081/api/user/edituser',
          {
            phone: val
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'phone change success',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            else {
              that.$message({
                message: 'phone change failed',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message({
              message: 'server failed',
              type: 'error'
            })
          }
      )
    },
    handleFirstNmae(val){
      // console.log(val)
      const that = this
      axios.post(
          'http://localhost:8081/api/user/edituser',
          {
            firstName: val
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'firstName change success',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            else {
              that.$message({
                message: 'firstName change failed',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message({
              message: 'server failed',
              type: 'error'
            })
          }
      )
    },
    handleLastName(val){
      // console.log(val)
      const that = this
      axios.post(
          'http://localhost:8081/api/user/edituser',
          {
            lastName: val
          },
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                message: 'lastName change success',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
            else {
              that.$message({
                message: 'lastName change failed',
                type: 'error'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
            }
          },
          function (err) {
            that.$message({
              message: 'server failed',
              type: 'error'
            })
          }
      )
    },
    handleGithub(event) {
      //github登录授权页面
      let oauth_uri = 'https://github.com/login/oauth/authorize'
      //github中获取
      let client_id = 'Iv1.187f346cb4978b94'
      //授权回调地址
      let redirect_uri = 'http://localhost:8080/oauth/bind'
      window.location.href = `${oauth_uri}?client_id=${client_id}&redirect_uri=${redirect_uri}`
    },
    handleGithubUntie(event) {
      const that = this
      axios.post(
          'http://localhost:8081/api/user/unbundgithub',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                    message: 'Unite success',
                    type: 'success'
                  }
              )
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
              location.reload();
            } else {
              that.$message({
                message: 'Unite failed',
                type: 'error'
              })
            }
          },
          function (err) {
            that.$message({
                  message: 'server failed',
                  type: 'error'
                }
            )
          }
      )
    },
    handleGoogle(event) {
      //github登录授权页面
      let oauth_uri = 'https://accounts.google.com/o/oauth2/v2/auth'
      //github中获取
      let client_id = '1015615497271-q5t9cf1h77n27f641i3n5pucunv9n95c.apps.googleusercontent.com'
      //授权回调地址
      let redirect_uri = 'http://localhost:8080/google/bind'
      let scope = 'https://www.googleapis.com/auth/calendar'
      window.location.href = `${oauth_uri}?client_id=${client_id}&redirect_uri=${redirect_uri}&scope=${scope}&response_type=code`
    },
    handleGoogleUntie(event) {
      const that = this
      axios.post(
          'http://localhost:8081/api/user/unbundgoogle',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response) {
            if (response.data.code === 200) {
              that.$message({
                    message: 'Unite success',
                    type: 'success'
                  }
              )
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
              location.reload();
            } else {
              that.$message({
                message: 'Unite failed',
                type: 'error'
              })
            }
          },
          function (err) {
            that.$message({
                  message: 'server failed',
                  type: 'error'
                }
            )
          }
      )
    },
    handletoken(){
      this.token_up = window.sessionStorage.getItem('token')
    },
    bindEmail(){
      const that = this
      axios.post(
          '/user/sendverifyemail',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response){
            if(response.data.code === 200){
              that.$message({
                message: '验证码发送成功，请检查邮箱',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
              that.$prompt('请输入验证码', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                // inputPattern: /[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/,
                // inputErrorMessage: '邮箱格式不正确'
              }).then(({ value }) => {
                const thatthat = that
                // that.$message({
                //   type: 'success',
                //   message: '你的邮箱是: ' + value
                // });
                axios.post(
                    '/user/verifycode',
                    {code:value},
                    {
                      headers: {
                        Authorization: window.sessionStorage.getItem('token')
                      }
                    }
                ).then(
                    function (response){
                      if(response.data.code === 200){
                        thatthat.$message({
                          message: 'Unite success',
                          type: 'success'
                        })
                        let newToken = response.headers.authorization
                        if(newToken != null) window.sessionStorage.setItem('token', newToken)
                        location.reload();
                      }
                      else{
                        thatthat.$message({
                          message: 'valid code error',
                          type: 'error'
                        })
                      }
                    }
                )
              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '取消输入'
                });
              });
            }
          },
          function (err){
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    },
    unbindEmail(){
      const that = this
      axios.post(
          '/user/unbindemail',
          {},
          {
            headers: {
              Authorization: window.sessionStorage.getItem('token')
            }
          }
      ).then(
          function (response){
            if(response.data.code === 200){
              that.$message({
                message: 'unbind success',
                type: 'success'
              })
              let newToken = response.headers.authorization
              if(newToken != null) window.sessionStorage.setItem('token', newToken)
              that.emailbind = false
              location.reload();
            }
            else {
              that.$message({
                message: 'unbind error',
                type: 'error'
              })
            }
          },
          function (err){
            that.$message({
              message: 'server error',
              type: 'error'
            })
          }
      )
    }
  }
}
</script>

<style scoped>

.avatardiv {
  border-radius: 50%;
  flex-shrink: 0;
  margin: auto;
  max-width: 20vw;
  max-height: 20vh;
}

.avatardiv > img {
  max-height: 20vh;
  max-width: 20vw;
  border-radius: 50%;
  flex-shrink: 0;
}

.displayavatar {
  max-width: 20vw;
}

.up-icon {
  max-width: 20vw;
  object-fit: contain;
}

.aside-div {
  max-height: 20vh;
  /*background-color: #00cfc8;*/
}

em {
  font-size: 5px;
  font-style: normal;
  color: #627597;
}

.line_height_300 {
  line-height: 300%;
}

.text_left {
  text-align: left;
  vertical-align: top;
}

.text_font {
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}

.print {
  background-color: #627597;
}

.text-center {
  align-items: center;
}

.icon_size_center {
  font-size: 20px;
  margin-bottom: 15px;
}

.board_Orange {
  border-radius: 30px;
  /*border-color: #c2a811;*/
  border: 5px solid #c2a811;
}
</style>
