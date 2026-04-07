<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="专业名称" prop="majorName">
        <el-input v-model="queryParams.majorName" placeholder="请输入专业名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="专业类别" prop="majorCategory">
        <el-select v-model="queryParams.majorCategory" placeholder="请选择专业类别" clearable>
          <el-option v-for="dict in major_category" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="学历层次" prop="eduLevel">
        <el-select v-model="queryParams.eduLevel" placeholder="请选择学历层次" clearable>
          <el-option v-for="dict in edu_level" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['base:major:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['base:major:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:major:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['base:major:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="majorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="专业编码" align="center" prop="majorCode" />
      <el-table-column label="专业名称" prop="majorName" :show-overflow-tooltip="true" />
      <el-table-column label="专业简称" align="center" prop="majorShortName" :show-overflow-tooltip="true" />
      <el-table-column label="专业类别" align="center" prop="majorCategory">
        <template #default="{ row }">
          <dict-tag :options="major_category" :value="row.majorCategory ? [row.majorCategory] : []" />
        </template>
      </el-table-column>
      <el-table-column label="学制" align="center" prop="duration" />
      <el-table-column label="学历层次" align="center" prop="eduLevel">
        <template #default="{ row }">
          <dict-tag :options="edu_level" :value="row.eduLevel ? [row.eduLevel] : []" />
        </template>
      </el-table-column>
      <el-table-column label="关联院校" align="center" prop="collegeName" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="{ row }">
          <dict-tag :options="sys_normal_disable" :value="row.status ? [row.status] : []" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template #default="{ row }">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(row)" v-hasPermi="['base:major:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(row)" v-hasPermi="['base:major:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改专业信息对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="专业编码" prop="majorCode">
          <el-input v-model="form.majorCode" placeholder="请输入专业编码" />
        </el-form-item>
        <el-form-item label="专业名称" prop="majorName">
          <el-input v-model="form.majorName" placeholder="请输入专业名称" />
        </el-form-item>
        <el-form-item label="专业简称" prop="majorShortName">
          <el-input v-model="form.majorShortName" placeholder="请输入专业简称" />
        </el-form-item>
        <el-form-item label="专业类别" prop="majorCategory">
          <el-select v-model="form.majorCategory" placeholder="请选择专业类别">
            <el-option v-for="dict in major_category" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="学制" prop="duration">
          <el-input v-model="form.duration" placeholder="请输入学制" />
        </el-form-item>
        <el-form-item label="学历层次" prop="eduLevel">
          <el-select v-model="form.eduLevel" placeholder="请选择学历层次">
            <el-option v-for="dict in edu_level" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="关联院校" prop="collegeId">
          <el-select v-model="form.collegeId" placeholder="请选择关联院校" filterable clearable>
            <el-option v-for="college in collegeList" :key="college.collegeId" :label="college.collegeName" :value="college.collegeId" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="专业简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" placeholder="请输入专业简介" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts" name="BaseMajor">
  import { listMajor, getMajor, delMajor, addMajor, updateMajor } from '@/api/base/major';
  import { listCollege } from '@/api/base/college';
  import type { BaseMajor, BaseMajorQueryParams } from '@/types/api/base/major';
  import type { BaseCollege } from '@/types/api/base/college';

  const { proxy } = getCurrentInstance();
  const { major_category, edu_level, sys_normal_disable } = proxy.useDict('major_category', 'edu_level', 'sys_normal_disable');

  const majorList = ref<BaseMajor[]>([]);
  const collegeList = ref<BaseCollege[]>([]);
  const open = ref<boolean>(false);
  const loading = ref<boolean>(true);
  const showSearch = ref<boolean>(true);
  const ids = ref<number[]>([]);
  const single = ref<boolean>(true);
  const multiple = ref<boolean>(true);
  const total = ref<number>(0);
  const title = ref<string>('');

  const data = reactive({
    form: {} as BaseMajor,
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      majorName: undefined,
      majorCategory: undefined,
      eduLevel: undefined,
    } as BaseMajorQueryParams,
    rules: {
      majorCode: [{ required: true, message: '专业编码不能为空', trigger: 'blur' }],
      majorName: [{ required: true, message: '专业名称不能为空', trigger: 'blur' }],
    },
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 加载院校列表 */
  async function loadCollegeList() {
    let params = {
      pageNum: 1,
      pageSize: 9999,
    };
    const res = await listCollege(params);
    if (res.code === 200) {
      collegeList.value = res.data || [];
    }
  }

  /** 查询专业信息列表 */
  async function getList() {
    loading.value = true;
    const res = await listMajor(queryParams.value);
    majorList.value = res.rows;
    total.value = res.total;
    loading.value = false;
  }

  /** 取消按钮 */
  function cancel() {
    open.value = false;
    reset();
  }

  /** 表单重置 */
  function reset() {
    form.value = {
      majorId: undefined,
      majorCode: undefined,
      majorName: undefined,
      majorShortName: undefined,
      majorCategory: undefined,
      duration: undefined,
      eduLevel: undefined,
      collegeId: undefined,
      intro: undefined,
      status: '0',
    };
    proxy.resetForm('formRef');
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    queryParams.value.pageNum = 1;
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm('queryRef');
    handleQuery();
  }

  /** 多选框选中数据 */
  function handleSelectionChange(selection: BaseMajor[]) {
    ids.value = selection.map((item) => item.majorId!);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = '添加专业信息';
  }

  /** 修改按钮操作 */
  async function handleUpdate(row: BaseMajor) {
    reset();
    const id = row.majorId || ids.value[0];
    const res = await getMajor(id);
    form.value = res.data!;
    open.value = true;
    title.value = '修改专业信息';
  }

  /** 提交按钮 */
  async function submitForm() {
    const valid = await proxy.$refs.formRef.validate();
    if (valid) {
      if (form.value.majorId) {
        await updateMajor(form.value);
        proxy.$modal.msgSuccess('修改成功');
      } else {
        await addMajor(form.value);
        proxy.$modal.msgSuccess('新增成功');
      }
      open.value = false;
      getList();
    }
  }

  /** 删除按钮操作 */
  async function handleDelete(row: BaseMajor) {
    const _majorIds = row.majorId || ids.value;
    try {
      await proxy.$modal.confirm('是否确认删除专业信息编号为"' + _majorIds + '"的数据项？');
      await delMajor(_majorIds);
      getList();
      proxy.$modal.msgSuccess('删除成功');
    } catch {}
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('base/major/export', { ...queryParams.value }, `major_#[[${new Date().getTime()}]]#.xlsx`);
  }

  loadCollegeList();
  getList();
</script>
