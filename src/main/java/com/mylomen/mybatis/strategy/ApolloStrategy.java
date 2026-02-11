package com.mylomen.mybatis.strategy;

import com.ctrip.framework.apollo.Config;
import com.mylomen.mybatis.domain.XxqMybatisBO;
import com.mylomen.mybatis.helper.DbInfoUtil;
import com.mylomen.mybatis.utils.ApolloUtils;
import com.mylomen.mybatis.utils.PropertiesHelper;
import org.springframework.util.StringUtils;

/**
 *
 *
 * @author: Shaoyongjun
 * @date: 2021/3/25
 * @time: 8:15 下午
 * @copyright
 */
public class ApolloStrategy implements ParseTabInfoListStrategy {

    static {
        //默认是test 环境. 如果需要更改 可以额外指定
        //System.getProperties().setProperty("env", "test");

        //如果项目中没有引入apollo 或者项目中没有封装 apollo.meta 的初始化。可以在这里设置apollo meta 地址
        // System.getProperties().setProperty("apollo.meta", "https://xxx.cn");
    }

    /**
     * 获取 tabInfoList
     *
     * @return
     */
    public static XxqMybatisBO getTabInfoList(XxqMybatisBO xxqMybatisBO) {

        //获取系统指定的 dbNamespace
        String dbNamespace = System.getProperties().getProperty("myDbNamespace");

        //get config by dbNamespace(by System settings OR According to the project name)
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
                config.getProperty("com.mysql.cj.jdbc.Driver", "com.mysql.cj.jdbc.Driver"),
                datasourceUrl,
                config.getProperty("spring.datasource.username", ""),
                config.getProperty("spring.datasource.password", ""),
                xxqMybatisBO.getTable()));


        return xxqMybatisBO.autoParse();
    }

    @Override
    public XxqMybatisBO parseTabInfoList(XxqMybatisBO dbBaseInfo) {
        return getTabInfoList(dbBaseInfo);
    }
}
