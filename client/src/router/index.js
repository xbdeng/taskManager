import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../components/main-frame/Main'
import Login from '../components/login-register/Login'
import register from "../components/login-register/register";
import profile from "../components/profile/profile";
import tree from '../components/main-frame/TreeTask'
import Github from "../components/sub-components/Github";
import GithubBind from "../components/sub-components/GithubBind";
import Google from "../components/sub-components/Google";
import GoogleBind from "../components/sub-components/GoogleBind";

Vue.use(VueRouter);


const router =  new VueRouter({
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
        },
        {
            path: "/oauth/bind",
            name: "githubBind",
            component: GithubBind
        },
        {
            path: "/google/login",
            name: "google",
            component: Google
        },
        {
            path: "/google/bind",
            name: "googleBind",
            component: GoogleBind
        }
    ],

});

router.beforeEach((to, from, next) => {
    let token = window.sessionStorage.getItem('token')
    if(token === null) {
        if(to.name === 'login' || to.name === 'register' || to.name === 'github' || to.name === 'githubBind' || to.name === 'google' || to.name === 'googlebind') {
            next()
        } else {
            next(`/login`)
        }
    } else {
        next()
    }
    next()
})
export default router;



