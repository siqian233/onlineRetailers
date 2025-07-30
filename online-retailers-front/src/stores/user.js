import { defineStore } from 'pinia';
import { ref } from 'vue';

import { loginAPI, registerAPI } from '@/api/UserApi';

export const useUserStore = defineStore('user', () => {
    const userInfo = ref({});
    const token = ref('');
    const username = ref('');
    const roles = ref([]);

    // 登录方法
    const login = async (inputUserName, inputUserPassword) => {
        try {
            const res = await loginAPI({ userName: inputUserName, userPassword: inputUserPassword });

            // 检查服务器响应码和数据结构
            if (res.data.code === 1 && res.data.data && res.data.data.token && res.data.data.myUserDTO) {
                const { token: apiToken, myUserDTO } = res.data.data;

                userInfo.value = myUserDTO;
                token.value = apiToken;
                username.value = myUserDTO.userName;
                roles.value = myUserDTO.roles;

                // 持久化存储到 localStorage
                localStorage.setItem('userInfo', JSON.stringify(userInfo.value));
                localStorage.setItem('token', token.value);
                localStorage.setItem('userName', username.value);
                localStorage.setItem('roles', JSON.stringify(roles.value)); // 数组需要转换为 JSON 字符串存储

                return res; // 登录成功后返回整个响应，供组件进一步处理（可选）
            } else {
                // 如果服务器返回码不是1，或者数据结构不符合预期
                // 可以从 res.msg 中获取错误信息，或者提供一个通用错误
                throw new Error(res.msg || '服务器返回数据格式不正确或操作失败。');
            }
        } catch (error) {
            console.error('User store login error:', error);
            const errorMessage = error.response?.data?.msg || error.message || '登录失败，请稍后再试。';
            throw new Error(errorMessage);
        }
    };

    // 退出登录
    const logout = () => {
        userInfo.value = {};
        token.value = '';
        username.value = '';
        roles.value = [];
        // 清空本地存储
        localStorage.removeItem('userInfo');
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('roles');
    };

    // 注册方法
    const register = async (userData) => {
        const res = await registerAPI(userData);
        // 假设注册API的返回结构也类似：{ code: 1, msg: "...", data: {...} }
        if (res.code === 1) {
            return res.data; // 返回注册成功的数据部分
        } else {
            throw new Error(res.msg || '注册失败。');
        }
    };

    // 页面刷新时，用户状态保持，从本地获取数据
    const getStorageData = () => {
        const storageUserInfo = localStorage.getItem('userInfo');
        const storageToken = localStorage.getItem('token');
        const storageUsername = localStorage.getItem('username');
        const storageRoles = localStorage.getItem('roles');

        if (storageUserInfo) {
            userInfo.value = JSON.parse(storageUserInfo);
        }
        if (storageToken) {
            token.value = storageToken;
        }
        if (storageUsername) {
            username.value = storageUsername;
        }
        if (storageRoles) {
            roles.value = JSON.parse(storageRoles); // 存储时转为JSON字符串，读取时需解析
        }
    };

    return {
        userInfo,
        token,
        username,
        roles,
        login,
        logout,
        register,
        getStorageData
    };
});