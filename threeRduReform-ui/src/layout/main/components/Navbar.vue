<template>
  <div class="navbar" :class="'nav' + settingsStore.navType">
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb v-if="settingsStore.navType == 1" id="breadcrumb-container" class="breadcrumb-container" />
    <top-nav v-if="settingsStore.navType == 2" id="topmenu-container" class="topmenu-container" />
    <template v-if="settingsStore.navType == 3">
      <logo v-show="settingsStore.sidebarLogo" :collapse="false"></logo>
      <top-bar id="topbar-container" class="topbar-container" />
    </template>

    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">
        <el-tooltip content="小助手" effect="dark" placement="bottom">
          <div class="right-menu-item right-menu-item-size hover-effect theme-switch-wrapper" @click="handleCommand('enterAssistant')" style="width: auto">
            <svg-icon icon-class="assistant" />
            <span style="font-size: 14px; padding-left: 4px">进入小助手</span>
          </div>
        </el-tooltip>

        <header-search id="header-search" class="right-menu-item right-menu-item-size hover-effect" />

        <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <!-- <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <screenfull id="screenfull" class="right-menu-item right-menu-item-size hover-effect" />

        <el-tooltip content="主题模式" effect="dark" placement="bottom">
          <div class="right-menu-item right-menu-item-size hover-effect theme-switch-wrapper" @click="toggleTheme">
            <svg-icon v-if="settingsStore.isDark" icon-class="sunny" />
            <svg-icon v-if="!settingsStore.isDark" icon-class="moon" />
          </div>
        </el-tooltip>

        <!-- <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip> -->
        <el-tooltip content="布局设置" effect="dark" placement="bottom" v-if="settingsStore.showSettings">
          <div class="right-menu-item right-menu-item-size hover-effect theme-switch-wrapper" @click="handleCommand('setLayout')">
            <svg-icon icon-class="setting" />
          </div>
        </el-tooltip>
      </template>

      <el-dropdown @command="handleCommand" placement="bottom-end" popper-class="profile-dropdown-popper" class="avatar-container" trigger="click">
        <div class="avatar-wrapper right-menu-item hover-effect">
          <img :src="userStore.avatar" class="user-avatar" />
          <span class="user-nickname">{{ userStore.nickName }}</span>
        </div>
        <template #dropdown>
          <div class="profile-dropdown">
            <!-- 用户信息区块 -->
            <div class="profile-header">
              <img :src="userStore.avatar" class="profile-avatar" />
              <div class="profile-info">
                <span class="profile-name">{{ userStore.nickName }}</span>
                <span class="profile-email">{{ userStore.email || 'user@example.com' }}</span>
              </div>
            </div>

            <!-- 分割线 -->
            <el-divider class="dropdown-divider" />

            <!-- 功能菜单列表 -->
            <el-dropdown-menu class="profile-menu">
              <router-link to="/user/profile">
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  <span>个人中心</span>
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item command="docs">
                <el-icon><Document /></el-icon>
                <span>使用文档</span>
              </el-dropdown-item>
              <el-dropdown-item command="github">
                <el-icon><Platform /></el-icon>
                <span>Github</span>
              </el-dropdown-item>
            </el-dropdown-menu>

            <!-- 分割线 -->
            <el-divider class="dropdown-divider" />

            <!-- 退出登录按钮 -->
            <div class="profile-footer">
              <el-dropdown-item command="logout" class="logout-btn">
                <el-icon><SwitchButton /></el-icon>
                <span>退出登录</span>
              </el-dropdown-item>
            </div>
          </div>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ElMessageBox, ElDivider } from 'element-plus';
  import { User, Document, Platform, SwitchButton } from '@element-plus/icons-vue';
  import Breadcrumb from '@/components/Breadcrumb/index.vue';
  import TopNav from '@/components/TopNav/index.vue';
  import TopBar from './TopBar/index.vue';
  import Logo from './Sidebar/Logo.vue';
  import Hamburger from '@/components/Hamburger/index.vue';
  import Screenfull from '@/components/Screenfull/index.vue';
  import SizeSelect from '@/components/SizeSelect/index.vue';
  import HeaderSearch from '@/components/HeaderSearch/index.vue';
  import RuoYiGit from '@/components/RuoYi/Git/index.vue';
  import RuoYiDoc from '@/components/RuoYi/Doc/index.vue';
  import useAppStore from '@/store/modules/app';
  import useUserStore from '@/store/modules/user';
  import useSettingsStore from '@/store/modules/settings';
  import useSystemStore from '@/store/modules/system';

  const appStore = useAppStore();
  const userStore = useUserStore();
  const settingsStore = useSettingsStore();

  function toggleSideBar(): void {
    appStore.toggleSideBar();
  }

  function handleCommand(command: string): void {
    switch (command) {
      case 'setLayout':
        setLayout();
        break;
      case 'logout':
        logout();
        break;
      case 'enterAssistant':
        enterAssistant();
        break;
      default:
        break;
    }
  }

  function logout(): void {
    ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(() => {
        userStore.logOut().then(() => {
          goToLogin();
        });
      })
      .catch(() => {});
  }

  function enterAssistant(): void {
    const BASE_URL = import.meta.env.BASE_URL;
    window.open(`${BASE_URL}/chat/sports`, '_blank');
  }

  // 跳转至登录页
  function goToLogin() {
    const currentPath = encodeURIComponent('/');
    if (!useSystemStore().casEnable) {
      const BASE_URL = import.meta.env.BASE_URL;
      console.log('BASE_URL', BASE_URL);
      location.href = `${BASE_URL}/login?redirect=` + currentPath;
    } else {
      // CAS登录场景使用当前页面重定向
      const redirectUrl = encodeURIComponent(window.location.href);
      const casLoginUrl = `${useSystemStore().casServerUrl}/login?service=${redirectUrl}`;
      window.location.href = casLoginUrl;
    }
  }

  const emits = defineEmits(['setLayout']);
  function setLayout(): void {
    emits('setLayout');
  }

  async function toggleTheme(event?: MouseEvent): Promise<void> {
    const x = event?.clientX || window.innerWidth / 2;
    const y = event?.clientY || window.innerHeight / 2;
    const wasDark = settingsStore.isDark;

    const isReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
    const isSupported = typeof (document as any).startViewTransition === 'function' && !isReducedMotion;

    if (!isSupported) {
      settingsStore.toggleTheme();
      return;
    }

    try {
      const transition = document.startViewTransition(async () => {
        await new Promise((resolve) => setTimeout(resolve, 10));
        settingsStore.toggleTheme();
        await nextTick();
      });
      await transition.ready;

      const endRadius = Math.hypot(Math.max(x, window.innerWidth - x), Math.max(y, window.innerHeight - y));
      const clipPath = [`circle(0px at ${x}px ${y}px)`, `circle(${endRadius}px at ${x}px ${y}px)`];
      document.documentElement.animate(
        {
          clipPath: !wasDark ? [...clipPath].reverse() : clipPath,
        },
        {
          duration: 650,
          easing: 'cubic-bezier(0.4, 0, 0.2, 1)',
          fill: 'forwards',
          pseudoElement: !wasDark ? '::view-transition-old(root)' : '::view-transition-new(root)',
        },
      );
      await transition.finished;
    } catch (error) {
      console.warn('View transition failed, falling back to immediate toggle:', error);
      settingsStore.toggleTheme();
    }
  }
