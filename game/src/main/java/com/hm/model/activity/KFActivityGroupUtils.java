package com.hm.model.activity;

import com.hm.libcore.util.string.StringUtil;

import java.util.List;

/**
 * 跨服活动分组
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/3/16 10:06
 */
public class KFActivityGroupUtils {
    /**
     * 获取活动分组id
     *
     * @param extend
     * @param serverId
     * @return
     */
    public static int getGroupId(String extend, int serverId) {
        //extend:1_20,101_120;21_40,121_140
        //如果serverId满足该组的条件则将分组id命名为起始id,如例子中，满足条件
        try {
            List<String> str = StringUtil.splitStr2StrList(extend, ";");
            for (String ss : str) {
                for (String s : ss.split(",")) {
                    int startId = Integer.parseInt(s.split("_")[0]);
                    int endId = Integer.parseInt(s.split("_")[1]);
                    if (serverId >= startId && serverId <= endId) {
                        return Integer.parseInt(ss.split("_")[0]);
                    }
                }
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }
}
