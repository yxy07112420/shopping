package com.neuedu.json;

import com.neuedu.utils.DateChangeUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.DateUtils;

import java.text.SimpleDateFormat;

/**
 * 封装objectMapper对象
 */
@Component
@Configuration
public class JsonObjectMapper {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        //对象中所有的字段序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        //取消默认timestamp格式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空bean转json错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS,false);
        //设置日期格式
        objectMapper.setDateFormat(new SimpleDateFormat(DateChangeUtils.STANDARD_FORMAT));
        //忽略在json字符串中存在，但是在java中不存在的属性，防止出错
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return objectMapper;
    }
}
