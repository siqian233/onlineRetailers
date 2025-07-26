import { createRouter, createWebHashHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    redirect: '/home'    // 将根路径重定向到首页
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login/LoginView.vue'),
    meta: { title: '登录' } // 添加页面标题
  },
  {
    path: '/category',
    name: 'category',
    component: () => import('../views/category/CategoryView.vue'),
    meta: { title: '分类' } // 添加页面标题
  },
  {
    path: '/detail',
    name: 'detail',
    component: () => import('../views/detail/DetailView.vue'),
    meta: { title: '详情' } // 添加页面标题
  },
  {
    path: '/reviews',
    name: 'reviews',
    component: () => import('../views/detail/ReviewsView.vue'),
    meta: { title: '评论' } // 添加页面标题
  },
  {
    path: '/shopcart',
    name: 'shopcart',
    component: () => import('../views/shopcart/ShopCartView.vue'),
    meta: { title: '购物车' } // 添加页面标题
  },
  {
    path: '/home',
    name: 'home',
    component: () => import('../views/home/HomeView.vue'),
    meta: { title: '首页' } // 添加页面标题
  },
  {
    path: '/myself',
    name: 'myself',
    component: () => import('../views/my/MyselfView.vue'),
    meta: { title: '我的' } // 添加页面标题
  },
  {
    path: '/customerService',
    name: 'customerService',
    component: () => import('../views/my/CustomerServiceView.vue'),
    meta: { title: '客服反馈' } // 添加页面标题
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
