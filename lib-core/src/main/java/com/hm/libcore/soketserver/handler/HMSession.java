package com.hm.libcore.soketserver.handler;

import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf.HMRequest;
import com.hm.libcore.protobuf.HMProtobuf.HMResponse;
import com.hm.libcore.protobuf.HMRespCreater;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.soketserver.ISession;
import com.hm.libcore.util.compression.Gzip;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Title: HMSession.java Description: Copyright: Copyright (c) 2014 Company:
 * Hammer Studio
 * 
 * @author 叶亮
 * @date 2014-8-8
 * @version 1.0
 */
@NoArgsConstructor
@Slf4j
public class HMSession implements ISession {
	private ChannelHandlerContext ctx;
	
	private String id;
	//玩家主体
	private Object attachment;

	private Map<String, Object> attribute = new HashMap<String, Object>();

	private int socketType;//0-正常客户端连接 1-H5客户端连接
    private long connId;


	public HMSession(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		this.id = this.ctx.channel().id().asShortText();
	}

	public void write(HMResponse obj) {
		debugResp(obj);
		this.ctx.writeAndFlush(obj);
	}
	
	public void write(HMRequest obj) {
		this.ctx.writeAndFlush(obj);
	}

	public void close() {
		this.ctx.close();
	}

	public void setAttribute(String key, Object value) {
		this.attribute.put(key, value);
	}

	public Object getAttribute(String key) {
		return this.attribute.get(key);
	}
	
	public void removeAttribute(String key){
		this.attribute.remove(key);
	}
	
	
	public Object getAttachment() {
		return attachment;
	}

	public void setAttachment(Object attachment) {
		this.attachment = attachment;
	}

	public boolean isConnect() {
		try {
			if(ctx != null) {
				return ctx.channel().isActive();
			}
		} catch (Exception e) {
		}
		return false;
	}

	public String id() {
		return this.id;
	}
	
	public Channel getChannel() {
		return ctx.channel();
	}

	public void disConnect() {
		this.ctx.disconnect();
	}

	public void disConnect(ChannelPromise channelPromise) {
		this.ctx.disconnect(channelPromise);
	}

	public void close(ChannelPromise channelPromise) {
		this.ctx.close(channelPromise);
	}
	
	public Set<Entry<String,Object>> getAttributes(){
		return this.attribute.entrySet();
	}

	public int getSocketType() {
		return socketType;
	}

	public void setSocketType(int socketType) {
		this.socketType = socketType;
	}

	private void debugResp(HMResponse resp) {
		if (ServerConfig.getInstance().isDebug() && resp.getMsgId() != 2330) {
			Map<String, Object> debugResp = new HashMap<String, Object>();
			debugResp.put("msgId", resp.getMsgId());
			debugResp.put("code", resp.getCode());
			debugResp.put("isCompress", resp.getIsCompress());
			if (resp.getIsCompress()) {// 压缩过
				byte[] original = Gzip.unGZip(resp.getData().toByteArray());
				debugResp.put("data", new String(original));
			} else {
				debugResp.put("data", resp.getData().toStringUtf8());
			}
			long playerId = this.attribute.containsKey("playerId") ? (long)this.attribute.get("playerId") : 0;
			
			log.info(getRemoteAddress()+" playerId="+ playerId+" send="+resp.getMsgId()+":length   "+ resp.getSerializedSize()  + "  " + JSONUtil.toJson(debugResp));
		}
	}

	public boolean containsAttrKey(String key){
		return this.attribute.containsKey(key);
	}
	
	public String getRemoteAddress(){
		return this.ctx.channel().remoteAddress().toString();
	}
	
	public String getRemoteIp(){
		String remoteAddress = this.getRemoteAddress().replace("/", "");
		String[] address = remoteAddress.split(":");
		return address[0]; 
	}
	

    public long getConnId() {
        return connId;
    }

    public void setConnId(long connId) {
        this.connId = connId;
    }

	public void clearSession() {
		setAttachment(null);
		close();
	}
}
