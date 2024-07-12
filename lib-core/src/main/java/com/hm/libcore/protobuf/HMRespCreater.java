package com.hm.libcore.protobuf;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.util.compression.Gzip;
import com.hm.libcore.util.gson.GSONUtils;


/**
 * Title: HMRespCreater.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014-11-3 13:50
 * @version 1.0
 */
public final class HMRespCreater {
	
	/**
	 * 创建成功的返回对象
	 * @param callback
	 * @param data
	 * @param isCompress
	 * @return
	 */
	public static HMProtobuf.HMResponse createSucHMResponse(int msgId,long playerId, Object obj){
		HMProtobuf.HMResponse.Builder builder = HMProtobuf.HMResponse.newBuilder();
		builder.setMsgId(msgId);
		builder.setCode(1);// 小于等于0出错 1成功
		builder.setPlayerId(playerId+"");
		String value = GSONUtils.ToJSONString(obj);
		boolean isCompress = value.length() > 300;
		if(isCompress){//压缩数据
			builder.setData(ByteString.copyFrom(Gzip.gZip(value.getBytes())));
		}else{//不压缩数据
			builder.setData(ByteString.copyFromUtf8(value));
		}
		builder.setIsCompress(isCompress);
		HMProtobuf.HMResponse resp = builder.build();
		return resp;
	}

	public static HMProtobuf.HMResponse createSucHMResponse(int msgId, Object obj){
		return createSucHMResponse(msgId,0,obj);
	}
	
	/**
	 * 创建错误信息对象
	 * @param code
	 * @param msg
	 * @return
	 */
	public static HMProtobuf.HMResponse createErrorHMresponse(int msgId, int code, String msg){
		HMProtobuf.HMResponse.Builder builder = HMProtobuf.HMResponse.newBuilder();
		Map<String,Integer> data = new HashMap<String, Integer>();
		data.put("code", code);
		builder.setMsgId(msgId);
		builder.setCode(code);// 小于等于0出错 1成功
		builder.setIsCompress(false);
		builder.setData(ByteString.copyFromUtf8(JSONUtil.toJson(data)));
		builder.setMsg(msg);
		HMProtobuf.HMResponse resp = builder.build();
		return resp;
	}

	public static HMProtobuf.HMResponse createErrorHMresponse(int msgId, int code){
		return createErrorHMresponse(0,msgId,code);
	}
	/**
	 * 创建错误信息对象
	 * @param code
	 * @param msg
	 * @return
	 */
	public static HMProtobuf.HMResponse createErrorHMresponse(Object playerId,int msgId, int code){
		HMProtobuf.HMResponse.Builder builder = HMProtobuf.HMResponse.newBuilder();
		Map<String,Integer> data = new HashMap<String, Integer>();
		data.put("code", code);
		builder.setMsgId(msgId);
		builder.setCode(code);// 小于等于0出错 1成功
		builder.setPlayerId(playerId.toString());
		builder.setIsCompress(false);
		builder.setData(ByteString.copyFromUtf8(JSONUtil.toJson(data)));
		builder.setMsg("");
		HMProtobuf.HMResponse resp = builder.build();
		return resp;
	}
	
	public static HMProtobuf.HMResponse createNormalErrorHMResponse(int msgId){
		HMProtobuf.HMResponse.Builder builder = HMProtobuf.HMResponse.newBuilder();
		Map<String,Integer> data = new HashMap<String, Integer>();
		builder.setMsgId(msgId);
		builder.setCode(0);// 小于等于0出错 1成功
		builder.setData(ByteString.copyFromUtf8(JSONUtil.toJson(data)));
		builder.setIsCompress(false);
		HMProtobuf.HMResponse resp = builder.build();
		return resp;
	}
	
}
