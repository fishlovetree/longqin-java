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
        :data="positionList"
        row-key="positionId"
        default-expand-all
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="positionName" label="职位名称" min-width="200" />
        <el-table-column label="职位等级" align="center" width="200">
          <template #default="scope">
            <el-tag v-if="scope.row.positionLevel === 1" type="success"
              >基层</el-tag
            >
            <el-tag v-if="scope.row.positionLevel === 2" type="success"
              >中层</el-tag
            >
            <el-tag v-if="scope.row.positionLevel === 3" type="success"
              >高层</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="description" label="备注" width="200" />

        <el-table-column label="操作" fixed="right" align="left" width="200">
          <template #default="scope">
            <el-button
              type="primary"
              link
              size="small"
              @click.stop="handleOpenDialog(scope.row.positionId, undefined)"
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
              @click.stop="handleDelete(scope.row.positionId)"
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
        ref="positionFormRef"
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
        <el-form-item label="上级职位" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            placeholder="选择上级职位"
            :data="positionList"
            filterable
            check-strictly
            :render-after-expand="false"
            node-key="id"
            :props="positionTreeProps"
          />
        </el-form-item>
        <el-form-item label="职位名称" prop="positionName">
          <el-input v-model="formData.positionName" placeholder="请输入职位名称" />
        </el-form-item>
        <el-form-item label="职位等级" prop="positionLevel">
          <el-select v-model="formData.positionLevel" placeholder="请选择职位等级">
            <el-option label="基层" value="1" />
            <el-option label="中层" value="2" />
            <el-option label="高层" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="description">
          <el-input v-model="formData.description" placeholder="请输入职位备注" />
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
  name: "Position",
  inheritAttrs: false,
});

import PositionAPI, { PositionTree, PositionForm } from "@/api/position";
import OrganizationAPI, { OrganizationPageVO } from "@/api/organization";

const positionFormRef = ref(ElForm);

const loading = ref(false);
const dialog = reactive({
  title: "",
  visible: false,
});

const positionList = ref<PositionTree[]>();
const positionTreeProps = reactive({
  label: 'title'
})

const showOrganization = sessionStorage.getItem('organizationId') == 0;
const organizationList = ref<OrganizationPageVO[]>();

const formData = reactive<PositionForm>({
  positionId: undefined,
  parentId: -1,
  positionName: '',
  positionLevel: undefined,
  description: '',
  organizationId: 0,
});

const rules = reactive({
  positionName: [{ required: true, message: "职位名称不能为空", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  positionLevel: [{ required: true, message: "职位等级不能为空", trigger: "blur" }],
  description: [{ max: 25, message: '输入长度在 0 到 50 个字符', trigger: 'blur' }],
});

/** 查询职位 */
function handleQuery() {
  loading.value = true;
  PositionAPI.getTree().then(({data}) => {
    positionList.value = data;
    loading.value = false;
  });
}

/**
 * 打开职位弹窗
 *
 * @param parentId 父职位ID
 * @param position 职位信息
 */
async function handleOpenDialog(parentId, position) {
  // 获取公司集合
  OrganizationAPI.getList()
    .then(({data}) => {
      organizationList.value = data;
  });
  dialog.visible = true;
  if (position && position.positionId) {
    dialog.title = "修改职位";
    Object.assign(formData, position);
    formData.positionLevel = formData.positionLevel + '';
  } else {
    dialog.title = "新增职位";
    formData.parentId = parentId ?? -1;
  }
}

/** 提交职位表单 */
function handleSubmit() {
  positionFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const positionId = formData.positionId;
      if (positionId) {
        PositionAPI.update(formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        PositionAPI.add(formData)
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

/** 删除职位 */
function handleDelete(positionId?: number) {
  ElMessageBox.confirm(`确认删除已选中的数据项?`, "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      PositionAPI.deleteById(positionId)
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

  positionFormRef.value.resetFields();
  positionFormRef.value.clearValidate();

  formData.positionId = undefined;
  formData.parentId = -1;
  formData.positionName = '';
  formData.description = '';
  formData.positionLevel = undefined;
  formData.creator = null;
  formData.createTime = null;
  formData.organizationId = 0;
}

onMounted(() => {
  handleQuery();
});
</script>