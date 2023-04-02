# 简介

PBP是Spring Boot的公共基础项目模块，开箱即用，集成了knife4j（API文档）、文件上传与下载、通用文章管理、图片验证码、管理员管理、权限与角色、数据字典、国密SM加密等功能，目的是为了不重复造轮子

# 系列介绍

* pbp 适用于单体项目【[查看详情](https://github.com/hlinfocc/pbp "查看详情")】
* pbp-micro 适用于SpringCloud 微服务【[查看详情](https://github.com/hlinfocc/pbp-micro "查看详情")】
* pbp-pay 字符模块,集成支付宝、微信支付、ios登录及ios内购校验，需要自行引入pom坐标【[查看详情](https://github.com/hlinfocc/pbp-pay "查看详情")】

# 快速使用

## 1.引入pom坐标

```xml
<dependency>
	<groupId>net.hlinfo</groupId>
	<artifactId>pbp</artifactId>
	<version>2.7.10</version>
</dependency>
```
完整pom.xml示例：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.7.10</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>net.hlinfo</groupId>
	<artifactId>spring-boot-demo</artifactId>
	<version>1.0.1</version>
	<name>spring-boot-demo</name>
	<description>Educational administration management platform</description>
	<properties>
		<java.version>1.8</java.version>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	</properties>
	<dependencies>
		<dependency>
			<groupId>net.hlinfo</groupId>
			<artifactId>pbp</artifactId>
			<version>2.7.10</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

## 3.配置

```yml
server:
  port: 1088
    
spring:
  profiles:
    active: dev #prod test
  application: 
    name: springBootDemo
  mvc:
    pathmatch:
      matching-strategy: ANT_PATH_MATCHER
  datasource:
    url: jdbc:postgresql://127.0.0.1:5432/example
    username: postgres
    password: 123456
    driver-class-name: org.postgresql.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    druid:
        initialSize: 5
        minIdle: 5
        maxActive: 150
        maxWait: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 'x'
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        filters: stat,wall
  redis:
    database: 1
    host: 127.0.0.1
    port: 6379
    password: 
    timeout: 60000
    jedis:
      pool:
        max-active: 200
        max-idle: 10
        min-idle: 0
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 100MB

mybatis:
  config-location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mybatis/mapper/*.xml
  type-aliases-package: com.example.entity

nutz:
  dao:
    enabled: true #是否启用Nutz
    runtime:
      create: true #是否自动建表 默认true
      migration: true #是否自动变更 默认true
      add-column: true # 是否添加列 默认true
      delete-column: true # 是否删除列 默认true
      foce-create: false # 是否删表重建，注意此功能会删除全部表及数据，一般应用于demo或测试 默认false
      check-index: false # 是否检查索引 默认true
      basepackage:  # 相关实体所在包(net.hlinfo.pbp.entity为pbp的实体路径)
        - net.hlinfo.pbp.entity
        - com.example.entity

upload:
  savePath: /www/upload
  baseUrl: http://192.168.1.1/upload
  relative: false

knife4j:
  enable: true
  production: false
  apiinfo:
    title: PBP系统API接口文档
    description: PBP系统API接口文档介绍
    terms: hlinfo.cc
    name: ylcy
    url: ylcxy.cn
    email: tmp@hlinfo.net
    version: V1.0
  global:
    param:
    - name: token
      description: AccessToken
      scalar-type: string
      required: false
      parameter-type: header

logging:
  pattern: 
    console: "%d %p %C %m%n"
  level: 
    net: 
      hlinfo: debug
    com: 
      github:
        xiaoymin: error
    org: error
    springfox:
      documentation: error

```

## 4.启动应用

按照常规方式启动应用即可

## 5.访问API文档

访问http://127.0.0.1:1088/doc.html
