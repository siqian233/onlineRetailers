<template>
    <div class="fullscreen-customer-service">
        <NavBar>
            <template #default>
                客服反馈
            </template>
        </NavBar>

        <div class="chat-container">
            <div class="messages" ref="messagesContainer">
                <div v-for="(message, index) in messages" :key="index" :class="['message', message.sender]">
                    <div class="message-content-wrapper">
                        <div class="message-content">{{ message.text }}</div>
                        <div class="message-time">{{ message.time }}</div>
                    </div>
                </div>
            </div>

            <div class="input-area">
                <input v-model="inputMessage" @keyup.enter="sendMessage" placeholder="请输入您的问题..."
                    class="message-input" />
                <button @click="sendMessage" class="send-button">发送</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';
import NavBar from '../../components/common/NavBar.vue';

const messages = ref([
    {
        text: '您好，请问有什么可以帮您？',
        sender: 'service',
        time: getCurrentTime()
    }
]);

const inputMessage = ref('');
const messagesContainer = ref(null);

function sendMessage() {
    if (inputMessage.value.trim() === '') return;

    // 添加用户消息
    messages.value.push({
        text: inputMessage.value,
        sender: 'customer',
        time: getCurrentTime()
    });

    // 清空输入框
    inputMessage.value = '';

    // 滚动到底部
    scrollToBottom();

    // 模拟客服回复
    setTimeout(() => {
        messages.value.push({
            text: '你好',
            sender: 'service',
            time: getCurrentTime()
        });
        scrollToBottom();
    }, 500);
}

function scrollToBottom() {
    nextTick(() => {
        if (messagesContainer.value) {
            messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
        }
    });
}

function getCurrentTime() {
    const now = new Date();
    return `${now.getHours()}:${now.getMinutes().toString().padStart(2, '0')}`;
}

onMounted(() => {
    scrollToBottom();
});
</script>

<style scoped>
.fullscreen-customer-service {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    display: flex;
    flex-direction: column;
    background-color: #f5f5f5;
    font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
    z-index: 1000;
}

.chat-container {
    display: flex;
    flex-direction: column;
    height: calc(100vh - 50px);
}

.messages {
    flex: 1;
    overflow-y: auto;
    padding-top: 65px;
    padding-left: 13px;
    padding-right: 13px;
    background-color: #f5f3f3;
}

.message {
    display: flex;
    margin-bottom: 15px;
}

.message.service {
    justify-content: flex-start;
}

.message.customer {
    justify-content: flex-end;
}

.message-avatar {
    margin-right: 10px;
}

.message-avatar img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
}

.message-content-wrapper {
    max-width: 70%;
}

.message-content {
    padding: 10px 15px;
    border-radius: 18px;
    word-wrap: break-word;
    line-height: 1.5;
}

.service .message-content {
    background-color: white;
    border-bottom-left-radius: 4px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.customer .message-content {
    background-color: #1e88e5;
    color: white;
    border-bottom-right-radius: 4px;
}

.message-time {
    font-size: 12px;
    color: #999;
    margin-top: 4px;
}

.service .message-time {
    text-align: left;
    padding-left: 15px;
}

.customer .message-time {
    text-align: right;
    padding-right: 15px;
}

.input-area {
    display: flex;
    padding: 10px;
    background-color: white;
    border-top: 1px solid #ddd;
    align-items: center;
    z-index: 1000;
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
}

.message-input {
    flex: 1;
    padding: 12px 15px;
    border: 1px solid #ddd;
    border-radius: 20px;
    outline: none;
    font-size: 14px;
}

.message-input:focus {
    border-color: #1e88e5;
}

.send-button {
    margin-left: 10px;
    padding: 10px 20px;
    background-color: #1e88e5;
    color: white;
    border: none;
    border-radius: 20px;
    cursor: pointer;
    font-size: 14px;
}

.send-button:hover {
    background-color: #1976d2;
}
</style>