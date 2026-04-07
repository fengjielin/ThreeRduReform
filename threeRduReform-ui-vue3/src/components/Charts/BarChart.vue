<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script setup lang="ts">
  import * as echarts from 'echarts';
  import 'echarts/theme/macarons';
  import { useResizeObserver } from './hooks/resize';

  interface ChartData {
    categories: string[];
    series: { name: string; data: number[] }[];
  }

  const animationDuration = 3000;

  const props = withDefaults(
    defineProps<{
      className?: string;
      width?: string;
      height?: string;
      autoResize?: boolean;
      chartData: ChartData;
    }>(),
    {
      className: 'chart',
      width: '100%',
      height: '300px',
      autoResize: true,
      chartData: {
        categories: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        series: [
          { name: 'pageA', data: [79, 52, 200, 334, 390, 330, 220] },
          { name: 'pageB', data: [80, 52, 200, 334, 390, 330, 220] },
          { name: 'pageC', data: [30, 52, 200, 334, 390, 330, 220] },
        ],
      },
    },
  );

  const chart = ref<echarts.ECharts | null>(null);

  const { resize } = useResizeObserver(chart);

  const setOptions = (chartData: ChartData) => {
    if (!chart.value) return;

    chart.value.setOption({
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow',
        },
      },
      grid: {
        top: 10,
        left: '2%',
        right: '2%',
        bottom: '3%',
        containLabel: true,
      },
      xAxis: [
        {
          type: 'category',
          data: chartData.categories,
          axisTick: {
            alignWithLabel: true,
          },
        },
      ],
      yAxis: [
        {
          type: 'value',
          axisTick: {
            show: false,
          },
        },
      ],
      series: chartData.series.map((item) => ({
        name: item.name,
        type: 'bar',
        stack: 'vistors',
        barWidth: '60%',
        data: item.data,
        animationDuration,
      })),
    });
  };

  const initChart = () => {
    chart.value = echarts.init(document.querySelector(`.${props.className}`) as HTMLElement, 'macarons');
    setOptions(props.chartData);
  };

  watch(
    () => props.chartData,
    (val) => {
      setOptions(val);
    },
    { deep: true },
  );

  onMounted(() => {
    nextTick(() => {
      initChart();
    });
  });

  onUnmounted(() => {
    if (chart.value) {
      chart.value.dispose();
      chart.value = null;
    }
  });
</script>
