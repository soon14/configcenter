<template>
  <div class="login-row">
  	<el-row type="flex" justify="space-around" align="middle">
  		<el-col :span="8">
  			<el-form :model="user" :rules="rules" ref="users" class="login-form">
  				<div class="message">配置中心</div>
  				<div id="darkbannerwrap"></div>
	  			<el-form-item prop="username">
				    <el-input type=text v-model="user.username" placeholder='请输入用户名' @keyup.enter.native="onSubmit('users')"></el-input>
				</el-form-item>
				<el-form-item prop="password">
				    <el-input v-model="user.password"  type="password" placeholder="请输入密码" @keyup.enter.native="onSubmit('users')"></el-input>
				</el-form-item>
				<el-form-item>
					<span class="register" @click="register">注册</span>
                    <span class="forget-password" @click="resetPass">忘记密码?</span>
				    <el-button id="submit-button" type="primary" @click="onSubmit('users')" htmlType='submit' style="width:100%">登录</el-button>
				</el-form-item>
			</el-form>
  		</el-col>
  	</el-row>
  	<div class="copyright">
        亚信科技（中国）有限公司 版权所有 © 2018
        <a href="http://10.9.236.182/" target="_blank">AsiaInfo</a>
    </div>
  </div>
</template>

<script>
  import { mapMutations } from 'vuex'
  export default {
    data() {
      return {
      	user: {
      		username:"",
      		password:"",
      	},

      	rules: {
      		username:[
      			{ required: true, message: '请输入用户名', trigger: 'blur' }
      		],
      		password:[
      			{ required: true, message: '请输入密码', trigger: 'blur' }
      		]
      	}
      };
    },
    computed: {
    },
    methods: {
    	onSubmit(formName) {
    		this.$refs[formName].validate(valid => {
    			if(valid) {
    				this.$http.requestLogin(this.user).then(res => {
    					this.$store.dispatch('login', this.user).then(() => {
    						 this.$router.push('/home');
    					})
    				})
    			} else {
    				return false;
    			}
    		})
    	},

      register() {
      	this.$router.push("/register");
      },
      resetPass() {
    	  this.$router.push('/resetPass');
      },
    },
    mounted() {
    },
  }
</script>

<style scoped>

	.message {
	  align-self: center;
	  margin: 10px 0 0 -58px;
	  padding: 18px 10px 18px 60px;
	  background: #27A9E3;
	  position: relative;
	  color: #fff;
	  font-size: 16px;
	  font-weight: bold;
	}

	#darkbannerwrap {
	  background: url(/static/img/aiwrap.png);
	  width: 18px;
	  height: 10px;
	  margin: 0 0 20px -58px;
	  position: relative;
	}

	.login-row{
	  background:url(/static/img/loginbg.jpg) no-repeat center;
	  background-size: 100%;
	  position: fixed;
	  top: 0;
	  left: 0;
	  right: 0;
	  bottom: 0;
	}

	.login-form {
    margin: 150px auto 0 auto;
    min-height: 420px;
    max-width: 420px;
    padding: 40px;
    background-color: #ffffff;
    margin-left: auto;
    margin-right: auto;
    border-radius: 4px;
    /* overflow-x: hidden; */
    box-sizing: border-box;
  }

	/* 密码和用户名文本框*/
	/*.login-form input[type=text],*/
	.login-form el-form-item el-input[type=password] {
		  border: 1px solid #DCDEE0;
		  vertical-align: middle;
		  border-radius: 3px;
		  height: 50px;
		  padding: 0px 16px;
		  font-size: 14px;
		  color: #555555;
		  outline:none;
		  width:100%;
	}

	.register{
	  float:left;
	  cursor: pointer;
	}

	.forget-password{
	  color:#2e7fdb;
	  float:right;
	  cursor: pointer;
	}

	.forget-password:hover{
	  color:#2e7fdb;
	  font-weight: bold;
	}

	.copyright{
	  font-size:14px;
	  //color:rgba(255,255,255,0.85);
	  display:block;
	  //position:absolute;
	  padding: 20px;
	  text-align: center;
	  bottom:15px;
	  right:450px;
	}

	#submit-button{
	  display: inline-block;
	  vertical-align: middle;
	  height:48px;
	  margin: 0px;
	  font-size: 18px;
	  line-height: 24px;
	  text-align: center;
	  white-space: nowrap;
	  cursor: pointer;
	  color: #ffffff;
	  background-color: #27A9E3;
	  border-radius: 3px;
	  border: none;
	  -webkit-appearance: none;
	  outline:none;
	  width:100%;
	}
</style>
