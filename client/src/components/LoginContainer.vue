<template>
    <div class="login-container">
        <el-container>
            <el-main>
                <el-form :model="ruleForm2" :rules="rules2"
                status-icon
                ref="ruleForm2"
                label-width="100px"
                class="demo-ruleForm login-page">
                    <el-row row type='flex' justify="center">
                        <h1 class="title">系统登录</h1>
                    </el-row>
                    <el-form-item prop="username" label='用户名'>
                        <el-input type="text"
                            v-model="ruleForm2.username"
                            auto-complete="off"
                            placeholder="请输入用户名"
                        ></el-input>
                    </el-form-item>
                    <el-form-item prop="password" label='密码'>
                        <el-input type="password"
                                v-model="ruleForm2.password"
                                auto-complete="off"
                                placeholder="请输入密码"
                                  show-password
                        ></el-input>
                    </el-form-item>
                        <el-checkbox
                            v-model="checked"
                            class="rememberme"
                        >记住密码</el-checkbox>
                    <el-form-item>
                      <el-col :span="12" :offset="3">
                        <el-button type="primary" @click="handleSubmit" :loading="logining">登录</el-button>
                        <el-button>注册</el-button>
                      </el-col>
                    </el-form-item>
                </el-form>
            </el-main>

        </el-container>
    </div>
</template>

<script>
export default {
    name:'LoginContainer',
    data(){
        return {
            logining: false,
            ruleForm2: {
                username: 'admin',
                password: '123456',
            },
            rules2: {
                username: [{required: true, message: 'please enter your account', trigger: 'blur'}],
                password: [{required: true, message: 'enter your password', trigger: 'blur'}]
            },
            checked: false
        }
    },
    methods: {
        handleSubmit(event){
            this.$refs.ruleForm2.validate((valid) => {
                if(valid){
                    this.logining = true;
                    if(this.ruleForm2.username === 'admin' &&
                       this.ruleForm2.password === '123456'){
                           this.logining = false;
                           sessionStorage.setItem('user', this.ruleForm2.username);
                           this.$router.push({path: '/main'});
                    }else{
                        this.logining = false;
                        this.$alert('username or password wrong!', 'info', {
                            confirmButtonText: 'ok'
                        })
                    }
                }else{
                    console.log('error submit!');
                    return false;
                }
            })
        }
    }
};
</script>

<style scoped>
.login-container {
    width: 100%;
    height: 100%;
}
.login-page {
    -webkit-border-radius: 5px;
    border-radius: 30px;
    margin: 180px auto;
    width: 500px;
    height:300px;
    padding: 35px 35px 15px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
}
label.el-checkbox.rememberme {
    margin: 0 0 15px;
    text-align: left;
}
</style>
