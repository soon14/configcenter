<template>
  <div>
    <el-col :span="24" class="toolbar">
      <el-form :inline="true" :model="conditions" style="margin-top: 20px;margin-left: 10px">
        <el-form-item size="small" label="策略名称">
          <el-input v-model="conditions.strategyName"></el-input>
        </el-form-item>
        <el-form-item size="small" label="创建人">
          <el-input v-model="conditions.creator"></el-input>
        </el-form-item>
        <el-form-item size="small" label="更新日期">
          <el-date-picker v-model="conditions.time" type="daterange" align="right"
                          unlink-panels range-separator="至"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期"
                          :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button size="small" type="primary" icon="el-icon-search" @click="quertStrategyList">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <QTable :table="tableData" :table-events="tableEvents">
      <template slot-scope="props" slot="updateTime">
        <span>{{ timestampToDate(props.obj.row.updateTime) }}</span>
      </template>
    </QTable>
    <el-col :span="24">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync" :page-size="20"
                     :total="total" style="float:right;">
      </el-pagination>
    </el-col>
  </div>
</template>

<script>
  import QTable from '../../components/Table';
  import {timestampToFullDate} from "../../common/date";

  export default {
    name: "StrategyIdList",
    components: {
      QTable
    },
    data() {
      return {
        conditions: {
          creator: '',
          time: [],
          strategyName: ''
        },
        tableData: {
          loading: false,
          data: [],
          columns: [
            {
              label: "策略名称",
              props: "strategyName"
            }, {
              label: "策略描述",
              props: "description"
            }, {
              label: "创建人",
              props: "creatorName"
            }, {
              label: "更新时间",
              props: "updateTime",
              show: true
            }, {
              label: "状态",
              props: "status"
            }
          ],
        },
        tableEvents: {
          rowDbClick: this.selectStrategy
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
        page: 0,
        pagesync:this.page+1,
        total: 0
      }
    },
    methods: {
      timestampToDate(value) {
        return timestampToFullDate(value);
      },
      quertStrategyList(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getStrategyList();
      },
      getStrategyList() {
        let appId = this.$router.history.current.params.appId;
        let timeArr = this.conditions.time;
        let beginT;
        let endT;
        if (timeArr) {
          beginT = new Date(timeArr[0]).getTime();
          endT = new Date(timeArr[1]).getTime();
        }
        let queryData = {
          appId: appId,
          startDate: beginT,
          endDate: endT,
          creatorName: this.conditions.creator,
          strategyName: this.conditions.strategyName
        };
        let params = {
          currentPage: this.page,
          pageSize: 20,
          data: queryData
        };
        this.tableData.loading = true;
        this.$http.queryStrategy(params).then(res => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        }).catch(err => {
          console.log('err', err);
          this.tableData.loading = false;
        })
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getStrategyList();
      },
      selectStrategy(row, event) {
        this.$emit('dealSelectStrategyRow', row);
      }
    },
  }
</script>
<style scoped>

</style>
