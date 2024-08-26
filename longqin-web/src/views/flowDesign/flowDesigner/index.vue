<template>
  <el-container style="min-height: calc(100vh - 50px);padding: 10px;">
    <el-container>
      <el-header>
        <el-button type="primary" @click="addNode" style="float:left;">新建节点</el-button>
        <el-button type="primary" @click="addPath" style="float:left;padding-left:5px;">新建连线</el-button>
        <el-input v-model="flowName" placeholder="流程名称" style="float:left; width:200px;padding-left:5px;" />
        <el-select v-model="flowSort" placeholder="请选择流程类别"  style="float:left; width:200px;padding-left: 5px;">
          <el-option label="考勤类" value="1" />
          <el-option label="行政类" value="2" />
          <el-option label="业务类" value="3" />
        </el-select>
        <el-button type="primary" @click="saveFlow" style="float:left;padding-left:5px;">保存</el-button>
      </el-header>
      <el-main>
        <div id="wrapper" style="min-height: calc(100vh - 160px);"></div>
      </el-main>
    </el-container>
    <el-aside width="320px">
      <el-card v-show="showNodeForm">
        <template #header>
          <div class="card-header">
            <span>节点属性</span>
          </div>
        </template>
        <el-form :model="nodeForm" label-width="100" style="max-width:300px;">
          <el-form-item label="节点名称">
            <el-input v-model="nodeForm.name" @input="handleNodeName" />
          </el-form-item>
          <el-form-item label="节点类型">
            <el-select v-model="nodeForm.rectType" placeholder="请选择节点类型" @change="handleRectType">
              <el-option label="普通节点" value="0" />
              <el-option label="分流节点" value="1" />
              <el-option label="合流节点" value="2" />
              <el-option label="分合流点" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="是否审批">
            <el-radio-group v-model="nodeForm.isApproval" @change="handleIsApproval">
              <el-radio value="1">是</el-radio>
              <el-radio value="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="节点表单">
            <el-select v-model="nodeForm.formId" placeholder="请选择节点表单" @change="handleFormId">
              <el-option
              v-for="item in formList"
              :key="item.id"
              :value="item.id"
              :label="item.formName"/>
            </el-select>
          </el-form-item>
          <el-form-item label="多人协作">
            <el-radio-group v-model="nodeForm.cooperation" @change="handleCooperation">
              <el-radio value="1">是</el-radio>
              <el-radio value="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="处理部门">
            <el-tree-select
              v-model="nodeForm.departmentId"
              :data="departmentTree"
              check-strictly
              :render-after-expand="false"
              node-key="id"
              :props="departmentTreeProps"
              style="width: 240px"
              @change="handleDepartment"
            />
          </el-form-item>
          <el-form-item label="处理职位">
            <el-tree-select
              v-model="nodeForm.positionId"
              :data="positionTree"
              check-strictly
              :render-after-expand="false"
              node-key="id"
              :props="positionTreeProps"
              style="width: 240px"
              @change="handlePosition"
            />
          </el-form-item>
          <el-form-item label="处理人">
            <el-select v-model="nodeForm.userId" placeholder="请选择处理人" @change="handleUser" filterable>
              <el-option
              v-for="item in userList"
              :key="item.userId"
              :value="item.userId"
              :label="item.nickName"/>
            </el-select>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card v-show="showLineForm">
        <template #header>
          <div class="card-header">
            <span>连线属性</span>
          </div>
        </template>
        <el-form :model="lineForm" label-width="100" style="max-width:300px;">
          <el-form-item label="连线名称">
            <el-input v-model="lineForm.name" @input="handleLineName" />
          </el-form-item>
          <el-form-item label="条件符号">
            <el-select v-model="lineForm.operator" placeholder="请选择条件符号" @change="handleOperator">
              <el-option label="=" value="=" />
              <el-option label="!=" value="!=" />
              <el-option label=">" value=">" />
              <el-option label="<" value="<" />
              <el-option label=">=" value=">=" />
              <el-option label="<=" value="<=" />
            </el-select>
          </el-form-item>
          <el-form-item label="条件表单">
            <el-select v-model="lineForm.formId" placeholder="用于筛选条件字段" @change="handleLineFormId">
              <el-option
              v-for="item in formList"
              :key="item.id"
              :value="item.id"
              :label="item.formName"/>
            </el-select>
          </el-form-item>
          <el-form-item label="条件字段">
            <el-select v-model="lineForm.field" placeholder="请选择条件字段" @change="handleLineField">
              <el-option
              v-for="item in fieldList"
              :key="item.name"
              :value="item.name"
              :label="item.label"/>
            </el-select>
          </el-form-item>
          <el-form-item label="条件值">
            <el-select v-if="lineForm.field==='positionLevel'" v-model="lineForm.operatorValue" @change="handleOperatorValue">
              <el-option label="基层" value="1" />
              <el-option label="中层" value="2" />
              <el-option label="高层" value="3" />
            </el-select>
            <el-select v-else-if="lineForm.field==='userId'" v-model="lineForm.operatorValue" @change="handleOperatorValue" filterable>
              <el-option
              v-for="item in userList"
              :key="item.userId"
              :value="item.userId"
              :label="item.nickName"/>
            </el-select>
            <el-input v-else v-model="lineForm.operatorValue" @input="handleOperatorValue" />
          </el-form-item>
        </el-form>
      </el-card>
    </el-aside>
  </el-container>
</template>

