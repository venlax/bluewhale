<!--Lab2新增-->
<!--商店列表父组件中的单个商店子组件-->
<script setup lang="ts">
import {ref} from "vue"
import {getStoreById} from "../api/store.ts";
import {getCommentCountByStoreId} from "../api/comment.ts";
const role = sessionStorage.getItem("role")
const props = defineProps({
  storeId:{
    type:Number,
    required:true
  }
})
const name = ref('')
const logoUrl = ref('')
const scoreNumber = ref(0)
const rating = ref(0)
const introduction = ref('')
const address = ref('')
const storeId = ref()
getStoreById(props.storeId).then(res =>{
  storeId.value=res.data.result.id
  name.value = res.data.result.name
  logoUrl.value=res.data.result.logo
  rating.value = res.data.result.score
  if (rating.value==null){
    rating.value=0
  }
  introduction.value = res.data.result.introduction
  address.value=res.data.result.address
  getCommentCountByStoreId(storeId.value).then(res=>{
    scoreNumber.value=res.data.result
  })
})
</script>

<template>
  <el-card class="store-item-card" :body-style="{ padding: '12px'}" shadow="hover">
    <el-container>
      <el-aside>
          <el-image class="logo-image" :src="logoUrl" fit="contain"/>
      </el-aside>
      <el-main>
        <div class="store-info">
          <h1 class="store-name">{{ name }}</h1>
          <el-descriptions class="store-details" :column="1">
            <el-descriptions-item class="rating-item" label="评分">
              <el-rate
                  v-model="rating"
                  disabled
                  show-score
                  text-color="#ff9900"
                  score-template="{value}分"
                  size="small"/>
              <span class="rating-count">
                (共{{ scoreNumber }}人打分)
              </span>
            </el-descriptions-item>
            <el-descriptions-item  label="地址：">
              <span>
                {{ address }}
              </span>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-main>
    </el-container>
  </el-card>
</template>

<style scoped>
.store-item-card{
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
.store-item-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 鼠标悬停时的阴影效果 */
}
.logo-image {
  height: 170px;
}
.card-image-container{
  margin-right: 90px;
}
.store-info {
  flex: 1;
  margin-right: 30px; /* 图片与信息之间的间距 */
}
.store-name {
  margin: 0; /* 去除默认的边距 */
  font-size: 1.2em; /* 字体大小 */
  font-weight: bold; /* 字体加粗 */
}

.store-details {
  color: #666; /* 文字颜色 */
}

.rating-item {
  margin-bottom: 10px; /* 评分项与其他项之间的间距 */
}

.rating-count {
  margin-left: 8px; /* 评分计数与评分之间的间距 */
  font-size: 0.8em; /* 字体大小 */
  color: #999; /* 文字颜色 */
}

</style>
