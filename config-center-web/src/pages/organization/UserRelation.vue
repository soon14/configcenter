<template>
  <section>
    <el-row>
        <el-col :span="5">
          <organization-tree @getCurrentOrganization="getCurrentOrganization" ref="organizationTree"></organization-tree>
        </el-col>
        <el-col :span="19">
            <el-row type="flex" justify="end" v-if="showOperationVisible">
              <span class="tooltip_div" v-if="showOperation">
                <a class="span_style" @click="addOraganization">新建组织</a>
                <a class="span_style" @click="updateOraganization">修改组织</a>
                <a class="span_style" @click="deleteOraganization">删除组织</a>
              </span>
              <span class="setting_img" @click="showOperation = !showOperation">
                <i class="el-icon-setting"></i>
                <i class="el-icon-caret-bottom"></i>
              </span>
            </el-row>
          <el-row>
            <organization-table  :table="orgnizationTableData" v-on:handleValueChange="valueChange"
            >
              <template slot-scope="props" slot="button-operation">
                <el-tooltip effect="dark" content="移动" placement="top-start">
                  <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="movePerson(props.obj.$index, props.obj.row)"></el-button>
                </el-tooltip>

                <el-tooltip effect="dark" content="修改" placement="top-start">
                  <el-button type="primary" icon="el-icon-edit-outline" circle size="mini" @click="modUserRole(props.obj.$index, props.obj.row)"></el-button>
                </el-tooltip>
              </template>
            </organization-table>
          </el-row>
          <el-row>
            <!-- 分页查询 -->
            <el-col :span="24" class="toolbar">
              <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :page-size="10" :total="total" style="float:right;">
              </el-pagination>
            </el-col>
          </el-row>
        </el-col>
    </el-row>
    <el-dialog :title="operationTitle" :visible.sync="addOrModVisible" :close-on-click-modal="false" :before-close="beforeCloseDialog">
        <add-or-mod-organization
          :operationType="operationType" v-if="addOrModVisible" @cancelSubmit="beforeCancel"
          :currentOrganization="currentOrganization" @updateTheOrganization="updateTheOrganization"></add-or-mod-organization>
    </el-dialog>

    <el-dialog title="人员角色修改" :visible.sync="modInfoVisible" :close-on-click-modal="false" width="40%">
      <mod-role v-if="modInfoVisible === true" :personInfo="perInfo" @handleData="refreshData"></mod-role>
    </el-dialog>

    <el-dialog :title="operationTitle" :visible.sync="movePersonVisible" :close-on-click-modal="false">
     <move-person :selected-person="currentSelectedPersonRow" @move="movePersonToTarget"></move-person>
    </el-dialog>

  </section>
</template>

