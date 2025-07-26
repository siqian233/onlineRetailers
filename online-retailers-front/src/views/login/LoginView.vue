<template>
    <div class="login-container">
        <NavBar>
            <template #default>
                登录
            </template>
        </NavBar>

        <!-- 主要内容区域 -->
        <div class="login-content">
            <!-- 使用Element Plus用户图标作为Logo -->
            <div class="logo">
                <el-icon color="#ff5000" :size="60">
                    <ShoppingTrolley />
                </el-icon>
            </div>

            <!-- 登录表单 -->
            <div class="login-form">
                <!-- 账号输入框 -->
                <div class="input-container">
                    <el-input v-model="account" placeholder="请输入账号" class="custom-input" :prefix-icon="User" />
                </div>

                <!-- 密码输入框 -->
                <div class="input-container">
                    <el-input v-model="password" placeholder="请输入密码" type="password" class="custom-input"
                        :prefix-icon="Lock" show-password />
                </div>

                <!-- 登录按钮 -->
                <el-button class="login-btn" type="primary" @click="handleLogin">
                    登录
                </el-button>
                <br />
                <br />
                <!-- 注册按钮 -->
                <el-button class="registered-btn">
                    还没有账号，去注册一个
                </el-button>
            </div>
        </div>

        <!-- 覆盖底部导航栏的空白区域 -->
        <div class="bottom-cover">
            <div class="bottom-links">
                <span @click="goToAbout">关于</span>
                <span class="divider">|</span>
                <span @click="goToResetPassword">找回密码</span>
            </div>
        </div>
    </div>
</template>

<script setup>
import NavBar from '../../components/common/NavBar.vue';
import { ref } from 'vue';
import { ShoppingTrolley, User, Lock } from '@element-plus/icons-vue';
import router from '@/router';

const userAccount = JSON.parse(localStorage.getItem('userAccount'));
const account = ref('');
const password = ref('');
const isLogin = ref(false);

const handleLogin = () => {
    if (userAccount.account === account.value && userAccount.password === password.value) {
        isLogin.value = true;
        userAccount.isLogin = true;
        localStorage.setItem('userAccount', JSON.stringify(userAccount));
        router.push({ name: 'myself' });
        alert('登录成功');
    } else {
        alert('账号或密码错误');
    }
};
</script>

<style scoped>
.login-container {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: white;
    /* 纯白背景 */
}

.login-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    margin-top: 20px;
}

.logo {
    margin-bottom: 40px;
    display: flex;
    justify-content: center;
}

.login-form {
    width: 100%;
    max-width: 300px;
}

.input-container {
    margin-bottom: 20px;
}

/* 统一输入框样式 */
:deep(.custom-input .el-input__wrapper) {
    border-radius: 25px;
    padding: 0 20px;
    height: 50px;
    box-shadow: 0 0 0 1px #dcdfe6 inset;
}

:deep(.custom-input .el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px #c0c4cc inset;
}

:deep(.custom-input .el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 1px #ff5000 inset;
}

:deep(.custom-input .el-input__inner) {
    height: 48px;
}

/* 登录按钮样式 */
.login-btn {
    width: 100%;
    height: 50px;
    border-radius: 25px;
    background-color: #ff5000;
    border: none;
    font-size: 16px;
    font-weight: bold;
}

.login-btn:hover {
    background-color: #ff3a00;
}

.registered-btn {
    width: 100%;
    height: 50px;
    border-radius: 25px;
    background-color: #ffffff;
    border: none;
    font-size: 16px;
    font-weight: bold;
    border: 1px solid #000000;
}

.registered-btn:hover {
    color: #3b3b3b;
    background-color: #f5f5f5;
}

/* 覆盖底部导航栏 */
.bottom-cover {
    height: 120px;
    background-color: white;
    z-index: 1000;
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    padding-bottom: 30px;
}

.bottom-links {
    display: flex;
    align-items: center;
    font-size: 14px;
}

.bottom-links span {
    cursor: pointer;
    color: #606266;
    transition: color 0.3s;
}

.bottom-links span:hover {
    color: #ff5000;
}

.bottom-links .divider {
    margin: 0 10px;
    color: #dcdfe6;
    cursor: default;
}
</style>