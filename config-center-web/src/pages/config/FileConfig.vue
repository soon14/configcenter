<template>
  <section>
    <el-row>
      <el-col :span="24" class="toolbar" >
        <el-form :inline="true" :model="conditions" size="small" style="padding-top: 20px;padding-left: 10px">
          <el-form-item label="">
            <el-input  v-model="conditions.name" placeholder="请输入配置文件名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="">
            <el-input  v-model="conditions.manName" placeholder="请输入创建人名字" clearable></el-input>
          </el-form-item>
          <el-form-item label="更新日期">
            <el-date-picker
              v-model="conditions.dateValue"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  @click="queryItemConfigList">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"   @click="fileUP">上传文件</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  @click="batchDownload">全量下载</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="batchCopy">批量复制</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary"  @click="batchDel">批量删除</el-button>
          </el-form-item>

        </el-form>
      </el-col>
      <c-table :table="tableData" v-on:onHandleSelectionChange="handleSelected" ref="datalist">
        <template slot-scope="props" slot="createTime">
          <span>{{timestampToFullDate1(props.obj.row.createTime)}}</span>
        </template>
        <template slot-scope="props" slot="updateTime">
          <span>{{timestampToFullDate1(props.obj.row.updateTime)}} </span>
        </template>

        <template slot-scope="props" slot="button-operation">
          <el-tooltip effect="dark" content="编辑文件" placement="top-start">
              <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="editFile(props.obj.$index, props.obj.row)"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" content="删除文件" placement="top-start">
              <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deleteFile(props.obj.$index, props.obj.row)"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" content="下载文件" placement="top-start">
              <el-button type="success" icon="el-icon-download" circle size="mini" @click="downloadFile(props.obj.$index, props.obj.row)"></el-button>
          </el-tooltip>
          <el-tooltip effect="dark" content="推送文件" placement="top-start">
            <el-button type="success"  circle size="mini" @click="pushFile(props.obj.$index, props.obj.row)">
              <i class="fa fa-arrow-up" aria-hidden="true"></i>
            </el-button>
          </el-tooltip>
          <el-tooltip effect="dark" content="文件历史版本" placement="top-start">
              <el-button type="info" circle size="mini" @click="showFileHistoryVersion(props.obj.$index, props.obj.row)"><i class="fa fa-history" aria-hidden="true"></i></el-button>
          </el-tooltip>
        </template>
      </c-table>
      <!-- 分页查询 -->
      <el-col :span="24" class="toolbar">
        <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="10" :total="total" style="float:right;">
        </el-pagination>
      </el-col>
    </el-row>
    <el-dialog :title="fileoperateName" :visible.sync="upFileVisible" width="70%" :before-close="close">
      <upload-config-file :currentEnv="selectedEnv"></upload-config-file>
    </el-dialog>


    <el-dialog :title="deloperateName" :visible.sync="delFileVisible" width="60%" :close-on-click-modal="false">
      <el-form :model="fileInfo" label-width="120px" ref="fileInfo">
        <el-form-item label="删除原因：" >
          <el-input type="textarea" v-model="delReason" style="width: 80%"> </el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button @click="deleteFileFinal(delId)" type="primary">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="fileoperateName" :visible.sync="editFileVisible" width="60%" :close-on-click-modal="false">
      <ReplaceConfigFile :currentEnv="selectedEnv" :oldConfigFileInfo="selectedRow" @closeDialog="editFileVisible=false"  v-if="editFileVisible==true"></ReplaceConfigFile>
    </el-dialog>

    <el-dialog :title="fileoperateName" :visible.sync="copyFileVisible" width="60%" :close-on-click-modal="false">
      <el-form :model="copyFileInfo" label-width="120px" ref="copyFileInfo">
        <el-form-item label="源环境" >
          <el-select v-model="copyFileInfo.envOpt"  filterable placeholder="请源环境" @change="changeCopyEnv" style="width: 80%">
           	<span v-for="option in copyFileInfo.sourceEnvs" :key="option.id">
				  	  <el-option :label="option.envName" :value="option.id.toString()"></el-option>
				    </span>
          </el-select>
        </el-form-item>

        <c-table :table="copytableData" v-on:onHandleSelectionChange="handleSelected" >
          <template slot-scope="props" slot="createTime">
            <span>{{timestampToFullDate1(props.obj.row.createTime)}}</span>
          </template>
          <template slot-scope="props" slot="updateTime">
            <span>{{timestampToFullDate1(props.obj.row.updateTime)}} </span>
          </template>
        </c-table>
        <!-- 分页查询 -->
        <el-col :span="24" class="toolbar">
          <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="copytotal" style="float:right;">
          </el-pagination>
        </el-col>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取消</el-button>
        <el-button @click="uploadFilesCopy" type="primary">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="fileOperation" :visible.sync="fileHistoryVisible" width="70%" :before-close="clearTheState">
      <el-row type="flex" justify="end">
        <el-button type="primary" size="mini" @click="compareDiff" v-if="currentShow ===  'historyVersion'">版本比对</el-button>
        <el-button type="primary" size="mini" @click="goBackToHistoryVersion" v-if="currentShow === 'versionDiff'">历史版本</el-button>
      </el-row>
      <el-row>
        <file-history-version v-if="currentShow ===  'historyVersion'" :dataTables="fileVersionData" :file-name="subfileName"></file-history-version>
        <file-version-diff v-if="currentShow === 'versionDiff'" :dataTables="fileVersionData"></file-version-diff>
      </el-row>
    </el-dialog>
    <el-dialog title="文件在线编辑" :visible.sync="onLineEditVisible" width="70%" :before-close="clearOnLineState" :close-on-click-modal="false">
      <code-mirror :content="fileContent" ref="childEditor" @returnValueToParent="getChildValue" v-if="showEditVisible == true"></code-mirror>
      <el-row type="flex" justify="end" style="margin-top: 10px">
        <el-button type="primary" size="small" @click="confirmEdit">提交</el-button>
      </el-row>
    </el-dialog>

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
      center
      :before-close="handleCloseDialog">
      <div style="text-align: center;">
        <span>请选择文件编辑类型！</span>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editFileReplace">文件替换</el-button>
        <el-button type="primary" @click="editOnline">在线编辑</el-button>
        <el-button type="primary" @click="editModStratege">修改策略</el-button>
      </span>
    </el-dialog>

    <el-dialog :title="fileoperateName" :visible.sync="modStrategeVisible" width="40%" :close-on-click-modal="false">
      <mod-refresh-stratege :currentEnv="selectedEnv" :oldConfigFileInfo="selectedRow" :subStrategyName="tempName" v-if="modStrategeVisible==true" @submit="modStratege"></mod-refresh-stratege>
    </el-dialog>

    <el-dialog title="文件下载" :visible.sync="testVisible" width="40%" :close-on-click-modal="false">
     <down-load-file :envId="selectedEnv" @save="saveFile"></down-load-file>
    </el-dialog>
  </section>
