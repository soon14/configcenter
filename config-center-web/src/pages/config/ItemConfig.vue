<template>
  <section>
    <el-row :span="24" class="toolbar">
      <el-form :inline="true" :model="conditions" ref="conditions" size="small" style="padding-top: 20px;padding-left: 10px">
        <el-form-item>
          <el-input  v-model="conditions.itemKey" placeholder="输入配置项名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-input  v-model="conditions.creatorName" placeholder="输入配置项创建人"></el-input>
        </el-form-item>
        <el-form-item label="更新日期">
          <el-date-picker  v-model="conditions.time" type="daterange" align="right" unlink-panels range-separator="至"
                          start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
        <el-button type="primary" @click="queryConfigItems">查询</el-button>
        </el-form-item>
        <el-form-item>
        <el-button  type="primary" @click="copyConfigItem">复制</el-button>
        </el-form-item>
        <el-form-item>
        <el-button  type="primary" @click="addConfigItems">新增</el-button>
        </el-form-item>
      </el-form>
    </el-row>

    <c-table :table="tableData">
      <template slot-scope="props" slot="updateTime">
        <span>{{timestampToDate(props.obj.row.updateTime)}}</span>
      </template>
      <template slot-scope="props" slot="button-operation">
        <el-tooltip effect="dark" content="修改" placement="top-start">
          <el-button type="primary" icon="el-icon-edit" circle size="mini" @click="modConfigItems(props.obj.$index, props.obj.row)">
          </el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="删除" placement="top-start">
          <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deleteConfigItem(props.obj.$index, props.obj.row)">
          </el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="推送" placement="top-start">
          <el-button type="success" icon="fa fa-arrow-up" circle size="mini" @click="pushConfigs(props.obj.$index, props.obj.row)">
          </el-button>
        </el-tooltip>
        <el-tooltip effect="dark" content="版本历史" placement="top-start">
          <el-button type="info" circle size="mini" icon="fa fa-history" @click="versionHistroy(props.obj.$index, props.obj.row)">
          </el-button>
        </el-tooltip>
      </template>
    </c-table>

    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="20"
                     :total="total" style="float:right;">
      </el-pagination>
    </el-col>

    <el-dialog :title="title" :visible.sync="addConfigItemVisible" width="38%" :close-on-click-modal="false">
      <AddItem :envId="selectedEnv" :modItem="configItem" :isAddOrMod="isAdd"
               v-if="addConfigItemVisible === true" v-on:closeEvent="closeDialog"></AddItem>
    </el-dialog>

    <el-dialog title="版本历史" :visible.sync="versionHistoryVisible" width="70%" :close-on-click-modal="false">
      <ConfigHis :envId="selectedEnv" :configItemId="id" v-if="versionHistoryVisible === true"></ConfigHis>
    </el-dialog>

    <el-dialog title="配置项复制" :visible.sync="copyConfigVisible" width="70%">
      <item-config-copy :allEnvs="envs" :currentEnvId="selectedEnv" ref="itemConfigCopy" @handleSuccess="handleSuccessFunction"></item-config-copy>
    </el-dialog>
  </section>
</template>

