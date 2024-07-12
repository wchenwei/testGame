package com.hm.libcore.msg;

import com.google.common.collect.Maps;
import com.hm.libcore.action.IAction;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.protobuf.HMProtobuf;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 消息分发类
 * 
 * @Author: siyunlong
 * @Version: V1.00
 * @Create Date: 2015-5-8下午3:31:24
 */
@Getter
@Slf4j
public class Router {

	private static final Router instance = new Router();
	public static Router getInstance() {
		return instance;
	}
	/**
     * 消息处理器集合
     */
    private final Map<Integer,IAction> msgProcesser = Maps.newHashMap();

    private PlayerMsgForwardProxy msgForwardProxy;

    public IAction getAction(int msgId) {
        return msgProcesser.get(msgId);
    }

    /**
     * 注册消息处理类
     *
     * @param msgId 消息id
     * @param process 消息处理器
     */
    public void registMsg(int msgId, IAction action) {
    	if(msgProcesser.containsKey(msgId)) {
    		System.err.println("!!!!消息号重复啦:"+msgId);
    	}
        msgProcesser.put(msgId, action);
    }

    public void process(HMProtobuf.HMRequest msg, HMSession session) {
        process(msg, session, true);
    }

    /**
     * 处理消息
     *
     * @param msg 具体的消息
     * @param ctx Netty ChannelHandlerContext
     */
    public void process(HMProtobuf.HMRequest msg, HMSession session, boolean isLock) {
    	int msgId = msg.getMsgId();
        if (msgForwardProxy != null && msgForwardProxy.msgForward(msg)) {
            return;
        }
        try {
        	IAction process = msgProcesser.get(msgId);
            if (process != null) {
            	JsonMsg clientMsg = new JsonMsg(msgId);
            	clientMsg.initHMRequest(msg);

                if (isLock) {
                    synchronized (session) {
                        process.doProcess(clientMsg, session);
                    }
                } else {
                    process.doProcess(clientMsg, session);
                }
            }else{
            	log.error("消息号"+msgId+"未找到处理方法action");
            }
        } catch (Exception ex) {
        	log.error("处理消息号"+msgId+"出错!",ex);
        } 
    }
    public void setMsgForwardProxy(PlayerMsgForwardProxy msgForwardProxy) {
        this.msgForwardProxy = msgForwardProxy;
    }
}
