<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :lg="24" :xs="24" class="mb-[12px]">
        <el-card style="min-width: 480px" v-for="item in formList">
          <v-form-render :form-json="item.formJson" :form-data="item.formData" :option-data="optionData" ref="vHistoryFormRef">
          </v-form-render>
        </el-card>

        <el-card style="min-width: 480px">
          <template #header>
            <div class="card-header">
              <span>{{ route.query.nodeName }}</span>
            </div>
          </template>

          <v-form-render :form-json="formJson" :form-data="formData" :option-data="optionData" ref="vFormRef">
          </v-form-render>

          <template #footer>
            <div class="dialog-footer">
              <el-button type="primary" @click="submitForm">提 交</el-button>
              <el-button type="primary" @click="handleSave">暂 存</el-button>
              <el-button type="primary" @click="handleTransfer">转 办</el-button>
              <el-button type="warning" @click="handleReject" v-show="rejected">驳 回</el-button>
            </div>
          </template>
        </el-card>

        <!-- 选择人员弹窗 -->
        <el-dialog
          v-model="userDialogVisible"
          :title="选择人员"
          width="500"
          align-center
        >
          <el-select v-model="transferUser" placeholder="请选择转办人员" filterable>
            <el-option
              v-for="item in userList"
              :key="item.userId"
              :value="item.userId"
              :label="item.nickName"/>
          </el-select>

          <template #footer>
            <div class="dialog-footer">
              <el-button @click="userDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="handleSelectUser">
                确 定
              </el-button>
            </div>
          </template>
        </el-dialog>

        <flowHistory :flowId="route.query.flowId" :workId="route.query.workId" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Deal",
  inheritAttrs: false,
});

import WorkAPI from "@/api/work";
import { FormData } from "@/api/desform";
import UserAPI from "@/api/user";
import router from "@/router";
import { useTagsViewStore } from "@/store";

const route = useRoute();
const tagsViewStore = useTagsViewStore();

const formList = ref([]);
const vHistoryFormRef = ref(null);

const formJson = reactive({"widgetList":[],"formConfig":{"modelName":"formData","refName":"vForm","rulesName":"rules","labelWidth":80,"labelPosition":"left","size":"","labelAlign":"label-left-align","cssCode":"","customClass":"","functions":"","layoutType":"PC","jsonVersion":3,"onFormCreated":"","onFormMounted":"","onFormDataChange":""}})
const formData = reactive({})
const optionData = reactive({})
const vFormRef = ref(null)
const flowData = reactive({
  flowId: 0,
  processId: 0,
  tableName: '',
  isSave: 0,
  direction: 1,
})
const rejected = ref(true)

const loading = ref(false);
// 提交
const submitForm = () => {
  vFormRef.value.getFormData().then(formData => {
    // Form Validation OK
    formData.flowId = flowData.flowId;
    formData.processId = flowData.processId;
    formData.tableName = flowData.tableName;
    formData.isSave = 0;
    formData.direction = 1;
    loading.value = true;
    WorkAPI.dealWork(formData)
      .then(({data}) => {
        ElMessage.success(data);
        // 跳转到待办工作
        tagsViewStore.closeCurrentView();
        router.push({ path: '/backlog/view'});
      })
      .finally(() => (loading.value = false));
  }).catch(error => {
    // Form Validation failed
    ElMessage.error(error)
  })
}

// 暂存
const handleSave = () => {
  vFormRef.value.getFormData().then(formData => {
    // Form Validation OK
    formData.flowId = flowData.flowId;
    formData.processId = flowData.processId;
    formData.tableName = flowData.tableName;
    formData.isSave = 1;
    formData.direction = 1;
    loading.value = true;
    WorkAPI.dealWork(formData)
      .then(({data}) => {
        ElMessage.success("暂存成功");
        // 生成并返回流程进程ID
        flowData.processId = data;
      })
      .finally(() => (loading.value = false));
  }).catch(error => {
    // Form Validation failed
    ElMessage.error(error)
  })
}

const userDialogVisible = ref(false);
const userList = ref([]);
const transferUser = ref(null);

// 转办
const handleTransfer = () => {
  userDialogVisible.value = true;
}

// 转办
const handleSelectUser = () => {
  if (!transferUser.value){
    ElMessage.warning("请选择转办人员");
    return;
  }
  loading.value = true;
  WorkAPI.workTransfer(flowData.processId, transferUser.value)
      .then(({data}) => {
        ElMessage.success("转办成功");
        // 跳转到待办工作
        tagsViewStore.closeCurrentView();
        router.push({ path: '/backlog/view'});
      })
      .finally(() => (loading.value = false));
}

