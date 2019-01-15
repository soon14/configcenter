<template>
  <el-steps :active="active" :taskId="taskId">
    <el-step title="开始" status="finish"></el-step>
    <template v-for="item in steps">
      <el-step :title="item.operatorName" :status="item.status" :description="item.description"></el-step>
    </template>
    <el-step title="结束" :status="endStatus"></el-step>
  </el-steps>
</template>

<script>
  import {timestampToFullDate} from "../../common/date";

  export default {
    name: "TaskFlow",
    data() {
      return {
        active: 10,
        endStatus: 'finish',
        steps: [
        ]
      }
    },
    props: {
      taskId: {
        type: Number,
        required: false,
        default() {
          return 0;
        }
      }
    },
    methods: {
      timestampToFullDate(value) {
        return timestampToFullDate(value)
      },
      getTaskFlow(taskId){
        if (taskId>0){
          let params = '?taskId=' + taskId;
          this.$http.getTaskFlow(params).then((res) => {
            if (res && res.data && res.data.result){
              let processBeforeEnd = false;
              this.steps = res.data.result;
              for (let i in this.steps){
                if (processBeforeEnd){
                  this.steps[i].status = 'wait';
                }else{
                  if (this.steps[i].operationState === 1 || this.steps[i].operationState === 4){
                    if (this.steps[i].operationState === 1) {
                      this.steps[i].status = 'finish';
                    } else {
                      this.steps[i].status = 'success';
                    }
                    this.steps[i].description = this.timestampToFullDate(this.steps[i].updateTime);
                  } else {
                    processBeforeEnd = true;
                    this.active = this.steps[i].step;
                    if (this.steps[i].operationState === 3){
                      this.steps[i].status = 'process';
                    }else{
                      this.steps[i].status = 'error';
                      this.steps[i].description = this.timestampToFullDate(this.steps[i].updateTime);
                    }
                  }
                }
              }
              if (processBeforeEnd){
                this.endStatus = 'wait';
              }
            }
          }).catch(()=> {

          })
        }
      }
    },
    mounted() {
      this.getTaskFlow(this.taskId);
    },
  }
</script>

<style scoped>

</style>
