//将身份转化为中文显示
import {ref} from "vue";

export function parseRole(role: string | null) {
    if (role === 'MANAGER') {
        return "管理员"
    } else if (role === 'CUSTOMER') {
        return "顾客"
    } else if (role === 'STAFF') {
        return "商家"
    } else if (role === 'CEO') {
        return "CEO"
    }
}

//将时间转化为日常方式
export function parseTime(time: string) {
    let times = time.split(/T|\./)
    return times[0] + " " + times[1]
}


//将商品品类转化为中文显示
export function parseCategory(category: string) {
    if (category === 'FOOD') {
        return "食品"
    } else if (category === 'CLOTHES') {
        return "服饰"
    } else if (category === 'FURNITURE') {
        return "家具"
    } else if (category === 'ELECTRONICS') {
        return "电子产品"
    } else if (category === 'ENTERTAINMENT') {
        return "娱乐"
    } else if (category === 'SPORTS') {
        return "体育产品"
    } else if (category === 'LUXURY') {
        return "奢侈品"
    }
}
export function parsePaymentState(type:string){
    if(type === "UNPAID"){
        return "待付款"
    }else if(type === "UNSEND"){
        return "待发货"
    }else if(type === "UNCOMMENT"){
        return"待评价"
    }else if(type === "UNGET"){
        return "待收货"
    }else if(type==="DONE"){
        return "已完成"
    }
}
//将订单类型转化为中文显示
export function parseOrderType(type: string) {
    if (type === "PICKUP") {
        return "到店自提"
    } else if (type === "DELIVERY") {
        return "快递到家"
    }
}
export function parseCouponType(type: string) {
    if (type === "FULL_REDUCTION") {
        return "满减券"
    } else if (type === "SPECIAL") {
        return "蓝鲸券"
    }
    return ''
}

export const subTypeList = ref([
    {name: 'VEGETABLES' , outerType: 'FOOD' , label: '蔬菜'},
    {name: 'FRUITS' , outerType: 'FOOD' , label: '水果'},
    {name: 'MEAT' , outerType: 'FOOD' , label: '肉类'},
    {name: 'SEAFOOD' , outerType: 'FOOD' , label: '海鲜'},
    {name: 'SNACKS' , outerType: 'FOOD' , label: '零食'},
    {name: 'DRINKS' , outerType: 'FOOD' , label: '饮品'},
    {name: 'TOPS' , outerType: 'CLOTHES' , label: '上衣'},
    {name: 'BOTTOMS' , outerType: 'CLOTHES' , label: '下衣'},
    {name: 'DRESSES' , outerType: 'CLOTHES' , label: '裙子'},
    {name: 'OUTWEAR' , outerType: 'CLOTHES' , label: '外套'},
    {name: 'SHOES' , outerType: 'CLOTHES' , label: '鞋子'},
    {name: 'ACCESSORIES' , outerType: 'CLOTHES' , label: '饰品'},
    {name: 'BEDS' , outerType: 'FURNITURE' , label: '床'},
    {name: 'TABLES' , outerType: 'FURNITURE' , label: '桌子'},
    {name: 'CHAIRS' , outerType: 'FURNITURE' , label: '椅子'},
    {name: 'SOFAS' , outerType: 'FURNITURE' , label: '沙发'},
    {name: 'CABINETS' , outerType: 'FURNITURE' , label: '橱柜'},
    {name: 'PHONES' , outerType: 'ELECTRONICS' , label: '手机'},
    {name: 'COMPUTERS' , outerType: 'ELECTRONICS' , label: '电脑'},
    {name: 'TVS' , outerType: 'ELECTRONICS' , label: '电视'},
    {name: 'CAMERAS' , outerType: 'ELECTRONICS' , label: '相机'},
    {name: 'HEADPHONES' , outerType: 'ELECTRONICS' , label: '耳机'},
    {name: 'BOOKS' , outerType: 'ENTERTAINMENT' , label: '书籍'},
    {name: 'MOVIES' , outerType: 'ENTERTAINMENT' , label: '影视'},
    {name: 'MUSIC' , outerType: 'ENTERTAINMENT' , label: '音乐'},
    {name: 'GAMES' , outerType: 'ENTERTAINMENT' , label: '游戏'},
    {name: 'FITNESS' , outerType: 'SPORTS' , label: '健身'},
    {name: 'YOGA' , outerType: 'SPORTS' , label: '瑜伽'},
    {name: 'RUNNING' , outerType: 'SPORTS' , label: '跑步'},
    {name: 'SWIMMING' , outerType: 'SPORTS' , label: '水上运动'},
    {name: 'JEWELRY' , outerType: 'LUXURY' , label: '珠宝'},
    {name: 'WATCHES' , outerType: 'LUXURY' , label: '手表'},
    {name: 'HANDBAGS' , outerType: 'LUXURY' , label: '箱包'},
    {name: 'OTHERS' , outerType: 'LUXURY' , label: '其它'},
])
