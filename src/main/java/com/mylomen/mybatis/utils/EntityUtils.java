package com.mylomen.mybatis.utils;

import com.google.common.primitives.Ints;
import com.mylomen.mybatis.domain.DbTabInfo;
import com.mylomen.mybatis.domain.XxqMybatisBO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


/**
 * @author: Shaoyongjun
 * @date: 2020/11/13
 * @time: 7:54 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class EntityUtils {


    public static void generateEntity(XxqMybatisBO xxqMybatisBO) {
        BufferedWriter bw = null;
        try {
            File file = XxqFileUtils.generateFile(xxqMybatisBO.getEntityFilePath());
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(getFileContent(xxqMybatisBO));

            System.out.println("生成Entity文件 成功");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("生成Entity文件 失败");
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
        StringBuilder content = new StringBuilder()
                .append("package " + xxqMybatisBO.getEntityPackage() + " ;\n\n")
                .append("import lombok.Getter;\n")
                .append("import lombok.Setter;\n")

                .append("import java.io.Serializable;\n")
                .append("import java.util.Date;\n")
                .append("import java.util.Map;\n")
                .append("import java.util.HashMap;\n")

                .append("\n\n")
                .append("/**\n")
                .append(" * @author \n")
                .append(" * @description\n")
                .append(" * @Date \n")
                .append(" * @Version 1.0 \n")
                .append(" */\n")
                .append("@Setter\n")
                .append("@Getter\n")
                .append("public class " + xxqMybatisBO.getEntityClazz() + " implements Serializable {\n\n")
                .append("\tprivate static final long serialVersionUID = 1L;\n\n");

        //字段
        generateMemberFiled(xxqMybatisBO, content);

        /**
         * toMap
         */
        generateToMap(xxqMybatisBO, content);

        return content.append("\n}").toString();
    }

    private static void generateMemberFiled(XxqMybatisBO xxqMybatisBO, StringBuilder content) {
        xxqMybatisBO.getTabInfoList().forEach(colInfo -> {
            content.append("\t/**\n")
                    .append("\t * " + colInfo.getRemarkInfo() + "\n")
                    .append("\t */\n");
            content.append("\tprivate " + colInfo.getValueType() + " " + XxqStrUtils.underscoreToLowerCamel(colInfo.getColName()) + ";\n\n");
        });
    }

    private static void generateToMap(XxqMybatisBO xxqMybatisBO, StringBuilder content) {
        /**
         * 参考  Maps.newHashMapWithExpectedSize(size)
         */
        List<DbTabInfo> tabInfoList = xxqMybatisBO.getTabInfoList();
        int mapSize;
        if (tabInfoList.size() < 3) {
            mapSize = tabInfoList.size() + 1;
        } else if (tabInfoList.size() < Ints.MAX_POWER_OF_TWO) {
            mapSize = (int) ((float) tabInfoList.size() / 0.75F + 1.0F);
        } else {
            mapSize = Integer.MAX_VALUE;
        }

        content.append("\tpublic Map<String, Object> toMap() {\n")
                .append("\t\tMap<String, Object> map = new HashMap<>(" + mapSize + ");\n\n");

        int count = 0;
        for (DbTabInfo colInfo : tabInfoList) {
            String memberName = XxqStrUtils.underscoreToLowerCamel(colInfo.getColName());
            content.append("\t\tmap.put(\"" + memberName + "\", " + memberName + ");\n");
            count++;

            if ((count % 3 == 0)) {
                content.append("\n");
            }
        }

        content.append("\t\treturn map;\n");
        content.append("\t}");
    }
}
