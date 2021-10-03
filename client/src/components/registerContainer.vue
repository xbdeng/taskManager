<template>
  <div class="register-container">
    <el-container>
      <el-header>Register</el-header>
      <el-container>
        <el-main>
          <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="0px"
                   class="demo-ruleForm register-page">

            <el-row class = "front-word">
              Welcome to Task Manager!
              <br>
              Let’s begin the adventure
            </el-row>

            <el-form-item prop="username">
              <el-row class="word">Enter your name</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input v-model="ruleForm.username" autocomplete="off" :disabled="checkList[0]" class="input-text" lang="scss"></el-input>
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
                  <el-input v-model="ruleForm.email" autocomplete="off" :disabled="checkList[1]"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[1] === false">
                  <el-button @click="setTrue(1)" :disabled="enable[1]  === false">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="pass" v-show="checkList[1] === true">
              <el-row class="word">Enter your password</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input type="password" v-model="ruleForm.pass" autocomplete="off" :disabled="checkList[2]"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[2] === false">
                  <el-button @click="setTrue(2)" :disabled="enable[2]  === false">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="checkPass" v-show="checkList[2] === true">
              <el-row class="word">Enter your password again</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" :disabled="checkList[3]"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[3] === false">
                  <el-button @click="setTrue(3)" :disabled="enable[3]  === false">continue</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item prop="age" v-show="checkList[3] === true">
              <el-row class="word">Enter your age</el-row>
              <el-row>
                <el-col class="sword" :span="1">
                </el-col>
                <el-col :span="18">
                  <el-input v-model.number="ruleForm.age" :disabled="checkList[4]"></el-input>
                </el-col>
                <el-col :span="4" v-show="checkList[4] === false">
                  <el-button @click="submitForm('ruleForm')">sign up</el-button>
                </el-col>
              </el-row>
            </el-form-item>

            <el-form-item>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
<!--              <el-button @click="print">test</el-button>-->
            </el-form-item>

          </el-form>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "registerContainer",
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'));
        } else {
          if (value < 18) {
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
        callback(new Error('请输入名称'));
      } else {
        // this.enable[1] = true;
        this.$set(this.enable, 0, true);
        callback();
      }
    };
    var validateEmail = (rule, value, callback) => {
      if (value === '') {
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
            callback(new Error('请输入正确的email'))
          }
        }
        // this.enable[1] = true;
        // callback();
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        // this.enable[2] = true;
        this.$set(this.enable, 2, true)
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
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
          {validator: validatePass, trigger: 'change'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'change'}
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
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
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
  border-radius: 10px;
  margin: auto;
  width: 500px;
  height:300px;
  padding: 35px 35px 35px;
  background: transparent;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
  text-align: left;
}
.front-word{
  color: #424242;
  font-family: "Adobe Caslon Pro", "Hoefler Text", Georgia, Garamond, Times, serif;
  letter-spacing:0.1em;
  text-align:left;
  text-transform: lowercase;
  line-height: 145%;
  font-size: 14pt;
  font-variant: small-caps;
}

.word{
  font-style: normal;
  font-variant: small-caps;
  text-decoration: none;
  color: rgb(19, 104, 155);
  font-size: 17px;
  text-align:left;
  font-weight: bold;
}
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

</style>

