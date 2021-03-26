package com.mylomen.mybatis.strategy;

import com.mylomen.mybatis.domain.XxqMybatisBO;
import com.mylomen.mybatis.helper.DbInfoUtil;

/**
 * @author: Shaoyongjun
 * @date: 2021/3/25
 * @time: 8:21 下午
 * @copyright
 */
public class DefaultStrategy implements ParseTabInfoListStrategy {


    /**
     * 获取 tabInfoList
     *
     * @param dbBaseInfo
     * @return
     */
    public static XxqMybatisBO getTabInfoList(XxqMybatisBO dbBaseInfo) {

        dbBaseInfo.setTabInfoList(DbInfoUtil.getTableInfo(
                dbBaseInfo.getDriver() == null ? "com.mysql.jdbc.Driver" : dbBaseInfo.getDriver(),
                dbBaseInfo.getDbUrl(),
                dbBaseInfo.getUserName(),
                dbBaseInfo.getPassword(),
                dbBaseInfo.getTable()));

        return dbBaseInfo.autoParse();
    }

    @Override
    public XxqMybatisBO parseTabInfoList(XxqMybatisBO dbBaseInfo) {
        return getTabInfoList(dbBaseInfo);
    }
}
