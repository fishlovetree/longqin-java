<template>
  <lay-container fluid="true" class="notice-box">
    <lay-card>
      <lay-form style="margin-top: 10px">
        <lay-row>
          <lay-col :md="5">
            <lay-form-item label="标题" label-width="80">
              <lay-input
                v-model="searchQuery.title"
                placeholder="请输入"
                size="sm"
                :allow-clear="true"
                style="width: 98%"
              ></lay-input>
            </lay-form-item>
          </lay-col>
          <lay-col :md="9">
            <lay-form-item label="公告时间" label-width="80">
              <lay-date-picker
                size="sm"
                v-model="searchQuery.rangeTime"
                range
                type="datetime"
                :placeholder="['开始日期', '结束日期']"
              ></lay-date-picker
            ></lay-form-item>
          </lay-col>
          <lay-col :md="5">
            <lay-form-item label-width="20">
              <lay-button
                style="margin-left: 20px"
                type="primary"
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
        class="table-style"
        :page="page"
        :columns="columns"
        :loading="loading"
        :default-toolbar="true"
        :data-source="dataSource"
        @change="change"
      >
        <template #attachments="{ row }">
          <div v-for="(comp, index) in row.attachments.split(',')" :key="index">
            <a :href="comp">{{ comp.substring(comp.lastIndexOf('/') + 1) }}</a>
          </div>
        </template>
        <template v-slot:toolbar>
          <lay-button size="sm" type="primary" @click="changeVisible('新增')">
            <lay-icon class="layui-icon-addition"></lay-icon>
            新增</lay-button
          >
        </template>
        <template v-slot:operator="{ row }">
          <lay-button
            size="xs"
            type="primary"
            @click="changeVisible('编辑', row)"
            >编辑</lay-button
          >
          <lay-button
            @click="toRemove(row.noticeId)"
            size="xs"
            border="red"
            border-style="dashed"
          >
            删除
          </lay-button>
        </template>
      </lay-table>
    </div>

    <lay-layer v-model="visible" :title="title" :area="['500px', '500px']">
      <div style="padding: 20px">
        <lay-form :model="model" ref="layFormRef" :rules="rules">
          <lay-form-item label="标题" prop="title" required>
            <lay-input v-model="model.title"></lay-input>
          </lay-form-item>
          <lay-form-item label="内容" prop="content">
            <lay-textarea v-model="model.content"></lay-textarea>
          </lay-form-item>
          <lay-form-item label="紧急程度" prop="noticeLevel" required>
            <lay-select v-model="model.noticeLevel" placeholder="请选择">
              <lay-select-option :value="1" label="普通"></lay-select-option>
              <lay-select-option :value="2" label="紧急"></lay-select-option>
              <lay-select-option :value="3" label="加急"></lay-select-option>
            </lay-select>
          </lay-form-item>
          <lay-form-item label="保密等级" prop="security" required>
            <lay-select v-model="model.security" placeholder="请选择">
              <lay-select-option :value="1" label="公开"></lay-select-option>
              <lay-select-option :value="2" label="内部公开"></lay-select-option>
              <lay-select-option :value="3" label="机密"></lay-select-option>
            </lay-select>
          </lay-form-item>
          <lay-form-item label="附件" prop="attachments">
            <lay-upload v-model="model.attachments" url="/api/upload/uploadFiles" name="files" field="files" :size="51200" @done="uploadDone" 
              :multiple="true" :number="5" :accept="file">
              <template #preview>
                <div v-for="(comp, index) in model.attachments.split(',')" :key="index">
                  <a :href="comp">{{ comp.substring(comp.lastIndexOf('/') + 1) }}</a>
                </div>
              </template>
            </lay-upload>
          </lay-form-item>
        </lay-form>
        <div style="width: 100%; text-align: center">
          <lay-button size="sm" type="primary" @click="toSubmit"
            >保存</lay-button
          >
          <lay-button size="sm" @click="toCancel">取消</lay-button>
        </div>
      </div>
    </lay-layer>
  </lay-container>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue'
import { layer } from '@layui/layui-vue'
import { noticePage, addNotice, updateNotice, deleteNotice } from "@/api/module/notice"
const searchQuery = ref({
  title: '',
  rangeTime: []
})

function toReset() {
  searchQuery.value = {
    title: '',
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
  { title: '标题', key: 'title' },
  { title: '内容', key: 'content', ellipsisTooltip: 'true' },
  { title: '发文人', key: 'creatorName' },
  { title: '发文时间', key: 'createTime', sort: 'desc' },
  { title: '发文部门', key: 'departmentName' },
  { title: '附件', key: 'attachments', customSlot: 'attachments' },
  {
    title: '操作',
    width: '120px',
    customSlot: 'operator',
    key: 'operator',
    fixed: 'right'
  }
])
const change = (page: any) => {
  loading.value = true
  setTimeout(() => {
    handleQuery(page.current, page.limit)
    loading.value = false
  }, 100)
}

const dataSource = ref()

const model = ref({
  noticeId: 0,
  title: '',
  content: '',
  noticeLevel: '',
  security: '',
  avatar: '',
  attachments: '',
})
const rules = ref({
  title: {
    type :  'string',
    max : 25,
  },
})
const layFormRef = ref()
const visible = ref(false)
const title = ref('新增')
const changeVisible = (text: any, row?: any) => {
  title.value = text
  if (row) {
    let info = JSON.parse(JSON.stringify(row))
    model.value = info
  } else {
    model.value = {
      noticeId: 0,
      title: '',
      content: '',
      noticeLevel: '',
      security: '',
      avatar: '',
      attachments: '',
    }
  }
  visible.value = !visible.value
}

const uploadDone = (req: any) => {
  let data = JSON.parse(req.data)
  if (data.code == 1){
    model.value.attachments = data.data
  }
  else{
    layer.msg('上传失败！', { icon: 2, time: 1000 })
  }
}

function toRemove(noticeId: any) {
  layer.confirm('您将删除所选中的数据？', {
    title: '提示',
    btn: [
      {
        text: '确定',
        callback: (id: any) => {
          deleteNotice({noticeId: noticeId}).then((res: any) => {
            if (res.code == 1) {
              layer.msg('您已成功删除')
              layer.close(id)
              handleQuery(1, 10);
            } else {
              layer.msg('删除失败！', { icon: 2, time: 1000 })
            }
          });
        }
      },
      {
        text: '取消',
        callback: (id: any) => {
          layer.msg('您已取消操作')
          layer.close(id)
        }
      }
    ]
  })
}
function toSubmit() {
  layFormRef.value.validate((isValidate: any, model: any, errors: any) => {
    if(isValidate){
      if(model.noticeId === 0){
        // 新建
        addNotice(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery(page.current, page.limit);
					} else {
						layer.msg('保存失败', { icon: 2, time: 1000 })
					}
				});
      }
      else{
        // 修改
        updateNotice(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery(page.current, page.limit);
					} else {
						layer.msg('保存失败', { icon: 2, time: 1000 })
					}
				});
      }
    }
  });
}
function toCancel() {
  visible.value = false
}
function handleQuery(page1: any, size: any) {
	loading.value = true;
  let title = searchQuery.value.title;
  let rangeTime = searchQuery.value.rangeTime;
	noticePage({page: page1, size: size, title: title, beginDate: rangeTime[0], endDate: rangeTime[1]})
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
.notice-box {
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
}

.isChecked {
  display: inline-block;
  background-color: #e8f1ff;
  color: red;
}

.layui-tree-select{
  width: 100%;
}
</style>