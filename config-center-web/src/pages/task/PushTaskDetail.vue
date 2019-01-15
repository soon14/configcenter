<template>
  <!--配置变更任务详情-->
  <section>
    <c-table :table="tableData">
      <template slot-scope="props" slot="configType">
        <span>{{configNameByType(props.obj.row.configType)}}</span>
      </template>
      <template slot-scope="props" slot="createTime">
        {{timestampToFullDate1(props.obj.row.createTime)}}
      </template>
      <template slot-scope="props" slot="button-operation">
        <el-tooltip class="item" v-if="taskState === '1'" effect="dark" content="撤销推送" placement="top">
          <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deletePreTask(props.obj.$index, props.obj.row)"></el-button>
        </el-tooltip>
        <el-tooltip class="item" effect="dark" content="查看配置内容详情" placement="top">
          <el-button type="primary" icon="el-icon-info" circle size="mini" @click="showConfigContent(props.obj.row)"></el-button>
        </el-tooltip>
      </template>
    </c-table>
    <el-row :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-row>
    <el-dialog title="配置内容详情" :visible.sync="configContentVisible" width="80%" append-to-body fullscreen>
      <div v-highlight>
        <pre><code v-html="configContent"></code></pre>
      </div>
    </el-dialog>
  </section>
</template>

<script>
  import CTable from "../../components/Table.vue";
  import {timestampToFullDate} from "../../common/date";

  export default {
    components: {
      CTable
    },
    name: "PushTaskDetail",
    data() {
      return {
        configContentVisible:false,
        configContent: '',
        page: 0,
        total: 0,
        tableData: {
          loading:false,
          data:[],
          columns:[
            {
              label:"配置类型",
              props:"configType",
              show:true
            },
            {
              label:"配置名称",
              props:"configName"
            },
            {
              label: "配置版本",
              props:"configVersion"
            },
            {
              label:"创建时间",
              props:"createTime",
              show:true
            },
          ],
          hasOperation:true,
          hasOperationTemplate:true,
          operation: {
            label:"操作",
            data:[
            ]
          }
        }
      }
    },
    props: {
      selectedEnv:"",
      taskState: {
        type: String,
        required: false,
        default() {
          return '0';
        }
      },
      currentTaskId:{
        type: Number,
        required: false,
        default() {
          return 0;
        }
      }
    },
    methods: {
      timestampToFullDate1(value) {
        return timestampToFullDate(value);
      },
      operateNameByType(type) {
        return type === 1 ? '新增':type === 2 ? '修改':'删除';
      },
      configNameByType(type) {
        return type === 1 ? '配置文件':'配置项';
      },
      showConfigContent(row) {
        this.configContent = '';
        switch(row.configType) {
          case 1:
            this.getConfigFileByVersion(row.configId, row.configVersion);
            break;
          case 2:
            this.getVersionContent(row.configId, row.configVersion);
            break;
        }
        this.configContentVisible = true;
      },
      getVersionContent(configItemId, configItemHisId) {
        let params = {
          configItemId: configItemId,
          configItemHisId: configItemHisId,
          envId: this.selectedEnv
        };
        this.$http.getVersionContent(params).then(res => {
          this.configContent = res.data.result.itemValue;
        }).catch(err => {
          console.log('err',err);
        })
      },
      getConfigFileByVersion(configFileId, version) {
        let params = {
          envId: this.selectedEnv,
          queryConfigContentVO: [
            {
              configFileId: configFileId,
              configFileVersion: version
            }
          ]
        };
        this.$http.getConfigFilesContent(params).then(res => {
          if (res.data.result && res.data.result[0]){
            this.configContent = "<xmp>"+res.data.result[0].configFileContent+"</xmp>";
          }
        }).catch(err => {
          console.log('err',err);
        })
      },
      deletePreTask(index, row) {
        let params = {
          "taskDetailId": row.id,
          "taskId": row.taskId,
        };
        this.$confirm('确定要撤销此条配置的推送吗？','提示',{
          type: "warning",
        }).then(() => {
          this.$http.rollbackTaskDetail(params).then(res => {
            this.$message({
              message:res.data.result,
              type:'success',
            });
            this.getPushTask();
          }).catch(err => {
            console.log('err',err);
          })
        }).catch(() => {
        })
      },
      getPushTask() {
        let data = {
          taskState: this.taskState,
          envId: this.selectedEnv,
          taskId: this.currentTaskId
        };
        let params = {
          currentPage:this.page,
          data:data,
          pageSize:10,
        };
        this.tableData.loading = true;
        this.$http.getPushTaskDetail(params).then(res => {
          this.tableData.data = res.data.result.entities?res.data.result.entities:[];
          this.total = res.data.result.count;
          this.$emit("setTaskInfo", this.total>0, this.tableData.data[0]);
          this.tableData.loading = false;
        }).catch(err => {
          this.tableData.loading = false;
          console.log('err',err);
        })
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getPushTask();
      }
    },
    created() {
      this.getPushTask();
    }
  }
</script>

<style scoped>

</style>
