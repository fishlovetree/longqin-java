<template>
  <lay-container fluid="false" class="log-box">
    <lay-card>
      <lay-form style="margin-top: 10px">
        <lay-row>
          <lay-col :md="9">
            <lay-form-item label="时间范围" label-width="80">
              <lay-date-picker
                size="sm"
                v-model="searchQuery.rangeTime"
                range
                type="datetime"
                :placeholder="['开始日期', '结束日期']"
              ></lay-date-picker
            ></lay-form-item>
          </lay-col>
          <lay-col :md="3">
            <lay-form-item label-width="20">
              <lay-button
                style="margin-left: 20px"
                type="normal"
                size="sm"
                @click="toSearch"
              >
                查询
              </lay-button>
              <lay-button size="sm" @click="toReset"> 重置 </lay-button>
            </lay-form-item>
          </lay-col>
        </lay-row>
      </lay-form>
    </lay-card>
    <!-- table -->
    <div class="table-box">
      <lay-table
        :page="page"
        :columns="columns"
        :loading="loading"
        :default-toolbar="true"
        :data-source="dataSource"
        :height="'100%'"
        @change="change"
      >
      </lay-table>
    </div>
  </lay-container>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue'
import { layer } from '@layui/layui-vue'
import { logPage } from "@/api/module/errorlog";
const searchQuery = ref({
  rangeTime: []
})

function toReset() {
  searchQuery.value = {
    rangeTime: []
  }
}

function toSearch() {
  page.current = 1
  change(page)
}

const loading = ref(false)
const page = reactive({ current: 1, limit: 10, total: 100 })
const columns = ref([
  { title: '异常信息', key: 'message', ellipsisTooltip: 'true' },
  { title: '异常堆栈', key: 'stacktrace', ellipsisTooltip: 'true' },
  { title: 'ip地址', width:'120', key: 'ip' },
  { title: '操作人', width:'120', key: 'nickName' },
  { title: '异常时间', width:'150', key: 'createTime', sort: 'desc' },
])
const change = (page: any) => {
  loading.value = true
  setTimeout(() => {
    handleQuery(page.current, page.limit);
    loading.value = false
  }, 100)
}
const dataSource = ref()

function handleQuery(page1: number, size: number) {
	// 重置父组件
	loading.value = true;
  let rangeTime = searchQuery.value.rangeTime;
	logPage({page: page1, size: size, beginDate: rangeTime[0], endDate: rangeTime[1]})
		.then((res: any) => {
			dataSource.value = res.data.list;
      page.total = res.data.total;
		})
		.then(() => {
			loading.value = false;
		});
}
onMounted(() => {
	handleQuery(page.current, page.limit);
});
</script>

<style scoped>
.log-box {
  width: calc(100vw - 220px);
  height: calc(100vh - 110px);
  margin-top: 10px;
  box-sizing: border-box;
  overflow: hidden;
}
.top-search {
  margin-top: 10px;
  padding: 10px;
  height: 40px;
  border-radius: 4px;
  background-color: #fff;
}
.table-box {
  padding: 10px;
  height: calc(100vh - 200px);
  width: 100%;
  border-radius: 4px;
  box-sizing: border-box;
  background-color: #fff;
}

.search-input {
  display: inline-block;
  width: 98%;
  margin-right: 10px;
}
.table-style {
  margin-top: 10px;
  width: 100%;
  height: 100%;
}
.isChecked {
  display: inline-block;
  background-color: #e8f1ff;
  color: red;
}
.oneRow {
  width: 180px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
}
.title {
  height: 40px;
  line-height: 40px;
  padding: 0 10px;
  display: inline-block;
  background: #f7f7f7;
  border-top: 1px solid #e8e8e8;
  border-left: 1px solid #e8e8e8;
}
.content {
  height: 40px;
  line-height: 40px;
  padding: 0 3px 0 10px;
  border-top: 1px solid #e8e8e8;
  border-left: 1px solid #e8e8e8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
}
.borderR {
  border-right: 1px solid #e8e8e8;
}
.borderB {
  border-bottom: 1px solid #e8e8e8;
}
.layui-form-item{
  margin-bottom: 0px;
}
</style>