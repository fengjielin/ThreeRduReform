<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script setup lang="ts">
  import * as echarts from 'echarts';
  import 'echarts/theme/macarons';
  import { useResizeObserver } from './hooks/resize';

  interface RadarIndicator {
    name: string;
    max: number;
  }

  interface RadarData {
    value: number[];
    name: string;
  }

  interface ChartData {
    indicators: RadarIndicator[];
    series: RadarData[];
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
        indicators: [
          { name: 'Sales', max: 10000 },
          { name: 'Administration', max: 20000 },
          { name: 'Information Techology', max: 20000 },
          { name: 'Customer Support', max: 20000 },
          { name: 'Development', max: 20000 },
          { name: 'Marketing', max: 20000 },
        ],
        series: [
          {
            value: [5000, 7000, 12000, 11000, 15000, 14000],
            name: 'Allocated Budget',
          },
          {
            value: [4000, 9000, 15000, 15000, 13000, 11000],
            name: 'Expected Spending',
          },
          {
            value: [5500, 11000, 12000, 15000, 12000, 12000],
            name: 'Actual Spending',
          },
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
      radar: {
        radius: '66%',
        center: ['50%', '42%'],
        splitNumber: 8,
        splitArea: {
          areaStyle: {
            color: 'rgba(127,95,132,.3)',
            opacity: 1,
            shadowBlur: 45,
            shadowColor: 'rgba(0,0,0,.5)',
            shadowOffsetX: 0,
            shadowOffsetY: 15,
          },
        },
        indicator: chartData.indicators,
      },
      legend: {
        left: 'center',
        bottom: '10',
        data: chartData.series.map((item) => item.name),
      },
      series: [
        {
          type: 'radar',
          symbolSize: 0,
          areaStyle: {
            normal: {
              shadowBlur: 13,
              shadowColor: 'rgba(0,0,0,.2)',
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1,
            },
          },
          data: chartData.series,
          animationDuration: animationDuration,
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
