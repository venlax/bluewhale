// Lab2新增
// 开发时请解除3-4行的注释
import {axios} from '../utils/request'
import {COMMODITY_MODULE} from './_prefix'
enum TypeEnum{
    FOOD = '食品',
    CLOTHES ='服饰',
    FURNITURE='家具',
    ELECTRONICS='电子产品',
    ENTERTAINMENT='娱乐',
    SPORTS='体育产品',
    LUXURY='奢侈品'
}
enum SubTypeEnum{
    VEGETABLES = '蔬菜', FRUITS = '水果', MEAT = '肉类', SEAFOOD = '海鲜', SNACKS = '零食', DRINKS = '饮品',  // FOOD

    TOPS = '上装', BOTTOMS = '下装', DRESSES = '裙子', OUTERWEAR = '外套', SHOES = ' 鞋', ACCESSORIES = '饰品',  // CLOTHES

    BEDS = '床', SOFAS = '沙发', TABLES = '桌子', CHAIRS = '椅子', CABINETS = '橱柜',   // FURNITURE

    PHONES = '手机', COMPUTERS = '电脑', TVS = '电视', CAMERAS = '相机', HEADPHONES = '耳机',  // ELECTRONICS

    BOOKS = '书籍', MOVIES = '影碟', MUSIC = '音乐', GAMES = '游戏',  // ENTERTAINMENT

    FITNESS = '健美', YOGA = '瑜伽', RUNNING = '跑步', SWIMMING = '水上运动',   // SPORTS

    JEWELRY = '珠宝', WATCHES = '手表', HANDBAGS = '箱包', OTHERS = '其它'  // LUXURY
}





type CreateCommodityInfo = {
    storeId : number,
    name: string,
    type: TypeEnum,
    subType:SubTypeEnum,
    inventory : number,
    picLink:string[],
    price :number,
    description:string,
}
export const createCommodity = (createCommodityInfo : CreateCommodityInfo) => {
    return axios.post(`${COMMODITY_MODULE}/createCommodity`, createCommodityInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
export const getAllCommodityInfo = (storeId:number) => {
    return axios.get(`${COMMODITY_MODULE}/?storeId=${storeId}`)
        .then(res=>{
            return res
        })
}
//更新库存
export const updateInventoryInfo = (storeId:number,id:number,newInventory:number)=>{
    const params = new URLSearchParams();
    params.append('storeId', storeId.toString());
    params.append('id', id.toString());
    params.append('newInventory', newInventory.toString());
    return axios.post(`${COMMODITY_MODULE}/updateInventory`,params,
        {headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}})
        .then(res =>{
            return res
        })
}
export const getCommodityById = (id :number)=>{
    return axios.get(`${COMMODITY_MODULE}/${id}`)
        .then(res =>{
            return res
        })
}
export const deleteCommodity = (storeId:number,commodityId:number)=>{
    const params = new URLSearchParams();
    params.append('storeId',storeId.toString());
    params.append('commodityId',commodityId.toString());
    return axios.post(`${COMMODITY_MODULE}/delete`,params,
        {headers:{ 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}})
        .then(res =>{
            return res
        })
}
export const updateCommodity = (updateCommodityInfo:CreateCommodityInfo) => {
    return axios.post(`${COMMODITY_MODULE}/update}`,updateCommodityInfo,
        {headers: {'Content-Type': 'application/json'}})
        .then(res => {
            return res
        })
}
//搜索
export const searchCommodity = (keyword:string,type:string,low:number,high:number)=>{
    const params = new URLSearchParams();
    if(keyword!=null){
        params.append('keyword', keyword.toString());
    }
    if(type!=null){
        params.append('type', type.toString());
    }
    if(low==null){
        params.append('low', String(0));
    }else {
        params.append('low', low.toString());
    }
    if(high!=null){
        params.append('high', high.toString());
    }
    return axios.post(`${COMMODITY_MODULE}/search`,params,
        {headers: { 'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'}})
        .then(res =>{
            return res
        })
}