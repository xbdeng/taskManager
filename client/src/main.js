// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from 'element-ui'
import md5 from 'js-md5'
import animated from 'animate.css'
import 'element-ui/lib/theme-chalk/index.css'
import 'lib-flexible/flexible'

Vue.use(ElementUI);
Vue.use(animated)
Vue.config.productionTip = false
Vue.prototype.$md5 = md5

/* eslint-disable no-new */
new Vue({
    el: '#app',
    components: { App },
    template: '<App/>'
})
