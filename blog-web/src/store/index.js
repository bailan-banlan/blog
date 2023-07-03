import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)
import { setUser } from '@/utils/auth'
export default new Vuex.Store({
  state: {
    skin: 'shallow',//shallow浅色模式  //deep深色模式
    loginFlag: false,
    drawer: false,
    searchDrawer: false,
    siteAccess: 0,
    visitorAccess: 0,
    userInfoDrawer: false,
    feedbackDialogVisible: false,
    userInfo: null,
    isCommentFlag: false,
    articleDrawer: {
      flag: false,
      id: null
    },
    siteCount: {
      articleCount: 0,
      tagCount: 0,
      categoryCount: 0
    },
    webSiteInfo: {
      loginTypeList: "",
      showList: "",
    },
    noticeFlag: true,
    hotArticles: {},
  },
  mutations: {
    closeModel(state) {
      state.loginFlag = false;
    },
    isCommentFlag(state, newValue) {
      state.isCommentFlag = newValue
    },
    setWebSiteInfo(state, newValue) {
      state.webSiteInfo = newValue
    },
    setHotArticles(state, newValue) {
      state.hotArticles = newValue
    },
    setSkin(state, newValue) {
      state.skin = newValue
    },
    setDrawer(state, newValue) {
      state.drawer = newValue
    },
    setUserInfoDrawer(state, newValue) {
      state.userInfoDrawer = newValue
    },
    setSearchDrawer(state, newValue) {
      state.searchDrawer = newValue
    },
    setLoginFlag(state, newValue) {
      state.loginFlag = newValue
    },
    setSiteCount(state, newValue) {
      state.siteCount = newValue
    },
    setUserInfo(state, newValue) {
      state.userInfo = newValue
    },
    setFeedbackDialogVisible(state, newValue) {
      state.feedbackDialogVisible = newValue
    },
  },

  // actions: {
  //   setSkin(context, value) {
  //     // 修改getskin的值
  //     context.commit('getskin', value)
  //   }
  // },
})