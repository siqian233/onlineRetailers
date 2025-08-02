<!-- 轮播图组件 -->
<template>
    <div class="banner-container">
        <el-carousel :interval="3000" height="200px">
            <el-carousel-item v-for="(item, index) in bannerData" :key="index">
                <h3 onclick="">
                    <el-image :src="item.imageUrl" fit="fill"></el-image>
                </h3>
            </el-carousel-item>
        </el-carousel>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';
defineProps(['bannerData']);
</script>

<style scoped>
.banner-container{
  width: 100%;
  height: 200px;
}
</style>