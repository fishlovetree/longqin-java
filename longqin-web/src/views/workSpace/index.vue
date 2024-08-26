<template>
  <div class="dashboard-container">
    <el-card shadow="never">
      <el-row justify="space-between">
        <el-col :span="18" :xs="24">
          <div class="flex h-full items-center">
            <img
              class="w-20 h-20 mr-5 rounded-full"
              :src="avatar"
            />
            <div>
              <p>{{ greetings }}</p>
              <p class="text-sm text-gray">
                {{ currentDate }}
              </p>
            </div>
          </div>
        </el-col>

        <el-col :span="6" :xs="24">
          <div class="flex h-full items-center justify-around">
            <el-link :underline="false" v-for="item in statisticData" @click="handleDataClick(item.path)">
              <el-statistic
                :key="item.key"
                :value="item.value"
              >
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    <svg-icon :icon-class="item.iconClass" size="20px" />
                    <span class="text-[16px] ml-1">{{ item.title }}</span>
                  </div>
                </template>
              </el-statistic>
            </el-link>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-row :gutter="10" class="mt-5">
      <el-col :xs="24" :span="16">
        <el-card style="height: 300px;">
          <template #header>
            <div class="card-header">
              <span>待办工作</span>
            </div>
          </template>

          <el-table
            v-loading="loading"
            :data="backlogData"
            highlight-current-row
            style="width: 100%"
          >
            <el-table-column label="流程名称" prop="flowName" width="150" />
            <el-table-column label="任务" prop="nodeName" width="150" />
            <el-table-column label="提交时间" prop="submitTime" width="180" />
            <el-table-column label="发起人" prop="creatorName" min-width="120" />
            <el-table-column label="发起时间" prop="createTime" width="180" />
            <el-table-column label="发起部门" prop="departmentName" width="150" />

            <el-table-column fixed="right" label="操作" width="160">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  link
                  @click="handleWork(scope.row)"
                >
                  <i-ep-edit />处理
                </el-button>

                <el-button
                  type="danger"
                  size="small"
                  link
                  v-show="scope.row.creator === currentUserId"
                  @click="handleDisable(scope.row)"
                >
                  <i-ep-edit />作废
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-if="backlogTotal > 0"
            v-model:total="backlogTotal"
            v-model:page="backlogQueryParams.page"
            v-model:limit="backlogQueryParams.size"
            @pagination="handleBacklogQuery"
          />
        </el-card>
      </el-col>
      <el-col :xs="24" :span="8">
        <el-card style="height: 300px;">
          <template #header>
            <div class="flex-x-between">
              <div class="flex-y-center">
                通知公告<el-icon class="ml-1"><Notification /></el-icon>
              </div>
              <el-link type="primary" @click="handleMoreNotice">
                <span class="text-xs">查看更多</span
                ><el-icon class="text-xs"><ArrowRight /></el-icon
              ></el-link>
            </div>
          </template>

          <el-scrollbar>
            <div
              v-for="(item, index) in notices"
              :key="index"
              class="flex-y-center py-3"
            >
              <el-tag :type="getNoticeLevelTag(item.noticeLevel)" size="small">
                {{ getNoticeLevel(item.noticeLevel) }}
              </el-tag>
              <el-tag :type="getNoticeSecurityTag(item.security)" size="small" style="margin-left:5px;">
                {{ getNoticeSecurity(item.security) }}
              </el-tag>
              <el-text
                truncated
                class="!mx-2 flex-1 !text-xs !text-[var(--el-text-color-secondary)]"
              >
                {{ item.title }}
              </el-text>
              <el-link @click="handleOpenDialog(item)">
                <el-icon class="text-sm"><View /></el-icon>
              </el-link>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" class="mt-5">
      <el-col :xs="24" :span="16">
        <el-card style="height: 380px;">
          <template #header>
            <div class="card-header">
              <span>流程发起</span>
            </div>
          </template>

          <el-table
            v-loading="loading"
            :data="flowData"
            highlight-current-row
            style="width: 100%"
          >
            <el-table-column label="流程名称" prop="flowName" min-width="200" />
            <el-table-column label="流程类别" prop="flowSort" width="150">
              <template #default="scope">
                <el-tag v-if="scope.row.flowSort === 1" type="success"
                  >考勤类</el-tag
                >
                <el-tag v-if="scope.row.flowSort === 2" type="success"
                  >行政类</el-tag
                >
                <el-tag v-if="scope.row.flowSort === 3" type="success"
                  >业务类</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime" width="200" />
    
            <el-table-column fixed="right" label="操作" width="160">
              <template #default="scope">
                <el-button
                  type="primary"
                  size="small"
                  link
                  @click="handleStart(scope.row)"
                >
                  <i-ep-edit />启动
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-if="flowTotal > 0"
            v-model:total="flowTotal"
            v-model:page="flowQueryParams.page"
            v-model:limit="flowQueryParams.size"
            @pagination="handleFlowQuery"
          />
        </el-card>
      </el-col>
      <el-col :xs="24" :span="8">
        <el-card style="height:380px;" class="calendar-card">
          <template #header>
            <div class="card-header">
              <span>日历</span>
            </div>
          </template>

          <el-calendar v-model="calendar" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 公告明细弹窗 -->
    <el-dialog
      v-model="noticeVisible"
      :title="公告明细"
      width="600px"
      @close="handleCloseDialog"
    >
      <el-form
        :model="noticeData"
        label-width="100px"
      >
      <el-form-item label="标题" prop="title">
        <el-input v-model="noticeData.title" placeholder="请输入标题" readonly />
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <el-input v-model="noticeData.content" :rows="5" type="textarea" placeholder="请输入内容" readonly />
      </el-form-item>

      <el-form-item label="紧急程度" prop="noticeLevel">
        <el-select v-model="noticeData.noticeLevel" placeholder="请选择紧急程度" disabled>
          <el-option label="普通" value="1" />
          <el-option label="加急" value="2" />
          <el-option label="紧急" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="保密等级" prop="security">
        <el-select v-model="noticeData.security" placeholder="请选择保密等级" disabled>
          <el-option label="公开" value="1" />
          <el-option label="内部公开" value="2" />
          <el-option label="机密" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="附件" prop="attachments">
        <div style="float:lef;padding:5px;" v-for="(attachment, index) in noticeFileList" :key="index">
          <Icon icon="vscode-icons:file-type-word" width="56" height="56" v-if="attachment.name.endsWith('docx')" />
          <Icon icon="vscode-icons:file-type-excel" width="56" height="56" v-else-if="attachment.name.endsWith('xls')" />
          <Icon icon="vscode-icons:file-type-excel" width="56" height="56" v-else-if="attachment.name.endsWith('xlsx')" />
          <Icon icon="vscode-icons:file-type-pdf2" width="56" height="56" v-else-if="attachment.name.endsWith('pdf')" />
          <Icon icon="bx:file" width="56" height="56" style="color: #9e6161" v-else />
          <span><a :href="attachment.url" target="_blank" style="text-decoration:underline;">{{ attachment.name }}</a></span>
        </div>
      </el-form-item>
    </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleCloseDialog">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Dashboard",
  inheritAttrs: false,
});

