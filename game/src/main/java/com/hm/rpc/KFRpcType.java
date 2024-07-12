package com.hm.rpc;


import com.alipay.sofa.rpc.config.ConsumerConfig;
import com.hm.libcore.rpc.IChatRpc;
import com.hm.libcore.rpc.IKFRpc;
import com.hm.libcore.rpc.IRPC;
import com.hm.libcore.rpc.SFRpcConf;
import lombok.Getter;

@Getter
public enum KFRpcType {
	Chat(0,"聊天") {
		@Override
		public IRPC buildRpc(String url) {
			ConsumerConfig<IChatRpc> consumerConfig = new ConsumerConfig<IChatRpc>()
					.setInterfaceId(IChatRpc.class.getName()) // 指定接口
					.setRepeatedReferLimit(SFRpcConf.repeatedReferLimit)
					.setTimeout(SFRpcConf.timeout)
					.setProtocol("bolt") // 指定协议
					.setDirectUrl(url); // 指定直连地址
			return consumerConfig.refer();
		}
	},
	KF(1,"跨服") {
		@Override
		public IRPC buildRpc(String url) {
			ConsumerConfig<IKFRpc> consumerConfig = new ConsumerConfig<IKFRpc>()
					.setInterfaceId(IKFRpc.class.getName()) // 指定接口
					.setRepeatedReferLimit(SFRpcConf.repeatedReferLimit)
					.setTimeout(SFRpcConf.timeout)
					.setProtocol("bolt") // 指定协议
					.setDirectUrl(url); // 指定直连地址
			return consumerConfig.refer();
		}
	},
	;


	private KFRpcType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	private String desc;

	public abstract IRPC buildRpc(String url);
}
