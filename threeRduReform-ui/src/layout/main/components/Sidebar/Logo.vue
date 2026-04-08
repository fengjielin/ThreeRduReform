<template>
  <div class="sidebar-logo-container" :class="{ collapse: collapse }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" :to="getPath">
        <img v-if="logo && showLogo" :src="logo" class="sidebar-logo" />
        <h1 v-else class="sidebar-title">{{ title }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" :to="getPath">
        <img v-if="logo && showLogo" :src="logo" class="sidebar-logo" />
        <h1 class="sidebar-title">{{ title }}</h1>
      </router-link>
    </transition>
  </div>
</template>

<script setup lang="ts">
  import logo from '@/assets/logo/logo.png';
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

  const title = import.meta.env.VITE_APP_TITLE;
  const settingsStore = useSettingsStore();
  const sideTheme = computed(() => settingsStore.sideTheme);
  const showLogo = computed(() => settingsStore.sidebarLogo);

  const getPath = computed(() => {
    let firstMenuPath = usePermissionStore().topbarRouters.find((item) => !item.hidden);
    if (firstMenuPath) {
      return getFirstPath(firstMenuPath);
    } else {
      return '/login';
    }
  });

  // 获取Logo背景色
  const getLogoBackground = computed(() => {
    if (settingsStore.isDark) {
      return 'var(--sidebar-bg)';
    }
    if (settingsStore.navType == 3) {
      return variables.menuLightBg;
    }
    if (sideTheme.value === 'theme-dark') {
      return variables.menuBg;
    } else if (sideTheme.value === 'theme-light') {
      return variables.menuLightBg;
    } else if (sideTheme.value === 'theme-blue') {
      return variables.menuBlueBackground;
    } else {
      return variables.menuBg;
    }
  });

  // 获取Logo文字颜色
  const getLogoTextColor = computed(() => {
    if (settingsStore.isDark) {
      return 'var(--sidebar-text)';
    }
    if (settingsStore.navType == 3) {
      return variables.menuLightText;
    }
    if (sideTheme.value === 'theme-dark') {
      return '#fff';
    } else if (sideTheme.value === 'theme-light') {
      return variables.menuLightText;
    } else if (sideTheme.value === 'theme-blue') {
      return variables.logoTitleBlueColor;
    } else {
      return variables.menuText;
    }
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
