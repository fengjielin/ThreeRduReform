import { debounce } from '@/utils';
import * as echarts from 'echarts';

export function useResizeObserver(chart: Ref<echarts.ECharts | null>) {
  const $_sidebarElm = ref<HTMLElement | null>(null);
  const $_resizeHandler = ref<(() => void) | null>(null);

  const $_sidebarResizeHandler = (e: TransitionEvent) => {
    if (e.propertyName === 'width') {
      $_resizeHandler.value?.();
    }
  };

  const initListener = () => {
    $_resizeHandler.value = debounce(() => {
      resize();
    }, 100);
    window.addEventListener('resize', $_resizeHandler.value!);

    $_sidebarElm.value = document.getElementsByClassName('sidebar-container')[0] as HTMLElement;
    $_sidebarElm.value?.addEventListener('transitionend', $_sidebarResizeHandler);
  };

  const destroyListener = () => {
    $_resizeHandler.value && window.removeEventListener('resize', $_resizeHandler.value);
    $_resizeHandler.value = null;

    $_sidebarElm.value?.removeEventListener('transitionend', $_sidebarResizeHandler);
  };

  const resize = () => {
    if (chart.value) {
      chart.value.resize();
    }
  };

  onMounted(() => {
    initListener();
  });

  onActivated(() => {
    if (!$_resizeHandler.value) {
      initListener();
    }
    resize();
  });

  onUnmounted(() => {
    destroyListener();
  });

  onDeactivated(() => {
    destroyListener();
  });

  return {
    $_sidebarElm,
    $_resizeHandler,
    $_sidebarResizeHandler,
    initListener,
    destroyListener,
    resize,
  };
}
