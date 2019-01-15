<template>
  <el-table
    :data="table.data"
    style="width: 100%">

    <!-- 根据列的值判断是否使用模板 -->
    <template v-for="item in table.columns">
      <el-table-column v-if="item.hasSearch === true" :label="item.label"  :prop="item.props" :width="200" :render-header="renderHeader"/>

      <el-table-column v-else :label="item.label" :width="item.width?item.width: ''" :prop="item.props"/>
    </template>
    <!-- 根据列值判断是否使用模板来显示按钮操作 -->
    <el-table-column
      v-if="table.hasOperation === true && table.hasOperationTemplate === true"
      column-key="operation"
      :label="table.operation.label"
      :width="table.operation.width ? table.operation.width : ''">
      <template slot-scope="scope">
        <slot name="button-operation" :obj="scope"></slot>
      </template>
    </el-table-column>
    <!-- 如果使用了按钮操作，需要在父组件中去监听子组件的事件 -->
    <el-table-column v-else-if="table.hasOperation === true && !table.hasOperationTemplate"
                     column-key="operation"
                     :label="table.operation.label"
                     :width="table.operation.width ? table.operation.width : ''">
      <template slot-scope="scope">
        <el-button
          v-for="item in table.operation.data"
          :key="item.id"
          :size="item.size"
          :type="item.type"
          @click.stop="handleOperation(scope.$index, scope.row, item.id)">{{ item.label }}</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
  export default {
    name: "OrganizationTable",
    data(){
      return{
        headColumn:"",
      }
    },
    props: {
      table: {
        type:Object,
        default() {
              return {
                hasOperation: false, //是否有操作功能
                hasOperationTemplate: false, //父组件是否传递了模板
                columns: [],//表头数据
                data: [],  //表中的数据
                operation: {//操作中的按钮
                  label: '操作',
                  width: '100',
                  data: [
                  ],
                }
              }
         }
      }
      },

    methods:{

      renderHeader(h, { column, $index })
      {
        this.headColumn=$index;
        if($index){
          return h('div',
            [
                  h('div',{
                      style: 'display:inline;width:50px;float:left;',
                    },
                    column.label
                  ),
                  h('el-input',
                    {
                      style: 'width:60%;display:inline;float:left;',
                      attrs:[{ placeholder:"搜索..."},{size:"mini"},],

                      on: {
                        input: this.changeRole
                      },
                    }),
            ]);
        }else {
          return h('div',
            [
                  h('div',{
                      style: 'display:inline;width:50px;float:left;',
                    },
                    column.label
                  ),
                  h('el-input',
                    {
                      style: 'width:60%;display:inline;float:left;',
                      attrs:[{ placeholder:"搜索..."},{size:"mini"},],

                      on: {
                        input: this.change
                      },
                    }),

            ]);
        }

      },
      change(val)
      {
        let type="name";
        this.$emit("handleValueChange",val,type);
      },
      changeRole(val)
      {
        let type="role";
        this.$emit("handleValueChange",val,type);
      },
      //处理按钮的响应事件，将子组件的数据抛到父组件上,父组件上需要监听此事件
      handleOperation(index,row,item_id) {
        const data = this.table.operation.data;
        for(let i=0;i<data.length;i++) {
          if(item_id === data[i].id) {
            this.$emit(data[i].Fun,index,row)
          }
        }
      }

    },
    mounted: function () {
      this.notify.$on('changOrganization', function (data) {

      });
    }
  }

</script>

<style scoped>

</style>
