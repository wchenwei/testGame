package com.hm.libcore.soketserver;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.protobuf.HMRespCreater;

public interface ISession {
    public default void sendErrorMsg(int msgId,int code, String msg) {
        write(HMRespCreater.createErrorHMresponse(msgId,code, msg));
    }

    public default void sendErrorMsg(int msgId,int code) {
        write(HMRespCreater.createErrorHMresponse(msgId,code));
    }

    public default void sendNormalErrorMsg(int msgId) {
        write(HMRespCreater.createNormalErrorHMResponse(msgId));
    }

    public default void sendMsg(int msgId, Object obj) {
        write(HMRespCreater.createSucHMResponse(msgId, obj));
    }

    public default void sendMsg(JsonMsg msg) {
        try {
            write(msg.createResponse());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnect();
    public void clearSession();
    public void write(HMProtobuf.HMResponse response);


    public Object getAttribute(String key);
}
