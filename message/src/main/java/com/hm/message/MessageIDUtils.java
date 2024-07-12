package com.hm.message;

import cn.hutool.core.collection.CollUtil;

import java.util.Map;

/**
 * 消息id->业务逻辑
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/19 17:22
 */
public class MessageIDUtils {
    public static Map<Integer, ServerMsgType> msgIdMap;

    public static void init() {
        msgIdMap = ServerMsgType.checkAllMessageId();
    }

    public static ServerMsgType getServerMsgType(int msgId) {
        if (CollUtil.isEmpty(msgIdMap)) {
            init();
        }
        return msgIdMap.get(msgId);
    }
}
