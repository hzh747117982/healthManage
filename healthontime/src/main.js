import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import echarts from 'echarts'
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";
import VueRouter from "vue-router";
Vue.config.productionTip = false
Vue.prototype.$axios = axios
Vue.use(VueRouter)
Vue.use(ElementUI)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
