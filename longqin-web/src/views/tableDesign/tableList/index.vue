<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="tableName" label="关键字">
          <el-input
            v-model="queryParams.tableName"
            placeholder="列表名称"
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
        :data="tableList"
        highlight-current-row
        border
      >
        <el-table-column label="列表名称" prop="tableName" min-width="200" />
        <el-table-column label="数据源" prop="formName" width="150" />
        <el-table-column label="创建时间" prop="createTime" width="200" />

        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
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
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "TableList",
  inheritAttrs: false,
});

import router from "@/router";
import DiytableAPI, { DiytablePageQuery, DiyTablePageVO, DiytableData } from "@/api/diytable";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<DiytablePageQuery>({
  page: 1,
  size: 10,
});

// 列表数据
const tableList = ref<DiyTablePageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  DiytableAPI.getPage(queryParams)
    .then(({data}) => {
      tableList.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 重置查询 */
function handleResetQuery() {
  queryFormRef.value.resetFields();
  queryParams.tableName = '';
  queryParams.page = 1;
  handleQuery();
}

/** 新增 */
function handleAdd() {
  router.push('/tableDesigner/view');
}

/** 编辑 */
function handleEdit(id: number) {
  router.push({ path: '/tableDesigner/view', query: { id: id }});
}

/** 删除 */
function handleDelete(id?: number) {

  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      DiytableAPI.deleteById(id)
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
