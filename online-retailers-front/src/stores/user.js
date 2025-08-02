import { defineStore } from 'pinia';
import { ref } from 'vue';
import { loginAPI, registerAPI } from '@/api/UserApi';

export const useUserStore = defineStore('user', () => {
    const userInfo = ref({});
    const token = ref('');
    const username = ref('');
    const roles = ref([]);

    // 登录方法
    const login = async (loginData) => {
        try {
            const response = await loginAPI(loginData);
            
            // 检查服务器响应
            if (response.data && response.data.token && response.data.myUserDTO) {
                const { token: userToken, myUserDTO } = response.data;
                
                // 更新状态
                userInfo.value = myUserDTO;
                token.value = userToken;
                username.value = myUserDTO.userName || myUserDTO.username || '';
                roles.value = myUserDTO.roles || [];
                
                // 持久化存储
                localStorage.setItem('userInfo', JSON.stringify(myUserDTO));
                localStorage.setItem('token', userToken);
                localStorage.setItem('username', username.value);
                localStorage.setItem('roles', JSON.stringify(roles.value));
                
                return { success: true, data: response.data };
            } else {
                throw new Error(response.data?.msg || '登录响应数据格式不正确');
            }
        } catch (error) {
            console.error('登录失败:', error);
            const errorMessage = error.response?.data?.msg || 
                               error.response?.data?.message || 
                               error.message || 
                               '登录失败，请稍后再试';
            return { success: false, error: errorMessage };
        }
    };

    // 退出登录
    const logout = () => {
        // 清空状态
        userInfo.value = {};
        token.value = '';
        username.value = '';
        roles.value = [];
        
        // 清空本地存储
        localStorage.removeItem('userInfo');
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('roles');
        
        return { success: true, message: '已退出登录' };
    };

    // 注册方法
    const register = async (userData) => {
        try {
            const response = await registerAPI(userData);
            
            if (response.data) {
                return { 
                    success: response.data.code === 1, 
                    data: response.data.data,
                    message: response.data.msg || '注册成功'
                };
            } else {
                throw new Error('注册响应数据格式不正确');
            }
        } catch (error) {
            console.error('注册失败:', error);
            const errorMessage = error.response?.data?.msg || 
                               error.response?.data?.message || 
                               error.message || 
                               '注册失败，请稍后再试';
            return { success: false, error: errorMessage };
        }
    };

    // 页面刷新时恢复用户状态
    const restoreUserState = () => {
        try {
            const storedUserInfo = localStorage.getItem('userInfo');
            const storedToken = localStorage.getItem('token');
            const storedUsername = localStorage.getItem('username');
            const storedRoles = localStorage.getItem('roles');

            if (storedUserInfo) {
                userInfo.value = JSON.parse(storedUserInfo);
            }
            if (storedToken) {
                token.value = storedToken;
            }
            if (storedUsername) {
                username.value = storedUsername;
            }
            if (storedRoles) {
                roles.value = JSON.parse(storedRoles);
            }
        } catch (error) {
            console.error('恢复用户状态失败:', error);
            // 如果解析失败，清空所有存储
            logout();
        }
    };

    // 检查用户是否已登录
    const isLoggedIn = () => {
        return !!token.value && token.value.length > 0;
    };

    // 获取用户角色
    const getUserRoles = () => {
        return roles.value;
    };

    // 检查是否具有特定角色
    const hasRole = (role) => {
        return roles.value.includes(role);
    };

    return {
        userInfo,
        token,
        username,
        roles,
        login,
        logout,
        register,
        restoreUserState,
        isLoggedIn,
        getUserRoles,
        hasRole
    };
});