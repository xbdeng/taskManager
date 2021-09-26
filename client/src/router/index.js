import Vue from 'vue'
import VueRouter from 'vue-router'
//这里要导入路由所需要的所有的组件
import Main from '../components/Main'

//安装路由
Vue.use(VueRouter);

//配置路由信息
export default new VueRouter({
    routes:[
        {
            //路由路径
            path:'/main',
            //跳转的组件
            component:Main,
            //路由的名字
            name:'main'
        }
    ]
});
