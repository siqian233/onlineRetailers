<template>
    <div class="search-bar">
        <input type="text" v-model="searchQuery" placeholder="请输入搜索内容..." @keyup.enter="handleSearch"
            class="search-input" />
        <button @click="handleSearch" class="search-button">
            <i class="search-icon">
                <Search />
            </i>
        </button>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'

const searchQuery = ref('')

</script>

<style scoped>
.search-bar {
    display: flex;
    align-items: center;
    max-width: 600px;
    margin: 0 auto;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    overflow: hidden;
    transition: all 0.3s;
    border-radius: 50px;
}

.search-bar:focus-within {
    border-color: #409eff;
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.search-input {
    flex: 1;
    padding: 10px 15px;
    border: none;
    outline: none;
    font-size: 14px;
}

.search-button {
    width: 50px;
    padding: 10px 15px;
    border: none;
    background-color: #f5f7fa;
    cursor: pointer;
    transition: background-color 0.3s;
}

.search-button:hover {
    background-color: #e6e9ed;
}

.search-icon {
    width: 20px;
    font-size: 16px;
}
</style>