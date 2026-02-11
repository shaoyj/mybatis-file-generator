# mybatis-file-generator

#### Description
One-click generation of configuration files of mybatis operation data based on java; 
support user customization;
Support custom methods such as apollo to obtain data sources;

#### Software Architecture
Software architecture description

#### Installation

1.  git clone https://gitee.com/Mylomen/mybatis-file-generator.git
2.  cd  mybatis-file-generator
3.  mvn clean && mvn deploy
4.  Introduce maven dependency

#### Instructions
This tool is suitable for java projects; and one of the application.properties or application.yml files is expected, and the spring.application.name parameter is required in the file;
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