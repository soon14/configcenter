<template>
  <div class="queryForm">
    <el-form :inline="true">
      <el-form-item>
        <el-input v-model="itemName" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="creatorName" placeholder="配置项创建人"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="queryConfigHis">查询</el-button>
      </el-form-item>
    </el-form>
    <el-row>
      <el-button size="mini" type="primary" @click="changeToHisList">历史版本</el-button>
      <el-button size="mini" type="primary" @click="compareVersion">版本对比</el-button>
    </el-row>
    <c-table :table="tableData" v-if="hisListVisible" @onHandleSelectionChange="getHisItemRow" :max="2">
      <template slot-scope="props" slot="createTime">
        <span>{{timestampToDate(props.obj.row.createTime)}}</span>
      </template>
      <template slot-scope="props" slot="button-operation">
        <el-tooltip effect="dark" content="推送" placement="top-start">
          <el-button type="primary" circle size="mini" @click="pushConfigs(props.obj.$index, props.obj.row)"><i class="fa fa-arrow-up" aria-hidden="true"></i></el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="查看实例" placement="top-start">
          <el-button type="info" circle size="mini" @click="showVersionInstances(props.obj.$index, props.obj.row)"><i class="fa fa-eye" aria-hidden="true"></i></el-button>
        </el-tooltip>
      </template>
    </c-table>
    <code-view :oldString="versionA" :newString="versionB" v-else></code-view>
    <el-dialog title="实例列表" :visible.sync="ItemInfoVisible" append-to-body>
      <specific-version-instance :itemInfo="itemInfo" :currentEnv="envId" v-if="ItemInfoVisible === true"></specific-version-instance>
    </el-dialog>
  </div>
</template>

<script>
  import CTable from '../../components/Table';
  import CodeView from "../../components/CodeView";
  import SpecificVersionInstance from "./SpecificVersionInstance";
  import {timestampToFullDate} from "../../common/date";

  export default {
    name: "ConfigHis",
    components: {
      CTable,
      CodeView,
      SpecificVersionInstance
    },
    data() {
      return {
        itemInfo:{},
        ItemInfoVisible:false,
        hisListVisible: true,
        versionA: '',
        versionB: '',
        hisItems: [],
        itemName: '',
        creatorName: '',
        tableData: {
          loading: false,
          data: [],
          columns: [
            {
              label: "历史版本号",
              props: "hisId"
            },
            {
              label:"配置项Value",
              props: "itemValue"
            },
            {
              label: "创建时间",
              props: "createTime",
              show: true
            },
            {
              label: "创建人",
              props: "creatorName",
            }
          ],
          hasSelect: true,
          min: 2,
          max: 2,
          hasOperation: true,
          hasOperationTemplate: true,
          operation: {
            label: "操作",
            data: []
          }
        }
      }
    },
    props: ['envId', 'configItemId'],
    methods: {
      showVersionInstances(index,row) {
        this.ItemInfoVisible = true;
        this.itemInfo.configId = this.configItemId;
        this.itemInfo.configType = 2;
        this.itemInfo.configVersion = row.hisId;
      },
      pushConfigs(index,row) {
        let data = {
          envId:this.envId,
          configId:this.configItemId,
          configType:2,
          configVersion:row.hisId,
        };
        this.$http.pushConfig(data).then(res => {
          this.$message({
            type: 'success',
            message:res.data.result
          });
        }).catch(err => {
          console.log('err',err);
        })
      },
      timestampToDate(value) {
        return timestampToFullDate(value);
      },
      queryConfigHis() {
        const queryDate = {
          envId: this.envId,
          configItemId: this.configItemId,
          creatorName: this.creatorName,
        };
        this.$http.getConfigItemHis(queryDate).then(res => {
          this.tableData.data = res.data.result;
          this.itemName = this.tableData.data[0].itemKey
        }).catch(err => {
          console.log('err', err);
        });

      },
      changeToHisList() {
        this.hisListVisible = true;
        this.hisItems = [];
      },
      compareVersion() {
        if (this.hisItems.length === 2) {
          this.versionA = this.hisItems[0].itemValue;
          this.versionB = this.hisItems[1].itemValue;
          this.hisListVisible = false;
        } else {
          this.$message({
            message: '请选择两个历史版本',
            type: 'info'
          });
        }
      },
      getHisItemRow(val) {
        this.hisItems = val;
      },
    },
    created() {
      this.queryConfigHis();
    },
  }
</script>

<style scoped>
</style>
