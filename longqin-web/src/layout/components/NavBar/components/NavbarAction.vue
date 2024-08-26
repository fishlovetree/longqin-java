<template>
  <div class="flex">
    <template v-if="!isMobile">
      <!--全屏 -->
      <div class="nav-action-item" @click="toggle">
        <svg-icon
          :icon-class="isFullscreen ? 'fullscreen-exit' : 'fullscreen'"
        />
      </div>

      <!-- 布局大小 -->
      <!-- <el-tooltip
        :content="$t('sizeSelect.tooltip')"
        effect="dark"
        placement="bottom"
      >
        <size-select class="nav-action-item" />
      </el-tooltip> -->

      <!-- 语言选择 -->
      <!-- <lang-select class="nav-action-item" /> -->

      <!-- 消息通知 -->
      <!-- <el-dropdown class="message nav-action-item" trigger="click">
        <el-badge is-dot>
          <div class="flex-center h100% p10px">
            <i-ep-bell />
          </div>
        </el-badge>
        <template #dropdown>
          <div class="px-5 py-2">
            <el-tabs v-model="activeTab">
              <el-tab-pane
                v-for="(label, key) in MessageTypeLabels"
                :label="label"
                :name="key"
                :key="key"
              >
                <div
                  class="w-[380px] py-2"
                  v-for="message in getFilteredMessages(key)"
                  :key="message.id"
                >
                  <el-link type="primary">
                    <el-text class="w-350px" size="default" truncated>
                      {{ message.title }}</el-text
                    >
                  </el-link>
                </div>
              </el-tab-pane>
            </el-tabs>
            <el-divider />
            <div class="flex-x-between">
              <el-link type="primary" :underline="false">
                <span class="text-xs">查看更多</span
                ><el-icon class="text-xs"><ArrowRight /></el-icon
              ></el-link>
              <el-link type="primary" :underline="false">
                <span class="text-xs">全部已读</span></el-link
              >
            </div>
          </div>
        </template>
      </el-dropdown> -->
    </template>

    <!-- 用户头像 -->
    <el-dropdown class="nav-action-item" trigger="click">
      <div class="flex-center h100% p10px">
        <img
          :src="avatar + '?imageView2/1/w/80/h/80'"
          class="rounded-full mr-10px w24px w24px"
        />
        <span>{{ userStore.user.username }}</span>
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="setUser">账号设置</el-dropdown-item>
          <el-dropdown-item @click="updatePwd">修改密码</el-dropdown-item>
          <el-dropdown-item divided @click="logout">
            {{ $t("navbar.logout") }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>

    <!-- 设置 -->
    <template v-if="defaultSettings.showSettings">
      <div class="nav-action-item" @click="settingStore.settingsVisible = true">
        <svg-icon icon-class="setting" />
      </div>
    </template>

    <!-- 修改密码弹窗 -->
    <el-drawer
      v-model="dialog.visible"
      :title="dialog.title"
      append-to-body
      @close="handleCloseDialog"
    >
      <!-- 用户新增/编辑表单 -->
      <el-form
        ref="pwdFormRef"
        :model="pwdFormData"
        :rules="pwdRules"
        label-width="80px"
      >
        <el-form-item label="原密码" prop="oldPwd">
          <el-input
            v-model="pwdFormData.oldPwd"
            placeholder="请输入原密码"
            type="password"
            maxlength="25"
            show-password
          />
        </el-form-item>
        <el-form-item label="原密码" prop="newPwd">
          <el-input
            v-model="pwdFormData.newPwd"
            placeholder="请输入新密码"
            type="password"
            maxlength="25"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd">
          <el-input
            v-model="pwdFormData.confirmPwd"
            placeholder="请输入确认密码"
            type="password"
            maxlength="25"
            show-password
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handlePwdSubmit">确 定</el-button>
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import {
  useAppStore,
  useTagsViewStore,
  useUserStore,
  useSettingsStore,
} from "@/store";
import defaultSettings from "@/settings";
import { DeviceEnum } from "@/enums/DeviceEnum";
import { MessageTypeEnum, MessageTypeLabels } from "@/enums/MessageTypeEnum";
import UserAPI from "@/api/user";

const appStore = useAppStore();
const tagsViewStore = useTagsViewStore();
const userStore = useUserStore();
const settingStore = useSettingsStore();

const route = useRoute();
const router = useRouter();

const avatar = sessionStorage.getItem('avatar');

const isMobile = computed(() => appStore.device === DeviceEnum.MOBILE);

const { isFullscreen, toggle } = useFullscreen();

const activeTab = ref(MessageTypeEnum.MESSAGE);

const messages = ref([
  {
    id: 1,
    title: "系统升级通知：服务器将于今晚12点进行升级维护，请提前保存工作内容。",
    type: MessageTypeEnum.MESSAGE,
  },
  {
    id: 2,
    title: "新功能发布：我们的应用程序现在支持多语言功能。",
    type: MessageTypeEnum.MESSAGE,
  },
  {
    id: 3,
    title: "重要提醒：请定期更改您的密码以保证账户安全。",
    type: MessageTypeEnum.MESSAGE,
  },
  {
    id: 4,
    title: "通知：您有一条未读的系统消息，请及时查看。",
    type: MessageTypeEnum.NOTICE,
  },
  {
    id: 5,
    title: "新订单通知：您有一笔新的订单需要处理。",
    type: MessageTypeEnum.NOTICE,
  },
  {
    id: 6,
    title: "审核提醒：您的审核请求已被批准。",
    type: MessageTypeEnum.NOTICE,
  },
  { id: 7, title: "待办事项：完成用户权限设置。", type: MessageTypeEnum.TODO },
  { id: 8, title: "待办事项：更新产品列表。", type: MessageTypeEnum.TODO },
  { id: 9, title: "待办事项：备份数据库。", type: MessageTypeEnum.TODO },
]);

const getFilteredMessages = (type: MessageTypeEnum) => {
  return messages.value.filter((message) => message.type === type);
};

/* 账号设置 */
function setUser(){
  router.push(`/user/baseset`);
}

/* 注销 */
function logout() {
  ElMessageBox.confirm("确定注销并退出系统吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
    lockScroll: false,
  }).then(() => {
    userStore
      .logout()
      .then(() => {
        tagsViewStore.delAllViews();
      })
      .then(() => {
        router.push(`/login?redirect=${route.fullPath}`);
      });
  }).catch(()=>{})
}

