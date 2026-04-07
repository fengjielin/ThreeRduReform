<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script setup lang="ts">
  import * as echarts from 'echarts';
  import 'echarts/theme/macarons';
  import { useResizeObserver } from './hooks/resize';

  interface ChartData {
    expectedData: number[];
    actualData: number[];
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
      height: '350px',
      autoResize: true,
    },
  );

  const chart = ref<echarts.ECharts | null>(null);

  const { resize } = useResizeObserver(chart);

  const setOptions = (chartData: ChartData) => {
    if (!chart.value) return;

    chart.value.setOption({
      xAxis: {
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        boundaryGap: false,
        axisTick: {
          show: false,
        },
      },
      grid: {
        left: 10,
        right: 10,
        bottom: 20,
        top: 30,
        containLabel: true,
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross',
        },
        padding: [5, 10],
      },
      yAxis: {
        axisTick: {
          show: false,
        },
      },
      legend: {
        data: ['expected', 'actual'],
      },
      series: [
        {
          name: 'expected',
          itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2,
              },
            },
          },
          smooth: true,
          type: 'line',
          data: chartData.expectedData,
          animationDuration: 2800,
          animationEasing: 'cubicInOut',
        },
        {
          name: 'actual',
          smooth: true,
          type: 'line',
          itemStyle: {
            normal: {
              color: '#3888fa',
              lineStyle: {
                color: '#3888fa',
                width: 2,
              },
              areaStyle: {
                color: '#f3f8ff',
              },
            },
          },
          data: chartData.actualData,
          animationDuration: 2800,
          animationEasing: 'quadraticOut',
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
