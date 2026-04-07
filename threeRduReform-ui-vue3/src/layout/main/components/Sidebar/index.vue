<template>
  <div :class="{ 'has-logo': showLogo, [menuStyleClass]: true }" class="sidebar-container">
    <logo :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="getMenuBackground"
        :text-color="getMenuTextColor"
        :active-text-color="getMenuActiveTextColor"
        :unique-opened="true"
        :collapse-transition="false"
        mode="vertical"
        :class="sideTheme"
        :popper-class="getPopperClass"
        @select="handleSelect">
        <sidebar-item v-for="(route, index) in sidebarRouters" :key="route.path + index" :item="route" :base-path="route.path" />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
  import Logo from './Logo.vue';
  import SidebarItem from './SidebarItem.vue';
  import variables from '@/assets/styles/variables.module.scss';
  import useAppStore from '@/store/modules/app';
  import useSettingsStore from '@/store/modules/settings';
  import usePermissionStore from '@/store/modules/permission';

  const route = useRoute();
  const appStore = useAppStore();
  const settingsStore = useSettingsStore();
  const permissionStore = usePermissionStore();

  const sidebarRouters = computed(() => permissionStore.sidebarRouters);
  const showLogo = computed(() => settingsStore.sidebarLogo);
  const sideTheme = computed(() => settingsStore.sideTheme);
  const menuStyle = computed(() => settingsStore.menuStyle);
  const theme = computed(() => settingsStore.theme);
  const isCollapse = computed(() => !appStore.sidebar.opened);
  const device = computed(() => appStore.device);

  // 获取菜单背景色
  const getMenuBackground = computed(() => {
    if (settingsStore.isDark) {
      return 'var(--sidebar-bg)';
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

  // 获取菜单文字颜色
  const getMenuTextColor = computed(() => {
    if (settingsStore.isDark) {
      return 'var(--sidebar-text)';
    }
    if (sideTheme.value === 'theme-dark') {
      return variables.menuText;
    } else if (sideTheme.value === 'theme-light') {
      return variables.menuLightText;
    } else if (sideTheme.value === 'theme-blue') {
      return variables.menuBlueColor;
    } else {
      return variables.menuText;
    }
  });

  // 获取菜单激活文字颜色
  const getMenuActiveTextColor = computed(() => {
    if (settingsStore.isDark) {
      return 'var(--menu-active-text)';
    }

    if (sideTheme.value === 'theme-blue') {
      return variables.menuBlueColorActive;
    }

    return theme.value;
  });

  const activeMenu = computed(() => {
    const { meta, path } = route;
    if (meta.activeMenu) {
      return meta.activeMenu;
    }
    return path;
  });
  const menuStyleClass = computed(() => {
    if (menuStyle.value === 'rounded') {
      return 'menu-style-rounded';
    } else if (menuStyle.value === 'plain') {
      return 'menu-style-plain';
    }
  });
  const getPopperClass = computed(() => {
    return `sidebar-popper ${menuStyleClass.value}`;
  });

  const handleSelect = (key: string) => {
    if (device.value === 'mobile' && appStore.sidebar.opened) {
      appStore.closeSideBar({ withoutAnimation: false });
    }
  };
</script>

<style lang="scss" scoped>
  .sidebar-container {
    background-color: v-bind(getMenuBackground);

    .scrollbar-wrapper {
      background-color: v-bind(getMenuBackground);
    }

    :deep(.el-menu) {
      border: none;
      height: 100%;
      width: 100%;

      /* 当 el-sub-menu 内部有激活的菜单项时 */
      .el-sub-menu.is-active > .el-sub-menu__title {
        color: var(--menu-active-text, #409eff);
      }

      // 菜单折叠时，el-sub-menu的样式
      &.el-menu--collapse {
        .el-sub-menu.is-active > .el-sub-menu__title {
          color: var(--menu-active-text, #409eff);

          &::before {
            content: '';
            position: absolute;
            top: 4px;
            bottom: 4px;
            left: 8px;
            right: 8px;
            background-color: color-mix(in srgb, var(--menu-active-text, #409eff), transparent 90%);
            border-radius: 6px;
          }
        }
      }

      .el-sub-menu__title {
        color: v-bind(getMenuTextColor);
      }
      .el-menu-item {
        color: v-bind(getMenuTextColor);
        position: relative;
      }
      .el-menu-item,
      .el-sub-menu__title {
        &:hover {
          background-color: transparent;

          &:not(.is-active)::before {
            content: '';
            position: absolute;
            top: 4px;
            bottom: 4px;
            left: 8px;
            right: 8px;
            background-color: color-mix(in srgb, var(--menu-active-text, #409eff), transparent 90%);
            border-radius: 6px;
          }
        }
      }
      .el-menu-item {
        &.is-active {
          color: var(--menu-active-text, #409eff);
          background-color: transparent;
        }

        &.is-active::before {
          content: '';
          position: absolute;
          top: 4px;
          bottom: 4px;
          left: 8px;
          right: 8px;
          background-color: var(--menu-active-text, #409eff);
          opacity: 0.1;
          border-radius: 6px;
        }
      }
    }

    // 导航菜单风格 rounded圆润 plain.朴素
    &.menu-style-plain {
      :deep(.el-menu) {
        .el-menu-item,
        .el-sub-menu__title {
          &:hover {
            &:not(.is-active)::before {
              inset: 0;
              border-radius: inherit;
            }
          }
        }

        .el-menu-item {
          &.is-active::before {
            inset: 0;
            border-radius: inherit;
          }
        }
        &.el-menu--collapse {
          .el-sub-menu.is-active > .el-sub-menu__title {
            &::before {
              inset: 0;
              border-radius: inherit;
            }
          }
        }
      }
    }
  }
</style>

<style lang="scss">
  .sidebar-popper {
    .el-sub-menu.is-active > .el-sub-menu__title {
      color: var(--menu-active-text, #409eff);
    }
    .el-sub-menu__title {
      // color: v-bind(getMenuTextColor);
    }
    .el-menu-item {
      // color: v-bind(getMenuTextColor);
      position: relative;
    }
    .el-menu-item,
    .el-sub-menu__title {
      &:hover {
        background-color: transparent;

        &:not(.is-active)::before {
          content: '';
          position: absolute;
          top: 4px;
          bottom: 4px;
          left: 8px;
          right: 8px;
          background-color: color-mix(in srgb, var(--menu-active-text, #409eff), transparent 90%);
          border-radius: 6px;
        }
      }
    }
    .el-menu-item {
      &.is-active {
        color: var(--menu-active-text, #409eff);
        background-color: transparent;
      }

      &.is-active::before {
        content: '';
        position: absolute;
        top: 4px;
        bottom: 4px;
        left: 8px;
        right: 8px;
        background-color: var(--menu-active-text, #409eff);
        opacity: 0.1;
        border-radius: 6px;
      }
    }

    &.menu-style-plain {
      .el-menu-item,
      .el-sub-menu__title {
        &:hover {
          &:not(.is-active)::before {
            inset: 0;
            border-radius: inherit;
          }
        }
      }
      .el-menu-item {
        &.is-active::before {
          inset: 0;
          border-radius: inherit;
        }
      }
    }
  }
</style>
