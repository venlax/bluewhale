import {axios} from '../utils/request'
import {ORDER_MODULE} from './_prefix'
enum deliveryMethodEnum{
    DELIVERY = '快递送达',
    PICKUP ='到店自提',
}
enum paymentStateEnum{
    UNPAID="待支付",
    UNSEND="待发货",
    UNGET="待收货",
    UNCOMMENT="待评价",
    DONE="已完成"
}
type CreateOrderInfo = {
    paymentState:paymentStateEnum,
    userId:number,
    storeId: number,
    commodityId: number,
    count : number,
    deliveryMethod: deliveryMethodEnum,
    price :number,
    deliveryAddress: string,
    deliveryPhone:string,
    pickDate:string
}
export const createOrder = (createOrderInfo : CreateOrderInfo) => {
    return axios.post(`${ORDER_MODULE}/createOrder`, createOrderInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
export const getOrderById = (orderId:number)=>{
    return axios.get(`${ORDER_MODULE}/getOrderById/${orderId}`)
        .then(res =>{
            return res
        })
}
export const getOrdersByStoreId = (storeId :number)=>{
    return axios.get(`${ORDER_MODULE}/getOrdersByStoreId/${storeId}`)
        .then(res =>{
            return res
        })
}

export const getOrdersByUserId = (userId :number)=>{
    return axios.get(`${ORDER_MODULE}/getOrdersByUserId/${userId}`)
        .then(res =>{
            return res
        })
}
export const getOrders = ()=>{
    return axios.get(`${ORDER_MODULE}/getOrders`)
        .then(res =>{
            return res
        })
}
export const updateOrderState = (orderId:number,state:paymentStateEnum)=>{
    const params = new URLSearchParams();
    params.append('orderId', orderId.toString());
    params.append('state', state.toString());
    return axios.post(`${ORDER_MODULE}/updateOrderState`,params,
        {headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}})
        .then(res =>{
            return res
        })
}
export const deleteOrder = (orderId:number)=>{
    const params = new URLSearchParams();
    params.append('orderId',orderId.toString());
    return axios.post(`${ORDER_MODULE}/deleteOrder`,params,
        {headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}})
        .then(res =>{
            return res
        })
}
// 订单支付（你可能需要修改这个方法，把优惠券加进来）
export const pay = (orderId: number,couponId:number) => {
    const params = new URLSearchParams();
    params.append('orderId',orderId.toString());
    if(couponId!=null) {
        params.append('couponId', couponId.toString());
    }
    return axios.post(`${ORDER_MODULE}/pay`,params,
        {headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}})
        .then(res => {
            document.write(res.data)
        })
}
export const getPrice = (orderId: number,couponId?:number)=>{
    const params = new URLSearchParams();
    params.append('orderId',orderId.toString());
    if(couponId != null){
        params.append('couponId',couponId.toString());
    }
    return axios.get(`${ORDER_MODULE}/getPrice${params.toString() ? '?' + params.toString() : ''}`)
        .then(res =>{
            return res
        })
}