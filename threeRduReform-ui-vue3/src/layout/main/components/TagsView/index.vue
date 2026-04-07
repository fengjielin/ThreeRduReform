<template>
  <div id="tags-view-container" class="tags-view-container">
    <scroll-pane ref="scrollPaneRef" class="tags-view-wrapper" @scroll="handleScroll">
      <draggable
        v-model="tagList"
        :animation="200"
        group="tags"
        item-key="path"
        handle=".tags-view-item"
        class="tags-view-list"
        ghost-class="tags-ghost"
        drag-class="tags-drag"
        @end="onDragEnd"
        :disabled="device === 'mobile'">
        <template #item="{ element: tag }">
          <router-link :key="tag.path" :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }" custom v-slot="{ navigate, isActive: active, href }">
            <span
              :class="{ active: active, 'has-icon': tagsIcon }"
              :style="activeStyle(tag)"
              @click="navigate"
              @click.middle="!isAffix(tag) ? closeSelectedTag(tag) : ''"
              @contextmenu.prevent="openMenu(tag, $event)"
              class="tags-view-item">
              <svg-icon v-if="tagsIcon && tag.meta && tag.meta.icon && tag.meta.icon !== '#'" :icon-class="tag.meta.icon" />
              {{ tag.title }}
              <span v-if="!isAffix(tag) && visitedViews.length != 1" @click.prevent.stop="closeSelectedTag(tag)">
                <close class="el-icon-close" style="width: 1em; height: 1em; vertical-align: -0.14em" />
              </span>
            </span>
          </router-link>
        </template>
      </draggable>
    </scroll-pane>
    <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
      <li @click="refreshSelectedTag(selectedTag)">
        <refresh-right style="width: 1em; height: 1em" />
        刷新页面
      </li>
      <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">
        <close style="width: 1em; height: 1em" />
        关闭当前
      </li>
      <li @click="closeOthersTags">
        <circle-close style="width: 1em; height: 1em" />
        关闭其他
      </li>
      <li v-if="!isFirstView()" @click="closeLeftTags">
        <back style="width: 1em; height: 1em" />
        关闭左侧
      </li>
      <li v-if="!isLastView()" @click="closeRightTags">
        <right style="width: 1em; height: 1em" />
        关闭右侧
      </li>
      <li @click="closeAllTags(selectedTag)">
        <circle-close style="width: 1em; height: 1em" />
        全部关闭
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
  import ScrollPane from './ScrollPane.vue';
  import draggable from 'vuedraggable/dist/vuedraggable.common';
  import { getNormalPath } from '@/utils/ruoyi';
  import useTagsViewStore from '@/store/modules/tagsView';
  import useSettingsStore from '@/store/modules/settings';
  import usePermissionStore from '@/store/modules/permission';
  import useAppStore from '@/store/modules/app';
  import { getFirstPath } from '@/utils/index';

  const visible = ref<boolean>(false);
  const top = ref<number>(0);
  const left = ref<number>(0);
  const selectedTag = ref<any>({});
  const affixTags = ref<any[]>([]);
  const scrollPaneRef = ref<any>(null);

  const { proxy } = getCurrentInstance();
  const route = useRoute();
  const router = useRouter();

  const visitedViews = computed(() => useTagsViewStore().visitedViews);
  const routes = computed(() => usePermissionStore().routes);
  const theme = computed(() => useSettingsStore().theme);
  const tagsIcon = computed(() => useSettingsStore().tagsIcon);
  const device = computed(() => useAppStore().device);

  // 拖动排序列表
  const tagList = computed({
    get: () => visitedViews.value,
    set: (val) => {
      useTagsViewStore().updateViewOrder(val);
    },
  });

  watch(route, () => {
    addTags();
    moveToCurrentTag();
  });

  watch(visible, (value: boolean) => {
    if (value) {
      document.body.addEventListener('click', closeMenu);
    } else {
      document.body.removeEventListener('click', closeMenu);
    }
  });

  onMounted(() => {
    initTags();
    addTags();
  });

  function isActive(r: any): boolean {
    return r.path === route.path;
  }

  function activeStyle(tag: any): Record<string, string> {
    if (!isActive(tag)) return {};
    return {
      color: `var(--menu-active-text, #409eff)`,
      'background-color': `color-mix(in srgb, var(--menu-active-text, #409eff), transparent 90%)`,
      'border-color': `color-mix(in srgb, var(--menu-active-text, #409eff), transparent 90%)`,
    };
  }

  function isAffix(tag: any): boolean {
    return tag.meta && tag.meta.affix;
  }

  function isFirstView(): boolean {
    try {
      return selectedTag.value.fullPath === '/index' || selectedTag.value.fullPath === visitedViews.value[1].fullPath;
    } catch (err) {
      return false;
    }
  }

  function isLastView(): boolean {
    try {
      return selectedTag.value.fullPath === visitedViews.value[visitedViews.value.length - 1].fullPath;
    } catch (err) {
      return false;
    }
  }

  function filterAffixTags(routes: any[], basePath = ''): any[] {
    const tags: any[] = [];
    routes.forEach((route) => {
      if (route.meta && route.meta.affix) {
        const tagPath = getNormalPath(basePath + '/' + route.path);
        tags.push({
          fullPath: tagPath,
          path: tagPath,
          name: route.name,
          meta: { ...route.meta },
        });
      }
      if (route.children) {
        const tempTags = filterAffixTags(route.children, route.path);
        if (tempTags.length >= 1) {
          tags.push(...tempTags);
        }
      }
    });
    return tags;
  }

  function initTags(): void {
    const res = filterAffixTags(routes.value);
    affixTags.value = res;
    for (const tag of res) {
      // Must have tag name
      if (tag.name) {
        useTagsViewStore().addVisitedView(tag);
      }
    }
  }

  function addTags(): void {
    const { name } = route;
    if (name) {
      useTagsViewStore().addView(route);
    }
  }

  function moveToCurrentTag(): void {
    nextTick(() => {
      for (const r of visitedViews.value) {
        if (r.path === route.path) {
          scrollPaneRef.value?.moveToTarget(r);
          // when query is different then update
          if (r.fullPath !== route.fullPath) {
            useTagsViewStore().updateVisitedView(route);
          }
        }
      }
    });
  }

  function refreshSelectedTag(view: any): void {
    proxy.$tab.refreshPage(view);
    if (route.meta.link) {
      useTagsViewStore().delIframeView(route);
    }
  }

  function closeSelectedTag(view: any): void {
    proxy.$tab.closePage(view).then(({ visitedViews }: any) => {
      if (isActive(view)) {
        toLastView(visitedViews, view);
      }
    });
  }
  function closeRightTags(): void {
    proxy.$tab.closeRightPage(selectedTag.value).then((visitedViews: any) => {
      if (!visitedViews.find((i: any) => i.fullPath === route.fullPath)) {
        toLastView(visitedViews);
      }
    });
  }

  function closeLeftTags(): void {
    proxy.$tab.closeLeftPage(selectedTag.value).then((visitedViews: any) => {
      if (!visitedViews.find((i: any) => i.fullPath === route.fullPath)) {
        toLastView(visitedViews);
      }
    });
  }

  function closeOthersTags(): void {
    router.push(selectedTag.value).catch(() => {});
    proxy.$tab.closeOtherPage(selectedTag.value).then(() => {
      moveToCurrentTag();
    });
  }

  function closeAllTags(view: any): void {
    proxy.$tab.closeAllPage().then(({ visitedViews }: any) => {
      if (affixTags.value.some((tag: any) => tag.path === route.path)) {
        return;
      }
      toLastView(visitedViews, view);
    });
  }

  function toLastView(visitedViews: any[], view?: any): void {
    const latestView = visitedViews.slice(-1)[0];
    if (latestView) {
      router.push(latestView.fullPath);
    } else {
      // now the default is to redirect to the home page if there is no tags-view,
      // you can adjust it according to your needs.
      if (view && view.name === 'Dashboard') {
        // to reload home page
        router.replace({ path: '/redirect' + view.fullPath });
      } else {
        router.push('/');
        const firstMenuPath = usePermissionStore().topbarRouters.find((item: any) => !item.hidden);
        if (firstMenuPath) {
          let path = getFirstPath(firstMenuPath);
          router.push(path);
        } else {
          router.push('/');
        }
      }
    }
  }

  function openMenu(tag: any, e: MouseEvent): void {
    const menuMinWidth = 105;
    const offsetLeft = proxy.$el.getBoundingClientRect().left; // container margin left
    const offsetWidth = proxy.$el.offsetWidth; // container width
    const maxLeft = offsetWidth - menuMinWidth; // left boundary
    const l = e.clientX - offsetLeft + 15; // 15: margin right

    if (l > maxLeft) {
      left.value = maxLeft;
    } else {
      left.value = l;
    }

    top.value = e.clientY;
    visible.value = true;
    selectedTag.value = tag;
  }

  function closeMenu(): void {
    visible.value = false;
  }

  function handleScroll(): void {
    closeMenu();
  }

  // 拖动排序结束
  function onDragEnd(): void {
    moveToCurrentTag();
  }
