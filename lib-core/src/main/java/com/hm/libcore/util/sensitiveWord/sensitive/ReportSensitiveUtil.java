package com.hm.libcore.util.sensitiveWord.sensitive;


import com.hm.libcore.util.sensitiveWord.SensitiveProxy;

/**
 * @author xjt
 * @version 1.0
 * @desc 举报关键字
 * @date 2022/3/24 9:13
 */
public class ReportSensitiveUtil {
    private static SensitiveProxy porxy;

    public static SensitiveProxy getInstance() {
        if (porxy == null) {
            porxy = new SensitiveProxy();
        }
        return porxy;
    }


}
