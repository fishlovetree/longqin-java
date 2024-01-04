import BaseLayout from '../../layouts/BaseLayout.vue';
import Login from '../../views/login/index.vue';


export default [
  {
    path: '/',
    redirect: '/workSpace'
  },
  {
    path: '/login',
    component: Login,
    meta: { title: '登录页面' },
  },
  {
    path: '/workspace',
    redirect: '/home/view',
    component: BaseLayout,
    meta: { title: '工作空间' },
    children: [
      {
        path: '/home/view',
        name: 'home',
        component: () => import('../../views/workSpace/workbench/index.vue'),
        meta: { title: '工作台', requireAuth: true, affix: true, closable: false },
      }
    ]
  }, {
    path: '/system',
    component: BaseLayout,
    meta: { title: '系统管理' },
    children: [
      {
        path: '/user/view',
        component: () => import('../../views/system/user/index.vue'),
        meta: { title: '用户管理', requireAuth: true },
      },
      {
        path: '/role/view',
        component: () => import('../../views/system/role/index.vue'),
        meta: { title: '角色管理', requireAuth: true },
      },
      {
        path: '/menu/view',
        component: () => import('../../views/system/menu/index.vue'),
        meta: { title: '菜单管理', requireAuth: true },
      },
      {
        path: '/organization/view',
        component: () => import('../../views/system/organization/index.vue'),
        meta: { title: '公司管理', requireAuth: true },
      },
      {
        path: '/department/view',
        component: () => import('../../views/system/department/index.vue'),
        meta: { title: '部门管理', requireAuth: true },
      },
      {
        path: '/position/view',
        component: () => import('../../views/system/position/index.vue'),
        meta: { title: '职位管理', requireAuth: true },
      },
      {
        path: '/log/view',
        component: () => import('../../views/system/log/index.vue'),
        meta: { title: '系统日志', requireAuth: true },
      },
      {
        path: '/errorlog/view',
        component: () => import('../../views/system/errorlog/index.vue'),
        meta: { title: '错误日志', requireAuth: true },
      },
      {
        path: '/notice/view',
        component: () => import('../../views/system/notice/index.vue'),
        meta: { title: '公告管理', requireAuth: true },
      },
      {
        path: '/baseset/view',
        component: () => import('../../views/system/baseset/index.vue'),
        meta: { title: '系统设置', requireAuth: true },
      },
    ]
  }
]
