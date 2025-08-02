<!-- 商品列表组件 -->
<template>
  <div class="goods-wrapper"
       v-infinite-scroll="loadMore"
       :infinite-scroll-disabled="loading || noMore || isLoadingDisabled"
       :infinite-scroll-distance="10">
    <!-- 商品列表 -->
    <div v-if="hasData" class="goods-container">
      <div v-for="(item, index) in displayGoods" :key="index" class="goods-item ">
        <router-link :to="{ name: 'detail', query: { item: JSON.stringify(item) } }" class="goods-link">
          <el-image :src="item.mainImage || item.url" :alt="item.name" fit="contain" lazy/>
          <p class="goods-name">{{ item.productName }}</p>
          <div class="price-info">
            <p class="goods-price">￥{{ item.price }}</p>
            <p class="original-price">￥{{ item.originalPrice || item.price }}</p>
          </div>
        </router-link>
      </div>
    </div>

    <!-- 加载指示器 -->
    <div v-if="loading && hasData" class="goods-container">
      <el-skeleton v-for="i in 4" :key="i" :loading="true" animated class="skeleton-item">
        <template #template>
          <el-skeleton-item variant="image" style="width: 100%; height: 150px" />
          <div style="padding: 14px 0">
            <el-skeleton-item variant="h3" style="width: 50%" />
            <div>
              <el-skeleton-item variant="text" style="margin-right: 16px" />
              <el-skeleton-item variant="text" style="width: 30%" />
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>

    <!-- 没有数据提示 -->
    <div v-if="!hasData && !loading" class="no-data">
      <el-empty description="暂无商品数据" />
    </div>

    <!-- 没有更多数据提示 -->
    <div v-if="noMore && hasData && displayGoods.length > 0" class="no-more">
      没有更多数据了
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { defineProps, defineEmits } from 'vue'
import { getAllDealProductList, getProductListByCategoryId } from '@/api/ProductApi.js'

const props = defineProps({
  // 商品类型: 'deal'表示特价商品, 数字表示分类ID
  goodsType: {
    type: [String, Number],
    default: 'deal'
  },
  // 每页大小
  pageSize: {
    type: Number,
    default: 6
  }
})

const emit = defineEmits(['loadMore'])

// 响应式数据
const currentPage = ref(1)
const loading = ref(false)
const noMore = ref(false)
const displayGoods = ref([])
const allGoodsLoaded = ref(false) // 是否所有商品都已加载完成

// 计算属性：是否有数据
const hasData = computed(() => {
  return displayGoods.value && displayGoods.value.length > 0
})

// 计算属性：是否禁用无限滚动
const isLoadingDisabled = computed(() => {
  // 如果是通过goodsData传入数据，则禁用组件内部的分页加载
  return props.goodsType === undefined
})

// 监听商品类型变化
watch(
  () => props.goodsType,
  () => {
    // 重置状态
    currentPage.value = 1
    noMore.value = false
    allGoodsLoaded.value = false
    displayGoods.value = []
    // 重新加载数据
    loadMore()
  }
)

// 加载更多数据
const loadMore = async () => {
  // 如果正在加载、没有更多数据或禁用了加载，则不执行加载
  if (loading.value || noMore.value || isLoadingDisabled.value || allGoodsLoaded.value) return

  loading.value = true

  try {
    let response
    // 根据商品类型调用不同的API
    if (props.goodsType === 'deal') {
      // 获取特价商品
      response = await getAllDealProductList(currentPage.value, props.pageSize)
    } else if (typeof props.goodsType === 'number') {
      // 根据分类ID获取商品
      response = await getProductListByCategoryId(props.goodsType, currentPage.value, props.pageSize)
    } else {
      // 不支持的类型
      noMore.value = true
      return
    }

    // 处理响应数据
    if (response.data.code === 1 || response.data.code === 200) {
      const newGoods = response.data.data?.records || response.data.data || []

      if (newGoods.length > 0) {
        // 如果是第一页，替换数据；否则追加数据
        if (currentPage.value === 1) {
          displayGoods.value = newGoods
        } else {
          displayGoods.value = [...displayGoods.value, ...newGoods]
        }

        // 如果返回的数据少于请求的数量，说明没有更多数据了
        if (newGoods.length < props.pageSize) {
          noMore.value = true
        }

        // 增加页码
        currentPage.value++
      } else {
        // 没有更多数据
        noMore.value = true
        allGoodsLoaded.value = true
      }
    } else {
      // API调用失败
      noMore.value = true
      allGoodsLoaded.value = true
    }
  } catch (error) {
    console.error('加载商品失败:', error)
    noMore.value = true
    allGoodsLoaded.value = true
  } finally {
    loading.value = false
  }
}

// 初始化加载数据
onMounted(() => {
  loadMore()
})
</script>

<style scoped>
.goods-wrapper {
  min-height: 300px;
}

/* 商品容器 - flex布局 */
.goods-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px; /* 商品间距 */
  padding: 12px; /* 容器内边距 */
}

/* 单个商品项样式 */
.goods-item {
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s;
  /* 设置商品项宽度为接近50%减去间距的一半 */
  flex: 0 1 calc(50% - 8px);
  min-width: 150px; /* 设置最小宽度 */
  min-height: 250px; /* 设置最小高度 */
}

.goods-item:hover {
  transform: translateY(-5px);
}

/* 商品链接 */
.goods-link {
  text-decoration: none;
  color: inherit;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 商品图片 */
.el-image {
  width: 100%;
  height: 150px;
}

/* 商品名称 */
.goods-name {
  padding: 12px;
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  flex-grow: 1;
}

/* 价格信息容器 */
.price-info {
  padding: 8px 12px 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 商品价格 */
.goods-price {
  font-size: 16px;
  font-weight: bold;
  color: #ff5000;
  margin: 0;
}

/* 原始价格 */
.original-price {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
  margin: 0;
}

/* 骨架屏样式 */
.skeleton-item {
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  flex: 0 1 calc(50% - 8px);
  min-width: 150px;
  min-height: 250px;
}

/* 没有数据提示 */
.no-data {
  padding: 40px 0;
  text-align: center;
}

/* 没有更多数据提示 */
.no-more {
  text-align: center;
  padding: 20px;
  color: #999;
  font-size: 14px;
}

/* 响应式设计 - 在小屏幕上调整布局 */
@media (max-width: 768px) {
  .goods-container {
    gap: 12px;
    padding: 8px;
  }

  .goods-item,
  .skeleton-item {
    flex: 0 1 calc(50% - 6px);
  }
}

@media (max-width: 480px) {
  .goods-item,
  .skeleton-item {
    flex: 0 1 100%;
  }
}
</style>
