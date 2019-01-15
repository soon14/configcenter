<template>
    <section>
        <div class="config-container">
          <div class="render-item-wrapper">
            <div class="render-item-title">
              <el-row>
                <span> 第{{sequence+1}}步</span>
                <span  style="float:right">
                  <el-button type="primary" size="mini" @click="saveItem('itemInfo')">{{item.id ? "修改":"保存"}}</el-button>
                  <el-button type="danger" size="mini" @click="deleteItem">移除</el-button>
                </span>
              </el-row>
            </div>
            <div class="config-wrapper">
              <div class="content" style="margin-top: 10px">
                <el-form ref="itemInfo" :model="itemInfo" label-width="120px" size="small" :rules="rules">
                    <el-form-item label="组织" style="margin-right: 80px" prop="organization">
                      <el-input v-model="itemInfo.organization" style="width: 100%" placeholder="点击右侧按钮选择组织" :disabled="true">
                        <el-button slot="append" icon="el-icon-search" @click="showOrganizationTree"></el-button>
                      </el-input>
                    </el-form-item>
                    <el-form-item label="角色" style="margin-right: 80px" prop="role">
                      <el-select v-model="itemInfo.role" placeholder="请选择组织" style="width: 100%;" filterable >
                        <el-option v-for="roleItem in roles" :label="roleItem.roleName" :value="roleItem.roleId" :key="roleItem.roleId"></el-option>
                      </el-select>
                    </el-form-item>
                </el-form>
              </div>
            </div>
          </div>
        </div>
      <el-dialog title="选取组织" :visible.sync="organizationTreeVisible" append-to-body>
        <el-row>
          <organization-tree ref="orgTree" bussiType="approve" :currentEnv="currentEnv" @getCurrentOrganization="getCurrentOrganization"></organization-tree>
        </el-row>
        <el-row type="flex" justify="end" style="margin-top: 10px">
          <el-button type="primary" size="small" :disabled="disabled" @click="confirmNode">确定</el-button>
        </el-row>
      </el-dialog>
    </section>
</template>

<script>
    import OrganizationTree from "../organization/OrganizationTree";
    export default {
        name: "item-info",
        props:['sequence','item','operatorType','currentEnv','approves'],
        components: {
          OrganizationTree
        },
        data() {
          return {
            disabled:true,
            organizationTreeVisible:false,
            roles:[],
            returnData:{},
            itemInfo: {
              organization:"",
              role:""
            },
            rules:{
              organization: [{required: true, message: '组织不能为空', trigger: 'blur' }],
              role: [{required: true, message: '角色不能为空', trigger: 'blur' }],
            }
          }
        },
        methods:{
          showOrganizationTree() {
            this.organizationTreeVisible = true;
          },
          getCurrentOrganization(data) {
            this.disabled = false;
            this.returnData = data;
          },
          deleteItem() {
            this.$emit("deleteItem");
          },
          isExists(id) {
            let flag = false;
            for(let i=0;i<this.approves.length;i++) {
              if(this.approves[i].orgId == id) {
                flag = true;
                break;
              }
            };
            return flag;
          },
          saveItem(formName) {
            this.$refs[formName].validate((valid) => {
              if(valid) {
                let data = {
                  envId: this.currentEnv,
                  roleId: this.itemInfo.role,
                };
                if(this.item && this.item.id != undefined) {
                  if(this.returnData && this.returnData.id != undefined) {
                    data.orgId = this.returnData.id;
                  } else {
                    data.orgId = this.item.orgId;
                  }
                  data.stepId = this.item.id;
                  this.$http.modAuditStrategyStep(data).then(res => {
                    this.$message({
                      message:"修改成功",
                      type:'success',
                    });
                  })
                } else {
                  //新增
                  if(this.isExists(this.returnData.id)) {
                    this.$message({
                      message:"请勿重新选择相同的组织",
                      type:'error',
                    });
                  } else {
                    data.orgId = this.returnData.id;
                    this.$http.addAuditStrategyStep(data).then(res => {
                      this.$message({
                        message:"保存成功",
                        type:'success',
                      });
                      let returnResult = {
                        id:res.data.result,
                        uniqueId:this.item.uniqueId,
                        orgId:this.returnData.id,
                        orgName:this.returnData.name,
                        roleId:this.itemInfo.role,
                      };
                      this.$emit('getSavedItemInfo',returnResult);
                    });
                  }
                }
              } else {
                return false;
              }
            })
          },
          confirmNode() {
            this.itemInfo.organization = this.returnData.name;
            this.organizationTreeVisible = false;
          }
        },
        created() {
          this.$http.getOrgRole().then(res => {
            this.roles = res.data.result;
          });
          if(this.operatorType == "update") {
            this.itemInfo.organization = this.item.orgName;
            this.itemInfo.role = this.item.roleId;
          }
        }
    }
</script>

<style lang="less">
  .render-item-wrapper{

  .render-item-title{
  &:hover{
     box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
   }
  position: relative;

  /*border-top: 1px solid #e8e8e8;
  border-right: 1px solid #e8e8e8;
  border-left: 1px solid #e8e8e8;*/
  border-top-left-radius:5px;
  border-top-right-radius:5px;

  padding:10px;
  font-size:14px;
  color: rgba(0, 0, 0, 0.43);
  background-color: #E7F5F2;
  }
  .title-configed{
    background-color: #E7F5F2;
  }
  .title-not-configed{
    background-color: #E2DED3;
  }
  }
  .config-container{
    margin-top: 10px;
    /*border-right: 1px solid #EFF2F7;
    border-bottom: 1px solid #EFF2F7;
    border-top: 1px solid #EFF2F7;*/
    width: 100%;
    background-color: #fff;
    padding: 10px;

  .config-clicked{
    border: 2px solid #429FFD;
    border-radius: 4px;
  }
  }

  .config-wrapper{
    background-color: #fff;
    display: inline-block;
    width: 100%;

    border: 1px solid #e8e8e8;
    border-bottom-right-radius:5px;
    border-bottom-left-radius:5px;
    transition: all .3s;

  &:hover{
     box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
   }

  .content{
    min-height: 0;
    padding:0;
  .ant-form-item{
    margin-bottom: 12px;
  }
  }
  }
</style>
