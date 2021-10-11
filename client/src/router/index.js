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
            path: '/main/:username',
            component: Main,
            name: 'Main',
            props:true
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
