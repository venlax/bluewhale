<!--Lab2新增-->
<!--商品列表父组件中的单个商品子组件-->
<script setup lang="ts">
import {getCommodityById} from "../api/product.ts";
import {ref} from "vue";
import {parseCategory,subTypeList} from "../utils";
const props = defineProps({
  commodityId:{
    type:Number,
    required:true
  },
})
const name = ref('')
//商品的storeId
const storeId = ref()
const price = ref()
//商品介绍
const description = ref('')
//库存
const inventory = ref()
const type = ref()
const logoUrl = ref('')
const picLink = ref<string[]>()
const score = ref(0)
const subType = ref()
getCommodityById(props.commodityId).then(res => {
  name.value=res.data.result.name
  storeId.value=res.data.result.storeId
  price.value=res.data.result.price
  inventory.value=res.data.result.inventory
  description.value=res.data.result.description
  type.value=res.data.result.type
  picLink.value=res.data.result.picLink
  logoUrl.value=picLink.value[0]
  score.value=res.data.result.score
  subType.value=res.data.result.subType
  if (score.value==null){
    score.value=0;
  }
})
</script>

<template>
  <el-card class="commodity-item-card" :body-style="{ padding: '10px'}" shadow="hover">
    <el-container>
      <el-aside width="45%">
        <el-image class="logo-image" :src="logoUrl" fit="contain"/>
      </el-aside>
      <el-main>
        <div class="commodity-info">
          <h3 class="commodity-name">名称：{{ name }}</h3>
          <el-descriptions class="commodity-details" :column="1">
            <template v-for="item in subTypeList">
              <el-descriptions-item v-if="item.name === subType"
                                    class="subType-details" label="类型">
                {{ parseCategory(type)+" - "+item.label }}
              </el-descriptions-item>
            </template>
            <el-descriptions-item class="price-details" label="价格:">
              <span>￥{{ price }}
              </span>
            </el-descriptions-item>
            <el-descriptions-item class="rating-item" label="评分">
              <el-rate
                  v-model="score"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}分"
                  size="small"/>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-main>
    </el-container>
  </el-card>
</template>

<style scoped>
.commodity-item-card{
  display: flex;
  justify-content: space-between;
  align-items: start;
  width: 90%; /* 或根据需要设置固定宽度 */
  height: 200px;
  margin-bottom: 20px; /* 每个商店卡片之间的间距 */
  border-radius: 4px; /* 圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  overflow: hidden; /* 防止内容溢出 */
  transition: box-shadow 0.3s ease; /* 平滑的阴影过渡效果 */
  box-sizing: border-box;
}
.commodity-item-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 鼠标悬停时的阴影效果 */
}
.logo-image {
  height: 170px;
}
.commodity-name {
  margin: 0; /* 去除默认的边距 */
  font-size: 1.2em; /* 字体大小 */
  font-weight: bold; /* 字体加粗 */
}

.commodity-details {
  color: #666; /* 文字颜色 */
}
</style>
