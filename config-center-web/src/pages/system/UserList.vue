<template>
  <!--展示系统所有用户的组件，支持复用-->
  <section>
    <el-col :span="24" class="toolbar">
      <el-form :inline="true" :model="conditions" style="margin-top: 20px;margin-left: 10px">
        <el-form-item label="用户名">
          <el-input v-model="conditions.username"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="conditions.nickname"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queyUserList">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <my-table :table="tableData" :tableEvents="tableEvents"></my-table>
    <!-- 分页查询 -->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="20" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <div style="clear: both;"></div>
  </section>

</template>

<script>
  import MyTable from '../../components/Table.vue'

  export default {
    name: "UserList",
    components: {
      MyTable
    },
    data () {
      return {
        conditions: {
          username: '',
          nickname: ''
        },
        tableData: {
          data:[],
          columns:[
            {
              label:"用户标识",
              props:"id"
            },
            {
              label:"用户名",
              props:"username"
            },
            {
              label:"昵称",
              props:"nickname",
            },
            {
              label:"邮箱帐号",
              props:"email"
            }
          ]
        },
        tableEvents: {
          rowDbClick: this.selectUser
        },
        page: 0,
        pagesync:this.page+1,
        total: 0
      }
    },
    methods: {
      queyUserList(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getUserList();
      },
      getUserList() {
        let data = {
          nickname: this.conditions.nickname,
          username: this.conditions.username
        };
        let params = {
          currentPage: this.page,
          pageSize: 20,
          data: data,
        };
        this.tableData.loading = true;
        this.$http.getUserList(params).then((res) => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        }).catch(error => {
          this.tableData.loading = false;
        });
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getUserList();
      },
      selectUser(row, event){
        this.$emit('dealSelectRow',row);
      }
    }
  }
</script>

<style scoped>
</style>
