import {ElMessage} from "element-plus";

// 获取轮播图（状态为1）
export async function getBannerList() {
    try {
        const response = await fetch('/product/banner/status/1');
        const data = await response.json();
        if(data.code === 1){
            return data.data;
        }else{
            ElMessage.error('轮播图请求失败');
        }
    }catch ( error){
        ElMessage.error('轮播图请求失败');
    }
}

// 获取推荐分类
export async function getCategoryList() {
    try {
        const response = await fetch('/product/category/all');
        const data = await response.json();
        if(data.code === 1){
            return data.data;
        }
        else{
            ElMessage.error('商品分类请求失败');
        }
    }catch ( error){
        ElMessage.error('商品分类请求失败');
    }
}
