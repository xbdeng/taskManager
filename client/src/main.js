// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import md5 from 'js-md5'
import animated from 'animate.css'
import 'element-ui/lib/theme-chalk/index.css'
import 'lib-flexible/flexible'
import ws from '../src/components/sub-components/ws'
import './assets/font_2960825_ecarnxsimlj/iconfont.css'
Vue.use(ElementUI);
Vue.use(animated)
Vue.config.productionTip = false
Vue.prototype.$md5 = md5
Vue.prototype.ws = ws

/* eslint-disable no-new */
new Vue({
    el: '#app',
    components: { App },
    template: '<App/>'
})
