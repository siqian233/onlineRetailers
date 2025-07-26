<template>
    <div class="taobao-cart">
        <div v-for="(item, index) in localShopCartData" :key="index" class="cart-item">
            <div class="check-box" @click="toggleCheck(index)">
                <div class="check-circle" :class="{ checked: item.checked }">
                    <el-icon v-if="item.checked" class="check-icon">
                        <Check />
                    </el-icon>
                </div>
            </div>
            <!-- 左侧图片 -->
            <div class="item-img">
                <img :src="item.url" :alt="item.name">
            </div>

            <!-- 中间商品信息 -->
            <div class="item-info">
                <h3 class="item-title">{{ item.name }}</h3>
                <p class="price">￥{{ item.price }}</p>
            </div>

            <!-- 右边操作 -->
            <div class="quantity-control">
                <div class="quantity-buttons">
                    <button class="round-btn minus" @click="updateQuantity(index, -1)"
                        :disabled="item.num <= 1">-</button>
                    <span class="quantity">{{ item.num }}</span>
                    <button class="round-btn plus" @click="updateQuantity(index, 1)">+</button>
                </div>
                <div class="delete-btn" @click="deleteItem(index)">
                    <el-icon>
                        <Delete />
                    </el-icon>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits, ref, watch, onMounted } from 'vue';
import {Check, Delete} from '@element-plus/icons-vue';

const props = defineProps(['shopCartData']);
const emit = defineEmits(['changeShopCartData', 'changeNumber', 'deleteProduct']);

// 确保即使 props.shopCartData 为 null 或 undefined 也能正常工作
const localShopCartData = ref([]);

// 在组件挂载后初始化数据
onMounted(() => {
    // 确保 props.shopCartData 是数组，如果不是则使用空数组
    localShopCartData.value = Array.isArray(props.shopCartData) ? [...props.shopCartData] : [];
});

// 监听 props.shopCartData 的变化
watch(() => props.shopCartData, (newVal) => {
    localShopCartData.value = Array.isArray(newVal) ? [...newVal] : [];
}, { immediate: true });

// 监听数据变化自动保存
watch(localShopCartData, (newVal) => {
    localStorage.setItem('shopCartData', JSON.stringify(newVal));
}, { deep: true });

const toggleCheck = (index) => {
    localShopCartData.value[index].checked = !localShopCartData.value[index].checked;
    emit('changeShopCartData', index, localShopCartData.value[index].checked);
}

const updateQuantity = (index, sign) => {
    localShopCartData.value[index].num += sign;
    emit('changeNumber', index, sign, localShopCartData.value[index].num);
}

const deleteItem = (index) => {
    localShopCartData.value.splice(index, 1);
    emit('deleteProduct', index);
}
</script>

<style scoped>
.taobao-cart {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    margin-bottom: 60px;
}

.cart-item {
    display: flex;
    padding: 15px;
    border-bottom: 1px solid #f5f5f5;
    align-items: center;
    position: relative;
}

/* 圆形选择按钮 */
.check-box {
    margin-right: 12px;
    cursor: pointer;
}

.check-circle {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    border: 1px solid #ddd;
    display: flex;
    align-items: center;
    justify-content: center;
    /* transition: all 0.2s; */
}

.check-circle.checked {
    background-color: #ff5000;
    border-color: #ff5000;
}

.check-icon {
    color: white;
    font-size: 12px;
    font-weight: bold;
}

/* 左侧图片 */
.item-img {
    width: 80px;
    height: 80px;
    margin-right: 15px;
    flex-shrink: 0;
}

.item-img img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 4px;
}

/* 中间商品信息 */
.item-info {
    flex: 1;
    min-width: 0;
    padding-right: 15px;
}

.item-title {
    margin: 0 0 8px 0;
    font-size: 14px;
    color: #333;
    font-weight: normal;
    line-height: 1.5;
    display: -webkit-box;
    line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: normal;
}

.price {
    color: #ff5000;
    font-size: 16px;
    font-weight: bold;
    margin: 0;
}

/* 右边圆形数量按钮 */
.quantity-control {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
}

.quantity-buttons {
    display: flex;
    align-items: center;
    gap: 5px;
}

.delete-btn {
    position: absolute;
    top: 80px;
    right: 15px;
    bottom: 15px;
    color: #999;
    cursor: pointer;
    transition: all 0.2s;
    padding: 4px;
    border-radius: 4px;
}

.round-btn {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    border: 1px solid #ddd;
    background: #f5f5f5;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 16px;
    color: #666;
    cursor: pointer;
    padding: 0;
    transition: all 0.2s;
}

.round-btn:hover {
    background: #eee;
}

.round-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.quantity {
    min-width: 30px;
    text-align: center;
    font-size: 14px;
}

/* 交互效果 */
.cart-item:hover {
    background: #fafafa;
}
</style>