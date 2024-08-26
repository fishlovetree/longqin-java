<!-- 部门树 -->
<template>
  <el-card shadow="never">
    <el-input v-model="departmentName" placeholder="部门名称" clearable>
      <template #prefix>
        <i-ep-search />
      </template>
    </el-input>

    <el-tree
      ref="departmentTreeRef"
      class="mt-2"
      :data="departmentList"
      node-key="id"
      :props="{ children: 'children', label: 'departmentName', disabled: '' }"
      :expand-on-click-node="false"
      :filter-node-method="handleFilter"
      default-expand-all
      @node-click="handleNodeClick"
    />
  </el-card>
</template>

<script setup lang="ts">
import DeptAPI from "@/api/dept";
const props = defineProps({
  modelValue: {
    type: [Number],
    default: undefined,
  },
});

const departmentList = ref<OptionType[]>(); // 部门列表
const departmentTreeRef = ref(ElTree); // 部门树
const departmentName = ref(); // 部门名称

const emits = defineEmits(["node-click"]);

const departmentId = useVModel(props, "modelValue", emits);

watchEffect(
  () => {
    departmentTreeRef.value.filter(departmentName.value);
  },
  {
    flush: "post", // watchEffect会在DOM挂载或者更新之前就会触发，此属性控制在DOM元素更新后运行
  }
);

/** 部门筛选 */
function handleFilter(value: string, data: any) {
  if (!value) {
    return true;
  }
  return data.departmentName.indexOf(value) !== -1;
}

/** 部门树节点 Click */
function handleNodeClick(data: { [key: string]: any }) {
  departmentId.value = data.departmentId;
  emits("node-click");
}

onBeforeMount(() => {
  DeptAPI.getTree().then(({data}) => {
    departmentList.value = data;
  });
});
</script>
