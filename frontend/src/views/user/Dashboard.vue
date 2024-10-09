<script setup lang="ts">
import {computed, ref} from 'vue'
import {addDeliveryInfo, userInfo, userInfoUpdate} from '../../api/user.ts'
import {parseRole, parseTime} from "../../utils"
import {router} from '../../router'
import {UserFilled} from "@element-plus/icons-vue";

const role = sessionStorage.getItem("role")
const name = ref('')
const storeName = ref('')
const tel = ref('')
const address = ref('')
const regTime = ref()

const newName = ref('')
//0是个人信息 1是密码 2是收货地址
const displayCard = ref(0)

const password = ref('')
const confirmPassword = ref('')

const hasConfirmPasswordInput = computed(() => confirmPassword.value != '')
const isPasswordIdentical = computed(() => password.value == confirmPassword.value)
const changeDisabled = computed(() => {
  return !(hasConfirmPasswordInput.value && isPasswordIdentical.value)
})
const deliveryInfos = ref()
getUserInfo()

function getUserInfo() {
  userInfo().then(res => {
    console.log(res.data.result.deliveryInfos)
    deliveryInfos.value=res.data.result.deliveryInfos
    name.value = res.data.result.name
    tel.value = res.data.result.phone
    storeName.value = res.data.result.storeName
    address.value = res.data.result.address
    regTime.value = parseTime(res.data.result.createTime)
    newName.value = name.value
  })
}

