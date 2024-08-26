import { RouteRecordRaw } from "vue-router";
import { constantRoutes } from "@/router";
import { store } from "@/store";
import { Menu } from "@/api/menu";

const modules = import.meta.glob("../../views/**/**.vue");
const Layout = () => import("@/layout/index.vue");

export const usePermissionStore = defineStore("permission", () => {
  /**
   * 应用中所有的路由列表，包括静态路由和动态路由
   */
  const routes = ref<RouteRecordRaw[]>([]);
  /**
   * 混合模式左侧菜单列表
   */
  const mixLeftMenus = ref<RouteRecordRaw[]>([]);

  /**
   * 隐藏的固定路由
   */
  const hiddenRoutes = ref<RouteRecordRaw[]>([]);

  /**
   * 数组形态的固定路由
   */
   const routesArr = ref<RouteRecordRaw[]>([]);

  // actions
  function setRoutes(menus: RouteRecordRaw[]) {
    routes.value = constantRoutes;
    // 菜单权限控制
    if (menus){
      let paths = [];
      transformMenu(menus, paths);
      handlePerm(constantRoutes, paths);
    }
  }

  function handlePerm(constantRoutes: RouteRecordRaw[], paths:[]){
    constantRoutes.forEach((route: any)=>{
      let isHidden = true;
      if (paths.includes(route.path)){
        isHidden = false;
      }
      if (isHidden && route.meta){
        route.meta.hidden = true;
      }
      if (route.children){
        handlePerm(route.children, paths);
      }
    })
  }

  function transformMenu(menus:[], paths: []){
    menus.forEach((menu: any)=>{
      paths.push(menu.menuUrl);
      if (menu.children){
        transformMenu(menu.children, paths);
      }
    })
  }

  // 将固定路由加工处理
  function transformConstantRoutes(constantRoutes: RouteRecordRaw[]){
    constantRoutes.forEach((route: RouteRecordRaw)=>{
      routesArr.value.push(route);
      if (route.meta && route.meta.hidden){
        hiddenRoutes.value.push(route);
      }
      if (route.children){
        transformConstantRoutes(route.children);
      }
    })
  }

  /**
   * 转换路由数据为组件
   */
  const transformRoutes = (menus:Menu[]) => {
    const asyncRoutes: RouteRecordRaw[] = [];
    menus.forEach((menu) => {
      const tmpRoute = routesArr.value.find((item) => item.path === menu.menuUrl);
      // 顶级目录，替换为 Layout 组件
      if (tmpRoute) {
        if (menu.children) {
          tmpRoute.children = transformRoutes(menu.children);
        }
        asyncRoutes.push(tmpRoute);
      }
      else if (menu.menuUrl?.startsWith("/diytable/view/")){
        let tmp = reactive({
          path: menu.menuUrl,
          name: 'diytable',
          component: () => import('@/views/tableDesign/diytable/index.vue'),
          meta: { title: menu.menuName, requireAuth: true, params: { title: menu.menuName} },
        });
        asyncRoutes.push(tmp);
      }
    });
    return asyncRoutes;
  };

  /**
   * 生成动态路由
   */
  function generateRoutes() {
    return new Promise<RouteRecordRaw[]>((resolve, reject) => {
      // MenuAPI.getRoutes()
      //   .then((data) => {
      //     const dynamicRoutes = transformRoutes(data);
      //     routes.value = constantRoutes.concat(dynamicRoutes);
      //     resolve(dynamicRoutes);
      //   })
      //   .catch((error) => {
      //     reject(error);
      //   });
      // const accessedRoutes = JSON.parse(<string>localStorage.getItem("menu"));
			// setRoutes(accessedRoutes);
      // resolve(accessedRoutes);
      const menus = JSON.parse(<string>localStorage.getItem("menu"));
      transformConstantRoutes(constantRoutes);
      const dynamicRoutes = transformRoutes(menus);
      routes.value = hiddenRoutes.value.concat(dynamicRoutes);
			resolve(dynamicRoutes);
    });
  }

  /**
   * 混合模式菜单下根据顶部菜单路径设置左侧菜单
   *
   * @param topMenuPath - 顶部菜单路径
   */
  const setMixLeftMenus = (topMenuPath: string) => {
    const matchedItem = routes.value.find((item) => item.path === topMenuPath);
    if (matchedItem && matchedItem.children) {
      mixLeftMenus.value = matchedItem.children;
    }
  };

  return {
    routes,
    generateRoutes,
    mixLeftMenus,
    setMixLeftMenus,
  };
});

/**
 * 转换路由数据为组件
 */
// const transformRoutes = (routes: RouteVO[]) => {
//   const asyncRoutes: RouteRecordRaw[] = [];
//   routes.forEach((route) => {
//     const tmpRoute = { ...route } as RouteRecordRaw;
//     // 顶级目录，替换为 Layout 组件
//     if (tmpRoute.component?.toString() == "Layout") {
//       tmpRoute.component = Layout;
//     } else {
//       // 其他菜单，根据组件路径动态加载组件
//       const component = modules[`../../views/${tmpRoute.component}.vue`];
//       if (component) {
//         tmpRoute.component = component;
//       } else {
//         tmpRoute.component = modules[`../../views/error-page/404.vue`];
//       }
//     }

//     if (tmpRoute.children) {
//       tmpRoute.children = transformRoutes(route.children);
//     }

//     asyncRoutes.push(tmpRoute);
//   });

//   return asyncRoutes;
// };

/**
 * 用于在组件外部（如在Pinia Store 中）使用 Pinia 提供的 store 实例。
 * 官方文档解释了如何在组件外部使用 Pinia Store：
 * https://pinia.vuejs.org/core-concepts/outside-component-usage.html#using-a-store-outside-of-a-component
 */
export function usePermissionStoreHook() {
  return usePermissionStore(store);
}
