package com.hm.libcore.util;

import cn.hutool.crypto.SecureUtil;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/4/24 17:12
 */
public class SignUtils {
    public static String buildSing(Map<String, String> paramMap,String addStr){
        List<String> keys = Lists.newArrayList(paramMap.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key + "=" + paramMap.get(key) + "&");
        }
        if (sb.length() > 0) {
            String data = sb.substring(0, sb.length() - 1).toString() + addStr;
            String sign = SecureUtil.md5(data);
            return sign;
        }
        return "";
    }
}
