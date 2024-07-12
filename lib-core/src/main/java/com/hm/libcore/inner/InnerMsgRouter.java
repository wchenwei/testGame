package com.hm.libcore.inner;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.msg.MsgLogUtils;
import com.hm.libcore.util.gson.GSONUtils;
import io.netty.util.collection.IntObjectHashMap;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class InnerMsgRouter {
    private static final InnerMsgRouter instance = new InnerMsgRouter();
    public static InnerMsgRouter getInstance() {
        return instance;
    }

    /**
     * 消息处理器集合
     */
    private final Map<Integer, InnerAction> msgProcesser = new IntObjectHashMap<>();


    public void registMsg(int msgId, InnerAction action) {
        if (msgProcesser.containsKey(msgId)) {
            log.error("!!!!消息号重复啦:" + msgId);
        }
        msgProcesser.put(msgId, action);
    }


    public void process(JsonMsg msg) {
        MsgLogUtils.showMsgLog(msg.getPlayerId(),msg);
        int msgId = msg.getMsgId();
        InnerAction process = msgProcesser.get(msgId);
        if (process != null) {
            process.doProcess(msg);
        }else {
            log.error("消息号 {} 未找到处理方法action, 其他参数为 : {}" , msgId , GSONUtils.ToJSONString(msg.getParamMap()));
        }
    }
}
