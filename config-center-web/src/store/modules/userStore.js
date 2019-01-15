/**
 * 用户模块
 */
//import Vue from "vue";
//import Vuex from "vuex";
import types from "../mutation-types";
import Cookies from 'js-cookie';
// initial state
const userInfo = Cookies.get('userInfo');
const state = userInfo ? JSON.parse(userInfo) : '';
import {requestLogin} from "../../api/api";
import {updateUserInfo} from "../../api/api";

// getters
const getters = {
  showName: state => state.nickname || state.username,
  isAdmin: state => state.isAdmin || false,
  userInfo: state => state,
};

// actions
const actions = {
  login ({ commit, state }, payload) {
    return  requestLogin(payload).then(res => {
      commit(types.SET_USER_VO, res.data.result)
    })
  },
  update ({commit, state}, newUserInfo) {
    return updateUserInfo(newUserInfo).then(res => {
      commit(types.UPDATE_USER_VO, newUserInfo)
    })
  },
};

// mutations
const mutations = {
  [types.SET_USER_VO] (state, data) {
    Cookies.set('userInfo',JSON.stringify(data));
    Object.assign(state, data)
  },
  [types.UPDATE_USER_VO] (state, data) {
    debugger;
    state.nickname = data.nickname;
    state.email = data.email;
    state.phone = data.phone;
    Cookies.set('userInfo',JSON.stringify(data));
  }

};

export default {
  state,
  getters,
  actions,
  mutations
}
