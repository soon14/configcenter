<template>
    <section>
      <el-row style="margin-top: 10px">
        <el-select v-model="oldFile" placeholder="请选择文件的旧版本号" size="small" clearable>
          <el-option v-for="item in dataTables.data" :label="timestampToFullDate1(item.commitTime)" :value="item.commitId" :key = "item.commitId"></el-option>
        </el-select>

        <el-select v-model="newFile" placeholder="请选择新文件的版本号" size="small" clearable>
          <el-option v-for="item in dataTables.data" :label="timestampToFullDate1(item.commitTime)" :value="item.commitId" :key = "item.commitId"></el-option>
        </el-select>
        <el-button type="primary" size="mini" @click="compareDiff">比对</el-button>
      </el-row>
      <el-row style="margin-top: 10px">
        <code-view :old-string="oldString " :newString="newString">
        </code-view>
      </el-row>
    </section>
</template>
<script>
    import CodeView from "../../components/CodeView";
    import {timestampToFullDate} from "../../common/date";
    export default {
      name: "file-version-diff",
      components: {
        CodeView
      },
      props:['dataTables'],
      data() {
        return {
          newFile:"",
          oldFile:"",
          newString:"",
          oldString:"",
        }
      },
      methods: {
        timestampToFullDate1(date) {
          return timestampToFullDate(date);
        },
        compareDiff() {
          let data = {
            envId:this.dataTables.appEnvId,
            queryConfigContentVO:[
              {
                configFileId:this.dataTables.fileId,
                configFileVersion:this.oldFile,
              },
              {
                configFileId:this.dataTables.fileId,
                configFileVersion:this.newFile,
              }
            ]
          };
          this.$http.getConfigFilesContent(data).then(res => {
            this.oldString = res.data.result[0].configFileContent;
            this.newString = res.data.result[1].configFileContent;
          }).catch(err => {
            console.log('err',err);
          })
        }
      }
    }
</script>

<style scoped>

</style>
