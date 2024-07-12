package com.hm.util;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.SignUtils;

import java.util.Map;

/**
 * @author xjt
 * @version 1.0
 * @desc 聊天输入合法性监测util
 * @date 2022/4/28 10:30
 */
public class ChatLegalUtil {
    public static boolean checkSign(JsonMsg msg) {
        Map<String, String> paramMap = Maps.newConcurrentMap();
        paramMap.put("content", msg.getString("content"));
        paramMap.put("time", msg.getString("time"));
        paramMap.put("now", msg.getString("now"));
        String sign = msg.getString("sign");
        return StrUtil.equals(sign, SignUtils.buildSing(paramMap, ServerConfig.getInstance().getChatcheckkey()));
    }

    //每百毫秒1.3字节
    public static boolean checkInputLegal(String content, long time, long now) {
//        //校验时间合法性
//        if (time <= 0 || System.currentTimeMillis() - now > 5 * GameConstants.SECOND) {
//            return false;
//        }
//        //检验输入合法性
//        double a = MathUtils.div(content.getBytes().length, time / 100);
//        boolean flag = a < 1.3;
//        return flag;
        return true;
    }
}
