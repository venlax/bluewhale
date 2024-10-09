import {axios} from '../utils/request'
import {COMMENT_MODULE} from './_prefix'

type CommentOrderInfo = {
    userId:number,
    commodityId: number,
    content:string,
    score:number,
    storeId: number,
    orderId:number,
}

export const commentOrder = (commentOrderInfo : CommentOrderInfo) => {
    return axios.post(`${COMMENT_MODULE}/commentOrder`, commentOrderInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
export const getCommentsByCommodityId = (commodityId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getCommentsByCommodityId/${commodityId}`)
        .then(res =>{
            return res
        })
}
export const getCommentsByUserId = (userId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getCommentsByUserId/${userId}`)
        .then(res =>{
            return res
        })
}
export const getCommentsByStoreId = (storeId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getCommentsByStoreId/${storeId}`)
        .then(res =>{
            return res
        })
}
export const getCommentByOrderId = (orderId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getCommentByOrderId/${orderId}`)
        .then(res =>{
            return res
        })
}
export const getScoreByStoreId = (storeId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getScoreByOrderId/${storeId}`)
        .then(res =>{
            return res
        })
}
export const getScoreByCommodityId = (commodityId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getScoreByCommodityId/${commodityId}`)
        .then(res =>{
            return res
        })
}
export const getCommentCountByCommodityId = (commodityId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getCommentCountByCommodityId/${commodityId}`)
        .then(res =>{
            return res
        })
}
export const getCommentCountByStoreId = (storeId:number)=>{
    return axios.get(`${COMMENT_MODULE}/getCommentCountByStoreId/${storeId}`)
        .then(res =>{
            return res
        })
}