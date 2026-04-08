<template>
  <div class="forward-home">
    <!-- 英雄区 -->
    <section class="hero-section">
      <h1 class="hero-title">三教改革资讯平台</h1>
      <p class="hero-desc">
        聚焦"教师、教材、教法"改革，<br />
        打造专业的职业教育资源管理与展示平台
      </p>
      <div class="hero-actions">
        <el-button type="warning" plain size="large" @click="scrollToSection('teachers')">
          了解师资力量
        </el-button>
        <el-button type="success" plain size="large" @click="scrollToSection('students')">
          查看学生培养
        </el-button>
      </div>
    </section>

    <!-- 数据统计区 -->
    <section class="stats-section">
      <el-row :gutter="16">
        <el-col :xs="24" :sm="8">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #ecf5ff; color: #409eff">
              <el-icon :size="28"><User /></el-icon>
            </div>
            <div class="stat-value">{{ statistics.teacherCount }}</div>
            <div class="stat-label">教师总数</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #f0f9eb; color: #67c23a">
              <el-icon :size="28"><Reading /></el-icon>
            </div>
            <div class="stat-value">{{ statistics.studentCount }}</div>
            <div class="stat-label">学生总数</div>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8">
          <div class="stat-card">
            <div class="stat-icon" style="background-color: #fef0f0; color: #f56c6c">
              <el-icon :size="28"><OfficeBuilding /></el-icon>
            </div>
            <div class="stat-value">{{ statistics.collegeCount }}</div>
            <div class="stat-label">院校数量</div>
          </div>
        </el-col>
      </el-row>
    </section>

    <!-- 师资展示区 -->
    <section id="teachers" class="teacher-section">
      <div class="section-header">
        <h2 class="section-title">师资力量</h2>
        <div class="filter-tabs">
          <el-radio-group v-model="teacherFilter" size="default">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button label="enterprise">企业导师</el-radio-button>
            <el-radio-button label="teacher">教师</el-radio-button>
          </el-radio-group>
        </div>
      </div>
      <el-row :gutter="16" v-loading="teacherLoading">
        <el-col
          v-for="teacher in filteredTeachers"
          :key="teacher.teacherId"
          :xs="24"
          :sm="12"
          :md="8"
          :lg="6"
        >
          <div class="teacher-card">
            <el-avatar
              :src="teacher.avatar || defaultAvatar"
              :size="80"
              class="teacher-avatar"
            />
            <div class="teacher-name">{{ teacher.teacherName }}</div>
            <span
              class="teacher-type"
              :style="{
                backgroundColor: teacher.teacherType === 'enterprise' ? '#fdf6ec' : '#ecf5ff',
                color: teacher.teacherType === 'enterprise' ? '#e6a23c' : '#409eff'
              }"
            >
              {{ teacher.teacherType === 'enterprise' ? '企业导师' : '教师' }}
            </span>
            <p class="teacher-intro">{{ teacher.intro || '暂无简介' }}</p>
          </div>
        </el-col>
        <el-col v-if="filteredTeachers.length === 0 && !teacherLoading" :span="24">
          <el-empty description="暂无师资数据" />
        </el-col>
      </el-row>
      <div v-if="filteredTeachers.length > 0" style="text-align: center; margin-top: 20px">
        <el-button type="primary" plain @click="goToTeacherList">查看更多</el-button>
      </div>
    </section>

    <!-- 学生情况展示区 -->
    <section id="students" class="student-section">
      <div class="section-header">
        <h2 class="section-title">学生培养</h2>
      </div>
      <el-row :gutter="24" v-loading="studentLoading">
        <el-col :xs="24" :lg="12">
          <div ref="chartRef" style="width: 100%; height: 300px"></div>
        </el-col>
        <el-col :xs="24" :lg="12">
          <el-table :data="recentStudents" stripe style="width: 100%">
            <el-table-column prop="studentName" label="姓名" width="80" />
            <el-table-column prop="sex" label="性别" width="60">
              <template #default="{ row }">
                {{ row.sex === '0' ? '男' : '女' }}
              </template>
            </el-table-column>
            <el-table-column prop="collegeName" label="院校" min-width="120" show-overflow-tooltip />
            <el-table-column prop="grade" label="年级" width="90" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </section>

    <!-- 快捷入口区 -->
    <section class="quick-links">
      <div class="section-header">
        <h2 class="section-title">快捷入口</h2>
      </div>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="6">
          <div class="link-card" @click="goToCollegeList">
            <div class="link-icon" style="background-color: #ecf5ff; color: #409eff">
              <el-icon :size="24"><OfficeBuilding /></el-icon>
            </div>
            <div class="link-content">
              <div class="link-title">院校展示</div>
              <div class="link-desc">查看各院校信息</div>
            </div>
            <el-icon class="link-arrow"><ArrowRight /></el-icon>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="link-card" @click="goToMajorList">
            <div class="link-icon" style="background-color: #f0f9eb; color: #67c23a">
              <el-icon :size="24"><Reading /></el-icon>
            </div>
            <div class="link-content">
              <div class="link-title">专业展示</div>
              <div class="link-desc">了解各专业信息</div>
            </div>
            <el-icon class="link-arrow"><ArrowRight /></el-icon>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="link-card" @click="goToTeacherList">
            <div class="link-icon" style="background-color: #fef0f0; color: #f56c6c">
              <el-icon :size="24"><User /></el-icon>
            </div>
            <div class="link-content">
              <div class="link-title">师资展示</div>
              <div class="link-desc">查看教师团队</div>
            </div>
            <el-icon class="link-arrow"><ArrowRight /></el-icon>
          </div>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <div class="link-card" @click="goToStudentList">
            <div class="link-icon" style="background-color: #fdf6ec; color: #e6a23c">
              <el-icon :size="24"><TrendCharts /></el-icon>
            </div>
            <div class="link-content">
              <div class="link-title">学生展示</div>
              <div class="link-desc">了解学生情况</div>
            </div>
            <el-icon class="link-arrow"><ArrowRight /></el-icon>
          </div>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, ref, computed, watch, nextTick, onUnmounted } from 'vue';
  import { useRouter } from 'vue-router';
  import * as echarts from 'echarts';
  import { User, Reading, OfficeBuilding, ArrowRight, TrendCharts } from '@element-plus/icons-vue';
  import { listTeacher } from '@/api/base/teacher';
  import { listStudent } from '@/api/base/student';
  import { listCollege } from '@/api/base/college';
  import type { BaseTeacher } from '@/types/api/base/teacher';
  import type { BaseStudent } from '@/types/api/base/student';

  const router = useRouter();
  const chartRef = ref<HTMLElement | null>(null);
  let chartInstance: echarts.ECharts | null = null;

  // 默认头像
  const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

  // 统计数据
  const statistics = ref({
    teacherCount: 0,
    studentCount: 0,
    collegeCount: 0
  });

  // 教师列表数据
  const teacherList = ref<BaseTeacher[]>([]);
  const teacherLoading = ref(false);
  const teacherFilter = ref('');

  // 学生列表数据
  const studentList = ref<BaseStudent[]>([]);
  const studentLoading = ref(false);
  const recentStudents = computed(() => studentList.value.slice(0, 5));

  // 过滤后的教师列表
  const filteredTeachers = computed(() => {
    let list = teacherList.value;
    if (teacherFilter.value) {
      list = list.filter(t => t.teacherType === teacherFilter.value);
    }
    return list.slice(0, 8);
  });

  // 获取统计数据
  async function fetchStatistics() {
    try {
      // 获取教师总数
      const teacherRes = await listTeacher({ pageNum: 1, pageSize: 1 });
      if (teacherRes.rows) {
        statistics.value.teacherCount = teacherRes.total || 0;
      }

      // 获取学生总数
      const studentRes = await listStudent({ pageNum: 1, pageSize: 1 });
      if (studentRes.rows) {
        statistics.value.studentCount = studentRes.total || 0;
      }

      // 获取院校总数
      const collegeRes = await listCollege({ pageNum: 1, pageSize: 1 });
      if (collegeRes.data) {
        statistics.value.collegeCount = Array.isArray(collegeRes.data) ? collegeRes.data.length : 0;
      }
    } catch (error) {
      console.error('获取统计数据失败:', error);
    }
  }

  // 获取教师列表
  async function fetchTeachers() {
    teacherLoading.value = true;
    try {
      const res = await listTeacher({ pageNum: 1, pageSize: 20 });
      teacherList.value = res.rows || [];
    } catch (error) {
      console.error('获取教师列表失败:', error);
    } finally {
      teacherLoading.value = false;
    }
  }

  // 获取学生列表
  async function fetchStudents() {
    studentLoading.value = true;
    try {
      const res = await listStudent({ pageNum: 1, pageSize: 50 });
      studentList.value = res.rows || [];
      nextTick(() => {
        initChart();
      });
    } catch (error) {
      console.error('获取学生列表失败:', error);
    } finally {
      studentLoading.value = false;
    }
  }

  // 初始化图表
  function initChart() {
    if (!chartRef.value) return;

    // 销毁旧实例
    if (chartInstance) {
      chartInstance.dispose();
    }

    chartInstance = echarts.init(chartRef.value);

    // 按年级统计
    const gradeMap = new Map<string, number>();
    studentList.value.forEach(student => {
      const grade = student.grade || '未知';
      gradeMap.set(grade, (gradeMap.get(grade) || 0) + 1);
    });

    const gradeData = Array.from(gradeMap.entries()).map(([name, value]) => ({
      name,
      value
    }));

    const option = {
      title: {
        text: '学生年级分布',
        left: 'center',
        textStyle: {
          fontSize: 16,
          fontWeight: 'bold',
          color: '#303133'
        }
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b}: {c}人 ({d}%)'
      },
      legend: {
        orient: 'vertical',
        right: '10%',
        top: 'center'
      },
      color: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#52606d'],
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          center: ['40%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 8,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: true,
            formatter: '{b}: {c}人'
          },
          data: gradeData.length > 0 ? gradeData : [{ name: '暂无数据', value: 0 }]
        }
      ]
    };

    chartInstance.setOption(option);
  }

  // 滚动到指定区域
  function scrollToSection(sectionId: string) {
    const element = document.getElementById(sectionId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  // 跳转到教师列表页
  function goToTeacherList() {
    router.push('/base/teacher');
  }

  // 跳转到学生列表页
  function goToStudentList() {
    router.push('/base/student');
  }

  // 跳转到院校列表页
  function goToCollegeList() {
    router.push('/base/college');
  }

  // 跳转到专业列表页
  function goToMajorList() {
    router.push('/base/major');
  }

  // 获取状态类型
  function getStatusType(status: string | undefined) {
    switch (status) {
      case '0':
        return 'success';
      case '1':
        return 'info';
      case '2':
        return 'warning';
      default:
        return 'info';
    }
  }

  // 获取状态文本
  function getStatusText(status: string | undefined) {
    switch (status) {
      case '0':
        return '在读';
      case '1':
        return '毕业';
      case '2':
        return '肄业';
      default:
        return '未知';
    }
  }

  // 监听窗口变化，重绘图表
  function handleResize() {
    if (chartInstance) {
      chartInstance.resize();
    }
  }

  onMounted(() => {
    fetchStatistics();
    fetchTeachers();
    fetchStudents();
    window.addEventListener('resize', handleResize);
  });

  // 监听教师过滤变化，重新渲染图表
  watch(studentList, () => {
    nextTick(() => {
      initChart();
    });
  });

  // 组件卸载时销毁图表
  onUnmounted(() => {
    if (chartInstance) {
      chartInstance.dispose();
      chartInstance = null;
    }
    window.removeEventListener('resize', handleResize);
  });
</script>

<style lang="scss" scoped>
  @use '@/assets/styles/forward.scss';
</style>