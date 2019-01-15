<template>
  <el-tabs type="border-card">
    <el-tab-pane label="修改基本信息">
      <el-form ref="form" status-icon :rules="rules" :model="form" label-width="80px">
        <el-form-item label="用户名" class="formItem">
          <el-input v-model="form.username" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="昵称" class="formItem" prop="nickname">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" class="formItem" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" class="formItem" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">更新</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="修改密码">
      <el-form ref="form2" status-icon :rules="rules2" :model="form2" label-width="100px">
        <el-form-item label="原密码" class="formItem" prop="oldPass">
          <el-input type="password" v-model="form2.oldPass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" class="formItem" prop="newPass">
          <el-input type="password" v-model="form2.newPass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" class="formItem" prop="ensurePass">
          <el-input type="password" v-model="form2.ensurePass" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit2">更新</el-button>
          <el-button @click="reSet">重置</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="操作日志">
      <OperationLog>
      </OperationLog>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import {REGEXP_MOBILE} from '../../common/regexp'
  import {CHECK_PASSWORD} from '../../common/regexp'
  import {updatePassword} from "../../api/api";
  import OperationLog from './OperationLog.vue';
  import {mapGetters,mapMutations} from 'vuex';
  import types from "../../store/mutation-types";

  export default {
    components: {
      OperationLog
    },
    data() {
      let user = this.$store.state.userStore;
      let validateEnsurePass = (rule, value, callback) => {
        if (value === this.form2.newPass) {
          callback();
        } else {
          callback(new Error('两次输入不一致'));
        }
      };
      return {
        form: {
          username: user.username,
          nickname: user.nickname,
          email: user.email,
          phone: user.phone
        },
        rules: {
          nickname: [
            {required: true, message: '昵称不可为空', trigger: 'blur'}
          ],
          email: [
            { required: true, message: '邮箱地址不可为空', trigger: 'blur' },
            { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
          ],
          phone: [
            {pattern: REGEXP_MOBILE, message:'请输入正确的手机号码', trigger: ['blur', 'change']}
          ]
        },
        form2: {
          oldPass:'',
          newPass:'',
          ensurePass:'',
        },
        rules2: {
          oldPass: [
            {required: true, message: '原密码不可为空', trigger: 'blur'}
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
      };
    },
    computed: {
    },
    methods: {
      onSubmit () {
        this.$refs['form'].validate((valid) => {
          if (valid) {

            this.$store.dispatch('update', this.form).then(() => {
              this.$notify({
                title: '成功',
                message: '基本信息更新成功',
                type: 'success',
                duration: 2000
              })
            })
          } else {

            return false
          }
        })
      },
      onSubmit2 () {
        this.$refs['form2'].validate((valid) => {
          if (valid) {

            updatePassword(this.form2).then(() => {
              this.reSet();
              this.$notify({
                title: '成功',
                message: '密码更新成功',
                type: 'success',
                duration: 2000
              })
      			})
          } else {

            return false
          }
        })
      },
      reSet () {
        this.$refs['form2'].resetFields()
      },
      ...mapMutations({
          setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
    },
    created() {
       let itmes = [
        {
          name:"个人信息",
          icon:"fa fa-cog",
          path:"/user-info",
        },
      ];
      this.setBreadcrumbItems(itmes);
    },
    mounted() {
    },
  }
</script>

<style scoped>
  .formItem{
    width: 40%;
    min-width: 200px;
  }
</style>
