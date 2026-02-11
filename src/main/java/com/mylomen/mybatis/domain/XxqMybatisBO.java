package com.mylomen.mybatis.domain;


import com.google.common.base.CaseFormat;
import com.mylomen.mybatis.utils.EntityUtils;
import com.mylomen.mybatis.utils.MapperUtils;
import com.mylomen.mybatis.utils.XmlUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/14
 * @time: 2:43 下午
 * @copyright
 */
@Getter
@Setter
@Builder
public class XxqMybatisBO implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(XxqMybatisBO.class);

    private static final long serialVersionUID = -2056682435323048848L;

    /**
     * 默认 com.mysql.cj.jdbc.Driver
     */
    private String driver;

    private String dbUrl;

    private String userName;

    private String password;

    private String table;

    private String beanName;

    private List<DbTabInfo> tabInfoList;


    private String xmlFilePath;

    /**
     * 默认地址 mybatis
     */
    private String xmlPackagePath;


    private String mapperFilePath;

    /**
     * mapper类 包路径(默认 mybatisUtils)
     */
    private String mapperPackage;

    private String mapperClazz;


    private String entityFilePath;

    /**
     * 实体类 包路径(默认 mybatisUtils)
     */
    private String entityPackage;

    private String entityClazz;

    /**
     * 是否需要 重新解析 生成的文件路径
     */
    private boolean reloadFilePath;


    /**
     * 装填表信息
     *
     * @return
     */


    public void createXml() {
        //tabInfoList
        if (this.getTabInfoList() == null) {
            System.out.println("this.tabInfoList is null. please execute fillTableInfo first.");
            return;
        }

        //生成entity 包路径 和 类名称
        autoParseEntityClazzInfo();

        //生成Mapper 包路径 和 类名称
        autoParseMapperClazzInfo();

        //生产xml 文件
        XmlUtils.createXml(this);
    }


    public void generateMapper() {
        //tabInfoList
        if (this.getTabInfoList() == null) {
            System.out.println("this.tabInfoList is null. please execute fillTableInfo first.");
            return;
        }

        //生成entity 包路径 和 类名称
        autoParseEntityClazzInfo();

        //生成Mapper 包路径 和 类名称
        autoParseMapperClazzInfo();

        //生产mapper 文件
        MapperUtils.generateMapper(this);
    }

    public void generateEntity() {
        //tabInfoList
        if (this.getTabInfoList() == null) {
            System.out.println("this.tabInfoList is null. please execute fillTableInfo first.");
            return;
        }

        //生成entity 包路径 和 类名称
        autoParseEntityClazzInfo();

        //生产entity文件
        EntityUtils.generateEntity(this);
    }

    public void generateXmlMapperEntity() {
        //1. 生成 xml
        createXml();

        //2. 生成 mapper
        generateMapper();

        //3. 生成 DO
        generateEntity();
    }


    /**
     * 生产entity 包路径 和 类名称
     */
    private void autoParseEntityClazzInfo() {
        if (this.reloadFilePath || this.entityClazz == null || this.entityPackage == null) {
            XxqMybatisClazzInfo entityClazzInfo = new XxqMybatisClazzInfo(this.entityFilePath);
            this.entityClazz = entityClazzInfo.getClazzName();
            this.entityPackage = entityClazzInfo.getPackagePath();
        }
    }


    /**
     * 生成Mapper 包路径 和 类名称
     */
    private void autoParseMapperClazzInfo() {
        if (this.reloadFilePath || this.mapperClazz == null || this.mapperPackage == null) {
            XxqMybatisClazzInfo mapperClazzInfo = new XxqMybatisClazzInfo(this.mapperFilePath);
            this.mapperClazz = mapperClazzInfo.getClazzName();
            this.mapperPackage = mapperClazzInfo.getPackagePath();
        }
    }


    /**
     * after setTabInfoList
     *
     * @return
     */
    public XxqMybatisBO autoParse() {
        if (this.getTabInfoList() == null) {
            throw new RuntimeException("should execute setTabInfoList first!");
        }

        //beanName
        String beanName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, this.getTable().replaceFirst("t_", ""));
        //workSpace
        String workSpace = System.getProperty("user.dir");

        //XmlFilePath
        if (this.isReloadFilePath() || StringUtils.isEmpty(this.getXmlFilePath()) || StringUtils.endsWithIgnoreCase(this.getXmlFilePath(), "XXX")) {
            if (StringUtils.isEmpty(this.getXmlPackagePath())) {
                this.setXmlPackagePath("mybatis");
            }
            this.setXmlFilePath(workSpace + "/src/main/resources/" + this.getXmlPackagePath() + "/" + beanName + "Mapper.xml");
        }

        //MapperFilePath
        if (this.isReloadFilePath() || StringUtils.isEmpty(this.getMapperFilePath()) || StringUtils.endsWithIgnoreCase(this.getMapperFilePath(), "XXX")) {
            if (StringUtils.isEmpty(this.getMapperPackage())) {
                this.setMapperPackage("mybatisUtils");
            }
            this.setMapperFilePath(workSpace + "/src/main/java/" + this.getMapperPackage().replaceAll("\\.", "/") + "/" + beanName + "Mapper.java");
        }

        //EntityFilePath
        if (this.isReloadFilePath() || StringUtils.isEmpty(this.getEntityFilePath()) || StringUtils.endsWithIgnoreCase(this.getEntityFilePath(), "XXX")) {
            if (StringUtils.isEmpty(this.getEntityPackage())) {
                this.setEntityPackage("mybatisUtils");
            }
            this.setEntityFilePath(workSpace + "/src/main/java/" + this.getEntityPackage().replaceAll("\\.", "/") + "/" + beanName + "DO.java");
        }

        this.reloadFilePath = true;

        //生成entity 包路径 和 类名称
        this.autoParseEntityClazzInfo();

        //生成Mapper 包路径 和 类名称
        this.autoParseMapperClazzInfo();

        this.reloadFilePath = false;
        return this;
    }
}
