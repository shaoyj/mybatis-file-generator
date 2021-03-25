package com.mylomen.mybatis.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Shaoyongjun
 * @date: 2021/3/25
 * @time: 8:53 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class DbTabInfoBO implements Serializable {

    private String tableName;

    private List<DbTabInfo> tabInfoList;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DbTabInfo> getTabInfoList() {
        return tabInfoList;
    }

    public void setTabInfoList(List<DbTabInfo> tabInfoList) {
        this.tabInfoList = tabInfoList;
    }
}
