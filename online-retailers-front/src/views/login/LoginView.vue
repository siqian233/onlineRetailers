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
                <!-- 用户名输入框 -->
                <div class="input-container">
                    <el-input
                        v-model="loginForm.username"
                        placeholder="请输入用户名"
                        class="custom-input"
                        :prefix-icon="User"
                        @keyup.enter="handleLogin"
                    />
                </div>

                <!-- 密码输入框 -->
                <div class="input-container">
                    <el-input
                        v-model="loginForm.password"
                        placeholder="请输入密码"
                        type="password"
                        class="custom-input"
                        :prefix-icon="Lock"
                        show-password
                        @keyup.enter="handleLogin"
                    />
                </div>

                <!-- 登录按钮 -->
                <el-button
                    class="login-btn"
                    type="primary"
                    @click="handleLogin"
                    :loading="loading"
                    :disabled="loading"
                >
                    {{ loading ? '登录中...' : '登录' }}
                </el-button>

                <!-- 注册按钮 -->
                <el-button class="register-btn" @click="goToRegister">
                    还没有账号？立即注册
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
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { ShoppingTrolley, User, Lock } from '@element-plus/icons-vue';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();

// 表单数据
const loginForm = reactive({
    username: '',
    password: ''
});

// 状态管理
const loading = ref(false);

// 登录处理
const handleLogin = async () => {
    // 表单验证
    if (!loginForm.username.trim()) {
        ElMessage.warning('请输入用户名');
        return;
    }

    if (!loginForm.password) {
        ElMessage.warning('请输入密码');
        return;
    }

    // 设置加载状态
    loading.value = true;

    try {
        // 调用登录方法
        const result = await userStore.login({
            username: loginForm.username,
            password: loginForm.password
        });

        if (result.success) {
            ElMessage.success('登录成功');
            // 跳转到首页或来源页面
            const redirect = router.currentRoute.value.query.redirect || '/my';
            router.push(redirect);
        } else {
            ElMessage.error(result.error || '登录失败');
        }
    } catch (error) {
        console.error('登录异常:', error);
        ElMessage.error('登录过程中发生错误');
    } finally {
        loading.value = false;
    }
};

// 页面跳转
const goToRegister = () => {
    router.push('/register');
};

const goToAbout = () => {
    router.push('/about');
};

const goToResetPassword = () => {
    router.push('/reset-password');
};
</script>

<style scoped>
.login-container {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: white;
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
    margin-bottom: 20px;
}

.login-btn:hover {
    background-color: #ff3a00;
}

.login-btn:disabled {
    background-color: #ffb399;
}

/* 注册按钮样式 */
.register-btn {
    width: 100%;
    height: 50px;
    border-radius: 25px;
    background-color: #ffffff;
    border: 1px solid #000000;
    font-size: 16px;
    font-weight: bold;
    margin-left: 0;
    color: #000000;
}

.register-btn:hover {
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