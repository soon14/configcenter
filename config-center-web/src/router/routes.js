import home from "../pages/home/Home.vue";
import NotFoundView from "../components/NotFound.vue";
import app from "../App.vue";
import application from "../pages/app/Index.vue"
import login from "../pages/system/Login.vue";
import register from "../pages/system/Register.vue";
import MySubmit from "../pages/task/MySubmit.vue";
import myReviewedWaiting from "../pages/task/MyReviewWaiting.vue";
import myReviewed from "../pages/task/MyReviewed.vue";
import userInfo from "../pages/system/UserInfo.vue";
import userManagement from "../pages/system/UserManagements.vue";
import CleanCache from "../pages/system/CleanCache.vue";
import ResetPassword from "../pages/system/ResetPassword.vue";
import AppInfo from "../pages/app/AppInfo.vue";
import Envs from "../pages/app/Envs.vue";
import Members from "../pages/app/Members.vue";
import OperateInfo from "../pages/app/OperateInfo.vue";
import Instances from "../pages/app/Instances.vue";
import middleRouter from "../pages/system/MiddleRouter.vue";
import Configs from "../pages/config/Configs.vue";
import ShowStrategy from "../pages/strategy/ShowStrategy";
import UserRelation from "../pages/organization/UserRelation";
import ShowApprove from "../pages/approve/ShowApprove";
// Routes
const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    component: login
  },
  {
    path: '/register',
    component: register
  },
  {
    path: '/resetPass',
    component: ResetPassword
  },
  {
    path: '',
    component: app,
    meta: {auth: true},
    children: [
      {path: '/home', component: home, name: '首页'},
      {path: '/apps', component: application, name: '应用'},
      {
        path: '',
        component:AppInfo,
        meta: {auth: true},
        children:[
          {path: '/apps/:appId/envs',component:Envs, name: '环境'},
          {path: '/apps/:appId/members',component:Members, name: '人员列表'},
          {path: '/apps/:appId/approves',component:ShowApprove, name: '审核策略'}
        ]
      },
      {
        path: '',
        component:OperateInfo,
        meta: {auth: true},
        children:[
          {path: '/apps/:appId/instances',component:Instances,name: '实例列表'},
          {path: '/apps/:appId/configs',component:Configs,name: '配置管理'},
          {path: '/apps/:appId/strategy',component:ShowStrategy,name: '刷新策略'}
        ]
      },
      {path: '/tasks/my-submit', component:MySubmit, name: '我创建的任务'},
      {path: '/tasks/my-review-waiting', component:myReviewedWaiting, name: '待审批的任务'},
      {path: '/tasks/my-reviewed', component:myReviewed, name: '审核完成的任务'},
      {path: '/user-info', component:userInfo, name:'个人信息'},
      {path: '/user-manage', component:userManagement, name:'用户管理'},
      //路由跳转的中间组件
      {path: '/middle-router',component:middleRouter, name:'路由跳转'},
      {path: '/clean-cache', component:CleanCache, name:'清除缓存'},
      {path: '/user-relation', component:UserRelation, name:'人员组织关系'}
    ]
  },
  {path: '*', component: NotFoundView}
]


export default routes
