<template>
  <section>
    <el-row style="background: #f2f2f2;">
      <el-form :inline="true" :model="conditions" style="padding-left: 20px;padding-top: 20px">
        <el-form-item label="应用名称">
          <el-input v-model="conditions.appName" placeholder="请输入应用名称" size="mini" style="width: 250px"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit" size="mini">查询</el-button>
        </el-form-item>
      </el-form>
    </el-row>

    <el-row :gutter="20" v-loading="appLoading" element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading">
      <el-col :span="6" style="textAlign:center">
        <div class="app" style="background: rgb(49, 180, 141)" @click="addApp">
            <span class="app-title" style="lineHeight:150px; fontSize: 100px;">+</span>
        </div>
      </el-col>
      <el-col :span="6" v-for="item in apps" :key="item.id">
        <div class="app"
             style="background: rgb(59, 103, 164)"
             @click="showOperateInfo(item)"
        >
          <el-row>
            <el-dropdown>
                <span class="setting" @click.stop="doNothing">
                  <i class="el-icon-edit-outline"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="operateApp('update',item)">修改应用</el-dropdown-item>
                  <el-dropdown-item @click.native="operateApp('delete',item)">删除应用</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>

            <span class="config" @click.stop="showAppInfo(item)" title='点击设置应用的基本信息'>
              <i class="fa fa-cog"></i>
            </span>
          </el-row>
          <el-tooltip :content="item.description" placement="top">
            <h2 class="app-title">{{item.appName}}</h2>
          </el-tooltip>
          <!-- 创建时间 -->
          <div class='description-wrapper' style="bottom:30px;">
              <span class='info'>{{timestampToENDate1(item.updateTime)}}</span>
          </div>
          <!-- 创建人名 -->
          <div class='description-wrapper'>
              <span class='info'>{{item.creatorName}}</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-dialog :title="title" :visible.sync="addAppVisible" width="60%" :close-on-click-modal="false">
      <el-form :model="appInfo" label-width="120px" ref="appInfo" :rules="rules">
        <el-form-item label="应用名称" prop="appName">
            <el-input v-model="appInfo.appName" :disabled="disabled" style="width: 500px"></el-input>
            <el-button type="primary" v-if="title=='修改应用'" @click="disabled = false">修改</el-button>
        </el-form-item>
        <el-form-item label="应用描述" prop="description">
          <el-input type="textarea" v-model="appInfo.description" style="width: 500px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel('appInfo')">取消</el-button>
        <el-button @click="handleSubmit('appInfo')" type="primary">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
  import {mapGetters,mapMutations} from 'vuex';
  import {CHECK_APP_NAME} from '../../common/regexp';
  import {timestampToENDate} from "../../common/date";
  import types from "../../store/mutation-types";
  export default {
    data() {
      var validateDesc = (rule, value, callback) => {
            if (value.length > 1000) {
              callback(new Error('描述信息不能超过1000个字符'));
            } else {
              callback();
            }
      };
      return {
        conditions: {
          appName:"",
        },
        disabled:false,
        appLoading:false,
        addAppVisible:false,
        title:"新增应用",
        appInfo: {
          appId:"",
          appName:"",
          description:"",
        },
        rules: {
          appName: [
            {required: true, message: '应用的名称不能为空', trigger: 'blur' },
            {pattern: CHECK_APP_NAME, message:'应用的名称应该在5-64个英文字符之间，只能由英文字母和数字加上下划线组成，首字母只能为字母', trigger: ['blur', 'change']}
          ],
          description:[
            { validator: validateDesc, trigger: 'blur' }
          ]
        },

        apps: [

        ],
      };
    },
    computed: {
      ...mapGetters({
        userInfo: 'userInfo',
      }),
    },
    methods: {
      timestampToENDate1(value) {
        return timestampToENDate(value)
      },
      addApp() {
        this.addAppVisible = true;
        this.title='新增应用';
        this.disabled=false;
        this.appInfo.appId = "";
        this.appInfo.appName = "";
        this.appInfo.description = "";
      },
      getApps() {
        this.appLoading = true;
        this.$http.getMyApps({appName:this.conditions.appName}).then(res => {
          this.apps = res.data.result;
          this.appLoading = false;
        }).catch(err => {
          this.appLoading = false;
          console.log('err',err);
        })
      },
      onSubmit() {
        this.getApps();
      },
      operateApp(type,data) {
        if(type === "delete") {
            this.$confirm('确定要删除应用吗？','提示',{
              type: "warning",
            }).then(() => {
              this.$http.deleteApp({appId:data.id}).then(res => {
                this.$message({
                  message: res.data.result,
                  type: 'success',
                });
                this.getApps();
              })
              }).catch(() => {
            });
        } else if(type === "update") {
            this.addAppVisible = true;
            this.disabled = true;
            this.title = "修改应用";
            this.appInfo.appId = data.id;
            this.appInfo.appName = data.appName;
            this.appInfo.description = data.description;
        }
      },

      handleCancel(formName) {
        this.$refs[formName].resetFields();
        this.addAppVisible = false;
      },

      handleSubmit(formName) {
        const data = this.appInfo;
        this.$refs[formName].validate((valid) => {
          if(valid) {
            if(!data.appId) {
              this.$http.createApp(data).then(res => {
              this.addAppVisible = false;
              this.getApps();
              this.$message({
                message: res.data.result,
                type: 'success',
              });
            }).catch(err => {
                console.log(err);
            })
          } else {
              this.$http.updateApp(data).then(res => {
                this.addAppVisible = false;
                this.getApps();
                this.$message({
                   message: res.data.result,
                   type: 'success',
                })
              }).catch(err => {
                console.log(err);
              })
          }
          }
        })


      },
      showAppInfo(item) {
        this.$router.push({
          path: `/apps/${item.id}/envs`,
        })
      },
      showOperateInfo(item) {

        this.$router.push({
           path: `/apps/${item.id}/configs`,
        });
        this.addRecentApp(item.id);
      },
      doNothing() {

      },
      ...mapMutations({
        setBreadcrumbItems: types.SET_BREADCRUMB_ITEMS,
      }),
      addRecentApp(appId) {
        this.$http.addRecentApp({appId:appId}).then(res => {

        }).catch(err => {
          console.log('err',err);
        })
      }
    },
    created() {
      let itmes = [
          {
            name:"应用管理",
            icon:"fa fa-windows",
            path:"/apps",
          },
      ];
      this.setBreadcrumbItems(itmes);
    },
    mounted() {
      this.getApps();
    },
  }
