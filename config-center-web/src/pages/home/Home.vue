<template>
  <section>
      <el-row style="margin-top: 50px">
          <el-col :span="18">
              <el-row type="flex" justify="space-around">
                <el-col :span="6" style="textAlign:center">
                  <span @click.stop="goToApp">
                    <card-info cardName="应用数" :cardNum="homeData.appNum" colorName="backgroundColor:#915c93" card-icon="el-icon-menu"></card-info>
                  </span>
                </el-col>
                <el-col :span="6" style="textAlign:center">
                  <card-info cardName="环境数" :cardNum="homeData.envNum" colorName="backgroundColor:#915c93" card-icon="fa fa-tag"></card-info>
                </el-col>
                <el-col :span="6" style="textAlign:center">
                  <card-info cardName="实例数" :cardNum="homeData.insNum" colorName="backgroundColor:#915c93" card-icon="fa fa-life-ring"></card-info>
                </el-col>
              </el-row>
              <el-row type="flex" justify="space-around" style="margin-top: 50px">
                <el-col :span="6" style="textAlign:center">
                  <span @click.stop="clickToTask('pass')">
                    <card-info cardName="通过任务" :cardNum="homeData.passTaskNum" colorName="backgroundColor:#FA9E05" card-icon="el-icon-check"></card-info>
                  </span>
                </el-col>
                <el-col :span="6" style="textAlign:center">
                  <span @click.stop="clickToTask('review')">
                    <card-info cardName="审核任务" :cardNum="homeData.auditTaskNum" colorName="backgroundColor:#FA9E05" card-icon="el-icon-info"></card-info>
                  </span>
                </el-col>
                <el-col :span="6" style="textAlign:center">
                  <span @click.stop="clickToTask('back')">
                      <card-info cardName="打回任务" :cardNum="homeData.rollBackTaskNum" colorName="backgroundColor:#FA9E05" card-icon="el-icon-close"></card-info>
                  </span>
                </el-col>
              </el-row>
          </el-col>
          <el-col :span="6" class="card-col">
            <el-card class="box-card">
              <div slot="header">
                <span>最近访问的项目</span>
              </div>
              <div v-for="item in homeData.appInfos" :key="item.appId" style="margin-top: 10px">
                <span class="hover_click" @click="goToAppItem(item)">{{item.appName}}</span>
              </div>
            </el-card>
          </el-col>
      </el-row>
  </section>
</template>

<script>
  import {mapMutations} from 'vuex';
  import types from "../../store/mutation-types";
  import CardInfo from "./CardInfo";
  export default {
    components: {
      CardInfo
    },
    data() {
      return {
        visible:false,
        homeData:{},
      }
    },
    methods: {
      ...mapMutations({
        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
      clickToTask(routerType) {
        let router = "";
        if(routerType == "pass") {
          router = "/tasks/my-reviewed";
        } else if(routerType == "review") {
          router = "/tasks/my-review-waiting";
        } else if(routerType == "back") {
          router = "/tasks/my-reviewed"
        }
        this.$router.push(router);
      },
      goToAppItem(item) {
        this.$router.push({
          path: `/apps/${item.appId}/configs`,
        })
      },
      goToApp() {
        this.$router.push({
          path: `/apps`,
        })
      },
      getHomePageData() {
        this.$http.getHomePageData().then(res => {
          this.homeData = res.data.result;
          if(!res.data.result.appInfos) {
            this.homeData.appInfos = [];
          }
        }).catch(err => {
          console.log('err',err);
        })
      }
    },

    created() {
      this.getHomePageData();
      let items = [
         {
          name:"主页",
          icon:"fa fa-home",
          path:"/home",
        }
      ];
      this.setBreadcrumbItems(items);
    },
    mounted() {
    }
  }
</script>

<style scoped lang="less">
  .card-col > .el-card {
    min-height: 350px;
  }
  .card-col > .el-card /deep/ .el-card__header {
    background: #fffbe6;
  }

  .hover_click {
    color: rgba(0,0,0,.85);
    transition: all .3s;
    font-weight: 400;

  &:hover{
     cursor: pointer;
     color: #09A8FA;
   }
  }
</style>
