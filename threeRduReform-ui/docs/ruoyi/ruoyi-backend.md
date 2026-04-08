# 若依后端技术文档

> 若依后端 (RuoYi-Vue-3.9.1) 基于 Spring Boot 的企业级开发框架

---

## 目录

- [项目整体目录结构](#项目整体目录结构)
- [主要技术框架版本](#主要技术框架版本)
- [模块划分方式](#模块划分方式)
- [核心配置文件](#核心配置文件)
- [各层组织方式](#各层组织方式)
- [统一响应格式和异常处理机制](#统一响应格式和异常处理机制)
- [权限管理机制](#权限管理机制)
- [工具类和其他常用组件](#工具类和其他常用组件)

---

## 项目整体目录结构

```
e:\Code\tools\RuoYi-Vue-3.9.1/
├── pom.xml                      # Maven父工程配置文件
├── ruoyi-admin/                 # 主应用模块（启动类、Controller）
├── ruoyi-framework/             # 框架核心模块（安全、配置、AOP）
├── ruoyi-system/                # 系统管理模块（用户、角色、菜单等）
├── ruoyi-quartz/                # 定时任务模块
├── ruoyi-generator/              # 代码生成模块
└── ruoyi-common/                 # 通用工具模块
```

---

## 主要技术框架版本

| 技术组件 | 版本号 | 说明 |
|---------|--------|------|
| **Spring Boot** | 2.5.15 | 核心框架 |
| **Spring Framework** | 5.3.39 | 底层框架 |
| **Spring Security** | 5.7.14 | 安全认证框架 |
| **MyBatis** | 内置版本 | ORM框架 |
| **PageHelper** | 1.4.7 | 分页插件 |
| **Druid** | 1.2.27 | 数据库连接池 |
| **JWT (jjwt)** | 0.9.1 | Token令牌 |
| **MySQL** | - | 数据库 |
| **Redis** | - | 缓存（Spring Data Redis） |
| **Swagger** | 3.0.0 | API文档 |
| **Fastjson2** | 2.0.60 | JSON处理 |
| **POI** | 4.1.2 | Excel操作 |
| **Kaptcha** | 2.3.3 | 验证码 |
| **Quartz** | - | 定时任务 |
| **Velocity** | 2.3 | 代码生成模板引擎 |
| **Logback** | 1.2.13 | 日志框架 |
| **Java** | 1.8 | JDK版本 |

---

## 模块划分方式

项目采用**多模块Maven项目**架构，按职责分层：

| 模块名称 | 职责 | 依赖关系 |
|---------|------|---------|
| `ruoyi-common` | 通用工具类、注解、异常、基类 | 被所有模块依赖 |
| `ruoyi-framework` | 核心框架配置、安全认证、AOP | 依赖common |
| `ruoyi-system` | 系统管理业务逻辑 | 依赖common、framework |
| `ruoyi-admin` | Web层（Controller）、启动类 | 依赖所有模块 |
| `ruoyi-quartz` | 定时任务管理 | 依赖common |
| `ruoyi-generator` | 代码生成器 | 依赖common |
| `ruoyi-resources` | ~~示例业务模块~~ | ~~依赖common、system~~ |
| ~~`ruoyi-resources`~~ | ~~示例业务模块~~ | ~~依赖common、system~~ |

---

## 核心配置文件

### 主配置文件

| 文件 | 位置 | 说明 |
|------|------|------|
| `application.yml` | ruoyi-admin/src/main/resources/ | 主配置 |
| `application-druid.yml` | ruoyi-admin/src/main/resources/ | 数据源配置 |
| `mybatis-config.xml` | ruoyi-admin/src/main/resources/mybatis/ | MyBatis全局配置 |
| `logback.xml` | ruoyi-admin/src/main/resources/ | 日志配置 |

### application.yml 关键配置

```yaml
# 服务配置
server:
  port: 8080
  servlet:
    context-path: /

# Token配置
token:
  header: Authorization
  secret: abcdefghijklmnopqrstuvwxyz
  expireTime: 30  # 30分钟

# MyBatis配置
mybatis:
  typeAliasesPackage: com.ruoyi.**.domain
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  configLocation: classpath:mybatis/mybatis-config.xml

# Redis配置
spring:
  redis:
    host: localhost
    port: 6379
    database: 0

# 分页插件
pagehelper:
  helperDialect: mysql
  supportMethodsArguments: true
```

---

## 各层组织方式

### Controller层 (`ruoyi-admin`)

```
ruoyi-admin/src/main/java/com/ruoyi/web/controller/
├── system/          # 系统管理（用户、角色、菜单、部门等）
├── monitor/         # 系统监控（在线用户、服务信息、日志）
├── tool/            # 工具类（代码生成等）
└── common/          # 通用（验证码、公共上传等）
```

**示例 Controller 路径**：

- `com.ruoyi.web.controller.system.SysUserController` - 用户管理
- `com.ruoyi.web.controller.system.SysRoleController` - 角色管理
- `com.ruoyi.web.controller.system.SysMenuController` - 菜单管理
- `com.ruoyi.quartz.controller.SysJobController` - 定时任务

### Service层 (`ruoyi-system`)

```
ruoyi-system/src/main/java/com/ruoyi/system/service/
├── ISysUserService.java        # 接口
├── impl/SysUserServiceImpl.java # 实现
└── ...
```

### Mapper层

**Java Mapper接口**：

```
ruoyi-system/src/main/java/com/ruoyi/system/mapper/
├── SysUserMapper.java
├── SysRoleMapper.java
├── SysMenuMapper.java
└── ...
```

**MyBatis XML映射文件**：

```
ruoyi-system/src/main/resources/mapper/system/
├── SysUserMapper.xml
├── SysRoleMapper.xml
├── SysMenuMapper.xml
└── ...
```

### Entity/Domain层

```
ruoyi-common/src/main/java/com/ruoyi/common/core/domain/entity/
├── SysUser.java      # 用户实体
├── SysRole.java      # 角色实体
├── SysMenu.java      # 菜单实体
├── SysDept.java      # 部门实体
├── SysDictType.java  # 字典类型
├── SysDictData.java   # 字典数据
└── ...

ruoyi-common/src/main/java/com/ruoyi/common/core/domain/
├── BaseEntity.java    # 实体基类
├── AjaxResult.java   # 统一响应
├── model/LoginUser.java  # 登录用户
└── page/TableDataInfo.java  # 分页数据
```

---

## 统一响应格式和异常处理机制

### 统一响应格式 - AjaxResult

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/core/domain/AjaxResult.java`

**响应结构**：

```json
{
  "code": 200,      // 状态码
  "msg": "操作成功", // 消息
  "data": {...}     // 数据（可选）
}
```

**常用方法**：

- `AjaxResult.success()` - 成功响应
- `AjaxResult.error()` - 错误响应
- `AjaxResult.warn()` - 警告响应

### HTTP状态码定义

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/constant/HttpStatus.java`

| 状态码 | 含义 |
|-------|------|
| 200 | 操作成功 |
| 401 | 未授权 |
| 403 | 禁止访问（无权限） |
| 404 | 资源未找到 |
| 500 | 系统内部错误 |
| 601 | 系统警告消息 |

### 全局异常处理器

**文件位置**：`ruoyi-framework/src/main/java/com/ruoyi/framework/web/exception/GlobalExceptionHandler.java`

处理的异常类型：

- `AccessDeniedException` - 权限校验失败
- `ServiceException` - 业务异常
- `MethodArgumentNotValidException` - 参数校验失败
- `BindException` - 绑定异常
- `DemoModeException` - 演示模式异常

### 分页响应格式

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/core/page/TableDataInfo.java`

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [...],      // 数据列表
  "total": 100        // 总记录数
}
```

---

## 权限管理机制

### 安全架构概览

基于 **Spring Security + JWT** 的无状态认证方案：

1. **登录流程**：用户名密码 -> 验证 -> 生成JWT Token -> 返回给前端
2. **请求认证**：携带Token -> Filter解析 -> 验证Token -> 设置SecurityContext
3. **权限校验**：@PreAuthorize注解 -> 权限表达式 -> 校验权限

### 核心组件

| 组件 | 位置 | 职责 |
|-----|------|------|
| `JwtAuthenticationTokenFilter` | framework/security/filter/ | Token验证过滤器 |
| `TokenService` | framework/web/service/ | Token管理服务 |
| `UserDetailsServiceImpl` | framework/web/service/ | 用户认证服务 |
| `SysPermissionService` | framework/web/service/ | 权限管理服务 |
| `SecurityConfig` | framework/config/ | Spring Security配置 |

### 权限控制注解

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/annotation/`

| 注解 | 用途 |
|-----|------|
| `@PreAuthorize("@ss.hasPermi('xxx')")` | 权限校验 |
| `@PreAuthorize("@ss.hasRole('xxx')")` | 角色校验 |
| `@Log(title="模块名", businessType=BusinessType.INSERT/UPDATE/DELETE/...)` | 操作日志 |

### 数据权限控制

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/annotation/DataScope.java`

支持5种数据范围：

1. **所有数据权限** - 可以查看所有数据
2. **自定义数据权限** - 只能查看关联部门数据
3. **本部门数据权限** - 只能查看本部门数据
4. **本部门及以下数据权限** - 只能查看本部门及下级部门数据
5. **仅本人数据权限** - 只能查看自己的数据

### 登录用户信息

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/core/domain/model/LoginUser.java`

```java
public class LoginUser implements UserDetails {
    private Long userId;           // 用户ID
    private Long deptId;          // 部门ID
    private String token;          // 令牌
    private Set<String> permissions; // 权限集合
    private SysUser user;          // 用户信息
    // ... 其他属性
}
```

### 工具类获取当前用户

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/utils/SecurityUtils.java`

```java
SecurityUtils.getUserId()      // 获取用户ID
SecurityUtils.getUsername()    // 获取用户名
SecurityUtils.getDeptId()      // 获取部门ID
SecurityUtils.getLoginUser()   // 获取登录用户
SecurityUtils.hasPermi("xxx")  // 权限校验
SecurityUtils.hasRole("xxx")   // 角色校验
```

---

## 工具类和其他常用组件

### 通用工具类 (`ruoyi-common/utils/`)

| 工具类 | 路径 | 功能 |
|-------|------|------|
| StringUtils | common/utils/StringUtils.java | 字符串处理 |
| DateUtils | common/utils/DateUtils.java | 日期处理 |
| ServletUtils | common/utils/ServletUtils.java | Servlet工具 |
| SecurityUtils | common/utils/SecurityUtils.java | 安全相关 |
| IdUtils | common/utils/uuid/IdUtils.java | ID生成 |
| Md5Utils | common/utils/sign/Md5Utils.java | MD5加密 |
| Base64 | common/utils/sign/Base64.java | Base64编解码 |

### 文件处理工具 (`ruoyi-common/utils/file/`)

| 工具类 | 功能 |
|-------|------|
| FileUploadUtils | 文件上传 |
| FileUtils | 文件操作 |
| FileTypeUtils | 文件类型判断 |
| ImageUtils | 图片处理 |

### Excel处理 (`ruoyi-common/utils/poi/`)

- `ExcelUtil<T>` - Excel导入导出工具

### Redis缓存 (`ruoyi-common/core/redis/`)

**文件位置**：`ruoyi-common/src/main/java/com/ruoyi/common/core/redis/RedisCache.java`

```java
redisCache.setCacheObject(key, value);              // 缓存对象
redisCache.getCacheObject(key);                      // 获取对象
redisCache.setCacheList(key, list);                 // 缓存List
redisCache.setCacheSet(key, set);                   // 缓存Set
redisCache.setCacheMap(key, map);                   // 缓存Map
redisCache.expire(key, timeout);                    // 设置过期
```

### 自定义注解 (`ruoyi-common/annotation/`)

| 注解 | 用途 |
|-----|------|
| `@Log` | 操作日志记录 |
| `@RepeatSubmit` | 防重复提交 |
| `@RateLimiter` | 接口限流 |
| `@DataScope` | 数据权限 |
| `@DataSource` | 多数据源切换 |
| `@Sensitive` | 数据脱敏 |
| `@Excel` | Excel导入导出 |
| `@Anonymous` | 匿名访问 |

### 业务异常 (`ruoyi-common/exception/`)

```
exception/
├── base/BaseException.java          # 基础异常
├── ServiceException.java            # 业务异常
├── DemoModeException.java           # 演示模式异常
├── user/                            # 用户相关异常
│   ├── UserException.java
│   ├── CaptchaException.java
│   ├── UserPasswordNotMatchException.java
│   └── ...
└── file/                            # 文件相关异常
```

### 定时任务 (`ruoyi-quartz/`)

**核心文件**：

- `controller/SysJobController.java` - 定时任务Controller
- `service/ISysJobService.java` - 定时任务服务接口
- `util/ScheduleUtils.java` - 调度工具类
- `util/CronUtils.java` - Cron表达式工具

**任务示例**：`ruoyi-quartz/src/main/java/com/ruoyi/quartz/task/RyTask.java`

---

## 代码分层规范总结

```
请求流程：
┌─────────────────────────────────────────────────────────────┐
│                        Client                                │
└───────────────────────────┬─────────────────────────────────┘
                            │ HTTP Request + JWT Token
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  Filter: JwtAuthenticationTokenFilter                        │
│  (Token解析、SecurityContext设置)                            │
└───────────────────────────┬─────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  Controller层 (ruoyi-admin)                                  │
│  @RestController                                            │
│  @RequestMapping("/system/user")                              │
│  @PreAuthorize("@ss.hasPermi('system:user:list')")          │
└───────────────────────────┬─────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  Service层 (ruoyi-system)                                    │
│  ISysXxxService (接口)                                      │
│  SysXxxServiceImpl (实现)                                   │
└───────────────────────────┬─────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  Mapper层 (ruoyi-system)                                     │
│  SysXxxMapper (MyBatis接口)                                 │
│  SysXxxMapper.xml (SQL映射)                                 │
└───────────────────────────┬─────────────────────────────────┘
                            ▼
┌─────────────────────────────────────────────────────────────┐
│  Database (MySQL + Druid连接池)                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 数据库表前缀

| 前缀 | 说明 |
|-----|------|
| `sys_` | 系统管理相关表 |
| `gen_` | 代码生成相关表 |
| `qrtz_` | 定时任务相关表 |

---

*文档最后更新: 2026-04-05*
