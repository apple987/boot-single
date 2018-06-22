﻿# boot-single

#### Project Introduction
boot-single is based on SpringBoot2.0.2 version, which integrates functional points commonly used in most projects. Under the default configuration, users can quickly start projects by configuring single MySQL and redis components themselves.。<br>

#### [OnlineDemo](http://106.12.30.85)  &nbsp;[ClusterVrsion](https://gitee.com/bootstrap2table/boot_master)   &nbsp;[DistributedTransaction](https://gitee.com/bootstrap2table/boot_master/tree/feature/jta/druid)  &nbsp;[SpringCloud](https://gitee.com/bootstrap2table/spring-cloud) 

#### Technology
    ● System Core Framework：SpringBoot
    ● Task Scheduling：ElasticJob+Zookeeper
    ● Data Persistence Framework：MyBatis
    ● Database Connection Pool：Alibaba Druid
    ● System Monitor：JavaMelody+Druid
    ● System Cache Framework：Redis
    ● System Front Framework：Freemaker+Bootstrap+Layui
    ● Search Engine Framework：Solr/SolrCloud
    ● Distributed Thread Lock：Redisson
    ● Distributed Current Limiter：Redisson
    ● System Message Queue：ActiveMq
    ● Security Authorization Framework：JwtToken+AES 
 
#### **Features**   
> * Configure the App module to repeat the same interface, and deny access directly.<br>
> * Configure the interface limiter, and the interface directly refuses requests exceeding the permitted number.<br>
> * Configure the game of Tank Wars, allowing you to relax and relax after your study.<br>
> * The APP module, using token authentication authority, supports token automatic renewal.<br>
> * Configure Druid and JavaMelody to monitor the index of the system and analyze the bottleneck of the system.<br>
> * Configure freemarker template engine with the page adopts bootstrap-table flexible.<br>



#### **Project Structure**
```
boot-master
│ 
├─doc  Project SQL Statement
│ 
├─common Public Configuration
│ 
├─framework Frame Configuration
│ 
├─modules  Functional Module
│  ├─app   API module
│  ├─controller System Module
│  ├─mapper  Sql File
│  ├─model   Database Entity 
│  └─service Business Module
│ 
├─StartUpApplication Startup Class
│  
├──resources
│  ├─page Page Resources
│  │  ├─static Static Resources
│  │  │  ├─css  Css Styles
│  │  │  ├─js   Js File
│  │  │  ├─images  Picture File 
│  │  │  └─plugins Plugins
│  │  │
│  │  └─view  Front-end page
│  │     ├─error System error page
│  │     ├─inc   Public resource page
│  │     └─other   System function page
│  │
│  ├─application.properties configuration file
│  ├─banner.txt  Custom boot Icon
│  ├─mybatis_config.xml configuration file
│  └─secure.jks  Security certificate
```
#### **Software Environment** 
- JDK1.8
- MySQL5.5+
- Maven3.0+
	 
#### **Start Instructions:**
- 1.Download the boot-single Branch source code with default project configuration.<br>
- 2.Create MySQL database ISEC instance (encode UTF-8), run the SQL file in doc directory.。<br>
- 3.Install the redis instance and start the redis(127.0.0.1:6379，password:qdone)。<br>
- 4.Run StartUpApplication startup project and browser access http://localhost<br>
	
#### **Friendship Link:**
- GitHub：https://github.com/apple987/boot_walk <br>
- mycat: https://gitee.com/bootstrap2table/boot_master/tree/feature/mycat<br>
- sharding-jdbc: https://gitee.com/bootstrap2table/boot-sharding<br>
- jtaDruid: https://gitee.com/bootstrap2table/boot_master/tree/feature/jta/druid<br>
- AutoCode： https://github.com/apple987/AutoCode<br>

#### **Question：**
- Feedback：https://gitee.com/bootstrap2table/boot_master/issues

### Project Screenshot
**Project start：** 
![boot-start](https://github.com/apple987/static/raw/master/boot/image/start.png "项目启动")<br>	
**Https：** 
![boot-ssl](https://github.com/apple987/static/raw/master/boot/image/ssl.png "初始化")<br>
**Welcome：** 
![boot-tank](https://github.com/apple987/static/raw/master/boot/image/tank.jpg "欢迎页面")<br>
**Student Management：** 
![boot-index](https://github.com/apple987/static/raw/master/boot/image/index.png "学生管理")<br>
**Interface document：** 
![boot-swagger](https://github.com/apple987/static/raw/master/boot/image/swagger.png "swagger在线文档")<br>
**Login interface：** 
![boot-applogin](https://github.com/apple987/static/raw/master/boot/image/appLogin.jpg "app登陆接口")<br>
**Get user interface：** 
![boot-appGetUser](https://github.com/apple987/static/raw/master/boot/image/appGetUser.jpg "app获得登陆信息接口")<br>
**Mail exception：** 
![boot-emailError](https://github.com/apple987/static/raw/master/boot/image/emailError.jpg "邮件发送异常")<br>
**Send message：** 
![boot-runmq](https://github.com/apple987/static/raw/master/boot/image/runmq.jpg "发送MQ消息")<br>
**Receive message：** 
![boot-mq](https://github.com/apple987/static/raw/master/boot/image/mq.jpg "MQ队列和订阅")<br>
**Staff Management：** 
![boot-selectStaff](https://github.com/apple987/static/raw/master/boot/image/selectStaff.jpg "职员信息列表")<br>
**Add the staff：** 
![boot-insertStaff](https://github.com/apple987/static/raw/master/boot/image/insertStaff.jpg "添加职员信息")<br>
**Validation failure：** 
![boot-insertError](https://github.com/apple987/static/raw/master/boot/image/insertStaffError.jpg "validate验证信息")<br>
**Alibaba Durid：** 
![boot-durid](https://github.com/apple987/static/raw/master/boot/image/druid.png "durid监控")<br>
**JavaMelody：** 
![boot-javaMelody](https://github.com/apple987/static/raw/master/boot/image/javaMelody.png "javaMelody监控")<br>
**Qr Code：** 	
![boot-qrcode](https://github.com/apple987/static/raw/master/boot/image/qrcode.png "生成二维码")<br>
**Print Qr Code：** 
![boot-print](https://github.com/apple987/static/raw/master/boot/image/print.png "打印二维码")<br>
**Solr Management：** 
![boot-solr](https://github.com/apple987/static/raw/master/boot/image/solr.png "solr导入数据")<br>
**Upload success:** 
![boot-upload](https://github.com/apple987/static/raw/master/boot/image/upload.jpg "文本上传")<br>
**Upload error：**
![boot-uploadError](https://github.com/apple987/static/raw/master/boot/image/uploadError.jpg "文件上传异常")<br>
**Current limiter：**
![boot-ratelimter](https://github.com/apple987/static/raw/master/boot/image/ratelimter.jpg "限流接口请求")<br>

	

		
        
