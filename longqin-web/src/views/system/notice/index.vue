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
      <template #header>
        <el-button type="success" @click="handleOpenDialog()"
          ><i-ep-plus />新增</el-button
        >
      </template>

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

        <el-table-column fixed="right" label="操作" width="150">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleOpenDialog(scope.row)"
            >
              <i-ep-edit />编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              link
              @click="handleDelete(scope.row.noticeId)"
            >
              <i-ep-delete />删除
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

    <!-- 公告表单弹窗 -->
    <el-dialog
      v-model="dialog.visible"
      :title="dialog.title"
      width="500px"
      @close="handleCloseDialog"
    >
      <el-form
        ref="noticeFormRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题" />
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" :rows="5" type="textarea" placeholder="请输入内容" />
        </el-form-item>

        <el-form-item label="紧急程度" prop="noticeLevel">
          <el-select v-model="formData.noticeLevel" placeholder="请选择紧急程度">
            <el-option label="普通" value="1" />
            <el-option label="加急" value="2" />
            <el-option label="紧急" value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="保密等级" prop="security">
          <el-select v-model="formData.security" placeholder="请选择保密等级">
            <el-option label="公开" value="1" />
            <el-option label="内部公开" value="2" />
            <el-option label="机密" value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="附件" prop="attachments">
          <el-upload
            v-model:file-list="fileList"
            class="upload-demo"
            action="/api/upload/uploadFiles"
            multiple
            :on-remove="handleRemove"
            :limit="3"
            :on-success="handleFileSuccess"
            :before-upload="beforeFileUpload"
          >
            <el-button type="primary">上传附件</el-button>

          </el-upload>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
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

import NoticeAPI, { NoticePageVO, NoticeForm, NoticePageQuery } from "@/api/notice";
import type { UploadProps, UploadUserFile } from 'element-plus';

const queryFormRef = ref(ElForm);
const noticeFormRef = ref(ElForm);

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
const formData = reactive<NoticeForm>({
  noticeId: null,
  title: '',
  noticeLevel: null,
  security: null,
  content: '',
  attachments: '',
});
const rules = reactive({
  title: [{ required: true, message: "请输入标题", trigger: "blur" },{ min: 1, max: 25, message: '输入长度在 1 到 25 个字符', trigger: 'blur' }],
  content: [{ required: true, message: "请输入公告内容", trigger: "blur" }],
  noticeLevel: [{ required: true, message: "请选择紧急程度", trigger: "blur" }],
  security: [{ required: true, message: "请选择保密等级", trigger: "blur" }],
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

const handleFileSuccess: UploadProps['onSuccess'] = (
  response,
  uploadFile
) => {
  formData.attachments = response.data
}

const beforeFileUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 50) {
    ElMessage.error('文件大小不能超过50MB!')
    return false
  }
  return true
}

const handleRemove: UploadProps['onRemove'] = (file, uploadFiles) => {
  // console.log(file, uploadFiles)
}

/** 打开公告弹窗 */
function handleOpenDialog(notice) {
  dialog.visible = true;
  fileList.value = [];
  if (notice && notice.noticeId) {
    dialog.title = "修改公告";
    Object.assign(formData, notice);
    formData.noticeLevel = formData.noticeLevel + '';
    formData.security = formData.security + '';
    if (formData.attachments){
      let files = JSON.parse(formData.attachments);
      for(let i = 0; i < files.length; i++){
        fileList.value.push({name: files[i]["name"], url: files[i]["url"]});
      }
    }
  } else {
    dialog.title = "新增公告";
  }
}

/** 提交公告表单 */
function handleSubmit() {
  noticeFormRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      // 处理附件
      let files = [];
      for(let i = 0; i < fileList.value.length; i++){
        if (fileList.value[i].response){
          files.push(fileList.value[i].response.data[0])
        }
        else{
          let fileJson = {};
          fileJson.name = fileList.value[i].name;
          fileJson.url = fileList.value[i].url;
          files.push(fileJson);
        }
      }
      formData.attachments = JSON.stringify(files);
      const noticeId = formData.noticeId;
      if (noticeId) {
        NoticeAPI.update(formData)
          .then(() => {
            ElMessage.success("修改成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      } else {
        NoticeAPI.add(formData)
          .then(() => {
            ElMessage.success("新增成功");
            handleCloseDialog();
            handleResetQuery();
          })
          .finally(() => (loading.value = false));
      }
    }
  });
}

/** 关闭公告弹窗 */
function handleCloseDialog() {
  dialog.visible = false;

  noticeFormRef.value.resetFields();
  noticeFormRef.value.clearValidate();

  formData.noticeId = null;
  formData.title = '';
  formData.noticeLevel = null;
  formData.security = null;
  formData.content = '';
  formData.attachments = '';
  formData.creator = null;
  formData.createTime = null;
  formData.organizationId = null;
}

/** 删除公告 */
function handleDelete(noticeId?: number) {
  ElMessageBox.confirm("确认删除已选中的数据项?", "警告", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then(
    () => {
      loading.value = true;
      NoticeAPI.deleteById(noticeId)
        .then(() => {
          ElMessage.success("删除成功");
          handleResetQuery();
        })
        .finally(() => (loading.value = false));
    },
    () => {
      ElMessage.info("已取消删除");
    }
  );
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
  .avatar-uploader .avatar {
    width: 108px;
    height: 108px;
    display: block;
  }
  </style>
  
  <style>
  .avatar-uploader .el-upload {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }
  
  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }
  
  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
  }
  </style>