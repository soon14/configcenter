<template>
	<section>
		<el-row>
			<el-select v-model="envOption" filterable  placeholder="请选择环境" @change="changeAppEnv" size="small">
				<span v-for="option in envOptions" :key="option.id">
					<el-option :label="option.envName" :value="option.id.toString()"
			    	></el-option>
				</span>
			</el-select>
		</el-row>
		<el-row>
			<el-col :span="24" class="toolbar" >
		      <el-form :inline="true" :model="conditions" size="small" style="padding-top: 20px;padding-left: 10px">
		        <el-form-item label="实例名称">
		          <el-input v-model="conditions.name" placeholder="请输入实例的名称" clearable></el-input>
		        </el-form-item>
		        <el-form-item label="IP地址">
		        	<el-input v-model="conditions.ipAddress" placeholder="请输入IP地址" clearable></el-input>
		        </el-form-item>
		        <el-form-item label="连接状态">
		        	<el-select v-model="conditions.status" :clearable="true" placeholder="请选择连接状态">
						<el-option label="连接" value="1"/>
						<el-option label="断开" value="0"/>
					</el-select>
		        </el-form-item>
		        <el-form-item>
		          <el-button type="primary" @click="queryInstance">查询</el-button>
		        </el-form-item>
		      </el-form>
		    </el-col>
		    <c-table :table="tableData" :tableEvents="tableEvents">
		    	<template slot-scope="props" slot="isAlive">
			    	<el-tag :type="getStatus(props.obj.row.isAlive)">{{getStatusText(props.obj.row.isAlive)}}</el-tag>
			    </template>
			    <template slot-scope="props" slot="button-operation">
			    	<el-button type="danger" v-if="props.obj.row.isAlive == 0" size="small" @click="breakUpConnection(props.obj.$index, props.obj.row)">移除</el-button>
			    </template>
			</c-table>
			<!-- 分页查询 -->
		    <el-col :span="24" class="toolbar">
		      <el-pagination layout="total, prev, pager, next" @current-change="handleCurrentChange"  :current-page.sync="pagesync"  :page-size="10" :total="total" style="float:right;">
		      </el-pagination>
		    </el-col>
		</el-row>
		<el-row v-if="tableDataItem.length>0">
			<el-dialog title="实例下的配置信息" :visible.sync="showConfigVisible" width="80%" :close-on-click-modal="false">
		      <config-item :tableDataItem="tableDataItem"></config-item>
		    </el-dialog>
		</el-row>
	</section>
</template>

<script>
	import CTable from "../../components/Table.vue";
	import ConfigItem from "./ShowConfigItem.vue";
	import {mapMutations,mapGetters} from 'vuex';
	import types from "../../store/mutation-types";
	export default {
		components: {
		    CTable,
		    ConfigItem
		},
		name: "Instances",
		data() {
			return {
				conditions:{
					name: '',
					ipAddress: '',
					status: '',
				},
				envOptions:[],
				envOption:'',
				tableDataItem:[],
				page:0,
        pagesync:this.page+1,
				total:0,
				tableData: {
					loading:false,
		            data:[
		            ],
		            columns:[
			            {
			              label:"实例名称",
			              props:"insName",
			            },
			            {
			               label:"IP地址",
			               props:"insIp",
			            },
			            {
			              label:"连接状态",
			              props:"isAlive",
			              show:true,
			            },
			        ],
			        hasOperation:true,
			        hasOperationTemplate:true,
			        operation: {
			        label:"操作",
			            data:[
			            ]
			        }
				},
				tableEvents: {
					rowDbClick:this.showConfigItem
				},
				showConfigVisible:false,
			}
		},
		methods: {
			getAppEnvs() {
				let appId = this.$router.history.current.params.appId;
				this.$http.getAppEnvs({appId:appId}).then(res => {
		          this.envOptions = res.data.result;
		          this.envOption = this.getCurrentAppEnv()?this.getCurrentAppEnv():res.data.result[0].id.toString();
		          this.getAppInstances();
		        }).catch(err => {
		          this.envLoading = false;
		          console.log('err',err);
		        })
			},
      queryInstance(){
        this.pagesync=1;
        this.page=this.pagesync-1;
        this.getAppInstances()
      },
			getAppInstances() {
        let appId = this.$router.history.current.params.appId;
				let data = {
					appId:appId,
					envId:this.envOption,
					insIp:this.conditions.ipAddress,
					insName:this.conditions.name,
					isAlive:this.conditions.status?this.conditions.status:-1,
				};
				let params = {
					currentPage:this.page,
					data:data,
					pageSize:10,
				};
				this.tableData.loading = true;
				this.$http.getInstances(params).then(res => {
					this.tableData.loading = false;
					this.tableData.data = res.data.result.entities;
					this.total = res.data.result.count;
				}).catch(err => {
					this.tableData.loading = false;
				})
			},
			showConfigItem(row) {
				//环境的ID
				let appId = this.$router.history.current.params.appId;
				let data = {
					appId:appId,
					envId:this.envOption,
					insId:row.id,
				};
				this.tableDataItem = [];
				this.$http.getInstancesConfigInfo(data).then(res => {
					this.tableDataItem = res.data.result;
				}).catch(err=> {
					console.log(err);
				});
				this.showConfigVisible = true;
			},
			handleCurrentChange(currentPage) {
				this.page = currentPage - 1;
        this.getAppInstances();
			},
			getStatus(status) {
				return status == 1 ? "success" : "danger";
			},
			getStatusText(status) {
				return status == 1 ? "连接" : "断开";
			},
			breakUpConnection(index, row) {
				this.$confirm('确定要移除断开的实例吗？','提示',{
					type: "warning",
				}).then(() => {
          let appId = this.$router.history.current.params.appId;
          let data = {
            appId:appId,
            envId:this.envOption,
            insId:row.id,
          };
				  this.$http.deleteInstance(data).then(res => {
            this.$message({
              message:res.data.result,
              type:'success',
            });
            this.getAppInstances();
          }).catch(err => {
            console.log('err',err);
          });
				}).catch(() => {
				})
			},
			changeAppEnv() {
				this.saveAppEnv(this.envOption);
				this.getAppInstances();
			},
			...mapGetters({
		      	getCurrentAppEnv:'currentAppEnv',
		    }),
		    ...mapMutations({
		        saveAppEnv: types.SAVE_APP_ENV,
		    }),
		},
		created() {

			this.getAppEnvs();
		},
		mounted () {
		},
	}
</script>

<style>
	.toolbar {
	    background: #f2f2f2;
	    margin: 10px 0px;
	}
</style>
