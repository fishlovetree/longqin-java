<template>
  <lay-container fluid="true" class="organization-box">
    <!-- table -->
    <div class="table-box">
      <lay-table
        class="table-style"
        :page="page"
        :columns="columns"
        :loading="loading"
        :default-toolbar="true"
        :data-source="dataSource"
        v-model:selected-keys="selectedKeys"
        @change="change"
        @sortChange="sortChange"
      >
        <template #logoPath="{ row }">
          <lay-avatar :src="row.logoPath"></lay-avatar>
        </template>

        <template v-slot:toolbar>
          <lay-button
            size="sm"
            type="primary"
            @click="changeVisible('新增', null)"
            >新增</lay-button
          >
        </template>
        <template v-slot:operator="{ row }">
          <lay-button
            size="xs"
            border="green"
            border-style="dashed"
            @click="changeVisible('编辑', row)"
            >编辑</lay-button
          >
          <lay-button
              @click="toRemove(row.organizationId)"
              size="xs"
              border="red"
              border-style="dashed"
            >
              删除
            </lay-button>
        </template>
      </lay-table>
    </div>

    <lay-layer v-model="visible" :title="title" :area="['500px', '570px']">
      <div style="padding: 20px">
        <lay-form :model="model" ref="layFormRef" :rules="rules">
          <lay-form-item label="公司编码" prop="organizationCode" required>
            <lay-input v-model="model.organizationCode"></lay-input>
          </lay-form-item>
          <lay-form-item label="公司名称" prop="organizationName" required>
            <lay-input v-model="model.organizationName"></lay-input>
          </lay-form-item>
          <lay-form-item label="地址" prop="address">
            <lay-input v-model="model.address"></lay-input>
          </lay-form-item>
          <lay-form-item label="联系电话" prop="phone">
            <lay-input v-model="model.phone"></lay-input>
          </lay-form-item>
          <lay-form-item label="LOGO" prop="logoPath">
            <lay-upload v-model="model.logoPath" url="/api/upload/uploadFile" field="file" :size="10240" @done="uploadDone">
              <template #preview>
                <img :src="model.logoPath" width="160" height="90" />
              </template>
            </lay-upload>
          </lay-form-item>
          <lay-form-item label="系统名称" prop="systemName">
            <lay-input v-model="model.systemName"></lay-input>
          </lay-form-item>
          <lay-form-item label="公司简介" prop="introduction">
            <lay-input v-model="model.introduction"></lay-input>
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
import { organizationPage, addOrganization, updateOrganization, deleteOrganization } from "@/api/module/organization";

const loading = ref(false)
const page = reactive({ current: 1, limit: 10, total: 100 })
const columns = ref([
  { title: '公司编号', key: 'organizationCode', sort: 'organizationCode' },
  { title: '公司名称', key: 'organizationName', sort: 'organizationName' },
  { title: '地址', key: 'address' },
  { title: '联系电话', key: 'phone' },
  { title: 'LOGO', key: 'logoPath', customSlot: 'logoPath' },
  { title: '系统名称', key: 'systemName' },
  { title: '简介', key: 'introduction' },
  {
    title: '操作',
    width: '150px',
    customSlot: 'operator',
    key: 'operator',
    fixed: 'right'
  }
])
const change = (page: any) => {
  loading.value = true
  setTimeout(() => {
    handleQuery(page.current, page.limit);
    //dataSource.value = loadDataSource(page.current, page.limit)
    loading.value = false
  }, 100)
}
const sortChange = (key: any, sort: number) => {
  //layer.msg(`字段${key} - 排序${sort}, 你可以利用 sort-change 实现服务端排序`)
}
const dataSource = ref()

const model = ref({
  organizationId: 0,
  organizationCode: '',
  organizationName: '',
  address: '',
  phone: '',
  logoPath: '',
  systemName: '',
  introduction: ''
})
const rules = ref({
  organizationCode: {
    type :  'string',
    max : 25
  },
  organizationName: {
    type :  'string',
    max : 50
  },
  address: {
    type :  'string',
    max : 100
  },
  phone: {
    type :  'string',
    max : 20
  },
  systemName: {
    type :  'string',
    max : 25
  },
  introduction: {
    type :  'string',
    max : 100
  },
})
const layFormRef = ref()
const visible = ref(false)
const title = ref('新增')
const changeVisible = (text: any, row: any) => {
  title.value = text
  if (row != null) {
    let info = JSON.parse(JSON.stringify(row))
    model.value = info
  } else {
    model.value = {
      organizationId: 0,
      organizationCode: '',
      organizationName: '',
      address: '',
      phone: '',
      logoPath: '',
      systemName: '',
      introduction: ''
    }
  }
  visible.value = !visible.value
}
const uploadDone = (req: any) => {
  let data = JSON.parse(req.data)
  if (data.code == 1){
    model.value.logoPath = data.data
  }
  else{
    layer.msg('上传失败！', { icon: 2, time: 1000 })
  }
}
function toRemove(organizationId) {
  layer.confirm('您将删除所选中的数据？', {
    title: '提示',
    btn: [
      {
        text: '确定',
        callback: (id: any) => {
          deleteOrganization({organizationId: organizationId}).then((res: any) => {
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
      if(model.organizationId === 0){
        // 新建
        addOrganization(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功！', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery(page.current, page.limit);
					} else {
						layer.msg('保存失败！', { icon: 2, time: 1000 })
					}
				});
      }
      else{
        // 修改
        updateOrganization(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功！', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery(page.current, page.limit);
					} else {
						layer.msg('保存失败！', { icon: 2, time: 1000 })
					}
				});
      }
    }
  });
}
function toCancel() {
  visible.value = false
}
function confirm() {
  layer.msg('您已成功删除')
}
function cancel() {
  layer.msg('您已取消操作')
}

function handleQuery(page1: number, size: number) {
	// 重置父组件
	loading.value = true;
	organizationPage({page: page1, size: size})
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
.organization-box {
  width: calc(100vw - 240px);
  height: calc(100vh - 110px);
  margin-top: 10px;
  box-sizing: border-box;
  background-color: #fff;
  overflow: hidden;
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
</style>