<template>
	<section>
		<el-row type="flex" justify="end">
			<el-button type="primary" @click="addEnv" size="small">新增环境</el-button>
		</el-row>

		<c-table :table="tableData">
			<template slot-scope="props" slot="envName">
				<el-tooltip :content="props.obj.row.description" placement="top">
				<span>{{props.obj.row.envName}}</span></el-tooltip>
			</template>
			<template slot-scope="props" slot="updateTime">
				{{timestampToFullDate1(props.obj.row.updateTime)}}
			</template>
			<template slot-scope="props" slot="button-operation">
				<el-button type="primary" icon="el-icon-edit" circle size="mini" @click="updateEnv(props.obj.$index, props.obj.row)"></el-button>
				<el-button type="danger" icon="el-icon-delete" circle size="mini" @click="deleteEnv(props.obj.$index, props.obj.row)"></el-button>
		    </template>
		</c-table>

		<el-dialog :title="title" :visible.sync="addEnvVisible" width="60%" :close-on-click-modal="false">
	      <el-form :model="envInfo" label-width="120px" ref="envInfo" :rules="rules">
	        <el-form-item label="环境名称" prop="envName">
	            <el-input v-model="envInfo.envName" :disabled="disabled" style="width: 500px"></el-input>
	            <el-button type="primary" v-if="title=='修改环境标签'" @click="disabled = false">修改</el-button>
	        </el-form-item>
	        <el-form-item label="环境描述" prop="desc">
	          <el-input type="textarea" v-model="envInfo.desc" style="width: 500px"></el-input>
	        </el-form-item>
	      </el-form>
	      <div slot="footer" class="dialog-footer">
	        <el-button @click="handleCancel('envInfo')">取消</el-button>
	        <el-button @click="handleSubmit('envInfo')" type="primary">提交</el-button>
	      </div>
    </el-dialog>

	</section>
</template>

<script>
  import CTable from "../../components/Table.vue";
  import {timestampToFullDate} from "../../common/date";
  import {CHECK_ENV_NAME} from '../../common/regexp';
  import {mapGetters} from 'vuex';

	export default {
		components: {
	      CTable,
	    },
		name: "Envs",
		data() {
			var validateDesc = (rule, value, callback) => {
		        if (value.length > 1000) {
		          callback(new Error('描述信息不能超过1000个字符'));
		        } else {
		          callback();
		        }
		    };
			return {
				title:"新增环境标签",
				addEnvVisible:false,
				disabled:false,
				envLoading:false,
				envInfo:{
					appId:"",
					envId:"",
					envName:"",
					desc:"",
				},

				rules:{
				  envName: [
		            {required: true, message: '环境的名称不能为空', trigger: 'blur' },
		            {pattern: CHECK_ENV_NAME, message:'环境的名称应该在2-64个英文字符之间，只能由英文字母和数字加上下划线组成，首字母只能为字母', trigger: ['blur', 'change']}
		          ],
          desc:[
            { validator: validateDesc, trigger: 'blur' }
          ]
				},
				tableData: {
					data:[
					],
					columns:[
		            {
		              label:"环境标签",
		              props:"envName",
		              show:true,
		            },{
		              label:"创建人",
		              props:"creatorName",
		            }, {
		              label:"修改时间",
		              props:"updateTime",
		              show:true
		            }
		          ],
		           hasOperation:true,
          		   hasOperationTemplate:true,
          		   operation: {
		            label:"操作",
		            data:[
		            ]
		          }
				},
			};
		},
		computed: {
	      ...mapGetters({
	        userInfo: 'userInfo',
	      }),
	    },
		methods:{
			addEnv() {
				this.addEnvVisible=true;
				this.title='新增环境标签';
				this.disabled=false;
				this.envInfo.envId = "";
				this.envInfo.envName = "";
				this.envInfo.desc = "";
			},
			timestampToFullDate1(date) {
				return timestampToFullDate(date);
			},

			handleCancel(formName) {
				this.$refs[formName].resetFields();
				this.addEnvVisible=false;
			},
			handleSubmit(formName) {
				const data = this.envInfo;
				let appId = this.$router.history.current.params.appId;
				data.appId = appId;
				this.$refs[formName].validate((valid) => {
					if(valid) {
						if(!data.envId) {
							this.$http.createAppEnv(data).then(res => {
							this.addEnvVisible = false;
							this.getAppEnvs();
							this.$message({
								message: res.data.result,
								type: 'success',
							});
							}).catch((err) => {
								console.log(err);
							})
						} else {
							this.$http.updateAppEnv(data).then(res => {
							this.addEnvVisible = false;
							this.getAppEnvs();
							this.$message({
								message: res.data.result,
								type: 'success',
							});
							}).catch((err) => {
								console.log(err);
							})
						}
					}
				})

			},

			getAppEnvs() {
				let appId = this.$router.history.current.params.appId;
				this.envLoading = true;
				this.$http.getAppEnvs({appId:appId}).then(res => {
		          this.tableData.data = res.data.result;
		          this.envLoading = false;
		        }).catch(err => {
		          this.envLoading = false;
		          console.log('err',err);
		        })
			},

			deleteEnv(index, row) {
				this.$confirm('确定要删除环境标签吗？','提示',{
						type: "warning",
					}).then(() => {
						this.$http.deleteAppEnv({envId:row.id}).then(res => {
							this.$message({
								message:res.data.result,
								type:'success',
							});
							this.getAppEnvs();
						})
					}).catch(() => {
					})
			},
			updateEnv(index, row) {
				this.addEnvVisible = true;
				this.title = "修改环境标签";
				this.envInfo.envId = row.id;
				this.envInfo.envName = row.envName;
				this.envInfo.desc = row.description;
				this.disabled = true;
			}
		},
		created() {
			this.getAppEnvs();
		},
		mounted() {},
	}
</script>

<style></style>
