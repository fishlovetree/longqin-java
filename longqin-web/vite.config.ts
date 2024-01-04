import { defineConfig, loadEnv, UserConfig, ConfigEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { LayuiVueResolver } from 'unplugin-vue-components/resolvers'
import { resolve } from "path";

const excludeComponents = ['LightIcon','DarkIcon']

export default defineConfig(({ mode }: ConfigEnv): UserConfig => {
  const env = loadEnv(mode, __dirname)
  return {
    base: env.VITE_MODE === 'production' ? './' : '/',
    resolve: {
      alias: [
        {
          find: '@',
          replacement: resolve(__dirname, './src')
        }
      ]
    },
    plugins: [
      AutoImport({
        imports: ["vue"],
        dts: "src/auto-imports.d.ts",
        resolvers: [
          LayuiVueResolver(),
        ],
      }),
      Components({
        resolvers: [
          LayuiVueResolver({
            resolveIcons: true,
            exclude: excludeComponents
          }),
        ],
      }),
      vue(),
    ],
    build: {
      sourcemap: false, // 不生成 source map 
      terserOptions: { 
        compress: { // 打包时清除 console 和 debug 相关代码
          drop_console: true,
          drop_debugger: true,
        },
      },
    },
    server: {
      host: '0.0.0.0',
      port: Number(env.VITE_APP_PORT),
      open: true, 
      proxy: {
        [env.VITE_APP_BASE_API]: {
          target: 'http://193.168.2.22:9163',  //后端微服务网关ip端口
          changeOrigin: true, 
          rewrite: (path) => path.replace(new RegExp("^" + env.VITE_APP_BASE_API), "") // 路径重写
        }
      }
    }
  }
});