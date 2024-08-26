<!-- 系统设置 -->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :lg="24" :xs="24" class="mb-[12px]">
        <el-card style="min-width: 480px">
          <template #header>
            <div class="card-header">
              <span>系统设置</span>
            </div>
          </template>
          <el-form
            ref="organizationFormRef"
            :model="formData"
            :rules="rules"
            label-width="100px"
            :disabled="isDisabled"
          >
            <el-form-item label="公司编码" prop="organizationCode">
              <el-input v-model="formData.organizationCode" placeholder="请输入公司编码" />
            </el-form-item>

            <el-form-item label="公司名称" prop="organizationName">
              <el-input v-model="formData.organizationName" placeholder="请输入公司名称" />
            </el-form-item>

            <el-form-item label="地址" prop="address">
              <el-input v-model="formData.address" placeholder="请输入公司地址" />
            </el-form-item>

            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>

            <el-form-item label="LOGO" prop="logoPath">
              <el-upload
                class="avatar-uploader"
                action="/api/upload/uploadFile"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                accept="image/*"
              >
                <img v-if="formData.logoPath" :src="formData.logoPath" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>

            <el-form-item label="系统名称" prop="systemName">
              <el-input v-model="formData.systemName" placeholder="请输入系统名称" />
            </el-form-item>

            <el-form-item label="公司简介" prop="introduction">
              <el-input v-model="formData.introduction" placeholder="请输入公司简介" />
            </el-form-item>
          </el-form>

          <template #footer>
            <div class="dialog-footer">
              <el-button type="primary" @click="handleSubmit" :disabled="isDisabled">确 定</el-button>
              <el-button @click="handleReset" :disabled="isDisabled">重 置</el-button>
            </div>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Baseset",
  inheritAttrs: false,
});

import OrganizationAPI, { OrganizationForm } from "@/api/organization";

const organizationFormRef = ref(ElForm);

// 表单数据
const formData = reactive<OrganizationForm>({});
// 超级管理员禁用
const isDisabled = ref(false);

/** 用户表单校验规则  */
const rules = reactive({
  organizationName: [{ required: true, message: "请输入公司名称", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  organizationCode: [{ required: true, message: "请输入公司编码", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  address: [{ max: 50, message: '输入长度在 0 到 50 个字符', trigger: 'blur' }],
  systemName: [{ max: 50, message: '输入长度在 0 到 25 个字符', trigger: 'blur' }],
  introduction: [{ max: 50, message: '输入长度在 0 到 25 个字符', trigger: 'blur' }],
  phone: [
    {
      pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
      message: "请输入正确的手机号码",
      trigger: "blur",
    },
  ],
});

const loading = ref(false);

/** 表单提交 */
const handleSubmit = useThrottleFn(() => {
  organizationFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      OrganizationAPI.update(formData)
        .then(() => {
          ElMessage.success("设置成功");
          sessionStorage.setItem('systemName', formData.systemName);
          sessionStorage.setItem('logoPath', formData.logoPath);
        })
        .finally(() => (loading.value = false));
    }
  });
}, 3000);

const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  formData.logoPath = response.data
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('图片大小不能超过10MB!')
    return false
  }
  return true
}

/** 重置 */
async function handleReset() {
  // 加载公司下拉数据源
  let orgId = sessionStorage.getItem("organizationId");
  if (orgId && orgId != '0'){
    let data = await OrganizationAPI.getById(parseInt(orgId));
    formData.organizationId = parseInt(orgId);
    formData.organizationCode = data.data.organizationCode;
    formData.organizationName = data.data.organizationName;
    formData.address = data.data.address;
    formData.phone = data.data.phone;
    formData.logoPath = data.data.logoPath;
    formData.systemName = data.data.systemName;
    formData.introduction = data.data.introduction;
    isDisabled.value = false;
  }
  else{
    isDisabled.value = true;
  }
  
}

onMounted(() => {
  handleReset();
});
</script>

<style scoped>
  .avatar-uploader .avatar {
    width: 108px;
    height: 108px;
    display: block;
  }
</style>
  
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }
  
  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }
  
  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
  }
</style>