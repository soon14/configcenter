import Vue from "vue";
import Vuex from "vuex";
import types from "./mutation-types";
import userStore from './modules/userStore';
import appStore from './modules/appStore';
import { getMenuList } from '../api/api';

Vue.use(Vuex)

const store = new Vuex.Store({
  strict: true,
  //加载单独的模块
   modules: {
    userStore,
    appStore
  },

  getters: {
    menuList: state => state.menuList,
    sidebar: state => state.sidebar,
    breadcrumbItems: state => state.breadcrumbItems,
  },
  state: {
    menuList: [],
    breadcrumbItems: [
    ],
    sidebar: {
      collapsed: false,
    },
  },
  mutations: {
    //只能同步的函数
    [types.LOAD_MENU] (state, menu) {
      state.menuList = menu.result.childrenMenu;
    },
    [types.TOGGLE_SIDEBAR] (state, collapsed) {
      if (collapsed == null) collapsed = !state.sidebar.collapsed;
      state.sidebar.collapsed = collapsed;
    },
    [types.SET_BREADCRUMB_ITEMS] (state, items) {
      state.breadcrumbItems = items;
    }
  }, 
  actions: {
    //异步的函数
    loadMenuList: ({commit}) => {
      getMenuList().then(res => {
        commit(types.LOAD_MENU, res.data);
      })
    }
  },
})

export default store
