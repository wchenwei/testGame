package com.hm.libcore.rpc;

import com.hm.libcore.msg.JsonMsg;

public interface IRPC {
    void sendInnerMsg(JsonMsg msg);
    void sendPBRequestMsg(byte[] msg);
}
