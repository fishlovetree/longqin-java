<template>
  <div class="app-container">
    <div class="search-container">
      <el-form ref="queryFormRef" :model="queryParams" :inline="true">
        <el-form-item prop="title" label="标题">
          <el-input
            v-model="queryParams.title"
            placeholder="标题"
            clearable
          />
        </el-form-item>
        <el-form-item prop="rangeTime" label="发文时间">
          <el-date-picker
            v-model="rangeTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleChange"
            value-format="YYYY-MM-DD"
          >
          </el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleQuery"
            ><i-ep-search />搜索</el-button
          >
          <el-button @click="handleResetQuery"><i-ep-refresh />重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-card shadow="never" class="table-container">

      <el-table
        ref="dataTableRef"
        v-loading="loading"
        :data="noticeList"
        highlight-current-row
        border
      >
        <el-table-column label="标题" prop="title" min-width="180" />
        <el-table-column label="内容" prop="content" width="200" show-overflow-tooltip />
        <el-table-column label="紧急程度" align="center" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.noticeLevel === 1" type="success"
              >普通</el-tag
            >
            <el-tag v-if="scope.row.noticeLevel === 2" type="warning"
              >加急</el-tag
            >
            <el-tag v-if="scope.row.noticeLevel === 3" type="danger"
              >紧急</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="保密等级" align="center" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.security === 1" type="success"
              >公开</el-tag
            >
            <el-tag v-if="scope.row.security === 2" type="warning"
              >内部公开</el-tag
            >
            <el-tag v-if="scope.row.security === 3" type="danger"
              >机密</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="发文人" prop="creatorName" width="100" />
        <el-table-column label="发文时间" prop="createTime" width="180" />
        <el-table-column label="发文部门" prop="departmentName" width="100" />
        <el-table-column label="附件" prop="attachments" width="250">
          <template #default="{ row }">
            <span v-for="(attachment, index) in handleAttachments(row.attachments)" :key="index">
              <p><a :href="attachment.url" target="_blank" style="text-decoration:underline;">{{ attachment.name }}</a></p>
            </span>
          </template>
        </el-table-column>

        <el-table-column fixed="right" label="操作" width="80">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleOpenDialog(scope.row)"
            >
              <!-- <i-ep-detail />明细 -->
              <Icon icon="bx:detail"/>明细
            </el-button>
          </template>
        </el-table-column>

      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.page"
        v-model:limit="queryParams.size"
        @pagination="handleQuery"
      />
    </el-card>

    <!-- 公告明细弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="600px"
      @close="handleCloseDialog"
    >
      <el-form
        :model="formData"
        label-width="100px"
      >
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入标题" readonly />
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <el-input v-model="formData.content" :rows="5" type="textarea" placeholder="请输入内容" readonly />
      </el-form-item>

      <el-form-item label="紧急程度" prop="noticeLevel">
        <el-select v-model="formData.noticeLevel" placeholder="请选择紧急程度" disabled>
          <el-option label="普通" value="1" />
          <el-option label="加急" value="2" />
          <el-option label="紧急" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="保密等级" prop="security">
        <el-select v-model="formData.security" placeholder="请选择保密等级" disabled>
          <el-option label="公开" value="1" />
          <el-option label="内部公开" value="2" />
          <el-option label="机密" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="附件" prop="attachments">
        <div style="float:lef;padding:5px;" v-for="(attachment, index) in fileList" :key="index">
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
  name: "Notice",
  inheritAttrs: false,
});

import NoticeAPI, { NoticePageVO,  NoticePageQuery } from "@/api/notice";
import { Icon } from '@iconify/vue';

const queryFormRef = ref(ElForm);

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<NoticePageQuery>({
  page: 1,
  size: 10,
  title: "",
  beginDate: "",
  endDate: "",
});
const rangeTime = ref("");
function handleChange(){
  queryParams.beginDate = rangeTime.value[0];
  queryParams.endDate = rangeTime.value[1];
}

// 公告表格数据
const noticeList = ref<NoticePageVO[]>();

// 弹窗
const dialog = reactive({
  title: "",
  visible: false,
});
// 公告表单
const formData = reactive({
  noticeId: null,
  title: '',
  noticeLevel: null,
  security: null,
  content: '',
  attachments: '',
});

/** 查询 */
function handleQuery() {
  loading.value = true;
  NoticeAPI.getPage(queryParams)
    .then(({data}) => {
      noticeList.value = data.list;
      total.value = data.total;
    })
    .finally(() => {
      loading.value = false;
    });
}
/** 重置查询 */
function handleResetQuery() {
  queryFormRef.value.resetFields();
  queryParams.page = 1;
  queryParams.title = "";
  rangeTime.value = "";
  queryParams.beginDate = "";
  queryParams.endDate = "";
  handleQuery();
}
const fileList = ref<UploadUserFile[]>([])

/** 打开公告弹窗 */
function handleOpenDialog(notice) {
  dialog.visible = true;
  fileList.value = [];
  dialog.title = "公告明细";
  Object.assign(formData, notice);
  formData.noticeLevel = formData.noticeLevel + '';
  formData.security = formData.security + '';
  if (formData.attachments){
    let files = JSON.parse(formData.attachments);
    for(let i = 0; i < files.length; i++){
      fileList.value.push({name: files[i]["name"], url: files[i]["url"]});
    }
  }
}

/** 关闭公告弹窗 */
function handleCloseDialog() {
  dialog.visible = false;

  formData.noticeId = null;
  formData.title = '';
  formData.noticeLevel = null;
  formData.security = null;
  formData.content = '';
  formData.attachments = '';
}

function handleAttachments(attachments){
  if (attachments){
    return JSON.parse(attachments);
  }
  else{
    return [];
  }
}

onMounted(() => {
  handleQuery();
});
</script>

<style scoped>

</style>