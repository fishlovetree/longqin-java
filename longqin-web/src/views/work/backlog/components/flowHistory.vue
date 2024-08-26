<template>
  <el-tabs type="border-card">
    <el-tab-pane label="流程记录">
      <el-table
        v-loading="loading"
        :data="stepData"
        highlight-current-row
        border
      >
        <el-table-column label="任务" prop="nodeName" width="150" />
        <el-table-column label="处理人" prop="submitterName" width="120" />
        <el-table-column label="处理时间" prop="submitTime" min-width="180" />
        <el-table-column label="处理方式" prop="action" width="150">
          <template #default="scope">
            <el-tag v-if="scope.row.action === 1" type="success"
              >通过</el-tag
            >
            <el-tag v-if="scope.row.action === 0" type="warning"
              >驳回</el-tag
            >
            <el-tag v-if="scope.row.action === 3" type="success"
              >转办</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column label="停留时长" prop="stayTime" min-width="180" />
      </el-table>

      <pagination
        v-if="total > 0"
        v-model:total="total"
        v-model:page="queryParams.page"
        v-model:limit="queryParams.size"
        @pagination="handleQuery"
      />
    </el-tab-pane>
    <el-tab-pane label="流程图">
      <div id="wrapper" style="width:100%;height: calc(100vh - 160px);overflow: auto;"></div>
    </el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">

import WorkAPI, { StepPageVO, StepPageQuery } from "@/api/work";
import Flow from "../../../flowDesign/flowDesigner/flow.js";
import DesflowAPI, { FlowData } from "@/api/desflow";

const props = defineProps({
  /**
   * 父层级流程id
   */
  flowId: {
    type: Number,
    required: true,
  },
  /**
   * 父层级工作实例id
   */
   workId: {
    type: Number,
    required: true,
  },
});

const loading = ref(false);
const total = ref(0);

const queryParams = reactive<StepPageQuery>({
  page: 1,
  size: 50,
  workId: null,
});

// 表格数据
const stepData = ref<StepPageVO[]>();
const stepNodeIds = ref([]);

// 流程图相关
const showNodeForm = ref(false);
const nodeForm = reactive({
  id: '',
  name: '',
  isApproval: '0',
  formId: '',
  showFormId: true,
  cooperation: '0',
  departmentId: '0',
  positionId: '0',
  userId: '0',
})
const showLineForm = ref(false);
const lineForm = reactive({
  id: '',
  name: '',
  operator: '',
  formId: '',
  field: '',
  operatorValue: '',
})
let pager;
let mflow;

/** 查询 */
function handleQuery() {
  loading.value = true;
  queryParams.workId = props.workId;
  WorkAPI.getWorkSteps(queryParams)
    .then(({data}) => {
      stepData.value = data.list;
      total.value = data.total;
      for(let i = 0; i < data.list.length; i++){
        stepNodeIds.value.push(data.list[i].nodeId.toString());
      }

      // 初始化流程图
      var container = document.getElementById("wrapper");
      pager = new Raphael(document.getElementById("wrapper"), 2000, 1000);
      mflow = Flow.createNew(pager, showNodeForm, nodeForm, showLineForm, lineForm);
      DesflowAPI.getFlowJson(props.flowId).then(({data}) => {
        var r = { data: eval(data) };
        mflow.init(r, stepNodeIds.value);
      })
    })
    .finally(() => {
      loading.value = false;
    });
}

onMounted(() => {
  handleQuery();
});
</script>
