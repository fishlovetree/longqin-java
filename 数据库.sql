/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50626
 Source Host           : localhost:3306
 Source Schema         : longqin

 Target Server Type    : MySQL
 Target Server Version : 50626
 File Encoding         : 65001

 Date: 01/11/2023 14:03:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for des_approval
-- ----------------------------
DROP TABLE IF EXISTS `des_approval`;
CREATE TABLE `des_approval`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `approval_status` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否通过',
  `details` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '意见明细',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of des_approval
-- ----------------------------

-- ----------------------------
-- Table structure for des_form
-- ----------------------------
DROP TABLE IF EXISTS `des_form`;
CREATE TABLE `des_form`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `json_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表单json',
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库表名',
  `form_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表单名称',
  `is_approval` tinyint(4) NULL DEFAULT 0 COMMENT '是否审批：1-是，0-否',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of des_form
-- ----------------------------
INSERT INTO `des_form` VALUES (1, '[     {         \"index\": 0,         \"tag\": \"radio\",         \"name\": \"approvalStatus\",         \"label\": \"审批意见\",         \"labelwidth\": 110,         \"width\": 100,         \"disabled\": false,         \"labelhide\": false,         \"options\": [             {                 \"title\": \"是\",                 \"value\": \"1\",                 \"checked\": true             },             {                 \"title\": \"否\",                 \"value\": \"0\",                 \"checked\": false             }         ]     },     {         \"index\": 1,         \"tag\": \"textarea\",         \"name\": \"details\",         \"label\": \"意见明细\",         \"placeholder\": \"请输入\",         \"default\": \"\",         \"maxlength\": \"\",         \"labelwidth\": 110,         \"width\": 100,         \"required\": false,         \"readonly\": false,         \"disabled\": false,         \"labelhide\": false     } ]', 'des_approval', '默认审批表单', 1, 1, '2023-09-19 09:12:52', 0, 1);

-- ----------------------------
-- Table structure for diy_table
-- ----------------------------
DROP TABLE IF EXISTS `diy_table`;
CREATE TABLE `diy_table`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列表名称',
  `data_source` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据源表名',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of diy_table
-- ----------------------------

