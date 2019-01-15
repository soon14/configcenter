<template>
  <section>
    <el-col :span="24" class="toolbar" >
      <el-form :inline="true" :model="conditions" style="padding-top: 15px;padding-left: 10px" size="mini">
        <el-form-item label="实例名称">
          <el-input v-model="conditions.name" placeholder="请输入实例的名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="IP地址">
          <el-input v-model="conditions.ipAddress" placeholder="请输入IP地址" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="querySpecificVersionInstances">查询</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <c-table :table="tableData"></c-table>
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <div style="clear: both"></div>
  </section>
</template>

<script>
  import CTable from "../../components/Table";
  export default {
    name: "specific-version-instance",
    components:{
      CTable
    },
    props:['currentEnv','itemInfo'],
    data() {
      return {
        conditions:{
          name: '',
          ipAddress: '',
        },
        page:0,
        pagesync:this.page+1,
        total:0,
        tableData: {
          loading: false,
          data: [],
          columns: [
            {
              label: "实例名称",
              props: "insName"
            },
            {
              label: "实例地址",
              props: "insIp",
            }
          ],
        }
      }
    },
    methods: {
      handleCurrentChange(currentPage) {
        this.page = currentPage -1;
        this.getSpecificVersionInstances();
      },
      querySpecificVersionInstances(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getSpecificVersionInstances();
      },
      getSpecificVersionInstances() {
        let data = {
          envId:this.currentEnv,
          configId:this.itemInfo.configId,
          configType:this.itemInfo.configType,
          configVersion:this.itemInfo.configVersion,
          insName:this.conditions.name,
          insIp:this.conditions.ipAddress
        };
        let params = {
          currentPage: this.page,
          data:data,
          pageSize:10
        };
        //这里调用接口
        this.$http.getInsByConfig(params).then(res => {
          this.tableData.data = res.data.result.entities;
          this.total = res.data.result.count
        }).catch(err => {
          console.log('err',err);
        })
      }
    },
    created() {
      this.getSpecificVersionInstances();
    }
  }
</script>

<style scoped></style>
