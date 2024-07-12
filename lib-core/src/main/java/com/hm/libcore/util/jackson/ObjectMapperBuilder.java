package com.hm.libcore.util.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

public class ObjectMapperBuilder {
    /**
     * 解释：
     * 字段被 static,transient,@Transient 修饰的不存储转换
     * 构建jackson构建器
     *
     * @return
     */
    public static ObjectMapper buildJacksonObjectMapper() {
        ObjectMapper typingMapper = new ObjectMapper();
        // 仅属性可见
        typingMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        typingMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        // 属性为null不序列化
        typingMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 使用自定义注解
        typingMapper.setAnnotationIntrospector(new MyJacksonAnnotationIntrospector());
        // 忽略未知字段
        typingMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 多态序列化配置
        // NON_FINAL: 所有非final类型或者非final类型元素的数组
        // JAVA_LANG_OBJECT: 当对象属性类型为Object时生效
        // OBJECT_AND_NON_CONCRETE: 当对象属性类型为Object或者非具体类型（抽象类和接口）时生效
        // NON_CONCRETE_AND_ARRAYS: 同上, 另外所有的数组元素的类型都是非具体类型或者对象类型
        typingMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 无属性bean不报异常
        typingMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return typingMapper;
    }

    public static void main(String[] args) {
        ObjectMapper mapper = buildJacksonObjectMapper();
//		mapper.readValue(src, valueType);
    }
}
