package com.gzcb.creditcard.data.clean.uitls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author tangliu
 */
public class PropertiesUtils {

    static final Logger logger = LoggerFactory.getLogger(PropertiesUtils.class);

    private static Properties properties = new Properties();

    static {

        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("解析配置文件<config.properties>错误");
        }

    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }

    /**
     * 获取属性值，优先从环境变量中获取，系统参数次之，配置文件最次
     *
     * @param name
     * @param defaultStr
     * @return
     */
    public static String getProperty(String name, String defaultStr) {

        String result = getEnvOrSystemPro(name);
        if (result != null && !result.equals("")) {
            return result;
        }

        result = properties.getProperty(name, defaultStr);
        return result;
    }

    /**
     * 从环境变量或系统参数获取
     *
     * @param key
     * @return
     */
    private static String getEnvOrSystemPro(String key) {

        if (key.startsWith("${") && key.endsWith("}")) {
            key = key.replace("${", "");
            key = key.substring(0, key.lastIndexOf("}"));
        }

        String envValue = System.getenv(key.replaceAll("\\.", "_"));

        if (envValue == null) {
            return System.getProperty(key);
        }
        return envValue;
    }
}
