<template>
  <div>
    <NavBar>
      <template #default>
        首页
      </template>
    </NavBar>
    <SearchBar/>
    <BannerView :bannerData="banners"/>
    <RecommendView :recommendData="recommends"/>
    <!-- 点击之后，从子组件里会返回一个index，然后再执行HomeView里的tabClick函数 -->
    <TabControl :tabData="tabs" @tabClick="tabClick"/>
    <GoodsList
      :goods-type="currentType"
      :page-size="6"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import NavBar from '@/components/common/NavBar.vue';
import SearchBar from '@/components/common/SearchBar.vue';
import BannerView from './BannerView.vue';
import RecommendView from './RecommendView.vue';
import TabControl from '@/components/content/TabControl.vue';
import GoodsList from '@/components/content/GoodsList.vue';
import {getAllBannerList} from "@/api/BannerApi.js";
import {getAllCategoryList} from "@/api/CategoryApi.js";


const banners = ref([]);
const recommends = ref([]);
const tabs = ref([]);

const currentType = ref("deal");

const tabClick = (index) => {
  // 根据tab类型设置goodsType
  const tab = tabs.value[index];
  if (tab.name === 'deal') {
    currentType.value = 'deal';
  } else if (tab.categoryId) {
    currentType.value = tab.categoryId;
  } else {
    currentType.value = tab.name;
  }
}

onMounted(async () => {
  const [bannerRes, categoryRes, tabRes] = await Promise.all([
    getAllBannerList(),
    getAllCategoryList(),
    fetch('/tabs.json')
  ]);

  banners.value = bannerRes.data.data || [];
  recommends.value = categoryRes.data.data || [];
  tabs.value = (await tabRes.json()).data || [];
});
</script>
