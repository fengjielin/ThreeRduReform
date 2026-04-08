<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" :to="getPath">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title">{{ title }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" :to="getPath">
        <img v-if="logo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title">{{ title }}</h1>
      </router-link>
    </transition>
  </div>
</template>

<script setup>
  // import logo from '@/assets/logo/logo.png';
  import useSettingsStore from '@/store/modules/settings';
  import usePermissionStore from '@/store/modules/permission';
  import variables from '@/assets/styles/variables.module.scss';
  import { getFirstPath } from '@/utils/index';

  defineProps({
    collapse: {
      type: Boolean,
      required: true,
    },
  });

  const title = '智慧体育小助手';
  const settingsStore = useSettingsStore();
  const sideTheme = computed(() => settingsStore.sideTheme);

  // 助手头像（豆包风格的头像）
  const logo = ref(
    'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiIGZpbGw9IiNmZmYiIHN0cm9rZT0iI2ZmZiIgc3Ryb2tlLXdpZHRoPSIwLjUiLz4KPHBhdGggZD0iTTE1IDE1TDIwIDI1TDI1IDE1IiBzdHJva2U9IiM1Y2Q4ZmYiIHN0cm9rZS13aWR0aD0iMiIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIiBzdHJva2UtbGluZWpvaW49InJvdW5kIi8+CjxwYXRoIGQ9Ik0xNSAyNUwyMCAzNUwyNSAyNSIgc3Ryb2tlPSIjOGY2ZTg1IiBzdHJva2Utd2lkdGg9IjIiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIgc3Ryb2tlLWxpbmVqb2luPSJyb3VuZCIvPgo8L3N2Zz4=',
  );

  const getPath = computed(() => {
    return '/chat/sports';
  });

  // 获取Logo背景色
  const getLogoBackground = computed(() => {
    return variables.menuLightBg;
  });

  // 获取Logo文字颜色
  const getLogoTextColor = computed(() => {
    return variables.menuLightText;
  });
</script>

<style lang="scss" scoped>
  .sidebarLogoFade-enter-active {
    transition: opacity 1.5s;
  }

  .sidebarLogoFade-enter,
  .sidebarLogoFade-leave-to {
    opacity: 0;
  }

  .sidebar-logo-container {
    position: relative;
    height: 50px;
    line-height: 50px;
    background: v-bind(getLogoBackground);
    text-align: center;
    overflow: hidden;

    & .sidebar-logo-link {
      height: 100%;
      width: 100%;

      & .sidebar-logo {
        width: 32px;
        height: 32px;
        vertical-align: middle;
        margin-right: 12px;
      }

      & .sidebar-title {
        display: inline-block;
        margin: 0;
        color: v-bind(getLogoTextColor);
        font-weight: 600;
        line-height: 50px;
        font-size: 14px;
        font-family:
          Avenir,
          Helvetica Neue,
          Arial,
          Helvetica,
          sans-serif;
        vertical-align: middle;
      }
    }

    &.collapse {
      .sidebar-logo {
        margin-right: 0px;
      }
    }
  }
</style>
