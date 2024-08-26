<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="keywords" label="关键字">
          <el-input
            v-model="queryParams.keywords"
            placeholder="表单名称"
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
        <el-button type="success" @click="handleAdd()"
          ><i-ep-plus />新增</el-button
        >
      </template>

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="formList"
        highlight-current-row
        border
      >
        <el-table-column label="表单名称" prop="formName" width="150" />
        <el-table-column label="数据对象名称" prop="tableName" width="150" />
        <el-table-column label="表单json" prop="jsonData" min-width="250" show-overflow-tooltip />

        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleOpenDialog(scope.row.jsonData)"
            >
              <i-ep-view />预览
            </el-button>
            <el-button
              type="primary"
              size="small"
              link
              @click="handleEdit(scope.row.id)"
            >
              <i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              link
              @click="handleDelete(scope.row.id)"
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

    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="handleCloseDialog"
    >
      <v-form-render :form-json="formJson" :form-data="formData" :option-data="optionData" ref="vFormRef">
      </v-form-render>
      <el-button type="primary" @click="submitForm">提 交</el-button>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "FormList",
  inheritAttrs: false,
});

import router from "@/router";
import DesformAPI, { FormPageVO, FormPageQuery } from "@/api/desform";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<FormPageQuery>({
  page: 1,
  size: 10,
});

// 表单表格数据
const formList = ref<FormPageVO[]>();

// 弹窗
const dialog = reactive({
  title: "",
  visible: false,
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  DesformAPI.getPage(queryParams)
    .then(({data}) => {
      formList.value = data.list;
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

/** 新增 */
function handleAdd() {
  router.push('/formDesigner/view');
}

/** 编辑 */
function handleEdit(id: number) {
  router.push({ path: '/formDesigner/view', query: { id: id }});
}

/** 打开弹窗 */
function handleOpenDialog(jsonData?: string) {
  dialog.visible = true;
  setTimeout(()=>{
    vFormRef.value.setFormJson(jsonData);
  }, 100)
}

/** 关闭弹窗 */
function handleCloseDialog() {
  dialog.visible = false;
}

/** 删除 */
function handleDelete(formId?: number) {

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      DesformAPI.deleteById(formId)
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

const formJson = reactive({})
const formData = reactive({})
const optionData = reactive({})
const vFormRef = ref(null)

const submitForm = () => {
  vFormRef.value.getFormData().then(formData => {
    // Form Validation OK
    alert( JSON.stringify(formData) )
  }).catch(error => {
    // Form Validation failed
    ElMessage.error(error)
  })
}

onMounted(() => {
  handleQuery();
});
</script>
