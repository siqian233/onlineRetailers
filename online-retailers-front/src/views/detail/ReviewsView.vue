<template>
    <div>
        <div class="section-title">
            <h3>商品评价</h3>
            <span class="more" @click="openPopup">查看全部 ></span>
        </div>

        <!-- 遮罩层 -->
        <transition name="fade">
            <div class="overlay" v-if="showPopup" @click="closePopup"></div>
        </transition>

        <transition name="slide-up">
            <div class="full-comment-popup" v-if="showPopup">
                <div class="popup-header">
                    <span class="title">商品评价</span>
                    <el-icon class="close-btn" @click="closePopup">
                        <Close />
                    </el-icon>
                </div>

                <!-- 弹出层筛选选项 -->
                <div class="popup-filter-tabs">
                    <div class="filter-item active">全部(356)</div>
                    <div class="filter-item">有图(128)</div>
                    <div class="filter-item">好评(320)</div>
                    <div class="filter-item">差评(36)</div>
                </div>

                <!-- 弹出层评论列表 -->
                <div class="popup-comment-list">
                    <!-- 示例评论项1 -->
                    <div v-for="(i) in 10" class="comment-item" :key="i">
                        <div class="user-info">
                            <img src="https://via.placeholder.com/40" class="avatar">
                            <div>
                                <span class="username">用户****1234</span>
                                <div class="rating">
                                    <el-icon>
                                        <StarFilled />
                                    </el-icon>
                                    <el-icon>
                                        <StarFilled />
                                    </el-icon>
                                    <el-icon>
                                        <StarFilled />
                                    </el-icon>
                                    <el-icon>
                                        <StarFilled />
                                    </el-icon>
                                    <el-icon>
                                        <Star />
                                    </el-icon>
                                </div>
                            </div>
                        </div>
                        <div class="comment-content">
                            <p class="text">衣服质量很好，穿着很舒服，和描述一致，物流也很快！</p>
                            <div class="comment-images">
                                <!-- <img src="https://via.placeholder.com/100" alt="评价图片"> -->
                                <!-- <img src="https://via.placeholder.com/100" alt="评价图片"> -->
                            </div>
                            <div class="comment-footer">
                                <span class="time">2023-05-15</span>
                                <span class="product-info">颜色：黑色 尺码：XL</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </transition>
    </div>
</template>

<script setup>
import { defineEmits, ref } from 'vue';
import { Close, StarFilled, Star } from '@element-plus/icons-vue';

const showPopup = ref(false);

// const emit = defineEmits(['update:showPopup']);
defineEmits(['update:showPopup']);

// const closePopup = () => {
//     emit('update:showPopup', false);
// };

const openPopup = () => {
  showPopup.value = true;
  document.body.style.overflow = 'hidden'; // 禁止页面滚动
};

const closePopup = () => {
  showPopup.value = false;
  document.body.style.overflow = ''; // 恢复页面滚动
};
</script>

<style scoped>
.section-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.section-title h3 {
    font-size: 16px;
    font-weight: bold;
}

.more {
    color: #999;
    font-size: 12px;
    cursor: pointer;
}

/* 新增遮罩层样式 */
.overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 99; /* 低于弹窗但高于页面内容 */
}

/* 遮罩层动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 评论区顶部筛选栏 */
.filter-tabs {
    display: flex;
    align-items: center;
    padding: 12px 15px;
    background: #fff;
    border-bottom: 1px solid #f5f5f5;
    position: sticky;
    top: 0;
    z-index: 10;
}

.filter-item {
    padding: 5px 12px;
    margin-right: 8px;
    font-size: 13px;
    color: #666;
    border-radius: 15px;
    background: #f5f5f5;
    cursor: pointer;
}

.filter-item.active {
    background: #ff5000;
    color: #fff;
}

.view-all {
    margin-left: auto;
    font-size: 13px;
    color: #ff5000;
    cursor: pointer;
}

/* 底部弹出层 */
.full-comment-popup {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 85vh;
    background: #fff;
    border-radius: 16px 16px 0 0;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
    z-index: 100;
    overflow-y: auto;
    padding-bottom: env(safe-area-inset-bottom);
}

/* 增强弹出动画效果 */
.slide-up-enter-active {
    transition: all 0.3s ease-out;
}

.slide-up-leave-active {
    transition: all 0.3s ease-in;
}

.slide-up-enter-from {
    transform: translateY(100%);
    opacity: 0;
}

.slide-up-leave-to {
    transform: translateY(100%);
    opacity: 0;
}

.popup-header {
    position: sticky;
    top: 0;
    background: #fff;
    padding: 15px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-bottom: 1px solid #f5f5f5;
    z-index: 10;
}

.popup-header .title {
    font-size: 16px;
    font-weight: bold;
}

.close-btn {
    position: absolute;
    right: 15px;
    font-size: 20px;
    color: #999;
}

.popup-filter-tabs {
    display: flex;
    padding: 12px 15px;
    background: #fff;
    border-bottom: 1px solid #f5f5f5;
    position: sticky;
    top: 50px;
    z-index: 9;
    overflow-x: auto;
    white-space: nowrap;
}

.popup-filter-tabs .filter-item {
    flex-shrink: 0;
}

.popup-comment-list {
    padding: 0 15px 20px;
}

/* 评论项样式 */
.comment-item {
    padding: 15px 0;
    border-bottom: 1px solid #f5f5f5;
}

.user-info {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    margin-right: 10px;
    background: #f5f5f5;
}

.username {
    font-size: 13px;
    color: #333;
}

.rating {
    margin-top: 3px;
}

.rating .el-icon {
    color: #ff9900;
    font-size: 14px;
}

.comment-content {
    margin-left: 46px;
}

.text {
    font-size: 14px;
    color: #333;
    line-height: 1.6;
    margin-bottom: 10px;
}

.comment-images {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 10px;
}

.comment-images img {
    width: 100px;
    height: 100px;
    border-radius: 4px;
    object-fit: cover;
}

.comment-footer {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: #999;
}

/* 弹出动画 */
/* .slide-up-enter-active,
.slide-up-leave-active {
    transition: transform 0.3s ease;
}

.slide-up-enter-from,
.slide-up-leave-to {
    transform: translateY(100%);
} */
</style>