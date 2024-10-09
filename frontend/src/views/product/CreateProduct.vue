<!--Lab2新增-创建商品界面-->
<!--你可以选择把创建商品改成一个弹窗或其他界面的一个部分。
这样的话，就不需要有这样一个views下的创建商品文件了，因为views下的文件一般会是一个单独的界面。
但如果你觉得这一个部分的代码较多，合到其他文件里会使那个文件太长，
可以在product文件夹下创建一个components子文件夹，里面存放product模块下产生的组件界面。把这个创建商品子组件放在里面。
这个传递数据的过程可能需要用到props-->
<script setup lang="ts">
import {computed, ref} from 'vue'
import {uploadImage} from '../../api/tools'
import {createCommodity} from "../../api/product.ts";
import {getAllStoresInfo} from "../../api/store.ts";
import {userInfo} from "../../api/user.ts";
import{ subTypeList } from "../../utils"
const role = sessionStorage.getItem("role")
//这边是需要实现上传至少2张图片。可以从CreateStore里提供的代码改，也可以自己搜了写。
const name = ref('')//商品名
const StoreId = ref()
const price = ref()
const type = ref()//商品类型
const description = ref('')//商品介绍
const inventory = ref()//库存
const subType = ref()//子类
const hasNameInput = computed(() => name.value !='')
const hasStoreIdInput = computed(()=> StoreId.value!=0)
const hasPrice = computed(()=> price.value!=0)
const hasType = computed(()=>type.value!='')
const hasDescription = computed(()=>description.value!='')
const hasInventory = computed(()=>inventory.value!='')
const hasLogoUrls = computed(()=>logoUrlList.value.length>=2)
const hasSubType = computed(()=> subType.value !=0)
// 价格的规则
const priceRegex = /^(([1-9][0-9]*)|0{1})(\.\d{2})$/
const priceLegal = computed(() => priceRegex.test(price.value))
// 库存的规则
const inventoryRegex = /^[1-9][0-9]*$/
const inventoryLegal = computed(() => inventoryRegex.test(inventory.value))


interface store {
  id:number;
  name:string;
}
const stores = ref<store[]>([]);
const storeID = ref(0)
const filteredStores = computed(() => {
  return stores.value.filter(store => store.id === storeID.value);
});
getUserInfo()
function getUserInfo() {
  userInfo().then(res => {
    storeID.value = res.data.result.storeId
  })
  console.log(storeID)
}

function getAllStores() {
  getAllStoresInfo().then(res => {
    stores.value = res.data.result;
  })
}
getAllStores()

//上传多个照片
const logoUrl = ref('')
const logoUrlList = ref<string[]>([])
const imageFileList = ref([])
function handleChange(file:any, fileList:any) {
  imageFileList.value = fileList
  let formData = new FormData();
  formData.append('file',file.raw)
    uploadImage(formData).then(res => {
      logoUrl.value = res.data.result; // 将新URL添加到现有数组中
      logoUrlList.value.push(logoUrl.value)
    }).catch(error => {
      // 处理上传错误
      console.error('上传图片失败:', error);
    });
  console.log(file)
  console.log(fileList)
}
//function handleExceed(){
 // ElMessage.warning(`当前限制选择 4 个文件`);
//}
function uploadHttpRequest() {
  return new XMLHttpRequest()
}



function handleCreateCommodity(){
  createCommodity({
    storeId:StoreId.value,
    name:name.value,
    picLink: logoUrlList.value,
    inventory:inventory.value,
    type:type.value,
    price:price.value,
    description:description.value,
    subType:subType.value,
  }).then(res =>{
    if (res.data.code === '000'){
      ElMessage({
        message: "创建成功！",
        type: 'success',
        center: true,
      })
    }else if (res.data.code === '400') {
      ElMessage({
        message: res.data.msg,
        type: 'error',
        center: true,
      })
    }
  })
}
const createCommodityDisabled = computed(() => {
  return !(hasNameInput.value
      && hasStoreIdInput.value
      && hasPrice.value
      && hasType.value
      && hasDescription.value
      && hasInventory.value
      && hasLogoUrls.value
      && priceLegal.value
      && inventoryLegal.value
      && hasSubType.value)
})

