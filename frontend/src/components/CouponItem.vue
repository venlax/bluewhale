<script setup lang="ts">
import {ref} from "vue";
import {parseCouponType, parseTime} from "../utils";
import {getCouponGroupsByGroupId, redeemCoupon} from "../api/couponGroup.ts";
import {getStoreById} from "../api/store.ts";
const role = sessionStorage.getItem("role")
const props = defineProps({
  groupId:{
    type:Number,
    required:true
  },
  amount:{
    type:Number,
    required:true
  },
  restAmount:{
    type:Number,
    required:true
  },
  storeId:{
    type:Number,
    required:true
  },
  whetherRedeemed:{
    type:Boolean,
    required:true
  }
})
const groupId = ref()
const couponType = ref()
const reduction=ref()
const threshold=ref()
const expirationTime = ref()
const storeName=ref('')
const userId=ref<number[]>([])
const amount=ref()
amount.value=props.amount
const restAmount=ref()
restAmount.value=props.restAmount
const storeId=props.storeId
const trueFlag=ref(true)
getCoupons()
function getCoupons(){
  getCouponGroupsByGroupId(props.groupId).then(res=>{
    groupId.value=res.data.result[0].groupId
    couponType.value=parseCouponType(res.data.result[0].couponType)
    threshold.value= res.data.result[0].threshold
    reduction.value=res.data.result[0].reduction
    expirationTime.value=parseTime(res.data.result[0].expirationTime)
    res.data.result.forEach(item=> {
      userId.value.push(item.userId)
    })
  })
  getStoreById(storeId).then(res=>{
    if(res && res.data && res.data.result && res.data.result.name !== null){
      storeName.value=res.data.result.name
    }
  })
}
function handleRedeemCoupon(){
  redeemCoupon(groupId.value).then(res=>{
    if (res.data.code === '000'){
      ElMessageBox.alert(
          '领取成功',
          {
            type: "success",
          })
      restAmount.value=props.restAmount
      console.log(restAmount.value)
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
  <el-card class="couponGroup-item-card"
           :body-style="{padding: '12px'}"
           shadow="hover">
    <el-container>
      <el-header class="header-style">
        <el-row class="row-style" :gutter="10">
          <el-col :span = "6">
            <p class="header-text"> 有效期截至 {{expirationTime}} </p>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-row :gutter="10">
          <el-col :span="2" class="state-type">
            <el-tag  :type="couponType==='蓝鲸券'?'primary':'danger'"  style="padding: 5px 10px;font-size: 15px;" disable-transitions>{{couponType}}</el-tag>
          </el-col>
          <el-col :span="8">
            <p class="main-text" v-if="couponType=='满减券'">满{{threshold}}元 可减{{reduction}}元</p>
            <p class="main-text" v-else-if="couponType=='蓝鲸券'">0-100元打九五折；
              100-200元打九折；
              200-300元打八五折；
              300-400元打八折；
              400-500元打七五折；
              500元以上打七折。</p>
          </el-col>
          <el-col :span="1"></el-col>
          <el-col :span="4" class="state-type">
            <el-tag v-if="storeName!=''" :type="'danger'" style="padding: 5px 10px;font-size: 15px;">{{storeName}}</el-tag>
            <el-tag v-if="storeName==''" :type="'warning'" style="padding: 5px 10px;font-size: 15px;">所有商店</el-tag>
          </el-col>
          <el-col :span="3" v-if="role!='CUSTOMER'">
            <p class="main-text">共有{{amount}}张</p>
          </el-col>
          <el-col :span="3" v-if="role!='CUSTOMER'">
            <p class="main-text">余{{restAmount}}张未领取</p>
          </el-col>
          <el-col v-if="role==='CUSTOMER'" :span="4">
            <el-col v-if="restAmount!=0">
              <el-button v-if="!whetherRedeemed" @click="handleRedeemCoupon"
                         type="primary" style="margin-top: 10px">
                领取
              </el-button>
              <el-button v-if="whetherRedeemed" @click="handleRedeemCoupon"
                         type="primary" disabled style="margin-top: 10px">
                已领取
              </el-button>
            </el-col>
            <el-button v-if="restAmount==0" @click="handleRedeemCoupon"  :disabled="trueFlag" style="margin-top: 10px">
              已领完
            </el-button>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-card>
</template>

<style scoped>
.couponGroup-item-card{
  margin: 6px;
  border: 1px solid rgba(150, 173, 230, 0.71);
  width: 99%;
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
.state-type{
  margin-top: 10px;
}
</style>