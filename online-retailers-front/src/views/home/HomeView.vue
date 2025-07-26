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
import NavBar from '../../components/common/NavBar.vue';
import SearchBar from '../../components/common/SearchBar.vue';
import BannerView from './BannerView.vue';
import RecommendView from './RecommendView.vue';
import TabControl from '../../components/content/TabControl.vue';
import GoodsList from '../../components/content/GoodsList.vue';

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
  // const res = await fetch('/banners.json');
  // const data = await res.json();
  // banners.value = data.data;
  const responseResult = await fetch('http://localhost:20001/product/banner/status/1');
  const data = await responseResult.json();
  if (data.code === 1) {
    banners.value = data.data;
  }
});

onMounted(async () => {
  const res = await fetch('/recommend.json');
  const data = await res.json();
  recommends.value = data.data;
});

onMounted(async () => {
  const res = await fetch('/tabs.json');
  const data = await res.json();
  tabs.value = data.data;
});

onMounted(async () => {
  const res = await fetch('/goodsman.json');
  const data = await res.json();
  goods.man = data.data;
});

onMounted(async () => {
  const res = await fetch('/goodswoman.json');
  const data = await res.json();
  goods.woman = data.data;
});

onMounted(async () => {
  const res = await fetch('/goodschildren.json');
  const data = await res.json();
  goods.children = data.data;
});


</script>
