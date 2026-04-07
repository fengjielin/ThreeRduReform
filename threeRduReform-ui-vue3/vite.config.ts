import { defineConfig, loadEnv, type UserConfigFn } from 'vite';
import path from 'path';
import createVitePlugins from './vite/plugins';

import createProxy from './dynamic_proxy';

const backendUrl = 'http://localhost:8080'; // 后端接口

// https://vitejs.dev/config/
const config: UserConfigFn = ({ mode, command }) => {
  const env = loadEnv(mode, process.cwd());
  const { VITE_APP_ENV, VITE_PORT: port, VITE_BASE_URL: baseUrl } = env;
  const publicPath = `/${baseUrl}` || '/toolsPage'; // 公共路径
  return {
    // 部署生产环境和开发环境下的URL。
    // 默认情况下，vite 会假设你的应用是被部署在一个域名的根路径上
    // 例如 https://www.ruoyi.vip/。如果应用被部署在一个子路径上，你就需要用这个选项指定这个子路径。例如，如果你的应用被部署在 https://www.ruoyi.vip/admin/，则设置 baseUrl 为 /admin/。
    base: VITE_APP_ENV === 'production' ? publicPath : publicPath,
    plugins: createVitePlugins(env, command === 'build', publicPath),
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // 设置路径
        '~': path.resolve(__dirname, './'),
        // 设置别名
        '@': path.resolve(__dirname, './src'),
      },
      // https://cn.vitejs.dev/config/#resolve-extensions
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
    },
    // 打包配置
    build: {
      // https://vite.dev/config/build-options.html
      sourcemap: command === 'build' ? false : 'inline',
      outDir: 'dist',
      assetsDir: 'assets',
      chunkSizeWarningLimit: 2000,
      // 多入口配置 - Vite使用rollupOptions.input
      rollupOptions: {
        input: {
          main: path.resolve(__dirname, 'index.html'),
        },
        output: {
          chunkFileNames: 'static/js/[name]-[hash].js',
          entryFileNames: 'static/js/[name]-[hash].js',
          assetFileNames: 'static/[ext]/[name]-[hash].[ext]',
          manualChunks: {
            'element-plus': ['element-plus'],
            vendor: ['vue', 'vue-router', 'pinia'],
          },
        },
      },
    },
    // vite 相关配置
    server: {
      port: Number(port) || 80,
      host: true,
      open: true,
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        '/dev-api': {
          target: backendUrl,
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/dev-api/, ''),
          // Vite 没有与Vue CLI 的 proxy中直接的 router 选项，但可以用 configure 实现动态代理
          configure: (proxy, options) => {
            (options as any).target = createProxy();
          },
        },
        // springdoc proxy
        '^/v3/api-docs/(.*)': {
          target: backendUrl,
          changeOrigin: true,
        },
      },
    },
    css: {
      postcss: {
        plugins: [
          {
            postcssPlugin: 'internal:charset-removal',
            AtRule: {
              charset: (atRule: any) => {
                if (atRule.name === 'charset') {
                  atRule.remove();
                }
              },
            },
          } as any,
        ],
      },
    },
  };
};

export default defineConfig(config);
