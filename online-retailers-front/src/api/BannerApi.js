import instance from '@/utils/request'

// 获取所有的轮播图列表
export const getAllBannerList = () => {
    return instance({
        url: '/product/banner/status/1',
        method: 'GET'
    })
}
