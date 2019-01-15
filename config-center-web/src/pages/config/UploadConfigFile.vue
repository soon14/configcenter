<template>
  <section>
    <el-row>
      <div style="text-align: end">
        <el-radio v-model="radioValue" label="single" border size="small">单配置文件上传</el-radio>
        <el-radio v-model="radioValue" label="multiple" border size="small">多配置文件上传</el-radio>
      </div>
    </el-row>
    <br>
    <el-row v-if="radioValue==='single'">
      <upload-file ref="uploadSingle"
                   @uploadFunction="uploadSingleFile"
                   uploadTip="请选择一个配置文件，文件最大1Mb">
      </upload-file>

      <el-form :model="fileInfo" label-width="120px">
        <el-form-item label="文件相对路径">
          <el-input  v-model="fileInfo.relativePath" style="width: 80%" placeholder="如果需要请输入配置文件的相对路径"></el-input>
        </el-form-item>
        <el-form-item  label="刷新策略" prop="strategyName">
          <el-input v-model="strategyName" style="width: 80%" placeholder="选择刷新策略">
            <el-button slot="append" icon="el-icon-search" @click="queryStrategy"></el-button>
          </el-input>
        </el-form-item>
        <el-form-item label="文件描述">
          <el-input type="textarea" v-model="fileInfo.desc" style="width: 80%" placeholder="配置文件描述"></el-input>
        </el-form-item>
      </el-form>

      <div style="text-align: end;">
        <el-button type="success" @click="submitUploadSingle">提交</el-button>
      </div>
    </el-row>
    <el-row v-else>
      <upload-file  ref="uploadMultiple"
                    @uploadFunction="uploadMultipleFile"
                    :fileSize="10"
                    includeSuffix="zip"
                    uploadTip="请将多个配置文件所在文件夹打包为压缩文件，只支持zip，文件最大10Mb">
      </upload-file>
      <div style="text-align: end;">
        <el-button type="success" @click="submitUploadMul">提交</el-button>
      </div>
    </el-row>
    <el-dialog title="刷新策略选择" :visible.sync="queryStrategyVisible" width="70%" :close-on-click-modal="true" append-to-body>
      <StrategyIdList @dealSelectStrategyRow="getStrategy"></StrategyIdList>
    </el-dialog>
  </section>
</template>

<script>
  import UploadFile from "../../components/UploadFile"
  import StrategyIdList from "./QueryStrategyId"

  export default {
    name: "UploadConfigFile",
    components: {
      UploadFile,
      StrategyIdList
    },
    data() {
      return {
        radioValue: "single",
        fileInfo: {
          relativePath: "",
          desc: "",
          strategyId: ''
        },
        queryStrategyVisible: false,
        strategyName:""
      }
    },
    props: {
      currentEnv: {
        type: String,
        required: true
      }
    },
    methods: {
      submitUploadSingle(){
        this.$refs.uploadSingle.submitUpload();
      },
      submitUploadMul(){
        this.$refs.uploadMultiple.submitUpload();
      },
      uploadSingleFile(fileData) {
        let param = {
          envId: this.currentEnv,
          fileSaveDir: this.fileInfo.relativePath,
          desc: this.fileInfo.desc,
          strategyId: this.fileInfo.strategyId,
          file: fileData
        };
        this.$http.uploadConfigFile(param).then(res =>{
          this.$message({
            message: res.data.result,
            type: 'success',
          });
          this.$refs.uploadSingle.clearFiles();
          this.fileInfo.strategyId= '';
          this.fileInfo.relativePath = '';
          this.fileInfo.desc='';
          this.strategyName = '';
        }).catch((err) => {
          console.log(err);
        })
      },
      uploadMultipleFile(fileData) {
        let param ={
          envId:this.currentEnv,
          zipFile:fileData
        };
        //上传压缩文件
        this.$http.uploadConfigFileYaSuo(param).then(res =>{
          this.$message({
            message: res.data.result,
            type: 'success',
          });
          this.$refs.uploadMultiple.clearFiles();
        }).catch((err) => {
          console.log(err);
        })
      },
      queryStrategy() {
        this.queryStrategyVisible = true;
      },
      getStrategy(row) {
        this.queryStrategyVisible = false;
        this.strategyName = row.strategyName;
        this.fileInfo.strategyId = row.id;
      }
    }
  }
</script>

<style scoped>

</style>
