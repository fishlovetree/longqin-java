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
    toolbarMaxWidth: 440,
    importJsonButton: true,
    exportJsonButton: true,
    exportCodeButton: false,
    generateSFCButton: true,
  });

  const formData = reactive<FormData>({
    id: undefined,
    jsonData: '',
    tableName: '',
    formName: '',
  });

  const saveFormJson = () => {
    let formJson = vfDesigner.value.getFormJson();

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