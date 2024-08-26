-- MySQL dump 10.13  Distrib 5.6.26, for Win64 (x86_64)
--
-- Host: localhost    Database: longqin
-- ------------------------------------------------------
-- Server version	5.6.26-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `des_approval_1`
--

DROP TABLE IF EXISTS `des_approval_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `des_approval_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `radio119648` varchar(50) DEFAULT NULL COMMENT '是否同意',
  `input48951` varchar(100) DEFAULT NULL COMMENT '备注',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='approvalForm';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `des_approval_1`
--

LOCK TABLES `des_approval_1` WRITE;
/*!40000 ALTER TABLE `des_approval_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `des_approval_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `des_form`
--

DROP TABLE IF EXISTS `des_form`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `des_form` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `json_data` text NOT NULL COMMENT '表单json',
  `table_name` varchar(50) NOT NULL COMMENT '数据库表名',
  `form_name` varchar(50) DEFAULT NULL COMMENT '表单名称',
  `is_approval` tinyint(4) DEFAULT '0' COMMENT '是否审批：1-是，0-否',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `des_form`
--

LOCK TABLES `des_form` WRITE;
/*!40000 ALTER TABLE `des_form` DISABLE KEYS */;
INSERT INTO `des_form` VALUES (1,'{\"widgetList\":[{\"key\":40519,\"type\":\"radio\",\"icon\":\"radio-field\",\"formItemFlag\":true,\"options\":{\"name\":\"radio119648\",\"label\":\"是否同意\",\"labelAlign\":\"\",\"defaultValue\":\"\",\"columnWidth\":\"200px\",\"size\":\"\",\"displayStyle\":\"inline\",\"buttonStyle\":false,\"border\":false,\"labelWidth\":null,\"labelHidden\":false,\"disabled\":false,\"hidden\":false,\"optionItems\":[{\"label\":\"同意\",\"value\":1},{\"label\":\"不同意\",\"value\":2}],\"required\":true,\"requiredHint\":\"\",\"validation\":\"\",\"validationHint\":\"\",\"customClass\":[],\"labelIconClass\":null,\"labelIconPosition\":\"rear\",\"labelTooltip\":null,\"onCreated\":\"\",\"onMounted\":\"\",\"onChange\":\"\",\"onValidate\":\"\"},\"id\":\"radio119648\"},{\"key\":23335,\"type\":\"input\",\"icon\":\"text-field\",\"formItemFlag\":true,\"options\":{\"name\":\"input48951\",\"label\":\"备注\",\"labelAlign\":\"\",\"type\":\"text\",\"defaultValue\":\"\",\"placeholder\":\"\",\"columnWidth\":\"200px\",\"size\":\"\",\"labelWidth\":null,\"labelHidden\":false,\"readonly\":false,\"disabled\":false,\"hidden\":false,\"clearable\":true,\"showPassword\":false,\"required\":false,\"requiredHint\":\"\",\"validation\":\"\",\"validationHint\":\"\",\"customClass\":\"\",\"labelIconClass\":null,\"labelIconPosition\":\"rear\",\"labelTooltip\":null,\"minLength\":null,\"maxLength\":null,\"showWordLimit\":false,\"prefixIcon\":\"\",\"suffixIcon\":\"\",\"appendButton\":false,\"appendButtonDisabled\":false,\"buttonIcon\":\"custom-search\",\"onCreated\":\"\",\"onMounted\":\"\",\"onInput\":\"\",\"onChange\":\"\",\"onFocus\":\"\",\"onBlur\":\"\",\"onValidate\":\"\",\"onAppendButtonClick\":\"\"},\"id\":\"input48951\"}],\"formConfig\":{\"modelName\":\"approval\",\"refName\":\"approvalForm\",\"rulesName\":\"rules\",\"labelWidth\":80,\"labelPosition\":\"left\",\"size\":\"\",\"labelAlign\":\"label-left-align\",\"cssCode\":\"\",\"customClass\":[],\"functions\":\"\",\"layoutType\":\"PC\",\"jsonVersion\":3,\"onFormCreated\":\"\",\"onFormMounted\":\"\",\"onFormDataChange\":\"\"}}','des_approval_1','approvalForm',0,17,'2024-08-13 14:50:08',1,1);
/*!40000 ALTER TABLE `des_form` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diy_table`
--

DROP TABLE IF EXISTS `diy_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diy_table` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(50) NOT NULL COMMENT '列表名称',
  `data_source` varchar(50) DEFAULT NULL COMMENT '数据源表名',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diy_table`
--

LOCK TABLES `diy_table` WRITE;
/*!40000 ALTER TABLE `diy_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `diy_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diy_table_columns`
--

DROP TABLE IF EXISTS `diy_table_columns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `diy_table_columns` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_id` int(11) NOT NULL COMMENT '列表id',
  `column_name` varchar(50) NOT NULL COMMENT '列表字段',
  `description` varchar(50) DEFAULT NULL COMMENT '字段描述',
  `column_index` int(11) DEFAULT NULL COMMENT '字段排序',
  `width` int(11) DEFAULT NULL COMMENT '字段宽度',
  `order_by` varchar(10) DEFAULT '0' COMMENT '是否排序：0-不排序，1-升序，2-降序',
  `search_type` varchar(10) DEFAULT NULL COMMENT '是否搜索：0-否，1-等于，2-模糊查询，3-介于',
  `formula` varchar(10) DEFAULT NULL COMMENT '公式：0-否，1-加，2-减，3-乘，4-除，5-拼接',
  `formula_value` varchar(50) DEFAULT NULL COMMENT '公式值',
  `column_type` varchar(50) DEFAULT NULL COMMENT '字段字符类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diy_table_columns`
--

LOCK TABLES `diy_table_columns` WRITE;
/*!40000 ALTER TABLE `diy_table_columns` DISABLE KEYS */;
/*!40000 ALTER TABLE `diy_table_columns` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_department`
--

DROP TABLE IF EXISTS `sys_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_department` (
  `department_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `department_name` varchar(50) NOT NULL COMMENT '部门名称',
  `parent_id` int(11) DEFAULT '-1' COMMENT '上级部门ID',
  `description` varchar(100) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `creator` int(11) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_department`
--

LOCK TABLES `sys_department` WRITE;
/*!40000 ALTER TABLE `sys_department` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_errorlog`
--

DROP TABLE IF EXISTS `sys_errorlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_errorlog` (
  `log_id` int(8) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` int(8) NOT NULL COMMENT '操作人ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作人IP地址',
  `broswer` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `message` text COMMENT '异常消息',
  `stacktrace` longtext COMMENT '异常堆栈',
  `action` text COMMENT '异常方法',
  `error_class` varchar(100) NOT NULL COMMENT '异常类',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '异常时间',
  PRIMARY KEY (`log_id`),
  UNIQUE KEY `pk_sys_errorlog_id` (`log_id`),
  KEY `pk_sys_errorlog_userid` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_errorlog`
--

LOCK TABLES `sys_errorlog` WRITE;
/*!40000 ALTER TABLE `sys_errorlog` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_errorlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) NOT NULL COMMENT '主题',
  `remark` varchar(100) DEFAULT '-1' COMMENT '描述',
  `controller_name` varchar(50) DEFAULT NULL COMMENT '控制器名称',
  `action_name` varchar(50) DEFAULT NULL COMMENT '方法名称',
  `action_parameters` text COMMENT '参数',
  `creator` int(11) DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `ip` varchar(50) DEFAULT NULL COMMENT '操作人IP',
  `operate_type` tinyint(4) DEFAULT NULL COMMENT '操作类型：增 -0;删-1;改-2；启用-3；停用-4；请求-5；响应-6；设置-7',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `menu_url` varchar(255) DEFAULT NULL COMMENT '菜单路径',
  `parent_id` int(11) DEFAULT '-1' COMMENT '上级部门ID',
  `group_seq` int(11) DEFAULT NULL COMMENT '菜单排序',
  `menu_icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `controller` varchar(50) DEFAULT NULL COMMENT '控制器名',
  `action` varchar(50) DEFAULT NULL COMMENT '方法名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `creator` int(11) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理','/system',-1,5,'layui-icon-set',NULL,NULL,'2023-09-19 09:51:52',0,1,1),(2,'菜单管理','/menu/view',1,7,'layui-icon-spread-left','','','2023-09-19 09:51:52',0,1,1),(3,'用户管理','/user/view',1,5,'layui-icon-user','','','2023-09-19 09:51:52',0,1,1),(4,'公司管理','/organization/view',1,1,'layui-icon-transfer','','','2023-09-19 09:51:52',0,1,1),(5,'部门管理','/department/view',1,3,'layui-icon-transfer','','','2023-09-19 09:51:52',0,1,1),(6,'角色管理','/role/view',1,2,'layui-icon-user','','','2023-09-19 09:51:52',0,1,1),(7,'系统日志','/log/view',1,6,'layui-icon-survey','','','2023-09-19 09:51:52',0,1,1),(8,'自定义表单','/formDesign',-1,2,'layui-icon-form',NULL,NULL,'2023-09-19 09:51:52',0,1,1),(9,'表单设计器','/formDesigner/view',8,2,'layui-icon-form','','','2023-09-19 09:51:52',0,1,1),(10,'表单列表','/formList/view',8,1,'layui-icon-form','','','2023-09-19 09:51:52',0,1,1),(11,'自定义流程','/flowDesign',-1,3,'layui-icon-template-one',NULL,NULL,'2023-09-19 09:51:52',0,1,1),(12,'流程设计器','/flowDesigner/view',11,2,'layui-icon-template-one','','','2023-09-19 09:51:52',0,1,1),(13,'职位管理','/position/view',1,4,'layui-icon-user','','','2023-09-19 09:51:52',0,1,1),(14,'流程列表','/flowList/view',11,1,'layui-icon-align-left','','','2023-09-19 09:51:52',0,1,1),(15,'我的工作','/work',-1,1,'layui-icon-app',NULL,NULL,'2023-09-19 09:51:52',0,1,1),(16,'待办工作','/backlog/view',15,1,'layui-icon-app','','','2023-09-19 09:51:52',0,1,1),(17,'已办工作','/completed/view',15,2,'layui-icon-app','','','2023-09-19 09:51:52',0,1,1),(18,'工作空间','/workspace',-1,0,'layui-icon-home',NULL,NULL,'2023-09-19 09:51:52',0,1,1),(19,'工作台','/home/view',18,0,'layui-icon-home','','','2023-09-19 09:51:52',0,1,1),(20,'系统设置','/baseset/view',18,1,'layui-icon-set','','','2023-09-19 09:51:52',0,1,1),(21,'流程发起','/startflow/view',15,3,'layui-icon-link','','','2023-09-19 09:51:52',0,1,1),(22,'公告管理','/notice/view',18,3,'layui-icon-notice','','','2023-09-19 09:51:52',0,1,1),(23,'自定义列表','/tableDesign',-1,4,'layui-icon-table',NULL,NULL,'2023-09-19 09:51:52',0,1,1),(24,'列表清单','/tableList/view',23,1,'layui-icon-table','','','2023-09-19 09:51:52',0,1,1),(25,'列表设计器','/tableDesigner/view',23,2,'layui-icon-table','','','2023-09-19 09:51:52',0,1,1),(27,'错误日志','/errorlog/view',1,7,'layui-icon-error',NULL,NULL,'2023-09-19 09:51:52',0,1,1);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_notice` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(50) NOT NULL COMMENT '主题',
  `notice_level` tinyint(4) DEFAULT '-1' COMMENT '紧急程度：1-普通，2-紧急，3-加急',
  `security` tinyint(4) DEFAULT NULL COMMENT '保密程度：1-公开，2-内部公开，3-机密',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `creator` int(11) DEFAULT NULL COMMENT '操作人',
  `content` text COMMENT '公告内容',
  `attachments` varchar(2000) DEFAULT NULL COMMENT '附件',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice_files`
--

DROP TABLE IF EXISTS `sys_notice_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_notice_files` (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主表ID',
  `file_path` varchar(200) NOT NULL COMMENT '文件路径',
  PRIMARY KEY (`notice_id`,`file_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice_files`
--

LOCK TABLES `sys_notice_files` WRITE;
/*!40000 ALTER TABLE `sys_notice_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_organization`
--

DROP TABLE IF EXISTS `sys_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_organization` (
  `organization_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `organization_code` varchar(50) DEFAULT NULL COMMENT '公司编码',
  `organization_name` varchar(100) DEFAULT '-1' COMMENT '公司名称',
  `parent_id` int(11) DEFAULT '-1' COMMENT '上级ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `address` varchar(100) DEFAULT NULL COMMENT '具体地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `logo_path` varchar(255) DEFAULT NULL COMMENT 'LOGO',
  `system_name` varchar(50) DEFAULT NULL COMMENT '系统名称',
  `introduction` varchar(255) DEFAULT NULL COMMENT '公司简介',
  PRIMARY KEY (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_organization`
--

LOCK TABLES `sys_organization` WRITE;
/*!40000 ALTER TABLE `sys_organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_position`
--

DROP TABLE IF EXISTS `sys_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_position` (
  `position_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `position_name` varchar(100) DEFAULT '-1' COMMENT '职位名称',
  `parent_id` int(11) DEFAULT '-1' COMMENT '上级ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `position_level` tinyint(4) DEFAULT NULL COMMENT '职位等级：1-基层，2-中层，3-高层',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) DEFAULT NULL COMMENT '职位简介',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_position`
--

LOCK TABLES `sys_position` WRITE;
/*!40000 ALTER TABLE `sys_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(100) DEFAULT '-1' COMMENT '角色名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) DEFAULT NULL COMMENT '职位简介',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_rolemenu`
--

DROP TABLE IF EXISTS `sys_rolemenu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_rolemenu` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_rolemenu`
--

LOCK TABLES `sys_rolemenu` WRITE;
/*!40000 ALTER TABLE `sys_rolemenu` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_rolemenu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL DEFAULT '-1' COMMENT '账号名称',
  `password` varchar(100) NOT NULL DEFAULT '-1' COMMENT '密码',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '昵称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `department_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `position_id` int(11) DEFAULT NULL COMMENT '职位ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','1bbd886460827015e5d605ed44252251','超级管理员','2023-09-19 11:06:47',1,'heyunche@sina.com','13616526044','/api/uploads/logo.png',0,0,NULL,0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_userrole`
--

DROP TABLE IF EXISTS `sys_userrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_userrole` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_userrole`
--

LOCK TABLES `sys_userrole` WRITE;
/*!40000 ALTER TABLE `sys_userrole` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_userrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_entrust`
--

DROP TABLE IF EXISTS `wf_entrust`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_entrust` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `entruster` int(11) NOT NULL COMMENT '委托人',
  `trustee` int(11) NOT NULL COMMENT '被委托人',
  `all_time` tinyint(4) DEFAULT '1' COMMENT '1:一直有效，0:否',
  `begin_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_entrust`
--

LOCK TABLES `wf_entrust` WRITE;
/*!40000 ALTER TABLE `wf_entrust` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_entrust` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_entrust_detail`
--

DROP TABLE IF EXISTS `wf_entrust_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_entrust_detail` (
  `entrust_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '委托表ID',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  PRIMARY KEY (`entrust_id`,`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_entrust_detail`
--

LOCK TABLES `wf_entrust_detail` WRITE;
/*!40000 ALTER TABLE `wf_entrust_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_entrust_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_flow`
--

DROP TABLE IF EXISTS `wf_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_flow` (
  `flow_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_name` varchar(50) NOT NULL COMMENT '流程名称',
  `flow_sort` tinyint(4) DEFAULT NULL COMMENT '流程类别',
  `flow_param` varchar(50) DEFAULT NULL COMMENT '流程参数',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`flow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_flow`
--

LOCK TABLES `wf_flow` WRITE;
/*!40000 ALTER TABLE `wf_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_link`
--

DROP TABLE IF EXISTS `wf_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_link` (
  `link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `link_name` varchar(50) DEFAULT NULL COMMENT '连线名称',
  `from_node_id` int(11) NOT NULL COMMENT '起始节点ID',
  `to_node_id` int(11) NOT NULL COMMENT '终到节点ID',
  `form_id` int(11) DEFAULT '1' COMMENT '表单ID',
  `field` varchar(50) DEFAULT NULL COMMENT '条件字段',
  `operator` varchar(10) DEFAULT NULL COMMENT '比较符号：><=等',
  `operator_value` varchar(50) DEFAULT NULL COMMENT '条件值',
  `position_x` int(11) DEFAULT NULL COMMENT '连线位置：x轴',
  `position_y` int(11) DEFAULT NULL COMMENT '连线位置：y轴',
  `flow_id` int(11) DEFAULT NULL COMMENT '流程ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`link_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_link`
--

LOCK TABLES `wf_link` WRITE;
/*!40000 ALTER TABLE `wf_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_node`
--

DROP TABLE IF EXISTS `wf_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_node` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_name` varchar(50) NOT NULL COMMENT '节点名称',
  `node_type` tinyint(4) NOT NULL COMMENT '节点类型：1-普通节点，2-分流节点，3-合流节点，4-分合流点',
  `group_seq` int(11) DEFAULT NULL COMMENT '节点排序',
  `form_id` int(11) DEFAULT '1' COMMENT '表单ID',
  `is_virtual` tinyint(4) DEFAULT '0' COMMENT '是否虚拟节点：1-是，0-否',
  `cooperation` tinyint(4) DEFAULT '0' COMMENT '是否多人协作：1-是，0-否',
  `department_id` int(11) DEFAULT NULL COMMENT '处理部门ID',
  `position_x` int(11) DEFAULT NULL COMMENT '节点位置：x轴',
  `position_y` int(11) DEFAULT NULL COMMENT '节点位置：y轴',
  `position_id` int(11) DEFAULT NULL COMMENT '处理职位ID',
  `user_id` int(11) DEFAULT NULL COMMENT '处理人ID',
  `is_approval` tinyint(4) DEFAULT NULL COMMENT '是否审批：1-是，0-否',
  `flow_id` int(11) DEFAULT NULL COMMENT '流程ID',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_node`
--

LOCK TABLES `wf_node` WRITE;
/*!40000 ALTER TABLE `wf_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_process`
--

DROP TABLE IF EXISTS `wf_process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_process` (
  `process_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_id` int(11) NOT NULL COMMENT '实例ID',
  `node_id` int(11) NOT NULL COMMENT '节点ID',
  `link_id` int(11) DEFAULT NULL COMMENT '连线ID',
  `sending_to` int(11) NOT NULL COMMENT '处理人',
  `process_type` tinyint(4) DEFAULT '1' COMMENT '1-主送，2-抄送',
  `flag` tinyint(4) DEFAULT '1' COMMENT '1-未读，2-已读',
  `submitter` int(11) DEFAULT NULL COMMENT '提交人',
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '1-运行中，0-关闭，2-就绪',
  PRIMARY KEY (`process_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_process`
--

LOCK TABLES `wf_process` WRITE;
/*!40000 ALTER TABLE `wf_process` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_sort`
--

DROP TABLE IF EXISTS `wf_sort`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_sort` (
  `sort_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sort_name` varchar(100) NOT NULL DEFAULT '-1' COMMENT '流程类别名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态:1-可用，0-删除',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`sort_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_sort`
--

LOCK TABLES `wf_sort` WRITE;
/*!40000 ALTER TABLE `wf_sort` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_sort` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_step`
--

DROP TABLE IF EXISTS `wf_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_step` (
  `step_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_id` int(11) NOT NULL COMMENT '实例ID',
  `node_id` int(11) NOT NULL COMMENT '节点ID',
  `process_id` int(11) DEFAULT NULL COMMENT '工作ID',
  `action` tinyint(4) NOT NULL DEFAULT '1' COMMENT '动作：1-前进，0-驳回，3-转办',
  `reason` varchar(100) DEFAULT '1' COMMENT '跳转原因',
  `submitter` int(11) DEFAULT NULL COMMENT '处理人',
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '处理时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  PRIMARY KEY (`step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_step`
--

LOCK TABLES `wf_step` WRITE;
/*!40000 ALTER TABLE `wf_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_work`
--

DROP TABLE IF EXISTS `wf_work`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_work` (
  `work_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_id` int(11) NOT NULL COMMENT '流程ID',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `organization_id` int(11) DEFAULT NULL COMMENT '组织机构ID',
  `status` tinyint(4) DEFAULT '1' COMMENT '1-运行中，0-关闭，9-作废',
  `close_time` datetime DEFAULT NULL COMMENT '关闭时间',
  PRIMARY KEY (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_work`
--

LOCK TABLES `wf_work` WRITE;
/*!40000 ALTER TABLE `wf_work` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_work` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wf_workform`
--

DROP TABLE IF EXISTS `wf_workform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wf_workform` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_id` int(11) NOT NULL COMMENT '实例ID',
  `node_id` int(11) NOT NULL COMMENT '节点ID',
  `process_id` int(11) NOT NULL COMMENT '工作ID',
  `form_data_id` int(11) NOT NULL COMMENT '表单数据ID',
  `table_name` varchar(50) DEFAULT NULL COMMENT '数据库表名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wf_workform`
--

LOCK TABLES `wf_workform` WRITE;
/*!40000 ALTER TABLE `wf_workform` DISABLE KEYS */;
/*!40000 ALTER TABLE `wf_workform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'longqin'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-08-26 16:43:10
