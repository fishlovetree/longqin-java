<!-- 用户管理 -->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 部门树 -->
      <el-col :lg="4" :xs="24" class="mb-[12px]">
        <dept-tree v-model="queryParams.departmentId" @node-click="handleQuery" />
      </el-col>

      <!-- 用户列表 -->
      <el-col :lg="20" :xs="24">
        <div class="search-container">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="关键字" prop="nickName">
              <el-input
                v-model="queryParams.nickName"
                placeholder="昵称"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleQuery"
                ><i-ep-search />搜索</el-button
              >
              <el-button @click="handleResetQuery">
                <i-ep-refresh />
                重置</el-button
              >
            </el-form-item>
          </el-form>
        </div>

        <el-card shadow="never" class="table-container">
          <template #header>
            <div class="flex-x-between">
              <div>
                <el-button
                  type="success"
                  @click="handleOpenDialog()"
                  ><i-ep-plus />新增</el-button
                >
              </div>
              <div>
                <el-button class="ml-3" @click="handleOpenImportDialog"
                  ><template #icon><i-ep-upload /></template>导入</el-button
                >

                <el-button class="ml-3" @click="handleExport"
                  ><template #icon><i-ep-download /></template>导出</el-button
                >
              </div>
            </div>
          </template>

          <el-table
            v-loading="loading"
            :data="pageData"
          >
            <el-table-column
              key="username"
              label="账号名"
              width="180"
              align="center"
              prop="userName"
            />
            <el-table-column
              label="昵称"
              min-width="180"
              align="center"
              prop="nickName"
            />

            <el-table-column
              label="部门"
              width="120"
              align="center"
              prop="departmentName"
            />
            <el-table-column
              label="职位"
              width="120"
              align="center"
              prop="positionName"
            />
            <el-table-column label="头像" prop="avatar" width="120">
              <template #default="scope">
                <el-avatar shape="square" :size="50" :src="scope.row.avatar" />
              </template>
            </el-table-column>
            <el-table-column
              label="邮箱"
              align="center"
              prop="email"
              width="160"
            />
            <el-table-column
              label="手机号码"
              align="center"
              prop="phone"
              width="120"
            />

            <el-table-column label="操作" fixed="right" width="220">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  link
                  @click="handleResetPassword(scope.row)"
                  ><i-ep-refresh-left />重置密码</el-button
                >
                <el-button
                  type="primary"
                  link
                  size="small"
                  @click="handleOpenDialog(scope.row)"
                  ><i-ep-edit />编辑</el-button
                >
                <el-button
                  type="danger"
                  link
                  size="small"
                  @click="handleDelete(scope.row.userId)"
                  ><i-ep-delete />删除</el-button
                >
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
      </el-col>
    </el-row>

    <!-- 用户表单弹窗 -->
    <el-drawer
      v-model="dialog.visible"
      :title="dialog.title"
      append-to-body
      @close="handleCloseDialog"
    >
      <!-- 用户新增/编辑表单 -->
      <el-form
        ref="userFormRef"
        :model="formData"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="所属公司" prop="organizationId" v-if="showOrganization">
          <el-select v-model="formData.organizationId" placeholder="请选择所属公司">
            <el-option
            v-for="item in organizationList"
            :key="item.organizationId"
            :value="item.organizationId"
            :label="item.organizationName"/>
          </el-select>
        </el-form-item>
        <el-form-item label="账号名" prop="userName">
          <el-input
            v-model="formData.userName"
            :readonly="!!formData.userId"
            placeholder="请输入账号名"
          />
        </el-form-item>

        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="formData.nickName" placeholder="请输入昵称" />
        </el-form-item>

        <el-form-item label="所属部门" prop="departmentId">
          <el-tree-select
            v-model="formData.departmentId"
            placeholder="请选择所属部门"
            :data="departmentOptions"
            filterable
            check-strictly
            :render-after-expand="false"
            node-key="id"
            :props="departmentTreeProps"
          />
        </el-form-item>

        <el-form-item label="职位" prop="positionId">
          <el-tree-select
            v-model="formData.positionId"
            placeholder="请选择职位"
            :data="positionOptions"
            filterable
            check-strictly
            :render-after-expand="false"
            node-key="id"
            :props="positionTreeProps"
          />
        </el-form-item>

        <el-form-item label="角色" prop="roleIds">
          <el-select v-model="roleIds" multiple placeholder="请选择">
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            />
          </el-select>
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
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 用户导入弹窗 -->
    <user-import
      v-model:visible="importDialogVisible"
      @import-success="handleOpenImportDialogSuccess"
    />
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "User",
  inheritAttrs: false,
});

import UserAPI, { UserForm, UserPageQuery, UserPageVO } from "@/api/user";
import OrganizationAPI, { OrganizationPageVO } from "@/api/organization";
import DeptAPI from "@/api/dept";
import PositionAPI from "@/api/position";
import RoleAPI from "@/api/role";

const queryFormRef = ref(ElForm);
const userFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);
const pageData = ref<UserPageVO[]>();

const showOrganization = sessionStorage.getItem('organizationId') == 0;
const organizationList = ref<OrganizationPageVO[]>();