const pwdFormRef = ref(ElForm);
const pwdFormData = reactive({
  oldPwd: '',
  newPwd: '',
  confirmPwd: '',
});
/** 密码校验规则  */
const validateConfirmPwd = (rule, value, callback) => {
  if (!value) {
    return callback(new Error('确认密码不能为空'));
  }
  if (value != pwdFormData.newPwd) {
    return callback(new Error('两次输入密码不一致'));
  }
  callback();
};
const pwdRules = reactive({
  oldPwd: [
    { required: true, message: "原密码不能为空", trigger: "blur" },
  ],
  newPwd: [
    { required: true, message: "新密码不能为空", trigger: "blur" },
    { min: 8, max: 25, message: '输入长度在 8 到 25 个字符', trigger: 'blur' },
  ],
  confirmPwd: [
    { validator: validateConfirmPwd, trigger: 'blur' },
  ],
});
/**  用户弹窗对象  */
const dialog = reactive({
  visible: false,
  title: "修改密码",
});

/**
 * 打开修改密码弹窗
 *
 */
 function updatePwd() {
  dialog.visible = true;
}

/** 关闭修改密码弹窗 */
function handleCloseDialog() {
  dialog.visible = false;
  pwdFormRef.value.resetFields();
  pwdFormRef.value.clearValidate();

  pwdFormData.oldPwd = '';
  pwdFormData.newPwd = '';
  pwdFormData.confirmPwd = '';
}

const loading = ref(false);
/** 修改密码提交 */
const handlePwdSubmit = useThrottleFn(() => {
  pwdFormRef.value.validate((valid: any) => {
    if (valid) {
      const userId = sessionStorage.getItem('userId');
      loading.value = true;
      UserAPI.updatePassword(userId, pwdFormData.oldPwd, pwdFormData.newPwd, pwdFormData.confirmPwd)
        .then(() => {
          ElMessage.success("修改密码成功");
          handleCloseDialog();
          userStore
          .logout()
          .then(() => {
            tagsViewStore.delAllViews();
          })
          .then(() => {
            router.push(`/login?redirect=${route.fullPath}`);
          });
        })
        .finally(() => (loading.value = false));
    }
  });
}, 3000);
</script>
<style lang="scss" scoped>
.nav-action-item {
  display: inline-block;
  min-width: 40px;
  height: $navbar-height;
  line-height: $navbar-height;
  color: var(--el-text-color);
  text-align: center;
  cursor: pointer;

  &:hover {
    background: rgb(0 0 0 / 10%);
  }
}

:deep(.message .el-badge__content.is-fixed.is-dot) {
  top: 5px;
  right: 10px;
}

:deep(.el-divider--horizontal) {
  margin: 10px 0;
}

.dark .nav-action-item:hover {
  background: rgb(255 255 255 / 20%);
}

.see-more {
  padding: 10px 0;
  text-align: center;
}

.see-more a {
  color: var(--el-color-primary);
  text-decoration: none;
}
</style>
