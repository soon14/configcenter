<template>
  <!-- 查询条件 -->
  <section>
    <el-col :span="24" class="toolbar" >
      <el-form :inline="true" :model="conditions" size="small" style="padding-top: 20px;padding-left: 10px">
        <el-form-item label="姓名">
          <el-input v-model="conditions.nickName" placeholder="请输入用户的名字"></el-input>
        </el-form-item>
        <el-form-item label="用户状态">
          <c-select :select="selectData" v-model="conditions.userStatus"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryUsers">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!-- 用户列表 -->
    <c-table :table="tableData">
      <!-- 格式化时间 -->
      <template slot-scope="props" slot="createTime">
        <span>{{timestampToENDate1(props.obj.row.createTime)}}</span>
      </template>
      <!-- 格式化用户的状态 -->
      <template slot-scope="props" slot="userStatus">
        <el-tag size="medium" :type="tagType(props.obj.row.userStatus)">{{ userState(props.obj.row.userStatus) }}</el-tag>
      </template>
      <!-- 按钮的操作 -->
      <template slot-scope="props" slot="button-operation">
        <el-button :type="buttonType(props.obj.row.userStatus)"
                   @click="changeUserState(props.obj.$index, props.obj.row)"
                   size="mini">{{buttonText(props.obj.row.userStatus)}}</el-button>
      </template>
    </c-table>
    <!-- 分页查询 -->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync" :page-size="20" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
  </section>
</template>

<script>
  import {timestampToENDate} from "../../common/date";
  import CTable from '../../components/Table.vue';
  import CSelect from '../../components/Select.vue';
  import {mapGetters,mapMutations} from 'vuex';
  import types from "../../store/mutation-types";
  export default {
    components: {
      CTable,
      CSelect
    },
    data() {
      return {
        conditions: {
          nickName: '',
          userStatus: '',
        },

        page: 0,
        pagesync:this.page+1,
        total: 0,

        selectData: {
          options:[
            {
              value:"1",
              text:"有效",
            },{
              value:"0",
              text:"失效",
            },
            {
              value:"2",
              text:"待审核",
            }
          ],
          clearable:true,
          placeholder:"请选择用户的状态",
        },

        tableData: {
          loading:false,
          hasSelect:true,
          data:[],
          columns:[
            {
              label:"姓名",
              props:"nickname",
            },{
              label:"邮箱",
              props:"email",
            }, {
              label:"加入时间",
              props:"createTime",
              show:true,//使用自定义的模板
            },{
              label:"用户角色",
              props:"roleName"
            }, {
              label:"状态",
              props:"userStatus",
              show:true,
            }
          ],
          hasOperation:true,
          hasOperationTemplate:true,
          operation: {
            label:"操作",
            data:[
            ]
          }
        }
      };
    },

    computed: {
    },
    methods: {
      timestampToENDate1(value) {
        return timestampToENDate(value)
      },
      changeUserState(index, row) {
        let params = {
          accountIds:[row.userId],
        };
        if(row.userStatus === 1) {
          this.$confirm('确定要注销用户吗？','提示', {
            type: "warning",
          }).then(() => {
            this.tableData.loading = true;
            this.$http.closeAccount(params).then(res => {
              this.tableData.loading = false;
              this.$message({
                message: '操作成功',
                type: 'success'
              });
              this.getUsers();
            }).catch(error => {
              console.log("error",error);
              this.tableData.loading = false;
            })
          }).catch(error => {
            console.log("error",error);
            this.tableData.loading = false;
          })
        } else if(row.userStatus === 2) {
          this.$confirm('是否同意用户加入平台', '提示', {
            confirmButtonText: '同意',
            cancelButtonText: '不同意',
            type: 'warning'
          }).then(() => {
            this.tableData.loading = true;
            params.operate = 1;
            this.$http.auditAccount(params).then(res => {
              this.tableData.loading = false;
              this.$message({
                message: '激活用户成功',
                type: 'success'
              });
              this.getUsers();
            })
          }).catch(() => {
            this.tableData.loading = true;
            params.operate = 2;
            this.$http.auditAccount(params).then(res => {
              this.tableData.loading = false;
              this.$message({
                message: '取消用户加入成功',
                type: 'success'
              });
              this.getUsers();
            }).catch(error => {
              console.log("error",error);
              this.tableData.loading = false;
            })
          });
        }
      },
      queryUsers(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getUsers();
      },
      getUsers() {
        let data = {
          nickName: this.conditions.nickName,
          userStatus: this.conditions.userStatus?this.conditions.userStatus:-1,
        };
        let params = {
          currentPage: this.page,
          pageSize: 20,
          data: data,
        };
        this.tableData.loading = true;
        this.$http.getUserListPage(params).then((res) => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;

          this.tableData.loading = false;
        }).catch(error => {
          this.tableData.loading = false;
        });
      },

      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getUsers();
      },

      userState(status) {
        return status === 1 ? "有效" : status === 0 ? "失效" : "待审核";
      },
      tagType(status) {
        return status === 1 ? "success" : status === 0 ? "danger" : "warning";
      },
      buttonType(status) {
        return status === 1 ? "danger" : status === 0 ? "info" : "success";
      },
      buttonText(status) {
        return status === 1 ? "注销" : status === 0 ? "激活" : "审核";
      },
      ...mapMutations({
        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
    },
    mounted() {
      let itmes = [
        {
          name:"用户管理",
          icon:"fa fa-cog",
          path:"/user-manage",
        },
      ];
      this.setBreadcrumbItems(itmes);
      //是用来判断路由跳转过来的
      if(this.$route && this.$route.params && this.$route.params.notificationType) {
        this.conditions.nickName = this.$route.params.creatorNickName,
          this.conditions.userStatus = "2";
      }
      this.getUsers();
    },
  }
</script>
<style scoped>
  .toolbar {
    background: #f2f2f2;
    margin: 10px 0px;
  }
</style>
