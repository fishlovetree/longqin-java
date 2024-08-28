<template>
  <v-form-designer ref="vfDesigner" :designer-config="designerConfig">
    <template #customToolButtons>
      <el-button type="text" @click="saveFormJson">保存</el-button>
    </template>
  </v-form-designer>
</template>

<script setup lang="ts">
  import DesformAPI, { FormData } from "@/api/desform";

  const route = useRoute();

  const vfDesigner = ref(null);
  const designerConfig = reactive({
    languageMenu: false,
    externalLink: false,
    toolbarMaxWidth: 300,
    importJsonButton: false,
    exportJsonButton: true,
    exportCodeButton: false,
    generateSFCButton: false,
  });

  const formData = reactive<FormData>({
    id: undefined,
    jsonData: '',
    tableName: '',
    formName: '',
  });

  const saveFormJson = () => {
    let formJson = vfDesigner.value.getFormJson();
    dealFileUpload(formJson.widgetList);

    formData.tableName = formJson.formConfig.modelName;
    formData.formName = formJson.formConfig.refName;
    formData.jsonData = JSON.stringify(formJson);

    if (formData.id){
      DesformAPI.updateForm(formData).then((res) => {
        ElMessage.success('表单已保存！');
      });
    }
    else{
      DesformAPI.createForm(formData).then((res) => {
        ElMessage.success('表单已保存！');
      });
    }
  }

  // 处理文件上传组件路径和文件格式问题
  const dealFileUpload = (widgetList:[]) => {
    for(let i = 0; i < widgetList.length; i++){
      let widget = widgetList[i];
      let type = widget.type;
      switch (type)
      {
        case "file-upload":
          widget.options.uploadURL = "/api/bus/upload/uploadFiles";
          if (!widget.options.fileTypes.includes("pdf")){
            widget.options.fileTypes.push("pdf");
          }
          if (!widget.options.fileTypes.includes("txt")){
            widget.options.fileTypes.push("txt");
          }
          
          // widget.options.onUploadSuccess = "return [{name: '图片或文件名称',url: '上传后的图片或文件URL'}]";
          break;
        case "grid":
          let cols = widget.cols;
          for (let j = 0 ; j < cols.length; j++){
            let col = cols[j];
            dealFileUpload(col.widgetList);
          }
          break;
        case "table":
          let rows = widget.rows;
          for (let j = 0 ; j < rows.length; j++){
            let row = rows[j];
            let rowCols = row.cols;
            for (let k = 0; k < rowCols.length; k++){
              let rowCol = rowCols[k];
              dealFileUpload(rowCol.widgetList);
            }
          }
          break;
        case "tab":
          let tabs = widget.tabs;
          for (let j = 0 ; j < tabs.length; j++){
            let tab = tabs[j];
            dealFileUpload(tab.widgetList);
          }
          break;
        case "card":
          dealFileUpload(widget.widgetList);
          break;
        case "input":
        case "textarea":
        case "rich-editor":
        case "number":
        case "radio":
        case "checkbox":
        case "select":
        case "time":
        case "time-range":
        case "date":
        case "date-range":
        case "switch":
        case "rate":
        case "color":
        case "slider":
        case "cascader":
            break;
        default:
            break;
      }
    }
  }

  onMounted(()=>{
    if (route.query.id){
      formData.id = route.query.id;
      DesformAPI.getById(route.query.id).then(({data}) => {
        if (data && data.jsonData){
          vfDesigner.value.setFormJson(data.jsonData);
        }
      })
    }
    else{
      vfDesigner.value.clearDesigner();
    }
  })
</script>

<style lang="scss">
body {
  margin: 0;  /* 如果页面出现垂直滚动条，则加入此行CSS以消除之 */
}

</style>