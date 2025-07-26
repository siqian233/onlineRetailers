<template>
  <router-view />

  <div class="mobile-tabbar">
    <router-link
      to="/home"
      class="tab-item"
      active-class="active"
    >
      <el-icon class="tab-icon"><HomeFilled /></el-icon>
      <span class="tab-text">首页</span>
    </router-link>
    <router-link
      to="/category"
      class="tab-item"
      active-class="active"
    >
      <el-icon class="tab-icon"><List /></el-icon>
      <span class="tab-text">分类</span>
    </router-link>
    <router-link
      to="/shopcart"
      class="tab-item"
      active-class="active"
    >
      <el-icon class="tab-icon"><ShoppingCart /></el-icon>
      <span class="tab-text">购物车</span>
    </router-link>
    <router-link
      to="/myself"
      class="tab-item"
      active-class="active"
    >
      <el-icon class="tab-icon"><User /></el-icon>
      <span class="tab-text">我的</span>
    </router-link>
  </div>
</template>

<script setup>
// 导入需要的Element Plus图标（按需引入）
import { HomeFilled, List, ShoppingCart, User } from '@element-plus/icons-vue'
</script>

<style scoped>
/* 基础移动端布局 */
.mobile-tabbar {
  position: fixed;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 60px; /* 稍微增高以适应图标 */
  display: flex;
  background: white;
  border-top: 1px solid #f0f0f0;
  z-index: 100;
  -webkit-tap-highlight-color: transparent;
}

.tab-item {
  flex: 1;
  display: flex;
  flex-direction: column; /* 改为垂直布局 */
  align-items: center;
  justify-content: center;
  color: #666;
  text-decoration: none;
  font-size: 12px;
  padding: 5px 0;
}

.tab-icon {
  font-size: 20px; /* 图标大小 */
  margin-bottom: 3px; /* 图标和文字的间距 */
}

.tab-text {
  font-size: 12px;
}

/* 激活状态样式 */
.tab-item.active {
  color: #ff5000;
}
.tab-item.active .tab-icon {
  color: #ff5000; /* 激活时图标也变红色 */
}

/* 防止内容被遮挡 */
body {
  padding-bottom: 60px; /* 对应tabbar高度 */
}
</style>