// Lab2新增
import {axios} from '../utils/request'
import {STORE_MODULE} from './_prefix'
type CreateStoreInfo = {
    name: string,
    logo: string,
    introduction :string,
    address:string
}
type UpdateStoreInfo={
    name:string,
    introduction :string,
    id :number
    address :string
}
export const createStore = (createStoreInfo : CreateStoreInfo) => {
    return axios.post(`${STORE_MODULE}/createStore`, createStoreInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
export const getAllStoresInfo = () => {
    return axios.get(`${STORE_MODULE}`)
        .then(res=>{
            return res
        })
}
//根据商店Id获取指定商店
export const getStoreById = (storeId :number)=>{
    return axios.get(`${STORE_MODULE}/${storeId}`)
        .then(res =>{
        return res
    })
}
export const deleteStore = (storeId :number) => {
    return axios.post(`${STORE_MODULE}/delete/${storeId}`,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
//更新商店信息
export const updateStore = (updateStoreInfo:UpdateStoreInfo) => {
    return axios.post(`${STORE_MODULE}/update`,updateStoreInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}