<template>
  <div class="register-container">
    <img src="https://github.githubassets.com/images/modules/site/home/hero-glow.svg" alt="Glowing universe" class="img_Test">
    <el-container>
      <el-header>Register</el-header>
      <el-container>
        <el-main class="register-page">
          <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="0px"
                   class="demo-ruleForm">

            <el-row class = "front-word text-gray-mktg typewriter">
              <p>
              Welcome to Task Manager!
              <br>
              Let’s begin the adventure
              </p>
            </el-row>

            <el-form-item prop="username">
              <el-row class="word">Enter your name</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input v-model="ruleForm.username" autocomplete="off" :disabled="checkList[0]" class="input-text"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[0] === false">
                  <el-button @click="setTrue(0)" :disabled="enable[0] === false" class="button_register">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="email" v-show="checkList[0] === true">
              <el-row class="word">Enter your email</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input v-model="ruleForm.email" autocomplete="off" :disabled="checkList[1]" class="input-text"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[1] === false">
                  <el-button @click="setTrue(1)" :disabled="enable[1]  === false" class="button_register">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="pass" v-show="checkList[1] === true">
              <el-row class="word">Enter your password</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input type="password" v-model="ruleForm.pass" autocomplete="off" :disabled="checkList[2]" class="input-text"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[2] === false">
                  <el-button @click="setTrue(2)" :disabled="enable[2]  === false" class="button_register">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="checkPass" v-show="checkList[2] === true">
              <el-row class="word">Enter your password again</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" :disabled="checkList[3]" class="input-text"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[3] === false">
                  <el-button @click="setTrue(3)" :disabled="enable[3]  === false" class="button_register">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="age" v-show="checkList[3] === true">
              <el-row class="word">Enter your age</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input v-model.number="ruleForm.age" :disabled="checkList[4]" class="input-text"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[4] === false">
                  <el-button @click="submitForm('ruleForm')" class="button_register">sign up</el-button>
                </el-col>
              </el-row>
            </el-form-item>

<!--            <el-form-item>-->
<!--              <el-button @click="resetForm('ruleForm')">重置</el-button>-->
<!--&lt;!&ndash;              <el-button @click="print">test</el-button>&ndash;&gt;-->
<!--            </el-form-item>-->

          </el-form>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: "registerContainer",
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        this.$set(this.enable, 4, false);
        return callback(new Error('年龄不能为空'));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          this.$set(this.enable, 4, false);
          callback(new Error('请输入数字值'));
        } else {
          if (value < 18) {
            this.$set(this.enable, 4, false);
            callback(new Error('必须年满18岁'));
          } else {
            this.$set(this.enable, 4, true);
            callback();
          }
        }
      }, 1000);
    };
    var validateName = (rule, value, callback) => {
      if(value === '') {
        this.$set(this.enable, 0, false);
        callback(new Error('请输入名称'));
      } else {
        // this.enable[1] = true;
        this.$set(this.enable, 0, true);
        callback();
      }
    };
    var validateEmail = (rule, value, callback) => {
      if (value === '') {
        this.$set(this.enable, 1, false);
        callback(new Error('请输入email'));
      } else {
        if (this.ruleForm.email !== '') {
          // this.$refs.ruleForm.validateField('email');
          var test_email = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/;
          if (test_email.test(value)) {
            // this.enable[0] = true;
            this.$set(this.enable, 1, true)
            callback()
          } else {
            this.$set(this.enable, 1, false);
            callback(new Error('请输入正确的email'));
          }
        }
        // this.enable[1] = true;
        // callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        this.$set(this.enable, 2, false);
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        if (!(value.length >= 6 && value.length <= 15)) {
          this.$set(this.enable, 2, false);
          callback(new Error('密码需要在6到15个字符长度之间'))
        }
        else {
          this.$set(this.enable, 2, true);
          callback();
        }
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        this.$set(this.enable, 3, false);
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        this.$set(this.enable, 3, false);
        callback(new Error('两次输入密码不一致!'));
      } else {
        // this.enable[3] = true;
        this.$set(this.enable, 3, true)
        callback();
      }
    };
    return {
      checkList: [false, false, false, false, false],
      enable : [false, false, false, false, false],
      ruleForm: {
        username: '',
        email: '',
        pass: '',
        checkPass: '',
        age: ''
      },
      rules: {
        username: [
          {validator: validateName,  trigger: 'change'}
        ],
        email: [
          {validator: validateEmail, trigger: 'change'}
        ],
        pass: [
          {validator: validatePass, trigger: 'change'},
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'change'},
        ],
        age: [
          {validator: checkAge, trigger: 'change'}
        ]
      }
    };
  },
  methods: {
    setTrue(index) {
      // this.checkList[index] = true;
      this.$set(this.checkList, index, true)
    },
    submitForm(formName) {
      let that = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //todo 登录注册验证方式同步
          // 向后端提交登录数据
          axios.post(
            'http://localhost:8081/api/user/register',
            {
              username:that.ruleForm.username,
              email:that.ruleForm.email,
              password:that.$md5(that.ruleForm.pass),
              age:that.ruleForm.age
            }
          ).then(
            function(response) {
              alert('注册成功')
              // 清空表单
              let username = that.ruleForm.username
              that.ruleForm.username = ''
              that.ruleForm.pass = ''
              that.ruleForm.email = ''
              that.ruleForm.age = ''

              // 跳转路由
              that.$router.push('/login')
            },
            function(err) {
              alert('注册失败');
            }
          )
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      for(var i = 0; i < 5; i++){
        this.$set(this.checkList, i, false);
        this.$set(this.enable, i, false);
      }
      this.$refs[formName].resetFields();
    },
    print(){
      console.log(this.checkList)
      console.log(this.enable)
      console.log(this.ruleForm)
    }
  }
}
</script>

