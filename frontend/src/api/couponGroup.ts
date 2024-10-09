import {axios} from '../utils/request'
import {COUPON_MODULE} from './_prefix'
enum CouponTypeEnum{
    FULL_REDUCTION= "满减券",
    SPECIAL = "蓝鲸券",
}
type CreateCouponInfo = {
    couponType:CouponTypeEnum,
    expirationTime:string,//到期时间
    threshold:number,// 满减券的满额条件, 如果为蓝鲸券则设置为null
    reduction:number,// 满减券的减额, 如果为蓝鲸券则设置为null
}
export const  distributeCouponGroup = (storeId:number,amount:number,createCouponInfo:CreateCouponInfo) => {
    if(storeId==null){
        return axios.post(`${COUPON_MODULE}/distributeCouponGroup?amount=${amount}`,createCouponInfo,
            {headers: {'Content-Type': 'application/json'}})
            .then(res => {
                return res
            })
    }
    return axios.post(`${COUPON_MODULE}/distributeCouponGroup?storeId=${storeId}&amount=${amount}`,createCouponInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
export const getCouponGroups = ()=>{
    return axios.get(`${COUPON_MODULE}/getCouponGroup`)
        .then(res =>{
            return res
        })
}
export const getCouponGroupsByGroupId = (groupId:number)=>{
    return axios.get(`${COUPON_MODULE}/getCouponByGroupId/${groupId}`)
        .then(res =>{
            return res
        })
}
export const getCouponsByUserId = (userId:number)=>{
    return axios.get(`${COUPON_MODULE}/getCouponByUserId/${userId}`)
        .then(res =>{
            return res
        })
}
export const redeemCoupon = (groupId:number)=>{
    return axios.get(`${COUPON_MODULE}/redeemCoupon/${groupId}`)
        .then(res =>{
            return res
        })
}