<template>
  <div class="app-container">
    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button type="success" @click="handleOpenDialog()"
          ><i-ep-plus />新增</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="roleList"
        highlight-current-row
        border
      >
        <el-table-column label="角色名称" prop="roleName" min-width="100" />
        <el-table-column label="备注" prop="description" width="250" />

        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleOpenAssignPermDialog(scope.row)"
            >
              <i-ep-position />分配权限
            </el-button>
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
              @click="handleDelete(scope.row.roleId)"
            >
              <i-ep-delete />删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 角色表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="handleCloseDialog"
    >
      <el-form
        ref="roleFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>

        <el-form-item label="备注" prop="description">
          <el-input v-model="formData.description" placeholder="请输入角色备注" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 分配权限弹窗 -->
    <el-drawer
      v-model="assignPermDialogVisible"
      :title="'【' + checkedRole.roleName + '】权限分配'"
      size="500"
    >
      <div class="flex-x-between">
        <el-input
          v-model="permKeywords"
          clearable
          class="w-[200px]"
          placeholder="菜单权限名称"
        >
          <template #prefix>
            <i-ep-search />
          </template>
        </el-input>

        <div class="flex-center">
          <el-button type="primary" size="small" plain @click="togglePermTree"
            ><i-ep-switch />{{ isExpanded ? "收缩" : "展开" }}</el-button
          >
          <el-checkbox
            v-model="parentChildLinked"
            @change="handleparentChildLinkedChange"
            class="ml-5"
            >父子联动
          </el-checkbox>

          <el-tooltip placement="bottom">
            <template #content>
              如果只需勾选菜单权限，不需要勾选子菜单或者按钮权限，请关闭父子联动
            </template>
            <i-ep-QuestionFilled
              class="ml-1 color-[--el-color-primary] inline-block cursor-pointer"
            />
          </el-tooltip>
        </div>
      </div>

      <el-tree
        ref="permTreeRef"
        node-key="id"
        show-checkbox
        :data="menuPermOptions"
        :filter-node-method="handlePermFilter"
        :default-expand-all="true"
        :check-strictly="!parentChildLinked"
        class="mt-5"
      >
        <template #default="{ data }">
          {{ data.menuName }}
        </template>
      </el-tree>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleAssignPermSubmit"
            >确 定</el-button
          >
          <el-button @click="assignPermDialogVisible = false">取 消</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Role",
  inheritAttrs: false,
});

import RoleAPI, { RoleVO, RoleForm } from "@/api/role";
import OrganizationAPI, { OrganizationPageVO } from "@/api/organization";
import MenuAPI from "@/api/menu";

const roleFormRef = ref(ElForm);
const permTreeRef = ref<InstanceType<typeof ElTree>>();

const loading = ref(false);

// 角色表格数据
const roleList = ref<RoleVO[]>();
// 菜单权限下拉
const menuPermOptions = ref<Menu[]>([]);
const menuTreeProps = reactive({
  label: 'menuName'
})

const showOrganization = sessionStorage.getItem('organizationId') == 0;
const organizationList = ref<OrganizationPageVO[]>();

// 弹窗
const dialog = reactive({
  title: "",
  visible: false,
});
// 角色表单
const formData = reactive<RoleForm>({
  roleId: undefined,
  roleName: '',
  description: '',
  organizationId: 0,
});

const rules = reactive({
  roleName: [{ required: true, message: "请输入角色名称", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  description: [{ max: 25, message: '输入长度在 0 到 50 个字符', trigger: 'blur' }],
});

// 选中的角色
interface CheckedRole {
  roleId?: number;
  roleName?: string;
}
const checkedRole = ref<CheckedRole>({});
const assignPermDialogVisible = ref(false);

const permKeywords = ref("");
const isExpanded = ref(true);

const parentChildLinked = ref(true);

/** 查询 */
function handleQuery() {
  loading.value = true;
  RoleAPI.getList()
    .then(({data}) => {
      roleList.value = data;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 打开角色弹窗 */
function handleOpenDialog(role) {
  // 获取公司集合
  OrganizationAPI.getList()
    .then(({data}) => {
      organizationList.value = data;
  });
  dialog.visible = true;
  if (role && role.roleId) {
    dialog.title = "修改角色";
    Object.assign(formData, role);
  } else {
    dialog.title = "新增角色";
  }
}

/** 提交角色表单 */
function handleSubmit() {
  roleFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const roleId = formData.roleId;
      if (roleId) {
        RoleAPI.update(formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        RoleAPI.add(formData)
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

/** 关闭角色弹窗 */
function handleCloseDialog() {
  dialog.visible = false;

  roleFormRef.value.resetFields();
  roleFormRef.value.clearValidate();

  formData.roleId = undefined;
  formData.roleName = '';
  formData.description = '';
  formData.organizationId = 0;
}

/** 删除角色 */
function handleDelete(roleId?: number) {
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      RoleAPI.deleteById(roleId)
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

/** 打开分配菜单权限弹窗 */
async function handleOpenAssignPermDialog(row: RoleVO) {
  const roleId = row.roleId;
  if (roleId) {
    assignPermDialogVisible.value = true;
    loading.value = true;

    checkedRole.value.roleId = roleId;
    checkedRole.value.roleName = row.roleName;

    // 获取所有的菜单
    let { data } = await MenuAPI.getMenuTree();
    menuPermOptions.value = data;

    // 回显角色已拥有的菜单
    RoleAPI.getRoleMenuIds(roleId)
      .then(({data}) => {
        const checkedMenuIds = data;
        checkedMenuIds.forEach((menuId) =>
          permTreeRef.value!.setChecked(menuId, true, false)
        );
      })
      .finally(() => {
        loading.value = false;
      });
  }
}

/** 分配菜单权限提交 */
function handleAssignPermSubmit() {
  const roleId = checkedRole.value.roleId;
  if (roleId) {
    const checkedMenuIds: number[] = permTreeRef
      .value!.getCheckedNodes(false, true)
      .map((node: any) => node.menuId);

    loading.value = true;
    RoleAPI.updateRoleMenus(roleId, checkedMenuIds.join())
      .then(() => {
        ElMessage.success("分配权限成功");
        assignPermDialogVisible.value = false;
        handleQuery();
      })
      .finally(() => {
        loading.value = false;
      });
  }
}

/** 展开/收缩 菜单权限树  */
function togglePermTree() {
  isExpanded.value = !isExpanded.value;
  if (permTreeRef.value) {
    Object.values(permTreeRef.value.store.nodesMap).forEach((node: any) => {
      if (isExpanded.value) {
        node.expand();
      } else {
        node.collapse();
      }
    });
  }
}

/** 权限筛选  */
watch(permKeywords, (val) => {
  permTreeRef.value!.filter(val);
});

function handlePermFilter(
  value: string,
  data: {
    [key: string]: any;
  }
) {
  if (!value) return true;
  return data.menuName.includes(value);
}

/** 父子菜单节点是否联动 change*/
function handleparentChildLinkedChange(val: any) {
  parentChildLinked.value = val;
}

onMounted(() => {
  handleQuery();
});
</script>
