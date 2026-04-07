import { QueryType } from './request';

// 模式类型定义
export type ModeType = 'chart' | 'report' | 'maintenance' | null;

// 模式管理器类
export class ModeManager {
  // 响应式状态
  public mode: Ref<ModeType>;
  public showChartRecommendations: Ref<boolean>;
  public showReportRecommendations: Ref<boolean>;
  public showMaintenanceRecommendations: Ref<boolean>;
  public showToast: Ref<boolean>;
  public toastMessage: Ref<string>;

  // 推荐语句数组
  public chartRecommendations: string[] = [
    '展示我近期引体向上的体测情况',
    '可视化我的50米跑成绩变化趋势',
    '展示我近期跑步的距离趋势',
    '展示我所有体测项目的达标情况',
    '展示我所有体测项目的历史最好成绩',
  ];

  public reportRecommendations: string[] = [
    '生成我全年体测成绩汇总报告',
    '分析我近两年的体测成绩变化报告',
    '生成我的体测达标情况详细报告',
    '对比分析我与同龄平均水平的差距',
    '提供提高我弱项成绩的训练建议报告',
  ];

  public maintenanceRecommendations: string[] = [
    'AI跑步系统的操作流程',
    'AI身高体重测试仪的操作流程',
    'AI视觉定位轮式测试设备的操作流程',
    'AI视觉倾斜起跑测试设备的操作流程',
    'AI视觉仰卧体测设备的操作流程',
    'AI视觉坐位体前屈测试设备的操作流程',
    'AI智能检测/活量测试设备的操作流程',
  ];

  constructor() {
    // 初始化响应式状态
    this.mode = ref<ModeType>(null);
    this.showChartRecommendations = ref(false);
    this.showReportRecommendations = ref(false);
    this.showMaintenanceRecommendations = ref(false);
    this.showToast = ref(false);
    this.toastMessage = ref('');
  }

  /**
   * 切换模式
   * @param mode
   */
  public toggleMode(mode: ModeType): void {
    this.resetMode();
    this.mode.value = mode;

    if (mode === 'chart') {
      this.showChartRecommendations.value = true;
    } else if (mode === 'report') {
      this.showReportRecommendations.value = true;
    } else if (mode === 'maintenance') {
      this.showMaintenanceRecommendations.value = true;
    } else {
      this.mode.value = null;
    }
  }
  /**
   * 重置模式
   */
  public resetMode(): void {
    this.mode.value = null;
    this.showChartRecommendations.value = false;
    this.showReportRecommendations.value = false;
    this.showMaintenanceRecommendations.value = false;
  }

  /**
   * 根据当前模式获取查询类型
   * @returns 查询类型
   */
  public getQueryTypeByMode(): QueryType {
    if (this.mode.value === 'chart') {
      return QueryType.SQL_NEEDED_CHART;
    } else if (this.mode.value === 'report') {
      return QueryType.SQL_NEEDED_REPORT;
    } else if (this.mode.value === 'maintenance') {
      return QueryType.MAINTENANCE_CONSULTATION;
    }
    // 默认返回文本类型
    return QueryType.SQL_NEEDED_ONLY_TEXT;
  }

  /**
   * 获取生成中的系统提示消息
   */
  public getGeneratingMessage(): string {
    if (this.mode.value === 'chart') {
      return '📊 图表生成中...';
    } else if (this.mode.value === 'report') {
      return '📋 报告生成中...';
    } else if (this.mode.value === 'maintenance') {
      return '🔧 运维咨询处理中...';
    }
    return '';
  }

  /**
   * 获取错误提示消息
   */
  public getErrorMessage(): string {
    if (this.mode.value === 'chart') {
      return '❌ 图表生成失败，请稍后重试';
    } else if (this.mode.value === 'report') {
      return '❌ 报告生成失败，请稍后重试';
    } else if (this.mode.value === 'maintenance') {
      return '❌ 运维咨询失败，请稍后重试';
    }
    return '❌ 处理失败，请稍后重试';
  }

  /**
   * 展开当前模式的推荐面板
   */
  public expandCurrentRecommendations(): void {
    if (this.mode.value === 'chart') {
      this.showChartRecommendations.value = true;
    } else if (this.mode.value === 'report') {
      this.showReportRecommendations.value = true;
    }
  }

  /**
   * 自动关闭所有推荐面板
   */
  public autoCloseRecommendations(): void {
    console.log('autoCloseRecommendations');
    this.showChartRecommendations.value = false;
    this.showReportRecommendations.value = false;
  }
}

// 创建全局模式管理器实例
export const modeManager = new ModeManager();
