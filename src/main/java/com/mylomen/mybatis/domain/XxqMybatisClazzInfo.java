package com.mylomen.mybatis.domain;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/14
 * @time: 7:38 下午
 * @copyright
 */
@Getter
public class XxqMybatisClazzInfo implements Serializable {

    private static final long serialVersionUID = 1689952260586935501L;

    private String packagePath;

    private String clazzName;


    public XxqMybatisClazzInfo(String filePath) {
        if (filePath == null) {
            throw new RuntimeException("文件路径 不能为空");
        }

        String[] split = filePath.split("/src/main/java/");
        if (split.length != 2) {
            throw new RuntimeException("文件路径不满足右边格式 ： */src/main/java/*");
        }

        String[] params = split[1].replaceAll(".java", "").split("/");
        int length = params.length;

        StringBuilder str = new StringBuilder();
        for (int i = 1; i < length - 1; i++) {
            str.append(".").append(params[i]);
        }


        this.packagePath = params[0] + str.toString();

        this.clazzName = params[length - 1];
    }

    @Override
    public String toString() {
        return "{" +
                "\"packagePath\":\"" + packagePath + '"' +
                ", \"clazzName\":\"" + clazzName + '"' +
                '}';
    }
}
