<template>
    <div class="category-container">
        <NavBar>
            <template #default>
                分类
            </template>
        </NavBar>

        <div class="category-content">
            <!-- 左侧分类导航 -->
            <div class="category-sidebar">
                <div v-for="(item, index) in categories" :key="index" class="sidebar-item"
                    :class="{ active: activeIndex === index }" @click="changeCategory(index)">
                    {{ item.name }}
                </div>
            </div>

            <!-- 右侧商品展示 -->
            <div class="category-main">
                <div class="subcategories">
                    <!-- 使用Element Plus图标 -->
                    <div v-for="(sub, subIndex) in currentSubcategories" :key="subIndex" class="subcategory-item">
                        <div class="subcategory-icon">
                            <el-icon :size="36" :color="sub.color">
                                <component :is="sub.icon" />
                            </el-icon>
                        </div>
                        <div class="subcategory-name">{{ sub.name }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import NavBar from '../../components/common/NavBar.vue';
import { ref, computed } from 'vue';
import {
    Star, // 推荐
    Goods, // 商品
    Present, // 礼品
} from '@element-plus/icons-vue';

// 分类数据（使用Element Plus图标）
const categories = ref([
    {
        name: '手机',
        subcategories: [
            { name: '热门推荐', icon: Star, color: '#ff9900' },
            { name: '新品上市', icon: Goods, color: '#ff5000' },
            { name: '限时特惠', icon: Present, color: '#f56c6c' },
            { name: '爆款榜单', icon: Star, color: '#e6a23c' },
            { name: '精选套装', icon: Goods, color: '#67c23a' },
            { name: '会员专享', icon: Present, color: '#409eff' }
        ]
    },
    {
        name: '平板',
        subcategories: [
            { name: '热门推荐', icon: Star, color: '#ff9900' },
            { name: '新品上市', icon: Goods, color: '#ff5000' },
            { name: '限时特惠', icon: Present, color: '#f56c6c' },
            { name: '爆款榜单', icon: Star, color: '#e6a23c' },
            { name: '精选套装', icon: Goods, color: '#67c23a' },
            { name: '会员专享', icon: Present, color: '#409eff' }
        ]
    },
    {
        name: '笔记本电脑',
        subcategories: [
            { name: '热门推荐', icon: Star, color: '#ff9900' },
            { name: '新品上市', icon: Goods, color: '#ff5000' },
            { name: '限时特惠', icon: Present, color: '#f56c6c' },
            { name: '爆款榜单', icon: Star, color: '#e6a23c' },
            { name: '精选套装', icon: Goods, color: '#67c23a' },
            { name: '会员专享', icon: Present, color: '#409eff' }
        ]
    },
    {
        name: '智能穿戴',
        subcategories: [
            { name: '热门推荐', icon: Star, color: '#ff9900' },
            { name: '新品上市', icon: Goods, color: '#ff5000' },
            { name: '限时特惠', icon: Present, color: '#f56c6c' },
            { name: '爆款榜单', icon: Star, color: '#e6a23c' },
            { name: '精选套装', icon: Goods, color: '#67c23a' },
            { name: '会员专享', icon: Present, color: '#409eff' }
        ]
    },
    {
        name: '电视',
        subcategories: [
            { name: '热门推荐', icon: Star, color: '#ff9900' },
            { name: '新品上市', icon: Goods, color: '#ff5000' },
            { name: '限时特惠', icon: Present, color: '#f56c6c' },
            { name: '爆款榜单', icon: Star, color: '#e6a23c' },
            { name: '精选套装', icon: Goods, color: '#67c23a' },
            { name: '会员专享', icon: Present, color: '#409eff' }
        ]
    }
]);

const activeIndex = ref(0);

// 当前选中的子分类
const currentSubcategories = computed(() => {
    return categories.value[activeIndex.value].subcategories;
});

const changeCategory = (index) => {
    activeIndex.value = index;
};
</script>

<style scoped>
.category-container {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: #fff;
}

.category-content {
    flex: 1;
    display: flex;
    overflow: hidden;
}

/* 左侧分类导航 */
.category-sidebar {
    width: 90px;
    background-color: #f8f8f8;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
}

.sidebar-item {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
    border-left: 3px solid transparent;
    transition: all 0.2s;
}

.sidebar-item.active {
    background-color: #fff;
    color: #ff5000;
    border-left: 3px solid #ff5000;
    font-weight: bold;
}

/* 右侧内容区域 */
.category-main {
    flex: 1;
    padding: 10px;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
}

.banner {
    margin-bottom: 15px;
    height: 100px;
    border-radius: 8px;
    overflow: hidden;
}

.subcategories {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 15px;
}

.subcategory-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 12px 0;
    background-color: #f9f9f9;
    border-radius: 8px;
    transition: transform 0.2s;
}

.subcategory-item:hover {
    transform: translateY(-3px);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.subcategory-icon {
    width: 40px;
    height: 40px;
    margin-bottom: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.subcategory-name {
    font-size: 12px;
    color: #333;
    text-align: center;
}

.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
}
</style>