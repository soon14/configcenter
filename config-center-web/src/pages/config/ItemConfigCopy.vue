<template>
  <section>
    <span>环境标签源:</span>
    <el-select v-model="selectedEnvId" filterable placeholder="请选择一个环境" size="small" @change="getConfigItems">
      <span v-for="option in otherEnvs" :key="option.id">
        <el-option :label="option.envName" :value="option.id"></el-option>
      </span>
    </el-select>
    <c-table :table="tableData" @onHandleSelectionChange="getCopyItemRow" ref="itemTable">
      <template slot-scope="props" slot="updateTime">
        <span>{{timestampToDate(props.obj.row.updateTime)}}</span>
      </template>
    </c-table>
    <div class="footerButton">
      <el-button size="small" type="primary" @click="copySubmit">确定</el-button>
    </div>
  </section>
</template>

<script>
  import CTable from '../../components/Table';
  import {timestampToFullDate} from "../../common/date";

  export default {
    name: "ItemConfigCopy",
    components: {
      CTable
    },
    data() {
      return {
        selectedEnvId: '',
        mulSelection: [],
        otherEnvs: [],
        lastEnvId: '',
        copyData: {
          envId: '',
          itemKey: '',
          itemValue: '',
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
          hasSelect: true,
        }
      }
    },
    props: {
      allEnvs: {
        type: Array,
        required: true
      },
      currentEnvId: {
        type: String,
        required: true
      }
    },
    methods: {
      timestampToDate(value) {
        return timestampToFullDate(value);
      },
      getCopyItemRow(val) {
        this.mulSelection = val;
      },
      getConfigItems() {
        let conditions = {
          envId: this.selectedEnvId
        };
        let params = {
          currentPage: 0,
          pageSize: 10,
          data: conditions
        };
        this.$http.getConfigItems(params).then(res => {
          this.tableData.data = res.data.result.entities;
        }).catch(err => {
          console.log('err', err);
        });
      },
      copySubmit() {
        if (this.mulSelection.length > 0) {
          let configItemIds = [];
          for (let i in this.mulSelection) {
            configItemIds.push(this.mulSelection[i].id);
          }
          let params = {
            envId: this.currentEnvId,
            sourceEnvId: this.selectedEnvId,
            configItemIds: configItemIds
          };
          this.$http.copyConfigItem(params).then(res => {
            this.$message({
              message: res.data.result,
              type: 'success'
            });
            this.mulSelection = [];
            this.$refs.itemTable.clearSelection();
            this.$emit("handleSuccess")
          }).catch(err => {
            console.log('err', err);
          });
        }
      },
      init(currentEnvId) {
        if (currentEnvId !== this.lastEnvId) {
          this.setOptions(currentEnvId);
        }
      },
      setOptions(currentEnvId) {
        this.lastEnvId = currentEnvId;
        this.otherEnvs = [];
        this.selectedEnv = '';
        if (this.allEnvs.length > 1) {
          for (let i = 0; i < this.allEnvs.length; i++) {
            if (this.allEnvs[i].id === parseInt(currentEnvId)) {
            } else {
              this.otherEnvs.push(this.allEnvs[i]);
            }
          }
          this.selectedEnvId = this.otherEnvs[0].id;
          this.getConfigItems();
        }
      }
    },
    created() {
      this.setOptions(this.currentEnvId);
    }
  }
</script>

<style scoped>
  .footerButton {
    margin-top: 20px;
    text-align:center;
  }
</style>
