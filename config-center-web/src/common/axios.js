import axios from "axios";
//import qs from "qs";
import { Notification } from 'element-ui';
import router from "../router";

// axios 配置
axios.defaults.timeout = 60000;
axios.defaults.withCredentials = true;

//http request 拦截器
axios.interceptors.request.use((config) => {
  return config;
}, error => {
    return Promise.reject(error);
});

axios.interceptors.response.use(response => {
  if(response && response.data) {
    if(!response.data.success) {
      if(response.data.errorCode === '401') {
        Notification({
          type:"error",
          message:"会话过期，系统即将跳转到登录界面",
          duration:2000,
          onClose: ()=> {
            sessionStorage.clear();
            router.push('/login');
          }
        });
        reject(new Error("会话过期"));
      } else {
        Notification({
          type:"error",
          message:response.data.errorMsg,
          duration:5000
        });
        reject(new Error(response.data.errorMsg));
      }
    } else {
      return response;
    }
  }
}, error => {
    if(error.response) {
      Notification({type:"error",message:"网络异常"});
    }
    return Promise.reject(error)
});

export default axios
