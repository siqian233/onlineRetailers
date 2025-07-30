import instance from '@/utils/request'

// 获取首页中显示的所有的有效的类别图列表
export const getAllCategoryList = () => {
    return instance({
        url: '/product/category/all',
        method: 'GET'
    })
}