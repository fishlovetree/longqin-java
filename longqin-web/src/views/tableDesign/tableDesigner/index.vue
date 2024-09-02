<template>
  <el-form
    ref="tableFormRef"
    :model="formData"
    :rules="rules"
    label-width="100px"
  >
    <el-row>
      <el-col :span="6">
        <el-card style="min-height: calc(100vh - 420px);margin:10px;">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          <el-form-item label="列表名称" prop="tableName">
            <el-input v-model="formData.tableName" placeholder="请输入列表名称" />
          </el-form-item>
          <el-form-item label="表头数据源" prop="dataSource">
            <el-select v-model="formData.dataSource" placeholder="请选择数据源" @change="handleDataSource">
              <el-option
              v-for="item in dataSourceList"
              :key="item.tableName"
              :value="item.tableName"
              :label="item.formName"/>
            </el-select>
          </el-form-item>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="min-height: calc(100vh - 420px);margin:10px 10px 10px 0px;">
          <template #header>
            <div class="card-header">
              <span>表头选择</span>
            </div>
          </template>

          <el-checkbox-group v-model="formData.headers" @change="handleCheckbox">
            <el-checkbox v-for="item in tableColumnList" :label="item.description" :value="item.columnName" />
          </el-checkbox-group>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="min-height: calc(100vh - 420px);margin:10px 10px 10px 0px;">
          <template #header>
            <div class="card-header">
              <span>表头属性(鼠标拖动调整位置)</span>
            </div>
          </template>
          <el-table
            ref="attributeTableRef"
            :data="selectedTableColumnList"
            style="width: 100%"
          >
            <el-table-column label="列名" prop="description" min-width="100" />
            <el-table-column label="列宽" prop="width" width="160">
              <template #default="scope">
                <el-input-number v-model="scope.row.width" :min="100" :max="500" />
              </template>
            </el-table-column>
            <el-table-column label="排序方式" prop="orderby" width="100">
              <template #default="scope">
                <el-select v-model="scope.row.orderby" placeholder="请选择排序方式">
                  <el-option label="请选择" value="0" />
                  <el-option label="升序" value="1" />
                  <el-option label="降序" value="2" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="搜索条件" prop="searchType" width="120">
              <template #default="scope">
                <el-select v-model="scope.row.searchType" placeholder="请选择搜索条件">
                  <el-option label="请选择" value="0" />
                  <el-option label="等于" value="1" />
                  <el-option label="模糊查询" value="2" />
                  <el-option label="介于" value="3" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="计算公式" prop="formula" width="100">
              <template #default="scope">
                <el-select v-model="scope.row.formula" placeholder="请选择计算公式">
                  <el-option label="请选择" value="0" />
                  <el-option label="加" value="1" />
                  <el-option label="减" value="2" />
                  <el-option label="乘" value="3" />
                  <el-option label="除" value="4" />
                  <el-option label="拼接" value="5" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="公式值" prop="formulaValue" width="120">
              <template #default="scope">
                <el-input v-model="scope.row.formulaValue" maxlength="25" />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-card style="min-height: 300px;margin: 0px 10px 10px 10px;">
          <template #header>
            <div class="card-header">
              <span>预览窗口</span>
            </div>
          </template>
          <div class="search-container">
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
                <el-button @click="handleQuery"
                  ><i-ep-search />搜索</el-button
                >
                <el-button @click="handleResetQuery"><i-ep-refresh />重置</el-button>
                <el-button type="primary" @click="handleSave"><i-ep-select />保存列表</el-button>
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
                  v-for="(item, index) in selectedTableColumnList"
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
        </el-card>
      </el-col>
    </el-row>
  </el-form>
</template>

