<template>
  <div align="center">
    <el-button type="primary" @click="cleanCache">清除redis缓存</el-button>
  </div>
</template>

<script>
  import {mapGetters,mapMutations} from 'vuex';
  import types from "../../store/mutation-types";

  export default {
    name: "CleanCache",
    methods: {
      cleanCache () {
        this.$http.cleanCache().then(() => {
          this.$notify({
            title: '成功',
            message: '缓存清理成功',
            type: 'success',
            duration: 2000
          })
        })
      },
      ...mapMutations({
        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
    },
    mounted() {
      let itmes = [
        {
          name:"清除缓存",
          icon:"fa fa-cog",
          path:"/clean-cache",
        },
      ];
      this.setBreadcrumbItems(itmes);
    }
  }
</script>

<style scoped>

</style>
