<script setup lang="ts">
import {computed, ref} from "vue";
import {getOrderById, updateOrderState} from "../../api/order.ts";
import {getCommodityById} from "../../api/product.ts";
import {getStoreById} from "../../api/store.ts";
import {useRoute} from "vue-router";
import {commentOrder} from "../../api/comment.ts";
import {router} from "../../router";
const route = useRoute()
const createTime = ref()
const orderId = ref()
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
const pickDate = ref()
const price = ref()//总价
const storeAddress=ref()
const userId=ref()
orderId.value=route.params.orderId
getOrderById(orderId.value).then( res =>{
  orderId.value=res.data.result.id
  createTime.value=res.data.result.createTime
  storeId.value = res.data.result.storeId
  commodityId.value = res.data.result.commodityId
  count.value=res.data.result.count
  deliveryMethod.value=res.data.result.deliveryMethod
  deliveryAddress.value=res.data.result.deliveryAddress
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
})
const score=ref()
const content=ref('')
const hasScore=computed(()=>score.value!='')
const hasContent=computed(()=>content.value!='')
const handleCommentDisabled = computed(()=>{
  return!(hasScore.value&&hasContent.value)
})
function handleComment(){
  commentOrder({
    userId:userId.value,
    commodityId:commodityId.value,
    content:content.value,
    score:score.value,
    storeId:storeId.value,
    orderId:orderId.value
  }).then(res=>{
    if (res.data.code === '000'){
      paymentState.value="DONE"
      updateOrderState(
          orderId.value,
          paymentState.value
      ).then(res=>{
        if (res.data.code ==='400'){
          ElMessage({
            type: 'error',
            message: res.data.msg,
          })
        }
      })
      toOrderPage()
      ElMessageBox.alert(
          '感谢您的评价！',
          {
            type: "success",
          })
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
function toOrderPage(){
  router.push("/allOrder");
}
</script>

<template>
  <el-main>
    <h4>订单评价</h4>
    <el-card class="comment-card">
        <el-row :gutter="10">
        <el-col :span = "4">
          <el-image class="logo-image" :src="commodityPic"/>
        </el-col>
        <el-col :span = "2">
          <p class="main-text">{{commodityName}}</p>
        </el-col>
        <el-col :span = "3">
          <p class="main-text">单价：￥{{ singlePrice}}</p>
        </el-col>
          <el-col :span = "3">
            <p class="main-text">实付款：￥{{ price }}</p>
          </el-col>
        </el-row>
        <p class="header-text">您于{{createTime}} 购买此商品{{count}}件</p>
      <h5>我们想听听您最真实的评价！</h5>
      <el-form>
        <el-form-item label="评分">
          <div class="comment-text">
            <el-rate
                v-model="score"
                show-text
            />
          </div>
        </el-form-item>
        <el-form-item label="评价">
          <el-input v-model="content"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click.prevent="handleComment"
                     :disabled="handleCommentDisabled">
            确定
          </el-button>
          <el-button @click.prevent="toOrderPage">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </el-main>
</template>

<style scoped>
.header-text{
  font-size: 12px;
  line-height: 2px;
  margin: 5px;
  color: #666666;
}
.main-text{
  font-size: 0.9em; /* 字体大小 */
}
.logo-image{
  width: 190px;
}

</style>