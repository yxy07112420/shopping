package com.neuedu.json;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ObjectMapper相关的api封装
 */
@Component
public class JsonObjectMapperApi {
    @Autowired
    ObjectMapper objectMapper;
    /**
     * java对象转为string
     */
    public <T> String obj2String(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String?(String)obj: objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * java对象转为string
     * 格式化
     */
    public <T> String obj2StringPretty(T obj){
        if (obj == null){
            return null;
        }
        try {
            return obj instanceof String?(String)obj: objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * String转java对象
     */
    public <T> T str2obj(String str,Class<T> clazz){
        if(StringUtils.isEmpty(str)|| clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class)?(T)str:objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * String转java对象
     * 含有数组或集合
     */
    public <T> T str2obj(String str,TypeReference<T> typeReference){
        if(StringUtils.isEmpty(str)|| typeReference == null){
            return null;
        }
        try {
            return typeReference.getType().equals(String.class)?(T)str:(T) objectMapper.readValue(str,typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
