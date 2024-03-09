# LongQin-Java

#### 功能说明
自定义表单 自定义流程 自定义列表 权限管理 部门管理 职位管理 用户管理 公告管理

#### 演示直达
https://www.signsing.com
账号密码：custom/11111111

#### 技术架构说明
数据库: mysql5.6.26
jdk: 1.8
springboot: 2.1.4
redis: 3.0.504
vue: 3.3.4
layui-vue: 2.10.0

#### 文档结构说明
longqin-business: 业务微服务
longqin-system: 账号微服务
longqin-register: 微服务注册中心
longqin-zuul: 微服务网关
longqin-web: 前端代码

#### 调试或部署说明

1.  下载源码后先在mysql数据库（5.6以上版本）中创建名为"longqin"的数据库；
2.  在"longqin"数据库中执行"数据库.sql"脚本；
3.  更改各微服务配置文件中数据库连接配置；
4.  安装并启动redis，默认端口6379，默认密码123456，若默认端口或者密码不一致，在配置文件application-test.yml中更改；
5.  在后端4个微服务都启动后，更改前端vite.conf.ts文件server.proxy.target中的ip地址和端口为后端微服务的ip和端口；
6.  部署默认超级管理员账号密码为admin/111111
