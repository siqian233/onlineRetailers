import instance from '@/utils/request'

// 获取按分页查找所有的特价商品列表
export const getAllDealProductList = (current, size) => {
    return instance({
        url: '/product/product/special-offers',
        method: 'GET',
        params: {
            current,
            size
        }
    })
}

// 根据类别id 获取此类别下 current项，每页size条 产品数据，按产品id降序排序
export const getProductListByCategoryId = (categoryId, current, size) => {
    return instance({
        url: '/product/product/page?productStatus=1',
        method: 'GET',
        params: {
            categoryId,
            current,
            size
        }
    })
}

