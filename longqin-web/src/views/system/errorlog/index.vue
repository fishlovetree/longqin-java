<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="rangeTime" label="异常时间">
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
        <el-table-column label="异常时间" prop="createTime" width="180" />
        <el-table-column label="操作人" prop="nickName" width="120" />
        <el-table-column label="异常信息" prop="message" width="200" showOverflowTooltip/>
        <el-table-column label="异常类" prop="errorClass" width="150" showOverflowTooltip/>
        <el-table-column label="异常堆栈" prop="stacktrace" min-width="200" showOverflowTooltip/>
        <el-table-column label="IP 地址" prop="ip" width="150" />
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
  name: "ErrorLog",
  inheritAttrs: false,
});

import ErrorLogAPI, { ErrorLogPageVO, ErrorLogPageQuery } from "@/api/errorlog";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<ErrorLogPageQuery>({
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
const pageData = ref<ErrorLogPageVO[]>();

/** 查询 */
function handleQuery() {
  loading.value = true;
  ErrorLogAPI.getPage(queryParams)
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
