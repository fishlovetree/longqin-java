<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="keywords" label="关键字">
          <el-input
            v-model="queryParams.keywords"
            placeholder="公司名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><i-ep-search />搜索</el-button
          >
          <el-button @click="handleResetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button type="success" @click="handleOpenDialog()"
          ><i-ep-plus />新增</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="organizationList"
        highlight-current-row
        border
      >
        <el-table-column label="公司编号" prop="organizationCode" min-width="100" />
        <el-table-column label="公司名称" prop="organizationName" width="150" />
        <el-table-column label="地址" prop="address" width="150" />
        <el-table-column label="联系电话" prop="phone" width="150" />
        <el-table-column label="LOGO" prop="logoPath" width="150">
          <template #default="scope">
            <el-avatar shape="square" :size="50" :src="scope.row.logoPath" />
          </template>
        </el-table-column>
        <el-table-column label="简介" prop="systemName" width="150" />
        <el-table-column label="系统名称" prop="introduction" width="150" />
        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleOpenDialog(scope.row)"
            >
              <i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              link
              @click="handleDelete(scope.row.organizationId)"
            >
              <i-ep-delete />删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.page"
        v-model:limit="queryParams.size"
        @pagination="handleQuery"
      />
    </el-card>

    <!-- 公司表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="handleCloseDialog"
    >
      <el-form
        ref="organizationFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
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
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Organization",
  inheritAttrs: false,
});

import OrganizationAPI, { OrganizationPageVO, OrganizationForm, OrganizationPageQuery } from "@/api/organization";

const queryFormRef = ref(ElForm);
const organizationFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<OrganizationPageQuery>({
  page: 1,
  size: 10,
});

// 公司表格数据
const organizationList = ref<OrganizationPageVO[]>();

// 弹窗
const dialog = reactive({
  title: "",
  visible: false,
});
// 公司表单
const formData = reactive<OrganizationForm>({
  organizationCode: '',
  organizationId: null,
  organizationName: '',
  address: '',
  phone: '',
  logoPath: '',
  systemName: '',
  introduction: '',
});
const validatePhone = (rule, value, callback) => {
  const reg = /^1[3-9]\d{9}$/;
  if (!value) {
    return callback(new Error('手机号码不能为空'));
  }
  if (!reg.test(value)) {
    return callback(new Error('请输入正确的手机号码'));
  }
  callback();
};
const rules = reactive({
  organizationName: [{ required: true, message: "请输入公司名称", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  organizationCode: [{ required: true, message: "请输入公司编码", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  address: [{ max: 50, message: '输入长度在 0 到 50 个字符', trigger: 'blur' }],
  phone: [{ validator: validatePhone, trigger: 'blur' }],
  systemName: [{ max: 50, message: '输入长度在 0 到 25 个字符', trigger: 'blur' }],
  introduction: [{ max: 50, message: '输入长度在 0 到 25 个字符', trigger: 'blur' }],
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  OrganizationAPI.getPage(queryParams)
    .then(({data}) => {
      organizationList.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 重置查询 */
function handleResetQuery() {
  queryFormRef.value.resetFields();
  queryParams.page = 1;
  handleQuery();
}

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

/** 打开公司弹窗 */
function handleOpenDialog(organization) {
  dialog.visible = true;
  if (organization && organization.organizationId) {
    dialog.title = "修改公司";
    Object.assign(formData, organization);
  } else {
    dialog.title = "新增公司";
  }
}

/** 提交公司表单 */
function handleSubmit() {
  organizationFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const organizationId = formData.organizationId;
      if (organizationId) {
        OrganizationAPI.update(formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        OrganizationAPI.add(formData)
          .then(() => {
            ElMessage.success("新增成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

/** 关闭公司弹窗 */
function handleCloseDialog() {
  dialog.visible = false;

  organizationFormRef.value.resetFields();
  organizationFormRef.value.clearValidate();

  formData.organizationCode = '';
  formData.organizationId = null;
  formData.organizationName = '';
  formData.address = '';
  formData.phone = '';
  formData.logoPath = '';
  formData.systemName = '';
  formData.introduction = '';
}

/** 删除公司 */
function handleDelete(organizationId?: number) {
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      OrganizationAPI.deleteById(organizationId)
        .then(() => {
          ElMessage.success("删除成功");
          handleResetQuery();
        })
        .finally(() => (loading.value = false));
    },
    () => {
      ElMessage.info("已取消删除");
    }
  );
}

onMounted(() => {
  handleQuery();
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