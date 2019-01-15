<template>
  <div  >
    <el-row>
      <div style="height: 30px;">
        <span>当前组织：</span><span>{{SelectedPerson.orgName	}}</span>
      </div>
    </el-row>
    <el-row style="margin-bottom: 10px;">
      <span>目标组织：</span>
      <span>{{currentNode.name	}}</span>
    </el-row>
    <el-row >
      <organization-tree @getCurrentOrganization="getCurrentOrganization"></organization-tree>
    </el-row>
    <el-row>
      <div style="text-align: end;">
        <el-button type="primary" @click="movePersonToTarget" > 移动 </el-button>
      </div>
    </el-row>
  </div>
</template>

<script>
    import OrganizationTree from "./OrganizationTree";
    export default {
        name: "MovePerson",
        components: {OrganizationTree},
      data(){
          return{
            currentNode:"",

          }
      },
      props:{
        SelectedPerson:"",
      },
      methods:{
        movePersonToTarget(){
          let value=this.currentNode;
          this.$emit('move',value);
        },
        getCurrentOrganization(msg) {
          this.currentNode = msg;
        },
      }
    }
</script>

<style scoped>

</style>
