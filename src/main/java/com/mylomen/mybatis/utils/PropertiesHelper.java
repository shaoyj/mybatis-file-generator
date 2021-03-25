package com.mylomen.mybatis.utils;


import com.mylomen.params.XxqMapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
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
    private static final Logger logger = LoggerFactory.getLogger(PropertiesHelper.class);

    private static Map<String, Map<String, Object>> ymlProperties = new HashMap<>();

    /**
     * 单例
     */
    public static final PropertiesHelper instance = new PropertiesHelper();

    static {
        File file = new File("application.yml");
        if (file.exists()) {
            Yaml yaml = new Yaml();
            try (InputStream in = PropertiesHelper.class.getClassLoader().getResourceAsStream("application.yml");) {
                ymlProperties = yaml.loadAs(in, HashMap.class);
            } catch (Exception e) {
                logger.error("Init yaml failed !", e);
            }
        } else {
            ymlProperties = null;
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
        File file = new File("application.properties");
        if (file.exists()) {
            Properties properties = new Properties();
            // 使用ClassLoader加载properties配置文件生成对应的输入流
            InputStream in = PropertiesHelper.class.getClassLoader().getResourceAsStream("application.properties");
            // 使用properties对象加载输入流
            try {
                properties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            //获取key对应的value值
            return properties.getProperty(key);
        }

        System.err.println("项目中找不到 application.properties 文件");
        return null;
    }

    public static String getApplicationValueFromYml(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }

        if (ymlProperties == null) {
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
