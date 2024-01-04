<template>
  <div style="height: 100%; width: 100%">
    <div style="height: 100%; width: 100%; overflow: auto">
      <lay-container
        :fluid="true"
        style="padding: 10px; padding-top: 10px; position: relative; height: 100%;"
      >
        <lay-form :model="model" ref="layFormRef" :rules="rules">
          <lay-card title="基本信息">
            <lay-row>
              <lay-col :md="10" :md-offset="7">
                <lay-form-item label="账号名" prop="userName">
                  <lay-input v-model="model.userName" disabled></lay-input>
                </lay-form-item>
                <lay-form-item label="昵称" prop="nickName" required>
                  <lay-input v-model="model.nickName"></lay-input>
                </lay-form-item>
                <lay-form-item label="所属部门" prop="departmentId">
                  <lay-tree-select v-model="model.departmentId" :data="departments" disabled></lay-tree-select>
                </lay-form-item>
                <lay-form-item label="职位" prop="positionId">
                  <lay-tree-select v-model="model.positionId" :data="positions" disabled></lay-tree-select>
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
                <lay-form-item>
                  <lay-row :space="20">
                    <lay-col :md="12">
                      <lay-button
                        :fluid="true"
                        @click="submitClick"
                        type="primary"
                        >提交</lay-button
                      >
                    </lay-col>
                    <lay-col :md="12">
                      <lay-button :fluid="true" @click="resetClick"
                        >重置</lay-button
                      >
                    </lay-col>
                  </lay-row>
                </lay-form-item>
              </lay-col>
            </lay-row>
          </lay-card>
        </lay-form>
      </lay-container>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { layer } from '@layui/layer-vue'
import { useUserStore } from '../../../store/user'
import { updateUser } from "@/api/module/user"
import { departmentTree } from "@/api/module/department"
import { positionTree } from "@/api/module/position"

const model = ref({
  userId: useUserStore().userInfo.userId,
  userName: useUserStore().userInfo.userName,
  nickName: useUserStore().userInfo.nickName,
  departmentId: useUserStore().userInfo.departmentId,
  positionId: useUserStore().userInfo.positionId,
  avatar: useUserStore().userInfo.avatar,
  phone: useUserStore().userInfo.phone,
  email: useUserStore().userInfo.email,
});
const rules = ref({
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
const departments = ref({id:1,title:'hello'})
const positions = ref()

const submitClick = function () {
  layFormRef.value.validate((isValidate: any, model: any, errors: any) => {
    if(isValidate){
      updateUser(model).then((res: any) => {
        if (res.code == 1) {
          layer.msg('保存成功', { icon: 1, time: 1000 })
          useUserStore().userInfo.nickName = model.nickName;
          useUserStore().userInfo.avatar = model.avatar;
          useUserStore().userInfo.phone = model.phone;
          useUserStore().userInfo.email = model.email;
        } else {
          layer.msg('保存失败', { icon: 2, time: 1000 })
        }
      });
    }
  });
}

const resetClick = function () {
  model.value = {
    userId: useUserStore().userInfo.userId,
    userName: useUserStore().userInfo.userName,
    nickName: useUserStore().userInfo.nickName,
    departmentId: useUserStore().userInfo.departmentId,
    positionId: useUserStore().userInfo.positionId,
    avatar: useUserStore().userInfo.avatar,
    phone: useUserStore().userInfo.phone,
    email: useUserStore().userInfo.email,
  };
}

const uploadDone = (req: any) => {
  let data = JSON.parse(req.data)
  if (data.code == 1){
    model.value.avatar = data.data
  }
  else{
    layer.msg('上传失败！', { icon: 2, time: 1000 })
  }
}

onMounted(() => {
  departmentTree()
    .then((res: any) => {
      departments.value = res.data;
    });
  positionTree()
    .then((res: any) => {
      positions.value = res.data;
      console.log(positions.value)
    });
});
</script>

<style scoped>
.title {
  font-size: 20px;
  font-weight: 500;
  margin-top: 12px;
  margin-bottom: 20px;
}

.describe {
  font-size: 14px;
  margin-bottom: 12px;
}
</style>
