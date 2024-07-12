package com.hm.rpc;

import com.hm.libcore.inner.InnerMsgRouter;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.rpc.IChatRpc;
import com.hm.libcore.util.gson.GSONUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatRpc implements IChatRpc {

    @Override
    public void sendInnerMsg(JsonMsg msg) {
        log.error(msg.getMsgId()+"->"+GSONUtils.ToJSONString(msg.getParamMap()));
        InnerMsgRouter.getInstance().process(msg);
    }

    @Override
    public void sendPBRequestMsg(byte[] data) {
        try {
            HMProtobuf.HMRequest msg = HMProtobuf.HMRequest.parseFrom(data);
            JsonMsg clientMsg = new JsonMsg(msg.getMsgId());
            clientMsg.initHMRequest(msg);

            sendInnerMsg(clientMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