// 驳回
const handleReject = () => {
  vFormRef.value.getFormData().then(formData => {
    // Form Validation OK
    formData.flowId = flowData.flowId;
    formData.processId = flowData.processId;
    formData.tableName = flowData.tableName;
    formData.isSave = 0;
    formData.direction = 0;
    loading.value = true;
    WorkAPI.dealWork(formData)
      .then(({data}) => {
        ElMessage.success(data);
        // 跳转到待办工作
        tagsViewStore.closeCurrentView();
        router.push({ path: '/backlog/view'});
      })
      .finally(() => (loading.value = false));
  }).catch(error => {
    // Form Validation failed
    ElMessage.error(error)
  })
}

function isNumber(value: string): boolean {
    return !isNaN(parseFloat(value)) && !isNaN(Number(value));
}

onMounted(() => {
  // 获取用户列表
  UserAPI.getUserList()
    .then(({data}) => {
      userList.value = data;
    })

  WorkAPI.getFlowProcessForm(route.query.processId).then(({data}) => {
    if (data && data.form && data.form.jsonData){
      let json = JSON.parse(data.form.jsonData);
      flowData.flowId = route.query.flowId;
      flowData.tableName = data.form.tableName;
      flowData.processId = route.query.processId;

      formJson.widgetList = json.widgetList;
      formJson.formConfig = json.formConfig;

      // 数据格式处理
      let historyData = data.formData;
      for(let key in historyData){
        if (typeof historyData[key] === 'string' && historyData[key].includes('[') && historyData[key].includes(']')){
          let str = historyData[key].substring(1, historyData[key].length - 1);
          historyData[key] = str.split(',');
        }
        else if (isNumber(historyData[key])){
          historyData[key] = Number(historyData[key])
        }
        formData[key] = historyData[key];
      }
    }
  }).finally(() => {
    for(let key in formData){
      vFormRef.value.setFieldValue(key, formData[key]);
    }
  });

  WorkAPI.getWorkFlowProcessList(route.query.workId)
      .then(({data}) => {
        data.forEach((item) => {
          let historyFormJson = JSON.parse(item.form.jsonData);
          historyFormJson.widgetList.push({ "type":"input","icon":"text-field","formItemFlag":true,"options":
          {"name":"inputSubmitter","label":"提交人","labelAlign":"","type":"text","defaultValue":item.form.submitterName,"columnWidth":"200px" }});
          historyFormJson.widgetList.push({ "type":"input","icon":"text-field","formItemFlag":true,"options":
          {"name":"inputSubmitTime","label":"提交时间","labelAlign":"","type":"text","defaultValue":item.form.submitTime,"columnWidth":"200px" }});
          let historyFormData = item.formData;
          // 数据格式处理
          for(let key in historyFormData){
            if (typeof historyFormData[key] === 'string' && historyFormData[key].includes('[') && historyFormData[key].includes(']')){
              let str = historyFormData[key].substring(1, historyFormData[key].length - 1);
              historyFormData[key] = str.split(',');
              for(let i = 0; i < historyFormData[key].length; i++){
                historyFormData[key][i] = historyFormData[key][i].trim();
                if (isNumber(historyFormData[key][i])){
                  historyFormData[key][i] = Number(historyFormData[key][i]);
                }
              }
            }
            else if (isNumber(historyFormData[key])){
              historyFormData[key] = Number(historyFormData[key])
            }
            else if (historyFormData[key] === "true"){
              historyFormData[key] = true;
            }
            else if (historyFormData[key] === "false"){
              historyFormData[key] = false;
            }
          }
          formList.value.push({formJson: historyFormJson, formData: historyFormData});
        });
      })
      .finally(() => {
        loading.value = false
        // 禁用历史表单
        if (vHistoryFormRef.value != null){
          vHistoryFormRef.value.forEach((form) => {
            form.disableForm();
          })  
        }
      });

    WorkAPI.getCreator(route.query.workId)
      .then(({data}) => {
        let currentUserId = sessionStorage.getItem("userId");
        if (currentUserId == data){
          rejected.value = false;
        }
        else{
          rejected.value = true;
        }
      })
      .finally(() => {
        loading.value = false
      });
});
</script>
