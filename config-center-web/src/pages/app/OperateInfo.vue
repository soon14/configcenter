<template>
	<section>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane name="configs">
				<span slot="label"><i class="fa fa-wrench" aria-hidden="true"></i> 配置管理</span>
				<configs v-if="activeName === 'configs'" @getCurrentEnv="getCurrentEnv"></configs>
			</el-tab-pane>
			<el-tab-pane name="instances">
				<span slot="label"><i class="fa fa-sitemap" aria-hidden="true"></i> 实例信息</span>
				<instances v-if="activeName === 'instances'"></instances>
			</el-tab-pane>
      <el-tab-pane name="strategy">
        <span slot="label"><i class="el-icon-time"></i> 刷新策略</span>
        <strategys v-if="activeName === 'strategy'"></strategys>
      </el-tab-pane>
		</el-tabs>
	</section>
</template>

<script>
	import {mapGetters,mapMutations} from 'vuex';
	import types from "../../store/mutation-types";
	import Instances from './Instances';
	import Configs from '../config/Configs';
	import Strategys from '../strategy/ShowStrategy';
	export default {
		components: {
      Instances,
      Configs,
      Strategys
    },
    data() {
			return {
				activeName: 'configs',
				currentEnv: '',
			};
		},
		computed: {
	      ...mapGetters({
	        breadcrumbItems: 'breadcrumbItems',
	      })
	    },
	    methods: {
        getCurrentEnv(value) {
          this.currentEnv = value;
        },
	    	handleClick(tab,) {
	    		let itmes = [
		          {
		            name:"应用管理",
		            icon:"fa fa-windows",
		            path:"/apps",
		          },
		      	];
				let appId = this.$router.history.current.params.appId;
				if(tab.name === "instances") {
					itmes.push({name:"实例信息"});
					this.$router.push({
			          path: `/apps/${appId}/instances`,
			        });
				}
				if(tab.name === "configs") {
					itmes.push({name:"配置管理"});
					this.$router.push({
			          path: `/apps/${appId}/configs`,
			        });
				}
				if(tab.name === "strategy") {
          itmes.push({name:"刷新策略"});
          this.$router.push({
            path: `/apps/${appId}/strategy`,
          });
        }
				this.setBreadcrumbItems(itmes);
	    	},
	    	...mapMutations({
		        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
		    }),
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
            if(real === "instances") {
              this.activeName = "instances";
              itmes.push({name:"实例信息"});
            } else if(real === "configs") {
              this.activeName = "configs";
              itmes.push({name:"配置管理"});
            } else if(real === "strategy") {
              this.activeName = "strategy";
              itmes.push({name:"刷新策略"});
            }
            this.setBreadcrumbItems(itmes);
      }
	}
</script>

<style></style>
