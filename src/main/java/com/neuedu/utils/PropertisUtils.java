package com.neuedu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取资源文件
 */
public class PropertisUtils {

    private static Properties properties = new Properties();
    static {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 读取配置文件的内容
     */
    public static String readByKey(String key){
        return properties.getProperty(key);
    }

}
