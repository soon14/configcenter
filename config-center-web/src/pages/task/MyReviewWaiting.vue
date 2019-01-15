<template>
  <!-- 查询条件 -->
  <section>
    <el-col :span="24" class="toolbar" >
      <el-form :inline="true" :model="conditions"  size="small" style="padding-top: 20px;padding-left: 10px">
        <el-form-item label="任务类型">
          <c-select :select="typeSelectData" v-model="conditions.taskType"/>
        </el-form-item>
        <el-form-item label="提交人">
          <el-input v-model="conditions.creatorName" placeholder="任务提交人"></el-input>
        </el-form-item>
        <el-form-item label="任务编号">
          <el-input v-model="conditions.taskId" placeholder="任务编号"></el-input>
        </el-form-item>
        <el-form-item label="任务提交时间">
          <el-date-picker
            v-model="conditions.time"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="queryInReviewTask">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>

    <!-- 我创建的任务列表 -->
    <c-table :table="tableData">
      <template slot-scope="props" slot="taskName">
        <span>
          <b>{{ props.obj.row.taskName }}</b><br/>
          <span style="font-family:楷体">{{ props.obj.row.description }}</span>
        </span>
      </template>
      <!-- 格式化时间 -->
      <template slot-scope="props" slot="createTime">
        <span>{{timestampToFullDate(props.obj.row.createTime)}}</span>
      </template>
      <!-- 格式化任务类型 -->
      <template slot-scope="props" slot="taskType">
        <el-tag size="small" :color="taskTypeTagColor(props.obj.row.taskType)" type="info">{{ formatTaskType(props.obj.row.taskType) }}</el-tag>
      </template>
      <!-- 格式化任务状态 -->
      <template slot-scope="props" slot="taskState">
        <el-popover placement="bottom-end" title="任务流程" width="600" trigger="click">
          <c-task-flow :taskId="props.obj.row.taskId" v-if="props.obj.row.initTaskFlow"></c-task-flow>
          <el-button slot="reference" :type="taskStateTagType(props.obj.row.taskState)"  size="mini" @click="props.obj.row.initTaskFlow=true" plain>{{ formatTaskState(props.obj.row.taskState) }}</el-button>
        </el-popover>
      </template>
      <!-- 格式化应用信息 -->
      <template slot-scope="props" slot="appName">
        <span>
          <b>{{ props.obj.row.appName }}</b><br/>
          <h4 style="font-family:楷体;color: lightblue">{{ props.obj.row.envName }}</h4>
        </span>
      </template>
      <!-- 格式化任务提交信息 -->
      <template slot-scope="props" slot="taskInfo">
        <span>
          <b>{{ props.obj.row.creatorName }}</b><br/>
          <b>{{timestampToFullDate(props.obj.row.createTime)}}</b>
        </span>
      </template>
      <!-- 按钮的操作 -->
      <template slot-scope="props" slot="button-operation">
        <!--<el-button type="success" @click="taskPass(props.obj.row.taskId, props.obj.row.step)" size="mini">通过</el-button>
        <el-button type="danger" @click="taskRejected(props.obj.row.taskId, props.obj.row.step)" size="mini">驳回</el-button>-->
        <el-button type="success" @click="showInReviewDialog(props.obj.row)" size="mini">审核</el-button>
      </template>
    </c-table>
    <!-- 分页查询 -->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <el-dialog :title="reviewTitle" :visible.sync="reviewVisible" width="70%">
      <in-review-task v-if="reviewVisible===true" :taskDetailInfo="currentTaskDetailInfo" @closeDialog="closeDialog"></in-review-task>
    </el-dialog>
  </section>
</template>

