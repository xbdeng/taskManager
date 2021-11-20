import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../components/main-frame/Main'
import Login from '../components/login-register/Login'
import register from "../components/login-register/register";
import profile from "../components/profile/profile";
import tree from '../components/main-frame/TreeTask'
import Github from "../components/sub-components/Github";

Vue.use(VueRouter);


export default new VueRouter({
    mode: 'history',
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
            props: true
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
            path: '/profile/:username',
            component: profile,
            name: 'Profile',
            props: true
        },
        {
            path: "/oauth/redirect",
            name: "github",
            component: Github
        }
    ]
});