-- ----------------------------
-- Table structure for diy_table_columns
-- ----------------------------
DROP TABLE IF EXISTS `diy_table_columns`;
CREATE TABLE `diy_table_columns`  (
  `table_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '列表ID',
  `table_column` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '列表字段',
  `column_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `column_index` int(11) NULL DEFAULT NULL COMMENT '字段排序',
  `width` int(11) NULL DEFAULT NULL COMMENT '字段宽度',
  `order_by` tinyint(4) NULL DEFAULT 0 COMMENT '是否排序：0-不排序，1-升序，2-降序',
  `seearch_type` tinyint(4) NULL DEFAULT NULL COMMENT '是否搜索：0-否，1-等于，2-模糊查询，3-介于',
  `formula` tinyint(4) NULL DEFAULT NULL COMMENT '公式：0-否，1-加，2-减，3-乘，4-除，5-拼接',
  `formula_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公式值',
  `column_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字段字符类型',
  PRIMARY KEY (`table_id`, `table_column`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of diy_table_columns
-- ----------------------------

-- ----------------------------
-- Table structure for sys_department
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department`  (
  `department_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `department_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `parent_id` int(11) NULL DEFAULT -1 COMMENT '上级部门ID',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `creator` int(11) NULL DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`department_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_department
-- ----------------------------
INSERT INTO `sys_department` VALUES (1, '总经办', -1, '总经办', '2023-10-27 13:54:08', 1, 1, 1);
INSERT INTO `sys_department` VALUES (2, '行政办', 1, '行政办', '2023-10-27 14:07:40', 1, 1, 1);

-- ----------------------------
-- Table structure for sys_errorlog
-- ----------------------------
DROP TABLE IF EXISTS `sys_errorlog`;
CREATE TABLE `sys_errorlog`  (
  `log_id` int(8) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(8) NOT NULL COMMENT '操作人ID',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人IP地址',
  `broswer` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '浏览器',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常消息',
  `stacktrace` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常堆栈',
  `action` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常方法',
  `error_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '异常类',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '异常时间',
  PRIMARY KEY (`log_id`) USING BTREE,
  UNIQUE INDEX `pk_sys_errorlog_id`(`log_id`) USING BTREE,
  INDEX `pk_sys_errorlog_userid`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_errorlog
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主题',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '-1' COMMENT '描述',
  `controller_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '控制器名称',
  `action_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `action_parameters` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数',
  `creator` int(11) NULL DEFAULT NULL COMMENT '操作人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作人IP',
  `operate_type` tinyint(4) NULL DEFAULT NULL COMMENT '操作类型：增 -0;删-1;改-2；启用-3；停用-4；请求-5；响应-6；设置-7',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单路径',
  `parent_id` int(11) NULL DEFAULT -1 COMMENT '上级部门ID',
  `group_seq` int(11) NULL DEFAULT NULL COMMENT '菜单排序',
  `menu_icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `controller` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '控制器名',
  `action` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `creator` int(11) NULL DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (1, '系统管理', '/system', -1, 5, 'layui-icon-set', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (2, '菜单管理', '/menu/view', 1, 7, 'layui-icon-spread-left', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (3, '用户管理', '/user/view', 1, 5, 'layui-icon-user', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (4, '公司管理', '/organization/view', 1, 1, 'layui-icon-transfer', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (5, '部门管理', '/department/view', 1, 3, 'layui-icon-transfer', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (6, '角色管理', '/role/view', 1, 2, 'layui-icon-user', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (7, '系统日志', '/log/view', 1, 6, 'layui-icon-survey', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (8, '自定义表单', '/formdesign', -1, 2, 'layui-icon-form', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (9, '表单设计器', '/formdesign/view', 8, 2, 'layui-icon-form', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (10, '表单列表', '/formlist/view', 8, 1, 'layui-icon-form', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (11, '自定义流程', '/flowdesign', -1, 3, 'layui-icon-template-one', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (12, '流程设计器', '/flowdesign/view', 11, 2, 'layui-icon-template-one', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (13, '职位管理', '/position/view', 1, 4, 'layui-icon-user', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (14, '流程列表', '/flowlist/view', 11, 1, 'layui-icon-align-left', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (15, '我的工作', '/work', -1, 1, 'layui-icon-app', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (16, '待办工作', '/backlog/view', 15, 1, 'layui-icon-app', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (17, '已办工作', '/completed/view', 15, 2, 'layui-icon-app', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (18, '工作空间', '/workspace', -1, 0, 'layui-icon-home', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (19, '工作台', '/home/view', 18, 0, 'layui-icon-home', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (20, '系统设置', '/baseset/view', 18, 1, 'layui-icon-set', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (21, '流程发起', '/startflow/view', 15, 3, 'layui-icon-link', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (22, '公告管理', '/notice/view', 18, 3, 'layui-icon-notice', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (23, '自定义列表', '/table', -1, 4, 'layui-icon-table', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (24, '列表清单', '/tablelist/view', 23, 1, 'layui-icon-table', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (25, '列表设计器', '/tabledesign/view', 23, 2, 'layui-icon-table', '', '', '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (27, '错误日志', '/errorlog/view', 1, 7, 'layui-icon-error', NULL, NULL, '2023-09-19 09:51:52', 0, 1, 1);
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `menu_url`, `parent_id`, `group_seq`, `menu_icon`, `controller`, `action`, `create_time`, `organization_id`, `status`, `creator`) VALUES (29, '请假列表', '/diytable/view/1', 23, 3, 'layui-icon-form', NULL, NULL, '2023-11-30 16:39:37', 1, 1, 3);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主题',
  `notice_level` tinyint(4) NULL DEFAULT -1 COMMENT '紧急程度：1-普通，2-紧急，3-加急',
  `security` tinyint(4) NULL DEFAULT NULL COMMENT '保密程度：1-公开，2-内部公开，3-机密',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `creator` int(11) NULL DEFAULT NULL COMMENT '操作人',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '公告内容',
  `attachments` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice_files
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_files`;
CREATE TABLE `sys_notice_files`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主表ID',
  `file_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径',
  PRIMARY KEY (`notice_id`, `file_path`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_notice_files
-- ----------------------------

-- ----------------------------
-- Table structure for sys_organization
-- ----------------------------
DROP TABLE IF EXISTS `sys_organization`;
CREATE TABLE `sys_organization`  (
  `organization_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `organization_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司编码',
  `organization_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '-1' COMMENT '公司名称',
  `parent_id` int(11) NULL DEFAULT -1 COMMENT '上级ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体地址',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `logo_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'LOGO',
  `system_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '系统名称',
  `introduction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '公司简介',
  PRIMARY KEY (`organization_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_organization
-- ----------------------------
INSERT INTO `sys_organization` VALUES (1, '111', '龙琴科技', -1, '2023-10-26 15:16:54', 1, '1111', '13616526044', '123456', NULL, '2222');

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position`  (
  `position_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `position_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '-1' COMMENT '职位名称',
  `parent_id` int(11) NULL DEFAULT -1 COMMENT '上级ID',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `position_level` tinyint(4) NULL DEFAULT NULL COMMENT '职位等级：1-基层，2-中层，3-高层',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位简介',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`position_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_position
-- ----------------------------
INSERT INTO `sys_position` VALUES (1, '总经理', -1, '2023-10-27 14:44:15', 1, 3, 1, '123456', 1);
INSERT INTO `sys_position` VALUES (2, '行政主管', 1, '2023-10-27 14:51:52', 1, 2, 1, NULL, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '-1' COMMENT '角色名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '职位简介',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', '2023-10-27 14:52:33', 1, 1, '111', 1);

-- ----------------------------
-- Table structure for sys_rolemenu
-- ----------------------------
DROP TABLE IF EXISTS `sys_rolemenu`;
CREATE TABLE `sys_rolemenu`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_rolemenu
-- ----------------------------
INSERT INTO `sys_rolemenu` VALUES (1, 1);
INSERT INTO `sys_rolemenu` VALUES (1, 2);
INSERT INTO `sys_rolemenu` VALUES (1, 3);
INSERT INTO `sys_rolemenu` VALUES (1, 4);
INSERT INTO `sys_rolemenu` VALUES (1, 5);
INSERT INTO `sys_rolemenu` VALUES (1, 6);
INSERT INTO `sys_rolemenu` VALUES (1, 7);
INSERT INTO `sys_rolemenu` VALUES (1, 8);
INSERT INTO `sys_rolemenu` VALUES (1, 9);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '账号名称',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '密码',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `department_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `position_id` int(11) NULL DEFAULT NULL COMMENT '职位ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '2023-09-19 11:06:47', 1, 'heyunche@sina.com', '13616526044', NULL, 0, 0, NULL, 0);
INSERT INTO `sys_user` VALUES (2, 'custom', 'e10adc3949ba59abbe56e057f20f883e', '用户1', '2023-10-27 14:59:27', 1, NULL, NULL, NULL, 1, 1, '4444', 1);

-- ----------------------------
-- Table structure for sys_userrole
-- ----------------------------
DROP TABLE IF EXISTS `sys_userrole`;
CREATE TABLE `sys_userrole`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_userrole
-- ----------------------------
INSERT INTO `sys_userrole` VALUES (2, 1);

-- ----------------------------
-- Table structure for wf_entrust
-- ----------------------------
DROP TABLE IF EXISTS `wf_entrust`;
CREATE TABLE `wf_entrust`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `entruster` int(11) NOT NULL COMMENT '委托人',
  `trustee` int(11) NOT NULL COMMENT '被委托人',
  `all_time` tinyint(4) NULL DEFAULT 1 COMMENT '1:一直有效，0:否',
  `begin_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_entrust
-- ----------------------------

-- ----------------------------
-- Table structure for wf_entrust_detail
-- ----------------------------
DROP TABLE IF EXISTS `wf_entrust_detail`;
CREATE TABLE `wf_entrust_detail`  (
  `entrust_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '委托表ID',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  PRIMARY KEY (`entrust_id`, `flow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_entrust_detail
-- ----------------------------

-- ----------------------------
-- Table structure for wf_flow
-- ----------------------------
DROP TABLE IF EXISTS `wf_flow`;
CREATE TABLE `wf_flow`  (
  `flow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流程名称',
  `flow_sort` tinyint(4) NOT NULL COMMENT '流程类别',
  `flow_param` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '流程参数',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`flow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_flow
-- ----------------------------

-- ----------------------------
-- Table structure for wf_link
-- ----------------------------
DROP TABLE IF EXISTS `wf_link`;
CREATE TABLE `wf_link`  (
  `link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `link_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '连线名称',
  `from_node_id` int(11) NOT NULL COMMENT '起始节点ID',
  `to_node_id` int(11) NOT NULL COMMENT '终到节点ID',
  `form_id` int(11) NULL DEFAULT 1 COMMENT '表单ID',
  `field` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件字段',
  `operator` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '比较符号：><=等',
  `operator_value` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '条件值',
  `position_x` int(11) NULL DEFAULT NULL COMMENT '连线位置：x轴',
  `position_y` int(11) NULL DEFAULT NULL COMMENT '连线位置：y轴',
  `flow_id` int(11) NULL DEFAULT NULL COMMENT '流程ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`link_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_link
-- ----------------------------

-- ----------------------------
-- Table structure for wf_node
-- ----------------------------
DROP TABLE IF EXISTS `wf_node`;
CREATE TABLE `wf_node`  (
  `node_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '节点名称',
  `node_type` tinyint(4) NOT NULL COMMENT '节点类型：1-普通节点，2-分流节点，3-合流节点，4-分合流点',
  `group_seq` int(11) NULL DEFAULT NULL COMMENT '节点排序',
  `form_id` int(11) NULL DEFAULT 1 COMMENT '表单ID',
  `virtual` tinyint(4) NULL DEFAULT 0 COMMENT '是否虚拟节点：1-是，0-否',
  `cooperation` tinyint(4) NULL DEFAULT 0 COMMENT '是否多人协作：1-是，0-否',
  `department_id` int(11) NULL DEFAULT NULL COMMENT '处理部门ID',
  `position_x` int(11) NULL DEFAULT NULL COMMENT '节点位置：x轴',
  `position_y` int(11) NULL DEFAULT NULL COMMENT '节点位置：y轴',
  `position_id` int(11) NULL DEFAULT NULL COMMENT '处理职位ID',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '处理人ID',
  `is_approval` tinyint(4) NULL DEFAULT NULL COMMENT '是否审批：1-是，0-否',
  `flow_id` int(11) NULL DEFAULT NULL COMMENT '流程ID',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_node
-- ----------------------------

-- ----------------------------
-- Table structure for wf_process
-- ----------------------------
DROP TABLE IF EXISTS `wf_process`;
CREATE TABLE `wf_process`  (
  `process_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_id` int(11) NOT NULL COMMENT '实例ID',
  `node_id` int(11) NOT NULL COMMENT '节点ID',
  `link_id` int(11) NULL DEFAULT NULL COMMENT '连线ID',
  `sending_to` int(11) NOT NULL COMMENT '处理人',
  `process_type` tinyint(4) NULL DEFAULT 1 COMMENT '1-主送，2-抄送',
  `flag` tinyint(4) NULL DEFAULT 1 COMMENT '1-未读，2-已读',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1-运行中，0-关闭，2-就绪',
  PRIMARY KEY (`process_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_process
-- ----------------------------

-- ----------------------------
-- Table structure for wf_sort
-- ----------------------------
DROP TABLE IF EXISTS `wf_sort`;
CREATE TABLE `wf_sort`  (
  `sort_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sort_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '流程类别名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态:1-可用，0-删除',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`sort_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_sort
-- ----------------------------

-- ----------------------------
-- Table structure for wf_step
-- ----------------------------
DROP TABLE IF EXISTS `wf_step`;
CREATE TABLE `wf_step`  (
  `step_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_id` int(11) NOT NULL COMMENT '实例ID',
  `node_id` int(11) NOT NULL COMMENT '节点ID',
  `process_id` int(11) NULL DEFAULT NULL COMMENT '工作ID',
  `action` tinyint(4) NOT NULL DEFAULT 1 COMMENT '动作：1-前进，2-跳转，3-转办',
  `reason` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '跳转原因',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`step_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_step
-- ----------------------------

-- ----------------------------
-- Table structure for wf_work
-- ----------------------------
DROP TABLE IF EXISTS `wf_work`;
CREATE TABLE `wf_work`  (
  `work_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `organization_id` int(11) NULL DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '1-运行中，0-关闭，9-作废',
  `close_time` datetime(0) NULL DEFAULT NULL COMMENT '关闭时间',
  PRIMARY KEY (`work_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_work
-- ----------------------------

-- ----------------------------
-- Table structure for wf_workform
-- ----------------------------
DROP TABLE IF EXISTS `wf_workform`;
CREATE TABLE `wf_workform`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_id` int(11) NOT NULL COMMENT '实例ID',
  `node_id` int(11) NOT NULL COMMENT '节点ID',
  `process_id` int(11) NOT NULL COMMENT '工作ID',
  `form_data_id` int(11) NOT NULL COMMENT '表单数据ID',
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库表名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of wf_workform
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
