<template>
  <div class="app-container">

    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button
          type="success"
          @click="handleOpenDialog(undefined, undefined)"
          ><i-ep-plus />新增</el-button
        >
      </template>

      <el-table
        v-loading="loading"
        :data="deptList"
        row-key="departmentId"
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="departmentName" label="部门名称" min-width="200" />
        <el-table-column prop="description" label="备注" width="200" />

        <el-table-column label="操作" fixed="right" align="left" width="200">
          <template #default="scope">
            <el-button
              type="primary"
              link
              size="small"
              @click.stop="handleOpenDialog(scope.row.departmentId, undefined)"
              ><i-ep-plus />新增
            </el-button>
            <el-button
              type="primary"
              link
              size="small"
              @click.stop="handleOpenDialog(scope.row.parentId, scope.row)"
              ><i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              link
              size="small"
              @click.stop="handleDelete(scope.row.departmentId)"
            >
              <i-ep-delete />删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="600px"
      @closed="handleCloseDialog"
    >
      <el-form
        ref="deptFormRef"
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
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            placeholder="选择上级部门"
            :data="deptList"
            filterable
            check-strictly
            :render-after-expand="false"
            node-key="id"
            :props="deptTreeProps"
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="departmentName">
          <el-input v-model="formData.departmentName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="备注" prop="description">
          <el-input v-model="formData.description" placeholder="请输入部门备注" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit"> 确 定 </el-button>
          <el-button @click="handleCloseDialog"> 取 消 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Dept",
  inheritAttrs: false,
});

import DeptAPI, { DeptTree, DeptForm } from "@/api/dept";
import OrganizationAPI, { OrganizationPageVO } from "@/api/organization";

const deptFormRef = ref(ElForm);

const loading = ref(false);
const dialog = reactive({
  title: "",
  visible: false,
});

const deptList = ref<DeptTree[]>();
const deptTreeProps = reactive({
  label: 'title'
})

const showOrganization = sessionStorage.getItem('organizationId') == 0;
const organizationList = ref<OrganizationPageVO[]>();

const formData = reactive<DeptForm>({
  departmentId: undefined,
  parentId: -1,
  departmentName: '',
  description: '',
  organizationId: 0,
});

const rules = reactive({
  departmentName: [{ required: true, message: "部门名称不能为空", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  description: [{ max: 25, message: '输入长度在 0 到 50 个字符', trigger: 'blur' }],
});

/** 查询部门 */
function handleQuery() {
  loading.value = true;
  DeptAPI.getTree().then(({data}) => {
    deptList.value = data;
    loading.value = false;
  });
}

/**
 * 打开部门弹窗
 *
 * @param parentId 父部门ID
 * @param department 部门信息
 */
async function handleOpenDialog(parentId, department) {
  // 获取公司集合
  OrganizationAPI.getList()
    .then(({data}) => {
      organizationList.value = data;
  });
  dialog.visible = true;
  if (department && department.departmentId) {
    dialog.title = "修改部门";
    Object.assign(formData, department);
  } else {
    dialog.title = "新增部门";
    formData.parentId = parentId ?? -1;
  }
}

/** 提交部门表单 */
function handleSubmit() {
  deptFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const deptId = formData.departmentId;
      if (deptId) {
        DeptAPI.update(formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        DeptAPI.add(formData)
          .then(() => {
            ElMessage.success("新增成功");
            handleCloseDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

/** 删除部门 */
function handleDelete(deptId?: number) {
  ElMessageBox.confirm(`确认删除已选中的数据项?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      DeptAPI.deleteById(deptId)
        .then(() => {
          ElMessage.success("删除成功");
          handleQuery();
        })
        .finally(() => (loading.value = false));
    },
    () => {
      ElMessage.info("已取消删除");
    }
  );
}

/** 关闭弹窗 */
function handleCloseDialog() {
  dialog.visible = false;

  deptFormRef.value.resetFields();
  deptFormRef.value.clearValidate();

  formData.departmentId = undefined;
  formData.parentId = -1;
  formData.departmentName = '';
  formData.description = '';
  formData.organizationId = 0;
}

onMounted(() => {
  handleQuery();
});
</script>