import { Icon } from '@iconify/vue';
import { useUserStore } from "@/store/modules/user";
import router from "@/router";
import WorkAPI, { WorkPageVO, WorkPageQuery } from "@/api/work";
import NoticeAPI, { NoticePageVO, NoticePageQuery } from "@/api/notice";
import DesflowAPI, { FlowPageVO, FlowPageQuery } from "@/api/desflow";
import UserAPI, { UserPageVO } from "@/api/user";

import StatsAPI, { VisitStatsVO } from "@/api/log";
const userStore = useUserStore();

const avatar = sessionStorage.getItem("avatar") + "?imageView2/1/w/80/h/80";

const date: Date = new Date();
const greetings = computed(() => {
  const hours = date.getHours();
  if (hours >= 6 && hours < 12) {
    return "上午好，" + sessionStorage.getItem("nickName") + "！";
  } else if (hours >= 12 && hours < 18) {
    return "下午好，" + sessionStorage.getItem("nickName") + "！";
  } else {
    return "晚上好，" + sessionStorage.getItem("nickName") + "！";
  } 
});

const currentDate = ref(date.toLocaleString());
setInterval(() =>{
  const current = new Date();
  currentDate.value = current.toLocaleString();
}, 1000)

// 右上角数量
const statisticData = ref([
  {
    value: 0,
    iconClass: "todo",
    title: "待办",
    key: "backlog",
    path: "/backlog/view",
  },
  {
    value: 0,
    iconClass: "select",
    title: "已办",
    key: "completed",
    path: "/completed/view",
  },
  {
    value: 0,
    iconClass: "user",
    title: "总用户",
    key: "user",
    path: "/user/addressList",
  },
]);

const handleDataClick = function(path){
  router.push({ path: path});
}

// 待办工作
const currentUserId = sessionStorage.getItem("userId");
const loading = ref(false);
const backlogTotal = ref(0);
const backlogQueryParams = reactive<WorkPageQuery>({
  page: 1,
  size: 3,
  status: 1,
});