<style scoped>
.register-container {
  width: 100%;
  height: 100%;
  background-color: transparent;
}
.register-page{
  -webkit-border-radius: 5px;
  /*border-radius: 10px;*/
  margin: auto;
  max-width: 600px;
  /*width: 300px;*/
  /*height:600px;*/
  /*padding: 35px 35px 35px;*/
  /*border: 1px solid #eaeaea;*/
  box-shadow: 0 0 25px #103154;
  text-align: left;
  background-color: #0c162d;
  border: 1px solid #202637;
  padding: 24px !important;
  border-radius: 6px !important;
  box-sizing: border-box;
  display: block;
}
/*.front-word{*/
/*  color: #424242;*/
/*  font-family: "Adobe Caslon Pro", "Hoefler Text", Georgia, Garamond, Times, serif;*/
/*  letter-spacing:0.1em;*/
/*  text-align:left;*/
/*  text-transform: lowercase;*/
/*  line-height: 145%;*/
/*  font-size: 14pt;*/
/*  font-variant: small-caps;*/
/*}*/

.typewriter p {
  overflow: hidden; /* Ensures the content is not revealed until the animation */
  border-right: .15em solid orange; /* The typwriter cursor */
  white-space: nowrap; /* Keeps the content on a single line */
  margin: 0 auto; /* Gives that scrolling effect as the typing happens */
  letter-spacing: .15em; /* Adjust as needed */
  animation:
      typing 3.5s steps(40, end),
      blink-caret .75s step-end infinite;
}

/* The typing effect */
@keyframes typing {
  from { width: 0 }
  to { width: 100% }
}

/* The typewriter cursor effect */
@keyframes blink-caret {
  from, to { border-color: transparent }
  50% { border-color: orange; }
}

.front-word {
  font-family: ui-monospace,SFMono-Regular,SF Mono,Menlo,Consolas,Liberation Mono,monospace !important;
}

.text-gray-mktg {
  color: #627597 !important;
}

/*.word{*/
/*  font-style: normal;*/
/*  font-variant: small-caps;*/
/*  text-decoration: none;*/
/*  color: rgb(19, 104, 155);*/
/*  font-size: 17px;*/
/*  text-align:left;*/
/*  font-weight: bold;*/
/*}*/
.sword::after{
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  border-top: 1px solid #8ae821;
  border-right: 1px solid #8ae821;
  transform: rotate(45deg);
  -webkit-transform: rotate(45deg);
}

/deep/ .el-input__inner {
  height: 30px;
  background-color: rgba(255, 255, 255, 0.247);
}

/deep/ .el-form-item {
  background-color: unset !important;
}

.el-form-item{
  margin-bottom: 0px;
}

.word {
  font-family: ui-monospace,SFMono-Regular,SF Mono,Menlo,Consolas,Liberation Mono,monospace !important;
  color: #00cfc8;
  font-size: 16px !important;
}
.img_Test{
  position: absolute !important;
  overflow: hidden !important;
  top: 50%;
  left: 50%;
  width: 200%;
  transform: translate(-50%, -50%);
}
.input-text >>> .el-input__inner{
  background-color: #0c162d;
  -webkit-text-fill-color: #fff;
  border: 0;
  font-size: 17px;
}
.button_register {
  border-radius: 6px;
  background-color: transparent;
  border: 1px solid rgba(119, 250, 0, 0.88);
  color: rgba(119, 250, 0, 0.88);
}

.el-button.is-disabled{
  background-color: transparent;
  border: 1px solid rgba(175, 175, 175, 0.26);
  color: rgba(175, 175, 175, 0.26);
}


</style>