<script>
    import {mapGetters,mapMutations} from 'vuex';
    import types from "../../store/mutation-types";
    import OrganizationTree from "./OrganizationTree";
    import AddOrModOrganization from "./AddOrModOrganization";
    import OrganizationTable from "./OrganizationTable";
    import MovePerson from "./MovePerson";
    import modRole from "./modRole";
    export default {
        name: "user-relation",
        components: {
          MovePerson,
          OrganizationTable,
          OrganizationTree,
          AddOrModOrganization,
          modRole
        },
        data() {
          return {
            showOperationVisible:false,
            modInfoVisible: false,
            isAddNewPer: false,
            nickName:"",
            roleName:"",
            page:0,
            total:0,
            showOperation:true,
            currentOrganization:null,
            currentSelectedPersonRow:"",
            movePersonVisible:false,
            operationTitle:'',
            addOrModVisible:false,
            operationType:'',
            perInfo: {},
            orgnizationTableData: {
              data: [],
              columns: [
                {
                  label: "姓名",
                  props: "nickname",
                  hasSearch: true,
                },
                {
                  label: "角色",
                  props: "roleName",
                  hasSearch: true,
                },
                {
                  label: "所属组织",
                  props: "orgName",
                  hasSearch: false,
                },
                {
                  label: "邮箱",
                  props: "email",
                  hasSearch: false,
                },
              ],
              hasOperation:true,
              hasOperationTemplate:true,
              operation: {
                label:"操作",
                data:[

                ]
              }
            },
          }
        },
      computed: {
        ...mapGetters({
          userInfo: 'userInfo',
        }),
      },
        methods: {
          ...mapMutations({
            setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
          }),
          refreshData(){
            this.modInfoVisible=false;
            this.getEmployeeInfo();
          },
          getEmployeeInfo(){
              let data=[];
              let para={
                currentPage:this.page,
                data:{
                  nickname:this.nickName,
                  orgId:this.currentOrganization.id,
                  roleName:this.roleName
                },
                pageSize:10
              };
              this.$http.queryUsers(para).then(res=>{
                this.orgnizationTableData.data=res.data.result.entities;
                this.total=res.data.result.count;
                }).catch(err=>{
                  console.log("err",err);
                });
          },
          valueChange(value,type){
            if(type==='name'){
              this.nickName=value;
              let name=value;
              let employeeDatas=this.getEmployeeInfo();
              let tempArray=[];
              for(let i=0;i < employeeDatas.length;i++){
                if(employeeDatas[i].nickname.indexOf(name)!==-1)
                {
                  tempArray.push(employeeDatas[i]);
                }
              }
              this.orgnizationTableData.data=tempArray;

              if(this.nickName.length==0&&this.roleName.length==0){
                this.orgnizationTableData.data=this.getEmployeeInfo();
              }

            }if(type==='role'){
              this.roleName=value;
              let name=value;
              let employeeDatas=this.getEmployeeInfo();
              let tempArray=[];
              for(let i=0;i < employeeDatas.length;i++){
                if(employeeDatas[i].roleName.indexOf(name)!==-1)
                {
                  tempArray.push(employeeDatas[i]);
                }
              }
              this.orgnizationTableData.data=tempArray;

              if(this.nickName.length==0&&this.roleName.length==0){
                this.orgnizationTableData.data=this.getEmployeeInfo();
              }
            }

          },
          movePerson(index,row){
            this.currentSelectedPersonRow=row;
            this.operationTitle="人员移动";
            this.movePersonVisible=true;
          },
          movePersonToTarget(data){
            if(data==null||data==""){
              this.$alert('请选择目标组织', '提示', {
                confirmButtonText: '确定',
                callback: action => {

                }
              });
            }else{
                let para={
                    orgId:this.currentSelectedPersonRow.orgId,
                    targetOrgId:data.id,
                    userId:this.currentSelectedPersonRow.userId
                };
                this.$http.moveUser(para).then(res=>{
                  this.$message({
                    message:res.data.result,
                    type:"success",
                  });
                  this.movePersonVisible=false;
                  this.getEmployeeInfo();
                }).catch(err=>{
                  console.log("err",err);
                });
            }
          },
          handleCurrentChange(currentPage) {
            this.page = currentPage - 1;
            this.getEmployeeInfo();
          },
          addOraganization() {
            if(this.currentOrganization) {
              this.operationTitle = "新建组织";
              this.addOrModVisible = true;
              this.operationType = "add";
            } else {
              this.$message({
                message:"请选择父组织",
                type:"error",
              });
            }
          },
          updateOraganization() {
            if(this.currentOrganization) {
              this.operationTitle = "修改组织";
              this.addOrModVisible = true;
              this.operationType = "modify";
            } else {
              this.$message({
                message:"请选择要修改的组织",
                type:"error",
              });
            }

          },
          deleteOraganization() {
            if(this.currentOrganization) {
              if(this.currentOrganization.children && this.currentOrganization.children.length > 0) {
                this.$message({
                  message:"此组织下面还有子组织，不能删除",
                  type:"error",
                });
              } else {
                if(this.userInfo.isAdmin || this.currentOrganization.parentLeader==this.userInfo.id){
                  this.$confirm('确定要删除此组织吗？','提示',{
                    type: "warning",
                  }).then(() => {
                    this.$http.delOrg({orgId:this.currentOrganization.id}).then(res => {
                      this.$message({
                        message:res.data.result,
                        type:"success",
                      });
                      this.$refs.organizationTree.getAllLevelOneOrg();
                    });
                  }).catch(()=>{});
                }
                else {
                  this.$message({
                    message:" 您非该组织的上级组织的领导者",
                    type:"error",
                  });
                }
              }
            } else {
              this.$message({
                message:"请选择要删除的组织",
                type:"error",
              });
            }
          },
          beforeCancel(){
              this.addOrModVisible = false;
          },
          beforeCloseDialog() {
              this.addOrModVisible = false;
          },
          modUserRole(index, row){
            this.modInfoVisible = true;
            this.perInfo = row;
          },
          getCurrentOrganization(msg) {
            if(this.userInfo.isAdmin || (msg.leader && msg.leader ==this.userInfo.id)) {
              this.showOperationVisible = true;
            } else {
              this.showOperationVisible = false;
            };
            this.currentOrganization = msg;
            this.getEmployeeInfo();
          },
          updateTheOrganization() {
            this.addOrModVisible = false;
            this.$refs.organizationTree.getAllLevelOneOrg();
          }
        },
        mounted() {
          let itmes = [
            {
              name:"人员组织关系",
              icon:"fa fa-cog",
              path:"/user-relation",
            },
          ];
          this.setBreadcrumbItems(itmes);
        }
    }
</script>

<style scoped>
  .setting_img {
    cursor: pointer;
    font-size: 20px;
    background: #F4F4F4;
    width:50px;
  }

  .tooltip_div{
    background-color: white;
    border:1px solid #EEEEEE;
    border-radius:5px;
    box-shadow: -1px 2px 5px #EEEEEE;
    padding:5px 10px 5px 10px;
    margin-right: 5px
  }

  .span_style {
    cursor: pointer;
    color: #7e83ff;
    margin-left: 10px;
  }
</style>