</script>

<style lang="scss" scoped>
  .navbar.nav3 {
    .hamburger-container {
      display: none !important;
    }
  }

  .navbar {
    height: 50px;
    overflow: hidden;
    position: relative;
    background: var(--navbar-bg);
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    display: flex;
    align-items: center;
    // padding: 0 8px;
    box-sizing: border-box;

    .hamburger-container {
      line-height: 46px;
      height: 100%;
      cursor: pointer;
      transition: background 0.3s;
      -webkit-tap-highlight-color: transparent;
      display: flex;
      align-items: center;
      flex-shrink: 0;
      margin-right: 8px;

      &:hover {
        background: rgba(0, 0, 0, 0.025);
      }
    }

    .breadcrumb-container {
      flex-shrink: 0;
    }

    .topmenu-container {
      position: absolute;
      left: 50px;
    }

    .topbar-container {
      flex: 1;
      min-width: 0;
      display: flex;
      align-items: center;
      overflow: hidden;
      margin-left: 8px;
    }

    .errLog-container {
      display: inline-block;
      vertical-align: top;
    }

    .right-menu {
      height: 100%;
      line-height: 50px;
      display: flex;
      align-items: center;
      margin-left: auto;
      gap: 10px;

      &:focus {
        outline: none;
      }

      .right-menu-item {
        display: inline-block;
        padding: 0 8px;
        height: 100%;
        font-size: 18px;
        color: #5a5e66;
        vertical-align: text-bottom;

        &.hover-effect {
          cursor: pointer;
          transition: background 0.3s;

          &:hover {
            background: rgba(0, 0, 0, 0.025);
          }
        }

        &.theme-switch-wrapper {
          display: flex;
          align-items: center;

          svg {
            transition: transform 0.3s;

            &:hover {
              transform: scale(1.15);
            }
          }
        }
      }

      .right-menu-item-size {
        width: 34px;
        height: 34px;
        line-height: 34px;
      }

      .avatar-container {
        margin-right: 16px;

        .avatar-wrapper {
          position: relative;
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 4px 8px;

          .user-avatar {
            cursor: pointer;
            width: 30px;
            height: 30px;
            border-radius: 50%;
          }

          .user-nickname {
            position: relative;
            font-size: 14px;
            font-weight: bold;
          }

          i {
            cursor: pointer;
            position: absolute;
            right: -20px;
            top: 25px;
            font-size: 12px;
          }
        }
      }
    }
  }
</style>

<style lang="scss">
  .profile-dropdown-popper {
    border-radius: 8px;

    .profile-dropdown {
      width: 240px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);

      .profile-header {
        display: flex;
        align-items: center;
        padding: 12px;

        .profile-avatar {
          width: 48px;
          height: 48px;
          border-radius: 50%;
          margin-right: 12px;
          border: 2px solid #f0f2f5;
        }

        .profile-info {
          display: flex;
          flex-direction: column;

          .profile-name {
            font-size: 15px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }

          .profile-email {
            font-size: 12px;
            color: #909399;
          }
        }
      }

      .dropdown-divider {
        margin: 4px 0;
      }

      .profile-menu,
      .profile-footer {
        .el-dropdown-menu__item {
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 10px 12px;
          border-radius: 8px;
          margin: 0px 4px;
          // color: #606266;
          font-size: 14px;
          transition: all 0.2s;
          .el-icon {
            font-size: 16px;
          }
        }
      }

      .profile-footer {
        padding-bottom: 4px;
      }
    }
  }
</style>