<script setup lang="ts">
  import router from "@/router";
  import DesformAPI, { FormPageVO, FormPageQuery, DesFormColumn } from "@/api/desform";
  import DiytableAPI, { DiyTablePageVO, DiytableData } from "@/api/diytable";
  import Sortable from 'sortablejs';
  import { changeTableData } from "@/utils/index";

  const route = useRoute();

  const tableFormRef = ref(ElForm);

  const loading = ref(false);

  const formData = reactive<DiytableData>({
    id: '',
    tableName: '',
    dataSource: '',
    data: '',
    headers: [],
  })
  // 表单验证规则
  const rules = reactive({
    tableName: [{ required: true, message: "请输入表单名称", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
    dataSource: [{ required: true, message: "请选择数据源", trigger: "blur" }],
  });

  const dataSourceQueryParams = reactive<FormPageQuery>({
    page: 1,
    size: 500,
  });
  const dataSourceList = ref([]);
  const tableColumnList = ref<DesFormColumn[]>();
  /** 查询数据源 */
  function handleQueryDataSource() {
    DesformAPI.getPage(dataSourceQueryParams)
      .then(({data}) => {
        dataSourceList.value = data.list;
      })
  }

  const selectedDataSource = ref(null);
  const widgetList = ref([]); // 原始表单组件
  function handleDataSource(value){
    DesformAPI.getTableColumns(value)
      .then(({data}) => {
        tableColumnList.value = data;
        // 不是修改状态或者切换数据源，清空
        if (selectedDataSource.value != value){
          formData.headers = [];
          selectedTableColumnList.value = [];
        }
      })
      DesformAPI.getByTableName(value)
      .then(({data}) => {
        if (data && data.jsonData){
          let json = JSON.parse(data.jsonData);
          widgetList.value = json.widgetList;
        }
      })
    // 预览数据源
    queryParams.dataSource = value;
  }

  const attributeTableRef = ref(null);
  const selectedTableColumnList = ref([]); // 选中列
  function handleCheckbox(){
    selectedTableColumnList.value = [];
    for (let i = 0 ; i < tableColumnList.value.length; i++){
      tableColumnList.value[i].width = 200; // 默认200列宽
      if (formData.headers.includes(tableColumnList.value[i].columnName)){
        selectedTableColumnList.value.push(tableColumnList.value[i]);
      }
    }
  }

  // 预览相关
  const queryFormRef = ref(ElForm);
  const total = ref(0);
  const queryParams = reactive({
    page: 1,
    size: 10,
    dataSource: null,
    columns: null,
  });
  const tableDataRef = ref(null);
  const tableDataList = ref([]);
  /** 查询 */
  function handleQuery() {
    loading.value = true;
    if (queryParams.dataSource == null){
      ElMessage.warnning("请选择数据源");
    }
    queryParams.columns = JSON.stringify(selectedTableColumnList.value);
    DiytableAPI.GetTableData(queryParams)
      .then(({data}) => {
        tableDataList.value = data.list;
        total.value = data.total;

        // select、radio等数据加工
        changeTableData(tableDataList.value, widgetList.value);
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
  watch(() => selectedTableColumnList.value, (newItems, oldItems) => {
    searchList.value = [];
    newItems.forEach((item, index) => {
      if (item.searchType && item.searchType != 0) {
        searchList.value.push(item);
        queryParams[item.columnName] = '';
      }
    });
    // 获取预览数据
    handleQuery();
  }, {
    deep: true, // 开启深度监听
  });

  watch(() => formData.dataSource, (newVal, oldVal) => {
    handleDataSource(newVal);
  }, {
    deep: true, // 开启深度监听
  });

  function handleSave(){
    tableFormRef.value.validate((isValid: boolean) => {
      if (isValid) {
        if (formData.headers.length == 0 ) {
          ElMessage.warnning("请至少选择一个表头");
          return;
        }
        formData.data = JSON.stringify(selectedTableColumnList.value);
        if (formData.id) {
          DiytableAPI.update(formData).then(() => {
            ElMessage.success("修改成功");
          });
        } else {
          DiytableAPI.create(formData).then(() => {
            ElMessage.success("新增成功");
          });
        }
      }
    });
  }

  onMounted(() => {
    handleQueryDataSource();

    let el = attributeTableRef.value.$el.querySelector('.el-table__body tbody');
    let sortable = new Sortable(el, {
        animation: 150,
        ghostClass: 'blue-background-class',
        onEnd: function (evt) {
            const arr = selectedTableColumnList.value;
            const currentRow = arr.splice(evt.oldIndex, 1)[0];
            arr.splice(evt.newIndex, 0, currentRow);
            selectedTableColumnList.value = [];
            nextTick(() =>{
              selectedTableColumnList.value = arr;
            })
        },
    });

    if (route.query.id){
      formData.id = route.query.id;
      DiytableAPI.getById(route.query.id).then(({data}) => {
        if (data){
          formData.tableName = data.tableName;
          formData.dataSource = data.dataSource;
          selectedDataSource.value = data.dataSource;
          selectedTableColumnList.value = data.columns;
          for(let i = 0; i < data.columns.length; i++){
            let column = data.columns[i];
            formData.headers.push(column.columnName);
          }
        }
      })
    }
  });
</script>

<style type="text/css">

</style>