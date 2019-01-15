<template>
  <section>
    <!--配置变更任务详情组件-->
    <span v-if="taskDetailInfo.taskType === 1">
      <config-task-detail :selectedEnv="taskDetailInfo.envId" :currentTaskId="taskDetailInfo.taskId" taskState='2' @setTaskInfo="setTaskInfo"></config-task-detail>
    </span>
    <!--配置推送任务详情组件-->
    <span v-else>
      <el-row>
        <push-task-detail :selectedEnv="taskDetailInfo.envId" :currentTaskId="taskDetailInfo.taskId" taskState='2' @setTaskInfo="setTaskInfo"></push-task-detail>
      </el-row>
      <el-row>
        <label>推送时间</label>
        <el-date-picker type="datetime" size="small" v-model="pushCronTime" :disabled="true"></el-date-picker>
        <el-switch v-model="instanceChooseVisible" active-text="显示实例选择详情"></el-switch>
      </el-row>
      <el-row v-if="instanceChooseVisible">
        <el-table :data="instanceData" height="250" style="width: 100%">
          <el-table-column prop="insName" label="实例名称" width="auto"/>
          <el-table-column prop="insIp" label="实例IP地址" width="auto"/>
        </el-table>
      </el-row>
    </span>
    <el-row style="margin-top: 20px;" type="flex" justify="end" v-if="showBtn">
      <el-button size="small" @click="taskRejected">驳回</el-button>
      <el-button type="primary" size="small" @click="taskPass">通过</el-button>
    </el-row>
    <el-dialog title="确认审核通过" :visible.sync="innerVisible" :close-on-click-modal="false" append-to-body>
      <choose-next-operator :taskId="taskDetailInfo.taskId" :step="taskDetailInfo.step" v-model="nextOperator"></choose-next-operator>
      <div slot="footer">
        <el-row type="flex" justify="end">
          <el-button @click="cancel()">取消</el-button>
          <el-button type="primary" @click="ensure()">确定</el-button>
        </el-row>
      </div>
    </el-dialog>
  </section>
</template>

<script>
  import ConfigTaskDetail from "./ConfigTaskDetail";
  import PushTaskDetail from "./PushTaskDetail"
  import ChooseNextOperator from "./ChooseNextOperator";
  import {timestampToFullDate} from "../../common/date";
  export default {
    components: {
      PushTaskDetail,
      ConfigTaskDetail,
      ChooseNextOperator
    },
    name: "InReviewTask",
    data() {
      return {
        instanceChooseVisible: false,
        showBtn: false,
        nextOperator: '',
        innerVisible: false,
        instanceData:[],
        pushCronTime:''
      }
    },
    props: {
      taskDetailInfo: {
        type: Object,
        required: true
      }
    },
    methods: {
      timestampToFullDate(date){
        return timestampToFullDate(date)
      },
      taskPass() {
        this.innerVisible = true;
      },
      ensure() {
        this.dealTask(this.taskDetailInfo.taskId, 2, this.taskDetailInfo.step, '');
      },
      cancel() {
        this.innerVisible = false;
      },
      taskRejected() {
        this.$prompt('请输入驳回原因', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'textarea',
          inputPattern:/.{5,200}/,
          inputErrorMessage: '驳回原因不少于5个字符，不超过200个字符'
        }).then(({ value }) => {
          this.dealTask(this.taskDetailInfo.taskId, 3, this.taskDetailInfo.step, value);
        }).catch(() => {
          console.log("取消驳回");
        });
      },
      dealTask(taskId, operateType, step, remark) {
        let params = {
          taskId: taskId,
          step: step,
          operateType: operateType,
          nextOperator: this.nextOperator,
          remark: remark
        };
        this.$http.dealTask(params).then((res) => {
          this.$message({
            message: res.data.result,
            type: 'success'
          });
          this.$emit("closeDialog");
        })
      },
      getPushInstances(){
        let para={
          taskId:this.taskDetailInfo.taskId
        };
        this.$http.getTaskTimeAndIns(para).then(res=>{
          this.pushCronTime=timestampToFullDate(parseInt(res.data.result.pushTime));
            this.instanceData=res.data.result.taskInsVOS;
        }).catch(err=>{
          console.log("err",err);
        })
      },
      setTaskInfo(showBtn, data) {
        this.showBtn = showBtn;
       this.pushData=data;
       this.getPushInstances();
      }
    }
  }
</script>

<style>
</style>
