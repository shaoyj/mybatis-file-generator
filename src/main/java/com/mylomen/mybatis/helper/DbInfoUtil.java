package com.mylomen.mybatis.helper;


import com.mylomen.mybatis.domain.DbTabInfo;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author: Shaoyongjun
 * @date: 2020/11/3
 * @time: 11:18 上午
 * @copyright
 */
public class DbInfoUtil {


    /**
     * 根据数据库的连接参数，获取指定表的基本信息：字段名、字段类型、字段注释
     *
     * @param driver 数据库连接驱动
     * @param dbUrl  数据库连接url
     * @param user   数据库登陆用户名
     * @param pwd    数据库登陆密码
     * @param table  表名
     * @return Map集合
     */
    public static List<DbTabInfo> getTableInfo(String driver, String dbUrl, String user, String pwd, String table) {
        if (StringUtils.isEmpty(table)) {
            throw new RuntimeException("table 不能为空");
        }
        if (StringUtils.isEmpty(dbUrl)) {
            throw new RuntimeException("dbUrl 不能为空");
        }

        driver = driver == null ? "com.mysql.cj.jdbc.Driver" : driver;

        List<DbTabInfo> result = new ArrayList<>();
        Connection conn = null;
        DatabaseMetaData dbCmd = null;

        try {
            conn = getConnections(driver, dbUrl, user, pwd);

            dbCmd = conn.getMetaData();
            ResultSet resultSet = dbCmd.getTables(null, "%", table, new String[]{"TABLE"});

            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                if (tableName.equals(table)) {
                    ResultSet rs = conn.getMetaData().getColumns(null, getSchema(conn), tableName.toUpperCase(), "%");

                    while (rs.next()) {
                        String colName = rs.getString("COLUMN_NAME");
                        DbTabInfo dbTabInfo = new DbTabInfo(colName);

                        String remarks = rs.getString("REMARKS");
                        if (StringUtils.isEmpty(remarks)) {
                            remarks = colName;
                        }
                        dbTabInfo.remarkInfo(remarks);

                        String dbType = rs.getString("TYPE_NAME");
                        dbTabInfo.dbType(dbType);
                        dbTabInfo.valueType(changeDbType(dbType));


                        result.add(dbTabInfo);
                    }
                }
            }

            System.out.println("获取表信息 成功");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("获取表信息 失败");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static String changeDbType(String dbType) {
        dbType = dbType.toUpperCase();
        return switch (dbType) {
            case "NUMBER", "DECIMAL" -> "BigDecimal";
            case "INT", "INT UNSIGNED", "SMALLINT", "SMALLINT UNSIGNED", "TINYINT", "TINYINT UNSIGNED", "INTEGER",
                 "INTEGER UNSIGNED", "BIT" -> "Integer";
            case "BIGINT", "BIGINT UNSIGNED" -> "Long";
            case "DATETIME", "TIMESTAMP", "DATE" -> "Date";
            default -> "String";
        };
    }

    /**
     * 获取连接
     *
     * @param driver
     * @param url
     * @param user
     * @param pwd
     * @return
     * @throws Exception
     */
    private static Connection getConnections(String driver, String url, String user, String pwd) throws Exception {
        Connection conn = null;
        try {
            Properties props = new Properties();
            props.put("remarksReporting", "true");
            props.put("user", user);
            props.put("password", pwd);
            Class.forName(driver);
            conn = DriverManager.getConnection(url, props);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return conn;
    }

    /**
     * 其他数据库不需要这个方法 oracle和db2需要
     *
     * @param conn
     * @return
     * @throws Exception
     */
    private static String getSchema(Connection conn) throws Exception {
        String schema;
        schema = conn.getMetaData().getUserName();
        if ((schema == null) || (schema.isEmpty())) {
            throw new Exception("ORACLE数据库模式不允许为空");
        }
        return schema.toUpperCase();

    }


}