const backlogData = ref<WorkPageVO[]>();

function handleBacklogQuery() {
  loading.value = true;
  WorkAPI.getProcessPage(backlogQueryParams)
    .then(({data}) => {
      backlogData.value = data.list;
      backlogTotal.value = data.total;
      statisticData.value[0].value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 待办处理 */
function handleWork(process) {
  router.push({ path: '/backlog/deal', query: { workId: process.workId, processId: process.processId, 
    flowId: process.flowId, nodeName: process.nodeName }});
}

/** 流程作废 */
function handleDisable(process) {
  WorkAPI.disableWork(process.workId)
    .then(({data}) => {
      ElMessage.success("作废成功");
      handleBacklogQuery();
    })
}

const completedQueryParams = reactive<WorkPageQuery>({
  page: 1,
  size: 3,
  status: 0,
});
function handleCompleteQuery() {
  loading.value = true;
  WorkAPI.getProcessPage(completedQueryParams)
    .then(({data}) => {
      statisticData.value[1].value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

// 通知公告
const notices = ref([]);
const noticeQueryParams = reactive<NoticePageQuery>({
  page: 1,
  size: 8,
});
function handleNoticeQuery() {
  loading.value = true;
  NoticeAPI.getPage(noticeQueryParams)
    .then(({data}) => {
      notices.value = data.list;
    })
    .finally(() => {
      loading.value = false;
    });
}

const getNoticeLevelTag = (type: number) => {
  switch (type) {
    case 1:
      return "success";
    case 2:
      return "warning";
    case 3:
      return "danger";
    default:
      return "success";
  }
};

const getNoticeLevel = (type: number) => {
  switch (type) {
    case 1:
      return "普通";
    case 2:
      return "加急";
    case 3:
      return "紧急";
    default:
      return "普通";
  }
};

const getNoticeSecurityTag = (type: number) => {
  switch (type) {
    case 1:
      return "success";
    case 2:
      return "warning";
    case 3:
      return "danger";
    default:
      return "success";
  }
};

const getNoticeSecurity = (type: number) => {
  switch (type) {
    case 1:
      return "公开";
    case 2:
      return "内部公开";
    case 3:
      return "机密";
    default:
      return "公开";
  }
};

const handleMoreNotice = function(){
  router.push({ path: '/notice/display'});
}

// 公告明细
const noticeVisible = ref(false);
const noticeData = reactive({
  noticeId: null,
  title: '',
  noticeLevel: null,
  security: null,
  content: '',
  attachments: '',
});

const noticeFileList = ref<UploadUserFile[]>([])

/** 打开公告弹窗 */
function handleOpenDialog(notice) {
  noticeVisible.value = true;
  noticeFileList.value = [];
  Object.assign(noticeData, notice);
  noticeData.noticeLevel = noticeData.noticeLevel + '';
  noticeData.security = noticeData.security + '';
  if (noticeData.attachments){
    let files = JSON.parse(noticeData.attachments);
    for(let i = 0; i < files.length; i++){
      noticeFileList.value.push({name: files[i]["name"], url: files[i]["url"]});
    }
  }
}

/** 关闭公告弹窗 */
function handleCloseDialog() {
  noticeVisible.value = false;

  noticeData.noticeId = null;
  noticeData.title = '';
  noticeData.noticeLevel = null;
  noticeData.security = null;
  noticeData.content = '';
  noticeData.attachments = '';
}

// 流程发起
const flowTotal = ref(0);
const flowQueryParams = reactive<FlowPageQuery>({
  page: 1,
  size: 5,
});

const flowData = ref<FlowPageVO[]>();

function handleFlowQuery() {
  loading.value = true;
  DesflowAPI.getPage(flowQueryParams)
    .then(({data}) => {
      flowData.value = data.list;
      flowTotal.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}

/** 启动流程 */
function handleStart(flow) {
  router.push({ path: '/startflow/start', query: { flowId: flow.flowId, flowName: flow.flowName }});
}

// 日历
const calendar = ref(new Date());

onMounted(() => {
  handleBacklogQuery();
  handleCompleteQuery();
  handleNoticeQuery();
  handleFlowQuery();

  UserAPI.getPage({page:1, size:10})
    .then(({data}) => {
      statisticData.value[2].value = data.total;
    })
});
</script>

<style>
.dashboard-container {
  position: relative;
  padding: 15px;
}
.el-statistic {
  text-align: center;
}
.calendar-card .el-calendar-table .el-calendar-day{
  width: 100%;
  height: 30px;
  text-align: center;
}
</style>
