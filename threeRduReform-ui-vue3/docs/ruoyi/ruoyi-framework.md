# 若依框架技术文档

> 本文档整理自若依框架 (RuoYi-Vue-3.9.1) 的技术栈和项目结构，便于快速了解和查阅。

---

## 目录

- [若依后端技术文档](./ruoyi-backend.md)
- [若依前端技术文档](./ruoyi-frontend.md)

---

## 项目概览

### 后端技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Spring Boot | 2.5.15 |
| 安全框架 | Spring Security + JWT | 5.7.14 |
| ORM框架 | MyBatis | - |
| 数据库连接池 | Druid | 1.2.27 |
| 分页插件 | PageHelper | 1.4.7 |
| 缓存 | Redis (Spring Data Redis) | - |
| 定时任务 | Quartz | - |
| 文档生成 | Swagger | 3.0.0 |
| Excel处理 | POI | 4.1.2 |
| JDK版本 | Java | 1.8 |

### 前端技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 核心框架 | Vue | 3.5.26 |
| 构建工具 | Vite | 6.4.1 |
| 类型系统 | TypeScript | 5.9.3 |
| UI组件库 | Element Plus | 2.13.1 |
| 状态管理 | Pinia | 3.0.4 |
| 路由管理 | Vue Router | 4.6.4 |
| HTTP请求 | Axios | 1.13.2 |
| CSS预处理器 | SCSS | - |

---

## 模块划分

### 后端模块

```
ruoyi-admin/          # 主应用模块（启动类、Controller）
ruoyi-framework/      # 框架核心模块（安全、配置、AOP）
ruoyi-system/         # 系统管理模块（用户、角色、菜单等）
ruoyi-common/         # 通用工具模块
ruoyi-quartz/         # 定时任务模块
ruoyi-generator/      # 代码生成模块
~~ruoyi-resources/      # 资源管理模块（示例业务模块）~~
```

### 前端目录

```
src/
├── api/              # API接口模块
├── assets/           # 静态资源
├── components/       # 公共组件
├── layout/           # 布局组件
├── plugins/          # 插件模块
├── router/           # 路由配置
├── store/            # 状态管理
├── utils/            # 工具函数
└── views/            # 页面视图
```

---

## 快速链接

- [后端项目结构详解](./ruoyi-backend.md#项目整体目录结构)
- [后端权限管理机制](./ruoyi-backend.md#权限管理机制)
- [后端统一响应格式](./ruoyi-backend.md#统一响应格式和异常处理机制)
- [前端权限控制实现](./ruoyi-frontend.md#权限控制实现)
- [前端状态管理方案](./ruoyi-frontend.md#状态管理方案)
- [前端API请求封装](./ruoyi-frontend.md#api请求封装方式)

---

*文档最后更新: 2026-04-05*
