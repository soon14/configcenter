<template>
    <section>
      <div class="tree_div">
        <el-input
          style="margin-bottom: 10px"
          placeholder="快速查询组织信息"
          size="small"
          v-model="filterText">
        </el-input>

        <el-tree
          :data="data"
          highlight-current
          :props="defaultProps"
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
          ref="tree">
        </el-tree>
      </div>
    </section>
</template>

<script>
    export default {
        name: "organization-tree",
        props:['bussiType','currentEnv'],
        data() {
          return {
            filterText: '',
            data: [],
            defaultProps: {
              children: 'children',
              label: 'name'
            }
          };
        },
        watch: {
          filterText(val) {
            this.$refs.tree.filter(val);
          }
        },
      methods: {
        filterNode(value, data) {
          if (!value) return true;
          return data.name.indexOf(value) !== -1;
        },
        handleNodeClick(data) {
          this.$emit('getCurrentOrganization',data);
        },
        changeResultToData(result,parentNodeleader) {
          if(result && result.length>0) {
            for(let i=0;i<result.length;i++) {
              if(result[i].children && result[i].children.length>0) {
                if(result[i].level==1){
                  result[i].parentLeader=result[i].leader;
                }
                for(let j=0;j<result[i].children.length;j++) {
                  result[i].children[j].parentId = result[i].id;
                  result[i].children[j].parentName = result[i].name;
                  result[i].children[j].parentLeader = result[i].leader;
                  if(result[i].children[j].children && result[i].children[j].children.length>0) {
                    this.changeResultToData(result[i].children[j].children,result[i].children[j].leader);
                  }
                }
              }else if(result[i].level==1){
                result[i].parentLeader=result[i].leader;
              }else {
                result[i].parentLeader=parentNodeleader;
              }

            }
          }
          return result;
        },
        getAllLevelOneOrg() {
          this.$http.getAllLevelOneOrg().then(res => {
            let data = this.changeResultToData(res.data.result);
            this.data = data;
          }).catch(err => {
            console.log('err',err);
          })
        },
        getOrgTree() {
          this.$http.getOrgTree({envId:this.currentEnv}).then(res => {
            let data = this.changeResultToData(res.data.result);
            let item = {
              id:0,
              name:"任务提交人所在组织",
              level:1,
              children:[],
            };
            data.push(item);
            this.data = data;
          })
        }
      },
      created() {
          if(this.bussiType && this.bussiType == "approve") {
            this.getOrgTree();
          } else {
            this.getAllLevelOneOrg();
          }
      }
    }
</script>

<style scoped>
  .tree_div{
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    border: 1px solid #D9D9D9;
    padding:10px;
    min-height: 500px;
  }
</style>
