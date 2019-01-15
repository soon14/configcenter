<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane name="envs">
			<span slot="label"><i class="fa fa-tags" aria-hidden="true"></i> 环境列表</span>
			<envs></envs>
		</el-tab-pane>
		<el-tab-pane name="members">
			<span slot="label"><i class="fa fa-users" aria-hidden="true"></i> 成员列表</span>
			<members></members>
		</el-tab-pane>
    <el-tab-pane name="approves">
      <span slot="label"><i class="fa fa-bandcamp" aria-hidden="true"></i> 审核策略</span>
        <show-approve></show-approve>
    </el-tab-pane>
	</el-tabs>
</template>

<script>
	import Envs from "./Envs";
	import Members from "./Members";
	import ShowApprove from "../approve/ShowApprove";
	import {mapGetters,mapMutations} from 'vuex';
	import types from "../../store/mutation-types";
	export default {
		components: {
	      Envs,
	      Members,
        ShowApprove
	    },
		data() {
			return {
				activeName: 'envs'
			};
		},
		computed: {
	      ...mapGetters({
	        breadcrumbItems: 'breadcrumbItems',
	      })
	    },
		methods: {
			handleClick(tab,event) {
				let items = [
		          {
		            name:"应用管理",
		            icon:"fa fa-windows",
		            path:"/apps",
		          },
		      	];
				let appId = this.$router.history.current.params.appId;
				if(tab.name == "envs") {
          items.push({name:"环境列表"});
					this.$router.push({
			          path: `/apps/${appId}/envs`,
			        });
				}
				if(tab.name == "members") {
          items.push({name:"成员列表"});
					this.$router.push({
			          path: `/apps/${appId}/members`,
			        });
				}
				if(tab.name == "approves") {
          items.push({name:"审核策略"});
				  this.$router.push({
            path: `/apps/${appId}/approves`,
          });
        }
				this.setBreadcrumbItems(items);
			},
			...mapMutations({
		        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
		    })
		},
		created(){
			let path = this.$router.history.current.path.split('/');
			let itmes = [
	          {
	            name:"应用管理",
	            icon:"fa fa-windows",
	            path:"/apps",
	          },
	      	];
	      	let real = path[path.length-1];
	      	if(real == "envs") {
	      		this.activeName = "envs";
	      		itmes.push({name:"环境列表"});
	      	} else if(real == "members") {
	      		this.activeName = "members";
	      		 itmes.push({name:"成员列表"});
	      	} else if(real == "approves") {
            this.activeName = "approves";
            itmes.push({name:"审核策略"});
          }
	      	this.setBreadcrumbItems(itmes);
		}
	}
</script>

<style>

</style>
