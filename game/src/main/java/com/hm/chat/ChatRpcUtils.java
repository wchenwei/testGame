
package com.hm.chat;

import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.protobuf.HMProtobuf;
import com.hm.rpc.KFRpcType;
import com.hm.rpc.RpcManager;


/**
 * 内网聊天通信服务端
 * @version
 */
public class ChatRpcUtils {
	public static String chatUrl;

	public static void startChatClientRpc() {
		InnerChatConfig innerChatConfig = InnerChatConfig.getInnerChatConfig();
		if(innerChatConfig == null) {
			return;
		}
		String host = innerChatConfig.getHost();
		int port = innerChatConfig.getPort();
		chatUrl = "bolt://" + host + ":" + port;
		RpcManager.startRpc(KFRpcType.Chat,chatUrl);
	}

	public static void sendMsg(JsonMsg msg) {
		RpcManager.sendMsg(KFRpcType.Chat,chatUrl,msg);
	}
	public static void sendMsg(HMProtobuf.HMRequest msg) {
		RpcManager.sendMsg(KFRpcType.Chat,chatUrl,msg);
	}

}