<script>
  import {timestampToFullDate} from "../../common/date";
  import CTable from '../../components/Table.vue';
  import CSelect from '../../components/Select.vue';
  import {mapMutations} from 'vuex';
  import types from "../../store/mutation-types";
  import CTaskFlow from "./TaskFlow";
  import InReviewTask from "./InReviewTask";

  export default {
    components: {
      CTable,
      CSelect,
      CTaskFlow,
      InReviewTask
    },
    data() {
      return {
        reviewVisible: false,
        reviewTitle: "任务详情",
        currentTaskDetailInfo: {},
        conditions: {
          taskType: '',
          taskState: '',
          taskId:''
        },
        page: 0,
        pagesync:this.page+1,
        total: 0,
        typeSelectData: {
          options:[
            {
              value:"1",
              text:"配置变更任务",
            },{
              value:"2",
              text:"配置推送任务",
            }
          ],
          clearable:true,
          placeholder:"请选择任务类型",
        },
        tableData: {
          loading:false,
          hasSelect:false,
          data:[],
          columns:[
            {
              label:"任务编号",
              props:"taskId",
              width: 90
            },
            {
              label:"任务信息",
              props:"taskName",
              show:true
            },
            {
              label:"应用信息",
              props:"appName",
              show:true,
              width: 150
            },
            {
              label:"任务类型",
              props:"taskType",
              show:true,
              width:120
            },
            {
              label:"任务状态",
              props:"taskState",
              show:true,
              width:120
            },
            {
              label:"任务提交信息",
              props:"taskInfo",
              show:true,
              width:160
            }
          ],
          conditionRules: [

          ],
          hasOperation:true,
          hasOperationTemplate:true,
          operation: {
            label:"操作",
            data:[
            ],
            width: 150
          }
        }
      };
    },

    computed: {
    },
    methods: {
      timestampToFullDate(value) {
        return timestampToFullDate(value)
      },
      taskTypeTagColor(taskType) {
        switch(taskType){
          case 1:
            return '#ffee22';
          case 2:
            return "#ffeeff";
          default:
            return '#888888';
        }
      },
      formatTaskType(taskType) {
        switch(taskType){
          case 1:
            return '配置变更';
          case 2:
            return '配置推送';
          default:
            return '未知任务类型';
        }
      },
      taskStateTagType(taskState) {
        switch(taskState){
          case 2:
            return 'primary';
          case 3:
            return 'success';
          default:
            return 'error';
        }
      },
      formatTaskState(taskState) {
        switch(taskState){
          case 2:
            return '审核中';
          case 3:
            return '结束';
          default:
            return '未知任务状态';
        }
      },
      queryInReviewTask(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getInReviewTask();
      },
      getInReviewTask() {
        let taskId = Number(this.conditions.taskId);
        let timeArray = this.conditions.time;
        let data = {
          taskId: taskId,
          taskType: this.conditions.taskType,
          creatorName: this.conditions.creatorName,
          startDate: timeArray?timeArray[0]:null,
          endDate:timeArray?timeArray[1]:null
        };
        let params = {
          currentPage: this.page,
          pageSize: 10,
          data: data,
        };
        this.tableData.loading = true;
        this.$http.getInReviewTask(params).then((res) => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        }).catch(() => {
          this.tableData.loading = false;
        });
      },
      showInReviewDialog(row) {
        this.reviewVisible = true;
        this.currentTaskDetailInfo.taskId = row.taskId;
        this.currentTaskDetailInfo.step = row.step;
        this.currentTaskDetailInfo.envId = row.envId;
        this.currentTaskDetailInfo.taskType = row.taskType;
        this.reviewTitle = row.taskType === 1? "配置变更任务详情":"配置推送任务详情";
      },
      closeDialog() {
        this.reviewVisible = false;
        this.getInReviewTask();
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getInReviewTask();
      },

      ...mapMutations({
        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
    },
    mounted() {
      let itmes = [
        {
          name:"待审核的任务",
          icon:"fa fa-tasks",
          path:"/tasks/my-review-waiting",
        },
      ];
      this.setBreadcrumbItems(itmes);
      //是用来判断路由跳转过来的
      if(this.$route && this.$route.params && this.$route.params.notificationType) {
        this.conditions.taskId = this.$route.params.detailInfoLink;
      }
      this.getInReviewTask();
    },
  }
</script>
<style scoped>
</style>
