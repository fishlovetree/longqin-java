<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="flowName" label="关键字">
          <el-input
            v-model="queryParams.flowName"
            placeholder="流程名称"
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
      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="flowList"
        highlight-current-row
        border
      >
        <el-table-column label="流程名称" prop="flowName" min-width="200" />
        <el-table-column label="流程类别" prop="flowSort" width="150">
          <template #default="scope">
            <el-tag v-if="scope.row.flowSort === 1" type="success"
              >考勤类</el-tag
            >
            <el-tag v-if="scope.row.flowSort === 2" type="success"
              >行政类</el-tag
            >
            <el-tag v-if="scope.row.flowSort === 3" type="success"
              >业务类</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="200" />

        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleStart(scope.row)"
            >
              <i-ep-edit />启动
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
  name: "FlowList",
  inheritAttrs: false,
});

import router from "@/router";
import DesflowAPI, { FlowPageVO, FlowPageQuery } from "@/api/desflow";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<FlowPageQuery>({
  page: 1,
  size: 10,
});

// 流程表格数据
const flowList = ref<FlowPageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  DesflowAPI.getPage(queryParams)
    .then(({data}) => {
      flowList.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 重置查询 */
function handleResetQuery() {
  queryFormRef.value.resetFields();
  queryFormRef.flowName = '';
  queryParams.page = 1;
  handleQuery();
}

/** 启动流程 */
function handleStart(flow) {
  router.push({ path: '/startflow/start', query: { flowId: flow.flowId, flowName: flow.flowName }});
}

onMounted(() => {
  handleQuery();
});
</script>
