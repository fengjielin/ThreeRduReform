<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="名称" prop="collegeName">
        <el-input v-model="queryParams.collegeName" placeholder="请输入院校名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="院校类型" prop="collegeType">
        <el-select v-model="queryParams.collegeType" placeholder="请选择院校类型" clearable>
          <el-option v-for="dict in college_type" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['base:college:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Sort" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="collegeList" row-key="collegeId" :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column label="院校名称" prop="collegeName" />
      <el-table-column label="院校简称" align="center" prop="collegeShortName" width="120" />
      <el-table-column label="院校类型" align="center" prop="collegeType" width="120">
        <template #default="{ row }">
          <dict-tag :options="college_type" :value="row.collegeType ? [row.collegeType] : []" />
        </template>
      </el-table-column>
      <el-table-column label="所在地区" align="center" prop="area"   />
      <el-table-column label="联系人" align="center" prop="contact" width="100" />
      <el-table-column label="联系电话" align="center" prop="phone" width="120" />
      <el-table-column label="状态" align="center" prop="status" width="120">
        <template #default="{ row }">
          <dict-tag :options="sys_normal_disable" :value="row.status ? [row.status] : []" />
        </template>
      </el-table-column>
      <el-table-column label="显示顺序" align="center" prop="orderNum" width="80" />
      <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
        <template #default="{ row }">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(row)" v-hasPermi="['base:college:edit']">修改</el-button>
          <el-button link type="primary" icon="Plus" @click="handleAdd(row)" v-hasPermi="['base:college:add']">新增</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(row)" v-hasPermi="['base:college:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改院校信息对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级院校" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="collegeOptions"
            :props="{ value: 'collegeId', label: 'collegeName', children: 'children' }"
            value-key="collegeId"
            placeholder="选择上级院校"
            check-strictly />
        </el-form-item>
        <el-form-item label="院校名称" prop="collegeName">
          <el-input v-model="form.collegeName" placeholder="请输入院校名称" />
        </el-form-item>
        <el-form-item label="院校简称" prop="collegeShortName">
          <el-input v-model="form.collegeShortName" placeholder="请输入院校简称" />
        </el-form-item>
        <el-form-item label="院校编码" prop="collegeCode">
          <el-input v-model="form.collegeCode" placeholder="请输入院校编码" />
        </el-form-item>
        <el-form-item label="院校类型" prop="collegeType">
          <el-select v-model="form.collegeType" placeholder="请选择院校类型">
            <el-option v-for="dict in college_type" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="所在地区" prop="area">
          <el-input v-model="form.area" placeholder="请输入所在地区" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="联系人" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="院校状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="院校简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" placeholder="请输入院校简介" />
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

<script setup lang="ts" name="BaseCollege">
  import { listCollege, getCollege, delCollege, addCollege, updateCollege } from '@/api/base/college';
  import type { BaseCollege, BaseCollegeQueryParams } from '@/types/api/base/college';

  const { proxy } = getCurrentInstance();
  const { college_type, sys_normal_disable } = proxy.useDict('college_type', 'sys_normal_disable');

  const collegeList = ref<BaseCollege[]>([]);
  const collegeOptions = ref<BaseCollege[]>([]);
  const open = ref<boolean>(false);
  const loading = ref<boolean>(true);
  const showSearch = ref<boolean>(true);
  const title = ref<string>('');
  const isExpandAll = ref<boolean>(true);
  const refreshTable = ref<boolean>(true);

  const data = reactive({
    form: {} as BaseCollege,
    queryParams: {
      collegeName: undefined,
      collegeType: undefined,
    } as BaseCollegeQueryParams,
    rules: {
      parentId: [{ required: true, message: '上级院校不能为空', trigger: 'blur' }],
      collegeName: [{ required: true, message: '院校名称不能为空', trigger: 'blur' }],
      collegeCode: [{ required: true, message: '院校编码不能为空', trigger: 'blur' }],
      orderNum: [{ required: true, message: '显示顺序不能为空', trigger: 'blur' }],
      status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
    },
  });

  const { queryParams, form, rules } = toRefs(data);

  /** 查询院校信息列表 */
  async function getList() {
    loading.value = true;
    const res = await listCollege(queryParams.value);
    collegeList.value = proxy.handleTree(res.data, 'collegeId', 'parentId') as BaseCollege[];
    loading.value = false;
  }

  /** 查询院校树下拉树结构 */
  async function getTreeselect() {
    const res = await listCollege({});
    collegeOptions.value = [];
    const data: BaseCollege = { collegeId: 0, collegeName: '顶级院校', children: [] };
    data.children = proxy.handleTree(res.data, 'collegeId', 'parentId') as BaseCollege[];
    collegeOptions.value.push(data);
  }

  /** 取消按钮 */
  function cancel() {
    open.value = false;
    reset();
  }

  /** 表单重置 */
  function reset() {
    form.value = {
      collegeId: undefined,
      parentId: 0,
      collegeCode: undefined,
      collegeName: undefined,
      collegeShortName: undefined,
      collegeType: undefined,
      area: undefined,
      address: undefined,
      contact: undefined,
      phone: undefined,
      intro: undefined,
      orderNum: 0,
      status: '0',
    };
    proxy.resetForm('formRef');
  }

  /** 搜索按钮操作 */
  function handleQuery() {
    getList();
  }

  /** 重置按钮操作 */
  function resetQuery() {
    proxy.resetForm('queryRef');
    handleQuery();
  }

  /** 展开/折叠操作 */
  function toggleExpandAll() {
    refreshTable.value = false;
    isExpandAll.value = !isExpandAll.value;
    nextTick(() => {
      refreshTable.value = true;
    });
  }

  /** 新增按钮操作 */
  async function handleAdd(row?: BaseCollege) {
    await getTreeselect();
    if (row) {
      form.value.parentId = row.collegeId;
    } else {
      form.value.parentId = 0;
    }
    open.value = true;
    title.value = '添加院校信息';
  }

  /** 修改按钮操作 */
  async function handleUpdate(row: BaseCollege) {
    await getTreeselect();
    form.value = { ...row };
    open.value = true;
    title.value = '修改院校信息';
  }

  /** 提交按钮 */
  async function submitForm() {
    const valid = await proxy.$refs.formRef.validate();
    if (valid) {
      if (form.value.collegeId) {
        await updateCollege(form.value);
        proxy.$modal.msgSuccess('修改成功');
      } else {
        await addCollege(form.value);
        proxy.$modal.msgSuccess('新增成功');
      }
      open.value = false;
      getList();
    }
  }

  /** 删除按钮操作 */
  async function handleDelete(row: BaseCollege) {
    try {
      await proxy.$modal.confirm('是否确认删除院校信息编号为"' + row.collegeId + '"的数据项？');
      await delCollege(row.collegeId!);
      getList();
      proxy.$modal.msgSuccess('删除成功');
    } catch {}
  }

  getList();
</script>
