<script setup lang="ts">
import { ref} from 'vue'
import {getAllCommodityInfo, searchCommodity} from '../../api/product.ts'
import {router} from "../../router";
import ProductItem from "../../components/ProductItem.vue";
import {getAllStoresInfo} from "../../api/store.ts";
const keyword = ref('')
const type = ref('')
const low=ref()
const high=ref()
const commodityList = ref([])
const storeList = ref()
const isSearching=ref(false)
function getCommodityList(){
  if(isSearching.value==false){
    getAllStoresInfo().then(res => {
      storeList.value = res.data.result
      commodityList.value = []
      storeList.value.forEach(store=>{
        getAllCommodityInfo(store.id).then(res => {
          if(res&&res.data&&res.data.result){
            commodityList.value = commodityList.value.concat(res.data.result)
          }
        })
      })
    })
  }
}
getCommodityList()
function handleSearch(){
  isSearching.value=true
  commodityList.value = []
  searchCommodity(keyword.value,type.value,low.value,high.value).then(res=>{
    console.log(res.data.result)
    commodityList.value = res.data.result
  })
}
function toCommodityDetailPage(commodityId : Number){
  router.push("/commodityDetail/"+commodityId)
}
</script>

<template>
  <el-container>
  <el-header>
    <el-card class="header-card">
      <div class="search-container">
        <el-form :inline="true">
          <el-form-item >
            <el-input id="keyword"
                      v-model="keyword" style="
                      width: 150px"
                      placeholder="请输入商品名称"/>
          </el-form-item>
          <el-form-item >
            <el-select id="type"
                       v-model="type"
                       placeholder="请选择商品类型"
                       style="width:200px;">
              <el-option value="FOOD" label="食品"/>
              <el-option value="CLOTHES" label="服饰"/>
              <el-option value="FURNITURE" label="家具"/>
              <el-option value="ELECTRONICS" label="电子产品"/>
              <el-option value="ENTERTAINMENT" label="娱乐"/>
              <el-option value="SPORTS" label="体育产品"/>
              <el-option value="LUXURY" label="奢侈品"/>
              <el-option value="" label="所有"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input id="low"
                      v-model="low"
                      placeholder="输入最低价格"
                      style="width: 120px"/>
             元 至
            <el-input id="high"
                      v-model="high"
                      placeholder="输入最高价格"
                      style="width: 120px"/>元
          </el-form-item>
          <el-form-item>
            <el-button type="primary" plain @click.prevent="handleSearch">搜索</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </el-header>
  <el-main>
    <div v-if="commodityList.length == 0">
      <el-empty description="没有符合条件的商品,请尝试其他搜索条件"></el-empty>
    </div>
    <div v-else>
      <el-row :gutter="20" class="store-items-row">
        <el-col :span="12" v-for="(commodity,index) in commodityList" :key="index" class="product-col">
          <ProductItem :commodityId="commodity.id" @click="toCommodityDetailPage(commodity.id)"/>
        </el-col>
      </el-row>
    </div>
  </el-main>
  </el-container>
</template>

<style scoped>
.search-container {
  display: flex;
  align-items: center;
  margin-top: 13px;
  justify-content: space-between;
  margin-left: 180px;
}
.header-card {
  margin-top: 5px;
  border: 1px solid #96ade6; /* 设置边框 */
  border-radius: 10px; /* 设置圆角 */
  overflow: hidden; /* 隐藏溢出内容 */
  display: flex; /* 横向排列子元素 */
  align-items: center; /* 垂直居中 */
  height: 60px;
}
</style>