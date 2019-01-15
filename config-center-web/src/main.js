import 'babel-polyfill'
import Vue from "vue";
import frame from "./Frame.vue";
import router from "./router";
import store from "./store";
import Element from "element-ui";
import 'element-ui/lib/theme-chalk/index.css';
import api from "./api/index.js";
import highlight from './directives/highlight';

Vue.prototype.$http = api;
Vue.use(Element);

let notify = new Vue();
Vue.prototype.notify = notify;

new Vue({
  store,
  router,
  el: "#app",
  render: h => h(frame)
})
