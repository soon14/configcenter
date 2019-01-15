<template>
  <section>
    <div style="width: 100%;text-align:center;height: 30px;"><span style="font-size: large">{{fileName}}</span></div>
    <c-table :table="tableData" :tableEvents="tableEvents">
      <template slot-scope="props" slot="commitTime">
        <span>{{timestampToFullDate1(props.obj.row.commitTime)}}</span>
      </template>
      <template slot-scope="props" slot="button-operation">
        <el-tooltip effect="dark" content="推送" placement="top-start">
          <el-button type="primary" circle size="mini" @click="pushConfigs(props.obj.$index, props.obj.row)"><i
            class="fa fa-arrow-up" aria-hidden="true"></i></el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="下载" placement="top-start">
          <el-button type="success" icon="el-icon-download" circle size="mini"
                     @click="downloadFile(props.obj.$index, props.obj.row)"></el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="查看实例" placement="top-start">
          <el-button type="info" circle size="mini" @click="showVersionInstances(props.obj.$index, props.obj.row)"><i
            class="fa fa-eye" aria-hidden="true"></i></el-button>
        </el-tooltip>
      </template>
    </c-table>

    <el-dialog :title="fileoperateName" :visible.sync="readFileVisible" width="60%" :close-on-click-modal="false"
               append-to-body>
      <code-mirror :content="fileContent" v-if="readFileVisible == true" :readOnly="true"></code-mirror>
    </el-dialog>
    <el-dialog title="实例列表" :visible.sync="fileInfoVisible" append-to-body width="60%">
      <specific-version-instance :itemInfo="fileInfo" :currentEnv="dataTables.appEnvId"
                                 v-if="fileInfoVisible === true"></specific-version-instance>
    </el-dialog>
  </section>
</template>

<script>
  import CTable from "../../components/Table";
  import {timestampToFullDate} from "../../common/date";
  import SpecificVersionInstance from "./SpecificVersionInstance";
  import CodeMirror from "../../components/CodeMirror";

  export default {
    name: 'file-history-version',
    components: {
      CTable,
      SpecificVersionInstance,
      CodeMirror
    },
    props: ['dataTables', 'fileName', 'selectedEnv'],
    data() {
      return {
        fileInfo: {},
        fileInfoVisible: false,
        isDownload: false,
        currentItem: {},
        configPushVisible: false,
        configPushItems: [],
        fileContent: "",
        content: '',
        fileoperateName: "文件内容",
        readFileVisible: false,
        tableData: {
          loading: false,
          data: this.getTableData(this.dataTables),
          columns: [
            {
              label: "创建人",
              props: "creatorName",
            }, {
              label: "版本号",
              props: "commitId",
            }, {
              label: "创建时间",
              props: "commitTime",
              show: true,
            },
          ],
          hasOperation: true,
          hasOperationTemplate: true,
          operation: {
            label: "操作",
            data: []
          }
        },
        tableEvents: {
          rowDbClick: this.seeFileContent
        },
      }
    },
    methods: {
      showVersionInstances(index, row) {
        this.fileInfoVisible = true;
        this.fileInfo.configId = this.dataTables.fileId;
        this.fileInfo.configType = 1;
        this.fileInfo.configVersion = row.commitId;
      },
      pushConfigs(index, row) {
        let data = {
          envId: this.dataTables.appEnvId,
          configId: this.dataTables.fileId,
          configType: 1,
          configVersion: row.commitId,
        };
        this.$http.pushConfig(data).then(res => {
          this.$message({
            type: 'success',
            message: res.data.result
          });
        }).catch(err => {
          console.log('err', err);
        });
      },

      timestampToFullDate1(date) {
        return timestampToFullDate(date);
      },
      getTableData(msg) {
        return msg.data;
      },
      //定义查看文件内容信息的方法
      seeFileContent(row) {
        //双击数据行后显示改行的文件内容信息
        let data = [
          {
            configFileId: this.dataTables.fileId,
            configFileVersion: row.fileVersion,
          }
        ];
        let para = {
          envId: this.dataTables.appEnvId,
          queryConfigContentVO: data
        };
        this.$http.getConfigFilesContent(para).then(res => {
          this.fileContent = res.data.result[0].configFileContent;
          if (this.isDownload == true) {
            this.readFileVisible =false;
            this.content = this.fileContent;
            let aTag = document.createElement('a');
            document.body.appendChild(aTag);
            aTag.style.display = 'none';
            let blob = new Blob([this.content]);
            aTag.download = this.fileName;
            aTag.href = URL.createObjectURL(blob);
            aTag.click();
            URL.revokeObjectURL(blob);
          } else {
            this.readFileVisible = true;
          }
        }).catch(err => {
          console.log("err", err);
          this.readFileVisible = false;
        })
      },
      downloadFile(row) {
        this.isDownload = true;
        this.seeFileContent(row);
      }
    },
    created() {
    }
  }
</script>

<style></style>
