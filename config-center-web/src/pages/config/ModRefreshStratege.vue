<template>
<section>
  <el-row>
    <el-form :model="oldConfigFileInfo" label-width="120px">
      <el-form-item label="文件名称">
        <el-input  v-model="fileInfo.name" style="width: 80%" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item  label="刷新策略" prop="strategyName">
        <el-input v-model="strategyName" style="width: 80%" placeholder="选择刷新策略">
          <el-button slot="append" icon="el-icon-search" @click="queryStrategy"></el-button>
        </el-input>
      </el-form-item>
      <el-form-item style="text-align: end;" >
       <el-button type="primary" @click="modStratege">提交</el-button>
      </el-form-item>
    </el-form>
  </el-row>
  <el-dialog title="刷新策略选择" :visible.sync="queryStrategyVisible" width="70%" :close-on-click-modal="true" append-to-body>
    <StrategyIdList @dealSelectStrategyRow="getStrategy"></StrategyIdList>
  </el-dialog>
</section>
</template>

<script>
  import StrategyIdList from "./QueryStrategyId"

  export default {
    name: "ModRefreshStratege",
    components: {
      StrategyIdList
    },
    data() {
      return{
        queryStrategyVisible: false,

        fileInfo: {
          configId: this.oldConfigFileInfo.id,
          strategyId: this.oldConfigFileInfo.strategyId,
          name: this.oldConfigFileInfo.fileName,
          envId:this.currentEnv,
          isSelectStrategy:false,
        },
        strategyName:this.subStrategyName,
      }
    },
    props: {
      currentEnv: {
        type: String,
        required: true
      },
      oldConfigFileInfo: {
        type: Object,
        required: true
      },
      subStrategyName:""
    },
    methods: {
      modStratege(){
        let val=this.fileInfo;
        this.$emit("submit",val);
      },
      queryStrategy() {
        this.queryStrategyVisible = true;
      },
      getStrategy(row) {
        this.fileInfo.isSelectStrategy=true;
        this.queryStrategyVisible = false;
        this.strategyName = row.strategyName;
        this.fileInfo.strategyId = row.id;
      }
    },
  }
</script>

<style scoped>

</style>
