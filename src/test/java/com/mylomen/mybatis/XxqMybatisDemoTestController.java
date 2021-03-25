package com.mylomen.mybatis;


import com.mylomen.mybatis.domain.XxqMybatisBO;
import com.mylomen.mybatis.helper.DbInfoHelper;
import org.junit.Test;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/4
 * @time: 9:35 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class XxqMybatisDemoTestController {

    @Test
    public void demoTest() {

//        firstDemo();

        apollo();

//        recommend();

//        simple();
    }

    private static void firstDemo() {
        //1. 初始化
        XxqMybatisBO xxqMybatisBO = DbInfoHelper.getTabInfoList(XxqMybatisBO.builder()
                .xmlFilePath("XXX")
                .mapperFilePath("XXX")
                .entityFilePath("XXX")

                .driver("com.mysql.jdbc.Driver")
                .dbUrl("jdbc:mysql://rm-bp1tu2eq09a7hb658.mysql.rds.aliyuncs.com:3306/yuer_anchor")
                .userName("yuer_anchor_rw")
                .password("zOSYp8XbLmC6Toh7")
                .table("t_anchor_credit").build());


        //2. 生成 xml
        xxqMybatisBO.createXml();

        //3. 生成 mapper
        xxqMybatisBO.generateMapper();

        //4. 生成 DO
        xxqMybatisBO.generateEntity();
    }

    private static void apollo() {
        XxqMybatisBO mybatisBO = ApolloHelper.getTabInfoList(
                "middleware.db.yuer-live-service",
                XxqMybatisBO.builder()
                        .mapperPackage("com.yupaopao.mybatis.util.test")
                        .entityPackage("com.yupaopao.mybatis.util.test")
                        .table("t_anchor_credit")
                        .build());

        mybatisBO.generateXmlMapperEntity();

        mybatisBO.generateXmlMapperEntity();
    }

    private static void recommend() {
        //1. 定义
        XxqMybatisBO xxqMybatisBO = ApolloHelper.getTabInfoList(XxqMybatisBO.builder()
                .mapperPackage("com.yupaopao.mybatis.util.test")
                .entityPackage("com.yupaopao.mybatis.util.test")
                .table("t_anchor_credit")
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
        ApolloHelper.getTabInfoList(XxqMybatisBO.builder().table("t_anchor_credit").build())
                .generateXmlMapperEntity();
    }
}
