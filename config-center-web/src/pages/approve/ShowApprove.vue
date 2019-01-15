<template>
    <section>
        <el-row>
            <el-select v-model="currentEnv" filterable size="small" @change="changeAppEnv">
              <span v-for="option in envOptions" :key="option.id">
                <el-option :label="option.envName" :value="option.id.toString()"></el-option>
              </span>
            </el-select>
            <el-button type="primary" size="small" @click="addApproveItem" style="float: right" v-if="isAdmin == true && approveInfo.length==0">新增</el-button>
        </el-row>
        <el-row style="margin-top: 10px" v-if="approveInfo.length>0">
            <el-row style="background-color: #f1f6ff;padding: 10px 0px">
              <el-col :span="22">
                <el-steps :active="approveInfo.length+1" align-center class="step-style">
                  <el-step title="开始"></el-step>
                  <el-step v-for="(step,index) in approveInfo" :title="getContentByStep(step)" :key="index"></el-step>
                </el-steps>
              </el-col>
              <el-col :span="2" v-if="isAdmin == true">
                  <el-button type="primary" icon="el-icon-edit" circle size="small" @click="editApproveItem"></el-button>
              </el-col>
            </el-row>
        </el-row>
        <el-row>
          <el-dialog :title="approveTitle" :visible.sync="addApproveVisible" width="70%" :close-on-click-modal="false" :before-close="isUpdateAllSaved">
              <add-approve ref="approveInfo" v-if="addApproveVisible === true" :approveResult="approveResult" :currentEnv="currentEnv"></add-approve>
          </el-dialog>
        </el-row>
    </section>
</template>

<script>
    import AddApprove from "./AddApprove";
    export default {
        name: "show-approve",
        components: {
          AddApprove
        },
        data() {
          return {
            envOptions:[],
            currentEnv:"",
            isAdmin:true,
            approveTitle:"",
            addApproveVisible:false,
            approveInfo:[],
            page:0,
            total:0,
            approveResult:{}
          }
        },
        methods:{
          changeAppEnv() {
            this.getAuditStrategy();
            this.checkPermission();
          },
          getContentByStep(step) {
              return step.orgName + "/" + step.roleName;
          },
          isUpdateAllSaved() {
            let result = this.$refs.approveInfo.isAllItemSaved();
            if(result) {
              this.addApproveVisible = false;
              this.getAuditStrategy();
            }
          },
          handleCurrentChange(currentPage) {
            this.page = currentPage - 1;
          },
          editApproveItem() {
            this.approveTitle = "修改审核策略";
            this.approveResult = this.approveInfo;
            this.addApproveVisible = true;
          },
          addApproveItem() {
            this.addApproveVisible = true;
            this.approveTitle = "新建审核策略";
            this.approveResult = {};
          },
          getAuditStrategy() {
            this.$http.getAuditStrategy({envId:this.currentEnv}).then(res => {
              this.approveInfo = res.data.result;
            })
          },
          checkPermission() {
            this.$http.checkPermission({envId:this.currentEnv}).then(res => {
              this.isAdmin = res.data.result;
            });
          }
        },
        created() {
          let appId = this.$router.history.current.params.appId;
          this.$http.getAppEnvs({appId:appId}).then(res => {
            this.envOptions = res.data.result;
            if(this.envOptions.length>0) {
              this.currentEnv = this.envOptions[0].id.toString();
              this.getAuditStrategy();
              this.checkPermission();
            }
          }).catch(err => {
            console.log('err',err);
          });
        }
    }
</script>

<style scoped>
  .step-style > .el-step /deep/ .el-step__main > .el-step__title{
    font-size: 12px;
  }
</style>
