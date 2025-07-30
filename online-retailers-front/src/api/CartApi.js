// src/api/CartApi.js
import instance from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';
import router from '@/router';
import { useCartStore } from '@/stores/cartStore'; // 导入购物车 Store

/**
 * 添加商品到购物车
 * @param {Object} productData - 要添加到购物车的商品数据，包含 productId, quantity 等
 * @returns {Promise<Object>} 返回服务器响应的Promise
 */
export const addItemToCart = (productData) => {
    const cartStore = useCartStore(); // 获取 Pinia Store 实例

    return new Promise((resolve, reject) => {
        instance({
            url: '/back/orders/cart/addItem',
            method: 'POST',
            data: productData,
            headers: {
                token: localStorage.getItem("token") || ""
            }
        }).then(response => {
            if (response.data.code === 1) {
                ElMessage.success(response.data.msg || '商品已成功加入购物车！');
                // *** 关键：更新 Pinia Store 中的购物车数据 ***
                cartStore.setCartItems(response.data.data); // 假设 response.data.data 是最新购物车列表
                resolve(response.data);
            } else {
                ElMessage.error(response.data.msg || '加入购物车失败！');
                reject(response.data);
            }
        }).catch(error => {
            // 错误处理逻辑保持不变
            if (error.response) {
                if (error.response.data && error.response.data.code === 0) {
                    ElMessageBox.confirm(error.response.data.msg + '，请先登录！', '登录提示', {
                        confirmButtonText: '去登录',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }).then(() => {
                        router.push('/login');
                        reject(error);
                    }).catch(() => {
                        ElMessage.info('已取消登录');
                        reject(error);
                    });
                } else if (error.response.status === 401) {
                    ElMessageBox.confirm('认证失败，请重新登录！', '登录提示', {
                        confirmButtonText: '去登录',
                        cancelButtonText: '取消',
                        type: 'warning',
                    }).then(() => {
                        router.push('/login');
                        reject(error);
                    }).catch(() => {
                        ElMessage.info('已取消登录');
                        reject(error);
                    });
                } else {
                    ElMessage.error(error.response.data.msg || '服务器错误，请稍后再试！');
                    reject(error);
                }
            } else {
                ElMessage.error('网络连接失败，请检查您的网络！');
                reject(error);
            }
        });
    });
};

/**
 * 获取购物车列表（用于初始化或刷新购物车数据）
 * @returns {Promise<Object>} 返回服务器响应的Promise
 */
export const getCartList = () => {
    return instance({
        url: '/back/orders/cart/list', // 假设后端有一个 /cart/list 接口返回当前用户的完整购物车列表
        method: 'GET',
        headers: {
            token: localStorage.getItem('token') || ''
        }
    });
};