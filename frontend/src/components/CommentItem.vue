<script setup lang="ts">
import {ref} from "vue";
import {getCommentByOrderId} from "../api/comment.ts";
import {parseTime} from "../utils";
import {getNameById} from "../api/user.ts";

const props=defineProps({
  orderId:{
    type:Number,
    required:true
  }
})
const userId = ref()
const content = ref()
const score = ref()
const time = ref()
const id = ref()
const userName = ref()
getCommentByOrderId(props.orderId).then(res=>{
  id.value=res.data.result.id
  userId.value=res.data.result.userId
  content.value=res.data.result.content
  score.value = res.data.result.score
  time.value = parseTime(res.data.result.time)
  getNameById(userId.value).then(res=>{
    userName.value=res.data.result
  })
})
</script>

<template>
  <el-card class="comment-item-card" :body-style="{padding:'12px'}" shadow="hover">
    <el-container>
      <el-header class="header-style">
        <p class="header-text">用户{{userName}} 评价于{{time}}</p>
      </el-header>
      <el-main>
        <p class="header-text">
          <el-rate
              v-model="score"
              disabled
              show-score
              text-color="#ff9900"
              score-template="{value}分"
              size="small"/>
        </p>
        <p class="main-text">{{content}}</p>
      </el-main>
    </el-container>
  </el-card>
</template>

<style scoped>
.header-style {
  height:auto;
  display: flex;
  flex-direction: column;
}
.header-text{
  font-size: 12px;
  line-height: 2px;
  margin: 5px;
  color: #666666;
}
.main-text{
  font-size: 0.8em; /* 字体大小 */
}
</style>