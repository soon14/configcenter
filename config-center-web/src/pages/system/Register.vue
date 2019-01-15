<template>
  <div>
    <el-header class="header">上海移动配置平台 - 用户注册</el-header>
    <hr/>
    <div class="userForm">
      <el-form ref="user" :model="user" status-icon :rules="rules" label-width="100px">
        <el-form-item label="用户名:" prop="username" auto-complete="off">
          <el-input v-model="user.username" placeholder="3-10个字符，只能由英文字母和数字组成,首字母不能为数字"></el-input>
        </el-form-item>
        <el-form-item label="昵 称:" prop="nickname" auto-complete="off">
          <el-input v-model="user.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮  箱:" prop="email" auto-complete="off">
          <el-input v-model="user.email"></el-input>
        </el-form-item>
        <el-form-item size="small" label="一级组织:" prop="orgName">
          <el-col :span="18">
            <el-input v-model="user.orgName" :disabled="true" placeholder="右侧查询（双击选中）">
              <el-button slot="append" icon="el-icon-search" @click="queryOrganization"></el-button>
            </el-input>
          </el-col>
        </el-form-item>
        <el-form-item size="small" label="角色" props="orgRoleId">
          <el-select v-model="user.orgRoleId" placeholder="请选择一个用户角色">
            <span v-for="option in roles" :key="option.roleId">
              <el-option :label="option.roleName" :value="option.roleId"></el-option>
            </span>
          </el-select>
        </el-form-item>
        <el-form-item label="密  码:" prop="password">
          <el-input type="password" v-model="user.password" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码:" prop="confirmPass">
          <el-input type="password" v-model="user.confirmPass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item class="submit">
          <el-button type="primary" @click="submitForm('user')">提交</el-button>
          <el-button @click="resetForm('user')">重置</el-button>
          <el-button @click="returnLogin()">返回登陆页面</el-button>
        </el-form-item>
      </el-form>
      <el-dialog title="一级组织列表" :visible.sync="queryOrganVisible" width="50%" :close-on-click-modal="false"
                 append-to-body>
        <Company @dealSelectRow="getOrganRow"></Company>
      </el-dialog>
      <div class="copyright">
        亚信科技（中国）有限公司 版权所有 © 2016
        <a href="http://10.9.236.162/" target="_blank">AsiaInfo</a>
      </div>
    </div>
  </div>
</template>

<script>
  import {CHECK_USERNAME, CHECK_PASSWORD} from "../../common/regexp";
  import Company from "../organization/OrganizationList"

  export default {
    components: {
      Company
    },
    data() {
      let validateConfirmPass = (rule, value, callback) => {
        if (value === "") {
          callback(new Error("请再次输入密码"));
        } else {
          if (value !== this.user.password) {
            callback(new Error("两次输入密码不一致!"));
          }
          callback();
        }
      };
      return {
        queryOrganVisible: false,
        roles: [],
        user: {
          email: "",
          nickname: "",
          password: "",
          username: "",
          confirmPass: "",
          orgName: "",
          orgId: "",
          orgRoleId: ''
        },
        rules: {
          username: [
            {required: true,
              pattern: CHECK_USERNAME,
              trigger: "blur",
              message: '3-10个字符，只能由英文字母和数字组成,首字母不能为数字！'
            }
          ],
          nickname: [
            {required: true, message: "昵称不可为空！", trigger: "blur"}
          ],
          email: [
            {required: true, message: "邮箱地址不可为空！", trigger: "blur"},
            {
              type: "email",
              message: "请输入正确的邮箱地址！",
              trigger: ["blur", "change"]
            }
          ],
          password: [
            {
              required: true,
              pattern: CHECK_PASSWORD,
              message: "密码6~16个字符，由英文字母和数字加下划线组成。",
              trigger: "blur"
            }
          ],
          confirmPass: [
            {required: true, validator: validateConfirmPass, trigger: "blur"}
          ],
          orgName: [ {required: true, message: '请选择您的一级组织！'} ],
          orgRoleId: [ {required: true, message: '请选择您的人员角色！'} ]
        }
      };
    },
    methods: {
      submitForm(user) {
        let userInfo = {
          email: this.user.email,
          nickname: this.user.nickname,
          password: this.user.password,
          username: this.user.username,
          orgId: this.user.orgId,
          orgRoleId: this.user.orgRoleId,
        };
        this.$refs['user'].validate((valid => {
            if (valid) {
              this.$http.postRegister(userInfo).then(response => {
                this.$notify({
                  title: "注册成功",
                  message: "请等待管理员审核.",
                  type: "success"
                });
                this.$refs[user].resetFields();
              });
            } else {
              return false;
            }
          }),
        )
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      returnLogin() {
        this.$router.push("/login");
      },
      queryOrganization() {
        this.queryOrganVisible = true;
      },
      getOrganRow(row) {
        this.queryOrganVisible = false;
        this.user.orgName = row.name;
        this.user.orgId = row.id;
      },
      getOrgRole(){
        this.$http.getOrgRole().then(res => {
          this.roles = res.data.result;
        }).catch(err =>{
          console.log('err', err);
        })
      }
    },
    created (){
      this.getOrgRole()
    }
  }
</script>
<style>
  .userForm {
    width: 50%;
    height: auto;
    margin: 0 auto;
    position: center;
  }
  .header {
    font-weight: 700;
    font-size: 30px;
    border: beige;
    margin-top: 15px;
  }
  .submit{
    text-align: center;
    margin-right: auto;
    margin-left: auto;
  }
  .copyright {
    font-size: 14px;
    display: block;
    padding: 20px;
    text-align: center;
    bottom: 15px;
    right: 450px;
  }
</style>
