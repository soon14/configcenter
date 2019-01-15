<template>
  <section>
    <el-row>
      <span>环境标签：</span>
      <el-select v-model="envOption"  filterable  placeholder="请选择对应的环境" @change="changeAppEnv" size="small">
				<span v-for="option in envOptions" :key="option.id">
					<el-option :label="option.envName" :value="option.id.toString()"></el-option>
				</span>
      </el-select>
      <span>
        <el-radio v-model="radioValue" label="config-file" border size="small">配置文件</el-radio>
        <el-radio v-model="radioValue" label="config-item" border size="small">配置项</el-radio>
      </span>
        <span style="float: right">
          <el-button size="small" @click="showConfigTask" type="success">配置变更</el-button>
          <el-button size="small" @click="showPushTask" type="warning">配置推送</el-button>
        </span>
    </el-row>
    <el-row v-if="radioValue==='config-file' && envOption">
      <config-files :selectedEnv="envOption" style="margin-top: 10px" ref="files" :envs="envOptions"></config-files>
    </el-row>
    <el-row v-if="radioValue==='config-item' && envOption">
      <config-items :selectedEnv="envOption" style="margin-top: 10px" ref="items" :envs="envOptions"></config-items>
    </el-row>
    <el-row>
      <el-dialog :title="taskTitle" :visible.sync="showTasksVisible" width="70%">
        <to-submit-task v-if="showTasksVisible===true"
                   :selectedEnv="envOption" :taskType="taskType"
                   @confirmCloseDialog="confirmCloseDialog">
        </to-submit-task>
      </el-dialog>
    </el-row>
  </section>
</template>

<script>
  import ConfigItems from './ItemConfig';
  import ConfigFiles from './FileConfig';
  import {mapGetters,mapMutations} from 'vuex';
  import types from "../../store/mutation-types";
  import ToSubmitTask from '../task/ToSubmitTask';
  export default {
    components: {
      ConfigItems,
      ConfigFiles,
      ToSubmitTask
    },
    data() {
      return {
        radioValue: 'config-file',
        envOptions:[],
        envOption:'',
        taskType: '1',
        taskTitle: '配置变更待提交任务详情',
        showTasksVisible:false
      }
    },
    methods: {
      showConfigTask() {
        this.showTasksVisible = true;
        this.taskType = '1';
        this.taskTitle = '配置变更待提交任务详情'
      },
      showPushTask() {
        this.showTasksVisible = true;
        this.taskType = '2';
        this.taskTitle = '配置推送待提交任务详情'
      },
      getAppEnvs() {
        let appId = this.$router.history.current.params.appId;
        this.$http.getAppEnvs({appId:appId}).then(res => {
            this.envOptions = res.data.result;
            const newAppId = this.getCurrentAppId();
            if(!newAppId || (newAppId && newAppId != appId)) {
              this.saveAppEnv(this.envOption);
            }
            this.envOption = this.getCurrentAppEnv()?this.getCurrentAppEnv():res.data.result[0].id.toString();
            this.saveAppId(appId);
            this.$emit('getCurrentEnv',this.envOption);
          }).catch(err => {
            this.envLoading = false;
            console.log('err',err);
          });
      },
      changeAppEnv() {
        if(this.$refs.files) {
          this.$refs.files.changeEnv(this.envOption);
        } else if(this.$refs.items) {
          this.$refs.items.changeEnv(this.envOption);
        }
        this.$emit('getCurrentEnv',this.envOption);
        this.saveAppEnv(this.envOption);
      },
      confirmCloseDialog() {
        this.showTasksVisible = false;
        if(this.$refs.files) {
          this.$refs.files.changeEnv(this.envOption);
        } else if(this.$refs.items) {
          this.$refs.items.changeEnv(this.envOption);
        }
      },
      ...mapGetters({
        getCurrentAppEnv:'currentAppEnv',
        getCurrentAppId: 'currentAppId',
      }),
      ...mapMutations({
        saveAppEnv: types.SAVE_APP_ENV,
        saveAppId: types.SAVE_APP_ID,
      }),
    },
    created() {
      this.getAppEnvs();
    },
    mounted () {
    },
  }
</script>
<style>
</style>
