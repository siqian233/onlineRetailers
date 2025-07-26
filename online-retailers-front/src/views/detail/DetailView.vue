<template>

    <div class="detail-container">
        <NavBar>
            <template #default>
                详情
            </template>
        </NavBar>
        <div class="product-detail">
            <!-- 商品图片 -->
            <div class="product-image">
                <img :src="item.url" alt="商品图片">
            </div>

            <!-- 商品信息 -->
            <div class="product-info">
                <h2 class="product-name">{{ item.name }}</h2>
                <p class="product-price">¥{{ item.price }}</p>
                <p class="product-desc">25年新款衣装服饰25年新款衣装服饰25年新款衣装服饰25年新款衣装服饰25年新款衣装服饰</p>
            </div>

            <!-- 商品评价 -->
            <div class="product-reviews">
                <ReviewsView :showPopup="showPopup" @update:showPopup="showPopup = $event" />

                <div class="review-list">
                    <div class="review-item">
                        <div class="user-info">
                            <img src="https://via.placeholder.com/40" class="avatar">
                            <span class="username">用户1234</span>
                        </div>
                        <div class="review-content">
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
                                <span class="rating-text">4.0</span>
                            </div>
                            <p class="review-text">质量很好，穿着很舒服，下次还会购买</p>
                            <div class="review-images">
                                <img src="https://via.placeholder.com/80" alt="评价图片">
                                <img src="https://via.placeholder.com/80" alt="评价图片">
                            </div>
                            <p class="review-time">2023-05-15</p>
                        </div>
                    </div>

                    <div class="review-item">
                        <div class="user-info">
                            <img src="https://via.placeholder.com/40" class="avatar">
                            <span class="username">买家5678</span>
                        </div>
                        <div class="review-content">
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
                                    <StarFilled />
                                </el-icon>
                                <span class="rating-text">5.0</span>
                            </div>
                            <p class="review-text">非常满意，物超所值！</p>
                            <p class="review-time">2023-05-10</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 商品详情图片 -->
            <div class="product-detail-images">
                <div class="section-title">
                    <h3>商品详情</h3>
                </div>
                <img :src="item.url" alt="商品图片">
                <img :src="item.url" alt="商品图片">
                <img :src="item.url" alt="商品图片">
                <img :src="item.url" alt="商品图片">
                <img :src="item.url" alt="商品图片">
            </div>

        </div>

        <!-- 底部操作栏 -->
        <div class="action-bar">
            <div class="action-icons">
                <div class="action-item">
                    <el-icon>
                        <ChatDotRound />
                    </el-icon>
                    <span>客服</span>
                </div>
                <router-link to="/shopcart" class="cart-link">
                    <div class="action-item">
                        <el-icon>
                            <ShoppingCart />
                        </el-icon>
                        <span>购物车</span>
                    </div>
                </router-link>
            </div>
            <div class="action-buttons">
                <button class="add-cart" @click="addCart">加入购物车</button>
                <button class="buy-now">立即购买</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import NavBar from '../../components/common/NavBar.vue';
import { useRoute } from 'vue-router';
import { ChatDotRound, ShoppingCart } from '@element-plus/icons-vue';
import ReviewsView from './ReviewsView.vue';

const route = useRoute();
const item = JSON.parse(route.query.item);

const addCart = () => {
    const shopCartData = JSON.parse(localStorage.getItem('shopCartData')) || [];

    const product = shopCartData.find((product) => product.id === item.id);

    console.log(product);
    if (product) {
        product.num += 1;
    } else {
        console.log(item);
        shopCartData.push({ ...item, num: 1, checked: false });
    }

    alert('加入购物车成功');
    localStorage.setItem('shopCartData', JSON.stringify(shopCartData));
}
</script>

<style scoped>
.detail-container {
    background-color: #f7f5f5;
}

.product-content {
    padding-bottom: 20px;
}

.product-image img {
    width: 100%;
    height: auto;
    display: block;
    background-color: #fff;
}

.product-info {
    padding: 10px;
    padding-top: 5px;
    padding-bottom: 5px;
    background-color: #fff;
    margin-bottom: 5px;
    border-radius: 10px;
    border: 1px solid #f0eeee;
}

.product-name {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
    line-height: 1.4;
}

.product-price {
    color: #ff5000;
    font-size: 22px;
    font-weight: bold;
    margin: 10px 0;
}

.product-desc {
    color: #666;
    font-size: 14px;
    line-height: 1.6;
}


/* 评价区域样式 */
.product-reviews {
    background-color: #fff;
    padding: 15px;
    margin-bottom: 5px;
    border-radius: 10px;
    border: 1px solid #f0f0f0;
}

.section-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.section-title h3 {
    font-size: 16px;
    font-weight: bold;
    margin-left: 10px;
}

.more {
    color: #999;
    font-size: 12px;
}

.review-item {
    padding: 15px 0;
    border-bottom: 1px solid #f0f0f0;
}

.user-info {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    margin-right: 10px;
}

.username {
    font-size: 14px;
    color: #333;
}

.rating {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
}

.rating .el-icon {
    color: #ff9900;
    font-size: 16px;
    margin-right: 3px;
}

.rating-text {
    color: #ff9900;
    font-size: 14px;
    margin-left: 5px;
}

.review-text {
    font-size: 14px;
    color: #333;
    line-height: 1.6;
    margin-bottom: 10px;
}

.review-images {
    display: flex;
    gap: 10px;
    margin-bottom: 10px;
}

.review-images img {
    width: 80px;
    height: 80px;
    border-radius: 4px;
    object-fit: cover;
}

.review-time {
    font-size: 12px;
    color: #999;
}

/* 商品详情图片 */
.product-detail-images {
    background-color: #fff;
    padding: 0;
    border-radius: 10px;
    border: 1px solid #f0f0f0;
}

.product-detail-images img {
    width: 100%;
    display: block;
    margin-bottom: -1px;
    /* 消除图片间缝隙 */
}


/* 底部操作栏样式 */
.action-bar {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    height: 65px;
    background: #fff;
    display: flex;
    align-items: center;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
    z-index: 1000;
    padding: 0 10px;
}

.action-icons {
    display: flex;
    flex: 1;
}

.cart-link {
    text-decoration: none;
}

.action-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 60px;
    font-size: 12px;
    color: #666;
}

.action-item .el-icon {
    font-size: 20px;
    margin-bottom: 2px;
}

.action-buttons {
    display: flex;
    flex: 2;
}

.add-cart,
.buy-now {
    flex: 1;
    height: 40px;
    border: none;
    border-radius: 20px;
    color: white;
    font-size: 14px;
    font-weight: bold;
    margin-left: 10px;
}

.add-cart {
    background: linear-gradient(to right, #ffd01e, #ff8f00);
}

.buy-now {
    background: linear-gradient(to right, #ff6034, #ff1d00);
}
</style>