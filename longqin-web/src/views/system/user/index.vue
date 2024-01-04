<template>
  <lay-container fluid="true" class="user-box">
    <lay-card>
      <lay-form style="margin-top: 10px">
        <lay-row>
          <lay-col :md="5">
            <lay-form-item label="部门" label-width="80">
              <lay-tree-select v-model="searchQuery.departmentId" :data="departments"></lay-tree-select>
            </lay-form-item>
          </lay-col>
          <lay-col :md="5">
            <lay-form-item label="昵称" label-width="80">
              <lay-input
                v-model="searchQuery.nickName"
                placeholder="请输入"
                size="sm"
                :allow-clear="true"
                style="width: 98%"
              ></lay-input>
            </lay-form-item>
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
        <template #avatar="{ row }">
          <lay-avatar :src="row.avatar"></lay-avatar>
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
            size="xs"
            border="blue"
            border-style="dashed"
            @click="toRoles(row)"
            >配置角色</lay-button
          >
          <lay-button
            @click="toRemove(row.userId)"
            size="xs"
            border="red"
            border-style="dashed"
          >
            删除
          </lay-button>
        </template>
      </lay-table>
    </div>

    <lay-layer v-model="visible" :title="title" :area="['500px', '550px']">
      <div style="padding: 20px">
        <lay-form :model="model" ref="layFormRef" :rules="rules">
          <lay-form-item v-if="useUserStore().userInfo.organizationId == 0" label="所属公司" prop="organizationId" required>
            <lay-select v-model="model.organizationId" :options="organizationItems"></lay-select>
          </lay-form-item>
          <lay-form-item label="账号名" prop="userName" required>
            <lay-input v-model="model.userName"></lay-input>
          </lay-form-item>
          <lay-form-item label="昵称" prop="nickName" required>
            <lay-input v-model="model.nickName"></lay-input>
          </lay-form-item>
          <lay-form-item label="所属部门" prop="departmentId">
            <lay-tree-select v-model="model.departmentId" :data="departments"></lay-tree-select>
          </lay-form-item>
          <lay-form-item label="职位" prop="positionId">
            <lay-tree-select v-model="model.positionId" :data="positions"></lay-tree-select>
          </lay-form-item>
          <lay-form-item label="头像" prop="avatar">
            <lay-upload v-model="model.avatar" url="/api/upload/uploadFile" field="file" :size="10240" @done="uploadDone">
              <template #preview>
                <img :src="model.avatar" width="160" height="90" />
              </template>
            </lay-upload>
          </lay-form-item>
          <lay-form-item label="手机号" prop="phone">
            <lay-input v-model="model.phone"></lay-input>
          </lay-form-item>
          <lay-form-item label="email" prop="email">
            <lay-input v-model="model.email"></lay-input>
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

    <lay-layer v-model="visibleRole" title="配置角色" :area="['500px', '450px']">
      <div style="padding: 20px; height: 300px; overflow: auto">
        <lay-checkbox-group v-model="checkedKeys" @change="groupChange">
          <lay-checkbox v-for="(item, index) in roles" :key="index" name="roles" :value="item.roleId">{{ item.roleName }}</lay-checkbox>
        </lay-checkbox-group>
      </div>
      <lay-line></lay-line>
      <div style="width: 90%; text-align: right">
        <lay-button size="sm" type="primary" @click="toSaveRole">保存</lay-button>
        <lay-button size="sm" @click="toCancel">取消</lay-button>
      </div>
    </lay-layer>
  </lay-container>
</template>
<script setup lang="ts">
import { ref, reactive } from 'vue'
import { layer } from '@layui/layui-vue'
import { useUserStore } from '../../../store/user'
import { userPage, addUser, updateUser, deleteUser, getUserRole, updateUserRole } from "@/api/module/user"
import { organizationList } from "@/api/module/organization"
import { departmentTree } from "@/api/module/department"
import { positionTree } from "@/api/module/position"
import { roleList } from "@/api/module/role";
const searchQuery = ref({
  departmentId: '',
  nickName: ''
})

