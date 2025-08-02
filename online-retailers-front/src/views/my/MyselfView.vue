<template>
    <div class="user-center">
        <NavBar>
            <template #default>
                我的
            </template>
        </NavBar>

        <!-- 用户信息栏 -->
        <div class="user-info-container" @click="goToProfile">
            <div class="user-info">
                <div class="avatar">
                    <el-icon>
                        <User />
                    </el-icon>
                </div>
                <div v-if="isLoggedIn" class="login-text">
                    <span>{{ username || '用户' }}</span>
                </div>
                <div v-else class="login-text" @click="goToLogin">
                    <span>点击登录</span>
                </div>
                <div class="arrow">
                    <el-icon>
                        <ArrowRight />
                    </el-icon>
                </div>
            </div>
        </div>

        <!-- 我的订单 -->
        <div class="order-section">
            <div class="section-header">
                <h3>我的订单</h3>
                <div class="view-all" @click="viewAllOrders">
                    <span>全部</span>
                    <el-icon>
                        <ArrowRight />
                    </el-icon>
                </div>
            </div>
            <div class="order-types">
                <div class="order-item" @click="viewOrders('pending')">
                    <el-icon>
                        <Money />
                    </el-icon>
                    <span>待付款</span>
                </div>
                <div class="order-item" @click="viewOrders('shipped')">
                    <el-icon>
                        <Box />
                    </el-icon>
                    <span>待发货</span>
                </div>
                <div class="order-item" @click="viewOrders('delivering')">
                    <el-icon>
                        <Van />
                    </el-icon>
                    <span>待收货</span>
                </div>
                <div class="order-item" @click="viewOrders('review')">
                    <el-icon>
                        <ChatLineRound />
                    </el-icon>
                    <span>待评价</span>
                </div>
                <div class="order-item" @click="viewOrders('refund')">
                    <el-icon>
                        <Service />
                    </el-icon>
                    <span>退款/售后</span>
                </div>
            </div>
        </div>

        <!-- 其他设置项 -->
        <div class="settings-section">
            <div class="setting-item" @click="goToAddress">
                <div class="item-left">
                    <el-icon>
                        <Location />
                    </el-icon>
                    <span>收货地址</span>
                </div>
                <el-icon>
                    <ArrowRight />
                </el-icon>
            </div>
            <div class="setting-item" @click="goToCustomerService">
                <div class="item-left">
                    <el-icon>
                        <Headset />
                    </el-icon>
                    <span>客服反馈</span>
                </div>
                <el-icon>
                    <ArrowRight />
                </el-icon>
            </div>
            <div class="setting-item" @click="goToSettings">
                <div class="item-left">
                    <el-icon>
                        <Setting />
                    </el-icon>
                    <span>通用设置</span>
                </div>
                <el-icon>
                    <ArrowRight />
                </el-icon>
            </div>
            <div class="setting-item" @click="goToSecurity">
                <div class="item-left">
                    <el-icon>
                        <User />
                    </el-icon>
                    <span>账号与安全</span>
                </div>
                <el-icon>
                    <ArrowRight />
                </el-icon>
            </div>
            <div class="setting-item" @click="goToAbout">
                <div class="item-left">
                    <el-icon>
                        <InfoFilled />
                    </el-icon>
                    <span>关于</span>
                </div>
                <el-icon>
                    <ArrowRight />
                </el-icon>
            </div>
        </div>

        <!-- 退出登录 -->
        <div v-if="isLoggedIn" class="logout-btn" @click="handleLogout">
            退出登录
        </div>
    </div>
</template>

<script setup>
import NavBar from '../../components/common/NavBar.vue';
import {
    ArrowRight, Money, Box, Van, Headset,
    ChatLineRound, Service, Location,
    Setting, InfoFilled, User,
} from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { computed } from 'vue';
import { useUserStore } from '@/stores/user';
import ElementPlus, {ElMessage} from "element-plus";

const router = useRouter();
const userStore = useUserStore();

// 计算属性
const isLoggedIn = computed(() => userStore.isLoggedIn());
const username = computed(() => userStore.username);

// 页面跳转方法
const goToLogin = () => {
    router.push('/login');
};

const goToProfile = () => {
    if (isLoggedIn.value) {
        router.push('/profile');
    } else {
        goToLogin();
    }
};

const viewAllOrders = () => {
    if (checkLogin()) {
        router.push('/orders');
    }
};

const viewOrders = (type) => {
    if (checkLogin()) {
        router.push(`/orders/${type}`);
    }
};

const goToAddress = () => {
    if (checkLogin()) {
        router.push('/address');
    }
};

const goToCustomerService = () => {
    if (checkLogin()) {
        router.push('/customer-service');
    }
};

const goToSettings = () => {
    if (checkLogin()) {
        router.push('/settings');
    }
};

const goToSecurity = () => {
    if (checkLogin()) {
        router.push('/security');
    }
};

const goToAbout = () => {
    router.push('/about');
};

// 检查登录状态
const checkLogin = () => {
    if (!isLoggedIn.value) {
        router.push('/login');
        ElMessage.error('请先登录');
        return false;
    }
    return true;
};

// 退出登录
const handleLogout = () => {
    userStore.logout();
    ElMessage.success('已退出登录');
    router.push('/login');
};
</script>

<style scoped>
.user-center {
    background-color: #f5f5f5;
    min-height: 100vh;
    padding-bottom: 60px;
}

/* 用户信息栏 */
.user-info-container {
    background-color: #fff;
    margin-bottom: 10px;
}

.user-info {
    display: flex;
    align-items: center;
    padding: 20px 15px;
}

.avatar {
    border-radius: 50%;
    overflow: hidden;
    margin-right: 15px;
    border: 1px solid #ccc;
    height: 60px;
    width: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.avatar .el-icon {
    font-size: 30px;
    color: #666;
}

.login-text {
    flex: 1;
    font-size: 16px;
    color: #333;
}

.arrow {
    color: #999;
}

/* 我的订单 */
.order-section {
    background-color: #fff;
    margin-bottom: 10px;
    padding: 15px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.section-header h3 {
    margin: 0;
    font-size: 16px;
    color: #333;
}

.view-all {
    display: flex;
    align-items: center;
    color: #999;
    font-size: 14px;
    cursor: pointer;
}

.view-all span {
    margin-right: 5px;
}

.order-types {
    display: flex;
    justify-content: space-around;
    text-align: center;
}

.order-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    cursor: pointer;
}

.order-item .el-icon {
    font-size: 24px;
    margin-bottom: 5px;
    color: #ff5000;
}

.order-item span {
    font-size: 12px;
    color: #666;
}

/* 设置项 */
.settings-section {
    background-color: #fff;
    margin-bottom: 10px;
}

.setting-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;
}

.item-left {
    display: flex;
    align-items: center;
}

.item-left .el-icon {
    margin-right: 10px;
    color: #ff5000;
}

.setting-item:last-child {
    border-bottom: none;
}

/* 退出登录 */
.logout-btn {
    margin: 20px 15px;
    padding: 12px 0;
    background-color: #ff5000;
    color: #fff;
    text-align: center;
    border-radius: 6px;
    font-size: 16px;
    cursor: pointer;
}
</style>