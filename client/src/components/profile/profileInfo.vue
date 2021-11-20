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
                <img src="../../assets/Test.png" alt="" class="up-icon">
              </div>
            </el-form-item>

<!--            <el-form-item>-->
<!--              <el-button>上传<i class="el-icon-upload el-icon&#45;&#45;right"></i></el-button>-->
<!--            </el-form-item>-->
            <el-form-item>
              <!-- TODO : token maybe && url api needed-->
            <el-upload
                action="https://jsonplaceholder.typicode.com/posts/"
                :show-file-list="false"
                :accept="'image/*'"
                :headers="{Authorization: this.username}"
                :on-success="handleSuccess"
                :on-error="handleError"
                :before-upload="handleBeforeUpload"
                :on-progress="handleProgress">
              <el-button>上传<i class="el-icon-upload el-icon--right"></i></el-button>
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
                  <el-input placeholder="Put your user name here" v-model="username" @change></el-input>
                </div>
              </el-col>

              <el-col :span="8">
                <div>
                  email
                  <br>
                  <el-input placeholder="Put your email address here" v-model="email"></el-input>
                </div>
              </el-col>

              <el-col :span="8">
                <div>
                  phone
                  <br>
                  <el-input placeholder="Put your phone number here" v-model="phone"></el-input>
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
                  <el-input placeholder="d" v-model="firstName"></el-input>
                </div>
              </el-col>

              <el-col :span="8">
                <div>
                  lastName
                  <br>
                  <el-input placeholder="xb" v-model="lastName"></el-input>
                </div>
              </el-col>

            </el-row>
          </el-form-item>

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
      loading: false
    }
  },
  mounted() {
    this.showInfo()
  },
  methods: {
    showInfo() {
      const that = this;
      axios.post(
          '/user/profile',
          {},
          {
            headers: {
              Authorization: window.localStorage.getItem('token')
            }
          }
      ).then(
          function (response){
            if (response.data.code === 200) {
              that.$message({
                message: 'fetch success',
                type: 'success'
              });
            }
            else{
              that.$message({
                message: 'fetch error',
                type: 'error'
              })
            }
            that.username = response.data.data.username
            that.phone = response.data.data.phone
            that.email = response.data.data.email
            that.firstName = response.data.data.firstName
            that.lastName = response.data.data.lastName
            that.userId = response.data.data.userId
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
