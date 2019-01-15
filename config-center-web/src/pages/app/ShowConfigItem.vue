<template>
	<section>
		<c-table :table="tableData">
			<template slot-scope="props" slot="configType">
			    	<el-tag :type="getStatus(props.obj.row.configType)">{{getStatusText(props.obj.row.configType)}}</el-tag>
			</template>
      <template slot-scope="props" slot="commitTime">
        {{timestampToFullDate1(props.obj.row.commitTime)}}
      </template>
		</c-table>
	</section>
</template>

<script>
	import CTable from "../../components/Table.vue";
  import {timestampToFullDate} from "../../common/date";
	export default {
		name:'ConfigItem',
		components: {
		    CTable,
		},
		props: {
			tableDataItem:{
				type: Array,
				default:[],
			}
		},
		data() {
			return {
				tableData: {
					loading:false,
					data:this.tableDataItem,
		            columns:[
			            {
			              label:"配置名称",
			              props:"configName",
			            },
			            {
			               label:"配置类型",
			               props:"configType",
			               show:true,
			            },
			            {
			              label:"版本号",
			              props:"configVersion",
			            },
                  {
                    label:"提交时间",
                    props:"commitTime",
                    show:true,
                  }
			        ],
				},
			}
		},

		methods: {
      timestampToFullDate1(date) {
        return timestampToFullDate(date);
      },
			getStatus(type) {
				return type == 1 ? "success" : "danger";
			},
			getStatusText(type) {
				return type == 1 ? "配置文件" : "配置项";
			}
		},

		created() {

		},
		mounted() {
		}
	}
</script>

<style></style>
