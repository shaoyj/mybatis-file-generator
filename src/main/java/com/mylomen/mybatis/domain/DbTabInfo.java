package com.mylomen.mybatis.domain;

import java.io.Serializable;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/3
 * @time: 11:40 上午
 * @copyright
 */
public class DbTabInfo implements Serializable {

    private static final long serialVersionUID = 2412574636856483655L;

    private String colName;

    private String jdbcType;

    private String remarkInfo;

    private String dbType;

    private String valueType;

    public String getColName() {
        return colName;
    }

    public String getJdbcType() {
        return jdbcType;
    }


    public String getRemarkInfo() {
        return remarkInfo;
    }


    public String getDbType() {
        return dbType;
    }



    public String getValueType() {
        return valueType;
    }


    public DbTabInfo(String colName) {
        this.colName = colName;
    }

    public DbTabInfo remarkInfo(String remarkInfo) {
        this.remarkInfo = remarkInfo;
        return this;
    }

    public DbTabInfo dbType(String dbType) {
        this.dbType = dbType;

        if ("BIT".equals(dbType)) {
            this.jdbcType = "";
        } else {
            this.jdbcType = dbType.split(" ")[0];
        }
        return this;
    }

    public DbTabInfo valueType(String valueType) {
        this.valueType = valueType;
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                "\"colName\":\"" + colName + '"' +
                ",\"remarkInfo\":\"" + remarkInfo + '"' +
                ", \"jdbcType\":\"" + jdbcType + '"' +
                ", \"dbType\":\"" + dbType + '"' +
                ", \"valueType\":\"" + valueType + '"' +
                '}';
    }

    public static void main(String[] args) {
        String str="BIGINT UNSIGNED";
        String[] split = str.split(" ");
        System.out.println(split);
    }
}
