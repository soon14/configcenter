<template>
  <section>
    <el-col :span="24" class="toolbar">
      <el-form :inline="true" :model="conditions" size="small">
        <el-form-item label="操作员" v-show="isAdmin">
          <el-input v-model="conditions.operator" style="width: 150px" :disabled=true>
            <el-button slot="append" icon="el-icon-search" @click="dialogVisible=true"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select v-model="conditions.operationType" clearable placeholder="请选择操作类型">
            <el-option label="登录" value="1"></el-option>
            <el-option label="修改密码" value="2"></el-option>
            <el-option label="修改个人信息" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="操作日期">
          <el-date-picker
            v-model="conditions.time"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryOperateLog">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <my-table :table="tableData">
      <!-- 格式化时间 -->
      <template slot-scope="props" slot="createTime">
        <span>{{timestampToENDate1(props.obj.row.createTime)}}</span>
      </template>
      <!-- 格式化操作类型 -->
      <template slot-scope="props" slot="operationType">
        <span>{{ formatType(props.obj.row.operationType) }}</span>
      </template>
    </my-table>
    <!-- 分页查询 -->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync" :page-size="20" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <el-dialog
      title="查询用户"
      :visible.sync="dialogVisible"
      width="70%">
      <user-list @dealSelectRow="getUserId"></user-list>
    </el-dialog>
  </section>
</template>

<script>
  import MyTable from '../../components/Table.vue'
  import UserList from './UserList'
  import {timestampToFullDate} from "../../common/date";

  export default {
    name: "OperationLog",
    components: {
      UserList,
      MyTable
    },
    data () {
      return {
        isAdmin: this.$store.getters.isAdmin,
        dialogVisible: false,
        conditions: {
          operationType: '',
          userId: 0
        },
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        tableData: {
          data:[],
          columns:[
            {
              label:"昵称",
              props:"nickname",
            },
            {
              label:"操作类型",
              props:"operationType",
              show: true
            },
            {
              label:"操作描述",
              props:"operationDesc",
            },
            {
              label: "创建时间",
              props: "createTime",
              show: true
            }
          ]
        },
        page: 0,
        pagesync:this.page+1,
        total: 0
      }
    },
    methods: {
      timestampToENDate1(value) {
        return timestampToFullDate(value)
      },
      queryOperateLog(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getOperatorLog();
      },
      getOperatorLog() {
        let timeArray = this.conditions.time;
        let startDate;
        let endDate;
        if (timeArray){
          startDate = new Date(timeArray[0]).getTime();
          endDate = new Date(timeArray[1]).getTime();
        }
        let data = {
          userId: this.conditions.userId,
          operateType: this.conditions.operationType,
          startDate: startDate,
          endDate: endDate
        };
        let params = {
          currentPage: this.page,
          pageSize: 20,
          data: data,
        };
        this.tableData.loading = true;
        this.$http.getOperateLog(params).then((res) => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        }).catch(error => {
          this.tableData.loading = false;
        });
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getOperatorLog();
      },
      getUserId(row){
        this.dialogVisible = false;
        this.conditions.userId = row.id;
        this.conditions.operator = row.nickname;
      },
      formatType(type) {
        switch(type){
          case '1':
            return "登录";
          case '2':
            return "修改密码";
          case '3':
            return "修改个人信息";
          default:
            return "未知类型";
        }
      }
    }
  }
</script>

<style scoped>
  .toolbar {
    background: #f2f2f2;
    padding: 10px;
    margin: 10px 0px;
  }
</style>
