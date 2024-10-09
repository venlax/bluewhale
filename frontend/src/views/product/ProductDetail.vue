<!--Lab2新增-商品详情界面-->
<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {computed, ref, watch} from "vue";
import {deleteCommodity, getCommodityById} from "../../api/product.ts";
import {getStoreById} from "../../api/store.ts";
import {userInfo} from "../../api/user.ts";
import {createOrder} from "../../api/order.ts";
import {getCommentCountByCommodityId, getCommentsByCommodityId} from "../../api/comment.ts";
import CommentItem from "../../components/CommentItem.vue";
import PayDialog from "../../components/PayDialog.vue";
import {parseCategory,subTypeList} from "../../utils";
const role = sessionStorage.getItem("role")
const route = useRoute()
const router = useRouter()
const commodityId=ref()
const type=ref()
const name=ref()
const inventory=ref()
const picLink=ref([])
const description=ref()
const price=ref()
const rating = ref()
const currentImage=ref()
const userAddress = ref()
// 当前图片索引
const currentIndex=ref(0)
const storeId=ref()
const subType=ref()
commodityId.value = route.params.commodityId
storeId.value=route.params.storeId
const storeAddress = ref('')
// 初始化图片显示
currentImage.value = picLink.value[currentIndex.value];
function nextImage() {
  currentIndex.value = (currentIndex.value+ 1) % picLink.value.length; // 循环索引
  currentImage.value = picLink.value[currentIndex.value]; // 更新图片源
}
const commodityScoreNum = ref(0)
const commentList = ref()
function getCommodityInfoById(){
  getCommodityById(commodityId.value).then(res =>{
    type.value=res.data.result.type
    name.value=res.data.result.name
    inventory.value=res.data.result.inventory
    picLink.value=res.data.result.picLink
    description.value=res.data.result.description
    price.value=res.data.result.price
    rating.value=res.data.result.score
    if(rating.value==null){
      rating.value=0
    }

    currentImage.value=picLink.value[currentIndex.value]
    storeId.value=res.data.result.storeId
    orderPrice.value=res.data.result.price
    subType.value=res.data.result.subType
    getStoreById(storeId.value).then(res =>{
      storeAddress.value=res.data.result.address
      storeName.value = res.data.result.name
      logoUrl.value=res.data.result.logo
      storeRating.value = res.data.result.score

      introduction.value = res.data.result.introduction
    })
    getCommentCountByCommodityId(commodityId.value).then(res=>{
      commodityScoreNum.value=res.data.result
    })
    getCommentsByCommodityId(commodityId.value).then(res=>{
      commentList.value=res.data.result
    })
  })
}
getCommodityInfoById()
const storeName = ref('')
const logoUrl = ref('')
const storeRating = ref(0)
const introduction = ref('')
function toStoreDetailPage(storeId:number){
  router.push("/storeDetail/"+storeId)
}
const deliveryInfos = ref()
const storeID=ref()
getUserInfo()
function getUserInfo() {
  userInfo().then(res => {
    deliveryInfos.value=res.data.result.deliveryInfos
    userAddress.value=res.data.result.address
    userId.value = res.data.result.id
    storeID.value = res.data.result.storeId
    deliveryAddress.value = res.data.result.address
  })
}
const dialogOfDeleteDisplay=ref(false)
function displayDeleteCommodityDialog(){
  dialogOfDeleteDisplay.value=true;
}
function closeDeleteDialog() {
  dialogOfDeleteDisplay.value = false;
}
function deleteCommodityInfo(){
  deleteCommodity(storeId.value,commodityId.value).then(res => {
    if (res.data.code === '000'){
      ElMessageBox.alert(
          '删除成功',
          {
            type: "success",
          })
      closeDeleteDialog()
      toStoreDetailPage(storeId.value)
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
//去创建订单
//关于订单：
const deliveryMethod = ref()
const deliveryAddress = ref('')
const count=ref(1)
const orderPrice = ref()
const dialogOfCreateOrderDisplay=ref(false)
const paymentState = ref()
const userId = ref()
const pickDate=ref()
//支付订单弹窗
const orderId = ref()
const dialogRef = ref()

function getOrderPrice(){
  let calculatedPrice= count.value*price.value
  let formattedPriceStr = calculatedPrice.toFixed(2);
  orderPrice.value = parseFloat(formattedPriceStr);
}
const hasDeliveryAddressInput = computed(()=>deliveryAddress.value!='')
const hasDeliveryMethodInput = computed(()=>deliveryMethod.value!='')
const hasCountInput = computed(()=>count.value!=0)
const hasPriceInput = computed(()=>orderPrice.value!=0)
const hasPickDateInput = computed(()=>pickDate.value)
const hasDeliveryPhoneInput = computed(()=>deliveryPhone.value!='')
const createOrderDisabled = computed(()=>{
  if (deliveryMethod.value=="PICKUP"){
    return!(hasCountInput.value&&hasPriceInput.value &&hasDeliveryAddressInput.value
        &&hasDeliveryMethodInput.value&&hasPickDateInput.value)
  }else if(deliveryMethod.value=="DELIVERY"){
    return!(hasCountInput.value&&hasPriceInput.value &&hasDeliveryAddressInput.value
        &&hasDeliveryMethodInput.value&&hasDeliveryPhoneInput.value)
  }else{
    return true
  }
})
const hasInventory = computed(()=>inventory.value!=0)
function decreaseQuantity(){
  if (count.value>1){
    count.value=count.value-1;
  }else{
    ElMessage({
      message:'最少购买1件！',
      type:'warning'
    });
  }
}
function increaseQuantity(){
  if (count.value < inventory.value){
    count.value=count.value+1;
  }else {
    ElMessage({
      message:'库存不足！最多购买'+inventory.value+'件',
      type:'warning'
    });
  }
}
watch(count,()=>{
  setQuantity();
  getOrderPrice();
},{immediate:true});
function setQuantity() {
  // 检查数量是否超过库存
  if (count.value > inventory.value) {
    // 重置数量为1，并提示库存不足
    count.value = inventory.value
    ElMessage({
      message:'库存不足，最多购买' + inventory.value + '件，已为您设置购买数量为最多。',
      type:'warning'
  });} else if (count.value < 1) {
    count.value=1;
    ElMessage({
      message:'最少购买1件',
      type:'warning'
    })
  }
  // 如果数量在合理范围内，返回设置后的数量
  return count.value;
}
function handleCreateOrder(){
  paymentState.value="UNPAID"
  createOrder({
    paymentState:paymentState.value,
    userId:userId.value,
    storeId:storeId.value,
    commodityId:commodityId.value,
    count:count.value,
    deliveryMethod:deliveryMethod.value,
    price:orderPrice.value,
    deliveryAddress:deliveryAddress.value,
    deliveryPhone:deliveryPhone.value,
    pickDate:pickDate.value
  }).then(res=>{
    if (res.data.code === '000'){
      ElMessage({
        message: "创建订单成功！",
        type: 'success',
        center: true,
      })
      orderId.value = res.data.result
      closeCreateOrderDialog()
      dialogRef.value.open(orderId.value)
    }else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}
function displayCreateOrderDialog(){
  dialogOfCreateOrderDisplay.value=true;
}
function closeCreateOrderDialog() {
  dialogOfCreateOrderDisplay.value = false;
  paymentState.value = '';
  count.value = 1;
  deliveryMethod.value='';
  orderPrice.value=price.value;
  deliveryAddress.value='';
  pickDate.value='';
  deliveryPhone.value=''
}
const selectedDeliveryInfoId=ref()
const deliveryPhone = ref('')
function handleDeliveryInfoChange(value:any){
  // 根据ID找到对应的地址和电话信息
  const selectedInfo = deliveryInfos.value.find(info => info.id === value);
  if (selectedInfo) {
    deliveryAddress.value = selectedInfo.address;
    deliveryPhone.value = selectedInfo.phone;
    console.log(deliveryPhone.value)
  }
}
</script>
<template>
  <el-container>
    <el-header class="page-header">
      <el-card class="header-card">
        <div class="header-content">
          <div class="header-left">
            <!-- 商店图片 -->
            <el-image :src="logoUrl"  class="store-image"/>
          </div>
          <div class="header-right">
            <h1 class="store-name">{{storeName}}</h1>
          </div>
          <p>
            <el-rate class="header-rate"
                     v-model="storeRating"
                     disabled
                     show-score
                     text-color="#ff9900"
                     score-template="{value}分"
                     size="small"/>
          </p>
          <div>
            <el-button class="header-button" type="primary" @click="toStoreDetailPage(storeId)">进入商店</el-button>
          </div>
        </div>
      </el-card>
    </el-header>
    <el-container>
      <el-aside  class="page-aside">
        <el-card :body-style="{ padding: '12px'}">
          <h2 class="commodity-name">{{ name }}</h2>
          <p>
            <el-rate
                v-model="rating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}分"
                size="small"/>
          </p>
          <h6>共{{commodityScoreNum}}人打分</h6>
          <h3 class="commodity-price">价格：￥{{ price }}</h3>
          <h5>库存：{{ inventory }}</h5>
          <el-descriptions>
            <template v-for="item in subTypeList">
              <el-descriptions-item v-if="item.name === subType"
                  class="subType-details" label="类型">
                {{ parseCategory(type)+" - "+item.label }}
              </el-descriptions-item>
            </template>
            <el-descriptions-item>
              <div v-if="role==='CUSTOMER'">
                <el-button class="create_order_button" type="primary" plain
                           @click="displayCreateOrderDialog"
                           :disabled="!hasInventory">
                  立即购买
                </el-button>
                <el-dialog class="create-order"
                           title="创建订单"
                           v-model="dialogOfCreateOrderDisplay"
                           @close="closeCreateOrderDialog">
                  <h3>选择收货方式</h3>
                  <el-form-item>
                    <el-select id="orderType"
                               v-model="deliveryMethod"
                               placeholder="请选择"
                               style="width：50%;">
                      <el-option value="PICKUP" label = "到店自提"/>
                      <el-option value="DELIVERY" label = "快递送达"/>
                    </el-select>
                  </el-form-item>
                  <el-form-item v-if="deliveryMethod === 'PICKUP'">
                    <span>商店地址：{{ storeAddress }}</span>
                    <div class="demo-datetime-picker">
                      <div class="block">
                        <el-date-picker
                            v-model="pickDate"
                            type="date"
                            placeholder="选择自提日期"
                            :disabled-date="date => date.getTime() < new Date().getTime()"
                            format="YYYY-MM-DD"
                            date-format="MMM DD, YYYY"
                        />
                      </div>
                    </div>
                  </el-form-item>
                  <el-form-item v-if="deliveryMethod === 'DELIVERY'">
                    <el-select v-model="selectedDeliveryInfoId" placeholder="请选择收货地址" @change="handleDeliveryInfoChange">
                      <el-option
                          v-for="info in deliveryInfos"
                          :key="info.id"
                          :label="'地址：' + info.address + ' - 电话：' + info.phone"
                          :value="info.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <h3> 确认订单信息</h3>
                  <p>店铺：{{ storeName }}</p>
                  <div>
                    <p>商品信息：</p>
                    <div class = "product-item">
                      <el-image class="logo-image" :src="picLink[0]" style="width: 100px;"/>
                      <span class="product-name" style="">{{name}}</span>
                      <span class = "solo-price" style="">单价：￥{{price}}</span>
                    </div>
                  </div>
                  <p>购买数量</p>
                  <div class="quantity-wrapper" style="margin-top: 10px;">
                    <button @click="decreaseQuantity" :disabled="count <= 1">-</button>
                    <input type="number" v-model.number="count"
                           min="1" style="width: 50px; text-align: center;"/>
                    <button @click="increaseQuantity" >+</button>
                  </div>
                  <p>总价 :￥{{orderPrice}}</p>
                  <h4>请确认订单信息无误！！</h4>
                  <span class="button-group">
              <el-button @click.prevent="handleCreateOrder"
                         :disabled="createOrderDisabled"
                         type = "primary" plain>
                创建订单
              </el-button>
            </span>
                </el-dialog >
                <!--确认支付弹窗组件-->
                <PayDialog ref="dialogRef"/>

              </div>
            </el-descriptions-item>
          </el-descriptions>
          <el-image class="image-viewer" :src="currentImage" fit="contain" @click="nextImage"/>
          <h6>（点击切换图片）</h6>
          <div v-if="role==='STAFF'&& storeID === storeId">

            <el-button class="delete-commodity-button" type="primary" plain
                       @click="displayDeleteCommodityDialog()">
              删除商品
            </el-button>
            <el-dialog class="delete-store"
                       title="删除商品"
                       v-model="dialogOfDeleteDisplay"
                       @close="closeDeleteDialog">
              <h3>确认删除该商品？</h3>
              <span class="button-group">
                <el-button @click.prevent="deleteCommodityInfo"
                           type="primary">
                  确认
                </el-button>
                <el-button @click.prevent="closeDeleteDialog">
                  取消
                </el-button>
              </span>
            </el-dialog>
          </div>
        </el-card>
      </el-aside>
      <el-main class="page-main">
        <h4 class="commodity-description">{{description}}</h4>

        <el-image class="commodity-pic"
                  v-for="pic in picLink"
                  :key="pic"
                  :src="pic"/>
        <h4>已购买用户的评价：</h4>
        <p>
          <el-rate
              v-model="rating"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}分"
              size="small"/>
        </p>
        <h6>共{{commodityScoreNum}}人打分</h6>
        <div class="comment-item-list">
          <CommentItem v-for="commentVO in commentList"
                           :orderId="commentVO.orderId"/>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>


<style scoped>
.image-viewer img {
  width: 100%;
  height: auto;
  cursor: pointer; /* 显示点击效果 */
}
.page-aside {
  border-right: lightgrey solid 1px;
}
.full-screen-container {
  width: 100vw; /* 视口宽度的 100% */
  height: 100vh; /* 视口高度的 100% */
  display: flex;
}
.commodity-pic {
  display: flex;
  width: 400px; /* 或者您想要的任何宽度 */
  object-fit: cover; /* 如果需要保持图片的宽高比 */
}
.commodity-item-card {
  width: 100%; /* 占据容器的全部宽度 */
  height: 100%; /* 占据容器的全部高度 */
  /* 根据需要添加背景色或其他样式 */
  background-color: #ffffff;
}
.commodity-name{
  color: darkblue;
}
.commodity-price{
  color: brown;
}
.page-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2px;
  position: relative;
  width: auto;
  height: 60px;
}
.header-card {
  margin-top: 5px;
  border: 1px solid #ccc; /* 设置边框 */
  border-radius: 10px; /* 设置圆角 */
  overflow: hidden; /* 隐藏溢出内容 */
  display: flex; /* 横向排列子元素 */
  align-items: center; /* 垂直居中 */
  height: 60px;
  width: 100%;
}

.header-content {
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: space-between;
  padding: 10px; /* 内边距 */
}

.header-right {
  margin-left: 50px;
  display: flex;
  flex-direction: column; /* 子元素纵向排列 */
  align-items: center; /* 垂直居中 */
}

.header-left {
  margin-right: 30px; /* 右侧间距 */
}
.store-image {
  margin-top: 5px;
  width: 50px; /* 图片宽度 */
  height: 50px; /* 图片高度 */
  object-fit: cover; /* 图片适应容器 */
  border-radius: 50%; /* 图片圆角 */
  margin-left: 20px;
}

.store-name {
  margin: 0; /* 移除标题的默认边距 */
}

.header-rate {
  margin-top: 5px; /* 评分与标题之间的间距 */
}

.header-button {
  margin-top: 5px;
  margin-left: 800px;
}
.page-main {
  position: absolute;
  left: 380px;
  right: 0;
  top: 140px;
  bottom: 0;
  overflow-y: scroll;
}
.page-aside {
  display: block;
  position: absolute;
  width: 380px;
  left: 0;
  top: 135px;
  bottom: 0;
}

.logo-image {
  margin-right: 25px; /* 图片和名称之间的间距 */
}
.product-name{
  font-size: medium;
  margin-right: 25px;
}
.quantity-wrapper {
  display: inline-block;
  vertical-align: middle;
}

.quantity-wrapper button {
  font-size: 14px;
  padding: 5px 10px;
  border: none;
  background-color: #f4f4f4;
  cursor: pointer;
}

.quantity-wrapper input {
  margin: 0 5px;
  border: 1px solid #ccc;
  text-align: center;
}
.demo-datetime-picker {
  display: flex;
  width: 100%;
  padding: 0;
  flex-wrap: wrap;
  justify-content: left;
  align-items: stretch;
}
.demo-datetime-picker .block {
  padding: 30px 0;
  text-align: center;
}
.line {
  width: 1px;
  background-color: var(--el-border-color);
}
</style>
