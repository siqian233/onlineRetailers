import request from '@/utils/request' // 引入request.js

// 登录的接口
export const loginAPI = (data) => {
    return request({
        url: '/auth/login',
        method: 'POST',
        data
    })
}

// 注册
export const registerAPI = (data) => {
    return request({
        url: '/auth/register',
        method: 'POST',
        data
    })
}

// 检查用户名是否存在
export const checkUsernameAPI = (username) => {
    return request({
        url: `/auth/check-username?username=${username}`,
        method: 'GET'
    })
}
