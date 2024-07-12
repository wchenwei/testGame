package com.hm.libcore.util.jackson;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.data.annotation.Transient;


/**
 * @description: Jackson 序列化忽略用非 Jackson 注释注释的字段
 * @author: chenwei
 * @create: 2020-05-09 14:31
 **/

public class MyJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
    private static final long serialVersionUID = 409963148088015915L;

    @Override
    protected boolean _isIgnorable(Annotated a) {
        boolean ignorable = super._isIgnorable(a);
        if (!ignorable) {
            Transient annotation = a.getAnnotation(Transient.class);
            ignorable = annotation != null;
        }
        return ignorable;
    }
}
