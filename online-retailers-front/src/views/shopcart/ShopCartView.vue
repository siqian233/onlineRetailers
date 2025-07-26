<template>
    <div>
        <NavBar>
            <template #default>
                购物车
            </template>
        </NavBar>
        <ShopCartList :shopCartData="showCart" @changeShopCartData="changeShopCart" @changeNumber="changeProductNumber" @deleteProduct="deleteGoalProduct" />
        <CheckoutBar :shopCartData="showCart" :changeProduct="changeSelectProduct" :changeNumber="changeNumber" :deleteProduct="deleteProduct" />
    </div>
</template>

<script setup>
import NavBar from '../../components/common/NavBar.vue';
import ShopCartList from '../../components/content/ShopCartList.vue';
import CheckoutBar from '../../components/content/CheckoutBar.vue';
import { computed, ref } from 'vue';

// 购物车数据保存在浏览器中
const showCart = JSON.parse(localStorage.getItem('shopCartData'));
// 选中商品的索引和选中状态
const selectProductIndex = ref();
const selectProductCheck = ref();
// 加减商品的索引和加减符号
const productIndex = ref();
const productSign = ref();
const productNumber = ref();
// 删除商品的索引
const deleteProductIndex = ref();


const changeSelectProduct = computed(() => {
    return { index: selectProductIndex.value, select: selectProductCheck.value }
});

const changeNumber = computed(() => {
    return { index: productIndex.value, sign: productSign.value, number: productNumber.value }
});

const deleteProduct = computed(() => {
    return deleteProductIndex.value;
});

const changeShopCart = (index, checked) => {
    selectProductIndex.value = index;
    selectProductCheck.value = checked;
};

const changeProductNumber = (index, sign, number) => {
    productIndex.value = index;
    productSign.value = sign;
    productNumber.value = number;
};

const deleteGoalProduct = (index) => {
    deleteProductIndex.value = index;
}
</script>
