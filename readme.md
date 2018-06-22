﻿# boot-single

#### 项目介绍
boot-single基于SpringBoot2.0.2版本，集成项目中常用的功能点。默认配置下，用户仅需要自己配置单机mysql和redis组件，就可以快速启动项目。<br>

#### [在线演示](http://106.12.30.85)  &nbsp;[集群版本](https://gitee.com/bootstrap2table/boot_master)   &nbsp;[分布式事务](https://gitee.com/bootstrap2table/boot_master/tree/feature/jta/druid)  &nbsp;[SpringCloud](https://gitee.com/bootstrap2table/spring-cloud)   &nbsp;[Dubbo](https://gitee.com/bootstrap2table/api-master)

#### 技术选型
    ● 系统核心框架：SpringBoot
    ● 定时任务调度：ElasticJob+Zookeeper
    ● 数据持久框架：MyBatis
    ● 数据库连接池：Alibaba Druid
    ● 系统监控插件：JavaMelody+Druid
    ● 系统缓存框架：Redis
    ● 系统前端框架：Freemaker+Bootstrap+Layui
    ● 搜索引擎框架：Solr/SolrCloud
    ● 分布式线程锁：Redisson
    ● 分布式限流器：Redisson
    ● 系统消息队列：ActiveMq
    ● 安全授权框架：JwtToken+AES 
 
