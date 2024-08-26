<!-- 账号设置 -->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :lg="24" :xs="24" class="mb-[12px]">
        <el-card style="min-width: 480px">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          <el-form
            ref="userFormRef"
            :model="formData"
            :rules="rules"
            label-width="80px"
          >
          
            <el-form-item label="账号名" prop="userName">
              <el-input
                v-model="formData.userName"
                placeholder="请输入账号名"
                readonly
              />
            </el-form-item>

            <el-form-item label="昵称" prop="nickName">
              <el-input v-model="formData.nickName" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="头像" prop="avatar">
              <el-upload
                class="avatar-uploader"
                action="/api/upload/uploadFile"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                accept="image/*"
              >
                <img v-if="formData.avatar" :src="formData.avatar" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
              </el-upload>
            </el-form-item>

            <el-form-item label="手机号码" prop="phone">
              <el-input
                v-model="formData.phone"
                placeholder="请输入手机号码"
                maxlength="11"
              />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="formData.email"
                placeholder="请输入邮箱"
                maxlength="50"
              />
            </el-form-item>
          </el-form>

          <template #footer>
            <div class="dialog-footer">
              <el-button type="primary" @click="handleSubmit">确 定</el-button>
              <el-button @click="handleReset">重 置</el-button>
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

import UserAPI, { UserForm } from "@/api/user";

const userFormRef = ref(ElForm);

// 用户表单数据
const formData = reactive<UserForm>({});

/** 用户表单校验规则  */
const rules = reactive({
  nickName: [
    { required: true, message: "用户昵称不能为空", trigger: "blur" },
    { min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' },
  ],
  // roleIds: [{ required: true, message: "用户角色不能为空", trigger: "blur" }],
  email: [
    {
      pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/,
      message: "请输入正确的邮箱地址",
      trigger: "blur",
    },
  ],
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
  userFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      UserAPI.update(formData)
        .then(() => {
          ElMessage.success("设置成功");
          sessionStorage.setItem("userName", formData.userName);
          sessionStorage.setItem("nickName", formData.nickName);
          sessionStorage.setItem("avatar", formData.avatar);
          sessionStorage.setItem("phone", formData.phone);
          sessionStorage.setItem("email", formData.email);
        })
        .finally(() => (loading.value = false));
    }
  });
}, 3000);

const handleAvatarSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  formData.avatar = response.data
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('图片大小不能超过10MB!')
    return false
  }
  return true
}

/** 重置 */
function handleReset() {
  formData.userName = sessionStorage.getItem("userName");
  formData.nickName = sessionStorage.getItem("nickName");
  formData.avatar = sessionStorage.getItem("avatar");
  formData.phone = sessionStorage.getItem("phone");
  formData.email = sessionStorage.getItem("email");
}

onMounted(() => {
  formData.userId = sessionStorage.getItem("userId");
  formData.userName = sessionStorage.getItem("userName");
  formData.nickName = sessionStorage.getItem("nickName");
  formData.avatar = sessionStorage.getItem("avatar");
  formData.phone = sessionStorage.getItem("phone");
  formData.email = sessionStorage.getItem("email");
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