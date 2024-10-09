<!--Lab2新增-全部商店界面/主页-->
<script setup lang="ts">
import {ref} from "vue"
import {router} from'../../router'
import {getAllStoresInfo} from "../../api/store.ts";
import StoreItem from "../../components/StoreItem.vue";

const role = sessionStorage.getItem("role")

const storeList = ref()

//获取商店列表
getAllStoresInfo().then(res => {
  storeList.value = res.data.result
})
//点击创建按商店按钮，跳转到创建商店界面
function toCreateStorePage(){
  router.push("/createStore")
}
//点击商店卡片，跳转到对应的商店界面
function toStoreDetailPage(storeId : Number){
  router.push("/storeDetail/"+storeId)
}
</script>>

<template>
  <el-main>
    <div v-if="role==='MANAGER'">
      <el-button class="add-store-button" type="primary" plain
                 @click="toCreateStorePage()">
        创建商店
      </el-button>
    </div>
    <div class="store-item-list">
      <StoreItem v-for="storeVO in storeList" :storeId="storeVO.id"
                 @click="toStoreDetailPage(storeVO.id)"/>
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
</style>
