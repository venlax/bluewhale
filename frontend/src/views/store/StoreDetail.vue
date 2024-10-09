<!--Lab2新增-商店详情界面-->
<script setup lang="ts">
import {computed, ref} from "vue"
import {deleteStore, getAllStoresInfo, getStoreById, updateStore} from "../../api/store.ts";
//import {router} from "../../router";
import {getAllCommodityInfo} from "../../api/product.ts";
import ProductItem from "../../components/ProductItem.vue";
import {useRoute, useRouter} from "vue-router";
import {getCommentCountByStoreId} from "../../api/comment.ts";
const role = sessionStorage.getItem("role")
const name = ref('')
const logoUrl = ref('')
const number = ref(0)
const rating = ref(0)
const introduction = ref('')
const commodityList = ref()
const storeList=ref()
const storeId = ref()
const address=ref()
const route = useRoute()
const router = useRouter()

getAllStoresInfo().then(res => {
  storeList.value = res.data.result
})
function getCommodityList(){
  getAllCommodityInfo(storeId.value).then(res => {
    commodityList.value = res.data.result
  })
}
storeId.value=route.params.storeId
getCommodityList()
const storeScoreNum = ref(0)
function getStoreInfo(){
  getStoreById(storeId.value).then(res =>{
    name.value = res.data.result.name
    logoUrl.value=res.data.result.logo
    rating.value = res.data.result.score
    if (rating.value==null){
      rating.value=0
    }
    introduction.value = res.data.result.introduction
    newName.value=res.data.result.name
    newIntroduction.value=res.data.result.introduction
    newAddress.value=res.data.result.address
    address.value=res.data.result.address
  })
  getCommentCountByStoreId(storeId.value).then(res=>{
    storeScoreNum.value=res.data.result
  })
}
getStoreInfo()
function toCommodityDetailPage(commodityId : Number){
  router.push("/commodityDetail/"+commodityId)
}
//删除商店
const dialogOfDeleteDisplay=ref(false)
function displayDeleteStoreDialog(){
  dialogOfDeleteDisplay.value=true
}
function closeDeleteDialog() {
  dialogOfDeleteDisplay.value = false;
}
function deleteStoreInfo(){
  deleteStore(storeId.value).then(res => {
    if (res.data.code === '000'){
      ElMessageBox.alert(
          '删除成功',
          {
            type: "success",
          })
      closeDeleteDialog()
      router.push("/allStore")
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
//修改商店信息
const dialogOfUpdateDisplay=ref(false)
const newName=ref()
const newIntroduction=ref()
const newAddress=ref()
function displayUpdateStoreDialog(){
  dialogOfUpdateDisplay.value=true
}
function closeUpdateDialog() {
  dialogOfUpdateDisplay.value = false;
}
function updateStoreInfo(){
  updateStore({
    name: newName.value,
    introduction:newIntroduction.value,
    id:storeId.value,
    address:address.value
  }).then(res=>{
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "修改成功！",
        type: 'success',
        center: true,
      })
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}
const hasNewName = computed(()=> newName.value!='')
const hasNewIntro = computed(()=>newIntroduction.value!='')
const hasNewAddress = computed(()=>newAddress.value!='')
const updateStoreInfoDisabled = computed(()=>{
  return !(hasNewIntro.value&&hasNewName.value&&hasNewAddress.value)
})
</script>


<template>
  <div class="full-screen-container">
    <el-container>
      <!--希望把商店详情的一部分内容放在这个侧边栏里，你要真不想放也没事-->
      <el-aside width="28%" class="page-aside">
        <el-card class="store-item-card" :body-style="{ padding: '15px'}">
          <div v-if="role==='MANAGER'">

<!--            <el-button class="update-store-button" type="primary" plain-->
<!--                       @click="displayUpdateStoreDialog()">-->
<!--              修改信息-->
<!--            </el-button>-->

            <el-button class="delete-store-button" type="primary" plain
                       @click="displayDeleteStoreDialog()">
              删除商店
            </el-button>
            <el-dialog class="delete-store"
                       title="删除商店"
                       v-model="dialogOfDeleteDisplay"
                       @close="closeDeleteDialog">
              <h2>确认删除该商店？</h2>
              <span class="button-group">
                <el-button @click.prevent="deleteStoreInfo"
                           type="primary">
                  确认
                </el-button>
                <el-button @click.prevent="closeDeleteDialog">
                  取消
                </el-button>
              </span>
            </el-dialog>
            <!--修改信息弹窗-->
            <el-dialog
                title="修改商店信息"
                v-model="dialogOfUpdateDisplay"
                @close="closeUpdateDialog">
              <el-form label-width="120px" label-position="top">
                <h3>商店名称</h3>
                <el-form-item>
                  <el-input id="newName"
                            v-model="newName">
                  </el-input>
                </el-form-item>
                <h3>商店地址</h3>
                <el-form-item>
                  <el-input id="newAddress"
                            v-model="newAddress">
                  </el-input>
                </el-form-item>
                <h3>商店简介</h3>
                <el-form-item>
                  <el-input class="input-newIntroduction" id="newIntroduction"
                            v-model="newIntroduction">
                  </el-input>
                </el-form-item>
                <el-button type="primary"
                           @click.prevent="updateStoreInfo"
                           :disabled="updateStoreInfoDisabled"
                >确认</el-button>
              </el-form>
            </el-dialog>
          </div>
        <h3 class="store-name">{{ name }}</h3>
        <div class="store-info">
          <el-rate
              v-model="rating"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}分"
              size="small"/>
            <span class="store-introduction">   （共{{ storeScoreNum }}人打分）</span>
        </div>
          <div class="store-info">
            <span class="store-introduction">地址：{{ address }}</span>
          </div>
        <el-image class="store-logo" :src="logoUrl" fit="contain"/>
        <div class="store-info">
          <span class="store-introduction">{{ introduction }}</span>
        <!-- 其他信息，如评分、数量等也可以在这里展示 -->
        </div>
        </el-card>
      </el-aside>

      <el-main>
        <el-row :gutter="20" class="store-items-row">
          <el-col :span="12" v-for="(commodity,index) in commodityList" :key="index" class="product-col">
            <ProductItem :commodityId="commodity.id" @click="toCommodityDetailPage(commodity.id)"/>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>


<style scoped>
.page-aside {
  border-right: lightgrey solid 1px;
}
.store-info {
  /* 添加样式来美化商店信息 */
}
.store-logo {
  width: 200px; /* 或其他合适的尺寸 */
  height: auto;
}
.store-introduction{
  font-size: 14px;
}
.full-screen-container {
  width: 100vw; /* 视口宽度的 100% */
  height: 100vh; /* 视口高度的 100% */
  display: flex;
}

.store-item-card {
  width: 100%; /* 占据容器的全部宽度 */
  height: 100%; /* 占据容器的全部高度 */

  /* 根据需要添加内边距 */
  padding: 15px;
  /* 根据需要添加背景色或其他样式 */
  background-color: #fff;
}
</style>
