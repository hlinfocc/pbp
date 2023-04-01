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

## 2.在启动类加入@EnableHlinfoPBP注解

示例：

```java
package net.hlinfo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import net.hlinfo.pbp.opt.EnableHlinfoPBP;

@SpringBootApplication
@EnableHlinfoPBP
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}
}

```

## 3.配置

使用nutz自动建表（实体类需要配置相关注解）


## 4.启动应用

