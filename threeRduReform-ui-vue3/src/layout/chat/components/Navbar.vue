<template>
  <div class="navbar" :class="'nav' + settingsStore.navType">
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <div class="right-menu">
      <div class="right-menu-item-group">
        <el-tooltip content="切换学号" effect="dark" placement="bottom" v-if="isAdmin || isTeacher">
          <div class="right-menu-item hover-effect">
            <el-button type="primary" class="student-id-btn" @click="handleCommand('switchStudentId')">
              <span class="fas fa-user"></span>
              <span class="btn-text">{{ currentStudentId || '设置学号' }}</span>
            </el-button>
          </div>
        </el-tooltip>
        <el-tooltip content="导出对话记录" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect">
            <el-button type="primary" class="export-btn" @click="handleCommand('exportConversation')">
              <span class="fas fa-download"></span>
              <span class="btn-text" v-if="appStore.device !== 'mobile'">导出对话</span>
            </el-button>
          </div>
        </el-tooltip>
        <el-tooltip content="清除对话内容" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect">
            <el-button type="primary" class="clear-btn" @click="handleCommand('clearConversation')">
              <span class="fas fa-trash-can"></span>
              <span class="btn-text" v-if="appStore.device !== 'mobile'">清除对话</span>
            </el-button>
          </div>
        </el-tooltip>
      </div>
      <!-- 未登录显示登录按钮，已登录显示头像下拉菜单 -->
      <el-dropdown v-if="isLogged" @command="handleCommand" class="avatar-container" trigger="click">
        <div class="avatar-wrapper right-menu-item hover-effect">
          <img :src="userStore.avatar" class="user-avatar" @error="handleAvatarError" />
          <span class="user-nickname" v-if="appStore.device !== 'mobile'">{{ userStore.nickName }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="enterBackend" v-if="isAdmin">进入后台</el-dropdown-item>
            <el-dropdown-item command="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button v-else type="primary" class="login-btn" @click="goToLogin">登录</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { ElMessageBox } from 'element-plus';
  import Hamburger from '@/components/Hamburger/index.vue';
  import useAppStore from '@/store/modules/app';
  import useUserStore from '@/store/modules/user';
  import useSettingsStore from '@/store/modules/settings';
  import useSystemStore from '@/store/modules/system';
  import useChatStore from '@/store/modules/chat';

  const appStore = useAppStore();
  const userStore = useUserStore();
  const settingsStore = useSettingsStore();
  const chatStore = useChatStore();
  const router = useRouter();

  const { proxy } = getCurrentInstance();

  // 默认头像占位图
  const DEFAULT_AVATAR = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

  // 头像加载错误处理
  function handleAvatarError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.src = DEFAULT_AVATAR;
  }

  const currentStudentId = computed(() => chatStore.currentStudentId);

  // 判断用户是否已登录
  const isLogged = computed(() => !!userStore.token);
  const isAdmin = computed(() => userStore.isAdmin);
  const isTeacher = computed(() => userStore.isTeacher);

  const showAssistantModal = ref(false); // 是否显示助手信息弹窗
  const currentAssistantType = ref(''); // 当前显示的助手类型
  const assistantInfo = ref(''); // 助手信息内容

  function toggleSideBar() {
    appStore.toggleSideBar();
  }

  function handleCommand(command) {
    switch (command) {
      case 'setLayout':
        setLayout();
        break;
      case 'logout':
        logout();
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

  function logout() {
    ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
      .then(() => {
        userStore.logOut().then(() => {
          // 刷新页面
          window.location.reload();
        });
      })
      .catch(() => {});
  }

  // 跳转至登录页
  function goToLogin() {
    const currentPath = encodeURIComponent('/chat/sports');
    if (!useSystemStore().casEnable) {
      const BASE_URL = import.meta.env.BASE_URL;
      location.href = `${BASE_URL}/login?redirect=` + currentPath;
    } else {
      // CAS登录场景使用当前页面重定向
      const redirectUrl = encodeURIComponent(window.location.href);
      const casLoginUrl = `${useSystemStore().casServerUrl}/login?service=${redirectUrl}`;
      window.location.href = casLoginUrl;
    }
  }

  // 跳转至后台
  function goToBackend() {
    // 备份当前roles
    const backupRoles = [...useUserStore().roles];
    try {
      useUserStore().roles = [];
      router.push({ path: '/' });
    } catch (error) {
      proxy.$modal.msgError('进入后台失败');
      useUserStore().roles = backupRoles;
    }
  }

  const emits = defineEmits(['setLayout']);
  function setLayout() {
    emits('setLayout');
  }
</script>

<style lang="scss" scoped>
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
    background: linear-gradient(90deg, #63a8ff, #4080ff);

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

      .right-menu-item-group {
        height: 100%;
        line-height: 50px;
        display: flex;
        align-items: center;
        margin-left: auto;
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
            color: #fff;
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

      .login-btn {
        margin-right: 16px;
      }
    }

    .btn-text {
      padding-left: 5px;
    }
  }
  .mobile .avatar-container {
    .avatar-wrapper {
      right: 0px !important;
    }
  }
</style>
