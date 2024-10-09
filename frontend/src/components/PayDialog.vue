<script setup lang="ts">
import {ref} from "vue"
import {getOrderById, getPrice, pay} from "../api/order.ts"
import {parseCouponType, parseOrderType, parseTime} from "../utils"
import {getCommodityById} from "../api/product.ts";
import {getCouponsByUserId} from "../api/couponGroup.ts";
import {userInfo} from "../api/user.ts";
import {getStoreById} from "../api/store.ts";

const saveOrderId = ref(0)
const orderDialogVisible = ref()
const emit = defineEmits(['operationFinish'])

const commodityId = ref()
const count = ref(0)
const deliveryMethod = ref('')
const totalPrice = ref(0)
const commodityName = ref('')
const storeId=ref()
const userId=ref()
const myCoupons=ref()
const storeName=ref()
const selectedCoupon=ref()
const usableCoupons=ref()
const commodityPrice=ref()
const finalPrice=ref()
const useCoupon=ref()
//通过父组件传来的orderId获取订单详情
function open(orderId: number) {
  //存储当前订单orderId
  saveOrderId.value = orderId
  selectedCoupon.value=''
  finalPrice.value=''
  useCoupon.value=''
  getOrderDetail(orderId)
  orderDialogVisible.value = true

}
defineExpose({
  open
})

function getOrderDetail(orderId: number) {
  getOrderById(orderId).then(res => {
    count.value = res.data.result.count
    deliveryMethod.value = res.data.result.deliveryMethod
    commodityId.value=res.data.result.commodityId
    finalPrice.value = res.data.result.price
    storeId.value=res.data.result.storeId
    getCommodityById(commodityId.value).then(res=>{
      commodityName.value=res.data.result.name
      commodityPrice.value=res.data.result.price
      totalPrice.value=count.value * commodityPrice.value
    })
    getStoreById(storeId.value).then(res=>{
      storeName.value=res.data.result.name
      userInfo().then(res => {
        userId.value = res.data.result.id
        getCouponsByUserId(userId.value).then(res => {
          myCoupons.value = res.data.result
          usableCoupons.value=myCoupons.value.filter(
              item => ((!item.storeName) || (item.storeName==storeName.value) )&&(!item.isUsed))
        })
      })
    })
  })
}
function handleRowClick(row: any) {
  selectedCoupon.value=row.id
  getPrice(saveOrderId.value,selectedCoupon.value).then(res=>{
    finalPrice.value=res.data.result
  })
}
function handleConfirmOrder() {
  pay(saveOrderId.value,selectedCoupon.value).then(res => {
    orderDialogVisible.value = false
  })
}
function getSelectedIndex(couponId:number) {
  const index = this.usableCoupons.findIndex(coupon => coupon.id === couponId);
  return index !== -1 ? index + 1 : null; // 返回编号（从1开始），如果未找到则返回null
}
function formatterCouponType(row:any){
  return parseCouponType(row.couponType)
}
function formatterTime(row:any){
  return parseTime(row.expirationTime)
}
function handleUseChange(){
  console.log(useCoupon.value)
  if (useCoupon.value==='false'){
    selectedCoupon.value='';
  }
  getPrice(saveOrderId.value, selectedCoupon.value ? selectedCoupon.value : null).then(res => {
    finalPrice.value = res.data.result;
  })
}
</script>


<template>
  <el-dialog v-model="orderDialogVisible" style="width: 900px">
    <el-row>
      <span class="pay-dialog-title">订单支付</span>
    </el-row>
    <div>
      <el-form >
        <el-form-item>
          <label for="commodityName">购买商品名称：</label>
          {{ commodityName }}
        </el-form-item>
        <el-form-item>
          <label for="count">购买数量：</label>
          {{ count }} 件
        </el-form-item>
        <el-form-item>
          <label for="type">提货方式：</label>
          {{ parseOrderType(deliveryMethod) }}
        </el-form-item>
        <el-form-item>
          <label for="totalPrice">优惠前总价：</label>
          ￥{{ totalPrice }}
        </el-form-item>
        <!-- 单选项来切换优惠券表格的显示 -->
        <el-form-item label="是否使用优惠券：" @change="handleUseChange">
          <el-radio-group v-model="useCoupon">
            <el-radio label="true">使用</el-radio>
            <el-radio label="false">不使用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item  v-if="useCoupon == 'true'" label="选择可用优惠券：">
          <el-table
              :data="usableCoupons"
              :header-cell-style="{ /* ... */ }"
              @row-click="handleRowClick"
              border
              :table-layout="auto"
              :cell-style="{'text-align':'center' }"
              max-height="40.5vh"
              ref="table"
              :row-key="getRowKey"
              highlight-current-row
          >
            <el-table-column
                label="ID"
                width="50"
            >
              <template v-slot="scope">
                {{ scope.$index +1 }} <!-- 使用计算属性中的编号 -->
              </template>
            </el-table-column>
            <el-table-column
                prop="couponType"
                label="类型"
                width="100"
            >
              <template v-slot="scope">
                <el-tag
                    :type="scope.row.couponType === 'SPECIAL' ? 'primary' : 'danger'"
                    disable-transitions>
                  {{ formatterCouponType(scope.row)}}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="couponType" label="详情" width="200">
              <template v-slot="scope">
                <div v-if="scope.row.couponType === 'SPECIAL'">
                  0-100元区间打九五折；
                  100-200元区间打九折；
                  200-300元区间打八五折；
                  300-400元区间打八折；
                  400-500元区间打七五折；
                  500元以上区间打七折
                </div>
                <div v-else>
                  满{{scope.row.threshold}}元减{{scope.row.reduction}}元
                </div>
              </template>
            </el-table-column>
            <el-table-column label="可用商店" width="150">
              <template v-slot="scope">
                <div>
                  <el-tag v-if="scope.row.storeName!=null"
                          :type="'danger'"
                          disable-transitions>
                    {{scope.row.storeName}}
                  </el-tag>
                  <el-tag v-else :type="'warning'">
                    所有商店
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="expirationTime" label="有效期截止时间" :formatter="formatterTime">
            </el-table-column>
          </el-table>
          <div v-if="selectedCoupon">
            当前选中的优惠券编号: {{ getSelectedIndex(selectedCoupon) }}
          </div>
        </el-form-item>
        <!-- 显示折扣后的总价 -->
        <el-form-item>
          <label for="finalPrice">总价：</label>
          ￥{{ finalPrice }}
        </el-form-item>
      </el-form>
      <el-button @click="handleConfirmOrder" type="primary" plain>确认支付</el-button>
    </div>
  </el-dialog>
</template>


<style scoped>
.pay-dialog-title {
  font-size: 30px;
  margin-bottom: 20px;
}
.el-table__body tr.current-row>td {
  background: #BDDBBB !important;
}

</style>