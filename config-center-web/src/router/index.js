import Vue from "vue";
import VueRouter from "vue-router";
import routeConfig from "./routes";
import Cookies from 'js-cookie';

//加载路由中间件
Vue.use(VueRouter);

//定义路由
const router = new VueRouter({
  routes: routeConfig,
  // mode: 'history'
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.auth)) {
    if(Cookies.get('userInfo')){
      next()
    } else {
      next({
        path: '/login',
        query: {redirect: to.fullPath}
      })
    }
  } else {
    next()
  }
});

export default router
