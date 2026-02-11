# mybatis-file-generator

#### 介绍

java语言开发中使用 mybatis 读写数据，一键生成配置文件；
支持使用者定制； 
支持apollo 等定制方式获取数据源；

#### 软件架构

软件架构说明

#### 安装教程

1.  git clone https://gitee.com/Mylomen/mybatis-file-generator.git
2.  cd  mybatis-file-generator
3.  mvn clean && mvn deploy
4.  Introduce maven dependency

#### 使用说明

1. 该工具适用于 java 项目；并且期望有application.properties 或者 application.yml 文件之一，且文件中需要有 spring.application.name 参数；
```aidl

import com.mylomen.mybatis.domain.XxqMybatisBO;
import com.mylomen.mybatis.strategy.DefaultStrategy;
import org.junit.Test;


public class XxqMybatisTestDemo {

    @Test
    public void demoTest() {

//        firstDemo();

        apollo();

//        recommend();

//        simple();
    }

    private static void firstDemo() {
        //1. 初始化
        XxqMybatisBO xxqMybatisBO = DefaultStrategy.getTabInfoList(XxqMybatisBO.builder()
                .xmlFilePath("XXX")
                .mapperFilePath("XXX")
                .entityFilePath("XXX")

                .driver("com.mysql.cj.jdbc.Driver")
                .dbUrl("dbUrl")
                .userName("userName")
                .password("pwd")
                .table("t_anchor_credit").build());


        //2. 生成 xml
        xxqMybatisBO.createXml();

        //3. 生成 mapper
        xxqMybatisBO.generateMapper();

        //4. 生成 DO
        xxqMybatisBO.generateEntity();
    }

    private static void apollo() {
        //如果需要指定 myDbNamespace
        System.getProperties().setProperty("myDbNamespace", "except->dbNamespace");

        XxqMybatisBO mybatisBO = ApolloStrategy.getTabInfoList(
                XxqMybatisBO.builder()
                        .table("yourTableName")
                        .build());

        mybatisBO.generateXmlMapperEntity();
    }

    private static void recommend() {
        //1. 定义
        XxqMybatisBO xxqMybatisBO = ApolloStrategy.getTabInfoList(XxqMybatisBO.builder()
                .mapperPackage("mapper.package.path")
                .entityPackage("entity.package.path")
                .table("yourTableName")
                .build());

        //2. 生成 xml
        xxqMybatisBO.createXml();

        //3. 生成 mapper
        xxqMybatisBO.generateMapper();

        //4. 生成 DO
        xxqMybatisBO.generateEntity();
    }

    /**
     * 默认文件 路径 mybatisUtils
     */
    private static void simple() {
        ApolloStrategy.getTabInfoList(XxqMybatisBO.builder().table("yourTableName").build()).generateXmlMapperEntity();
    }
    
 }

```