# 若依前端技术文档

> 若依前端 (ruoyi-ui-vue3) 基于 Vue 3 的企业级后台管理系统

---

## 目录

- [项目整体目录结构](#项目整体目录结构)
- [主要技术框架及版本](#主要技术框架及版本)
- [状态管理方案](#状态管理方案)
- [路由管理](#路由管理)
- [权限控制实现](#权限控制实现)
- [样式处理方案](#样式处理方案)
- [构建配置](#构建配置)

---

## 项目整体目录结构

```
ruoyi-ui-vue3/
├── src/
│   ├── api/                    # API接口模块
│   │   ├── system/            # 系统管理模块API
│   │   │   ├── user.ts       # 用户管理
│   │   │   ├── role.ts       # 角色管理
│   │   │   ├── menu.ts       # 菜单管理
│   │   │   ├── dept.ts       # 部门管理
│   │   │   ├── dict/         # 字典管理
│   │   │   ├── config.ts     # 参数配置
│   │   │   ├── notice.ts    # 通知公告
│   │   │   └── post.ts       # 岗位管理
│   │   ├── monitor/          # 系统监控模块API
│   │   │   ├── online.ts     # 在线用户
│   │   │   ├── operlog.ts   # 操作日志
│   │   │   ├── logininfor.ts # 登录日志
│   │   │   ├── job.ts        # 定时任务
│   │   │   ├── jobLog.ts     # 任务日志
│   │   │   ├── server.ts     # 服务器监控
│   │   │   └── cache.ts      # 缓存监控
│   │   ├── tool/             # 系统工具模块API
│   │   │   └── gen.ts        # 代码生成
│   │   ├── resources/        # 资源管理API
│   │   ├── login.ts          # 登录接口
│   │   └── menu.ts           # 路由菜单接口
│   ├── assets/               # 静态资源
│   │   ├── icons/            # 图标资源
│   │   ├── images/           # 图片资源
│   │   └── styles/           # 样式文件
│   ├── components/            # 公共组件
│   │   ├── Charts/           # 图表组件
│   │   ├── Crontab/          # Cron表达式组件
│   │   ├── Pagination/       # 分页组件
│   │   ├── RightToolbar/     # 右侧工具栏
│   │   ├── Editor/           # 富文本编辑器
│   │   ├── FileUpload/       # 文件上传
│   │   ├── ImageUpload/      # 图片上传
│   │   ├── ImagePreview/     # 图片预览
│   │   ├── SvgIcon/         # SVG图标
│   │   └── DictTag/          # 字典标签
│   ├── layout/               # 布局组件
│   │   ├── main/            # 主布局（后台管理）
│   │   │   ├── index.vue    # 主布局入口
│   │   │   └── components/   # 布局子组件
│   │   │       ├── Navbar.vue      # 顶部导航
│   │   │       ├── Sidebar/        # 侧边栏
│   │   │       ├── TagsView/       # 标签页
│   │   │       ├── AppMain.vue     # 主内容区
│   │   │       ├── TopBar/         # 顶部栏
│   │   │       ├── Settings/       # 设置面板
│   │   │       ├── Copyright/      # 版权信息
│   │   │       └── InnerLink/      # 内链组件
│   │   └── chat/            # 聊天布局（AI助手）
│   │       ├── index.vue
│   │       └── components/
│   ├── plugins/              # 插件模块
│   │   ├── index.ts         # 插件入口
│   │   ├── auth.ts          # 权限判断
│   │   ├── tab.ts           # 页签操作
│   │   ├── cache.ts         # 缓存操作
│   │   ├── modal.ts         # 模态框操作
│   │   └── download.ts      # 下载操作
│   ├── router/              # 路由配置
│   │   ├── index.ts         # 路由入口
│   ├── store/               # 状态管理
│   │   ├── index.ts         # Store入口
│   │   └── modules/         # Store模块
│   │       ├── user.ts      # 用户状态
│   │       ├── permission.ts # 权限状态
│   │       ├── app.ts       # 应用状态
│   │       ├── settings.ts  # 设置状态
│   │       ├── tagsView.ts  # 标签页状态
│   │       ├── dict.ts      # 字典缓存
│   │       ├── system.ts     # 系统配置
│   │       └── chat.ts      # 聊天状态
│   ├── directive/           # 指令模块
│   │   ├── index.ts         # 指令入口
│   │   ├── permission/      # 权限指令
│   │   │   ├── hasRole.ts   # v-hasRole
│   │   │   └── hasPermi.ts  # v-hasPermi
│   │   └── common/          # 通用指令
│   │       └── copyText.ts  # v-copyText
│   ├── types/               # TypeScript类型定义
│   │   ├── api/            # API类型
│   │   │   ├── index.ts    # 通用类型
│   │   │   ├── common.ts   # 通用响应类型
│   │   │   └── system/     # 系统模块类型
│   │   └── global.d.ts     # 全局声明
│   ├── utils/               # 工具函数
│   │   ├── request.ts      # Axios封装
│   │   ├── auth.ts         # Token管理
│   │   ├── ruoyi.ts        # 通用工具
│   │   ├── dict.ts         # 字典工具
│   │   ├── index.ts        # 其他工具
│   │   └── errorCode.ts    # 错误码
│   ├── views/              # 页面视图
│   │   ├── system/         # 系统管理
│   │   │   ├── user/       # 用户管理
│   │   │   ├── role/       # 角色管理
│   │   │   ├── menu/       # 菜单管理
│   │   │   ├── dept/       # 部门管理
│   │   │   ├── dict/       # 字典管理
│   │   │   ├── config/     # 参数管理
│   │   │   └── notice/     # 通知管理
│   │   ├── monitor/        # 系统监控
│   │   │   ├── online/     # 在线用户
│   │   │   ├── operlog/    # 操作日志
│   │   │   ├── logininfor/ # 登录日志
│   │   │   ├── job/        # 定时任务
│   │   │   ├── server/     # 服务器
│   │   │   ├── cache/      # 缓存
│   │   │   └── druid/      # Druid监控
│   │   ├── tool/           # 系统工具
│   │   │   ├── gen/        # 代码生成
│   │   │   └── swagger/    # Swagger文档
│   │   ├── error/          # 错误页面
│   │   ├── widgets/         # 小部件示例
│   │   ├── chat/           # AI聊天页面
│   │   ├── login.vue       # 登录页
│   │   └── index.vue       # 首页
│   ├── App.vue             # 根组件
│   ├── main.ts             # 入口文件
│   ├── permission.ts       # 路由权限守卫
│   └── settings.ts         # 项目配置
├── public/                 # 公共资源
├── index.html              # HTML入口
├── vite.config.ts          # Vite配置
├── tsconfig.json           # TypeScript配置
├── package.json            # 依赖配置
└── .env.*                 # 环境变量
```

---

## 主要技术框架及版本

### 核心框架

| 框架 | 版本 | 用途 |
|------|------|------|
| **Vue** | 3.5.26 | 核心框架 |
| **Vue Router** | 4.6.4 | 路由管理 |
| **Pinia** | 3.0.4 | 状态管理 |
| **TypeScript** | 5.9.3 | 类型系统 |
| **Vite** | 6.4.1 | 构建工具 |

### UI框架与组件库

| 库 | 版本 | 用途 |
|-----|------|------|
| **Element Plus** | 2.13.1 | UI组件库 |
| **@element-plus/icons-vue** | 2.3.2 | Element Plus图标 |

### HTTP请求与工具

| 库 | 版本 | 用途 |
|-----|------|------|
| **Axios** | 1.13.2 | HTTP请求 |
| **@vueuse/core** | 14.1.0 | Vue组合式API工具集 |
| **js-cookie** | 3.0.5 | Cookie操作 |
| **nprogress** | 0.2.0 | 进度条 |

### 编辑器与富文本

| 库 | 版本 | 用途 |
|-----|------|------|
| **@vueup/vue-quill** | 1.2.0 | Quill编辑器 |
| **@wangeditor/editor** | 5.1.23 | 王编辑器 |

### 数据处理与文件

| 库 | 版本 | 用途 |
|-----|------|------|
| **xlsx** | 0.18.5 | Excel处理 |
| **file-saver** | 2.0.5 | 文件下载 |
| **marked** | 17.0.1 | Markdown解析 |
| **js-beautify** | 1.15.4 | 代码格式化 |

### 其他重要依赖

| 库 | 版本 | 用途 |
|-----|------|------|
| **echarts** | 5.6.0 | 图表库 |
| **fuse.js** | 7.1.0 | 模糊搜索 |
| **clipboard** | 2.0.11 | 剪贴板 |
| **jsencrypt** | 3.3.2 | RSA加密 |
| **splitpanes** | 4.0.4 | 分割面板 |
| **vue-cropper** | 1.1.1 | 图片裁剪 |
| **vuedraggable** | 4.1.0 | 拖拽排序 |

### 开发依赖

| 库 | 版本 | 用途 |
|-----|------|------|
| **@vitejs/plugin-vue** | 5.2.4 | Vue插件 |
| **unplugin-auto-import** | 0.18.6 | 自动导入 |
| **vite-plugin-compression** | 0.5.1 | 压缩插件 |
| **vite-plugin-svg-icons** | 2.0.1 | SVG图标插件 |
| **sass-embedded** | 1.97.2 | SCSS编译 |
| **prettier** | 3.8.1 | 代码格式化 |

---

## 状态管理方案

项目采用 **Pinia** 作为状态管理库，结构清晰，模块化设计。

### Store模块划分

| 模块 | 文件 | 职责 |
|------|------|------|
| **user** | `store/modules/user.ts` | 用户登录、Token、角色权限信息 |
| **permission** | `store/modules/permission.ts` | 路由权限、菜单生成 |
| **app** | `store/modules/app.ts` | 侧边栏状态、设备类型、尺寸 |
| **settings** | `store/modules/settings.ts` | 布局设置、主题配置 |
| **tagsView** | `store/modules/tagsView.ts` | 页签历史记录管理 |
| **dict** | `store/modules/dict.ts` | 字典数据缓存 |
| **system** | `store/modules/system.ts` | 系统配置、CAS认证 |
| **chat** | `store/modules/chat.ts` | AI聊天状态 |

### Store初始化入口

```typescript
// src/store/index.ts
const store = createPinia();
export default store;
```

### 使用示例

```typescript
import useUserStore from '@/store/modules/user';
const userStore = useUserStore();

// 访问状态
console.log(userStore.token);
console.log(userStore.roles);
console.log(userStore.permissions);

// 调用Actions
userStore.login({ username, password, code, uuid });
userStore.getInfo();
userStore.logOut();
```

---

## 路由管理

项目使用 **Vue Router 4** 进行路由管理，采用**动态路由**方案。

### 路由配置结构

```typescript
// src/router/index.ts

// 1. 公共路由 - 始终显示
export const constantRoutes = [
  { path: '/login', component: () => import('@/views/login.vue') },
  { path: '/register', component: () => import('@/views/register.vue') },
  { path: '/:pathMatch(.*)*', component: () => import('@/views/error/404.vue') },
  // ...
];

// 2. 动态路由 - 基于权限动态加载
export const dynamicRoutes = [
  { path: '/system/user-auth', permissions: ['system:user:edit'], children: [...] },
  { path: '/system/role-auth', permissions: ['system:role:edit'], children: [...] },
  // ...
];

// 3. 路由创建
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRoutes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) return savedPosition;
    return { top: 0 };
  },
});
```

### 路由元信息 (meta)

```typescript
meta: {
  title: '菜单名称',           // 菜单标题
  icon: 'svg-name',          // 菜单图标
  noCache: true,             // 是否缓存
  breadcrumb: false,         // 是否显示面包屑
  activeMenu: '/system/user', // 高亮菜单
  requireAuth: 'noAuth'      // 是否需要认证
}
```

---

## API请求封装方式

项目基于 **Axios** 封装了统一的请求模块。

### 请求封装核心 (`src/utils/request.ts`)

```typescript
import axios from 'axios';

// 创建Axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 10000,
});

// 请求拦截器
service.interceptors.request.use(config => {
  // 添加Token
  if (getToken()) {
    config.headers['Authorization'] = 'Bearer ' + getToken();
  }
  // 防重复提交处理
  // 参数处理
  return config;
});

// 响应拦截器
service.interceptors.response.use(
  res => {
    const code = res.data.code || 200;
    // 状态码处理：401过期、500错误、601警告等
  },
  error => {
    // 网络错误、超时等处理
  }
);

export default service;
```

### API模块封装示例

```typescript
// src/api/system/user.ts
import request from '@/utils/request';

// 查询用户列表
export function listUser(query: UserQueryParams): Promise<TableDataInfo<SysUser[]>> {
  return request({
    url: '/system/user/list',
    method: 'get',
    params: query,
  });
}

// 新增用户
export function addUser(data: SysUser): Promise<AjaxResult> {
  return request({
    url: '/system/user',
    method: 'post',
    data: data,
  });
}

// 修改用户
export function updateUser(data: SysUser): Promise<AjaxResult> {
  return request({
    url: '/system/user',
    method: 'put',
    data: data,
  });
}

// 删除用户
export function delUser(userId: number | number[]): Promise<AjaxResult> {
  return request({
    url: '/system/user/' + userId,
    method: 'delete',
  });
}
```

### 文件下载封装

```typescript
// 通用下载方法
export function download(url: string, params: any, filename: string, config?: any) {
  return service.post(url, params, {
    transformRequest: [(params) => tansParams(params)],
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    responseType: 'blob',
  }).then(async (data) => {
    const blob = new Blob([data]);
    saveAs(blob, filename);
  });
}
```

---

## 组件组织方式

项目采用**功能模块化**的组件组织方式。

### 组件分类

| 类型 | 目录 | 说明 |
|------|------|------|
| **全局通用组件** | `src/components/` | 可在任何页面使用 |
| **布局组件** | `src/layout/` | 页面整体布局 |
| **业务组件** | `src/views/xxx/components/` | 特定业务模块的组件 |
| **第三方封装** | `src/components/xxx/` | 对第三方库的封装 |

### 全局组件注册 (`src/main.ts`)

```typescript
// 全局组件挂载
app.component('DictTag', DictTag);
app.component('Pagination', Pagination);
app.component('FileUpload', FileUpload);
app.component('ImageUpload', ImageUpload);
app.component('ImagePreview', ImagePreview);
app.component('RightToolbar', RightToolbar);
app.component('Editor', Editor);
```

### 常用全局组件

| 组件 | 路径 | 功能 |
|------|------|------|
| **Pagination** | `components/Pagination/index.vue` | 分页组件 |
| **RightToolbar** | `components/RightToolbar/index.vue` | 表格右侧工具栏 |
| **DictTag** | `components/DictTag/index.vue` | 字典标签显示 |
| **FileUpload** | `components/FileUpload/index.vue` | 文件上传 |
| **ImageUpload** | `components/ImageUpload/index.vue` | 图片上传 |
| **ImagePreview** | `components/ImagePreview/index.vue` | 图片预览 |
| **Editor** | `components/Editor/index.vue` | 富文本编辑器封装 |
| **SvgIcon** | `components/SvgIcon/index.vue` | SVG图标组件 |

### 布局组件导出 (`src/layout/main/components/index.ts`)

```typescript
export { default as Navbar } from './Navbar.vue';
export { default as AppMain } from './AppMain.vue';
export { default as Settings } from './Settings/index.vue';
// 通过 barrel pattern 导出
```

---

## 布局组件结构

项目采用**多布局**设计，支持后台管理和AI助手两种布局。

### 主布局结构 (`layout/main/`)

```
主布局 (index.vue)
├── 侧边栏 (Sidebar)
│   ├── Logo组件
│   ├── 菜单项 (SidebarItem)
│   │   └── 递归子菜单
│   └── Link组件（内链处理）
├── 主体区域
│   ├── 固定头部
│   │   ├── 顶部导航 (Navbar)
│   │   │   ├── 面包屑
│   │   │   ├── 搜索
│   │   │   ├── 尺寸切换
│   │   │   ├── 全屏
│   │   │   ├── 通知
│   │   │   └── 用户信息
│   │   └── 标签页 (TagsView)
│   │       ├── 标签滚动
│   │       └── 标签操作菜单
│   └── 主内容区 (AppMain)
│       └── 路由视图 <router-view />
└── 设置面板 (Settings)
```

### 布局核心代码

```vue
<!-- src/layout/main/index.vue -->
<template>
  <div :class="classObj" class="app-wrapper">
    <!-- 移动端遮罩 -->
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" />
    
    <!-- 侧边栏 -->
    <sidebar v-if="!sidebar.hide" />
    
    <!-- 主内容区 -->
    <div class="main-container">
      <div class="fixed-header">
        <navbar />
        <tags-view v-if="needTagsView" />
      </div>
      <app-main />
      <settings />
    </div>
  </div>
</template>
```

---

## 路由管理---

## 权限控制实现

项目采用**多层次权限控制**体系。

### 权限控制层次

```
┌─────────────────────────────────────┐
│     1. 路由守卫权限 (路由级)        │
│        permission.ts                │
├─────────────────────────────────────┤
│     2. 动态菜单权限 (菜单级)        │
│        后端返回菜单数据             │
├─────────────────────────────────────┤
│     3. 按钮指令权限 (操作级)        │
│        v-hasPermi / v-hasRole      │
├─────────────────────────────────────┤
│     4. 函数级权限 (逻辑级)          │
│        $auth.hasPermi()            │
└─────────────────────────────────────┘
```

### 路由守卫权限控制

```typescript
// src/permission.ts
router.beforeEach((to, from, next) => {
  NProgress.start();
  
  if (getToken()) {
    // 已登录
    if (to.path === '/login') {
      next('/');
    } else {
      // 检查是否已获取用户信息
      if (useUserStore().roles.length === 0) {
        useUserStore().getInfo().then(() => {
          usePermissionStore().generateRoutes().then(accessRoutes => {
            // 动态添加路由
            accessRoutes.forEach(route => {
              router.addRoute(route);
            });
          });
        });
      }
    }
  } else {
    // 白名单直接通过
    if (isWhiteList(to.path)) {
      next();
    } else {
      next(`/login?redirect=${to.fullPath}`);
    }
  }
});
```

### 指令级权限

```typescript
// src/directive/permission/hasPermi.ts
// v-hasPermi="['system:user:add']"

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding;
    const permissions = useUserStore().permissions;
    
    if (value && value instanceof Array) {
      const hasPermissions = permissions.some(permission => {
        return '*:*:*' === permission || value.includes(permission);
      });
      
      if (!hasPermissions) {
        el.parentNode?.removeChild(el);
      }
    }
  },
};

// src/directive/permission/hasRole.ts
// v-hasRole="['admin', 'editor']"

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding;
    const roles = useUserStore().roles;
    
    if (value && value instanceof Array) {
      const hasRole = roles.some(role => {
        return 'admin' === role || value.includes(role);
      });
      
      if (!hasRole) {
        el.parentNode?.removeChild(el);
      }
    }
  },
};
```

### 函数级权限判断

```typescript
// src/plugins/auth.ts
export default {
  // 验证单个权限
  hasPermi(permission: string): boolean { },
  
  // 验证多个权限（只需一个）
  hasPermiOr(permissions: string[]): boolean { },
  
  // 验证多个权限（需要全部）
  hasPermiAnd(permissions: string[]): boolean { },
  
  // 验证单个角色
  hasRole(role: string): boolean { },
  
  // 验证多个角色（只需一个）
  hasRoleOr(roles: string[]): boolean { },
  
  // 验证多个角色（需要全部）
  hasRoleAnd(roles: string[]): boolean { },
};
```

### 权限使用示例

```vue
<!-- 模板中使用指令 -->
<el-button type="primary" @click="handleAdd" v-hasPermi="['system:user:add']">
  新增
</el-button>

<el-button type="danger" @click="handleDelete" v-hasRole="['admin']">
  删除
</el-button>

<!-- 逻辑中判断权限 -->
<script setup>
const { proxy } = getCurrentInstance();

// 函数级权限判断
if (proxy.$auth.hasPermi('system:user:edit')) {
  // 有编辑权限
}
</script>
```

---

## 样式处理方案

项目采用 **SCSS** 作为CSS预处理器，支持多主题和暗黑模式。

### 样式文件结构

```
src/assets/styles/
├── index.scss        # 主样式入口
├── mixin.scss        # 混入（mixins）
├── variables.module.scss  # 变量定义
├── transition.scss   # 过渡动画
├── element-ui.scss   # Element Plus覆盖
├── sidebar.scss      # 侧边栏样式
├── btn.scss          # 按钮样式
├── ruoyi.scss        # 若依通用样式
└── theme-blue.scss   # 蓝色主题
```

### 样式入口

```scss
// src/assets/styles/index.scss
@use './mixin.scss';
@use './transition.scss';
@use './element-ui.scss';
@use './sidebar.scss';
@use './btn.scss';
@use './ruoyi.scss';
@use './theme-blue.scss';
```

### 主题变量定义

```scss
// src/assets/styles/variables.module.scss

// 默认主题（深色）
$menuText: #bfcbd9;
$menuActiveText: #409eff;
$menuBg: #304156;
$menuHover: #263445;

// 浅色主题
$menuLightBg: #ffffff;
$menuLightText: #303133;

// 蓝色主题
$base-menu-blue-color: #f0f0f0;
$base-menu-blue-background: #2774f4;

// 导出变量供JS使用
:export {
  menuText: $menuText;
  menuActiveText: $menuActiveText;
  // ...
}
```

### 暗黑模式支持

```scss
// 暗黑模式变量
html.dark {
  --el-bg-color: #141414;
  --el-text-color-primary: #ffffff;
  --sidebar-bg: #141414;
  // ...
}
```

### 组件样式隔离

项目使用 **CSS Modules** 或 **Scoped CSS** 防止样式污染：

```vue
<!-- 使用 scoped 局部样式 -->
<style scoped>
.component-wrapper {
  // ...
}
</style>

<!-- 使用 CSS Modules -->
<style lang="scss" module>
.className {
  // ...
}
</style>
```

---

## 构建配置

### Vite配置 (`vite.config.ts`)

```typescript
import { defineConfig } from 'vite';
import path from 'path';

export default defineConfig({
  // 基础路径
  base: VITE_APP_ENV === 'production' ? '/toolsPage' : '/toolsPage',
  
  // 别名配置
  resolve: {
    alias: {
      '~': path.resolve(__dirname, './'),
      '@': path.resolve(__dirname, './src'),
    },
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
  },
  
  // 打包配置
  build: {
    sourcemap: false,
    outDir: 'dist',
    chunkSizeWarningLimit: 2000,
    rollupOptions: {
      input: {
        main: path.resolve(__dirname, 'index.html'),
      },
      output: {
        chunkFileNames: 'static/js/[name]-[hash].js',
        manualChunks: {
          'element-plus': ['element-plus'],
          vendor: ['vue', 'vue-router', 'pinia'],
        },
      },
    },
  },
  
  // 开发服务器配置
  server: {
    port: 80,
    host: true,
    proxy: {
      '/dev-api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (p) => p.replace(/^\/dev-api/, ''),
      },
    },
  },
});
```

### 环境变量配置

```bash
# .env.development - 开发环境
VITE_APP_TITLE=工具
VITE_PORT=80
VITE_BASE_URL='toolsPage'
VITE_APP_BASE_API='/dev-api'

# .env.production - 生产环境
VITE_APP_TITLE=工具
VITE_BASE_URL='toolsPage'
VITE_APP_BASE_API='/prod-api'
VITE_BUILD_COMPRESS=gzip
```

### NPM Scripts

```json
{
  "scripts": {
    "dev": "vite",                      // 开发
    "build:prod": "vite build",         // 生产构建
    "build:stage": "vite build --mode staging",  // 测试环境构建
    "preview": "vite preview",          // 预览
    "format": "prettier --write .",     // 格式化
    "type-check": "vue-tsc --noEmit"   // 类型检查
  }
}
```

### Vite插件配置

```typescript
// vite/plugins/index.ts
export default function createVitePlugins(env, isBuild, publicPath) {
  return [
    vue(),
    // 自动导入
    AutoImport({
      imports: ['vue', 'vue-router', 'pinia'],
      resolvers: [],
    }),
    // SVG图标插件
    vitePluginSvgIcons({
      iconDirs: [path.resolve(__dirname, 'src/assets/icons/svg')],
      symbolId: 'icon-[dir]-[name]',
    }),
    // 压缩插件
    viteCompression({
      ext: '.gz',
      algorithm: 'gzip',
      threshold: 10240,
    }),
  ];
}
```

---

## 总结

若依前端是一个功能完善的**Vue3 + Element Plus**企业级后台管理系统，具备以下特点：

| 特性 | 说明 |
|------|------|
| **技术栈** | Vue3 + TypeScript + Vite + Pinia + Element Plus |
| **权限控制** | 多层次：路由守卫 + 菜单级 + 按钮级 + 函数级 |
| **状态管理** | Pinia模块化设计，8个独立Store |
| **API封装** | Axios统一封装，支持防重放、防重复提交 |
| **样式方案** | SCSS + CSS变量 + 暗黑模式 + 多主题 |
| **构建优化** | Vite + 代码分割 + Gzip压缩 |
| **开发体验** | TypeScript类型支持 + Prettier格式化 |
| **多布局支持** | 主后台布局 + AI聊天布局 |
| **多入口构建** | 主应用入口 |

---

## 布局系统总结

项目支持两种不同的布局方案，适用于不同场景：

| 布局 | 路由 | 用途 |
|------|------|------|
| **主布局 (Main)** | `/` + 动态路由 | 后台管理系统（侧边栏+顶部导航+标签页） |
| **聊天布局 (Chat)** | `/chat/*` | AI助手聊天界面（沉浸式对话体验） |

---

*文档最后更新: 2026-04-05*
