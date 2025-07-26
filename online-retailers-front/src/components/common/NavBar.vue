<template>
    <div class="nav-bar">
        <div class="left" @click="goBack">
            <!-- 命名插槽：用于更新内容 -->
            <slot name="left">
                <el-icon class="icon">
                    <ArrowLeft />
                </el-icon>
            </slot>
        </div>
        <div class="center">
            <!-- 默认插槽：用于显示不同的文字，表明当前在哪个视图上 -->
            <slot />
        </div>
        <div class="right">
            <!-- 命名插槽：用于更新内容 -->
            <slot name="right" />
        </div>
    </div>
</template>

<script setup>
import { ArrowLeft } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const goBack = () => {
    router.back();
};

</script>

<style scoped>
/* 导航栏 */
.nav-bar {
    position: fixed; /* 固定在页面 */
    top: 0; /* 距离顶部 0 像素 */
    left: 0; /* 距离左侧 0 像素 */
    right: 0; /* 距离右侧 0 像素 */
    z-index: 100; /* 设置层级，确保导航栏显示在其他内容上方 */
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 0 10px;
    height: 44px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
/* 左侧图标 */
.icon {
    font-size: 20px;
}
</style>

