import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../components/Main'
import Login from '../components/Login'
import register from "../components/register";
import profile from "../components/profile";
import profileInfo from "../components/profileInfo"
import ChangePasswd from "../components/ChangePasswd"



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
        },
        {
            path: '/profile',
            component: profile,
            name: 'profile',
            children:[
                {
                path: '/profile/Info',
                component: profileInfo,
                name: 'profileInfo'
            },
                {
                    path: '/profile/ChangePasswd',
                    component: ChangePasswd,
                    name: 'ChangePasswd'
                },
            ]
        },
    ]
});
