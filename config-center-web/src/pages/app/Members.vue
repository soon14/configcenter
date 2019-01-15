<template>
  <section>
    <el-row>
      <el-col :span="24" class="toolbar">
        <el-form :inline="true" :model="conditions" style="padding-top: 20px;padding-left: 10px" size="small">
          <el-form-item label="姓名">
            <el-input v-model="conditions.nickName" placeholder="请输入用户的名字"></el-input>
          </el-form-item>
          <el-form-item label="角色">
            <el-select v-model="conditions.roleId" :clearable="true" placeholder="请选择用户的角色">
              <el-option label="经理" value="1002"/>
              <el-option label="维护人员" value="1003"/>
              <el-option label="开发人员" value="1004"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="queryUser">查询</el-button>
            <el-button type="primary"  @click="addAppUser">新增人员</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <c-table :table="tableData">
        <template slot-scope="props" slot="createTime">
          <span>{{timestampToFullDate1(props.obj.row.createTime)}}</span>
        </template>
        <template slot-scope="props" slot="button-operation">
          <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="updateUserRole(props.obj.$index, props.obj.row)"></el-button>
          <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deleteUser(props.obj.$index, props.obj.row)"></el-button>
        </template>
      </c-table>
      <!-- 分页查询 -->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="10" :total="total" ref="pageObject" style="float:right;">
        </el-pagination>
      </el-col>
    </el-row>
    <el-dialog :title="appUserTitle" :visible.sync="appUserVisible" width="40%" :before-close="handleClose">
      <el-form ref="appUserForm" status-icon :rules="appUserRules" :model="appUserForm" label-width="80px">
        <el-form-item label="姓名" style="width: 52%"  prop="nickname">
          <el-input v-model="appUserForm.nickname" :disabled="isMod">
            <el-button slot="append" icon="el-icon-search" :disabled="isMod" @click="queryUserVisible=true"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleId">
          <el-select v-model="appUserForm.roleId" placeholder="请选择用户的角色">
            <el-option label="维护人员" value="1003"/>
            <el-option label="开发人员" value="1004"/>
          </el-select>
        </el-form-item>
        <el-form-item style="text-align: end;">
          <el-button type="primary" @click="addOrModAppUser">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="查询用户"
      :visible.sync="queryUserVisible"
      width="70%">
      <user-list @dealSelectRow="getUserId"></user-list>
    </el-dialog>
  </section>
</template>

<script>
  import CTable from "../../components/Table.vue";
  import CSelect from "../../components/Select.vue";
  import {timestampToFullDate} from "../../common/date";
  import {mapGetters} from 'vuex';
  import UserList from  "../../pages/system/UserList.vue";

  export default {
    components: {
      CTable,
      CSelect,
      UserList
    },
    name: "Members",
    data() {
      return {
        conditions: {
          appId: '',
          nickName: '',
          roleId: '',
        },
        currentAppUser:{},
        page: 0,
        pagesync:this.page+1,
        total: 0,
        tableData: {
          loading:false,
          data:[],
          columns:[
            {
              label:"姓名",
              props:"nickname",
            },{
              label:"角色",
              props:"roleName",
            }, {
              label:"加入时间",
              props:"createTime",
              show:true,
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
        appUserVisible: false,
        appUserTitle: '添加用户',
        queryUserVisible: false,
        isMod: true,
        appUserForm: {
          appId: this.$router.history.current.params.appId,
          roleId: '',
          nickname: '',
          userId: 0
        },
        appUserRules: {
          nickname: [
            {required: true, message: '用户不可为空', trigger: ['blur', 'change']}
          ]
        }
      };
    },
    computed: {
      ...mapGetters({
        userInfo: 'userInfo',
      }),
    },
    methods: {
      timestampToFullDate1(date) {
        return timestampToFullDate(date);
      },
      getAppUsers() {
        let appId = this.$router.history.current.params.appId;
        let data = this.conditions;
        data.appId = appId;
        let params = {
          currentPage: this.page,
          pageSize: 10,
          data: data,
        };
        this.tableData.loading = true;
        this.$http.getAppUsers(params).then(res => {
          this.tableData.loading = false;
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          let managers = res.data.result.entities.filter(item => {
            return item.roleId === 1002;
          });
          if(managers.length>0) {
            this.currentAppUser = managers[0];
          }
        }).catch(err => {
          console.log('err',err);
          this.tableData.loading = false;
        })
      },
      queryUser(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getAppUsers();
      },
      deleteUser(index,row) {
        let appId = this.$router.history.current.params.appId;
        if(row.roleId === 1002) {
          this.$message({
            message: '不能移除应用的创建人',
            type:'warning'
          })
        } else {
          this.$confirm('确定要移除此人员吗？','提示',{
            type: "warning",
          }).then(() => {
            let data = {
              appId:appId,
              userId:row.userId,
            };
            this.$http.deleteAppUser(data).then(res => {
              this.$message({
                message:res.data.result,
                type:'success',
              });
              this.getAppUsers();
            })
          })
        }
      },
      updateUserRole(index, row) {
        if (row.roleId === 1002){
          this.$message({
            message: '不能修改经理角色用户',
            type:'warning'
          });
          return;
        }
        this.appUserVisible = true;
        this.isMod = true;
        this.appUserTitle = '修改用户角色';
        this.appUserForm.userId = row.userId,
          this.appUserForm.nickname = row.nickname;
        this.appUserForm.roleId = row.roleId.toString();
      },
      addAppUser() {
        this.appUserVisible = true;
        this.isMod = false;
        this.appUserTitle = '添加用户';
        this.appUserForm.nickname = '';
        this.appUserForm.userId = 0;
        this.appUserForm.roleId = '1003';
      },
      getUserId(row){
        this.queryUserVisible = false;
        this.appUserForm.userId = row.id;
        this.appUserForm.nickname = row.nickname;
      },
      addOrModAppUser() {
        this.$refs['appUserForm'].validate((valid) => {
          if (valid){
            if (this.isMod){
              //修改
              this.$http.appModUser(this.appUserForm).then(res => {
                this.$message({
                  message: res.data.result,
                  type: 'success',
                });
                this.getAppUsers();
                this.appUserVisible = false;
              })
            }else{
              //新增
              this.$http.appAddUser(this.appUserForm).then(res => {
                this.$message({
                  message: res.data.result,
                  type: 'success',
                });
                this.getAppUsers();
                this.appUserVisible = false;
              })
            }
          }
        });

      },
      handleClose(done){
        this.$refs['appUserForm'].resetFields();
        done();
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getAppUsers();
      }
    },
    created() {
      this.getAppUsers();
    }
  }
</script>

<style>
  .toolbar {
    background: #f2f2f2;
    margin: 10px 0px;
  }
</style>
