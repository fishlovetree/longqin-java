<!-- 用户管理 -->
<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 部门树 -->
      <el-col :lg="4" :xs="24" class="mb-[12px]">
        <dept-tree v-model="queryParams.departmentId" @node-click="handleQuery" />
      </el-col>

      <!-- 用户列表 -->
      <el-col :lg="20" :xs="24">
        <div class="search-container">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="关键字" prop="nickName">
              <el-input
                v-model="queryParams.nickName"
                placeholder="昵称"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleQuery"
                ><i-ep-search />搜索</el-button
              >
              <el-button @click="handleResetQuery">
                <i-ep-refresh />
                重置</el-button
              >
            </el-form-item>
          </el-form>
        </div>

        <el-card shadow="never" class="table-container">

          <el-table
            v-loading="loading"
            :data="pageData"
          >
            <el-table-column
              label="昵称"
              min-width="180"
              align="center"
              prop="nickName"
            />

            <el-table-column
              label="部门"
              width="180"
              align="center"
              prop="departmentName"
            />
            <el-table-column
              label="职位"
              width="180"
              align="center"
              prop="positionName"
            />
            <el-table-column
              label="邮箱"
              align="center"
              prop="email"
              width="200"
            />
            <el-table-column
              label="手机号码"
              align="center"
              prop="phone"
              width="160"
            />
          </el-table>

          <pagination
            v-if="total > 0"
            v-model:total="total"
            v-model:page="queryParams.page"
            v-model:limit="queryParams.size"
            @pagination="handleQuery"
          />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "AddressList",
  inheritAttrs: false,
});

import UserAPI, { UserPageQuery, UserPageVO } from "@/api/user";

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);
const pageData = ref<UserPageVO[]>();

/** 用户查询参数  */
const queryParams = reactive<UserPageQuery>({
  page: 1,
  size: 10,
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  UserAPI.getPage(queryParams)
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
  queryParams.departmentId = undefined;
  queryParams.nickName = undefined;
  handleQuery();
}

onMounted(() => {
  handleQuery();
});
</script>
  
<style>

</style>