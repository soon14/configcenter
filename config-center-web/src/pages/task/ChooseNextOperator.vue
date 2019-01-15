<template>
  <!--选择审核节点下一节点操作员-->
  <section>
    <el-row v-if="strategyStep === null">
      <span style="color: #ffff00"><b>提示：您是最后一位审核人</b></span>
    </el-row>
    <el-row v-else>
      <el-col :span="2">
        <label>组织：</label>
      </el-col>
      <el-col :span="6">
        <el-input :disabled="true" :value="strategyStep.orgName"></el-input>
      </el-col>
      <el-col :span="2">
        <label>角色：</label>
      </el-col>
      <el-col :span="6">
        <el-input :disabled="true" :value="strategyStep.roleName"></el-input>
      </el-col>
      <el-col :span="1">
        <label>：</label>
      </el-col>
      <el-col :span="7">
        <el-select :clearable="true" placeholder="请选择审核人员" :value="value" @input="change($event)">
          <el-option v-for="item in orgUsers"
            :key="item.userId"
            :label="item.nickname"
            :value="item.userId">
          </el-option>
      </el-select>
      </el-col>
    </el-row>
  </section>
</template>

<script>

  export default {
    name: "ChooseNextOperator",
    props: {
      taskId: "",
      step: "",
      value:""
    },
    data() {
      return {
        orgUsers: [],
        strategyStep: null,
      }
    },
    methods: {
      getNextOperatorInfo() {
        let params = {
          taskId: this.taskId,
          step: this.step
        };
        this.$http.getNextOperatorInfo(params).then(res => {
          let result = res.data.result;
          if (result){
            this.strategyStep = result.strategyStep?result.strategyStep:{};
            this.orgUsers = result.orgUsers?result.orgUsers:[];
          }
        }).catch(err => {
          console.log('err',err);
        })
      },
      change: function(value) {
        //将这个值抛出去，使父组件能够接受到这个值
        this.$emit('input', value);
      }
    },
    created() {
      this.getNextOperatorInfo();
    }
  }
</script>

<style scoped>

</style>
