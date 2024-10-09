<script setup lang="ts">
//这个文件用来写 展示所有订单
import {computed, ref} from 'vue'
import {Tickets,CreditCard,Box,Van,ChatDotRound,Finished,Delete} from "@element-plus/icons-vue";
import {getOrders, getOrdersByStoreId, getOrdersByUserId} from "../../api/order.ts";
import OrderItem from "../../components/OrderItem.vue";
import {generateStatement, userInfo} from "../../api/user.ts";
const userId=ref()
const storeId=ref()
interface orderVO{
  paymentState:string
  id:number
  userId:number
  storeId:number
}
const orderList = ref<orderVO[]>([])
const role = sessionStorage.getItem("role")
if ((role =="MANAGER") || (role=="CEO")){
  getOrders().then(res=>{
    orderList.value=res.data.result
  })
}
if (role=="CUSTOMER"){
  userInfo().then(res=>{
    userId.value=res.data.result.id
    getOrdersByUserId(userId.value).then(res=>{
      orderList.value=res.data.result
    })
  })
}
if (role=="STAFF"){
  userInfo().then(res=>{
    storeId.value=res.data.result.storeId
    getOrdersByStoreId(storeId.value).then(res=>{
      orderList.value=res.data.result
    })
  })
}
const filterType = ref('ALL')//筛选器种类，它将决定对被渲染的订单进行何种筛选
const handleTabClick=(tab:any)=>{
  filterType.value=tab.name
}
const filteredOrder = computed(() => {
  switch(filterType.value){
    case 'ALL':
      return orderList.value;
    default:
      return orderList.value.filter(orderVO => orderVO.paymentState === filterType.value);
  }
});//根据被查看的订单类型不同，对订单进行不同的筛选

function handleGenerateState(){
  generateStatement().then(res=>{
    if (res.data.code === '000'){
      ElMessageBox.alert(
          '生成报表成功',
          {
            type: "success",
          })
      window.location.href = res.data.result;
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}

</script>
<template>
  <el-header style="margin-top: 15px">
    <el-tabs v-model="filterType"
               class="demo-tabs"
               @tab-click="handleTabClick">
        <el-tab-pane label="所有订单" name="ALL">
          <template #label>
            <el-icon size="25" color="Black" ><Tickets /></el-icon>
            <span>所有订单</span>
          </template>
          </el-tab-pane>
        <el-tab-pane label="待付款" name="UNPAID">
          <template #label>
            <el-icon size="25" color="Black" ><CreditCard /></el-icon>
            <span>待付款</span>
          </template>
          </el-tab-pane>
        <el-tab-pane label="待发货" name="UNSEND">
          <template #label>
            <el-icon size="25" color="Black" ><Box /></el-icon>
            <span>待发货</span>
          </template>
          </el-tab-pane>
        <el-tab-pane label="待收货" name="UNGET">
          <template #label>
            <el-icon size="25" color="Black" ><Van /></el-icon>
            <span>待收货</span>
          </template>
        </el-tab-pane>
        <el-tab-pane label="待评价" name="UNCOMMENT">
          <template #label>
            <el-icon size="25" color="Black" ><ChatDotRound /></el-icon>
            <span>待评价</span>
          </template>
          </el-tab-pane>
        <el-tab-pane label="已完成" name="DONE">
          <template #label>
            <el-icon size="25" color="Black" ><Finished /></el-icon>
            <span>已完成</span>
          </template>
          </el-tab-pane>
        <el-tab-pane label="已取消" name="CANCELLED">
          <template #label>
            <el-icon size="25" color="Black" ><Delete /></el-icon>
            <span>已取消</span>
          </template>
          </el-tab-pane>
      </el-tabs>

  </el-header>

  <el-main>
    <el-button v-if="role==='CEO' || role==='STAFF'"
               @click.prevent="handleGenerateState"
               type="primary" plain >下载订单报表</el-button>
    <div class="order-item-list" v-if="filteredOrder.length>0">
      <OrderItem v-for="orderVO in filteredOrder"
                 :orderId="orderVO.id"
                 :storeId="orderVO.storeId"
                 :userId="orderVO.userId"/>
    </div>
    <div class="no-order-tip" v-else>
      <el-empty description="没有符合条件的订单,请尝试其他搜索条件"></el-empty>
    </div>
  </el-main>
</template>

<style scoped>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
.order-item-list{
}
</style>