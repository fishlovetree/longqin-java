import type { App } from "vue";
import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";

export const Layout = () => import("@/layout/index.vue");

// 静态路由
export const constantRoutes: RouteRecordRaw[] = [
  {
    path: "/redirect",
    component: Layout,
    meta: { hidden: true },
    children: [
      {
        path: "/redirect/:path(.*)",
        component: () => import("@/views/redirect/index.vue"),
        meta: { hidden: true },
      },
    ],
  },
  {
    path: '/',
    redirect: '/workSpace',
    meta: { hidden: true },
  },
  {
    path: '/login',
    component: () => import("@/views/login/index.vue"),
    meta: { title: '登录页面', hidden: true },
  },
  {
    path: '/setUser',
    component: Layout,
    meta: { title: '账号设置', hidden: true },
    children: [
      {
        path: '/user/baseset',
        name: 'setUser',
        component: () => import('@/views/system/user/baseset.vue'),
        meta: { title: '账号设置', hidden: true},
      },
    ]
  },
  {
    path: '/workspace',
    redirect: '/home/view',
    component: Layout,
    meta: { title: '工作空间' },
    children: [
      {
        path: '/home/view',
        name: 'workSpace',
        component: () => import('@/views/workSpace/index.vue'),
        meta: { title: '工作台', requireAuth: true, affix: true, closable: false },
      },
      {
        path: '/notice/view',
        name: 'notice',
        component: () => import('@/views/system/notice/index.vue'),
        meta: { title: '公告管理', requireAuth: true },
      },
      {
        path: '/notice/display',
        name: 'noticeDisplay',
        component: () => import('@/views/system/notice/display.vue'),
        meta: { title: '公告列表', requireAuth: true, hidden: true },
      },
      {
        path: '/baseset/view',
        name: 'baseset',
        component: () => import('@/views/system/baseset/index.vue'),
        meta: { title: '系统设置', requireAuth: true },
      },
    ]
  }, {
    path: '/work',
    component: Layout,
    meta: { title: '我的工作' },
    children: [
      {
        path: '/backlog/view',
        name: 'backlog',
        component: () => import('@/views/work/backlog/index.vue'),
        meta: { title: '待办工作', requireAuth: true },
      },
      {
        path: '/completed/view',
        name: 'completed',
        component: () => import('@/views/work/completed/index.vue'),
        meta: { title: '已办工作', requireAuth: true },
      },
      {
        path: '/startflow/view',
        name: 'startflow',
        component: () => import('@/views/work/startflow/index.vue'),
        meta: { title: '流程发起', requireAuth: true },
      },
      {
        path: '/startflow/start',
        name: 'start',
        component: () => import('@/views/work/startflow/start.vue'),
        meta: { title: '启动流程', requireAuth: true, hidden: true },
      },
      {
        path: '/backlog/deal',
        name: 'deal',
        component: () => import('@/views/work/backlog/deal.vue'),
        meta: { title: '流程处理', requireAuth: true, hidden: true },
      },
      {
        path: '/completed/details',
        name: 'details',
        component: () => import('@/views/work/completed/details.vue'),
        meta: { title: '流程明细', requireAuth: true, hidden: true },
      },
    ]
  }, 
  {
    path: '/formDesign',
    component: Layout,
    meta: { title: '自定义表单' },
    children: [
      {
        path: '/formDesigner/view',
        name: 'formDesigner',
        component: () => import('@/views/formDesign/formDesigner/index.vue'),
        meta: { title: '表单设计器', requireAuth: true },
      },
      {
        path: '/formList/view',
        name: 'formList',
        component: () => import('@/views/formDesign/formList/index.vue'),
        meta: { title: '表单列表', requireAuth: true },
      },
    ]
  }, 
  {
    path: '/flowDesign',
    component: Layout,
    meta: { title: '自定义流程' },
    children: [
      {
        path: '/flowDesigner/view',
        name: 'flowDesigner',
        component: () => import('@/views/flowDesign/flowDesigner/index.vue'),
        meta: { title: '流程设计器', requireAuth: true },
      },
      {
        path: '/flowList/view',
        name: 'flowList',
        component: () => import('@/views/flowDesign/flowList/index.vue'),
        meta: { title: '流程列表', requireAuth: true },
      },
    ]
  },  
  {
    path: '/tableDesign',
    component: Layout,
    meta: { title: '自定义列表' },
    children: [
      {
        path: '/tableDesigner/view',
        name: 'tableDesigner',
        component: () => import('@/views/tableDesign/tableDesigner/index.vue'),
        meta: { title: '列表设计器', requireAuth: true },
      },
      {
        path: '/tableList/view',
        name: 'tableList',
        component: () => import('@/views/tableDesign/tableList/index.vue'),
        meta: { title: '列表清单', requireAuth: true },
      },
      {
        path: '/diytable/view/:id',
        name: 'diytable',
        component: () => import('@/views/tableDesign/diytable/index.vue'),
        meta: { title: '表单标题', requireAuth: true },
      },
    ]
  }, 
  {
    path: '/system',
    component: Layout,
    meta: { title: '系统管理' },
    children: [
      {
        path: '/user/view',
        name: 'user',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', requireAuth: true },
      },
      {
        path: '/user/addressList',
        name: 'addressList',
        component: () => import('@/views/system/user/addressList.vue'),
        meta: { title: '通讯录', requireAuth: true, hidden: true },
      },
      {
        path: '/role/view',
        name: 'role',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', requireAuth: true },
      },
      {
        path: '/menu/view',
        name: 'menu',
        component: () => import('@/views/system/menu/index.vue'),
        meta: { title: '菜单管理', requireAuth: true },
      },
      {
        path: '/organization/view',
        name: 'organization',
        component: () => import('@/views/system/organization/index.vue'),
        meta: { title: '公司管理', requireAuth: true },
      },
      {
        path: '/department/view',
        name: 'department',
        component: () => import('@/views/system/department/index.vue'),
        meta: { title: '部门管理', requireAuth: true },
      },
      {
        path: '/position/view',
        name: 'position',
        component: () => import('@/views/system/position/index.vue'),
        meta: { title: '职位管理', requireAuth: true },
      },
      {
        path: '/log/view',
        name: 'log',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '系统日志', requireAuth: true },
      },
      {
        path: '/errorlog/view',
        name: 'errorlog',
        component: () => import('@/views/system/errorlog/index.vue'),
        meta: { title: '错误日志', requireAuth: true },
      },
    ]
  }
];

/**
 * 创建路由
 */
const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
  // 刷新时，滚动条位置还原
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// 全局注册 router
export function setupRouter(app: App<Element>) {
  app.use(router);
}

/**
 * 重置路由
 */
export function resetRouter() {
  router.replace({ path: "/login" });
}

export default router;
