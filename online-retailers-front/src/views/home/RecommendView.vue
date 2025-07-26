<!-- 商品推荐列表组件 -->
<template>
    <div class="recommend">
        <div v-for="(item, index) in recommendData.slice(0, 4)" :key="index" class="recommend-item">
            <a href="#">
                <img :src="item.url">
            </a>
            <div class="recommend-title">
                <p>{{ item.title }}</p>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';
defineProps(['recommendData']);
</script>

<style scoped>
.recommend {
    display: flex; /* 启用Flex布局 */
    justify-content: space-between; /* 均匀分布项目 */
    padding: 10px 0; /* 添加一些内边距 */
    overflow-x: auto; /* 如果内容过多允许横向滚动 */
    gap: 10px; /* 项目之间的间隔 */
}

.recommend-item {
    flex: 1; /* 每个项目平均分配空间 */
    min-width: 0; /* 防止内容溢出 */
    text-align: center; /* 文字居中 */
}

.recommend-img {
    width: 100%; /* 图片宽度填充容器 */
    height: auto; /* 保持图片比例 */
    max-width: 100%; /* 防止图片溢出 */
    border-radius: 8px; /* 可选：添加圆角 */
    object-fit: cover; /* 保持图片比例 */
}

.recommend-title p {
    margin-top: 8px; /* 标题与图片的间距 */
    font-size: 14px; /* 标题字体大小 */
    color: #333; /* 标题颜色 */
    white-space: nowrap; /* 防止文字换行 */
    overflow: hidden; /* 隐藏溢出文字 */
    text-overflow: ellipsis; /* 文字溢出显示省略号 */
}
</style>