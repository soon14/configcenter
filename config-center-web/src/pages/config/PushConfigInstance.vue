<template>
    <section>
      <el-col :span="24" class="toolbar" >
        <el-form :inline="true" :model="conditions" style="padding-top: 15px;padding-left: 10px" size="small">
          <el-form-item label="实例名称">
            <el-input v-model="conditions.insName" placeholder="请输入实例的名称" clearable></el-input>
          </el-form-item>
          <el-form-item label="IP地址">
            <el-input v-model="conditions.insIp" placeholder="请输入IP地址" clearable></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="queryInstance">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="selectAllItem">全选</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="notSelectAllItem">全不选</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-table ref="multipleTable" :data="pushConfigInstanceData" tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="insName" label="实例名称"></el-table-column>
        <el-table-column prop="insIp" label="实例地址"></el-table-column>
      </el-table>

      <el-col :span="24" class="toolbar">
        <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange" :current-page.sync="pagesync" :page-size="5" :total="total" style="float:right;"></el-pagination>
      </el-col>
      <div style="clear: both"></div>
    </section>
</template>
<script>
    import CTable from "../../components/Table";
    export default {
        name: "push-config-instance",
        components: {
          CTable
        },
        props:['selectedEnv'],
        data() {
            return {
              conditions: {
                insName:"",
                insIp:"",
              },
              //1、普通的选择，2、全选
              selectedType:1,
              selectedItem:[],
              cancelItem:[],
              page:0,
              pagesync:this.page+1,
              total:0,
              pushConfigInstanceData:[],
              operateCurrentPage:true,
            }
        },
        methods: {

          queryInstance() {
            this.selectedType = 1;
            this.cancelItem = [];
            this.selectedItem = [];
            this.pagesync=1;
            this.page=this.pagesync-1;
            this.getAppInstances();
          },
          getUnContainerItem(val) {
            let unContainerItem = [];
            for(let i=0;i<this.pushConfigInstanceData.length;i++) {
              let flag = false;
              let item = this.pushConfigInstanceData[i];
              for(let j=0;j<val.length;j++) {
                if(item.id === val[j].id) {
                  flag = true;
                  break;
                }
              }
              if(!flag) {
                unContainerItem.push(item);
              }
            }
            return unContainerItem;
          },
          handleSelectionChange(val) {
            if(this.selectedType === 2 && this.operateCurrentPage) {
              //选中时，如果被取消列表中的元素又被选中了，需要将被选中的列表中的元素移除
              for(let i=0;i<val.length;i++) {
                let index = this.cancelItem.indexOf(val[i].id);
                if(index !== -1) {
                  this.cancelItem.splice(index,1);
                }
              }
              //得到未被选中的元素
              let unContainerItem = this.getUnContainerItem(val);
              //将未被选中的元素，且还没有存在于取消列表中的元素添加到取消列表中
              for(let i=0;i<unContainerItem.length;i++) {
                if(this.cancelItem.indexOf(unContainerItem[i].id) === -1) {
                  this.cancelItem.push(unContainerItem[i].id);
                }
              }
            } else if(this.selectedType === 1 && this.operateCurrentPage) {
              //将当前选中的元素添加到选中列表中
              for(let i=0;i<val.length;i++) {
                this.selectedItem.push(val[i].id);
              }
              //对选中的元素进行去重
              this.selectedItem = Array.from(new Set(this.selectedItem));
              //得到未被选中的元素
              let unContainerItem = this.getUnContainerItem(val);
              //如果未被选中的元素存在于已经选中的元素，需要将元素移除
              for(let i=0;i<unContainerItem.length;i++) {
                let index = this.selectedItem.indexOf(unContainerItem[i].id);
                if(index !== -1) {
                  this.selectedItem.splice(index,1)
                }
              }
            }
          },

          handleCurrentChange(currentPage) {
            this.page = currentPage - 1;
            this.operateCurrentPage = false;
            this.getAppInstances();
          } ,

          selectAllItem() {
            this.selectedType = 2;
            this.pushConfigInstanceData.forEach(row => {
              this.$refs.multipleTable.toggleRowSelection(row,true);
            });
            this.cancelItem = [];
            this.selectedItem = [];
          },

          notSelectAllItem() {
            //清空选中的列表，同时将被取消的列表也设置为空
            this.$refs.multipleTable.clearSelection();
            this.selectedType = 1;
            this.cancelItem = [];
            this.selectedItem = [];
          },
          //提供给调用此组件的父组件使用的
          returnResultToParent() {
            let data = {
              selectedType:this.selectedType,
              selectedItem:this.selectedItem,
              cancelItem:this.cancelItem,
              insName:this.conditions.insName,
              insIp:this.conditions.insIp
            };
            return data;
          },

          getAppInstances() {
            let data = {
              envId:this.selectedEnv,
              insIp:this.conditions.insIp,
              insName:this.conditions.insName,
              isAlive:1,
            };
            let params = {
              currentPage: this.page,
              data:data,
              pageSize:5,
            };
            this.$http.getInstances(params).then(res => {
              this.pushConfigInstanceData = res.data.result.entities;
              this.total = res.data.result.count;
            }).catch(err => {
              console.log('err',err);
            })
          },
        },
        created() {
          this.getAppInstances();
        },

        updated() {
          if(this.selectedType === 2 && !this.operateCurrentPage) {
            //全选时，换页时需要将不存在与取消列表的元素勾选上
            for(let i=0;i<this.pushConfigInstanceData.length;i++) {
              let index = this.cancelItem.indexOf(this.pushConfigInstanceData[i].id);
              if(index === -1) {
                this.$refs.multipleTable.toggleRowSelection(this.pushConfigInstanceData[i],true);
              }
            }
            this.operateCurrentPage = true;
          } else if(this.selectedType === 1 && !this.operateCurrentPage) {
            //不全选时，需要选中的列表勾选上
            for(let i=0;i<this.pushConfigInstanceData.length;i++) {
              let index = this.selectedItem.indexOf(this.pushConfigInstanceData[i].id);
              if(index !== -1) {
                this.$refs.multipleTable.toggleRowSelection(this.pushConfigInstanceData[i],true);
              }
            }
            this.operateCurrentPage = true;
          }
        }
    }
</script>

<style scoped>

</style>
