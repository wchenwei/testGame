package com.hm.libcore.msg;

import com.hm.libcore.protobuf.HMProtobuf;

/**
 * 玩家消息过滤转发器
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/23 16:49
 */
public interface PlayerMsgForwardProxy {
    boolean msgForward(HMProtobuf.HMRequest msg);
}
