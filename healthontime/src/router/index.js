import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'login',
    component: () => import("../views/Login.vue")
  },
  {
    path: '/Register',
    name: 'register',
    component: () => import("../views/Register.vue")
  },
  {
    path:'/Index',
    name:'index',
    component:()=>import('../views/Index.vue'),
    children:[
      {
        path:'/Home',
        name:'home',
        meta:{
          title:'首页'
        },
        component:()=>import('../views/Home.vue')
      },
      {
        path: '/CheckInDatail',
        name: 'checkInDatail',
        component: () => import("../views/CheckInDatail.vue")
      },
      ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})
export function resetRouter() {
  router.matcher = new VueRouter({
    mode:'history',
    routes: []
  }).matcher
}
const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
  return VueRouterPush.call(this, to).catch(err => err)
}
export default router
