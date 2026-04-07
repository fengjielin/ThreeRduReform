<template>
  <div class="navbar" :class="['nav' + settingsStore.navType, { mobile: appStore.device === 'mobile' }]">
    <!-- Logo区域 - 优化显示逻辑 -->
    <!-- <logo class="logo-container" :collapse="false" v-if="isCollapse" @click="toggleSideBar" :style="{ cursor: 'pointer' }" /> -->

    <!-- 汉堡菜单 - 优化点击体验 -->
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <!-- 右侧菜单区域 - 优化结构 -->
    <div class="right-menu">
      <div class="right-menu-item-group">
        <!-- 清除对话按钮 - 优化样式 -->
        <!-- <el-tooltip content="清除对话内容" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect">
            <el-button type="text" class="clear-btn" icon="Delete" @click="handleCommand('clearConversation')" size="default">
              <span class="btn-text" v-if="appStore.device !== 'mobile'">清除对话</span>
            </el-button>
          </div>
        </el-tooltip> -->
      </div>

      <!-- 登录/头像区域 - 优化交互 -->
      <el-dropdown v-if="isLogged" @command="handleCommand" class="avatar-container" trigger="hover" placement="bottom-end">
        <div class="avatar-wrapper right-menu-item hover-effect">
          <img :src="userStore.avatar || DEFAULT_AVATAR" class="user-avatar" @error="handleAvatarError" alt="用户头像" />
          <span class="user-nickname" v-if="appStore.device !== 'mobile'">{{ userStore.nickName || '未知用户' }}</span>
          <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu :show-arrow="true">
            <el-dropdown-item command="enterBackend" v-if="isAdmin">
              <el-icon><Setting /></el-icon>
              <span>进入后台</span>
            </el-dropdown-item>
            <el-dropdown-item command="resetPassword">
              <el-icon><Key /></el-icon>
              <span>重置密码</span>
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <el-button v-else type="primary" class="login-btn" @click="goToLogin" :icon="User" size="default">登录</el-button>
    </div>

    <resetPwd ref="resetPwdRef" />
  </div>
</template>

<script setup lang="ts">
  import { useRouter } from 'vue-router';
  import { ElMessageBox, ElTooltip } from 'element-plus';
  import { ArrowDown, Setting, Key, SwitchButton, User } from '@element-plus/icons-vue'; // 引入Element图标

  // 组件导入
  import Hamburger from '@/components/Hamburger/index.vue';
  import Logo from '@/layout/chat/components/Sidebar/Logo.vue';

  // Store导入
  import useAppStore from '@/store/modules/app';
  import useUserStore from '@/store/modules/user';
  import useSettingsStore from '@/store/modules/settings';
  import useSystemStore from '@/store/modules/system';
  import usePermissionStore from '@/store/modules/permission';
  import useChatStore from '@/store/modules/chat';

  // 组件导入
  import resetPwd from '@/layout/chat/resetPwd.vue';

  // 工具函数
  import { getFirstPath } from '@/utils/index';

  // Store实例化
  const appStore = useAppStore();
  const userStore = useUserStore();
  const settingsStore = useSettingsStore();
  const chatStore = useChatStore();
  const permissionStore = usePermissionStore();
  const systemStore = useSystemStore(); // 修正：使用systemStore而非直接调用
  const router = useRouter();

  // 获取组件实例
  const { proxy } = getCurrentInstance() as { proxy: any };

  // 引用
  const resetPwdRef = ref<InstanceType<typeof resetPwd> | null>(null);

  // 常量定义
  const DEFAULT_AVATAR = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

  // 头像加载错误处理
  function handleAvatarError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.src = DEFAULT_AVATAR;
  }

  // 计算属性
  const isCollapse = computed(() => !appStore.sidebar.opened);
  const isLogged = computed(() => !!userStore.token);

  // 侧边栏切换
  function toggleSideBar() {
    appStore.toggleSideBar();
  }

  // 命令处理
  function handleCommand(command: string) {
    switch (command) {
      case 'logout':
        logout();
        break;
      case 'resetPassword':
        resetPwdRef.value?.show(); // 可选链操作，避免报错
        break;
      case 'enterBackend':
        goToBackend();
        break;
      case 'switchStudentId':
        chatStore.triggerSwitchStudentId();
        break;
      case 'exportConversation':
        chatStore.triggerExportConversation();
        break;
      case 'clearConversation':
        chatStore.triggerClearConversation();
        break;
      default:
        break;
    }
  }

  // 退出登录
  function logout() {
    ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'logout-confirm-btn',
    })
      .then(() => {
        userStore.logOut().then(() => {
          window.location.reload();
        });
      })
      .catch(() => {
        // 取消操作，无提示
      });
  }

  // 跳转登录页
  function goToLogin() {
    const currentPath = encodeURIComponent('/chat/sports');
    if (!systemStore.casEnable) {
      location.href = `/threeRduReformPage/login?redirect=${currentPath}`;
    } else {
      const redirectUrl = encodeURIComponent(window.location.href);
      const casLoginUrl = `${systemStore.casServerUrl}/login?service=${redirectUrl}`;
      window.location.href = casLoginUrl;
    }
  }

  // 跳转后台
  function goToBackend() {
    const backupRoles = [...userStore.roles];
    try {
      userStore.roles = [];
      router.push({ path: '/' });
    } catch (error) {
      proxy.$modal?.msgError('进入后台失败');
      userStore.roles = backupRoles;
    }
  }
</script>

