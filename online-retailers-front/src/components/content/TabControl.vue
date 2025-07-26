<!-- 内容选项卡组件 -->
<template>
  <el-tabs v-model="activeTab" @tab-click="tabClick">
    <!-- label 属性用于定义选项卡的显示文本，也就是用户在界面上能看到的选项卡名称 -->
    <!-- name 属性是选项卡的唯一标识符，用于区分不同的选项卡 -->
    <el-tab-pane v-for="(item, index) in tabData" :key="index" :label="item.title" :name="item.name">
        {{ item.title }}
    </el-tab-pane>
  </el-tabs>
</template>

<script setup>
import { ref, defineProps, watch, defineEmits } from 'vue';

const props = defineProps(['tabData']);
const emit = defineEmits(['tabClick']);

const activeTab = ref('') // 默认激活的tab

// 监听 tabData 的变化
watch(() => props.tabData, (newTabData) => {
  if (newTabData && newTabData.length > 0) {
    // 当 tabData 有数据时，将第一个选项卡的 name 赋值给 activeTab
    activeTab.value = newTabData[0].name;
  }
}, { immediate: true }); // immediate: true 表示在组件初始化时立即执行一次

const tabClick = (tab) => {
  emit('tabClick', tab.index);
}
</script>