</script>

<style lang="scss" scoped>
  .tags-view-container {
    height: 38px;
    width: 100%;
    background: var(--tags-bg, #fff);
    border-bottom: 1px solid var(--tags-item-border, #d8dce5);
    // box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);

    .tags-view-wrapper {
      .tags-view-list {
        display: inline-flex;
        align-items: flex-start;
        padding: 2px 10px;
      }

      .tags-view-item {
        display: inline-block;
        position: relative;
        cursor: pointer;
        height: 26px;
        line-height: 26px;
        border: 1px solid var(--tags-item-border, #d8dce5);
        border-radius: 4px;
        color: var(--tags-item-text, #495060);
        background: var(--tags-item-bg, #fff);
        padding: 0 8px;
        font-size: 12px;
        margin-left: 5px;
        margin-top: 6px;

        &:first-of-type {
          margin-left: 0;
        }

        &:last-of-type {
          margin-right: 15px;
        }

        &.active {
          background-color: #42b983;
          color: #fff;
          border-color: #42b983;

          &::before {
            content: '';
            background: #fff;
            display: inline-block;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            position: relative;
            margin-right: 5px;
          }
        }
      }

      // 拖动时的占位元素样式
      .tags-ghost {
        opacity: 0.5;
        background: #f0f0f0;
        border: 1px dashed #409eff;
      }

      // 拖动时的元素样式
      .tags-drag {
        opacity: 1;
        transform: scale(1.05);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
      }
    }

    .tags-view-item.active.has-icon::before {
      content: none !important;
    }

    .contextmenu {
      margin: 0;
      background: var(--el-bg-color-overlay, #fff);
      z-index: 3000;
      position: absolute;
      list-style-type: none;
      padding: 5px 0;
      border-radius: 4px;
      font-size: 12px;
      font-weight: 400;
      color: var(--tags-item-text, #333);
      box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
      border: 1px solid var(--el-border-color-light, #e4e7ed);

      li {
        margin: 0;
        padding: 7px 16px;
        cursor: pointer;
        display: flex;
        align-items: anchor-center;
        gap: 3px;

        &:hover {
          background: var(--tags-item-hover, #eee);
        }
      }
    }
  }
</style>

<style lang="scss">
  //reset element css of el-icon-close
  .tags-view-wrapper {
    .tags-view-item {
      .el-icon-close {
        width: 16px;
        height: 16px;
        vertical-align: 2px;
        border-radius: 50%;
        text-align: center;
        transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
        transform-origin: 100% 50%;

        &:before {
          transform: scale(0.6);
          display: inline-block;
          vertical-align: -3px;
        }

        &:hover {
          // background-color: var(--tags-close-hover, #b4bccc);
          background-color: var(--menu-active-text, #409eff);
          color: #fff;
          width: 12px !important;
          height: 12px !important;
        }
      }
    }
  }
</style>
