# Charts 图表组件库

基于 ECharts 的 Vue 3 图表组件库，提供柱状图、折线图、饼图、雷达图等常用图表。

## 特性

- 🚀 **轻量级** - 仅封装 ECharts 核心功能
- 📱 **响应式** - 自动适应窗口和侧边栏变化
- 🔧 **TypeScript 支持** - 完整的类型定义
- 🎨 **统一风格** - 使用 macarons 主题
- 🧹 **内存管理** - 自动清理图表实例

## 文件结构

```
src/components/Charts/
├── hooks/
│   └── resize.ts           # 图表响应式调整 Hook
├── BarChart.vue            # 柱状图组件
├── LineChart.vue           # 折线图组件
├── PieChart.vue            # 饼图组件
└── RaddarChart.vue         # 雷达图组件
```

## 安装依赖

```bash
npm install echarts
```

## 快速开始

### 基础用法

```vue
<template>
  <BarChart />
</template>

<script setup lang="ts">
import BarChart from '@/components/Charts/BarChart.vue'
</script>
```

### 带数据用法

```vue
<template>
  <LineChart :chartData="lineData" />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import LineChart from '@/components/Charts/LineChart.vue'

const lineData = ref({
  expectedData: [100, 120, 110, 140, 130, 150, 160],
  actualData: [90, 110, 120, 130, 120, 140, 150]
})
</script>
```

---

## 组件 API

### 通用 Props

所有图表组件都支持以下 Props：

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `className` | `string` | `'chart'` | 图表容器的 CSS 类名 |
| `width` | `string` | 组件默认 | 图表宽度 |
| `height` | `string` | 组件默认 | 图表高度 |

---

## BarChart 柱状图

### 示例

```vue
<BarChart
  className="sales-chart"
  width="100%"
  height="400px"
/>
```

### 默认数据

```javascript
xAxis: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
series:
  - pageA: [79, 52, 200, 334, 390, 330, 220]
  - pageB: [80, 52, 200, 334, 390, 330, 220]
  - pageC: [30, 52, 200, 334, 390, 330, 220]
```

### 特性

- 堆叠柱状图（stack: 'vistors'）
- 动画时长 6000ms
- 柱宽度 60%

---

## LineChart 折线图

### Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `chartData` | `ChartData` | 必填 | 图表数据 |
| `autoResize` | `boolean` | `true` | 是否自动调整大小 |

### 接口定义

```typescript
interface ChartData {
  expectedData: number[];  // 预期数据
  actualData: number[];     // 实际数据
}
```

### 示例

```vue
<LineChart
  :chartData="chartData"
  :autoResize="true"
/>

<script setup lang="ts">
import { ref } from 'vue'
import LineChart from '@/components/Charts/LineChart.vue'

const chartData = ref({
  expectedData: [100, 120, 110, 140, 130, 150, 160],
  actualData: [90, 110, 120, 130, 120, 140, 150]
})
</script>
```

### 特性

- 平滑曲线（smooth: true）
- 面积填充（areaStyle）
- 响应数据变化自动更新
- 动画时长 2800ms

---

## PieChart 饼图

### 默认数据

```javascript
data:
  - Industries: 320
  - Technology: 240
  - Forex: 149
  - Gold: 100
  - Forecasts: 59
```

### 特性

- 玫瑰图模式（roseType: 'radius'）
- 内半径 15%，外半径 95%
- 显示百分比 tooltip

---

## RaddarChart 雷达图

### 接口定义

```typescript
interface RadarIndicator {
  name: string;  // 指标名称
  max: number;   // 最大值
}

interface RadarData {
  value: number[];  // 数据值
  name: string;     // 数据系列名称
}
```

### 默认指标

```javascript
[
  { name: 'Sales', max: 10000 },
  { name: 'Administration', max: 20000 },
  { name: 'Information Techology', max: 20000 },
  { name: 'Customer Support', max: 20000 },
  { name: 'Development', max: 20000 },
  { name: 'Marketing', max: 20000 }
]
```

