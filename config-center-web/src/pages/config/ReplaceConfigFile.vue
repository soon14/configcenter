<template>
<section>
  <el-row>
    <el-form :model="oldConfigFileInfo" label-width="120px">
      <el-form-item label="文件名称">
        <el-input  v-model="fileInfo.name" style="width: 80%" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="文件描述">
        <el-input type="textarea" v-model="fileInfo.desc" style="width: 80%" placeholder="配置文件描述"></el-input>
      </el-form-item>
    </el-form>
    <upload-file ref="uploadSingle"
                 @uploadFunction="uploadFileForReplace"
                 uploadTip="请选择一个配置文件，文件最大1Mb">
    </upload-file>
  </el-row>
  <el-row style="text-align: end;">
    <el-button type="success" @click="submit">提交</el-button>
  </el-row>
</section>
</template>

<script>
  import UploadFile from "../../components/UploadFile"
  import StrategyIdList from "./QueryStrategyId"

  export default {
    name: "ReplaceConfigFile",
    components: {
      UploadFile,
      StrategyIdList
    },
    data() {
      return{
        queryStrategyVisible: false,
        fileInfo: {
          configId: this.oldConfigFileInfo.id,
          strategyId: this.oldConfigFileInfo.strategyId,
          name: this.oldConfigFileInfo.fileName,
          desc: this.oldConfigFileInfo.description
        },
        strategyName: this.oldConfigFileInfo.strategyName
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
      }
    },
    methods: {
      submit(){
        this.$refs.uploadSingle.submitUpload();
      },
      uploadFileForReplace(fileData) {
        if (!this.fileInfo.name.endsWith(fileData.name)){
          this.$message.warning(`文件名称必须与原文件一致，请重新选择`);
          this.$refs.uploadSingle.clearFiles();
          return false;
        }
        let param = {
          envId: this.currentEnv,
          configId: this.fileInfo.configId,
          desc: this.fileInfo.desc,
          strategyId: this.fileInfo.strategyId,
          file: fileData
        };
        this.$http.upConfigFileAndUpdate(param).then(res =>{
          this.$message({
            message: res.data.result,
            type: 'success',
          });
          this.$refs.uploadSingle.clearFiles();
          this.fileInfo.strategyId= '';
          this.fileInfo.desc='';
          this.strategyName = '';
          this.$emit("closeDialog");
        }).catch((err) => {
          console.log(err);
        })
      },
    }
  }
</script>

<style scoped>

</style>
