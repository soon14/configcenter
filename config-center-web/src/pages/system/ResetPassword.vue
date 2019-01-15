<template>
  <div>
    <h2>密码重置</h2>
    <div class="container">
      <div class="header">
        <el-steps :active=active :center=true finish-status="success">
          <el-step title="填写帐号" description="填写需要重置密码的帐号和绑定的邮箱"></el-step>
          <el-step title="重置密码" description="填写发送至邮箱的验证码，并填写新密码"></el-step>
          <el-step title="完成" description="返回登录"></el-step>
        </el-steps>
      </div>
      <div class="body">
        <span v-show="showStep1">
          <el-form ref="step1" :model="step1" label-width="80px" :rules="step1Rules" class="form">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="step1.username"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="step1.email"></el-input>
            </el-form-item>
          </el-form>
        </span>
        <span v-show="showStep2">
          <el-form ref="step2" :model="step2" label-width="100px" :rules="step2Rules" class="form">
            <el-form-item label="验证码" prop="code">
              <el-input v-model="step2.code">
                <el-button slot="append" @click="sendCode" :disabled="isDisabled">{{buttonName}}</el-button>
              </el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPass">
              <el-input type="password" v-model="step2.newPass"></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="ensurePass">
              <el-input type="password" v-model="step2.ensurePass"></el-input>
            </el-form-item>
          </el-form>
        </span>
      </div>
      <div class="footer">
        <el-button @click="last" v-show="showLast">上一步</el-button>
        <el-button type="primary" @click="next" v-show="showNext">下一步</el-button>
        <el-button type="primary" @click="returnLogin" v-show="showReturn">返回登录</el-button>
      </div>
    </div>
  </div>
</template>

<script>
  import {CHECK_USERNAME} from '../../common/regexp'
  import {CHECK_PASSWORD} from '../../common/regexp'
  import {CHECK_AUTH_CODE} from '../../common/regexp'

  export default {
    name: "ResetPassword",
    data() {
      let validateEnsurePass = (rule, value, callback) => {
        if (value === this.step2.newPass) {
          callback();
        } else {
          callback(new Error('两次输入不一致'));
        }
      };
      return {
        active: 0,
        showNext: true,
        showLast: false,
        showReturn: false,
        showStep1: true,
        showStep2: false,
        step1:{
          username: '',
          email: ''
        },
        step2:{
          code: '',
          newPass: '',
          ensurePass: ''
        },
        isDisabled: false,
        buttonName: '发送验证码',
        time: 300,
        step1Rules: {
          username: [
            {required: true, message: '用户名不可为空', trigger: 'blur'},
            { pattern:CHECK_USERNAME, message: '用户名由英文字母和数字组成，首字母不能为数字，3-10个字符', trigger: ['blur', 'change'] }
          ],
          email: [
            {required: true, message: '邮箱不可为空', trigger: 'blur'},
            { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
          ]
        },
        step2Rules: {
          code: [
            { required: true, message: '验证码不可为空', trigger: 'blur' },
            { pattern: CHECK_AUTH_CODE, message: '验证码由10为数字组成', trigger: ['blur', 'change'] }
          ],
          newPass: [
            { required: true, message: '新密码不可为空', trigger: 'blur' },
            { pattern: CHECK_PASSWORD, message: '密码6-16个字符，只能由英文字母和数字加下划线组成', trigger: ['blur', 'change'] }
          ],
          ensurePass: [
            { required: true, message: '确认新密码不可为空', trigger: 'blur' },
            { validator: validateEnsurePass, trigger:  ['blur', 'change'] }
          ]
        }
      }
    },
    methods: {
      next() {
        let curForm = 'step1';
        if (this.active === 1 ){
          curForm = 'step2';
        }
        this.$refs[curForm].validate((valid) => {
          if (valid){
            if (this.active === 1) {
              this.resetPassword();
            }else{
              this.active++;
              this.changePage();
            }
          }
        });
      },
      last () {
        if (this.active>0) {
          this.active--;
        }
        this.changePage();
      },
      returnLogin(){
        this.$router.push("/login");
      },
      changePage () {
        switch (this.active){
          case 0:
            this.showLast = false;
            this.showReturn = false;
            this.showNext = true;
            this.showStep1 = true;
            this.showStep2 = false;
            break;
          case 1:
            this.showLast = true;
            this.showReturn = false;
            this.showNext = true;
            this.showStep1 = false;
            this.showStep2 = true;
            break;
          case 2:
            this.showLast = false;
            this.showReturn = true;
            this.showNext = false;
            this.showStep1 = false;
            this.showStep2 = false;
        }
      },
      sendCode () {
        let params = '?username='+this.step1.username+'&email='+this.step1.email;
        this.$http.getAuthCode(params).then(() => {
          this.$notify({
            title: '成功',
            message: '邮件发送成功',
            type: 'success',
            duration: 2000
          });
          this.isDisabled = true;
          let interval = window.setInterval(() => {
            this.buttonName = '（' + this.time + '秒）后重发';
            --this.time;
            if(this.time < 0) {
              this.buttonName = "重新发送";
              this.time = 10;
              this.isDisabled = false;
              window.clearInterval(interval);
            }
          }, 1000);
        })
      },
      resetPassword () {
        this.$http.resetPassword(this.step2).then(res => {
          this.$notify({
            title: '成功',
            message: res.data.result,
            type: 'success',
            duration: 2000
          });
          this.active++;
          this.changePage();
        })
      }
    }
  }
</script>

<style scoped>
  .container{
    background-color: beige;
  }
  .header{
    padding: 20px;
  }
  .body{
    padding-top: 20px;
    min-height: 100px;
  }
  .footer{
    text-align: center;
    padding-top: 20px;
    padding-bottom: 20px;
  }
  .form{
    width: 35%;
    min-width: 200px;
    margin: auto;
  }

</style>
