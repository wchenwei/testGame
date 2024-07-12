package com.hm.libcore.util.sensitiveWord.sensitive;


import com.hm.libcore.util.sensitiveWord.SensitiveProxy;

/**
 * 敏感词
 */
public class SysWordSensitiveUtil {
    private static SensitiveProxy porxy;
    public static SensitiveProxy getInstance(){
        if(porxy==null){
            porxy = new SensitiveProxy();
        }
        return porxy;
    }
}
