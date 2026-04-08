export default {
  /**
   * 网页标题
   */
  title: import.meta.env.VITE_APP_TITLE,

  /**
   * 侧边栏主题 深色主题theme-dark，浅色主题theme-light
   */
  sideTheme: 'theme-blue',

  /**
   * 主题颜色
   */
  theme: '#409eff',

  /**
   * 是否系统布局配置
   */
  showSettings: true,

  /**
   * 菜单导航模式 1、纯左侧 2、混合（左侧+顶部） 3、纯顶部
   */
  navType: 1,

  /**
   * 是否显示 tagsView
   */
  tagsView: true,

  /**
   * 显示页签图标
   */
  tagsIcon: true,

  /**
   * 是否固定头部
   */
  fixedHeader: true,

  /**
   * 是否显示logo
   */
  sidebarLogo: false,

  /**
   * 是否显示动态标题
   */
  dynamicTitle: true,

  /**
   * 是否显示底部版权
   */
  footerVisible: false,

  /**
   * 底部版权文本内容
   */
  footerContent: 'Copyright © 2018-2026 RuoYi. All Rights Reserved.',

  /**
   * 导航菜单风格 rounded:圆润 plain.朴素
   */
  menuStyle: 'rounded',

  // 控制开关
  casEnable: false,
  // cas登陆url
  casLoginUrl: 'http://127.0.0.1:80/tools/login',
  // cas登出url
  casLogoutUrl: 'http://127.0.0.1:80/tools/login',
};