function updateInfo() {
  userInfoUpdate({
    name: newName.value,
    password: undefined,
    address: address.value,
  }).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        customClass: 'customMessage',
        type: 'success',
        message: '更新成功！',
      })
      getUserInfo()
    } else if (res.data.code === '400') {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
    }
  })
}
function updatePassword() {
  userInfoUpdate({
    name: undefined,
    password: password.value,
    address: undefined
  }).then(res => {
    if (res.data.code === '000') {
      password.value = ''
      confirmPassword.value = ''
      ElMessageBox.alert(
          `请重新登录`,
          '修改成功',
          {
            customClass: "customDialog",
            confirmButtonText: '跳转到登录',
            type: "success",
            showClose: false,
            roundButton: true,
            center: true
          }).then(() => router.push({path: "/login"}))
    } else if (res.data.code === '400') {
      ElMessage({
        customClass: 'customMessage',
        type: 'error',
        message: res.data.msg,
      })
      password.value = ''
      confirmPassword.value = ''
    }
  })
}
// 电话号码的规则
const chinaMobileRegex = /^1(3[0-9]|4[579]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[189])\d{8}$/
const telLegal = computed(() => chinaMobileRegex.test(addPhone.value))
const hasPhoneInput = computed(() => addPhone.value != '')
const hasAddressInput = computed(() => addAddress.value != '')
const dialogOfAddDisplay=ref(false)
const addPhone = ref('')
const addAddress = ref('')
const handleAddDisabled = computed(()=>{
  return !(hasPhoneInput.value && hasAddressInput.value && telLegal.value)
})
function displayDialog(){
  dialogOfAddDisplay.value=true
}
function closeDialog() {
  dialogOfAddDisplay.value = false;
  addPhone.value='';
  addAddress.value='';
}
function handleAdd(){
  addDeliveryInfo(addAddress.value,addPhone.value).then(res => {
    if (res.data.code === '000') {
      ElMessage({
        message: "创建成功！",
        type: 'success',
        center: true,
      })
      closeDialog()
    } else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}

</script>
<template>
  <el-main class="main-container">
    <el-card class="aside-card">
      <div class="avatar-area">
        <el-avatar :icon="UserFilled" :size="80">
        </el-avatar>
        <span class="avatar-text"> 欢迎您，{{ name }}</span>
      </div>

      <el-divider></el-divider>
      <el-descriptions v-if="role==='MANAGER'"
          :column="1"
          border
          title="功能"
          style="margin-top: 20px;"
      >
        <template #extra>
          <router-link to="/createStore" v-slot="{navigate}">
            <el-button type="primary"  @click="navigate">创建商店</el-button>
          </router-link>
        </template>
      </el-descriptions>

      <el-descriptions v-if="role==='STAFF'"
                       :column="1"
                       border
                       title="功能"
                       style="margin-top: 20px;"
      >
        <template #extra>
          <router-link to="/createProduct" v-slot="{navigate}">
            <el-button type="primary"  @click="navigate">创建商品</el-button>
          </router-link>
        </template>
      </el-descriptions>



      <el-descriptions
          :column="1"
          border
          title="个人信息"
      >
        <template #extra>
          <el-button type="primary"
                     @click="displayCard = 0">
            <span>修改个人信息</span>
          </el-button>
          <el-button type="primary"
                     @click="displayCard = 1">
            <span>修改密码</span>
          </el-button>
          <el-button type="primary"
                     v-if="role==='CUSTOMER'"
                     @click="displayCard = 2">
            <span>收货信息管理</span>
          </el-button>
        </template>
        <el-descriptions-item label="身份">
          <el-tag>{{ parseRole(role) }}</el-tag>
        </el-descriptions-item>

        <el-descriptions-item label="所属商店" v-if="role === 'STAFF'">
          {{ storeName }}
        </el-descriptions-item>

        <el-descriptions-item label="联系电话">
          {{ tel }}
        </el-descriptions-item>

        <el-descriptions-item label="地址" v-if="role === 'CUSTOMER' || role === 'STAFF'||role === 'MANAGER' || role==='CEO'">
          {{ address }}
        </el-descriptions-item>

        <el-descriptions-item label="注册时间">
          {{ regTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card v-if="displayCard===0" class="change-card">
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
          <el-button @click="updateInfo">更新</el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label for="name">昵称</label>
          <el-input type="text" id="name" v-model="newName"/>
        </el-form-item>

        <el-form-item>
          <label for="phone">手机号</label>
          <el-input id="phone" v-model="tel" disabled/>
        </el-form-item>

        <el-form-item v-if="role === 'CUSTOMER' || role === 'STAFF'">
          <label for="address">收货地址</label>
          <el-input id="address" type="textarea"
                    rows="4"
                    v-model="address" placeholder="中华门"></el-input>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card v-if="displayCard===1" class="change-card">
      <template #header>
        <div class="card-header">
          <span>修改密码</span>
          <el-button @click="updatePassword" :disabled="changeDisabled">
            修改
          </el-button>
        </div>
      </template>

      <el-form>
        <el-form-item>
          <label for="password">密码</label>
          <el-input type="password" id="password" v-model="password" placeholder="•••••••••" required/>
        </el-form-item>
        <el-form-item>
          <label v-if="!hasConfirmPasswordInput" for="confirm_password">确认密码</label>
          <label v-else-if="!isPasswordIdentical" for="confirm_password" class="error-warn">密码不一致</label>
          <label v-else for="confirm_password">确认密码</label>

          <el-input type="password" id="confirm_password" v-model="confirmPassword"
                    :class="{'error-warn-input' :(hasConfirmPasswordInput && !isPasswordIdentical)}"
                    placeholder="•••••••••" required/>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card v-if="displayCard===2" class="change-card">
      <template #header>
        <div class="card-header">
          <span>我的收货信息</span>
          <el-button @click="displayDialog">
            新增收货信息
          </el-button>
        </div>
      </template>
      <el-dialog
        title="新增收货信息"
        v-model="dialogOfAddDisplay"
        @close="closeDialog">
        <el-form label-width="120px" label-position="top">
          <h4>手机号码</h4>
          <el-form-item>
            <label v-if="!hasPhoneInput" for="addPhone">
              输入手机号
            </label>
            <label v-else-if="!telLegal" for="addPhone" class="error-warn">
              手机号不合法
            </label>
            <el-input id="addPhone"
                      v-model="addPhone"
                      placeholder="请输入收货人手机号码"/>
          </el-form-item>
          <h4>收货地址</h4>
          <el-form-item>
            <el-input id="addAddress"
                      v-model="addAddress"
                      placeholder="请输入收货人地址"/>
          </el-form-item>
          <el-button type="primary"
                     @click.prevent="handleAdd"
                     :disabled="handleAddDisabled"
          >确认</el-button>
        </el-form>
      </el-dialog>
      <div v-for="info in deliveryInfos" :key="info.id" class="deliveryInfo-group">
        <el-descriptions :column="1"
                         border>
          <el-descriptions-item label="地址">
            {{ info.address }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ info.phone }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

    </el-card>
  </el-main>
</template>
<style scoped>
.deliveryInfo-group {
  margin-bottom: 20px; /* 根据需要调整间距大小 */
}
.error-warn {
  color: red;
}

.error-warn-input {
  --el-input-focus-border-color: red;
}

.main-container {
  display: flex;
  flex-direction: row;
  padding: 15px;
  gap: 5px;
  justify-content: center;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.change-card {
  width: 66%;
}

.avatar-area {
  display: flex;
  justify-content: space-around;
  align-items: center;
  gap: 30px;
}

.avatar-text {
  font-size: x-large;
  font-weight: bolder;
  padding-right: 40px;
}


</style>