</script>
<template>
  <el-main>
    <div class="page-container">
      <div class="big-title">创建商品</div>
      <el-card class="createStore-card">
        <el-form>
          <h4>选择我的商店</h4>
          <el-form-item>
            <el-select id="storeId"
                       v-model="StoreId"
                       placeholder="请选择"
                       style="width: 100%;">
              <el-option
                  v-for="store in filteredStores"
                  :key="store.id"
                  :value="store.id"
                  :label="store.name">
              </el-option>
            </el-select>
          </el-form-item>
          <h4>商品名称</h4>
          <el-form-item>
            <el-input id="name"
                      v-model="name"
                      placeholder="请输入商品名称"/>
          </el-form-item>
          <h4>设置价格</h4>
          <el-form-item>
            <el-input id="price"
                      v-model="price" :class="{'error-warn-input' :(hasPrice && !priceLegal)}"
                      placeholder="请输入商品价格（单位为元 输入格式：xx.xx  例如：88.88) 精确到小数点后2位（分）"/>
            <label v-if="!hasPrice" for="price">输入价格</label>
            <label v-else-if="!priceLegal" for="price" class="error-warn">价格格式不正确</label>
            <label v-else for="price">输入价格</label>
          </el-form-item>
          <h4>商品类型</h4>
          <el-form-item>
            <el-select id="type"
                       v-model="type"
                       placeholder="请选择"
                       style="width: 100%;">
              <el-option value="FOOD" label="食品"/>
              <el-option value="CLOTHES" label="服饰"/>
              <el-option value="FURNITURE" label="家具"/>
              <el-option value="ELECTRONICS" label="电子产品"/>
              <el-option value="ENTERTAINMENT" label="娱乐"/>
              <el-option value="SPORTS" label="体育产品"/>
              <el-option value="LUXURY" label="奢侈品"/>
            </el-select>
          </el-form-item>
          <h4>详细类型</h4>
          <el-form-item>
            <el-select id="subType"
                       v-model="subType"
                       placeholder="请选择详细分类"
                       style="width: 100%;">
              <template v-for="item in subTypeList">
                <el-option v-if="item.outerType === type"
                           :value="item.name"
                           :label="item.label">
                </el-option>
              </template>

            </el-select>
          </el-form-item>
          <h4>设置库存</h4>
          <el-form-item>
            <el-input id="inventory"
                      v-model="inventory" :class="{'error-warn-input' :(hasInventory && !inventoryLegal)}"
                      placeholder="请输入商品库存"/>
            <label v-if="!hasInventory" for="inventory">输入库存</label>
            <label v-else-if="!inventoryLegal" for="inventory" class="error-warn">库存格式不正确（必须为非负整数）</label>
            <label v-else for="inventory">输入库存</label>
          </el-form-item>
          <h4>商品介绍</h4>
          <el-form-item>
            <el-input id="description"
                      v-model="description"
                      placeholder="请输入商品介绍"/>
          </el-form-item>
          <h4>商品图片</h4>
          <el-form-item>
            <el-upload
                v-model:file-list="imageFileList"
                :on-change="handleChange"
                :on-remove="handleChange"
                class="upload-demo"
                list-type="picture"
                :http-request="uploadHttpRequest"
                drag>
              <el-icon class="el-icon--upload">
                <upload-filled/>
              </el-icon>
              <div class="el-upload__text">
                将商店图片文件拖到此处或单击此处上传。上传至少两张图片。
              </div>
            </el-upload>
          </el-form-item>
          <span class="button-group">
            <el-button @click.prevent="handleCreateCommodity"
                       :disabled="createCommodityDisabled"
                       type="primary">
              确认创建
            </el-button>
            <router-link to="/myStore" v-slot="{navigate}">
              <el-button @click="navigate">
                返回我的商店
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
.error-warn {
  color: red;
}
.error-warn-input {
  --el-input-focus-border-color: red;
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
