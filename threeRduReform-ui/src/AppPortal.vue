<template>
  <router-view />
</template>

<script setup></script>

<style>
  /* 全局样式覆盖 */
  html,
  body,
  #portal-app {
    margin: 0;
    padding: 0;
    height: 100%;
  }

  #portal-app {
    font-family: 'Microsoft YaHei', 'Helvetica Neue', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
</style>
