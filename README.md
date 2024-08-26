# LongQin-Java

#### 功能说明
自定义表单 自定义流程 自定义列表 权限管理 部门管理 职位管理 用户管理 公告管理

#### 演示直达
1、.NET版本：https://www.signsing.com
默认账号密码：custom/11111111
1、JAVA版本：暂无资金租赁服务器进行部署~~~

#### 技术架构说明
数据库: mysql5.6.26以上
jdk: 1.8
springboot: 2.1.4
redis: 任意版本
vue: 3.4.31
element-plus: 2.7.6
sortablejs: 1.15.2
vform3-builds: <a href="https://www.vform666.com/" target="_blank">3.0.10</a>

#### 项目结构说明
longqin-business: 业务微服务，默认端口: 9162
longqin-system: 账号微服务，默认端口: 9161
longqin-register: 微服务注册中心，默认端口: 9160
longqin-zuul: 微服务网关，默认端口: 9163
longqin-web: 前端代码，默认端口: 3000

#### 代码运行说明

1.  下载源码后先在mysql数据库（5.6以上版本）中创建名为"longqin"的数据库；
2.  在"longqin"数据库中执行"数据库.sql"脚本；
3.  更改各微服务配置文件中数据库连接配置，如在开发环境配置文件application-dev.yml中更改spring.datasource相关信息，测试及生产环境同理；
4.  安装并启动redis，默认端口6379，默认密码123456，若默认端口或者密码不一致，在开发环境配置文件application-dev.yml中更改，测试及生产环境同理；
5.  启动后端4个微服务，用vscode加载前端代码，更改前端.env.development文件中VITE_APP_API_URL为后端微服务的ip和端口；
6.  在vscode中执行pnpm install命令安装依赖包，安装成功后执行pnpm run dev命令启动；
7.  在登录页面中输入默认超级管理员账号密码admin/11111111；
8.  一般性操作流程：新增公司->新增角色->分配权限->新增部门->新增职位->新增账号->表单设计->流程设计->列表设计；
9.  工作流节点可配置处理部门、处理职位及处理人任意一项或多项，系统自动寻找相关处理人，若三项都未配置，则系统根据提交人额定部门职位自动匹配相关处理人。
