package com.mylomen.mybatis.utils;


import com.mylomen.mybatis.domain.XxqMybatisBO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/4
 * @time: 9:48 下午
 * @copyright
 */
public class MapperUtils {


    public static void generateMapper(XxqMybatisBO xxqMybatisBO) {
        BufferedWriter bw = null;
        try {
            File file = XxqFileUtils.generateFile(xxqMybatisBO.getMapperFilePath());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(getFileContent(xxqMybatisBO));

            System.out.println("生成Mapper文件 成功");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("生成Mapper文件 失败");
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFileContent(XxqMybatisBO xxqMybatisBO) {
        StringBuilder content = new StringBuilder();
        String entityFile = xxqMybatisBO.getEntityPackage() + "." + xxqMybatisBO.getEntityClazz();

        content.append("package " + xxqMybatisBO.getMapperPackage() + " ;\n\n")
                .append("import " + entityFile + ";\n")
                .append("import org.apache.ibatis.annotations.Mapper;\n")
                .append("import org.apache.ibatis.annotations.Param;\n")
                .append("import java.util.List;\n");

        content.append("\n\n")
                .append("/**\n")
                .append(" * @author \n")
                .append(" * @description\n")
                .append(" * @Date \n")
                .append(" * @Version 1.0 \n")
                .append(" */\n")
                .append("@Mapper\n")
                .append("public interface " + xxqMybatisBO.getMapperClazz() + " {\n\n\n");

        /**
         * add
         */
        add(content, xxqMybatisBO);

        /**
         * remove
         */
        remove(content, xxqMybatisBO);

        /**
         * update
         */
        update(content, xxqMybatisBO);


        /**
         * findById
         */
        findById(content, xxqMybatisBO);

        /**
         * findByIdList
         */
        findByIdList(content, xxqMybatisBO);


        /**
         * count
         */
        count(content, xxqMybatisBO);

        /**
         * queryPage
         */
        queryPage(content, xxqMybatisBO);


        return content.append("\n}").toString();
    }


    private static void add(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        content.append("\t/** 添加数据\n")
                .append("\t *\n")
                .append("\t * @param entity\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\tint add(" + xxqMybatisBO.getEntityClazz() + " entity);\n\n");
    }

    private static void remove(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        content.append("\t/** 根据id 删除数据\n")
                .append("\t *\n")
                .append("\t * @param id\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\tint remove(@Param(\"id\") Long id);\n\n");
    }


    private static void update(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        content.append("\t/** 根据id 跟新数据\n")
                .append("\t *\n")
                .append("\t * @param entity\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\tint update(" + xxqMybatisBO.getEntityClazz() + " entity);\n\n");
    }


    private static void findById(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        content.append("\t/**\n")
                .append("\t * 根据id 查询\n")
                .append("\t *\n")
                .append("\t * @param id\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\t" + xxqMybatisBO.getEntityClazz() + " findById(@Param(\"id\") Long id);\n\n");
    }

    private static void findByIdList(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        content.append("\t/**\n")
                .append("\t * 根据idList 批量查询\n")
                .append("\t *\n")
                .append("\t * @param idList\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\tList<" + xxqMybatisBO.getEntityClazz() + "> findByIdList(@Param(\"idList\") List<Long> idList);\n\n");
    }


    private static void count(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        content.append("\t/** \n")
                .append("\t * 计算数据总数\n")
                .append("\t *\n")
                .append("\t * @param entity\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\tlong count(@Param(\"data\") " + xxqMybatisBO.getEntityClazz() + " entity);\n\n");
    }

    private static void queryPage(StringBuilder content, XxqMybatisBO xxqMybatisBO) {
        String entityClazz = xxqMybatisBO.getEntityClazz();

        content.append("\t/**\n")
                .append("\t * 分页查询数据\n")
                .append("\t *\n")
                .append("\t * @param entity\n")
                .append("\t * @param left\n")
                .append("\t * @param pageSize\n")
                .append("\t * @return\n")
                .append("\t */\n")
                .append("\tList<" + entityClazz + "> queryPage(@Param(\"data\") " + entityClazz + " entity,@Param(\"left\")Integer left,@Param(\"pageSize\")Integer pageSize);\n\n");

    }
}
