import Vue from 'vue'
import App from './App.vue'
import config from "./assets/js/config";
import './assets/font/iconfont.css'
import './assets/font/iconfont.js'
import store from './store'
import animated from 'animate.css'
import 'wowjs/css/libs/animate.css'
import wow from 'wowjs'

import VMdPreview from '@kangc/v-md-editor/lib/preview';
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';

import "../src/icons";
import { vueBaberrage } from 'vue-baberrage'
import { setSkin } from '@/utils/skin'
import jsCookie from 'js-cookie'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import VueImageSwipe from 'vue-image-swipe'
import 'vue-image-swipe/dist/vue-image-swipe.css'

import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

import Prism from 'prismjs';
import Clipboard from 'clipboard'

Vue.prototype.Clipboard = Clipboard
VueMarkdownEditor.use(vuepressTheme, {
  Prism,
});

Vue.use(VueMarkdownEditor);
VMdPreview.use(vuepressTheme, {
  Prism
});
VMdPreview.use(createLineNumbertPlugin())
VMdPreview.use(createCopyCodePlugin())
Vue.use(VMdPreview);

Vue.use(VueImageSwipe);
Vue.use(ElementUI);
Vue.prototype.$cookie = jsCookie;  // 在页面里可直接用 this.$cookie 调用
Vue.prototype.$setSkin = setSkin;
Vue.use(vueBaberrage)
Vue.prototype.$wow = wow
Vue.config.productionTip = false
Vue.prototype.config = config;
import router from './router'

new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})
router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: "instant"
  });
});


