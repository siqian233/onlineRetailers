<!-- 商品列表组件 -->
<template>
    <div class="goods-container">
        <div v-for="(item, index) in goodsData" :key="index" class="goods-item">
            <router-link :to="{ name: 'detail', query: { item: JSON.stringify(item) } }">
                <div class="goods-link"><img :src="item.url" :alt="item.name" class="goods-image"></div>
            </router-link>
            <p class="goods-name">{{ item.name }}</p>
            <p class="goods-price">￥{{ item.price }}</p>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';
defineProps(['goodsData']);
</script>

<style scoped>
/* 商品容器 - 网格布局 */
.goods-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 每行两列 */
  gap: 16px; /* 商品间距 */
  padding: 12px; /* 容器内边距 */
}

/* 单个商品项样式 */
.goods-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s;
}

.goods-item:hover {
  transform: translateY(-5px);
}

/* 商品链接 */
.goods-link {
  display: block;
  text-decoration: none;
  color: #333;
}

/* 商品图片 */
.goods-image {
  width: 100%;
  height: 160px;
  object-fit: cover;
  display: block;
}

/* 商品名称 */
.goods-name {
  margin: 8px 12px;
  font-size: 14px;
  line-height: 1.4;
  display: -webkit-box;
  line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 商品价格 */
.goods-price {
  margin: 0 12px 12px;
  color: #ff5000;
  font-size: 16px;
  font-weight: bold;
}
</style>
