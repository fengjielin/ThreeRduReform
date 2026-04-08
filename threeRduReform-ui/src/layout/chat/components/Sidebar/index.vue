<template>
  <div :class="{ 'has-logo': showLogo }" class="sidebar-container">
    <logo v-if="showLogo" :collapse="isCollapse" />

    <!-- <div class="user-info-container">
      <el-dropdown @command="handleCommand" class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="userStore.avatar" class="user-avatar" @error="handleAvatarError" />
          <span class="user-nickname">{{ userStore.nickName }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div> -->
  </div>
</template>

<script setup lang="ts">
  import { ElMessageBox } from 'element-plus';
  import Logo from '@/layout/chat/components/Sidebar/Logo.vue';
  import useAppStore from '@/store/modules/app';
  import useSettingsStore from '@/store/modules/settings';
  import useChatStore from '@/store/modules/chat';
  import useUserStore from '@/store/modules/user';
  import useSystemStore from '@/store/modules/system';

  const { proxy } = getCurrentInstance();

  const appStore = useAppStore();
  const settingsStore = useSettingsStore();
  const userStore = useUserStore();
  const chatStore = useChatStore();
  const systemStore = useSystemStore();

  const showLogo = computed(() => settingsStore.sidebarLogo);
  const isCollapse = computed(() => !appStore.sidebar.opened);

  const isAdmin = computed(() => userStore.isAdmin);
  const isTeacher = computed(() => userStore.isTeacher);
  const isStudent = computed(() => userStore.isStudent);

  const permissionList = ref([]);
  const loading = ref(true);
  const loadingFaq = ref(true);

  // 默认头像占位图
  const DEFAULT_AVATAR = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

  // 头像加载错误处理
  function handleAvatarError(event: Event) {
    const img = event.target as HTMLImageElement;
    img.src = DEFAULT_AVATAR;
  }

  function handleCommand(command: string) {
    switch (command) {
      case 'logout':
        logout();
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
          if (!systemStore.casEnable) {
            let publicPath = 'assistantPage';
            location.href = '/' + publicPath + '/login';
          } else {
            window.location.href = systemStore.casLogoutUrl;
          }
        });
      })
      .catch(() => {});
  }
</script>

<style lang="scss" scoped>
  .sidebar-container {
    background-color: #ffffff;

    .scrollbar-wrapper {
      background-color: #ffffff;
    }

    /* 用户信息区域 */
    .user-info-container {
      padding: 14px 20px;
      border-top: 1px solid #f0f2f5;
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: 8px;
      position: absolute;
      bottom: 0;
      width: 100%;

      .avatar-container {
        .avatar-wrapper {
          display: flex;
          align-items: center;
          position: relative;

          .user-avatar {
            cursor: pointer;
            width: 36px;
            height: 36px;
            border-radius: 50%;
          }

          .user-nickname {
            font-size: 14px;
            font-weight: bold;
            margin-left: 8px;
          }
        }
      }
    }
  }
</style>