</template>

<script>
  import CTable from "../../components/Table.vue";
  import {timestampToFullDate} from "../../common/date";
  import FileVersionDiff from './FileVersionDiff';
  import FileHistoryVersion from './FileHistoryVersion';
  import CodeMirror from "../../components/CodeMirror";
  import UploadConfigFile from "./UploadConfigFile";
  import ReplaceConfigFile from "./ReplaceConfigFile"
  import ModRefreshStratege from "./ModRefreshStratege";
  import DownLoadFile from "../../components/DownLoadFile";

  export default {
    name: "ConfigFiles",
    components: {
      DownLoadFile,
      ModRefreshStratege,
      UploadConfigFile,
      ReplaceConfigFile,
      FileVersionDiff,
      FileHistoryVersion,
      CTable,
      CodeMirror
    },
    data() {
      return {
        testVisible:false,
        modStrategeVisible:false,
        dialogVisible:false,
        radioValue:'upSinglefile',
        onLineEditVisible:false,
        showEditVisible:false,
        fileContent:"",
        downfileContent:"",
        subfileName:"",
        envIds:[],
        selectedRow: {},
        tempName:"",
        fileHistoryVisible:false,
        fileVersionData:[],

        currentShow:'historyVersion',
        fileOperation: '文件历史版本',
        conditions:{
          name: '',
          manName:'',
          dateValue:'',
        },

        fileInfo:{
          relatPath:"",
          desc:""
        },
        editFileInfo:{
          relatPath:"",
          desc:""
        },
        copyFileInfo:{
          sourceEnvs:[],
          envOpt:"",

        },
        upFileVisible:false,
        editFileVisible:false,
        copyFileVisible:false,
        fileoperateName:"",


        delReason:"",
        delFileVisible:false,
        deloperateName:"",
        delId:"",
        operateid:[],/*对数据进行增删改下载时需要用到的id是一个数组*/

        fileFlag:false,
        fileIsYaSuo:false,

        tableDataItem:[],
        page:0,
        pagesync:this.page+1,
        total:0,
        copytotal:0,
        tableData: {
          loading:false,
          hasSelect:true,
          data:[],
          columns:[
                {
                  label:"配置文件名称",
                  props:"fileName",
                },
                {
                  label:"创建人",
                  props:"creatorName",
                },
                {
                  label:"更新时间",
                  props:"updateTime",
                  show:true
            }
          ],
          hasOperation:true,
          hasOperationTemplate:true,
          operation: {
            label:"操作",
            data:[

            ]
          }
        },
        selectedData:[],
        fileList:[],
        copytableData: {
          loading:false,
          hasSelect:true,
          data:[],
          columns:[
            {
              label:"配置文件名称",
              props:"fileName",
            },
            {
              label:"创建人",
              props:"creatorName",
            },
            {
              label:"创建时间",
              props:"createTime",
              show:true
            }, {
              label:"更新时间",
              props:"updateTime",
              show:true
            }
          ],
          hasOperation:false,
          hasOperationTemplate:false,

        },
      };
    },
    props: {
      selectedEnv:{
        type: String,
        required:true,
      },
      envs:{
        type:Array,
        required:true,
      }
    },
    methods: {
      saveFile(value){
            window.open("/center/configFile/getAllConfigFile?envId="+value);
            this.testVisible=false;
      },
      showFileHistoryVersion(index,row) {
        let data = {
          envId:this.selectedEnv,
          configId: row.id,
        };
        this.$http.getFileHistory(data).then(res => {
          let result = [];
          for(let i=0;i<res.data.result.length;i++) {
            let obj = {
              creatorName:row.creatorName,
              commitId:res.data.result[i].commitId,
              commitTime:res.data.result[i].commitTime,
            };
            result.push(obj);
          }
          this.fileVersionData.data = result;
          this.fileVersionData.fileId = row.id;
          this.fileVersionData.appEnvId = row.appEnvId;
          this.currentShow = 'historyVersion';
          this.subfileName=row.fileName;
          this.fileHistoryVisible = true;
        }).catch(err => {
          console.log('err',err);
        });
      },
      compareDiff() {
        this.currentShow = 'versionDiff';
        this.fileOperation = "文件版本比对";
      },
      goBackToHistoryVersion() {
        this.currentShow = 'historyVersion';
        this.fileOperation = "文件历史版本";
      },
      close(){
        this.upFileVisible=false;
      },
      clearTheState(done) {
        this.currentShow = '';
        this.fileOperation = "文件历史版本";
        done();
      },
      uploadFilesCopy() {
        if(this.operateid.length>0){
          //调用文件复制接口
          let params = {
            configIds: this.operateid,
            envId: this.selectedEnv,
            originEnvId: this.copyFileInfo.envOpt,
          };
          this.$http.copyConfigFile(params).then(res => {
            this.$message({
              message: res.data.result,
              type: 'success',
            });
            this.copyFileVisible=false;
            this.operateid=[];
          }).catch((err) => {
            console.log("err",err);
            this.operateid=[];
          })
        }else{
          this.$alert('请选择要复制的文件', '操作提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
        }

      },
      handleSelected(selectedRow) {
        this.operateid=[];
        if(selectedRow.length>0)
        {
          for (let i=0;i<selectedRow.length;i++) {
            /*获取选中数据行的id*/
            this.operateid[i] = selectedRow[i].id;
          }
        }
      },
      queryItemConfigList() {
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getItemconfigList(this.selectedEnv,this.page);
      },
      getItemconfigList(currentEnv, page) {
        let timeArray = this.conditions.dateValue;
        let startDate;
        let endDate;
        if (timeArray){
          startDate = new Date(timeArray[0]).getTime();
          endDate = new Date(timeArray[1]).getTime();
        }
        let data = {
          envId:currentEnv,
          fileName: this.conditions.name,
          creatorName:this.conditions.manName,
          startDate:startDate,
          endDate:endDate
        };
        let params = {
          currentPage: page,
          data:data,
          pageSize:10,
        };
        this.tableData.loading = true;
        this.$http.getFileConfigs(params).then(res => {
          this.tableData.loading = false;
          this.tableData.data = res.data.result.entities;
          this.total = res.data.result.count;
        }).catch(err => {
          this.tableData.loading = false;
        })
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getItemconfigList(this.selectedEnv, this.page);
      },
      selectedItem(value) {
        this.selectedData = value;
      },
      timestampToFullDate1(date) {
        return timestampToFullDate(date);
      },
      changeEnv(env) {
        this.getItemconfigList(env,0);
      },
      changeCopyEnv(env){
        this.getCopyconfigList(env);
      },
      getCopyconfigList(env){

        let data = {
          envId:env,
          fileName: "",
          creatorName:"",
          startDate:"",
          endDate:""
        };
        let params = {
          currentPage:this.page,
          data:data,
          pageSize:10,
        };
        this.copytableData.loading = true;
        this.$http.getFileConfigs(params).then(res => {
          this.copytableData.loading = false;
          this.copytableData.data = res.data.result.entities;
          this.copytotal = res.data.result.count;
        }).catch(err => {
          this.copytableData.loading = false;
        })
      },
      //文件的操作
      handleTimeout(){
        this.$alert('上传超时', '操作提示', {
          confirmButtonText: '确定',
          callback: action => {
            this.$message({
              type: 'info',
              message: `timeout`
            });
          }
        });
      },
      handleCancel(){
        this.upFileVisible=false;
        this.editFileVisible=false;
        this.copyFileVisible=false;
        this.editFileInfo.desc="";
        this.editFileInfo.relatPath="";
        this.copyFileInfo.aimEnv="";
        this.copyFileInfo.sourceEnv="";
        this.fileList=[];
        this.fileInfo.desc="";
        this.fileInfo.relatPath="";
      },
      handleClose() {
        this.delFileVisible=false;
        this.fileList=[];
        this.delReason="";
      },
      fileUP(){
        this.upFileVisible=true;
        this.fileoperateName="文件上传";
      },
      batchDownload(){
        if(this.total!=0){
          this.testVisible=true;
        }else{
          this.$message({
            message:"该环境下没有文件！",
            type:'info',
          });
        }

      },
      batchCopy(){
        this.copyFileVisible=true;
        this.fileoperateName="文件复制";
        this.copyFileInfo.envOpt="";
        this.copytotal=0;
        this.copytableData.data=[];
        this.envIds=[];
        for(let i=0;i<this.envs.length;i++){
          if(this.envs[i].id == this.selectedEnv){

          }else{
            this.envIds.push(this.envs[i]);
          }
        }
        this.copyFileInfo.sourceEnvs=this.envIds;
      },
      editFile(index,row){
        this.editId=row.id;
        this.selectedRow = row;
        this.dialogVisible=true;
        this.tempName="";
        if(this.selectedRow.strategyId!=null){
          let para={
            appId:this.$router.history.current.params.appId,
            strategyId: this.selectedRow.strategyId,
          };
          this.$http.getStrategyName(para).then(res=>{
            this.tempName=res.data.result;
          }).catch(err=>{
            console.log("err",err);
          });
        }

      },
      handleCloseDialog(){
        this.dialogVisible=false;
      },
      editModStratege(){
        this.modStrategeVisible=true;
        this.fileoperateName="修改文件刷新策略";
        this.dialogVisible=false;
      },
      modStratege(info){
        if(!info.isSelectStrategy){
          this.$message({
            message:"策略未修改！",
            type:'info',
          });
        }else {
          let para={
            configId:info.configId,
            envId:info.envId,
            strategyId:info.strategyId
          };
          this.$http.changeStrategy(para).then(res=>{
            this.$message({
              message:res.data.result,
              type:'success',
            });
          }).catch();
          this.modStrategeVisible=false;
        }

      },
      editOnline(){
        this.onLineEdit();
        this.dialogVisible=false;
      },
      editFileReplace(){
        this.fileReplace();
        this.dialogVisible=false;
      },
      fileReplace(){
        this.editFileVisible=true;
        this.fileoperateName="文件替换";

      },
      onLineEdit(){
        let data = {
          envId:this.selectedEnv,
          queryConfigContentVO: [
            {
              configFileId:this.editId,
              configFileVersion:this.fileVersion,
            }
          ]
        };
        this.$http.getConfigFilesContent(data).then(res => {
          this.fileContent = res.data.result[0].configFileContent;
          this.showEditVisible = true;
        }).catch(err => {
          console.log('err',err);
        });
        this.onLineEditVisible = true;
        this.fileoperateName="在线编辑";
      },
      clearOnLineState(done) {
        this.fileContent = '';
        this.showEditVisible = false;
        done();
      },
      getChildValue(value) {
        let data = {
          configFileContent: value,
          configId: this.editId,
          envId:this.selectedEnv,
        };
        this.$http.saveConfigFileContent(data).then(res => {
          this.$message({
            message:res.data.result,
            type:'success',
          });
          this.onLineEditVisible = false;
          this.showEditVisible = false;
          this.fileContent = "";
        }).catch(err => {
          console.log('err',err);
        });
      },

      confirmEdit() {
        this.$refs.childEditor.returnValueToParent();
      },
      deleteFile(index, row) {
        this.delFileVisible = true;
        this.deloperateName = "删除文件";
        this.delId = row.id;
      },
      deleteFileFinal(fileid){
        let delids= [];
        delids.push(fileid);
        let para={
          configIds:delids,
          envId:this.selectedEnv,
          reason:this.delReason,
        };
        this.$http.deleteConfigFile(para).then(res => {
          this.delFileVisible = false;
          this.$message({
            message:res.data.result,
            type:'success',
          });
          this.delReason="";

        }).catch((err) => {
          this.delFileVisible = false;
          console.log(err);
          this.delReason="";
        });
      },
      batchDel(){
        if(this.operateid.length>0){
          this.$confirm('确定要批量删除吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            /*进行批量删除*/
            let para={
              configIds:this.operateid,
              envId:this.selectedEnv,
              reason:this.delReason+"该文件是批量删除的",
            };
            this.$http.batchDeleteConfigFile(para).then(res => {

              this.$message({
                message:res.data.result,
                type:'success',
              });
              this.delReason="";
              this.operateid=[];
            }).catch((err) => {
              console.log(err);
              this.delReason="";
              this.operateid=[];
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
            this.$refs.datalist.clearSelection();
          });
        }
        else{
          this.$alert('请选择要删除的文件！', '操作提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
        }
      },
      pushFile(index,row){
        this.$confirm('是否确定推送此文件?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let data = {
            envId: this.selectedEnv,
            configId:row.id,
            configType: 1,
            configVersion: row.fileVersion,
          };
          this.$http.pushConfig(data).then(res => {
            this.$message({
              type: 'success',
              message: res.data.result
            });
          }).catch(err => {
            console.log('err', err);
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消推送'
          });
        });

      },
      downloadFile(index,row){
        this.$confirm('是否确定下载该文件?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.saveSingleFile(row);
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消下载'
          });
        });
      },
      saveSingleFile(row){
        let data = [
          {
            configFileId:row.id,
            configFileVersion: row.fileVersion,
          }
        ];
        let para = {
          envId:this.selectedEnv,
          queryConfigContentVO: data
        };
        this.$http.getConfigFilesContent(para).then(res => {
          this.downfileContent = res.data.result[0].configFileContent;
          const content = this.downfileContent;
          let aTag = document.createElement('a');
          document.body.appendChild(aTag);
          aTag.style.display = 'none';
          let blob = new Blob([content]);
          aTag.download = row.fileName;
          aTag.href = URL.createObjectURL(blob);
          aTag.click();
          URL.revokeObjectURL(blob);
        }).catch(err => {
          console.log("err", err);
        })
      }
    },
    created() {
      this.getItemconfigList(this.selectedEnv,0);
    },
  }
</script>

<style scoped>
  .toolbar {
    background: #f2f2f2;
    margin: 10px 0px;
  }

  .footer {
    width: 38%;
    margin-top: 5px;
    margin-left: auto;
    margin-right: auto;
  }

  .body .el-form {
    width: 70%;
    position: center;
    margin-left: auto;
    margin-right: auto;
  }
</style>
