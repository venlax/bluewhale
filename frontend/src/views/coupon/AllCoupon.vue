<script setup lang="ts">
import {ref,computed} from "vue";
import {
  distributeCouponGroup,
  getCouponGroups,
  getCouponsByUserId
} from "../../api/couponGroup.ts";
import {userInfo} from "../../api/user.ts";
import CouponItem from "../../components/CouponItem.vue";
import {parseCouponType, parseTime} from "../../utils";
const role = sessionStorage.getItem("role")
const storeId=ref()
const userId=ref()

function getUserInfo(){
  userInfo().then(res=>{
    userId.value=res.data.result.id
    storeId.value = res.data.result.storeId
  })
}

getUserInfo()
const couponType=ref()
const threshold=ref()//满
const reduction=ref()//减
const amount=ref()
const expirationTime=ref('')
const hasCouponTypeInput= computed(()=>couponType.value!='')
const hasThresholdInput= computed(()=>threshold.value!=''&&threshold.value!=0)
const hasReductionInput= computed(()=>reduction.value!=''&&reduction.value!=0)
const hasAmountInput= computed(()=>amount.value!=''&&amount.value!=0)
const hasExpirationTimeInput= computed(()=>expirationTime.value!='')
const distributeCouponDisabled=computed(()=>{
  if(couponType.value =='FULL_REDUCTION'){
    return!(hasThresholdInput.value&&hasReductionInput.value&&hasCouponTypeInput.value&&hasAmountInput.value&&hasExpirationTimeInput.value)
  }else if(couponType.value =='SPECIAL'){
    return!(hasCouponTypeInput.value&&hasAmountInput.value&&hasExpirationTimeInput.value&&hasCouponTypeInput.value)
  }else{
    return true
  }
})
function closeCreateCoupon() {
  couponType.value=''
  threshold.value=''
  reduction.value=''
  amount.value=''
  expirationTime.value=''
}
function handleDistributeCoupon(){
  distributeCouponGroup(storeId.value,amount.value,{
    couponType:couponType.value,
    expirationTime:expirationTime.value,
    threshold:threshold.value,
    reduction:reduction.value
  }).then(res=>{
    if(res.data.code === '000'){
    ElMessage({
      message: "发布成功！",
      type: 'success',
      center: true,
    })
      closeCreateCoupon()
  } else if (res.data.code === '400') {
    ElMessage({
      message: res.data.msg,
      type: 'error',
      center: true,
      })
    }
  })
}
interface couponGroupVO{
  id:number
  amount:number
  restAmount:number
  storeId:number
}
const myCouponList = ref([])
const allGroupList=ref<couponGroupVO[]>([])
getCouponGroups().then(res=>{
    allGroupList.value=res.data.result
  })
if(role=="CUSTOMER"){
  userInfo().then(res=>{
    userId.value=res.data.result.id
    getCouponsByUserId(userId.value).then(res=>{
      myCouponList.value=res.data.result
    })
  })
}
const activeTab=ref('all')
const handleTabClick=(tab:any)=>{
  activeTab.value=tab.name
}
function formatterCouponType(row:any){
  return parseCouponType(row.couponType)
}
function formatterTime(row:any){
  return parseTime(row.expirationTime)
}
function whetherWasRedeemed(CouponGroupId:number){
  var wasRedeemed = false
  myCouponList.value.forEach(function (element){
    wasRedeemed = wasRedeemed || (element.groupId == CouponGroupId)
  })
  return wasRedeemed
}


</script>
<template>
  <el-tabs type="border-card" v-model="activeTab" @tab-click="handleTabClick">
    <el-tab-pane label="所有优惠券" name="all">所有优惠券</el-tab-pane>
    <el-tab-pane v-if="role==='CUSTOMER'" label="我的优惠券" name="my" >我的优惠券</el-tab-pane>
    <el-tab-pane v-if="role!='CUSTOMER'" label="发布优惠券" name="distribute">发布优惠券</el-tab-pane>
  </el-tabs>
  <div class="couponGroup-item-list" v-if="activeTab==='all'" @click="closeCreateCoupon">
    <CouponItem v-for="couponGroupVO in allGroupList"
                :groupId="couponGroupVO.id"
                :amount="couponGroupVO.amount"
                :restAmount="couponGroupVO.restAmount"
                :storeId="couponGroupVO.storeId"
                :whether-redeemed="whetherWasRedeemed(couponGroupVO.id)"/>
  </div>
  <div v-if="role=='CUSTOMER'&& activeTab=='my'">
    <div class="no-order-tip" v-if="myCouponList.length==0">
      <el-empty description="还没有优惠券哦，快去领取吧！"></el-empty>
    </div>
    <div v-else>
      <el-table :data="myCouponList"
                style="width: 100% "
                :default-sort="{prop:'expirationTime', order:'descending'}">
        <el-table-column width="50"></el-table-column>
        <el-table-column prop="couponType" label="类型" sortable width="200">
          <template v-slot="scope">
            <el-tag
                :type="scope.row.couponType === 'SPECIAL' ? '' : 'danger'"
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
              满 {{scope.row.threshold}}元 可减{{scope.row.reduction}}元
            </div>
          </template>
        </el-table-column>
        <el-table-column width="100"></el-table-column>
        <el-table-column label="可用商店" width="200">
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
        <el-table-column label="状态">
          <template v-slot="scope">
            <div>
              <el-tag v-if="scope.row.isUsed" :type="'info'" >
                已使用
              </el-tag>
              <el-tag v-else :type="'success'" >
                未使用
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="expirationTime" label="有效期截止时间" sortable :formatter="formatterTime">
        </el-table-column>
      </el-table>
    </div>

  </div>
  <div v-if="role!='CUSTOMER' && activeTab==='distribute'" >
    <el-card class="distribute-coupon">
      <h3>选择优惠券类型</h3>
      <el-form-item>
        <el-select id="couponType"
                   v-model="couponType"
                   placeholder="请选择"
                   style="width: 60%;">
          <el-option value="FULL_REDUCTION" label="满减券"/>
          <el-option value="SPECIAL" label = "蓝鲸券"/>
        </el-select>
      </el-form-item>
      <el-form-item v-if="couponType==='FULL_REDUCTION'">
        <div>
          <h3>设置满减规则</h3>
          <el-row :gutter="12">
            满
            <el-col :span="6">
              <el-input v-model="threshold" placeholder="请输入满额"></el-input>
            </el-col>
            元 可减
            <el-col :span="8">
              <el-input v-model="reduction" placeholder="请输入减去的金额"></el-input>
            </el-col>
            元
          </el-row>
        </div>
      </el-form-item>
      <h3>设置发布数量</h3>
      <el-form-item>
        <el-input style="width: 200px" v-model="amount" placeholder="请输入数量"></el-input>
      </el-form-item>
      <h3>设置到期时间</h3>
      <el-form-item>
        <div class="demo-datetime-picker">
          <div class="block">
            <el-date-picker
                v-model="expirationTime"
                type="date"
                placeholder="选择优惠券到期时间"
                :disabled-date="(date:any) => date.getTime() < new Date().getTime()"
                format="YYYY-MM-DD"
                date-format="MMM DD, YYYY"
            />
          </div>
        </div>
      </el-form-item>
      <el-button @click.prevent="handleDistributeCoupon"
                 :disabled="distributeCouponDisabled"
                 type="primary">
        确认发布
      </el-button>
    </el-card>
  </div>
</template>

<style scoped>
.distribute-coupon{
  width: 99%;
  margin:auto;
}
</style>