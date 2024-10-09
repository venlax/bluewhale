<!--Lab2新增-创建商店界面-->
<!--你可以选择把创建商店改成一个弹窗或其他界面的一个部分。
这样的话，就不需要有这样一个views下的创建商店文件了，因为views下的文件一般会是一个单独的界面。
但如果你觉得这一个部分的代码较多，合到其他文件里会使那个文件太长，
可以在store文件夹下创建一个components子文件夹，里面存放store模块下产生的组件界面。把这个创建商店子组件放在里面
这个传递数据的过程可能需要用到props-->

<script setup lang="ts">
import {computed, ref} from 'vue'
import {uploadImage} from '../../api/tools'
import {createStore} from "../../api/store.ts";
//import {router} from "../../router";
const name = ref()
//这里为大家提供上传且仅能上传1张图片的代码实现。
const introduction=ref('')
const imageFileList = ref([])
const logoUrl = ref('')
const address=ref('')
const hasAddressInput = computed(()=> address.value!='')
const hasIntroductionInput = computed(() => introduction.value !='')
const hasNameInput = computed(() => name.value !='')
const hasLogoInput = computed(() => logoUrl.value !='')


//创建按钮可用性
const createStoreDisabled = computed(() => {
  return !(hasAddressInput.value&&hasNameInput.value && hasLogoInput.value && hasIntroductionInput.value)
})

function handleChange(file: any, fileList: any) {
  imageFileList.value = fileList
  let formData = new FormData()
  formData.append('file', file.raw)
  uploadImage(formData).then(res => {
    logoUrl.value = res.data.result
  })
}
function handleRemove(file: any, fileList: any){
  logoUrl.value = ''
}

function handleExceed() {
  ElMessage.warning(`当前限制选择 1 个文件`);
}

function uploadHttpRequest() {
  return new XMLHttpRequest()
}
//创建商店按钮触发
function handleCreateStore() {
  createStore({
    name : name.value,
    logo: logoUrl.value,
    introduction:introduction.value,
    address:address.value
  }).then(res => {
    if (res.data.code === '000') {  //类型守卫，它检查 res.data 对象中是否存在名为 code 的属性
      ElMessage({
        message: "创建成功！",
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
</script>
<script>
</script>
<template>
  <el-main>
    <div class="page-container">
      <div class="big-title">创建商店</div>
      <el-card class="createStore-card">
        <h1>商店名称</h1>
        <el-form>
          <el-form-item>
            <el-input id="name"
                      v-model="name"
                      placeholder="请输入商店名称"/>
          </el-form-item>
          <h2>商店地址</h2>
          <el-form-item>
            <el-input id="address"
                      v-model="address"
                      placeholder="请输入商店地址"/>
          </el-form-item>
          <h2>商店简介</h2>
          <el-form-item>
            <el-input id="introduction"
                      v-model="introduction"
                      placeholder="请输入商店简介"/>
          </el-form-item>
          <h2>商店logo</h2>
          <el-form-item>
            <el-upload
                v-model:file-list="imageFileList"
                :limit="1"
                :on-change="handleChange"
                :on-exceed="handleExceed"
                :on-remove="handleRemove"
                class="upload-demo"
                list-type="picture"
                :http-request="uploadHttpRequest"
                drag>
              <el-icon class="el-icon--upload">
                <upload-filled/>
              </el-icon>
              <div class="el-upload__text">
                将商店图片文件拖到此处或单击此处上传。仅允许上传一份文件。
              </div>
            </el-upload>
          </el-form-item>
          <span class="button-group">
            <el-button @click.prevent="handleCreateStore"
                       :disabled="createStoreDisabled"
                       type="primary">
              确认创建
            </el-button>
            <router-link to="/allStore" v-slot="{navigate}">
              <el-button @click="navigate">
                返回主界面
              </el-button>
            </router-link>
          </span>
        </el-form>
      </el-card>

  </div>
  </el-main>
</template>

<style scoped>
.page-container {
  position: relative; /* 确保大标题是相对于这个容器定位的 */

  /* 其他样式 */
}

.big-title {
  position: relative; /* 绝对定位，相对于最近的定位祖先元素（在这里是 .page-container） */
  top: 10px; /* 距离容器顶部的距离 */
  left: 20px; /* 距离容器左侧的距离 */
  font-size: 32px; /* 字体大小 */
  font-weight: bold; /* 字体加粗 */
  color: #409eff;
  /* 其他样式，如颜色、字体家族等 */
}
.createStore-card{
  position: relative;
  top:30px;
  left:50px;
  width: 1250px;
  height: auto;
}
.button-group {
  padding-top: 10px;
  display: flex;
  flex-direction: row;
  gap: 30px;
  align-items: center;
  justify-content: right;
}
.bgimage {
  background-image: url("../../assets/shopping-1s-1084px.svg");
}
</style>
