import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/home'    // 将根路径重定向到首页
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login/LoginView.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/category',
    name: 'category',
    component: () => import('../views/category/CategoryView.vue'),
    meta: { title: '分类' }
  },
  {
    path: '/detail',
    name: 'detail',
    component: () => import('../views/detail/DetailView.vue'),
    meta: { title: '详情' }
  },
  {
    path: '/reviews',
    name: 'reviews',
    component: () => import('../views/detail/ReviewsView.vue'),
    meta: { title: '评论' }
  },
  {
    path: '/shopcart',
    name: 'shopcart',
    component: () => import('../views/shopcart/ShopCartView.vue'),
    meta: { title: '购物车' }
  },
  {
    path: '/home',
    name: 'home',
    component: () => import('../views/home/HomeView.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/myself',
    name: 'myself',
    component: () => import('../views/my/MyselfView.vue'),
    meta: { title: '我的' }
  },
  {
    path: '/customerService',
    name: 'customerService',
    component: () => import('../views/my/CustomerServiceView.vue'),
    meta: { title: '客服反馈' }
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
});

// 路由守卫，在路由切换时修改页面标题
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  next();
});

export default router
