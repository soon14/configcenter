/**
 * 应用模块
 */
import types from "../mutation-types";

const state = {
  currentAppEnv: "",
  currentAppId: "",
};
// getters
const getters = {
  currentAppEnv: state => state.currentAppEnv,
  currentAppId: state => state.currentAppId,
};

// actions
const actions = {
};

// mutations
const mutations = {
  [types.SAVE_APP_ENV] (state, data) {
    state.currentAppEnv = data;
  },
  [types.SAVE_APP_ID] (state, data) {
    state.currentAppId = data;
  }
};

export default {
  state,
  getters,
  actions,
  mutations
}
