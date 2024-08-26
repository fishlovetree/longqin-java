<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="rangeTime" label="操作时间">
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
        <el-table-column label="操作时间" prop="createTime" width="180" />
        <el-table-column label="操作人" prop="creatorName" width="120" />
        <el-table-column label="标题" prop="title" width="100" />
        <el-table-column label="日志内容" prop="remark" min-width="200" />
        <el-table-column label="IP 地址" prop="ip" width="150" />
        <el-table-column label="操作类型" prop="operateType" width="150">
          <template #default="scope">
            <el-tag v-if="scope.row.operateType === 0" type="success"
              >增</el-tag
            >
            <el-tag v-if="scope.row.operateType === 1" type="success"
              >删</el-tag
            >
            <el-tag v-if="scope.row.operateType === 2" type="success"
              >改</el-tag
            >
            <el-tag v-if="scope.row.operateType === 3" type="success"
              >启用</el-tag
            >
            <el-tag v-if="scope.row.operateType === 4" type="success"
              >停用</el-tag
            >
            <el-tag v-if="scope.row.operateType === 5" type="success"
              >请求</el-tag
            >
            <el-tag v-if="scope.row.operateType === 6" type="success"
              >响应</el-tag
            >
            <el-tag v-if="scope.row.operateType === 7" type="success"
              >设置</el-tag
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
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Log",
  inheritAttrs: false,
});

import LogAPI, { LogPageVO, LogPageQuery } from "@/api/log";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<LogPageQuery>({
  page: 1,
  size: 10,
  beginDate: "",
  endDate: "",
});
const rangeTime = ref("");

function handleChange(){
  queryParams.beginDate = rangeTime.value[0];
  queryParams.endDate = rangeTime.value[1];
}

// 日志表格数据
const pageData = ref<LogPageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  LogAPI.getPage(queryParams)
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

onMounted(() => {
  handleQuery();
});
</script>