### 默认数据

```javascript
- Allocated Budget: [5000, 7000, 12000, 11000, 15000, 14000]
- Expected Spending: [4000, 9000, 15000, 15000, 13000, 11000]
- Actual Spending: [5500, 11000, 12000, 15000, 12000, 12000]
```

### 特性

- 六维雷达图
- 阴影效果增强立体感
- 半径 66%

---

## hooks/resize 响应式调整

### 用途

自动监听窗口大小变化和侧边栏收折，触发图表重新渲染。

### 工作原理

```
┌─────────────────────────────────────────────────────────┐
│                   触发时机                              │
├─────────────────────────────────────────────────────────┤
│  1. 浏览器窗口大小变化                                  │
│  2. 侧边栏展开/收缩动画完成                             │
│  3. keep-alive 组件激活                                │
└─────────────────────────────────────────────────────────┘
                              │
                              ▼
              ┌───────────────────────────────────────┐
              │          防抖处理 (100ms)              │
              └───────────────────────────────────────┘
                              │
                              ▼
              ┌───────────────────────────────────────┐
              │        chart.resize()                 │
              └───────────────────────────────────────┘
```

### 数据流向图
```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   父组件     │ ──► │   图表组件    │ ──► │  ECharts     │
│  传入数据    │     │  初始化图表   │     │  渲染图表    │
└──────────────┘     └──────────────┘     └──────────────┘
                              │
                              ▼
                     ┌──────────────────┐
                     │  window resize   │
                     │  sidebar toggle  │
                     └──────────────────┘
                              │
                              ▼
                     ┌──────────────────┐
                     │  chart.resize()  │
                     └──────────────────┘
```

### 在自定义组件中使用

```vue
<script setup lang="ts">
import { ref } from 'vue'
import * as echarts from 'echarts'
import { useResizeObserver } from '@/components/Charts/hooks/resize'

const chart = ref<echarts.ECharts | null>(null)

// 使用 resize hook
const { resize } = useResizeObserver(chart)

const initChart = () => {
  chart.value = echarts.init(document.querySelector('.chart') as HTMLElement)
  chart.value.setOption({ /* ... */ })
}
</script>
```

---

## 响应式示例

```vue
<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="12">
        <BarChart className="chart-1" height="300px" />
      </el-col>
      <el-col :span="12">
        <LineChart
          className="chart-2"
          height="300px"
          :chartData="lineData"
        />
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BarChart from '@/components/Charts/BarChart.vue'
import LineChart from '@/components/Charts/LineChart.vue'

const lineData = ref({
  expectedData: [100, 120, 110, 140, 130, 150, 160],
  actualData: [90, 110, 120, 130, 120, 140, 150]
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
</style>
```

---

## 主题定制

### 使用其他主题

```typescript
import * as echarts from 'echarts'
import 'echarts/theme/roma'  // 导入主题

const chart = echarts.init(
  document.querySelector('.chart') as HTMLElement,
  'roma'  // 指定主题
)
```

### 内置主题

ECharts 内置主题：
- `macarons`（默认）
- `roma`
- `shine`
- `infographic`
- `vintage`

---

## 注意事项

1. **容器必须有宽高** - 图表容器需要明确设置 width 和 height
2. **及时清理** - 组件卸载时会自动 dispose 图表实例
3. **避免重复初始化** - 同一个 DOM 元素只能初始化一次图表
4. **响应式数据** - LineChart 会监听 chartData 变化自动更新

---

## 💡 总结
这个 Charts 组件库封装了 ECharts 的常用图表，提供了：

1. 统一的使用接口 - 相同的 props 结构
2. 响应式处理 - 自动适应窗口/侧边栏变化
3. 资源管理 - 正确清理图表实例防止内存泄漏
4. TypeScript 支持 - 完整的类型定义

## License

MIT