#### **项目特点**   
> * 配置App模块，针对相同接口重复提交，直接拒绝访问(针对多读情况，可手动关闭限制)。<br>
> * 配置接口限流器，接口端直接拒绝超过允许数量的请求，减轻服务器端在高并发环境下的压力。<br>
> * 配置坦克大战小游戏，让您在学习之余可以愉快的放松休息。<br>
> * 配套[代码生成工具](https://github.com/apple987/AutoCode):快速生成前后端代码，极大的提高开发效率。<br>
> * 引入[ApacheCommons](https://gitee.com/bootstrap2table/boot_master/blob/master/src/test/java/com/qdone/DemoApacheCommonsTest.java)工具包，大幅简化开发中的io,file,collection,jexl等处理过程 。<br>
> * 引入APP模块，根据token作为登录令牌，支持token自动续期，极大的方便了APP接口开发。<br>
> * 引入[HibernateValidator](https://gitee.com/bootstrap2table/boot_master/blob/master/src/main/java/com/qdone/module/controller/TestController.java)校验框架，轻松实现后端校验。<br>
> * 引入druid,javaMelody监控系统各项指标，分析系统瓶颈。<br>
> * 前端采用freemarker模板化引擎,页面采用bootstrap-table灵活强大的表格插件。<br>
> * 前端使用layui弹出层框架，极大的简化了弹出层的开发过程。
> * 前端采用JqueryValidate插件,快捷方便进行数据验证。<br>
> * 后端配置swagger在线文档，极大的降低前后端项目成员的沟通成本，快速同步文档。 <br>
> * 配置druid,fastjson,cors,xss,redis-cluster等组件服务。<br>
> * 配置全局异常处理，通用日志打印,pagehelper分页。<br>
> * 配置redisson集群模式,使用分布式锁，保证并发的数据一致性。<br>
> * 配置全局errorPage和welcomeFile完善全局异常处理，优化异常处理代码。<br>
> * 配置devtools热部署，针对page目录下的css,js,html页面资源修改之后，项目不需要重新启动。<br>
> * 配置elastic-job定时器，强悍的分布式定时任务配置。<br>
> * 配置fileupload(默认配置最大100MB)，下载文件，生成二维码，二维码打印，mail发邮件等功能。<br>
> * 配置https安全协议，提高系统安全性,配置log4j日志，系统出现异常自动发送邮件。<br>
> * 配置poi和csv简单导出excel功能点,poi目前是多sheet智能导出。<br>
> * 前端使用vkbeautify插件,页面格式化json，xml,css,sql数据显示。<br>
> * 配置[activeMq](https://gitee.com/bootstrap2table/boot_master/blob/master/src/test/java/com/qdone/DemoApplicationTests.java)支持同时发送队列和主题消息。<br>
> * 配置[solr和solrCloud](https://gitee.com/bootstrap2table/boot_master/blob/master/src/main/java/com/qdone/module/app/SolrDataController.java)支持分词搜索查询。<br>


#### **项目结构**
```
boot-master
│ 
├─doc  项目SQL语句
│ 
├─common 公共配置
│ 
├─framework 框架配置
│ 
├─modules 功能模块
│  ├─app API接口模块(APP调用)
│  ├─controller 系统模块
│  ├─mapper  mybatis的sql文件
│  ├─model   数据库实体类
│  └─service 业务逻辑层
│ 
├─StartUpApplication 项目启动类
│  
├──resources
│  ├─page 页面资源
│  │  ├─static 静态资源
│  │  │  ├─css  css样式
│  │  │  ├─js   js文件 
│  │  │  ├─images  图片文件 
│  │  │  └─plugins 前端插件
│  │  │
│  │  └─view  前端页面
│  │     ├─error 系统错误页
│  │     ├─inc   公共资源页面
│  │     └─其他   系统功能页面
│  │
│  ├─application.properties 配置文件
│  ├─banner.txt  自定义启动图标
│  ├─mybatis_config.xml mybatis配置项
│  └─secure.jks  ssl安全证书
```
#### **软件环境** 
- JDK1.8
- MySQL5.5+
- Maven3.0+
	 
#### **启动说明:**
- 1.下载boot-single分支源码，建议采用初始默认项目配置。<br>
- 2.创建mysql数据库isec实例(编码UTF-8),运行doc目录里面的sql文件。<br>
- 3.安装redis实例，启动redis(127.0.0.1:6379，密码:qdone)。<br>
- 4.运行StartUpApplication启动项目，浏览器访问http://localhost<br>
	
#### **友情链接：**
- GitHub：https://github.com/apple987/boot_walk <br>
- mycat版本: https://gitee.com/bootstrap2table/boot_master/tree/feature/mycat<br>
- sharding-jdbc版本: https://gitee.com/bootstrap2table/boot-sharding<br>
- jtaDruid版本: https://gitee.com/bootstrap2table/boot_master/tree/feature/jta/druid<br>
- 代码生成： https://github.com/apple987/AutoCode<br>

#### **问题反馈：**
- 意见反馈：https://gitee.com/bootstrap2table/boot_master/issues
- 作者姓名：付为地<br>
- 作者QQ: 1335157415<br>
- 作者邮箱: m15171479289@163.com<br>
- 加入QQ群：<br>
![boot-qq](https://github.com/apple987/static/raw/master/boot/image/qq.jpg "QQ群")<br>

### 项目截图
**项目启动效果图：** 
![boot-start](https://github.com/apple987/static/raw/master/boot/image/start.png "项目启动")<br>	
**Https跳转效果图：** 
![boot-ssl](https://github.com/apple987/static/raw/master/boot/image/ssl.png "初始化")<br>
**欢迎页面效果图：** 
![boot-tank](https://github.com/apple987/static/raw/master/boot/image/tank.jpg "欢迎页面")<br>
**学生管理效果图：** 
![boot-index](https://github.com/apple987/static/raw/master/boot/image/index.png "学生管理")<br>
**接口文档效果图：** 
![boot-swagger](https://github.com/apple987/static/raw/master/boot/image/swagger.png "swagger在线文档")<br>
**登录接口效果图：** 
![boot-applogin](https://github.com/apple987/static/raw/master/boot/image/appLogin.jpg "app登陆接口")<br>
**获取用户效果图：** 
![boot-appGetUser](https://github.com/apple987/static/raw/master/boot/image/appGetUser.jpg "app获得登陆信息接口")<br>
**邮件异常效果图：** 
![boot-emailError](https://github.com/apple987/static/raw/master/boot/image/emailError.jpg "邮件发送异常")<br>
**发送消息效果图：** 
![boot-runmq](https://github.com/apple987/static/raw/master/boot/image/runmq.jpg "发送MQ消息")<br>
**接收消息效果图：** 
![boot-mq](https://github.com/apple987/static/raw/master/boot/image/mq.jpg "MQ队列和订阅")<br>
**职员列表效果图：** 
![boot-selectStaff](https://github.com/apple987/static/raw/master/boot/image/selectStaff.jpg "职员信息列表")<br>
**添加职员效果图：** 
![boot-insertStaff](https://github.com/apple987/static/raw/master/boot/image/insertStaff.jpg "添加职员信息")<br>
**验证失败效果图：** 
![boot-insertError](https://github.com/apple987/static/raw/master/boot/image/insertStaffError.jpg "validate验证信息")<br>
**AlibabaDurid效果图：** 
![boot-durid](https://github.com/apple987/static/raw/master/boot/image/druid.png "durid监控")<br>
**JavaMelody效果图：** 
![boot-javaMelody](https://github.com/apple987/static/raw/master/boot/image/javaMelody.png "javaMelody监控")<br>
**生成二维码效果图：** 	
![boot-qrcode](https://github.com/apple987/static/raw/master/boot/image/qrcode.png "生成二维码")<br>
**打印二维码效果图：** 
![boot-print](https://github.com/apple987/static/raw/master/boot/image/print.png "打印二维码")<br>
**Solr操作效果图：** 
![boot-solr](https://github.com/apple987/static/raw/master/boot/image/solr.png "solr导入数据")<br>
**文本上传效果图：** 
![boot-upload](https://github.com/apple987/static/raw/master/boot/image/upload.jpg "文本上传")<br>
**上传出错效果图：**
![boot-uploadError](https://github.com/apple987/static/raw/master/boot/image/uploadError.jpg "文件上传异常")<br>
**限流生效效果图：**
![boot-ratelimter](https://github.com/apple987/static/raw/master/boot/image/ratelimter.jpg "限流接口请求")<br>

	

		
        
