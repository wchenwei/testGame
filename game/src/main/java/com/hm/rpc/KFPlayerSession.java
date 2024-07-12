package com.hm.rpc;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.libcore.protobuf.HMRespCreater;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.libcore.util.compression.Gzip;
import com.hm.message.MessageComm;
import com.hm.observer.ObservableEnum;

import java.util.HashMap;
import java.util.Map;

public abstract class KFPlayerSession extends DBEntity<Long>{
	public void sendErrorMsg(int code) {
		sendResponse(HMRespCreater.createErrorHMresponse(getId(),MessageComm.S2C_ErrorMsg, code));
	}

	public void sendMsg(int msgId) {
		sendMsg(JsonMsg.create(msgId));
	}

	public void sendMsg(JsonMsg msg) {
		msg.setPlayerId(getId());
		//转换msgId
		sendResponse(msg.createResponse());
	}
	public void sendMsg(int msgId, Object obj) {
		sendResponse(HMRespCreater.createSucHMResponse(msgId, obj));
	}

	public void sendResponse(HMProtobuf.HMResponse response) {
		debugResp(response);

		try {
			IGameRpc gameRpc = GameRpcClientUtils.getGameRpcByServerId(getServerId());
			if(gameRpc != null) {
				gameRpc.sendPlayerHMResponse(getId(),response.toByteArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendNormalErrorMsg(int msgId) {
		sendResponse(HMRespCreater.createNormalErrorHMResponse(msgId));
	}

	public boolean addOrSpendPlayerItem(String items,boolean isAdd,String log) {
		if(StrUtil.isEmpty(items)) {
			return true;
		}
		try {
			IGameRpc gameRpc = GameRpcClientUtils.getGameRpcByServerId(getServerId());
			if(gameRpc == null) {
				return false;
			}
			return gameRpc.addOrSpendPlayerItem(getId(),items,isAdd,log);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void notifyObservers(ObservableEnum observableEnum, Object... argv) {
		try {
			IGameRpc gameRpc = GameRpcClientUtils.getGameRpcByServerId(getServerId());
			if(gameRpc == null) {
				return;
			}
			gameRpc.sendPlayerObs(getId(),observableEnum.getEnumId(),argv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void debugResp(HMProtobuf.HMResponse resp) {
		if (ServerConfig.getInstance().isDebug()) {
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
			long playerId = getId();

			System.out.println(" playerId="+ playerId+" send="+resp.getMsgId()+":length   "+ resp.getSerializedSize()  + "  " + JSONUtil.toJson(debugResp));
		}
	}
}
