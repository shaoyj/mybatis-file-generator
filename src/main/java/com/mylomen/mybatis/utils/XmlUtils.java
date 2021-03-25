package com.mylomen.mybatis.utils;


import com.mylomen.mybatis.domain.DbTabInfo;
import com.mylomen.mybatis.domain.XxqMybatisBO;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/3
 * @time: 2:35 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class XmlUtils {


    private final static String baseResultMap = "baseResultMap";
    private final static String CONDITION = "condition";


    public static void createXml(XxqMybatisBO xxqMybatisBO) {
        createXml(
                xxqMybatisBO.getXmlFilePath(),
                xxqMybatisBO.getMapperPackage() + "." + xxqMybatisBO.getMapperClazz(),
                xxqMybatisBO.getEntityPackage() + "." + xxqMybatisBO.getEntityClazz(),
                xxqMybatisBO.getTabInfoList(),
                xxqMybatisBO.getTable()
        );
    }

    /**
     * 生成xml方法
     */
    private static void createXml(String xmlFileAddress, String namespace, String entity, List<DbTabInfo> tabInfoList, String tabName) {
        try {
            //创建document对象
            Document document = DocumentHelper.createDocument();
            //设置DOCTYPE
            document.addDocType("mapper", "-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
            //创建根节点 mapper
            Element mapper = document.addElement("mapper");
            //向mapper节点添加 namespace属性
            mapper.addAttribute("namespace", namespace);


            generateResultMap(entity, tabInfoList, mapper);

            generateBase_Column_List(tabInfoList, mapper);

            generateCondition(tabInfoList, mapper);

            generateSetField(tabInfoList, mapper);

            generateInsert(entity, tabName, mapper);

            generateRemove(entity, tabName, mapper);

            generateUpdate(entity, tabName, mapper);

            generateFindById(tabName, mapper);

            generateFindByIdList(tabName, mapper);

            generateCount(tabName, mapper);

            generateQueryPage(tabName, mapper);


            //生成xml文件
            OutputFormat format = getOutputFormat();
            File file = XxqFileUtils.generateFile(xmlFileAddress);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
            System.out.println("生成xml文件 成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成xml文件 失败");
        }
    }

    private static void generateResultMap(String entity, List<DbTabInfo> tabInfoList, Element mapper) {
        Element resultMap = mapper.addElement("resultMap");
        resultMap.addAttribute("id", baseResultMap);
        resultMap.addAttribute("type", entity);
        tabInfoList.forEach(colInfo -> {
            Element result = resultMap.addElement("result");
            result.addAttribute("column", colInfo.getColName());
            result.addAttribute("property", XxqStrUtils.underscoreToLowerCamel(colInfo.getColName()));
        });
    }

    private static void generateSetField(List<DbTabInfo> tabInfoList, Element mapper) {
        Element sql = mapper.addElement("sql");
        sql.addAttribute("id", "setField");
        Element set = sql.addElement("set");
        tabInfoList.forEach(colInfo -> {
            if ("id".equalsIgnoreCase(colInfo.getColName())) {
                return;
            }
            String underscoreToLowerCamel = XxqStrUtils.underscoreToLowerCamel(colInfo.getColName());
            Element ifEle = set.addElement("if");
            ifEle.addAttribute("test", underscoreToLowerCamel + "!=null");
            ifEle.setText(" " + colInfo.getColName() + " = #{" + underscoreToLowerCamel + "},");
        });
    }

    private static void generateCondition(List<DbTabInfo> tabInfoList, Element mapper) {
        Element sql = mapper.addElement("sql");
        sql.addAttribute("id", CONDITION);

        Element dataIf = sql.addElement("if");
        dataIf.addAttribute("test", "data!=null");

        tabInfoList.forEach(colInfo -> {
            if ("id".equalsIgnoreCase(colInfo.getColName())) {
                return;
            }

            String underscoreToLowerCamel = XxqStrUtils.underscoreToLowerCamel(colInfo.getColName());
            Element ifEle = dataIf.addElement("if");
            ifEle.addAttribute("test", "data." + underscoreToLowerCamel + "!=null");
            ifEle.setText("\n" +
                    "\t\t\t\tAND " + colInfo.getColName() + " = #{data." + underscoreToLowerCamel + "}\n" +
                    "\t\t\t");
        });
    }

    private static void generateBase_Column_List(List<DbTabInfo> tabInfoList, Element mapper) {
        Element sql = mapper.addElement("sql");
        sql.addAttribute("id", "Base_Column_List");
        String colNameList = tabInfoList.stream().map(DbTabInfo::getColName).collect(Collectors.toList()).toString();
        sql.setText("\n" +
                "\t" +
                "\t" + colNameList.replaceAll("\\[|\\]", "") + "\n" +
                "\t");
    }


    private static OutputFormat getOutputFormat() {
        // 设置生成xml的格式
        OutputFormat format = new OutputFormat();
        // 设置编码格式
        format.setEncoding("UTF-8");

        //保持格式
        format.setTrimText(false);

        format.setNewlines(true);
        format.setPadText(false);
        // 生成缩进
        format.setIndent(true);
        // 使用4个空格进行缩进, 可以兼容文本编辑器
        format.setIndent("    ");

        format.setNewLineAfterNTags(10);
        format.setLineSeparator("\n");
        return format;
    }

    private static void generateRemove(String entity, String tabName, Element mapper) {
        Element delete = mapper.addElement("delete");
        delete.addAttribute("id", "remove");
        delete.setText("\n\t\t" +
                "DELETE FROM " + tabName + "\n " +
                "\t\tWHERE id = #{id}\n" +
                "\t");
    }

    private static void generateUpdate(String entity, String tabName, Element mapper) {
        Element update = mapper.addElement("update");
        update.addAttribute("id", "update");
        update.addAttribute("parameterType", entity);
        update.setText("\n\t\t" +
                "update " + tabName + "\n " +
                "\t\t<include refid=\"setField\"/> \n" +
                "\t\tWHERE id = #{id}\n" +
                "\t");
    }

    private static void generateFindById(String tabName, Element mapper) {
        Element findById = mapper.addElement("select");
        findById.addAttribute("id", "findById");
        findById.addAttribute("parameterType", "Long");
        findById.addAttribute("resultMap", baseResultMap);
        findById.setText("\n\t\t" +
                "SELECT\n" +
                "\t\t\t<include refid=\"Base_Column_List\"/>\n" +
                "\t\tFROM \n" +
                "\t\t\t" + tabName + " \n" +
                "\t\tWHERE id=#{id}  \n" +
                "\t\tlimit 1 \n" +
                "\t");
    }

    private static void generateFindByIdList(String tabName, Element mapper) {
        Element findByIdList = mapper.addElement("select");
        findByIdList.addAttribute("id", "findByIdList");
        findByIdList.addAttribute("resultMap", baseResultMap);
        findByIdList.setText("\n\t\t" +
                "SELECT\n" +
                "\t\t\t<include refid=\"Base_Column_List\"/>\n" +
                "\t\tFROM \n" +
                "\t\t\t" + tabName + "\n" +
                "\t\tWHERE id in\n" +
                "\t\t<foreach close=\")\" collection=\"idList\" item=\"itemId\" open=\"(\" separator=\",\">\n" +
                "\t\t\t#{itemId}\n" +
                "\t\t</foreach>\n" +
                "\t");
    }

    private static void generateCount(String tabName, Element mapper) {
        Element count = mapper.addElement("select");
        count.addAttribute("id", "count");
        count.addAttribute("resultType", "Long");
        count.setText("\n\t\t" +
                "SELECT\n" +
                "\t\t\tcount(*) as total\n" +
                "\t\tFROM \n" +
                "\t\t\t" + tabName + "\n" +
                "\t\tWHERE 1=1\n" +
                "\t\t<include refid=\"" + CONDITION + "\"/>\n" +
                "\t");
    }

    private static void generateQueryPage(String tabName, Element mapper) {
        Element queryPage = mapper.addElement("select");
        queryPage.addAttribute("id", "queryPage");
        queryPage.addAttribute("resultMap", baseResultMap);
        queryPage.setText("\n\t\t" +
                "SELECT\n" +
                "\t\t\t<include refid=\"Base_Column_List\"/>\n" +
                "\t\tFROM \n" +
                "\t\t\t" + tabName + "\n" +
                "\t\tWHERE 1=1\n" +
                "\t\t<include refid=\"" + CONDITION + "\"/>\n" +
                "\t\tlimit #{left},#{pageSize}\n" +
                "\t");
    }

    private static void generateInsert(String entity, String tabName, Element mapper) {
        Element insert = mapper.addElement("insert");
        insert.addAttribute("id", "add");
        insert.addAttribute("parameterType", entity);
        insert.setText("\n\t\t" +
                "insert into " + tabName + "\n " +
                "\t\t<include refid=\"setField\"/>\n" +
                "\t");
    }
}
