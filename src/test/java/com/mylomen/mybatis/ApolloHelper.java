package com.mylomen.mybatis;

import com.ctrip.framework.apollo.Config;
import com.mylomen.mybatis.domain.XxqMybatisBO;
import com.mylomen.mybatis.utils.DbInfoUtil;
import com.mylomen.mybatis.utils.PropertiesHelper;
import org.springframework.util.StringUtils;

/**
 * 注意 apollo 首先读取当前项目的 apollo 空间
 *
 * @author: Shaoyongjun
 * @date: 2021/3/25
 * @time: 8:15 下午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class ApolloHelper {

    public static XxqMybatisBO getTabInfoList(XxqMybatisBO xxqMybatisBO) {
        return getTabInfoList(null, xxqMybatisBO);
    }

    /**
     * 获取 tabInfoList
     *
     * @return
     */
    public static XxqMybatisBO getTabInfoList(String dbNamespace, XxqMybatisBO xxqMybatisBO) {


        //获取apolloConfig by dbNamespace or config
        Config config = null;
        if (StringUtils.isEmpty(dbNamespace)) {

            //根据自己项目中 dbNamespace 的命名规律自行实现自动拼接。以下是一个demo

            String applicationName = PropertiesHelper.getApplicationKey("spring.application.name");
            dbNamespace = "middleware.db." + applicationName;
            config = ApolloUtils.getNoEmptyConfigByNamespace(dbNamespace);
            if (config == null) {
                config = ApolloUtils.getNoEmptyConfigByNamespace(dbNamespace + "-master");
            }
        } else {
            config = ApolloUtils.getNoEmptyConfigByNamespace(dbNamespace);
        }

        //依然为空则不支持
        if (config == null) {
            throw new RuntimeException("apollo 配置获取失败。config is null. 请检查 dbNamespace 是否准确 -> " + dbNamespace + " 以及当前项目是否已经配置了Apollo");
        }

        //更新 解析到的 dbNamespace
        System.out.println("apollo 当前解析道的 dbNamespace ：" + dbNamespace);


        //获取 datasourceUrl
        String datasourceUrl = config.getProperty("spring.datasource.url", "");
        if (StringUtils.isEmpty(datasourceUrl)) {
            throw new RuntimeException("apollo 配置获取 datasourceUrl 失败。请检查 dbNamespace 是否准确 -> " + dbNamespace);
        }

        //获取表配置
        xxqMybatisBO.setTabInfoList(DbInfoUtil.getTableInfo(
                config.getProperty("com.mysql.jdbc.Driver", "com.mysql.jdbc.Driver"),
                datasourceUrl,
                config.getProperty("spring.datasource.username", ""),
                config.getProperty("spring.datasource.password", ""),
                xxqMybatisBO.getTable()));


        return xxqMybatisBO.autoParse();
    }
}