<style lang="scss" scoped>
  // 全局变量定义
  :deep(:root) {
    --navbar-height: 56px;
    --navbar-gradient: linear-gradient(90deg, #63a8ff, #4080ff);
    --navbar-hover-bg: rgba(255, 255, 255, 0.1);
    --navbar-active-bg: rgba(255, 255, 255, 0.15);
    --text-primary: #ffffff;
    --text-secondary: rgba(255, 255, 255, 0.9);
    --text-tertiary: rgba(255, 255, 255, 0.7);
    --border-radius-sm: 4px;
    --border-radius-md: 8px;
    --border-radius-lg: 12px;
    --shadow-sm: 0 2px 8px rgba(64, 128, 255, 0.2);
    --transition-default: all 0.2s ease-in-out;
  }

  // 导航栏主样式
  .navbar {
    height: var(--navbar-height);
    overflow: hidden;
    position: relative;
    background: var(--navbar-gradient);
    box-shadow: var(--shadow-sm);
    display: flex;
    align-items: center;
    padding: 0 16px;
    box-sizing: border-box;
    -webkit-background-clip: padding-box;
    background-clip: padding-box;

    // 汉堡菜单容器
    .hamburger-container {
      cursor: pointer;
      transition: var(--transition-default);
      -webkit-tap-highlight-color: transparent;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      margin-right: 12px;
      border-radius: var(--border-radius-md);

      &:hover {
        background: var(--navbar-hover-bg);
      }
    }

    // Logo容器
    .logo-container {
      padding: 0 12px;
      cursor: pointer;
      &.sidebar-logo-container {
        background: transparent;
      }
    }

    // 右侧菜单区域
    .right-menu {
      height: 100%;
      display: flex;
      align-items: center;
      margin-left: auto;
      gap: 8px;
      padding: 0 4px;

      &:focus {
        outline: none;
      }

      // 菜单项分组
      .right-menu-item-group {
        height: 100%;
        display: flex;
        align-items: center;
        gap: 4px;
      }

      // 基础菜单项
      .right-menu-item {
        display: inline-flex;
        align-items: center;
        justify-content: center;
        padding: 0 10px;
        height: 40px;
        font-size: 16px;
        color: var(--text-secondary);
        border-radius: var(--border-radius-md);
        transition: var(--transition-default);

        &.hover-effect {
          cursor: pointer;

          &:hover {
            background: var(--navbar-hover-bg);
            color: var(--text-primary);
          }
        }
      }

      // 头像容器
      .avatar-container {
        margin-right: 8px;
        margin-top: 8px;

        .avatar-wrapper {
          position: relative;
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 4px 10px;
          border-radius: var(--border-radius-md);
          cursor: pointer;
          transition: var(--transition-default);

          &:hover {
            background: var(--navbar-hover-bg);
          }

          // 用户头像
          .user-avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            border: 2px solid rgba(255, 255, 255, 0.2);
            object-fit: cover;
          }

          // 姓名
          .user-nickname {
            font-size: 14px;
            font-weight: 500;
            color: var(--text-primary);
            white-space: nowrap;
          }

          // 下拉图标
          .dropdown-icon {
            color: var(--text-tertiary);
            font-size: 14px;
            margin-left: 4px;
            transition: var(--transition-default);

            &:hover {
              color: var(--text-primary);
            }
          }
        }
      }

      // 登录按钮
      .login-btn {
        height: 32px;
        padding: 8px 16px;
        border: 1px solid #e5e9f2;
        border-radius: 20px;
        background: white;
        margin-top: 8px;
        color: var(--text-primary);
        font-weight: 500;
        transition: var(--transition-default);

        &:hover {
          background: rgba(255, 255, 255, 0.3);
        }
      }

      // Element按钮样式穿透
      :deep(.el-button) {
        &.clear-btn {
          color: var(--text-secondary);
          border: 1px solid #e5e9f2;
          border-radius: 20px;
          background: white;
          height: 32px;
          padding: 0 10px;
          margin-top: 8px;
          &:hover {
            color: var(--text-primary);
            background: var(--navbar-hover-bg);
          }
        }

        &.el-button--primary {
          --el-button-text-color: #fff;
          --el-button-bg-color: rgba(255, 255, 255, 0.2);
          --el-button-border-color: rgba(255, 255, 255, 0.3);
          --el-button-hover-text-color: #fff;
          --el-button-hover-bg-color: rgba(255, 255, 255, 0.3);
          --el-button-hover-border-color: rgba(255, 255, 255, 0.4);
        }
      }

      // 下拉菜单样式穿透
      :deep(.el-dropdown-menu) {
        border-radius: var(--border-radius-md);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        border: 1px solid rgba(255, 255, 255, 0.1);
        background: #fff;

        .el-dropdown-item {
          padding: 8px 16px;
          font-size: 14px;
          display: flex;
          align-items: center;
          gap: 8px;

          &:hover {
            background: #f5f7fa;
          }

          &.is-divided {
            border-top: 1px solid #f0f2f5;
          }
        }
      }
    }

    // 按钮文字辅助样式
    .btn-text {
      padding-left: 6px;
      font-size: 14px;
      color: var(--text-primary);
    }
  }

  // 移动端适配
  .mobile {
    .navbar {
      padding: 0 8px;
    }

    .avatar-container {
      .avatar-wrapper {
        .user-nickname {
          display: none;
        }
      }
    }

    .right-menu-item {
      padding: 0 6px;
    }

    .btn-text {
      display: none;
    }
  }

  // 响应式适配
  @media (max-width: 768px) {
    .navbar {
      padding: 0 12px;
    }

    .right-menu {
      gap: 4px;
    }

    .login-btn {
      padding: 0 12px !important;
      font-size: 13px !important;
    }
  }
</style>
