<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="queryParams.sex" placeholder="请选择性别" clearable>
          <el-option label="男" value="0" />
          <el-option label="女" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="年级" prop="grade">
        <el-input v-model="queryParams.grade" placeholder="请输入年级" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in student_status" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="所属院校" prop="collegeId">
        <el-select v-model="queryParams.collegeId" placeholder="请选择所属院校" clearable filterable>
          <el-option v-for="college in collegeList" :key="college.collegeId" :label="college.collegeName" :value="college.collegeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="所学专业" prop="majorId">
        <el-select v-model="queryParams.majorId" placeholder="请选择所学专业" clearable filterable>
          <el-option v-for="major in majorList" :key="major.majorId" :label="major.majorName" :value="major.majorId" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['base:student:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['base:student:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['base:student:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['base:student:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学号" align="center" prop="studentCode" />
      <el-table-column label="年级" align="center" prop="grade" />
      <el-table-column label="班级" align="center" prop="className" />
      <el-table-column label="学生姓名" prop="studentName" :show-overflow-tooltip="true" />
      <el-table-column label="性别" align="center" prop="sex">
        <template #default="{ row }">
          <span>{{ row.sex === '0' ? '男' : '女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="出生日期" align="center" prop="birthday">
        <template #default="{ row }">
          <span>{{ parseTime(row.birthday, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="手机号码" align="center" prop="phone" />
      <el-table-column label="所属院校" align="center" prop="collegeName" :show-overflow-tooltip="true" />
      <el-table-column label="所学专业" align="center" prop="majorName" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="{ row }">
          <dict-tag :options="student_status" :value="row.status ? [row.status] : []" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template #default="{ row }">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(row)" v-hasPermi="['base:student:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(row)" v-hasPermi="['base:student:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改学生信息对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="学号" prop="studentCode">
          <el-input v-model="form.studentCode" placeholder="请输入学号" />
        </el-form-item>
        <el-form-item label="学生姓名" prop="studentName">
          <el-input v-model="form.studentName" placeholder="请输入学生姓名" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="form.sex">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthday">
          <el-date-picker v-model="form.birthday" type="date" value-format="YYYY-MM-DD" placeholder="请选择出生日期" />
        </el-form-item>
        <el-form-item label="民族" prop="nation">
          <el-input v-model="form.nation" placeholder="请输入民族" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="所属院校" prop="collegeId">
          <el-select v-model="form.collegeId" placeholder="请选择所属院校" filterable @change="handleCollegeChange">
            <el-option v-for="college in collegeList" :key="college.collegeId" :label="college.collegeName" :value="college.collegeId" />
          </el-select>
        </el-form-item>
        <el-form-item label="所学专业" prop="majorId">
          <el-select v-model="form.majorId" placeholder="请选择所学专业" filterable>
            <el-option v-for="major in majorList" :key="major.majorId" :label="major.majorName" :value="major.majorId" />
          </el-select>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input v-model="form.grade" placeholder="请输入年级" />
        </el-form-item>
        <el-form-item label="班级" prop="className">
          <el-input v-model="form.className" placeholder="请输入班级" />
        </el-form-item>
        <el-form-item label="入校时间" prop="entranceDate">
          <el-date-picker v-model="form.entranceDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择入校时间" />
        </el-form-item>
        <el-form-item label="毕业时间" prop="graduationDate">
          <el-date-picker v-model="form.graduationDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择毕业时间" />
        </el-form-item>
        <el-form-item label="毕业去向" prop="graduationDest">
          <el-input v-model="form.graduationDest" placeholder="请输入毕业去向" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in student_status" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
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

<script setup lang="ts" name="BaseStudent">
  import { listStudent, getStudent, delStudent, addStudent, updateStudent } from '@/api/base/student';
  import { listCollege } from '@/api/base/college';
  import { listMajor } from '@/api/base/major';

  import type { BaseStudent, BaseStudentQueryParams } from '@/types/api/base/student';
  import type { BaseCollege } from '@/types/api/base/college';
  import type { BaseMajor } from '@/types/api/base/major';

  const { proxy } = getCurrentInstance();
  const { student_status } = proxy.useDict('student_status');

  const studentList = ref<BaseStudent[]>([]);
  const collegeList = ref<BaseCollege[]>([]);
  const majorList = ref<BaseMajor[]>([]);
  const open = ref<boolean>(false);
  const loading = ref<boolean>(true);
  const showSearch = ref<boolean>(true);
  const ids = ref<number[]>([]);
  const single = ref<boolean>(true);
  const multiple = ref<boolean>(true);
  const total = ref<number>(0);
  const title = ref<string>('');

  const data = reactive({
    form: {} as BaseStudent,
    queryParams: {
      pageNum: 1,
      pageSize: 10,
      studentName: undefined,
      sex: undefined,
      grade: undefined,
      status: undefined,
    } as BaseStudentQueryParams,
    rules: {
      studentCode: [{ required: true, message: '学号不能为空', trigger: 'blur' }],
      studentName: [{ required: true, message: '学生姓名不能为空', trigger: 'blur' }],
      sex: [{ required: true, message: '性别不能为空', trigger: 'change' }],
      collegeId: [{ required: true, message: '所属院校不能为空', trigger: 'change' }],
      majorId: [{ required: true, message: '所学专业不能为空', trigger: 'change' }],
      entranceDate: [{ required: true, message: '入校时间不能为空', trigger: 'change' }],
    },
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 加载院校和专业列表 */
  async function loadCollegeAndMajor() {
    let params = {
      pageNum: 1,
      pageSize: 9999,
    };
    const [collegeRes, majorRes] = await Promise.all([listCollege(params), listMajor(params)]);
    if (collegeRes.code === 200) {
      collegeList.value = collegeRes.data || [];
    }
    if (majorRes.code === 200) {
      majorList.value = majorRes.rows || [];
    }
  }

  /** 院校选择变更 */
  function handleCollegeChange() {
    form.value.majorId = undefined;
  }

  /** 查询学生信息列表 */
  async function getList() {
    loading.value = true;
    const res = await listStudent(queryParams.value);
    studentList.value = res.rows;
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
      studentId: undefined,
      studentCode: undefined,
      studentName: undefined,
      sex: '0',
      birthday: undefined,
      nation: undefined,
      idCard: undefined,
      phone: undefined,
      collegeId: undefined,
      majorId: undefined,
      grade: undefined,
      className: undefined,
      entranceDate: undefined,
      graduationDate: undefined,
      graduationDest: undefined,
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
  function handleSelectionChange(selection: BaseStudent[]) {
    ids.value = selection.map((item) => item.studentId!);
    single.value = selection.length != 1;
    multiple.value = !selection.length;
  }

  /** 新增按钮操作 */
  function handleAdd() {
    reset();
    open.value = true;
    title.value = '添加学生信息';
  }

  /** 修改按钮操作 */
  async function handleUpdate(row: BaseStudent) {
    reset();
    const id = row.studentId || ids.value[0];
    const res = await getStudent(id);
    form.value = res.data!;
    open.value = true;
    title.value = '修改学生信息';
  }

  /** 提交按钮 */
  async function submitForm() {
    const valid = await proxy.$refs.formRef.validate();
    if (valid) {
      if (form.value.studentId) {
        await updateStudent(form.value);
        proxy.$modal.msgSuccess('修改成功');
      } else {
        await addStudent(form.value);
        proxy.$modal.msgSuccess('新增成功');
      }
      open.value = false;
      getList();
    }
  }

  /** 删除按钮操作 */
  async function handleDelete(row: BaseStudent) {
    const _studentIds = row.studentId || ids.value;
    try {
      await proxy.$modal.confirm('是否确认删除学生信息编号为"' + _studentIds + '"的数据项？');
      await delStudent(_studentIds);
      getList();
      proxy.$modal.msgSuccess('删除成功');
    } catch {}
  }

  /** 导出按钮操作 */
  function handleExport() {
    proxy.download('base/student/export', { ...queryParams.value }, `student_#[[${new Date().getTime()}]]#.xlsx`);
  }

  loadCollegeAndMajor();
  getList();
</script>
