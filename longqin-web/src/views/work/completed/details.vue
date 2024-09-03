<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :lg="24" :xs="24" class="mb-[12px]">
        <el-card style="min-width: 480px" v-for="item in formList">
          <v-form-render :form-json="item.formJson" :form-data="item.formData" ref="vHistoryFormRef">
          </v-form-render>
        </el-card>

        <flowHistory :flowId="route.query.flowId" :workId="route.query.workId" />
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
defineOptions({
  name: "Details",
  inheritAttrs: false,
});

import WorkAPI from "@/api/work";
import router from "@/router";
import flowHistory from "../backlog/components/flowHistory.vue";
import { transferFormData } from "@/utils/index";

const route = useRoute();

const formList = ref([]);
const vHistoryFormRef = ref(null);

function isNumber(value: string): boolean {
    return !isNaN(parseFloat(value)) && !isNaN(Number(value));
}

onMounted(() => {

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
          transferFormData(historyFormData, historyFormJson.widgetList);
          formList.value.push({formJson: historyFormJson, formData: historyFormData});
        });
      })
      .finally(() => {
        // 禁用历史表单
        if (vHistoryFormRef.value != null){
          vHistoryFormRef.value.forEach((form) => {
            form.disableForm();
          })  
        }
      });
});
</script>
