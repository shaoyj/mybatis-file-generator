package com.mylomen.mybatis.utils;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author: Shaoyongjun
 * @date: 2021/1/19
 * @time: 9:23 下午
 * @copyright
 */
public class ApolloUtils {

    /**
     * @param namespace
     * @return
     */
    public static Config getNoEmptyConfigByNamespace(String namespace) {

        try {
            Config config = ConfigService.getConfig(StringUtils.isEmpty(namespace) ? "application" : namespace);
            Set<String> propertyNames = config.getPropertyNames();
            if (CollectionUtils.isEmpty(propertyNames)) {
                return null;
            }

            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
