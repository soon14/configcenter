<template>
  <div class="form">
    <el-form label-width="100px" :model="user" ref="userForm">
      <el-form-item size="small" label="姓名">
        <el-input v-model="user.nickname" style="width: 80%" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item size="small" label="公司">
        <el-input v-model="user.orgName" style="width: 80%" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item size="small" label="邮箱">
        <el-input v-model="user.email" style="width: 80%" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item size="small" label="角色">
        <el-select v-model="roleId" placeholder="请选择一个用户角色">
          <span v-for="option in roles" :key="option.roleId">
            <el-option :label="option.roleName" :value="option.roleId"></el-option>
          </span>
        </el-select>
      </el-form-item>
    </el-form>
    <el-row type="flex" justify="end" style="margin-right: 50px">
      <el-button type="primary" size="small" @click="modSubmit">确定</el-button>
    </el-row>
  </div>
</template>

<script>
  export default {
    name: "modRole",
    data() {
      return {
        roles: {},
        user: {},
        roleId: ''
      }
    },
    props: {
      personInfo: {},
    },
    methods: {
      modSubmit() {
        if(this.roleId!==''){
          let params = {
            newRoleId: this.roleId,
            orgId: this.user.orgId,
            userId: this.user.userId,
          };
          this.$http.modUserRole(params).then( res => {
            this.$message({
              message: res.data.result,
              center: true,
              type:"success"
            })
            this.$emit("handleData");
          }).catch(error => {
            console.log('err',error);
          })
        }else {
          this.$message({
            message: '请选择一个角色',
            type: 'warning'
          });
        }

      },
      getOrgRole(){
        this.$http.getOrgRole().then(res => {
          this.roles = res.data.result;
        }).catch(err =>{
          console.log('err', err);
        })
      }
    },
    created() {
      this.user = this.personInfo;
      this.getOrgRole();
    }
  }
</script>

<style scoped>

</style>
