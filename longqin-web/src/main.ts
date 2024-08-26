import { createApp } from "vue";
import App from "./App.vue";
import setupPlugins from "@/plugins";
import VForm3 from 'vform3-builds';  //引入VForm3库
import VFormRender from 'vform3-builds/dist/render.umd.js'  //引入VFormRender组件

// 本地SVG图标
import "virtual:svg-icons-register";

// 样式
// import "element-plus/theme-chalk/dark/css-vars.css";
import ElementPlus from 'element-plus'  //引入element-plus库
import 'element-plus/dist/index.css'  //引入element-plus样式
import "@/styles/index.scss";
import "uno.css";
import "animate.css";
import 'vform3-builds/dist/designer.style.css'  //引入VForm3样式
import 'vform3-builds/dist/render.style.css'  //引入VFormRender样式

const app = createApp(App);
app.use(setupPlugins);
app.use(ElementPlus);
app.use(VForm3);
app.use(VFormRender);  //全局注册VFormRender等组件
app.mount("#app");
