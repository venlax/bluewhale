<script setup lang="ts">
import {computed,ref} from "vue"
import {router} from'../../router'
import {getAllStoresInfo} from "../../api/store.ts";
import StoreItem from "../../components/StoreItem.vue";
import {userInfo} from "../../api/user.ts";
import {getAllCommodityInfo, getCommodityById, updateInventoryInfo} from "../../api/product.ts";
//import {parseTime} from "../../utils";

const role = sessionStorage.getItem("role")
const storeID = ref()
interface store {
  id:number;
  name:string;
}
const storeList = ref<store[]>([])
const filteredStores = computed(() => {
  return storeList.value.filter(store => store.id === storeID.value);
});

getUserInfo()
function getUserInfo() {
  userInfo().then(res => {
    storeID.value = res.data.result.storeId
    getCommodityList()
  })
}
//获取商店列表
getAllStoresInfo().then(res => {
  storeList.value = res.data.result
})
//点击商店卡片，跳转到对应的商店界面
function toStoreDetailPage(storeId : Number){
  router.push("/storeDetail/"+storeId)
}
function toCreateProductPage(){
  router.push("/createProduct")
}
//有关修改库存弹窗内容
const inventoryRegex = /^([1-9][0-9]*)$/
const inventoryLegal = computed(() => inventoryRegex.test(newInventory.value) || newInventory.value==0)
const hasInventory = computed(()=>newInventory.value!='')
const hasCommodityId = computed(() => commodityId.value!='')
//商店列表
const commodityList = ref()
//商品名
const name = ref('')
//商品id
const commodityId=ref()
//商品的storeId
const storeId = ref()
const price = ref()
//商品介绍
const description = ref('')
//库存
const inventory = ref()
//修改前的库存
const originalInventory=ref()
const newInventory=ref()
getCommodityInfoById()
function getCommodityList(){
  getAllCommodityInfo(storeID.value).then(res => {
    commodityList.value = res.data.result
  })
}
getCommodityList()
function getCommodityInfoById(){
  getCommodityById(commodityId.value).then(res => {
    name.value=res.data.result.name
    storeId.value=res.data.result.storeId
    price.value=res.data.result.price
    inventory.value=res.data.result.inventory
    originalInventory.value=res.data.result.inventory
    description.value=res.data.result.description
  }).catch(error =>{
    console.error("获取商品信息时错误",error)
  })
}
const dialogOfInventoryDisplay=ref(false)
function displayDialog(){
  dialogOfInventoryDisplay.value=true
}
function closeDialog() {
  dialogOfInventoryDisplay.value = false;
  commodityId.value = '';
  newInventory.value = '';
  originalInventory.value=''
}
function updateInventory(){
  updateInventoryInfo(
      storeId.value,
      commodityId.value,
      newInventory.value
  ).then(res => {
    if (res.data.code === '000'){
      originalInventory.value=inventory.value
      ElMessageBox.alert(
          '更新成功',
          {
            type: "success",
          })
      closeDialog()
    } else if (res.data.code === '400') {
      ElMessage({
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
const updateInventoryDisabled = computed(() => {
  return !(hasCommodityId.value
      && hasInventory.value
      && inventoryLegal.value)
})
</script>

<template>
  <el-main>
    <div v-if="role === 'STAFF'">
      <el-button class="add-store-button" type="primary" plain
                 @click="toCreateProductPage()">
        创建商品
      </el-button>
      <el-button @click.prevent="displayDialog"
                 type="primary" plain>
        修改库存
      </el-button>
      <el-dialog
          title="修改商品库存"
          v-model="dialogOfInventoryDisplay"
          @close="closeDialog">
        <el-form label-width="120px" label-position="top">
          <h3>选择商品</h3>
          <el-form-item>
            <el-select id="id"
                       v-model="commodityId"
                       placeholder="请选择"
                       style="width:100%;"
                       @change="getCommodityInfoById">
              <el-option
                  v-for="commodityVO in commodityList"
                  :key="commodityVO.id"
                  :value="commodityVO.id"
                  :label="commodityVO.name">
              </el-option>
            </el-select>
          </el-form-item>
          <h3>原始库存</h3>
          <p>{{ originalInventory }}</p>
          <h3>库存修改为</h3>
          <el-form-item>
            <el-input id="newInventory"
                      v-model="newInventory" class="{'error-warn-input' :(hasInventory && !inventoryLegal)}"
                      placeholder="请输入新的库存量"/>
            <label v-if="!hasInventory" for="newInventory">输入库存</label>
            <label v-else-if="!inventoryLegal" for="newInventory" class="error-warn">库存格式不正确（必须为非负整数）</label>
            <label v-else for="newInventory">输入库存</label>
          </el-form-item>
          <el-button type="primary"
                     @click.prevent="updateInventory"
                     :disabled="updateInventoryDisabled"
          >确认</el-button>
        </el-form>
      </el-dialog>
    </div>
    <div class="store-item-list">
      <StoreItem v-for="store in filteredStores"
                 :key="store.id"
                 :storeId="store.id"
                 @click="toStoreDetailPage(store.id)"/>
    </div>
  </el-main>
</template>

<style scoped>
.add-store-button{
  margin-left: 30px;
}
.store-item-list{
  display: flex;
  padding: 2px;
  flex-flow: wrap;
  justify-content: center;
  align-content: start;
}
.error-warn {
  color: red;
}
.error-warn-input {
  --el-input-focus-border-color: red;
}
.bgimage {
  background-image: url("../../assets/shopping-1s-1084px.svg");
}
</style>