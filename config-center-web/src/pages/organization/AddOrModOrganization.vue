<template>
    <section>
      <el-form :model="organizationInfo" label-width="120px" ref="organizationInfo" :rules="rules" size="medium">
        <el-form-item label="组织名称" prop="orgName" style="margin-right: 20px">
          <el-input v-model="organizationInfo.orgName"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description" style="margin-right: 20px">
          <el-input type="textarea" v-model="organizationInfo.description"></el-input>
        </el-form-item>
        <el-form-item label="领导" prop="leader" style="margin-right: 20px">
          <el-select v-model="organizationInfo.leader" style="width: 100%" filterable clearable>
            <el-option v-for="item in leaders" :label="item.nickname" :value="item.userId" :key="item.userId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="父组织名称" prop="preOrg" style="margin-right: 20px">
          <el-input v-model="organizationInfo.preOrg" :disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="邮件组" prop="email" style="margin-right: 20px">
          <el-input v-model="organizationInfo.email">
          </el-input>
        </el-form-item>
      </el-form>
      <el-row   type="flex" justify="end" style="margin-right: 20px">
          <el-button  size="small" @click="cancelSubmit">取消</el-button>
          <el-button type="primary" size="small" @click="confirmSubmit('organizationInfo')">提交</el-button>
      </el-row>
    </section>
</template>

<script>
  import {mapGetters} from 'vuex';
    export default {
        name: "add-or-mod-organization",
        props:['operationType','currentOrganization'],
        data() {
          return {
            organizationInfo: {
                orgName: '',
                description: '',
                leader: '',
                preOrg:'',
                email:'',
                reason:''
            },
            leaders:[],
            rules:{
              orgName: [{required: true, message: '组织名称不能为空', trigger: 'blur' }],
              preOrg: [{required: true, message: '父组织的名称不能为空', trigger: 'blur' }],
              reason: [{required: true, message: '父组织的名称不能为空', trigger: 'blur' }],
            }
          }
        },
      computed: {
        ...mapGetters({
          userInfo: 'userInfo',
        }),
      },
        methods: {
          cancelSubmit() {
              this.$emit('cancelSubmit');
          },
          confirmSubmit(form) {
            this.$refs[form].validate((valid) => {
              if (valid) {
                  let data = {
                    "description": this.organizationInfo.description,
                    "email": this.organizationInfo.email,
                    "leader": this.organizationInfo.leader,
                    "name": this.organizationInfo.orgName,
                    "orgId": this.currentOrganization.id ? this.currentOrganization.id : ''
                  };
                  if(this.operationType == "add") {
                      this.$http.addOrg(data).then(res => {
                        this.$message({
                          message:res.data.result,
                          type:"success",
                        });
                        this.$emit('updateTheOrganization');
                      }).catch(err => {
                        console.log('err',err);
                      })
                  } else if(this.operationType == "modify") {
                      if(this.userInfo.isAdmin || this.userInfo.id==this.currentOrganization.parentLeader){
                      this.$http.modOrg(data).then(res => {
                        this.$message({
                          message:res.data.result,
                          type:"success",
                        });
                        this.$emit('updateTheOrganization');
                      }).catch(err => {
                        console.log('err',err);
                      })
                    }else {
                        this.$message({
                          message:" 您非该组织的上级组织的领导者",
                          type:"error",
                        });
                      }
                  }
              } else {
                return false;
              }
            });
          }
        },
        created() {

          this.$http.getOrgUsers(this.currentOrganization.id).then(res => {
            this.leaders = res.data.result;
            if(this.operationType == "add") {
              this.organizationInfo.preOrg = this.currentOrganization.name;
            } else if(this.operationType == "modify") {
              this.$http.getOrgInfo(this.currentOrganization.id).then(res => {

                if(res.data.result.leader) {
                  let currentPerson = {
                    userId:res.data.result.leader,
                    nickname:res.data.result.leaderName
                  }
                  this.leaders.push(currentPerson);
                };
                this.organizationInfo.leader = res.data.result.leader ? res.data.result.leader:"";
                this.organizationInfo.orgName = this.currentOrganization.name;
                this.organizationInfo.description = res.data.result.description;
                this.organizationInfo.email = this.currentOrganization.email;
                this.organizationInfo.preOrg = this.currentOrganization.parentName ? this.currentOrganization.parentName:"无";
              });
            }
          });

        },
    }
</script>

<style scoped>

</style>
