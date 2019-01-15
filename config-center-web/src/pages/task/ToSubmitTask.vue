<template>
  <section>
    <!--配置变更任务详情组件-->
    <config-task-detail v-if="taskType === '1'" :selectedEnv="selectedEnv" taskState='1' @setTaskInfo="setTaskInfo"></config-task-detail>
    <!--配置推送任务详情组件-->
    <push-task-detail v-else :selectedEnv="selectedEnv" taskState='1' @setTaskInfo="setTaskInfo"></push-task-detail>
    <el-row style="margin-top: 20px;" type="flex" justify="end" v-if="showBtn">
      <el-button size="small" @click="cancelAllPreTasks">删除任务</el-button>
      <el-button type="primary" size="small" @click="submitAllTasks">提交任务</el-button>
    </el-row>
    <el-dialog title="提交任务" :visible.sync="innerVisible" :width="taskType==='1'?'50%':'70%'" :close-on-click-modal="false" append-to-body>
      <el-form :model="taskInfo" label-width="80px" ref="taskInfo" :rules="rules">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="taskInfo.taskName"></el-input>
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input type="textarea" v-model="taskInfo.description"></el-input>
        </el-form-item>
        <span v-if="taskType === '1'">
          <!--预留配置变更任务扩展信息-->
        </span>
        <span v-else>
          <el-form-item label="推送时间" prop="pushTime">
            <el-date-picker
              v-model="pushTime"
              type="datetime"
              placeholder="请选择今天以后的时间"
              :picker-options="pickerOptions">
            </el-date-picker>
            <el-switch
              v-model="instanceChooseVisible"
              active-text="显示实例选择界面">
            </el-switch>
          </el-form-item>
          <push-config-instance v-if="instanceChooseVisible" :selectedEnv="selectedEnv" ref="instancesChoose"></push-config-instance>
        </span>
        <choose-next-operator :taskId="this.currentTaskId" step="1" v-model="nextOperator"></choose-next-operator>
      </el-form>
      <div slot="footer">
        <el-row type="flex" justify="end">
          <el-button @click="cancelSubmit()">取消</el-button>
          <el-button type="primary" @click="handleSubmit('taskInfo')">提交</el-button>
        </el-row>
      </div>
    </el-dialog>
  </section>
</template>

<script>
  import ConfigTaskDetail from "./ConfigTaskDetail";
  import PushTaskDetail from "./PushTaskDetail";
  import PushConfigInstance from "../config/PushConfigInstance";
  import ChooseNextOperator from "./ChooseNextOperator";

  export default {
    components: {
      ChooseNextOperator,
      ConfigTaskDetail,
      PushTaskDetail,
      PushConfigInstance
    },
    name: "ToSubmitTask",
    data() {
      return {
        innerVisible:false,
        instanceChooseVisible: true,
        currentTaskId:'',
        nextOperator: '',
        showBtn: false,
        taskInfo: {
          taskName:'',
          description:''
        },
        pushTime: '',
        extInfo: {},
        rules: {
          taskName: [
            {required: true, message: '任务的名称不能为空', trigger: 'blur' },
            // {pattern: CHECK_APP_NAME, message:'应用的名称应该在5-64个英文字符之间，只能由英文字母和数字加上下划线组成，首字母只能为字母', trigger: ['blur', 'change']}
          ]
        },
        pickerOptions: {
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
        }
      }
    },
    props: {
      selectedEnv:{
        type: String,
        required:true
      },
      taskType: {
        type: String,
        required: false,
        default() {
          //默认为配置变更
          return "1";
        }
      }
    },
    methods: {
      cancelAllPreTasks() {
        this.$confirm(`确认删除任务？点击确定后所有修改将会被撤销`)
          .then(_ => {
            const taskId = this.currentTaskId;
            let params = {taskId:taskId};
            this.$http.rollbackTask(params).then(res => {
              this.$message({
                message:res.data.result,
                type:'success',
              });
              this.$emit('confirmCloseDialog');
            }).catch(err => {
              console.log('err',err);
            })

          })
          .catch(_ => {});
      },
      submitAllTasks() {
        this.innerVisible = true;
      },
      closeTwoDialog() {
        this.innerVisible=false;
        this.$emit('confirmCloseDialog');
      },
      cancelSubmit() {
        this.innerVisible = false;
      },
      setTaskInfo(showBtn, data) {
        this.showBtn = showBtn;
        if(data!=null){
        this.currentTaskId = data.taskId;
        }

      },
      getTaskExtInfo() {
        this.extInfo = {};
        let data = this.$refs.instancesChoose.returnResultToParent();
        if (data.selectedType === 1){
          if (data.selectedItem.length === 0){
            this.$message.warning("请选择要推送的实例");
            return false;
          }else {
            this.extInfo.selectedItem = data.selectedItem.toString();
          }
        }else {
          this.extInfo.cancelItem = data.cancelItem.toString();
          this.extInfo.insName = data.insName;
          this.extInfo.insIp = data.insIp;
        }
        this.extInfo.selectedType = data.selectedType;
        this.extInfo.pushTime = this.pushTime?this.pushTime.getTime():'';
        return true;
      },
      handleSubmit(formName) {
        this.$refs[formName].validate((valid) => {
          if(valid) {
            let params = {
              taskId:this.currentTaskId,
              taskName:this.taskInfo.taskName,
              description:this.taskInfo.description,
              nextOperator: this.nextOperator
            };
            if (this.taskType === '2'){
              if (this.getTaskExtInfo()){
                params.extInfo = this.extInfo;
              }else{
                return;
              }
            }
            this.$http.submitTask(params).then(res => {
              this.$message({
                message: res.data.result,
                type: 'success',
              });
              this.closeTwoDialog();
            }).catch(err => {
              console.log('err',err);
            })
          }
        });
      }
    }
  }
</script>

<style>
</style>
