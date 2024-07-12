package com.hm.message;


import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务器类型
 *
 * @author siyunlong
 * @date 2021/2/25
 */
public enum ServerMsgType {
    None(0, 0, 0, null),//无
    GameBiz(1, 0, 2000000, MessageComm.class),
    Chat(2, 2100000, 2200000, ChatMessageComm.class),//聊天

    KFRank(3, 3100000, 3200000, KFRankMessageComm.class),//跨服
    InnerMsg(4, 3200000, 3300000, InnerMessageComm.class),//跨服
    KFYZ(5, 3300000, 3400000, KFYZMessageComm.class),//跨服
    KFScore(6, 3400000, 3500000, KFScoreMessageComm.class),//跨服

    ;

    private int id;
    private int startId;
    private int endId;
    private Class messageClass;

    ServerMsgType(int id, int startId, int endId, Class messageClass) {
        this.id = id;
        this.startId = startId;
        this.endId = endId;
        this.messageClass = messageClass;
    }

    public int getId() {
        return id;
    }

    public boolean isFitId(int id) {
        return id > startId && id < endId;
    }

    public Class getMessageClass() {
        return messageClass;
    }

    public static ServerMsgType getServerMsgType(int type) {
        return Arrays.stream(ServerMsgType.values()).filter(e -> e.getId() == type).findFirst().orElse(null);
    }

    public boolean isKFMsg() {
        return this == KFRank || this == KFYZ || this == KFScore;
    }

    /**
     * 加载消息id所匹配的服务器类型
     *
     * @return
     */
    public static Map<Integer, ServerMsgType> checkAllMessageId() {
        Map<Integer, ServerMsgType> msgIdMap = new HashMap<>();
        for (ServerMsgType value : ServerMsgType.values()) {
            if (value.getId() <= 0) {
                continue;
            }
            try {
                for (Field field : ReflectUtil.getFields(value.getMessageClass())) {
                    int msgId = field.getInt(null);
                    if (value.isFitId(msgId)) {
                        if(msgIdMap.containsKey(msgId)) {
                            System.out.println(value.getMessageClass().getSimpleName() + " message id 重复 :" + msgId);
//                            throw new RuntimeException(value.getMessageClass().getSimpleName() + " message id 重复 :" + msgId);
                        }
                        msgIdMap.put(msgId, value);
                    } else {
                        throw new RuntimeException(value.getMessageClass().getSimpleName() + " message id 不在设定区间以内 :" + msgId);
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return msgIdMap;
    }

}
