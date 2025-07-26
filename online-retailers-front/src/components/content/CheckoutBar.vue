<template>
    <div class="checkout-bar">
        <!-- 全选按钮 -->
        <div class="select-all">
            <div class="check-circle" @click="toggleSelectAll" :class="{ 'checked': selectAll }">
                <!-- 选中时添加 checked class -->
                <el-icon class="check-icon">
                    <Check />
                </el-icon>
            </div>
            <span class="select-text">全选</span>
        </div>

        <!-- 总计金额区域 -->
        <div class="total-wrapper">
            <div class="total-amount">
                <span class="label">合计:</span>
                <span class="price">￥{{ totalPrice.toFixed(2) }}</span>
            </div>
            <!-- 结算按钮 -->
            <button class="checkout-btn">
                结算({{ count }})
            </button>
        </div>
    </div>
</template>

<script setup>
import { defineProps, onMounted, ref, watch } from 'vue';
import { Check } from '@element-plus/icons-vue';

const props = defineProps(['shopCartData', 'changeProduct', 'changeNumber', 'deleteProduct']);

const localShopCartData = ref([...props.shopCartData]);
const selectAll = ref(false);
const totalPrice = ref(0);
const count = ref(0);

onMounted(() => {
    let sum = 0;
    let cnt = 0;
    localShopCartData.value.forEach(item => {
        if (item.checked) {
            sum += (item.price * item.num);
            cnt = cnt + 1;
        }
    });
    totalPrice.value = Math.max(0, sum);
    count.value = Math.max(0, cnt);
})

const toggleSelectAll = () => {
    if (!selectAll.value) {
        let sum = 0;
        let cnt = 0;
        localShopCartData.value.forEach(item => {
            item.checked = true;
            sum += (item.price * item.num);
            cnt = cnt + 1;
        });

        // 计算总价和数量
        totalPrice.value = Math.max(0, sum);
        count.value = Math.max(0, cnt);
    } else {
        localShopCartData.value.forEach(item => {
            item.checked = false;
        });
        totalPrice.value = 0;
        count.value = 0;
    }
    selectAll.value = !selectAll.value;
};

const handleChangeProduct = (value) => {
    if (value && typeof value.index === 'number' && value.index >= 0 && value.index < localShopCartData.value.length) {
        const item = localShopCartData.value[value.index];
        if (item.checked) {
            totalPrice.value += (item.price * item.num);
            count.value += 1;

            // 判断是否全选
            let allChecked = true;
            localShopCartData.value.forEach(item => {
                if (!item.checked) {
                    allChecked = false;
                }
            });
            selectAll.value = allChecked;
        } else {
            totalPrice.value -= (item.price * item.num);
            totalPrice.value = Math.max(0, totalPrice.value);
            count.value -= 1;
            count.value = Math.max(0, count.value);
            selectAll.value = false;
        }
    }
};

const handleChangeNumber = (value) => {
    if (value && typeof value.index === 'number' && typeof value.sign === 'number' && value.index >= 0 && value.index < localShopCartData.value.length) {
        const item = localShopCartData.value[value.index];
        if (item.checked) {
            totalPrice.value += (item.price * value.sign);
            totalPrice.value = Math.max(0, totalPrice.value);
        }
    }
};

const handleDeleteProduct = (value) => {
    if (value && typeof value === 'number' && value >= 0 && value < localShopCartData.value.length) {
        const item = localShopCartData.value[value];
        if (item.checked) {
            totalPrice.value -= (item.price * item.num);
            totalPrice.value = Math.max(0, totalPrice.value);
            count.value -= 1;
            count.value = Math.max(0, count.value);
        }
        localShopCartData.value.splice(value, 1);
    }
};

// 监听数据变化自动保存
watch(localShopCartData, (newVal) => {
    localStorage.setItem('shopCartData', JSON.stringify(newVal));
}, { deep: true });

// 监听 changeProduct 变化
watch(() => props.changeProduct, (newVal) => {
    handleChangeProduct(newVal);
}, { immediate: true });

// 监听 changeNumber 变化
watch(() => props.changeNumber, (newVal) => {
    handleChangeNumber(newVal);
}, { immediate: true });

// 监听 deleteProduct 变化
watch(() => props.deleteProduct, (newVal) => {
    handleDeleteProduct(newVal);
}, { immediate: true });

</script>

<style scoped>
.checkout-bar {
    position: fixed;
    bottom: 59px;
    left: 0;
    right: 0;
    height: 50px;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px;
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
    z-index: 100;
}

/* 全选区域 */
.select-all {
    display: flex;
    align-items: center;
    cursor: pointer;
}

.check-circle {
    width: 18px;
    height: 18px;
    border-radius: 50%;
    border: 1px solid #ccc;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 8px;
    /* transition: all 0.2s; */
}

/* 选中状态 - 使用时添加到 check-circle */
.check-circle.checked {
    background: #ff5000;
    border-color: #ff5000;
}

.check-icon {
    width: 12px;
    height: 12px;
    color: white;
}

.select-text {
    font-size: 14px;
    color: #333;
}

/* 总计金额 */
.total-wrapper {
    display: flex;
    align-items: center;
    margin-left: auto;
    /* 关键：将整个区块推到最右侧 */
}

.total-amount {
    display: flex;
    align-items: baseline;
    /* 保持文字基线对齐 */
    margin-right: 15px;
    white-space: nowrap;
    /* 防止换行 */
}

.label {
    font-size: 14px;
    color: #333;
}

.price {
    color: #ff5000;
    font-weight: bold;
    font-size: 16px;
    margin-left: 5px;
}

/* 结算按钮 */
.checkout-btn {
    background: #ff5000;
    color: white;
    border: none;
    height: 40px;
    min-width: 100px;
    padding: 0 20px;
    border-radius: 20px;
    font-size: 15px;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;
}

/* 禁用状态 - 使用时添加到 checkout-btn */
.checkout-btn.disabled {
    background: #ccc;
    cursor: not-allowed;
}

/* 两侧半圆效果 */
.checkout-btn::before,
.checkout-btn::after {
    content: '';
    position: absolute;
    width: 20px;
    height: 40px;
    background: #ff5000;
}

.checkout-btn::before {
    left: -10px;
    border-radius: 0 20px 20px 0;
}

.checkout-btn::after {
    right: -10px;
    border-radius: 20px 0 0 20px;
}

/* 禁用状态的半圆 */
.checkout-btn.disabled::before,
.checkout-btn.disabled::after {
    background: #ccc;
}
</style>