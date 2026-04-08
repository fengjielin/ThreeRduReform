<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script setup lang="ts">
  import * as echarts from 'echarts';
  import 'echarts/theme/macarons';
  import { useResizeObserver } from './hooks/resize';

  interface PieDataItem {
    value: number;
    name: string;
  }

  interface ChartData {
    data: { value: number; name: string }[];
  }

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
        data: [
          { value: 320, name: 'Industries' },
          { value: 240, name: 'Technology' },
          { value: 149, name: 'Forex' },
          { value: 100, name: 'Gold' },
          { value: 59, name: 'Forecasts' },
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
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)',
      },
      legend: {
        left: 'center',
        bottom: '10',
        data: chartData.data.map((item) => item.name),
      },
      series: [
        {
          name: 'WEEKLY WRITE ARTICLES',
          type: 'pie',
          roseType: 'radius',
          radius: [15, 95],
          center: ['50%', '38%'],
          data: chartData.data,
          animationEasing: 'cubicInOut',
          animationDuration: 2600,
        },
      ],
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
