<template>
  <div>
    <OTable :table="tableData" :table-events="tableEvents">
    </OTable>
  </div>
</template>

<script>
  import OTable from '../../components/Table';

  export default {
    name: "Company",
    components: {
      OTable
    },
    data() {
      return {
        conditions: {
          company: ''
        },
        tableData: {
          loading: false,
          data: [],
          columns: [
            {
              label: "公司",
              props: "name"
            }, {
              label: "详情",
              props: "description"
            },
          ],
        },
        tableEvents: {
          rowDbClick: this.selectOrganization
        },
      }
    },
    methods: {
      getCompanyList() {
        this.$http.getCompanyList().then(res => {
          this.tableData.data = res.data.result;
          this.tableData.loading = false;
        }).catch(err => {
          console.log('err', err);
          this.tableData.loading = false;
        })
      },
      selectOrganization(row, event) {
        this.$emit('dealSelectRow',row)
      }
    },
    created () {
      this.getCompanyList();
    }
  }
</script>

<style scoped>

</style>
