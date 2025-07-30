// 封装axios请求模块: 1.请求头携带token     2.响应拦截器处理错误     3.区分模拟端还是真实服务器端, 摘取核心响应数据    4.处理401错误
import { useUserStore } from '@/stores/user' // 引入用户仓库

import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'
const baseURL = 'http://localhost:20001' // 定义服务端基地址, 将来上线后, 会根据环境变量来切换基地址

// 创建axios实例
const instance = axios.create({
    baseURL,
    timeout: 10000
})

// axios的请求拦截器, 查看本地（浏览器的localStorage是否有token), 如果有，则每个请求携带token
instance.interceptors.request.use(
    (config) => {
        const userStore = useUserStore()
        // // 如果用户仓库中有token, 就携带token
        if (userStore.token) {
            // 请求头中用  Authorization 字段表示token
            config.headers.Authorization = userStore.token
            config.headers.token = userStore.token
        }
        return config
    },
    (err) => Promise.reject(err)
)
// 响应拦截器，因为这是个前端项目，有可能还没有开发后端服务，所以我们需要模拟数据，所以我们需要判断一下，如果是模拟数据，就不需要处理响应拦截器，
// 否则会报错。
instance.interceptors.response.use(
    (res) => {
        // 如果请求url中含有 /imitate 地址，则表示是处理模拟数据的情况
        // 直接返回响应数据,
        if (res.config.url.includes('/imitate/')) {
            return res
        }
        // 处理正常服务端的响应数据
        if (res.data.code === 200 || res.data.code === 1) {
            // console.log("这是request.js中的响应数据:" + res.data.data);
            // console.log(res.data.data);
            return res
        }
        // 处理业务失败，给出错误提示，抛出错误.
        ElMessage({ message: res.data.msg || '服务异常', type: 'error' })
        return Promise.reject(res.data)
    },
    (err) => {
        // 错误默认情况
        ElMessage({
            message: err.message + '服务异常',
            type: 'error'
        })
        console.log(err)
        // 错误特殊情况 => 401 权限不足 或 token 过期 => 拦截到登录
        if (err.response?.status === 401) {
            router.push('/login')   // 如果没有登录，则通过路由到 login
        }
        return Promise.reject(err)
    }
)

export default instance
export { baseURL }
