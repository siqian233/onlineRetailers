import { defineStore } from 'pinia';
import { getCartList } from '@/api/CartApi';

export const useCartStore = defineStore('cart', {
    state: () => ({
        items: [], // 购物车商品列表
        count: 0,  // 购物车商品总数量（或总品类数量）
        totalAmount: 0, // 总金额
    }),
    actions: {
        // 设置购物车商品列表
        setCartItems(cartItems) {
            this.items = cartItems;
            // 重新计算购物车总数量
            this.count = cartItems.reduce((sum, item) => sum + item.num, 0);
            // 如果你还需要计算总金额等，可以在这里添加
            this.totalAmount = cartItems.reduce((sum, item) => sum + item.price * item.num, 0);
        },

        // 异步获取最新购物车数据并更新 Store
        async fetchCartItems() {
            try {
                const response = await getCartList(); // 调用获取购物车列表的API
                if (response.data.code === 1) {
                    this.setCartItems(response.data.data); // 假设后端返回的数据在 response.data.data
                } else {
                    // 处理获取购物车失败的情况，可能用户未登录或token失效
                    // CartApi 应该已经处理了登录提示，这里只需日志记录
                    console.error('Failed to fetch cart items:', response.data.msg);
                    this.setCartItems([]); // 清空购物车，因为数据不准确
                }
            } catch (error) {
                console.error('Error fetching cart items:', error);
                this.setCartItems([]); // 清空购物车
            }
        },

        // 示例：更新单个商品数量（可选，如果后端支持增量更新）
        // 如果你只依赖后端返回完整列表，则这个 action 可以不用
        updateItemQuantity(productId, newQuantity) {
            const item = this.items.find(i => i.productId === productId);
            if (item) {
                item.num = newQuantity;
                this.count = this.items.reduce((sum, item) => sum + item.num, 0);
            }
        },

        // 示例：从购物车移除商品（可选，如果后端支持）
        removeItem(productId) {
            this.items = this.items.filter(item => item.productId !== productId);
            this.count = this.items.reduce((sum, item) => sum + item.num, 0);
        }
    },
    // 可以添加 getters 来从 state 派生数据，例如 checkedItems, totalPrice 等
    getters: {
        cartTotalCount: (state) => state.count,
        // checkedItems: (state) => state.items.filter(item => item.checked),
        checkedTotalPrice: (state) => state.items.filter(item => item.checked).reduce((sum, item) => sum + item.price * item.num, 0),
    }
});