package com.mylomen.mybatis.utils;


import com.mylomen.params.XxqMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: Shaoyongjun
 * @date: 2021/1/20
 * @time: 10:21 上午
 * @copyright by  上海鱼泡泡信息技术有限公司
 */
public class PropertiesHelper {

    /**
     * 此处引入log
     */
    private static final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

    private static Map<String, Map<String, Object>> ymlProperties = new HashMap<>();

    /**
     * 单例
     */
    public static final PropertiesHelper instance = new PropertiesHelper();

    static {
        Yaml yaml = new Yaml();
        try (InputStream in = PropertiesHelper.class.getClassLoader().getResourceAsStream("application.yml");) {
            ymlProperties = yaml.loadAs(in, HashMap.class);
        } catch (Exception e) {
            System.err.println("Init yaml failed ! msg : " + e.getMessage());
            logger.error("Init yaml failed ! msg_{}", e.getMessage());
        }
    }

    public static String getApplicationKey(String key) {
        String value = getApplicationValueFromProperties(key);
        if (value != null) {
            return value;
        }

        return getApplicationValueFromYml(key);
    }

    public static String getApplicationValueFromProperties(String key) {
        Properties properties = new Properties();

        // 使用properties对象加载输入流
        try {

            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = PropertiesHelper.class.getClassLoader().getResourceAsStream("application.properties");
            if (in == null) {
                System.err.println("###### 项目中找不到 application.properties 文件 ######");
                return null;
            }
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //获取key对应的value值
        return properties.getProperty(key);
    }

    public static String getApplicationValueFromYml(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        if (ymlProperties == null || ymlProperties.isEmpty()) {
            System.err.println("###### 项目中也找不到 application.yml 文件 ######");
            return null;
        }

        String[] split = key.split("\\.");
        int length = split.length;

        Map map = ymlProperties;
        for (int i = 0; i < length - 1; i++) {
            map = XxqMapUtils.getMap(map, split[i]);
        }

        return XxqMapUtils.getStringValue(map, split[length - 1]);
    }
}
