<template>
    <section>
        <el-row v-if="approveInfoData.length>0 && controlVisible==true">
          <item-info v-for="(item, index) in approveInfoData"
                     :key="index" :sequence="index" :item="item"
                     :operatorType="operatorType"
                     :currentEnv="currentEnv"
                     :approves="approveInfoData"
                     @deleteItem="deleteItem(item)"
                     @getSavedItemInfo="updateItemInfo"></item-info>
        </el-row>
        <el-row type="flex" justify="end">
            <el-button type="primary" size="small" @click="addStep">新增步骤</el-button>
        </el-row>
    </section>
</template>

<script>
    import ItemInfo from "./ItemInfo";
    export default {
        name: "add-approve",
        components: {
          ItemInfo
        },
        props:['approveResult','currentEnv'],
        data() {
          return {
            uuid:0,
            approveInfoData:[],
            operatorType:"add",
            controlVisible:true
          }
        },
        methods:{
          addStep() {
            if(this.approveInfoData.length > 0 && !this.approveInfoData[this.approveInfoData.length-1].id) {
              this.$message({
                message:"请先保存第"+this.approveInfoData.length+"步",
                type:'error',
              });
            } else{
              this.uuid = this.uuid + 1;
              this.approveInfoData.push({
                uniqueId:this.uuid,
              });
            }
          },
          updateItemInfo(returnResult) {
            let index = -1;
            for(let i=0;i<this.approveInfoData.length;i++) {
              if(this.approveInfoData[i].uniqueId == returnResult.uniqueId) {
                index = i;
                break;
              }
            };
            let message = "";
            if(this.approveInfoData[index].id) {
              message = "修改步骤成功";
            } else {
              message = "新增步骤成功";
              let result = {
                id:returnResult.id,
                uniqueId:returnResult.uniqueId,
                orgId:returnResult.orgId,
                orgName:returnResult.orgName,
                roleId:returnResult.roleId
              };
              this.approveInfoData.splice(index,1,result);
            };
            this.$message({
              message:message,
              type:'success',
            });
          },

          deleteItem(item) {
            this.controlVisible = false;
            this.$confirm('确定要移除此步骤吗？','提示',{
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: "warning",
            }).then(() => {
              if(item.id) {
                let data = {
                  envId: this.currentEnv,
                  stepId: item.id,
                };
                this.$http.delAuditStrategyStep(data).then(res => {
                })
              }
              this.approveInfoData = this.approveInfoData.filter(value => {
                return value.uniqueId != item.uniqueId;
              });
              this.controlVisible = true;
              this.$message({
                message:"移除成功",
                type:'success',
              });
            }).catch(() => {
              this.controlVisible = true;
              this.$message({
                message:"已取消",
                type:'info',
              });
            });
          },
          isAllItemSaved() {
            if(this.approveInfoData.length>0 && !this.approveInfoData[this.approveInfoData.length -1].id) {
              this.$message({
                message:"关闭前请先保存第"+this.approveInfoData.length+"步骤的数据，如果不需要请先移除后再关闭页面",
                type:"error",
              });
              return false;
            } else {
              return true;
            }
          }
        },
      created() {
          if(this.approveResult && this.approveResult.length>0) {
            for(let i=0;i<this.approveResult.length;i++) {
              this.uuid = this.uuid + 1;
              this.approveResult[i].id = this.approveResult[i].stepId;
              this.approveResult[i].uniqueId = this.uuid;
            }
            this.approveInfoData = this.approveResult;
            this.operatorType = "update"
          }
      }
    }
</script>

<style scoped>

</style>
