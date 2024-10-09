<script setup lang="ts" >
import {router} from '../router'
import {parseRole} from "../utils"
import {User, SwitchButton,Document,Discount,Search} from "@element-plus/icons-vue"   //图标
const role = sessionStorage.getItem('role')    //登录的时候插入的

//退出登录
function logout() {
  ElMessageBox.confirm(
      '是否要退出登录？',
      '提示',
      {
        customClass: "customDialog",
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: "warning",
        showClose: false,
        roundButton: true,
        center: true
      }
  ).then(() => {
    sessionStorage.setItem('token', '')
    router.push({path: "/login"})
  })
}
</script>


<template>
  <el-header class="custom-header" height="20">
    <el-row :gutter="10">

      <el-col :span="3" class="header-icon">
        <router-link to="/allStore" v-slot="{navigate}" class="no-link">
          <h1 @click="navigate" class="header-text"> 蓝鲸在线购物</h1>
        </router-link>
      </el-col>
      <el-col :span="2">
        <el-tag class="role-tag" size="large">{{ parseRole(role) }}版</el-tag>
      </el-col>
      <el-col :span = "0.5"></el-col>
      <el-col :span="1.5" class="nav-link">
        <router-link to="/allStore" v-slot="{navigate}" class="no-link">
          <h1 @click="navigate" class="showStore-tag">所有商店</h1>
        </router-link>
      </el-col>
      <el-col :span="1.5" class="nav-link" v-if="role==='STAFF'">
        <router-link to="/myStore" v-slot="{navigate}" class="no-link">
          <h1 @click="navigate" class="showStore-tag">我的商店</h1>
        </router-link>
      </el-col>
      <el-col :span="10"></el-col>

      <el-col :span = "1" v-if="(role==='MANAGER')||(role==='CEO')||(role==='CUSTOMER')"></el-col>
      <el-col :span="1" class="header-icon">
        <router-link to="/dashboard" v-slot="{navigate}">
          <el-icon @click="navigate" :size="35" color="white" ><User /></el-icon>
        </router-link>
      </el-col>
      <el-col :span="1" class="header-icon">
        <router-link to="/searchProduct" v-slot="{navigate}">
          <el-icon @click="navigate" :size="35" color="white"><Search /></el-icon>
        </router-link>
      </el-col>
      <el-col :span="1" class="header-icon" v-if="(role==='CUSTOMER')|| (role==='STAFF')">
        <router-link  to="/allOrder" v-slot="{navigate}">
          <el-icon @click="navigate" :size="35" color="white" ><Document /></el-icon>
        </router-link>
      </el-col>
      <el-col :span="1" class="header-icon" v-if="(role==='MANAGER')|| (role==='CEO')" >
        <router-link  to="/allOrder" v-slot="{navigate}">
          <el-icon @click="navigate" :size="35" color="white" ><Document /></el-icon>
        </router-link>
      </el-col>

      <el-col :span="1" class="header-icon">
        <router-link to="/allCoupon" v-slot="{navigate}">
          <el-icon @click="navigate" :size="35" color="white" ><Discount /></el-icon>
        </router-link>
      </el-col>


      <el-col :span="1" class="header-icon">
        <a @click="logout">
          <el-icon :size="35" color="white" ><SwitchButton /></el-icon>
        </a>
      </el-col>
    </el-row>
  </el-header>
</template>


<style scoped>
.custom-header {
  background-color: #409eff;
  border-bottom-left-radius: 20px;
  border-bottom-right-radius: 20px;

  display: flex;
  flex-direction: column;
}

.no-link {
  text-decoration: none;
}

.role-tag {
  margin-top: 20px;
  font-size: 20px;
}
.showStore-tag{
  margin-top: 20px;
  font-size: 20px;
  background-size:10px 10px;
  color: aliceblue;
}
.header-text {
  color: #ffffff;
  font-size: x-large;
  min-width: max-content;
  margin-top: 15px;
  margin-bottom: 15px;
}
.header-icon {
  display: flex;
  flex-direction: column;
  align-items:center;
  justify-content: center;
}
.nav-link {
  padding:1px 1px;
  color: #00fdda; /* 文字颜色 */
  cursor: pointer;
}
.nav-link:hover {
  background-color: #008bfd; /* 鼠标悬停时的背景色 */
}
</style>
