<template>
  <div class="app-container">
    <div class="search-container" v-if="isSearch">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item
          v-for="(item, index) in searchList"
          :key="index"
          :label="item.description"
        >
          <el-input
            v-model="queryParams[item.columnName]" placeholder="请输入"
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
        ref="tableDataRef"
        :data="tableDataList"
        style="width: 100%"
      >
        <el-table-column
            v-for="(item, index) in tableColumnList"
            :key="index"
            :label="item.description"
            :prop="item.columnName"
            :show-overflow-tooltip="true"
            :min-width="item.width"
        >
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
    name: "Diytable",
    inheritAttrs: false,
  });

  import router from "@/router";
  import DiytableAPI from "@/api/diytable";
  import { changeTableData } from "@/utils/index";
  import DesformAPI from "@/api/desform";

  const route = useRoute();

  const queryFormRef = ref(ElForm);

  const loading = ref(false);
  const total = ref(0);

  const queryParams = reactive({
    page: 1,
    size: 10,
    id: route.params.id,
    dataSource: null,
  });
  const tableColumnList = ref([]);
  const tableDataRef = ref(null);
  const tableDataList = ref([]);
  const widgetList = ref([]); // 原始表单组件
  /** 查询 */
  function handleQuery() {
    loading.value = true;
    DiytableAPI.GetTableData(queryParams)
      .then(({data}) => {
        tableDataList.value = data.list;
        total.value = data.total;

        // select、radio等数据加工
        DesformAPI.getByTableName(queryParams.dataSource)
        .then(({data}) => {
          if (data && data.jsonData){
            let json = JSON.parse(data.jsonData);
            widgetList.value = json.widgetList;
            changeTableData(tableDataList.value, widgetList.value);
          }
        })
      })
      .finally(() => {
        loading.value = false;
      });
  }
  /** 重置查询 */
  function handleResetQuery() {
    queryFormRef.value.resetFields();
    queryParams.page = 1;
    for (let key in queryParams){
      if (key != "page" && key != "size" && key != "id" && key != "dataSource"){
        queryParams[key] = '';
      }
    }
    handleQuery();
  }

  const searchList = ref([]);
  const isSearch = ref(false);
  watch(() => tableColumnList.value, (newItems, oldItems) => {
    searchList.value = [];
    newItems.forEach((item, index) => {
      if (item.searchType && item.searchType != 0) {
        searchList.value.push(item);
        queryParams[item.columnName] = '';
        isSearch.value = true;
      }
    });
  }, {
    deep: true, // 开启深度监听
  });

  onMounted(() => {
    DiytableAPI.getTableColumns(route.params.id).then(({data}) => {
      if (data){
        tableColumnList.value = data;
        DiytableAPI.getById(route.params.id).then(({data}) => {
          if (data){
            queryParams.dataSource = data.dataSource;
            handleQuery();
          }
        })
      }
    })
  });
</script>
