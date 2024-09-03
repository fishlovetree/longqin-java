<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :lg="24" :xs="24" class="mb-[12px]">
        <el-card style="min-width: 480px">
          <template #header>
            <div class="card-header">
              <span>{{ route.query.flowName }}</span>
            </div>
          </template>

          <v-form-render :form-json="formJson" :form-data="formData" :option-data="optionData" ref="vFormRef">
          </v-form-render>

          <template #footer>
            <div class="dialog-footer">
              <el-button type="primary" @click="submitForm">提 交</el-button>
              <el-button type="primary" @click="handleSave">暂 存</el-button>
            </div>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Start",
  inheritAttrs: false,
});

import WorkAPI from "@/api/work";
import { FormData } from "@/api/desform";
import router from "@/router";
import { useTagsViewStore, useSettingsStore } from "@/store";

const route = useRoute();
const tagsViewStore = useTagsViewStore();
const settingsStore = useSettingsStore();

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

const loading = ref(false);
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
        // 跳转到流程发起
        if (settingsStore.tagsView){
          tagsViewStore.closeCurrentView();
        }
        router.push({ path: '/startflow/view'});
      })
      .finally(() => (loading.value = false));
  }).catch(error => {
    // Form Validation failed
    ElMessage.error(error)
  })
}

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

onMounted(() => {
  WorkAPI.getFlowBeginNodeForm(route.query.flowId).then(({data}) => {
    if (data && data.jsonData){
      let json = JSON.parse(data.jsonData);
      flowData.flowId = route.query.flowId;
      flowData.tableName = data.tableName;
      formJson.widgetList = json.widgetList;
      formJson.formConfig = json.formConfig;
    }
  })
});
</script>
