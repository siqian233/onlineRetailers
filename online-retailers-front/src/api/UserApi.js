import request from '@/utils/request' // 引入request.js

// 登录的接口
export const loginAPI = (data) => {
    return request({
        url: '/user/auth/login',
        method: 'POST',
        data
    })
}

// 注册
export const registerAPI = (data) => {
    return request({
        url: '/user/auth/register',
        method: 'POST',
        data
    })
}
