import Vue from 'vue'
import Router from 'vue-router'

const options = [
  { path: '/', component: 'HelloWorld' }
]

const routes = options.map(route => {
  return {
    ...route,
    component: () => import(`@/components/${route.component}.vue`)
  }
})

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes
})
