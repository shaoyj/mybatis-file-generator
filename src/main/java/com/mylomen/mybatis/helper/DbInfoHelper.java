package com.mylomen.mybatis.helper;

import com.mylomen.mybatis.domain.XxqMybatisBO;
import com.mylomen.mybatis.utils.DbInfoUtil;

/**
 * @author: Shaoyongjun
 * @date: 2021/3/25
 * @time: 8:21 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class DbInfoHelper {


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

}
