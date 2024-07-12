package com.hm.libcore.msg;

import cn.hutool.core.convert.Convert;
import com.google.protobuf.ByteString;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.soketserver.handler.HMSession;
import com.hm.libcore.util.compression.Gzip;
import com.hm.libcore.util.gson.GSONUtils;
import org.springframework.data.annotation.Transient;

public class JsonMsg extends DefaultMsg{
	private HMProtobuf.HMResponse response;

	@Transient
	private transient HMSession session;

	public JsonMsg(int msgId) {
		this.msgId = msgId;
	}
	
	public static JsonMsg create(int msgId) {
		return new JsonMsg(msgId);
	}


	public void initHMRequest(HMProtobuf.HMRequest msg) {
		try {
			this.playerId = Convert.toLong(msg.getPlayerId(),0L);
    		this.paramMap = JSONUtil.fromJson(msg.getData().toStringUtf8(), paramMap.getClass());
    		this.createTime = System.currentTimeMillis();
		} catch (Exception e) {
		}
	}
	
	public HMProtobuf.HMResponse createResponse(){
		if(this.response != null) {
			return this.response;
		}
		HMProtobuf.HMResponse.Builder builder = HMProtobuf.HMResponse.newBuilder();
		builder.setMsgId(msgId);
		builder.setCode(1);// 小于等于0出错 1成功
		builder.setPlayerId(getPlayerId()+"");
		String value = GSONUtils.ToJSONString(paramMap);
		boolean isCompress = value.length() > 300;
		if(isCompress){//压缩数据
			builder.setData(ByteString.copyFrom(Gzip.gZip(value.getBytes())));
		}else{//不压缩数据
			builder.setData(ByteString.copyFromUtf8(value));
		}
		builder.setIsCompress(isCompress);
		this.response = builder.build();
		return response;
	}
	
	public HMProtobuf.HMRequest createRequest(){
		HMProtobuf.HMRequest.Builder builder = HMProtobuf.HMRequest.newBuilder();
		builder.setMsgId(msgId);
		String value = GSONUtils.ToJSONString(paramMap);
		builder.setData(ByteString.copyFromUtf8(value));
		return builder.build();
	}


	public HMSession getSession() {
		return session;
	}

	public void setSession(HMSession session) {
		this.session = session;
	}

}
