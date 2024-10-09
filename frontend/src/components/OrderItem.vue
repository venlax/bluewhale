<script setup lang="ts">
//这个用来设计 订单组件
import {ref} from "vue";
import {getOrderById, updateOrderState} from "../api/order.ts";
import {getCommodityById} from "../api/product.ts";
import {getStoreById} from "../api/store.ts";
import {deleteOrder} from "../api/order.ts";
import {router} from "../router";
import PayDialog from "./PayDialog.vue";
import {parsePaymentState, parseTime} from "../utils";
import {getNameById} from "../api/user.ts";

const role = sessionStorage.getItem("role")
const props = defineProps({
  orderId:{
    type:Number,
    required:true
  },
  storeId:{
    type:Number,
    required:true
  },
  userId:{
    type:Number,
    required:true
  }

})

const dialogOfDeleteOrder = ref(false)
const dialogOfSend = ref(false)
const dialogOfGet = ref(false)
const createTime = ref()
const orderId = ref(0)
const storeId = ref()
const storeName=ref()
const commodityId = ref()
const commodityPic = ref('')
const commodityName = ref('')
const singlePrice = ref('')//商品单价
const count = ref()
const deliveryMethod = ref()
const paymentState = ref()
const deliveryAddress = ref()
const deliveryPhone= ref()
const pickDate = ref()
const price = ref()//总价
const storeAddress=ref()
const userId=ref()
const userName=ref()
getOrderDetail()
function getOrderDetail(){
  getOrderById(props.orderId).then( res =>{
    orderId.value=res.data.result.id
    createTime.value=parseTime(res.data.result.createTime)
    storeId.value = res.data.result.storeId
    commodityId.value = res.data.result.commodityId
    count.value=res.data.result.count
    deliveryMethod.value=res.data.result.deliveryMethod
    deliveryAddress.value=res.data.result.deliveryAddress
    deliveryPhone.value=res.data.result.deliveryPhone
    paymentState.value=res.data.result.paymentState
    pickDate.value=res.data.result.pickDate
    price.value=res.data.result.price
    userId.value=res.data.result.userId
    getCommodityById(commodityId.value).then(res =>{
      commodityName.value=res.data.result.name
      singlePrice.value=res.data.result.price
      commodityPic.value=res.data.result.picLink[0]
    })
    getStoreById(storeId.value).then(res=>{
      storeName.value=res.data.result.name
      storeAddress.value=res.data.result.address
    })
    getNameById(userId.value).then(res=>{
      userName.value=res.data.result
      console.log(userName.value)
    })
  })
}
function handleSend(){
  paymentState.value = "UNGET"
  updateOrderState(
      orderId.value,
      paymentState.value
  ).then(res=>{
    if (res.data.code === '000'){
      closeHandleSendDialog()
      ElMessageBox.alert(
          '发货成功',
          {
            type: "success",
          })
      getOrderDetail()
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
function handleGet(){
  paymentState.value="UNCOMMENT"
  updateOrderState(
      orderId.value,
      paymentState.value
  ).then(res=>{
    if (res.data.code === '000'){
      ElMessageBox.alert(
          '确认收货成功',
          {
            type: "success",
          })
      closeHandleGetDialog()
      getOrderDetail()
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })

}
const dialogRef = ref()
function handlePay() {
  //触发支付订单弹窗，传入当前orderId
  dialogRef.value.open(props.orderId)
}
//注册回调，用于更新当前订单详情（如果不用这个，可能只能强制刷新整个界面了，用户体验不好）
function handleConfirmOrder(success: boolean) {
  if (success) {
    getOrderDetail()
  }
}
function orderDelete(){
  deleteOrder(
      orderId.value
  ).then(res=>{
    if (res.data.code === '000'){
      ElMessageBox.alert(
          '取消订单成功',
          {
            type: "success",
          })
      closeDeleteOrderDialog()
      getOrderDetail()
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
function displayDeleteOrderDialog(){
  dialogOfDeleteOrder.value = true;
}
function closeDeleteOrderDialog(){
  dialogOfDeleteOrder.value = false;
}
function displayHandleSendDialog(){
  dialogOfSend.value = true;
}
function closeHandleSendDialog(){
  dialogOfSend.value = false;
}
function displayHandleGetDialog(){
  dialogOfGet.value = true;
}
function closeHandleGetDialog(){
  dialogOfGet.value = false;
}
function toCommentPage(orderId:any) {
  router.push("/commentOrder/" + orderId.toString());
}
</script>

<template>
  <el-card class = "order-item-card" :body-style="{padding:'12px'}" shadow="hover">
    <el-container>
      <el-header class="header-style">
        <el-row class="row-style" :gutter="10" >
          <el-col :span = "5">
            <p class="header-text">用户：{{userName}}</p>
          </el-col>
          <el-col :span = "5">
            <p class="header-text">订单号：{{ orderId }}</p>
          </el-col>
          <el-col :span = "5">
          <p class="header-text">店铺：{{ storeName }}</p>
          </el-col>
          <el-col :span = "5">
            <p class="header-text">创建时间：{{ createTime }}</p>
          </el-col>
        </el-row>
        <el-row class="row-style" :gutter="10">

          <el-col v-if="deliveryMethod==='PICKUP'" :span = "5">
            <p class="header-text">提货方式：到店自提</p>
          </el-col>
          <el-col v-if="deliveryMethod==='DELIVERY'" :span = "5">
            <p class="header-text">提货方式：快递送达</p>
          </el-col>
          <el-col v-if="deliveryMethod==='PICKUP'" :span = "5">
            <p class="header-text">自提地址：{{ storeAddress }}</p>
          </el-col>
          <el-col v-if="deliveryMethod==='PICKUP'" :span = "5">
            <p class="header-text">自提日期：{{ parseTime(pickDate) }}</p>
          </el-col>
          <el-col v-if="deliveryMethod==='DELIVERY'" :span = "5">
            <p class="header-text">收货地址：{{ deliveryAddress }}</p>
          </el-col>
          <el-col v-if="deliveryMethod==='DELIVERY'" :span = "5">
            <p class="header-text">联系电话：{{ deliveryPhone }}</p>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-row :gutter="10">
          <el-col :span = "3">
            <el-image class="logo-image" :src="commodityPic"/>
          </el-col>
          <el-col  :span = "4" class="state-type">
            <el-tag  :type="'danger'"
                     style="padding: 5px 10px;font-size: 15px;"
                     disable-transitions>{{commodityName}}</el-tag>
          </el-col>
          <el-col :span = "2">
            <p class="main-text">数量：{{count}}件</p>
          </el-col>
          <el-col :span = "3">
            <p class="main-text">单价：￥{{singlePrice}}</p>
          </el-col>
          <el-col :span = "3">
            <p class="main-text">付款:￥{{ price }}</p>
          </el-col>
          <el-col  :span = "1" class="state-type">
            <el-tag v-if="paymentState != 'CANCELLED'"
                    style="padding: 5px 10px;font-size: 15px;"
                    disable-transitions>{{parsePaymentState(paymentState)}}</el-tag>
            <el-tag v-else
                    type="info"
                    style="padding: 5px 10px;font-size: 15px;"
                    disable-transitions>{{parsePaymentState(paymentState)}}</el-tag>
          </el-col>
          <!--对订单进行操作：-->
          <el-col :span="3"></el-col>
          <el-col v-if="role==='STAFF'" :span="4">
                <el-button v-if="paymentState==='UNSEND'"
                    @click.prevent="displayHandleSendDialog"
                           type="primary">
                  去发货
                </el-button>
            <el-dialog title="发货"
                       v-model="dialogOfSend"
                       @close="closeHandleSendDialog">
              <h2>确认发货？</h2>
              <span class="button-group">
                <el-button @click.prevent="handleSend" type="primary">
                确认
                </el-button>
                <el-button @click.prevent="closeHandleSendDialog">
                取消
              </el-button></span>
            </el-dialog>
          </el-col>
          <el-col v-if="role==='CUSTOMER'" :span="4">
            <el-button v-if="paymentState==='UNGET'"
                       @click.prevent="displayHandleGetDialog"
                       type="primary">
              确认收货
            </el-button>
            <el-button v-if="paymentState==='UNCOMMENT'"
                       @click.prevent="toCommentPage(orderId)"
                       type="primary">
              去评价
            </el-button>
            <span class="button-group" v-if="paymentState==='UNPAID'">
              <el-button @click="handlePay"
                            type="primary">
                去付款
              </el-button>
              <el-button @click="displayDeleteOrderDialog">
                取消订单
              </el-button>
            </span>
            <el-dialog title="确认收货"
                       v-model="dialogOfGet"
                       @close="closeHandleGetDialog">
              <h2>确认已收到商品？</h2>
              <span class="button-group">
                <el-button @click.prevent="handleGet" type="primary">
                  确认
                </el-button>
                <el-button @click.prevent="closeHandleGetDialog">
                  取消
                </el-button>
              </span>
            </el-dialog>
            <el-dialog title="取消订单"
                       v-model="dialogOfDeleteOrder"
                       @close="closeDeleteOrderDialog">
              <h2>确认取消订单？</h2>
              <span class="button-group">
                <el-button @click.prevent="orderDelete" type="primary">
                  确认
                </el-button>
                <el-button @click.prevent="closeDeleteOrderDialog">
                  取消
                </el-button>
              </span>
            </el-dialog>
            <PayDialog ref="dialogRef" @operation-finish="handleConfirmOrder"/>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-card>

</template>

<style scoped>
.order-item-card{
  margin-top: 5px;
  border: 1px solid rgba(150, 173, 230, 0.71);
}
.header-style {
  height:auto;
  display: flex;
  flex-direction: column;
  border-bottom: lightgrey solid 1px;
}
.header-text{
  font-size: 12px;
  line-height: 2px;
  margin: 5px;
  color: #666666;
}
.main-text{
  font-size: 0.9em; /* 字体大小 */
  color: #575757;
}
.row-style {
  /* 设置每行的样式 */
  display: flex;
  align-items: center; /* 列内容垂直居中 */
  margin-bottom: 10px; /* 行与行之间的间距 */
}
.logo-image{
  height: 75px;
}
.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 20px;
  align-items: center;
  justify-content: right;
}
.state-type{
  margin-top: 10px;
}
</style>