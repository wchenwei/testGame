package com.hm.model.activity.kfactivity;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 跨服url映射
 * @date 2023/3/23 9:43
 */
public class KFServerInfoUtil {
    public static Map<String, String> kfUrlMap = Maps.newHashMap();

    public static void init() {
        Map<String, String> tempMap = Maps.newHashMap();
        for (KfServerInfo serverInfo : KfServerInfo.getAllKfServerInfo()) {
            if (StrUtil.isNotEmpty(serverInfo.getOutUrl())) {
                tempMap.put(serverInfo.getClientUrl(), serverInfo.getOutUrl());
            }
        }
        kfUrlMap = tempMap;
    }

    public static String getOutUrl(String url) {
        String outUrl = kfUrlMap.get(url);
        return StrUtil.isEmpty(outUrl) ? url : outUrl;
    }

}