/** 部门下拉选项 */
const departmentOptions = ref<DeptTree[]>();
  const departmentTreeProps = reactive({
  label: 'title'
})
/** 职位下拉选项 */
const positionOptions = ref<PositionTree[]>();
  const positionTreeProps = reactive({
  label: 'title'
})
/** 角色下拉选项 */
const roleOptions = ref<OptionType[]>();
const roleIds = ref([]);

/** 用户查询参数  */
const queryParams = reactive<UserPageQuery>({
  page: 1,
  size: 10,
});

/**  用户弹窗对象  */
const dialog = reactive({
  visible: false,
  title: "",
});

/** 导入弹窗显示状态 */
const importDialogVisible = ref(false);

// 用户表单数据
const formData = reactive<UserForm>({});

/** 用户表单校验规则  */
const rules = reactive({
  userName: [
    { required: true, message: "用户名不能为空", trigger: "blur" },
    {
      pattern: /^[A-Za-z0-9]+$/,
      message: "请输入数字和英文",
      trigger: "blur",
    },
    { min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' },
  ],
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

/** 查询 */
function handleQuery() {
  loading.value = true;
  UserAPI.getPage(queryParams)
    .then(({data}) => {
      pageData.value = data.list;
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
  queryParams.departmentId = undefined;
  queryParams.nickName = undefined;
  handleQuery();
}

/** 重置密码 */
function handleResetPassword(row: { [key: string]: any }) {
  ElMessageBox.prompt(
    "请输入账号「" + row.userName + "」的新密码",
    "重置密码",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
    }
  ).then(({ value }) => {
    if (!value || value.length < 6) {
      // 检查密码是否为空或少于6位
      ElMessage.warning("密码至少需要6位字符，请重新输入");
      return false;
    }
    UserAPI.resetPassword(row.userId, value).then(() => {
      ElMessage.success("密码重置成功，新密码是：" + value);
    });
  });
}

/**
 * 打开弹窗
 *
 * @param id 用户ID
 */
async function handleOpenDialog(user) {
  dialog.visible = true;
  // 加载角色下拉数据源
  let roles = await RoleAPI.getList();
  roleOptions.value = roles.data;
  roleIds.value = [];
  if (user && user.roleIds){
    let arr = user.roleIds.split(',');
    arr.forEach(element => {
      roleIds.value.push(parseInt(element))
    });
  }
  // 加载公司下拉数据源
  let orgs = await OrganizationAPI.getList();
  organizationList.value = orgs.data;
  // 加载部门下拉数据源
  let depts = await DeptAPI.getTree();
  departmentOptions.value = depts.data;
  // 加载职位下拉数据源
  let positions = await PositionAPI.getTree();
  positionOptions.value = positions.data;

  if (user && user.userId) {
    dialog.title = "修改用户";
    Object.assign(formData, { ...user });
  } else {
    dialog.title = "新增用户";
  }
}

/** 关闭弹窗 */
function handleCloseDialog() {
  dialog.visible = false;
  userFormRef.value.resetFields();
  userFormRef.value.clearValidate();

  formData.userId = undefined;
  formData.userName = '';
  formData.nickName = '';
  formData.email = '';
  formData.phone = '';
  formData.roleIds = '';
  formData.departmentId = undefined;
  formData.positionId = undefined;
}

/** 表单提交 */
const handleSubmit = useThrottleFn(() => {
  userFormRef.value.validate((valid: any) => {
    if (valid) {
      const userId = formData.userId;
      loading.value = true;
      formData.roleIds = roleIds.value.join();
      if (userId) {
        UserAPI.update(formData)
          .then(() => {
            ElMessage.success("修改用户成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        UserAPI.add(formData)
          .then(() => {
            ElMessage.success("新增用户成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}, 3000);

/** 删除用户 */
function handleDelete(id?: number) {
  ElMessageBox.confirm("确认删除用户?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    function () {
      loading.value = true;
      UserAPI.deleteById(id)
        .then(() => {
          ElMessage.success("删除成功");
          handleResetQuery();
        })
        .finally(() => (loading.value = false));
    },
    function () {
      ElMessage.info("已取消删除");
    }
  );
}

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

/** 打开导入弹窗 */
function handleOpenImportDialog() {
  importDialogVisible.value = true;
}

/** 导入用户成功 */
function handleOpenImportDialogSuccess() {
  handleQuery();
}

/** 导出用户 */
function handleExport() {
  UserAPI.export(queryParams).then((response: any) => {
    const fileData = response.data;
    const fileName = decodeURI(
      response.headers["content-disposition"].split(";")[1].split("=")[1]
    );
    const fileType =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8";

    const blob = new Blob([fileData], { type: fileType });
    const downloadUrl = window.URL.createObjectURL(blob);

    const downloadLink = document.createElement("a");
    downloadLink.href = downloadUrl;
    downloadLink.download = fileName;

    document.body.appendChild(downloadLink);
    downloadLink.click();

    document.body.removeChild(downloadLink);
    window.URL.revokeObjectURL(downloadUrl);
  });
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