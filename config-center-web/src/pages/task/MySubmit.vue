<template>
  <!-- 查询条件 -->
  <section>
    <el-col :span="24" class="toolbar" >
      <el-form :inline="true" :model="conditions"  size="small" style="padding-top: 20px;padding-left: 10px">
        <el-form-item label="任务类型">
          <c-select :select="typeSelectData" v-model="conditions.taskType"/>
        </el-form-item>
        <el-form-item label="任务状态">
          <c-select :select="stateSelectData" v-model="conditions.taskState"/>
        </el-form-item>
        <el-form-item label="提交时间">
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
          <el-button type="primary" @click="querySubmitTask">查询</el-button>
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
    </c-table>
    <!-- 分页查询 -->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
  </section>
</template>

<script>
  import {timestampToFullDate} from "../../common/date";
  import CTable from '../../components/Table.vue';
  import CSelect from '../../components/Select.vue';
  import {mapGetters,mapMutations} from 'vuex';
  import types from "../../store/mutation-types";
  import CTaskFlow from "./TaskFlow"

  export default {
    components: {
      CTable,
      CSelect,
      CTaskFlow
    },
    data() {
      return {
        conditions: {
          taskType: '',
          taskState: ''
        },

        page: 0,
        pagesync:this.page+1,
        total: 0,

        stateSelectData: {
          options:[
            {
              value:"2",
              text:"审核中",
            },{
              value:"3",
              text:"结束",
            }
          ],
          clearable:true,
          placeholder:"请选择任务状态",
        },
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
          loading:false,
          hasSelect:false,
          data:[],
          columns:[
            {
              label:"任务信息",
              props:"taskName",
              show:true
            },
            {
              label:"应用信息",
              props:"appName",
              show:true,
              width: 200
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
              label:"提交时间",
              props:"createTime",
              show:true,
              width:160
            }
          ],
          hasOperation:false,
          hasOperationTemplate:false
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
      querySubmitTask(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getSubmitTask();
      },
      getSubmitTask() {
        let timeArray = this.conditions.time;
        let data = {
          taskType: this.conditions.taskType,
          taskState: this.conditions.taskState,
          startDate: timeArray?timeArray[0]:null,
          endDate:timeArray?timeArray[1]:null
        };
        let params = {
          currentPage: this.page,
          pageSize: 10,
          data: data,
        };
        this.tableData.loading = true;
        this.$http.getSubmitTask(params).then((res) => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        }).catch(error => {
          this.tableData.loading = false;
        });
      },

      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getSubmitTask();
      },

      ...mapMutations({
        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
    },
    mounted() {
      let itmes = [
        {
          name:"我提交的任务",
          icon:"fa fa-tasks",
          path:"/tasks/my-created",
        },
      ];
      this.setBreadcrumbItems(itmes);
      this.getSubmitTask();
    },
  }
</script>
<style scoped>
  .toolbar {
    background: #f2f2f2;
    margin: 10px 0px;
  }
</style>
