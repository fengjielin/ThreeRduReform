<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="教师姓名" prop="teacherName">
        <el-input v-model="queryParams.teacherName" placeholder="请输入教师姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="教师类型" prop="teacherType">
        <el-select v-model="queryParams.teacherType" placeholder="请选择教师类型" clearable>
          <el-option v-for="dict in teacher_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable>
          <el-option label="男" value="0" />
          <el-option label="女" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="所属院校" prop="collegeId">
        <el-select v-model="queryParams.collegeId" placeholder="请选择所属院校" clearable filterable>
          <el-option v-for="college in collegeList" :key="college.collegeId" :label="college.collegeName" :value="college.collegeId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['base:teacher:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['base:teacher:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:teacher:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['base:teacher:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="teacherList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="教师工号" align="center" prop="teacherCode" />
      <el-table-column label="教师姓名" prop="teacherName" :show-overflow-tooltip="true" />
      <el-table-column label="教师类型" align="center" prop="teacherType">
        <template #default="{ row }">
          <dict-tag :options="teacher_type" :value="row.teacherType ? [row.teacherType] : []" />
        </template>
      </el-table-column>
      <el-table-column label="性别" align="center" prop="sex">
        <template #default="{ row }">
          <span>{{ row.sex === '0' ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号码" align="center" prop="phone" />
      <el-table-column label="邮箱" align="center" prop="email" :show-overflow-tooltip="true" />
      <el-table-column label="所属院校" align="center" prop="collegeName" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="{ row }">
          <dict-tag :options="sys_normal_disable" :value="row.status ? [row.status] : []" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template #default="{ row }">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(row)" v-hasPermi="['base:teacher:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(row)" v-hasPermi="['base:teacher:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改教师信息对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="教师工号" prop="teacherCode">
          <el-input v-model="form.teacherCode" placeholder="请输入教师工号" />
        </el-form-item>
        <el-form-item label="教师姓名" prop="teacherName">
          <el-input v-model="form.teacherName" placeholder="请输入教师姓名" />
        </el-form-item>
        <el-form-item label="教师类型" prop="teacherType">
          <el-select v-model="form.teacherType" placeholder="请选择教师类型">
            <el-option v-for="dict in teacher_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="个人简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" placeholder="请输入个人简介" />
        </el-form-item>
        <el-form-item label="所属院校" prop="collegeId">
          <el-select v-model="form.collegeId" placeholder="请选择所属院校" filterable clearable>
            <el-option v-for="college in collegeList" :key="college.collegeId" :label="college.collegeName" :value="college.collegeId" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
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

<script setup lang="ts" name="BaseTeacher">
  import { listTeacher, getTeacher, delTeacher, addTeacher, updateTeacher } from '@/api/base/teacher';
  import { listCollege } from '@/api/base/college';
  import type { BaseTeacher, BaseTeacherQueryParams } from '@/types/api/base/teacher';
  import type { BaseCollege } from '@/types/api/base/college';

  const { proxy } = getCurrentInstance();
  const { teacher_type, sys_normal_disable } = proxy.useDict('teacher_type', 'sys_normal_disable');

  const teacherList = ref<BaseTeacher[]>([]);
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
    form: {} as BaseTeacher,
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      teacherName: undefined,
      teacherType: undefined,
      sex: undefined,
    } as BaseTeacherQueryParams,
    rules: {
      teacherCode: [{ required: true, message: '教师工号不能为空', trigger: 'blur' }],
      teacherName: [{ required: true, message: '教师姓名不能为空', trigger: 'blur' }],
      teacherType: [{ required: true, message: '教师类型不能为空', trigger: 'change' }],
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

  /** 查询教师信息列表 */
  async function getList() {
    loading.value = true;
    const res = await listTeacher(queryParams.value);
    teacherList.value = res.rows;
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
      teacherId: undefined,
      teacherCode: undefined,
      teacherName: undefined,
      teacherType: undefined,
      sex: '0',
      phone: undefined,
      email: undefined,
      avatar: undefined,
      intro: undefined,
      collegeId: undefined,
      sort: 0,
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
  function handleSelectionChange(selection: BaseTeacher[]) {
    ids.value = selection.map((item) => item.teacherId!);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = '添加教师信息';
  }

  /** 修改按钮操作 */
  async function handleUpdate(row: BaseTeacher) {
    reset();
    const id = row.teacherId || ids.value[0];
    const res = await getTeacher(id);
    form.value = res.data!;
    open.value = true;
    title.value = '修改教师信息';
  }

  /** 提交按钮 */
  async function submitForm() {
    const valid = await proxy.$refs.formRef.validate();
    if (valid) {
      if (form.value.teacherId) {
        await updateTeacher(form.value);
        proxy.$modal.msgSuccess('修改成功');
      } else {
        await addTeacher(form.value);
        proxy.$modal.msgSuccess('新增成功');
      }
      open.value = false;
      getList();
    }
  }

  /** 删除按钮操作 */
  async function handleDelete(row: BaseTeacher) {
    const _teacherIds = row.teacherId || ids.value;
    try {
      await proxy.$modal.confirm('是否确认删除教师信息编号为"' + _teacherIds + '"的数据项？');
      await delTeacher(_teacherIds);
      getList();
      proxy.$modal.msgSuccess('删除成功');
    } catch {}
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('base/teacher/export', { ...queryParams.value }, `teacher_#[[${new Date().getTime()}]]#.xlsx`);
  }

  loadCollegeList();
  getList();
</script>
