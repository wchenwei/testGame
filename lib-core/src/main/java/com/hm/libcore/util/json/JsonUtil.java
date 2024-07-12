package com.hm.libcore.util.json;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.hm.libcore.util.jackson.MyJacksonAnnotationIntrospector;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class JsonUtil {
    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        // 转换为格式化的json
        //mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);

        // 仅属性可见
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // 属性为null不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //序列化的时候序列对象的所有属性
//        mapper.setSerializationInclusion(Include.ALWAYS);
        // 使用自定义注解
        mapper.setAnnotationIntrospector(new MyJacksonAnnotationIntrospector());

        // 如果json中有新增的字段并且是实体类类中不存在的，不报错,忽略未知字段
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //如果是空对象的时候,不抛异常
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 设置是否格式化输出
     *
     * @param indent
     */
    public static void setIndent(boolean indent) {
        if (indent) {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        } else {
            mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        }
    }

    public static JsonNode str2JsonNode(String s) throws IOException {
        return mapper.readTree(s);
    }

    public static String object2JsonStr(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static JsonNode createJson(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T jsonStr2Object(String json, Class<T> targetClass) {
        if (StrUtil.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, targetClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    public static <T> List<T> jsonStr2List(String jsonStr) {
//        JavaType javaType = JsonUtil.mapper.getTypeFactory().constructParametricType(ArrayList.class, T.class);
//        return mapper.readValue(jsonStr, javaType);
//    }
    public static ObjectNode obj2ObjectNode(Object o) {
        return mapper.valueToTree(o);
    }

    public static ArrayNode obj2ArrayNode(Object o) {
        return mapper.valueToTree(o);
    }

    public static <T> List<T> jsonStr2List(String json, Class<T> targetClass) {
        if (StrUtil.isEmpty(json)) {
            return Lists.newArrayList();
        }
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, targetClass);
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> obj2List(Object src, Class<T> targetClass) {
        return jsonStr2List(JsonUtil.object2JsonStr(src), targetClass);
    }


    public static <K, V> Map<K, V> jsonStr2Map(String json, Class<K> keyClass, Class<V> valueClass) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(Map.class, keyClass, valueClass);
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
