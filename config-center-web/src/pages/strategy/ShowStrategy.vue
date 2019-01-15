<template>
  <section>
    <el-row class="toolbar">
      <el-form :inline="true" :model="conditions" style="padding-top: 20px;padding-left: 10px" ref="strategy">
          <el-form-item label="策略名称">
            <el-input v-model="conditions.strategyName" placeholder="请输策略的名称" size="small"></el-input>
          </el-form-item>
          <el-form-item label="创建人">
            <el-input v-model="conditions.creator" placeholder="创建人" size="small"></el-input>
          </el-form-item>
          <el-form-item label="操作日期">
            <el-date-picker size="small" v-model="conditions.time" type="daterange" align="right" unlink-panels range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="mini" @click="queryCondition(page)">查询</el-button>
          </el-form-item>
        <el-form-item v-if="isAdmin">
            <el-button type="primary" size="mini" @click=" addStrategyVisible =true ">新增</el-button>
        </el-form-item>
      </el-form>
    </el-row>

    <el-row>
      <c-table :table="tableData">
        <template slot-scope="props" slot="createTime">
          <span>{{timestampToFullDate1(props.obj.row.createTime)}}</span>
        </template>
        <template slot-scope="props" slot="updateTime">
          <span>{{timestampToFullDate1(props.obj.row.updateTime)}}</span>
        </template>
        <template slot-scope="props" slot="button-operation">
          <el-tooltip effect="dark" content="修改" placement="top"  v-if="isAdmin">
            <el-button type="primary" icon="el-icon-edit" circle size="small" @click="updateOneStrategy(props.obj.$index, props.obj.row)">
            </el-button>
          </el-tooltip>
          <el-tooltip effect="dark" content="删除" placement="top"  v-if="isAdmin">
            <el-button type="danger" icon="el-icon-delete" circle size="small" @click="deleteOneStrategy(props.obj.$index, props.obj.row)"></el-button>
          </el-tooltip>
        </template>
      </c-table>
      <el-col :span="24" class="toolbar">
        <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="10" :total="total" style="float:right;">
        </el-pagination>
      </el-col>
    </el-row>

    <el-dialog :title="title" :visible.sync="addStrategyVisible" width="80%" :close-on-click-modal="false" :before-close="isAddAllSaved">
      <add-strategy ref="addStrategy" @refreshData="refreshTableData"></add-strategy>
    </el-dialog>

    <el-dialog title="刷新策略详细步骤" :visible.sync="updateStrategyVisible" width="80%" :close-on-click-modal="false" :before-close="isUpdateAllSaved">
      <add-strategy :strategy-result="strategyResult" v-if="updateStrategyVisible===true" ref="updateStrategy"></add-strategy>
    </el-dialog>
  </section>
</template>

<script>
    import CTable from "../../components/Table.vue";
    import AddStrategy from "./AddStrategy";
    import {timestampToFullDate} from "../../common/date";
    export default {
      name: "show-strategy",
      components: {
        CTable,
        AddStrategy
      },
      data() {
        return {
          title:'添加刷新策略',
          isAdmin:false,
          addStrategyVisible:false,
          updateStrategyVisible:false,
          conditions: {
            strategyName:'',
            creator:'',
            time:[],
          },
          strategyResult:{},
          page:0,
          pagesync:this.page+1,
          total:0,
          tableData: {
            loading:false,
            data:[
            ],
            columns:[
              {
                label:"刷新策略名称",
                props:"strategyName",
              },{
                label:"创建人",
                props:"creatorName",
              },
              {
                label:"创建时间",
                props:"createTime",
                show:true,
              },
              {
                label:"修改时间",
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
        }
      },
      methods: {
        isAddAllSaved() {
          let result = this.$refs.addStrategy.isAllItemSaved();
          if(result) {
            this.addStrategyVisible = false;
            this.$refs.addStrategy.strategyInfo.strategyName="";
            this.$refs.addStrategy.strategyInfo.description="";
            this.$refs.addStrategy.strategyInfo.id="";
          }
        },
        isUpdateAllSaved() {
          let result = this.$refs.updateStrategy.isAllItemSaved();
          if(result) {
            this.updateStrategyVisible = false;
          }
        },
        timestampToFullDate1(date) {
          return timestampToFullDate(date);
        },
        refreshTableData(){
          this.getAppStrategy();
        },
        queryCondition() {
          this.pagesync=1;
          this.page=this.pagesync-1;
          this.getAppStrategy();
        },
        deleteOneStrategy(index, row) {
          this.$confirm('确定要删除这个刷新策略吗？','提示',{
            type: "warning",
          }).then(() => {
            let data = {
              appId:row.appId,
              strategyId:row.id
            };
            this.$http.deleteConfigUpdateStrategy(data).then(res => {
              this.$message({
                message:res.data.result,
                type:'success',
              });
              this.getAppStrategy();
            }).catch(err => {
              console.log('err',err);
            });
          }).catch(() => {
          })
        },
        updateOneStrategy(index,row) {
          let data = {
            appId:row.appId,
            strategyId:row.id
          };
          this.$http.getUpdateStrategyAllInfo(data).then(res => {
            this.strategyResult = res.data.result;
            this.updateStrategyVisible = true;
          }).catch(err => {
            console.log('err',err);
          });
        },
        handleCurrentChange(currentPage) {
          this.page = currentPage - 1;
          this.getAppStrategy();
        },
        getAppStrategy() {
          let appId = this.$router.history.current.params.appId;
          let data = {
            appId:appId,
            strategyName:this.conditions.strategyName,
            creatorName:this.conditions.creator,
          };
          if(this.conditions.time && this.conditions.time.length === 2) {
            data.startDate = new Date(this.conditions.time[0]).getTime();
            data.endDate = new Date(this.conditions.time[1]).getTime();
          }
          let params = {
            currentPage: this.page,
            data: data,
            pageSize:10,
          };
          this.tableData.loading = true;
          this.$http.queryStrategy(params).then(res => {
            this.tableData.loading = false;
            this.total = res.data.result.count;
            this.tableData.data = res.data.result.entities;
          }).catch(err => {
            this.tableData.loading = false;
            console.log('err',err);
          })
        }
      },
      created() {
        let appId = this.$router.history.current.params.appId;
        let para={
          appId:appId
        };
        this.$http.checkUserPermission(para).then(res=>{
          this.isAdmin=res.data.result;
        }).catch(err=>{
          console.log("err",err)
        })
        this.getAppStrategy();
      }
    }
</script>

<style scoped>
</style>
