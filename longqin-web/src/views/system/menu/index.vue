<template>
  <lay-container fluid="true" class="menu-box">
    <!-- table -->
    <div class="table-box">
      <lay-table
        :height="`570px`"
        ref="tableRef"
        :loading="loading"
        children-column-name="children"
        :columns="columns"
        :data-source="dataSource"
        :default-toolbar="true"
        :default-expand-all="defaultExpandAll"
        :expand-index="0"
      >
        <template #toolbar>
          <lay-button
            size="sm"
            @click="changeVisible('新建', null)"
            type="normal"
          >
            新建
          </lay-button>
          <lay-button size="sm" @click="expandAll(true)">展开全部</lay-button>
          <lay-button size="sm" @click="expandAll(false)">折叠全部</lay-button>
        </template>
        <template #name="{ row }">
          <lay-icon :class="row.menuIcon"></lay-icon> &nbsp;&nbsp;
          {{ row.menuName }}
        </template>
        <template #option="{ row }">
          <lay-button
            @click="changeVisible('修改', row)"
            size="xs"
            border="green"
            border-style="dashed"
          >
            修改
          </lay-button>
          <lay-button
            @click="toRemove(row.menuId)"
            size="xs"
            border="red"
            border-style="dashed"
          >
            删除
          </lay-button>
        </template>
      </lay-table>
    </div>

    <lay-layer v-model="visible" :title="title" :area="['700px', '320px']">
      <div style="padding: 20px">
        <lay-form :model="model" ref="layFormRef" :rules="rules">
          <lay-row>
            <lay-col md="12">
              <lay-form-item label="菜单名称" prop="menuName" required>
                <lay-input v-model="model.menuName"></lay-input>
              </lay-form-item>
              <lay-form-item label="菜单路径" prop="menuUrl" required>
                <lay-input v-model="model.menuUrl"></lay-input>
              </lay-form-item>
              <lay-form-item label="图标" prop="menuIcon">
                <lay-input v-model="model.menuIcon"></lay-input>
              </lay-form-item>
            </lay-col>
            <lay-col md="12">
              <lay-form-item label="上级菜单" prop="parentId">
                <lay-tree-select v-model="model.parentId" :data="dataSource"></lay-tree-select>
              </lay-form-item>
              <lay-form-item label="排序" prop="groupSeq" required>
                <lay-input-number
                  style="width: 100%"
                  v-model="model.groupSeq"
                  position="right"
                ></lay-input-number>
              </lay-form-item>
            </lay-col>
          </lay-row>
        </lay-form>
        <div style="width: 97%; text-align: right">
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
import { menuList, addMenu, updateMenu, deleteMenu } from "@/api/module/menu";

const change = (page: any) => {
  loading.value = true
  setTimeout(() => {
    //
    loading.value = false
  }, 1000)
}
const page = reactive({ current: 1, limit: 10, total: 100 })
const loading = ref(false)
const tableRef = ref()

const columns = [
  {
    title: '菜单名称',
    key: 'menuName'
  },
  {
    title: '菜单路径',
    key: 'menuUrl'
  },
  {
    title: '图标',
    key: 'menuIcon'
  },
  {
    title: '排序',
    width: '120px',
    key: 'groupSeq'
  },
  {
    title: '操作',
    width: '150px',
    key: 'option',
    customSlot: 'option'
  }
]

const dataSource = ref()

const defaultExpandAll = ref(false)

const expandAll = function (flag: any) {
  defaultExpandAll.value = flag
}
const model = ref({
  menuId: 0,
  menuName: '',
  menuUrl: '',
  menuIcon: '',
  groupSeq: 0,
  parentId: -1
})
const rules = ref({
  menuName: {
    type :  'string',
    min : 2,
    max : 25
  },
  menuUrl: {
    type :  'string',
    max : 255
  },
  menuIcon: {
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
      menuId: 0,
      menuName: '',
      menuUrl: '',
      menuIcon: '',
      groupSeq: 0,
      parentId: -1
    }
  }
  visible.value = !visible.value
}
// 清除校验
const clearValidate = function () {
  layFormRef.value.clearValidate()
}
// 重置表单
const reset = function () {
  layFormRef.value.reset()
}
function toRemove(menuId : any) {
  layer.confirm('您将删除所有选中的数据？', {
    title: '提示',
    btn: [
      {
        text: '确定',
        callback: (id: any) => {
          deleteMenu({menuId: menuId}).then((res: any) => {
            if (res.code == 1) {
              layer.msg('您已成功删除')
              layer.close(id)
              handleQuery();
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
      if(model.menuId === 0){
        // 新建
        addMenu(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功！', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery();
					} else {
						layer.msg('保存失败！', { icon: 2, time: 1000 })
					}
				});
      }
      else{
        // 修改
        updateMenu(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功！', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery();
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
function handleQuery() {
	// 重置父组件
	loading.value = true;
	menuList()
		.then((res: any) => {
			dataSource.value = res.data;
		})
		.then(() => {
			loading.value = false;
		});
}
onMounted(() => {
	handleQuery();
});
</script>

<style scoped>
.menu-box {
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
  height: calc(100vh - 110px);
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