</script>

<style scoped lang="less">

  .app {
    position: relative;
    height:160px;
    -webkit-user-select:none;
    -moz-user-select:none;
    -ms-user-select:none;
    user-select:none;
    margin-top: 20px;
    border-radius: 4px;
    color: #fff;
    box-shadow: 4px 4px 4px rgba(0,21,41,.35);

    &:hover{
      background: rgb(117, 56, 199) !important;
      box-shadow: 0px 0px 20px 1px rgb(117, 56, 199);
    }
    &:active{
      background: #048DBD !important;
    }

    h2{
      margin-bottom: 0;
      color: #fff;
    }
}

.app-title {
  line-height: 30px;
  font-size: 20px;
  text-align: center;
  padding: 0 10px;
  margin: 24px 0;

  overflow: hidden;
  white-space:nowrap;
  text-overflow:ellipsis;
}

.description-wrapper{
  width: 100%;
  padding: 0 4px 0 10px;
  position: absolute;
  bottom: 10px;
  height: 12px;
  line-height: 12px;
  overflow: hidden;
  white-space:nowrap;
  text-overflow:ellipsis;

  .info{
    color: rgb(255, 255, 255);
    font-size: 12px;
    font-weight: 400;
    opacity: 0.8;
  }
}

.setting {
  float: right;
  margin-top: 10px;
  margin-left: 10px;
  font-size: 25px;
  color:#fff;
  /*position: absolute;*/
  opacity: 0.8;

  //旋转1
  transition: All 0.4s ease-in-out;
  -webkit-transition: All 0.4s ease-in-out;
  -moz-transition: All 0.4s ease-in-out;
  -o-transition: All 0.4s ease-in-out;

  &:hover {
    //旋转2
    transform: rotate(360deg) scale(1.2);
    -webkit-transform: rotate(360deg) scale(1.2);
    -moz-transform: rotate(360deg) scale(1.2);
    -o-transform: rotate(360deg) scale(1.2);
    -ms-transform: rotate(360deg) scale(1.2);
  }

  &：active {
    color: #666666;
  }
}

.config {
  cursor: pointer;
  float: right;
  margin: 10px 10px 0 0;
  font-size: 25px;
  //position: absolute;
  opacity: 0.8;

  //旋转1
  transition: All 0.4s ease-in-out;
  -webkit-transition: All 0.4s ease-in-out;
  -moz-transition: All 0.4s ease-in-out;
  -o-transition: All 0.4s ease-in-out;

  &:hover {
    color: #12fffd;
    //旋转2
    transform: rotate(360deg) scale(1.2);
    -webkit-transform: rotate(360deg) scale(1.2);
    -moz-transform: rotate(360deg) scale(1.2);
    -o-transform: rotate(360deg) scale(1.2);
    -ms-transform: rotate(360deg) scale(1.2);
  }

  &：active {
    color: #666666;
  }
}
</style>