function toReset() {
  searchQuery.value = {
    departmentId: '',
    nickName: ''
  }
}

function toSearch() {
  page.current = 1
  change(page)
}

const loading = ref(false)
const page = reactive({ current: 1, limit: 10, total: 100 })
const columns = ref([
  { title: '所属部门', key: 'departmentName' },
  { title: '账号名', key: 'userName', sort: 'desc' },
  { title: '昵称', key: 'nickName', sort: 'desc' },
  { title: '职位', key: 'positionName' },
  { title: '头像', key: 'avatar', customSlot: 'avatar' },
  { title: '手机号', key: 'phone' },
  { title: '邮箱', key: 'email' },
  {
    title: '操作',
    width: '180px',
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
  userId: 0,
  userName: '',
  nickName: '',
  departmentId: '',
  positionId: '',
  avatar: '',
  phone: '',
  email: '',
  organizationId: useUserStore().userInfo.organizationId == 0 ? null : 0
})
const rules = ref({
  userName: {
    type :  'string',
    pattern: /^\w+$/,
    message: '账号名只能由字母数字和下划线组成，且不超过25位',
    max : 25,
  },
  nickName: {
    type :  'string',
    max : 25
  },
  phone: {
    type :  'string',
    max : 20
  },
  email: {
    type :  'email'
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
      userId: 0,
      userName: '',
      nickName: '',
      departmentId: '',
      positionId: '',
      avatar: '',
      phone: '',
      email: '',
      organizationId: useUserStore().userInfo.organizationId == 0 ? null : 0
    }
  }
  visible.value = !visible.value
}

const organizationItems = ref()
const departments = ref()
const positions = ref()

const uploadDone = (req: any) => {
  let data = JSON.parse(req.data)
  if (data.code == 1){
    model.value.avatar = data.data
  }
  else{
    layer.msg('上传失败！', { icon: 2, time: 1000 })
  }
}

function toRemove(userId: any) {
  layer.confirm('您将删除所选中的数据？', {
    title: '提示',
    btn: [
      {
        text: '确定',
        callback: (id: any) => {
          deleteUser({userId: userId}).then((res: any) => {
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
      if(model.userId === 0){
        // 新建
        addUser(model).then((res: any) => {
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
        updateUser(model).then((res: any) => {
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
  visibleRole.value = false
}

const visibleRole = ref(false)
const checkedKeys = ref([])
const showCheckbox = ref(true)
const userId = ref()

const roles = ref()

function toRoles(row: any) {
  visibleRole.value = true
  userId.value = row.userId
  roleList().then((res: any) => {
    roles.value = res.data;
    getUserRole({userId: row.userId}).then((res: any) => {
      checkedKeys.value = res.data;
    });
	})
}
function toSaveRole() {
  updateUserRole({userId: userId.value, roleIdStr: checkedKeys.value.join(',')}).then((res: any) => {
    if (res.code == 1) {
      layer.msg('保存成功', { icon: 1, time: 1000 })
      //visibleMenu.value = false
      //handleQuery();
    } else {
      layer.msg('保存失败', { icon: 2, time: 1000 })
    }
  });
}
const groupChange = function(val) {
  //alert(checkedKeys.value);
}

function handleQuery(page1: any, size: any) {
	loading.value = true;
  let departmentId = searchQuery.value.departmentId;
  let nickName = searchQuery.value.nickName;
	userPage({page: page1, size: size, departmentId: departmentId, nickName: nickName})
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
  if (useUserStore().userInfo.organizationId == 0){
    organizationList()
      .then((res: any) => {
        const options = [];
        for(const r in res.data){
          const org = res.data[r];
          options.push({ label: org.organizationName, value: org.organizationId })
        }
        organizationItems.value = options;
      });
  }
  departmentTree()
		.then((res: any) => {
			departments.value = res.data;
		});
  positionTree()
		.then((res: any) => {
			positions.value = res.data;
		});
});
</script>

<style scoped>
.user-box {
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