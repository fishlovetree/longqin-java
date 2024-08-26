<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="rangeTime" label="日期范围">
          <el-date-picker
            v-model="rangeTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleChange"
            value-format="YYYY-MM-DD"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><i-ep-search />搜索</el-button
          >
          <el-button @click="handleResetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="pageData"
        highlight-current-row
        border
      >
        <el-table-column label="流程名称" prop="flowName" width="150" />
        <el-table-column label="任务" prop="nodeName" width="150" />
        <el-table-column label="提交时间" prop="submitTime" width="180" />
        <el-table-column label="发起人" prop="creatorName" min-width="120" />
        <el-table-column label="发起时间" prop="createTime" width="180" />
        <el-table-column label="发起部门" prop="departmentName" width="150" />

        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleWork(scope.row)"
            >
              <i-ep-edit />处理
            </el-button>

            <el-button
              type="danger"
              size="small"
              link
              v-show="scope.row.creator === currentUserId"
              @click="handleDisable(scope.row)"
            >
              <i-ep-edit />作废
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
  name: "Backlog",
  inheritAttrs: false,
});

import router from "@/router";
import WorkAPI, { WorkPageVO, WorkPageQuery } from "@/api/work";

const currentUserId = parseInt(sessionStorage.getItem('userId'));

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<WorkPageQuery>({
  page: 1,
  size: 10,
  beginDate: "",
  endDate: "",
  status: 1,
});
const rangeTime = ref("");

function handleChange(){
  queryParams.beginDate = rangeTime.value[0];
  queryParams.endDate = rangeTime.value[1];
}

// 待办表格数据
const pageData = ref<WorkPageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  WorkAPI.getProcessPage(queryParams)
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
  rangeTime.value = "";
  queryParams.beginDate = "";
  queryParams.endDate = "";
  handleQuery();
}

/** 待办处理 */
function handleWork(process) {
  router.push({ path: '/backlog/deal', query: { workId: process.workId, processId: process.processId, 
    flowId: process.flowId, nodeName: process.nodeName }});
}

/** 流程作废 */
function handleDisable(process) {
  WorkAPI.disableWork(process.workId)
    .then(({data}) => {
      ElMessage.success("作废成功");
      handleQuery();
    })
}

onMounted(() => {
  handleQuery();
});
</script>
