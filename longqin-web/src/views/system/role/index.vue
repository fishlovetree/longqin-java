<template>
  <lay-container fluid="true" class="role-box">
    <!-- table -->
    <div class="table-box">
      <lay-table
        class="table-style"
        :page="page"
        :columns="columns"
        :loading="loading"
        :default-toolbar="true"
        :data-source="dataSource"
      >
        <template v-slot:toolbar>
          <lay-button
            size="sm"
            type="primary"
            @click="changeVisible('新增', null)"
          >
            <lay-icon class="layui-icon-addition"></lay-icon>
            新增</lay-button
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
            size="xs"
            border="blue"
            border-style="dashed"
            @click="toPrivileges(row)"
            >分配权限</lay-button
          >
          <lay-button
            @click="toRemove(row.roleId)"
            size="xs"
            border="red"
            border-style="dashed"
          >
            删除
          </lay-button>
        </template>
      </lay-table>
    </div>

    <lay-layer v-model="visible" :title="title" :area="['500px', '320px']">
      <div style="padding: 20px">
        <lay-form :model="model" ref="layFormRef" :rules="rules">
          <lay-form-item v-if="useUserStore().userInfo.organizationId == 0" label="所属公司" prop="organizationId" required>
            <lay-select v-model="model.organizationId" :options="organizationItems"></lay-select>
          </lay-form-item>
          <lay-form-item label="角色名称" prop="roleName" required>
            <lay-input v-model="model.roleName"></lay-input>
          </lay-form-item>
          <lay-form-item label="描述" prop="description">
            <lay-textarea
              placeholder="请输入描述"
              v-model="model.description"
            ></lay-textarea>
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

    <lay-layer v-model="visibleMenu" title="分配权限" :area="['500px', '450px']">
      <div style="height: 320px; overflow: auto">
        <lay-tree
          style="margin-left: 40px"
          :tail-node-icon="false"
          :data="menuJson"
          :showCheckbox="showCheckbox"
          v-model:checkedKeys="checkedKeys"
        >
          <template #title="{ data }">
            <lay-icon :class="data.menuIcon"></lay-icon>
            {{ data.title }}
          </template>
        </lay-tree>
      </div>
      <lay-line></lay-line>
      <div style="width: 90%; text-align: right">
        <lay-button size="sm" type="primary" @click="toSaveMenu">保存</lay-button>
        <lay-button size="sm" @click="toCancel">取消</lay-button>
      </div>
    </lay-layer>
  </lay-container>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue'
import { layer } from '@layui/layui-vue'
import { useUserStore } from '../../../store/user'
import { roleList, addRole, updateRole, deleteRole, getRoleMenu, updateRoleMenu } from "@/api/module/role";

const loading = ref(false)
const columns = ref([
  { title: '角色名称', key: 'roleName', sort: 'roleName' },
  { title: '备注', key: 'description' },
  { title: '创建时间', key: 'createTime' },
  {
    title: '操作',
    width: '180px',
    customSlot: 'operator',
    key: 'operator',
    fixed: 'right'
  }
])

const dataSource = ref()

const model = ref({
  roleId: 0,
  roleName: '',
  description: '',
  organizationId: useUserStore().userInfo.organizationId == 0 ? null : 0
})
const rules = ref({
  roleName: {
    type :  'string',
    max : 25
  },
  description: {
    type :  'string',
    max : 50
  }
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
      roleId: 0,
      roleName: '',
      description: '',
      organizationId: useUserStore().userInfo.organizationId == 0 ? null : 0
    }
  }
  visible.value = !visible.value
}

function toRemove(roleId : any) {
  layer.confirm('您将删除所选中的数据？', {
    title: '提示',
    btn: [
      {
        text: '确定',
        callback: (id: any) => {
          deleteRole({roleId: roleId}).then((res: any) => {
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
      if(model.roleId === 0){
        // 新建
        addRole(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery();
					} else {
						layer.msg('保存失败', { icon: 2, time: 1000 })
					}
				});
      }
      else{
        // 修改
        updateRole(model).then((res: any) => {
					if (res.code == 1) {
						layer.msg('保存成功', { icon: 1, time: 1000 })
            visible.value = false
						handleQuery();
					} else {
						layer.msg('保存失败', { icon: 2, time: 1000 })
					}
				});
      }
    }
  });
}
function toSaveMenu() {
  updateRoleMenu({roleId: roleId.value, menuIdStr: checkedKeys.value.join(',')}).then((res: any) => {
    if (res.code == 1) {
      layer.msg('保存成功', { icon: 1, time: 1000 })
      //visibleMenu.value = false
      //handleQuery();
    } else {
      layer.msg('保存失败', { icon: 2, time: 1000 })
    }
  });
}
function toCancel() {
  visible.value = false
  visibleMenu.value = false
}
function confirm() {
  layer.msg('您已成功删除')
}
function cancel() {
  layer.msg('您已取消操作')
}

const visibleMenu = ref(false)
const checkedKeys = ref([])
const showCheckbox = ref(true)
const roleId = ref()

const menuJson = useUserStore().menus

function toPrivileges(row: any) {
  visibleMenu.value = true
  roleId.value = row.roleId
  getRoleMenu({roleId: row.roleId}).then((res: any) => {
        checkedKeys.value = res.data;
      });
}

function handleQuery() {
	// 重置父组件
	loading.value = true;
	roleList()
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
.role-box {
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
  height: 100%;
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