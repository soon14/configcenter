<template>
  <div>
    <el-col :span="24" style="background-color: #f2f2f2">
      <el-form :inline= "true" :model="conditions" ref="conditions" style="margin-top: 20px;margin-left: 10px">
        <el-form-item label="消息创建人" prop="reviewedPerson">
          <el-input v-model="conditions.reviewedPerson" placeholder="请输入消息创建人" size="small"></el-input>
        </el-form-item>

        <el-form-item label="通知类型" prop="type">
          <c-select :select="selectData" v-model="conditions.type" size="small"/>
        </el-form-item>

        <el-form-item label="是否已读" prop="has_read">
          <el-select v-model="conditions.has_read" placeholder="是否已读" size="small" value="0">
            <el-option label="已读" value="1"></el-option>
            <el-option label="未读" value="0"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" @click="queryNotification">查询</el-button>
          <el-button type="primary" size="small" @click="resetCondition('conditions')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <c-table :table="tableData">
      <template slot-scope="props" slot="notificationTitle">
        <el-tag size="medium">{{getValueByKey1(props.obj.row.notificationType,selectData.options)}}</el-tag>
      </template>
      <template slot-scope="props" slot="createTime">
        {{timestampToFullDate(props.obj.row.createTime)}}
      </template>
      <template slot-scope="props" slot="button-operation">
        <template v-if="props.obj.row.hasRead===0">
          <el-button v-if="showOperateButton(props.obj.$index, props.obj.row)" type="success"
                     @click="operateNotification(props.obj.$index, props.obj.row) "
                     size="mini">去审核</el-button>
          <el-button type="primary" size="mini" @click="knowTheNotification(props.obj.$index, props.obj.row)">我知道了</el-button>
        </template>
        <template v-else>
          <el-button type="danger" size="mini" @click="delTheNotification(props.obj.$index, props.obj.row)">删除</el-button>
        </template>
      </template>
    </c-table>
    <!-- 分页 -->
    <el-col :span="24" class="toolbar">
      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <div style="clear: both"></div>
  </div>
</template>

<script>
import CTable from "../../components/Table.vue";
import CSelect from "../../components/Select";
import { timestampToFullDate } from "../../common/date";
import { getValueByKey } from "../../common/utils";

export default {
  components: {
    CTable,
    CSelect
  },
  name: "Notice",
  data() {
    return {
      tableData: {
        loading: false,
        data: [],
        columns: [
          {
            label: "消息创建人",
            props: "creatorNickName",
            width: 120
          },
          {
            label: "通知类型",
            props: "notificationTitle",
            show: true,
            width: 150
          },
          {
            label: "通知内容",
            props: "notificationContent"
          },
          {
            label: "申请时间",
            props: "createTime",
            show: true,
            width: 150
          }
        ],
        hasOperation: true,
        hasOperationTemplate: true,
        operation: {
          label: "操作",
          data: [],
          width: 200
        }
      },
      selectData: {
        options: [],
        clearable: true,
        placeholder: "请选择通知类型"
      },
      conditions: {
        reviewedPerson: "",
        type: "",
        has_read: "0"
      },
      page: 0,
      pagesync:this.page+1,
      total: 0
    };
  },
  methods: {
    getValueByKey1(key, array) {
      return getValueByKey(key, array);
    },
    timestampToFullDate(value) {
      return timestampToFullDate(value);
    },
    handleCurrentChange(currentPage) {
      this.page = currentPage - 1;
      this.getNotification();
    },
    knowTheNotification(index, row) {
      this.$http.consumeNotification({id: row.id}).then(res => {
        this.$message({
          message: res.data.result,
          type: 'success',
        });
        this.getNotification();
      }).catch(err => {
        console.log('err');
      })
    },
    delTheNotification(index, row) {
      this.$confirm('确定删除此条消息?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '后台接口尚未实现'
        });
      }).catch(() => {

      });
    },
    showOperateButton(index, row) {
      let result = this.selectData.options.filter(item => {
        return item.value == row.notificationType;
      });
      if (result && result[0] && result[0].ext2){
        row.routerUrl = result[0].ext2;
        row.routerName = result[0].ex3;
        return true;
      }else{
        return false;
      }
    },
    operateNotification(index, row) {
      //调用后台消费信息接口
      this.$http.consumeNotification({id:row.id}).then(res => {
        this.getNotification();
        this.$emit("closeNotice");
      });
      let params = {
        data:row
      };
      this.$router.push({
        path:"/middle-router",
        name: "路由跳转", //必须,不然参数传不过去
        params: params
      });
    },
    resetCondition(conditionName) {
      this.$refs[conditionName].resetFields();
      this.getNotification();
    },
    queryNotification(){
      this.pagesync=1;
      this.page=this.pagesync-1;
      this.getNotification()
    },
    //调用后台接口来获取数据
    getNotification() {
      let data = {
        creator: this.conditions.reviewedPerson,
        type: this.conditions.type,
        hasRead: this.conditions.has_read
      };
      let params = {
        currentPage: this.page,
        pageSize: 10,
        data: data
      };
      this.tableData.loading = true;
      this.$http
        .getNotificationInfo(params)
        .then(res => {
          this.total = res.data.result.count;
          this.tableData.data = res.data.result.entities;
          this.tableData.loading = false;
        })
        .catch(err => {
          console.log('err',err);
          this.tableData.loading = false;
        });
    },
    //调用后台接口来获取下拉列表
    getSelectData() {
      this.$http.getSelectData("NOTIFICATION_TYPE").then(res => {
        this.selectData.options = res.data.result;
      });
    }
  },
  created() {
    this.getNotification();
    this.getSelectData();
  }
};
</script>

<style>
</style>
