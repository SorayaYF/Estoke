import Vue from 'vue'
import App from './App.vue'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import 'jquery/dist/jquery.min.js'
import '@/assets/styles/global.css'

Vue.config.productionTip = false

new Vue({
    render: h => h(App),
}).$mount('#app')
