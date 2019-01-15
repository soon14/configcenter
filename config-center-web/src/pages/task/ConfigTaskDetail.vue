<template>
  <!--配置变更任务详情-->
  <section>
    <c-table :table="tableData">
      <template slot-scope="props" slot="changeType">
        <span>{{operateNameByType(props.obj.row.changeType)}}</span>
      </template>
      <template slot-scope="props" slot="configType">
        <span>{{configNameByType(props.obj.row.configType)}}</span>
      </template>
      <template slot-scope="props" slot="updateTime">
        {{timestampToFullDate1(props.obj.row.updateTime)}}
      </template>
      <template slot-scope="props" slot="button-operation">
        <el-tooltip class="item" v-if="taskState === '1'" effect="dark" content="撤销变更" placement="top">
          <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deletePreTask(props.obj.$index, props.obj.row)"></el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="查看变更内容详情" placement="top">
          <el-button type="primary" icon="el-icon-info" circle size="mini" @click="showDetail(props.obj.row)"></el-button>
        </el-tooltip>
      </template>
    </c-table>
    <el-row :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-row>
    <el-dialog title="配置变更内容详情" :visible.sync="changeInfoVisible" width="80%" append-to-body fullscreen>
      <code-view :oldString="oldContent" :newString="newContent"></code-view>
    </el-dialog>
    <el-dialog title="刷新策略变更详情" :visible.sync="changeStrategyVisible" width="80%" append-to-body>
      <el-row style="padding-top: 20px">
        <el-col :span="12">
          <label>原刷新策略：</label>
          <span v-if="oldStrategy">
            <span style="color: #8cc5ff">{{oldStrategy.strategyId}} {{oldStrategy.strategyName}}</span>
            <add-strategy :strategy-result="oldStrategy" operateType="show"></add-strategy>
          </span>
          <span v-else>
            <span style="color: #5daf34">无</span>
          </span>
        </el-col>
        <el-col :span="12">
          <label>新刷新策略：</label>
          <span v-if="newStrategy">
            <span style="color: #5daf34">{{newStrategy.strategyId}} {{newStrategy.strategyName}}</span>
            <add-strategy :strategy-result="newStrategy" operateType="show"></add-strategy>
          </span>
          <span v-else>
            <span style="color: #5daf34">无</span>
          </span>
        </el-col>
      </el-row>
    </el-dialog>
  </section>
</template>

<script>
  import CTable from "../../components/Table.vue";
  import CodeView from "../../components/CodeView";
  import AddStrategy from "../strategy/AddStrategy";
  import {timestampToFullDate} from "../../common/date";

  export default {
    components: {
      CTable,
      CodeView,
      AddStrategy
    },
    name: "ConfigTaskDetail",
    data() {
      return {
        changeInfoVisible:false,
        changeStrategyVisible: false,
        oldContent: '',
        newContent: '',
        oldStrategy: null,
        newStrategy: null,
        page: 0,
        total: 0,
        tableData: {
          loading:false,
          data:[],
          columns:[
            {
              label:"操作类型",
              props:"changeType",
              show:true
            },
            {
              label:"配置类型",
              props:"configType",
              show:true
            },
            {
              label:"配置名称",
              props:"configName"
            },
            {
              label:"修改时间",
              props:"updateTime",
              show:true
            },
          ],
          hasOperation:true,
          hasOperationTemplate:true,
          operation: {
            label:"操作",
            data:[
            ]
          }
        }
      }
    },
    props: {
      selectedEnv:{
        type: String,
        required:true
      },
      taskState: {
        type: String,
        required: false,
        default() {
          return '0';
        }
      },
      currentTaskId:{
        type: Number,
        required: false,
        default() {
          return 0;
        }
      }
    },
    methods: {
      timestampToFullDate1(value) {
        return timestampToFullDate(value);
      },
      operateNameByType(type) {
        switch(type){
          case 1:
            return '配置新增';
          case 2:
            return '配置修改';
          case 3:
            return '配置删除';
          case 4:
            return '策略变更';
          default:
            return '错误类型';
        }
      },
      configNameByType(type) {
        return type === 1 ? '配置文件':'配置项';
      },
      showDetail(row){
        let changeType = row.changeType;
        if (changeType === 4){
          //刷新策略变更
          this.getStrategyChangeDetail(row.id);
          this.changeStrategyVisible = true;
        } else{
          this.getConfigChangeDetail(row.id);
          this.changeInfoVisible = true;
        }
      },
      getStrategyChangeDetail(taskDetailId){
        this.oldStrategy = null;
        this.newStrategy = null;
        let params = '?taskDetailId='+taskDetailId;
        this.$http.getStrategyChangeDetail(params).then(res => {
          if (res.data.result){
            this.oldStrategy = res.data.result.oldStrategy;
            this.newStrategy = res.data.result.newStrategy;
          }
        }).catch(err => {
          console.log('err',err);
        });
      },
      getConfigChangeDetail(taskDetailId){
        this.oldContent = '';
        this.newContent = '';
        let params = '?taskDetailId='+taskDetailId;
        this.$http.getConfigChangeDetail(params).then(res => {
          if (res.data.result){
            this.oldContent = res.data.result.oldContent;
            this.newContent = res.data.result.newContent;
          }
        }).catch(err => {
          console.log('err',err);
        });
      },
      deletePreTask(index, row) {
        let params = {
          "taskDetailId": row.id,
          "taskId": row.taskId,
        };
        this.$confirm('确定要撤销此条配置变更吗？','提示',{
          type: "warning",
        }).then(() => {
          this.$http.rollbackTaskDetail(params).then(res => {
            this.$message({
              message:res.data.result,
              type:'success',
            });
            this.getConfigTask();
          }).catch(err => {
            console.log('err',err);
          })
        }).catch(() => {
        });
      },
      getConfigTask() {
        let data = {
          taskState: this.taskState,
          envId: this.selectedEnv,
          taskId: this.currentTaskId
        };
        let params = {
          currentPage:this.page,
          data:data,
          pageSize:10,
        };
        this.tableData.loading = true;
        this.$http.getConfigTaskDetail(params).then(res => {
          this.tableData.data = res.data.result.entities?res.data.result.entities:[];
          this.total = res.data.result.count;
          this.$emit("setTaskInfo", this.total>0, this.tableData.data[0]);
          this.tableData.loading = false;
        }).catch(err => {
          this.tableData.loading = false;
          console.log('err',err);
        });
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getConfigTask();
      }
    },
    created() {
      this.getConfigTask();
    }
  }
</script>

<style scoped>

</style>