<script>
  import CTable from "../../components/Table.vue";
  import {timestampToFullDate} from "../../common/date";
  import AddItem from './AddItemConfig';
  import ConfigHis from "./ConfigHistroy";
  import ItemConfigCopy from "./ItemConfigCopy";

  export default {
    name: "ConfigItems",
    components: {
      AddItem,
      CTable,
      ConfigHis,
      ItemConfigCopy,
    },
    data() {
      let validateDesc = (rules, value, callback) => {
        if (value.length > 512) {
          callback(new Error('描述信息请控制在512个字符以内'));
        } else {
          callback();
        }
      };
      return {
        addConfigItemVisible: false,
        versionHistoryVisible: false,
        copyConfigVisible: false,
        isAdd: true,
        title: '',
        id: '',
        pushData: {},
        conditions: {
          envId: '',
          itemKey: '',
          creatorName: '',
          beginTime: '',
          endTime: ''
        },
        configItem: {
          envId: '',
          itemKey: '',
          itemValue: '',
          strategyId: '',
          desc: ''
        },
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        tableData: {
          loading: false,
          data: [],
          columns: [
            {
              label: "配置项KEY",
              props: "itemKey"
            },
            {
              label: "配置项VALUE",
              props: "itemValue"
            },
            {
              label: "更新时间",
              props: "updateTime",
              show: true
            },
            {
              label: "创建人",
              props: "creatorName"
            }
          ],
          hasOperation: true,
          hasOperationTemplate: true,
          operation: {
            label: "操作",
            data: []
          }
        },
        page: 0,
        pagesync:this.page+1,
        total: 0,
        rules: {
          itemValue: [
            {required: true, message: '配置项Value不可为空', trigger: 'blur'},
          ],
          desc: [
            {validator: validateDesc, message: '2-512个字符之间', trigger: 'blur'}
          ]
        },
      };
    },
    props: {
      selectedEnv: {
        type: String,
        required: true,
      },
      envs: {
        type: Array,
        required: true,
      }
    },
    methods: {
      handleSuccessFunction(){
        this.copyConfigVisible=false;
      },
      timestampToDate(value) {
        return timestampToFullDate(value);
      },
      queryConfigItems(){
        this.pagesync=1;
        this.getConfigItems(this.selectedEnv,this.pagesync-1)
      },
      getConfigItems(env, page) {
        let timeArray = this.conditions.time;
        let beginT;
        let endT;
        if (timeArray) {
          beginT = new Date(timeArray[0]).getTime();
          endT = new Date(timeArray[1]).getTime();
        }
        const configData = {
          envId: env,
          itemKey: this.conditions.itemKey,
          creatorName: this.conditions.creatorName,
        };
        configData.beginTime = beginT;
        configData.endTime = endT;
        let params = {
          currentPage: page,
          pageSize: 10,
          data: configData
        };
        this.tableData.loading = true;
        this.$http.getConfigItems(params).then(res => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        }).catch(err => {
          console.log('err', err);
          this.tableData.loading = false;
        })
      },
      deleteConfigItem(index, row) {
        this.$confirm('删除此配置项?', '提示', {
          type: 'warring',
        }).then(() => {
          const delData = {
            envId: this.selectedEnv,
            itemKey: row.itemKey,
          };
          this.$http.delConfigItem(delData).then(res => {
            this.$message({
              message: res.data.result,
              type: 'success',
            }).catch(err => {
              console.log('err', err);
            });
            this.getConfigItems(envId,0);
          })
        })
      },
      copyConfigItem() {
        this.copyConfigVisible = true;
        if (this.$refs['itemConfigCopy']) {
          this.$refs["itemConfigCopy"].init(this.selectedEnv);
        }
      },
      addConfigItems() {
        this.addConfigItemVisible = true;
        this.isAdd = true;
        this.title = "配置项新增";
      },
      modConfigItems(index, row) {
        this.isAdd = false;
        this.title = "配置项修改";
        this.addConfigItemVisible = true;
        this.configItem.envId = this.selectedEnv;
        this.configItem.itemKey = row.itemKey;
        this.configItem.itemValue = row.itemValue;
        this.configItem.strategyId = row.strategyId;
        this.configItem.desc = row.desc;
      },
      versionHistroy(index, row) {
        this.versionHistoryVisible = true;
        this.id = row.id;
      },
      pushConfigs(index,row){
        const queryDate = {
          envId: row.envId,
          configItemId: row.id,
        };
        this.$http.getConfigItemHis(queryDate).then(res => {
          let data = res.data.result;
          this.pushData = {
            envId: row.envId,
            configId: row.id,
            configType: 2,
            configVersion: data[0].hisId,
          };
          this.$confirm('推送此项配置至服务器?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.$http.pushConfig(this.pushData).then(res => {
              this.$message({
                type: 'success',
                message: res.data.result
              });
            }).catch(err => {
              console.log('err', err);
            });
          }).catch(err => {
            console.log('err', err);
          });
        })
      },
      changeEnv(env) {
        this.getConfigItems(env,0);
      },
      handleCurrentChange(currentPage) {
        this.page = currentPage - 1;
        this.getConfigItems(this.selectedEnv, this.page);
      },
      closeDialog(val) {
        this.addConfigItemVisible = val;
      }
    },
    created() {
      this.getConfigItems(this.selectedEnv, this.page = 0);
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
