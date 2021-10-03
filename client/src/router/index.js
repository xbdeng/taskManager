import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../components/Main'
import Login from '../components/Login'
import register from "../components/register";


Vue.use(VueRouter);


export default new VueRouter({
    mode:'history',
    routes: [
        //default webpage: /login
        {
            path: '*',
            redirect: '/login'
        },
        {
            path: '/main',
            component: Main,
            name: 'main'
        },
        {
            path: '/login',
            component: Login,
            name: 'login'
        },
        {
            path: '/register',
            component: register,
            name: 'register'
        }
    ]
});