<script setup lang="ts">
  import Flow from "./flow.js";
  import DesformAPI, { FormPageVO, FormPageQuery } from "@/api/desform";
  import DesflowAPI, { FlowData } from "@/api/desflow";
  import DeptAPI, { DeptTree } from "@/api/dept";
  import PositionAPI, { positionTree } from "@/api/position";
  import UserAPI from "@/api/user";
  import { ElMessage } from 'element-plus';

  const route = useRoute();

  const flowId = ref(null);
  const flowName = ref('');
  const flowSort = ref('');
  const showNodeForm = ref(false);
  const nodeForm = reactive({
    id: '',
    name: '',
    isApproval: '0',
    formId: '',
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
  const formList = ref([]);
  const departmentTree = ref([]);
  const departmentTreeProps = reactive({
    label: 'title'
  })
  const positionTree = ref([]);
  const positionTreeProps = reactive({
    label: 'title'
  })
  const userList = ref([]);
  const fieldList = ref([]);
  const flowData = reactive<FlowData>({
    flowId: null,
    flowName: '',
    flowSort: '',
    nodes: '',
    links: '',
  });

  let pager;
  let mflow;
  function addNode(){
    mflow.util.addrect();
  }
  function addPath(){
    mflow.util.addpath();
  }
  function saveFlow(){
    if (flowName.value == ''){
      ElMessage({
        message: '请输入流程名称',
        type: 'warning',
      })
      return;
    }
    if (!mflow.util.check()){
      ElMessage({
        message: '请检查流程',
        type: 'warning',
      })
      return;
    }
    let ele = mflow.util.saveFlow();

    flowData.flowId = flowId.value;
    flowData.flowName = flowName.value;
    flowData.flowSort = flowSort.value;
    flowData.nodes = ele.nodes;
    flowData.links = ele.links;

    DesflowAPI.saveFlow(flowData).then((res) => {
      ElMessage.success('流程已保存！');
    });
  }
  onMounted(()=>{
    var container = document.getElementById("wrapper");
    pager = new Raphael(document.getElementById("wrapper"), 2000, 1000);
    mflow = Flow.createNew(pager, showNodeForm, nodeForm, showLineForm, lineForm);
    if (route.query.flowId){ // 编辑状态
      DesflowAPI.getById(route.query.flowId).then(({data}) => {
        flowId.value = route.query.flowId;
        flowName.value = data.flowName;
        flowSort.value = ''+data.flowSort;
      })
      DesflowAPI.getFlowJson(route.query.flowId).then(({data}) => {
        var r = { data: eval(data) };
        mflow.init(r);
      })
    }
    else{ // 新增状态
      mflow.init();
    }

    const queryParams = reactive<FormPageQuery>({
      page: 1,
      size: 100,
    });

    // 获取表单列表
    DesformAPI.getPage(queryParams)
    .then(({data}) => {
      formList.value = data.list;
    })

    // 获取部门树
    DeptAPI.getTree()
    .then(({data}) => {
      departmentTree.value = data;
    })

    // 获取职位树
    PositionAPI.getTree()
    .then(({data}) => {
      positionTree.value = data;
    })

    // 获取用户列表
    UserAPI.getUserList()
    .then(({data}) => {
      userList.value = data;
    })
  })

  //事件集
  function handleNodeName(value){
    mflow.util.handleNodeName(nodeForm.id, nodeForm.name)
  }
  function handleRectType(value){
    mflow.util.handleRectType(nodeForm.id, nodeForm.rectType)
  }
  function handleIsApproval(value){
    mflow.util.handleIsApproval(nodeForm.id, nodeForm.isApproval)
  }
  function handleFormId(value){
    mflow.util.handleFormId(nodeForm.id, nodeForm.formId)
  }
  function handleCooperation(value){
    mflow.util.handleCooperation(nodeForm.id, nodeForm.cooperation)
  }
  function handleDepartment(value){
    mflow.util.handleDepartment(nodeForm.id, nodeForm.departmentId)
  }
  function handlePosition(value){
    mflow.util.handlePosition(nodeForm.id, nodeForm.positionId)
  }
  function handleUser(value){
    mflow.util.handleUser(nodeForm.id, nodeForm.userId)
  }
  function handleLineName(value){
    mflow.util.handleLineName(lineForm.id, lineForm.name)
  }
  function handleOperator(value){
    mflow.util.handleOperator(lineForm.id, lineForm.operator)
  }
  function handleLineFormId(value){
    mflow.util.handleLineFormId(lineForm.id, lineForm.formId)
    fieldList.value = [];
    fieldList.value.push({ 'label': '提交人', 'name': 'userId' });
    fieldList.value.push({ 'label': '提交人职级', 'name': 'positionLevel' });
    // 获取表单字段列表
    DesformAPI.getById(lineForm.formId)
    .then(({data}) => {
      if (data['jsonData'] != undefined){
        let jsonData = JSON.parse(data['jsonData']);
        for(let i = 0; i < jsonData.widgetList.length; i++){
          let widget = jsonData.widgetList[i];
          fieldList.value.push({ 'label': widget.options.label, 'name': widget.options.name });
        }
      } 
    })
  }
  function handleLineField(value){
    mflow.util.handleLineField(lineForm.id, lineForm.field)
  }
  function handleOperatorValue(value){
    mflow.util.handleOperatorValue(lineForm.id, lineForm.operatorValue)
  }
  
</script>

<style type="text/css">

</style>