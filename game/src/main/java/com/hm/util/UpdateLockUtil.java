package com.hm.util;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author wyp
 * @description
 *    修改限制
 * @date 2022/6/30 16:15
 */
public class UpdateLockUtil {
    public static long startTime;
    public static long endTime;
    // public static final int C2S_Player_Rename = 2311;//修改名字
    // public static final int C2S_Player_ChangeFrame = 2359;//改变头像框
    // public static final int C2S_Guild_ChangeName = 30021;//修改部落名字
    // public static final int C2S_Friend_Chat = 332365;  // 私聊
    public static List<Integer> lockMsgIdList = Lists.newArrayList(2311,2359,30021, 332365);

    public static boolean isLockUpdate(int msgId){
        if(startTime <= 0 || endTime <= 0){
            return false;
        }
        if(!CollUtil.contains(lockMsgIdList, msgId)){
            return false;
        }
        return isNoChange();
    }

    public static boolean isNoChange() {
        long timeMillis = System.currentTimeMillis();
        return timeMillis >= startTime && timeMillis <= endTime;
    }
}
