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
    <GoodsList :goodsData="showGoods"/>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue';
import NavBar from '@/components/common/NavBar.vue';
import SearchBar from '@/components/common/SearchBar.vue';
import BannerView from './BannerView.vue';
import RecommendView from './RecommendView.vue';
import TabControl from '@/components/content/TabControl.vue';
import GoodsList from '@/components/content/GoodsList.vue';
import {getBannerList, getCategoryList} from "@/api/ProductApi.js";

const banners = ref([]);
const recommends = ref([]);
const tabs = ref([]);

const currentType = ref('man');
const goods = reactive({});

const showGoods = computed(() => {
  return goods[currentType.value];
});

const tabClick = (index) => {
  currentType.value = tabs.value[index].name;
}

onMounted(async () => {
  const [bannerRes, categoryRes, tabRes, manRes, womanRes, childrenRes] = await Promise.all([
    getBannerList(),
    getCategoryList(),
    fetch('/tabs.json'),
    fetch('/goodsman.json'),
    fetch('/goodswoman.json'),
    fetch('/goodschildren.json')
  ]);

  banners.value = bannerRes;
  recommends.value = categoryRes;
  tabs.value = (await tabRes.json()).data;
  goods.man =(await manRes.json()).data;
  goods.woman =(await womanRes.json()).data;
  goods.children = (await childrenRes.json()).data;
});
</script>
