package com.hm.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.chat.ChatRpcUtils;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.PlayerMsgForwardProxy;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.message.MessageIDUtils;
import com.hm.message.ServerMsgType;
import com.hm.rpc.KFRpcType;
import com.hm.rpc.RpcManager;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 业务服消息转发
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/7/23 16:52
 */
@Slf4j
@Biz
public class GamePlayerMsgForwardProxy implements PlayerMsgForwardProxy {
    public Map<String,String> urlMap = Maps.newHashMap();

    public void register(String key,String url) {
        if(StrUtil.isEmpty(url)) {
            System.out.println(key+" register url is null");
            return;
        }
        urlMap.put(key,url);
    }

    public void register(ServerMsgType kfType,int serverId,String url) {
        String key = kfType.getId()+"_"+serverId;
        register(key,url);
    }

    @Override
    public boolean msgForward(HMProtobuf.HMRequest msg) {
        ServerMsgType serverMsgType = MessageIDUtils.getServerMsgType(msg.getMsgId());
        if (serverMsgType == ServerMsgType.GameBiz) {
            return false;//业务消息,还给本服务器处理
        }
        if(serverMsgType.isKFMsg()) {
            doKFMsg(msg,serverMsgType);
        }else if(serverMsgType == ServerMsgType.Chat) {
            ChatRpcUtils.sendMsg(msg);
        }
        return true;
    }


    public void doKFMsg(HMProtobuf.HMRequest msg, ServerMsgType serverMsgType) {
        long playerId = Convert.toLong(msg.getPlayerId(),0L);
        int serverId = ServerUtils.getServerId(playerId);
        String key = serverMsgType.getId()+"_"+serverId;
        String url = this.urlMap.get(key);
        if(url == null) {
            log.error(playerId+"_"+key+" 转发跨服找不到url");
            return;
        }
        RpcManager.sendMsg(KFRpcType.KF,url,msg);
    }
}
