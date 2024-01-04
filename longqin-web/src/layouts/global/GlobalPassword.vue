<template>
  <lay-layer
    title="修改密码"
    :area="['450px', '320px']"
    v-model="visiblePassword"
  >
  <div style="padding: 20px">
    <lay-form :model="pwdModel" ref="pwdModelRef" :rules="pwdRules" required>
      <lay-form-item label="原密码" prop="oldPwd">
        <lay-input v-model="pwdModel.oldPwd" type="password"></lay-input>
      </lay-form-item>
      <lay-form-item label="新密码" prop="newPwd">
        <lay-input v-model="pwdModel.newPwd" type="password"></lay-input>
      </lay-form-item>
      <lay-form-item label="确认密码" prop="confirmPwd">
        <lay-input v-model="pwdModel.confirmPwd" type="password"></lay-input>
      </lay-form-item>
    </lay-form>
    <div style="width: 100%; text-align: center">
      <lay-button size="sm" type="primary" @click="toPwdSubmit"
        >保存</lay-button
      >
      <lay-button size="sm" @click="toPwdCancel">取消</lay-button>
    </div>
  </div>
  </lay-layer>
</template>

<script lang="ts">
export default {
  name: 'GlobalPassword'
}
</script>

<script lang="ts" setup>
import { useAppStore } from '../../store/app'
import { useUserStore } from '../../store/user'
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { updatePassword, logout } from "@/api/module/user"

const emits = defineEmits(['update:modelValue'])

interface PasswordProps {
  modelValue: boolean
}

const props = withDefaults(defineProps<PasswordProps>(), {
  modelValue: false
})

const pwdModel = ref({
  oldPwd: '',
  newPwd: '',
  confirmPwd: ''
})
const pwdModelRef = ref()
const pwdRules = ref({
  oldPwd: {
    type :  'string',
    max : 25,
  },
  newPwd: {
    type :  'string',
    max : 25,
    min : 8
  },
  confirmPwd: {
    type :  'string',
    validator(rule, value) {
      return value === pwdModel.value.newPwd
    },
    message: "两次输入密码不一致"
  }
})

const visiblePassword = ref(props.modelValue)

const toPwdCancel = function() {
  visiblePassword.value = false
}
const router = useRouter()
const toPwdSubmit = function() {
  pwdModelRef.value.validate((isValidate: any, model: any, errors: any) => {
    if(isValidate){
      updatePassword({userId: useUserStore().userInfo.userId, oldPwd: model.oldPwd, newPwd: model.newPwd, confirmPwd: model.confirmPwd})
      .then((res: any) => {
        console.log(res)
        if (res.code == 1) {
          layer.msg('修改成功，请重新登录', { icon: 1, time: 1000 })
          setTimeout(() => {
            logout().then(({ data, code, msg }) => {
              if (code == 1) {
                const userInfoStore = useUserStore()
                userInfoStore.token = ''
                userInfoStore.userInfo = {}
                router.push('/login')
              } else {
                layer.msg(msg, { icon: 2 })
              }
            }), 1000
          })
        } else {
          layer.msg(res.msg, { icon: 2, time: 1000 })
        }
      });
    }
  });
}

watch(visiblePassword, (val) => {
  emits('update:modelValue', val)
})

watch(
  () => props.modelValue,
  (val) => {
    visiblePassword.value = val
  }
)
</script>
