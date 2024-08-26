<template>
  <div class="app-container">
    <el-card shadow="never" class="table-container">
      <template #header>
        <el-button
          type="success"
          @click="handleOpenDialog(-1)"
        >
          <template #icon><i-ep-plus /></template>
          新增</el-button
        >
      </template>

      <el-table
        v-loading="loading"
        :data="menuTableData"
        highlight-current-row
        row-key="menuId"
        :tree-props="{
          children: 'children',
          hasChildren: 'hasChildren',
        }"
      >
        <el-table-column label="菜单名称" min-width="200">
          <template #default="scope">
            <template
              v-if="scope.row.menuIcon && scope.row.menuIcon.startsWith('el-icon')"
            >
              <el-icon style="vertical-align: -0.15em">
                <component :is="scope.row.menuIcon.replace('el-icon-', '')" />
              </el-icon>
            </template>
            <template v-else-if="scope.row.menuIcon">
              <svg-icon :icon-class="scope.row.menuIcon" />
            </template>
            <template v-else>
              <svg-icon icon-class="menu" />
            </template>
            {{ scope.row.menuName }}
          </template>
        </el-table-column>

        <el-table-column
          label="菜单路径"
          align="left"
          width="200"
          prop="menuUrl"
        />

        <el-table-column label="排序" align="center" width="80" prop="groupSeq" />

        <el-table-column fixed="right" align="center" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              link
              size="small"
              @click.stop="handleOpenDialog(scope.row.menuId)"
            >
              <i-ep-plus />新增
            </el-button>

            <el-button
              type="primary"
              link
              size="small"
              @click.stop="handleOpenDialog(undefined, scope.row)"
            >
              <i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              link
              size="small"
              @click.stop="handleDelete(scope.row.menuId)"
              ><i-ep-delete />
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-drawer
      v-model="dialog.visible"
      :title="dialog.title"
      @close="handleCloseDialog"
      size="50%"
    >
      <el-form
        ref="menuFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="父级菜单" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            placeholder="选择上级菜单"
            :data="menuTableData"
            filterable
            check-strictly
            :render-after-expand="false"
            node-key="id"
            :props="menuTreeProps"
          />
        </el-form-item>

        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="formData.menuName" placeholder="请输入菜单名称" />
        </el-form-item>

        <el-form-item label="菜单路径" prop="menuUrl">
          <el-input v-model="formData.menuUrl" placeholder="请输入菜单路径" />
        </el-form-item>

        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="formData.groupSeq"
            style="width: 100px"
            controls-position="right"
            :min="0"
          />
        </el-form-item>

        <el-form-item
          label="图标"
          prop="icon"
        >
          <!-- 图标选择器 -->
          <icon-select v-model="formData.menuIcon" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Menu",
  inheritAttrs: false,
});

import MenuAPI, { Menu } from "@/api/menu";

const menuFormRef = ref(ElForm);

const loading = ref(false);
const dialog = reactive({
  title: "新增菜单",
  visible: false,
});

// 菜单表格数据
const menuTableData = ref<Menu[]>([]);

const menuTreeProps = reactive({
  label: 'menuName'
})

// 初始菜单表单数据
const initialMenuFormData = ref<Menu>({
  menuId: undefined,
  parentId: -1,
  menuName: '',
  groupSeq: 1,
  menuIcon: '',
  menuUrl: '',
});

// 菜单表单数据
const formData = ref({ ...initialMenuFormData.value });

// 表单验证规则
const rules = reactive({
  menuName: [{ required: true, message: "请输入菜单名称", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  menuUrl: [{ required: true, message: "请选择菜单路径", trigger: "blur" },{ min: 1, max: 100, message: '输入长度在 1 到 100 个字符', trigger: 'blur' }],
});

// 查询
function handleQuery() {
  loading.value = true;
  MenuAPI.getMenuTree()
    .then(({data}) => {
      menuTableData.value = data;
    })
    .finally(() => {
      loading.value = false;
    });
}

/**
 * 打开表单弹窗
 *
 * @param parentId 父菜单ID
 * @param menuId 菜单ID
 */
function handleOpenDialog(parentId?: number, menu?: Menu) {
  dialog.visible = true;
  if (menu && menu.menuId) {
    dialog.title = "编辑菜单";
    initialMenuFormData.value = { ...menu };
    formData.value = menu;
  } else {
    dialog.title = "新增菜单";
    formData.value.parentId = parentId ?? -1;
  }
}

/** 菜单保存提交 */
function submitForm() {
  menuFormRef.value.validate((isValid: boolean) => {
    if (isValid) {
      const menuId = formData.value.menuId;
      if (menuId) {
        MenuAPI.update(formData.value).then(() => {
          ElMessage.success("修改成功");
          handleCloseDialog();
          handleQuery();
        });
      } else {
        MenuAPI.add(formData.value).then(() => {
          ElMessage.success("新增成功");
          handleCloseDialog();
          handleQuery();
        });
      }
    }
  });
}

// 删除菜单
function handleDelete(menuId: number) {
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      MenuAPI.deleteById(menuId)
        .then(() => {
          ElMessage.success("删除成功");
          handleQuery();
        })
        .finally(() => {
          loading.value = false;
        });
    },
    () => {
      ElMessage.info("已取消删除");
    }
  );
}

// 关闭弹窗
function handleCloseDialog() {
  dialog.visible = false;
  menuFormRef.value.resetFields();
  menuFormRef.value.clearValidate();
  formData.value.menuId = undefined;
  formData.value.menuName = '';
  formData.value.menuUrl = '';
  formData.value.menuIcon = '';
  formData.value.groupSeq = 1;
  formData.value.parentId = -1;
}

onMounted(() => {
  handleQuery();
});
</script>
