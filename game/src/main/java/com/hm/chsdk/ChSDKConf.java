package com.hm.chsdk;

import cn.hutool.core.util.StrUtil;
import com.hm.util.PubFunc;
import lombok.Getter;
import lombok.Setter;

public class ChSDKConf {
	private int channelId;
	private String serverSDK;

	private int appId;
	private String channelAppId;
	@Getter@Setter
	private boolean isSendCH1 = true;//是否给草花payment接口发送

	public void init() {
		String[] infos = serverSDK.split("_");
		if(infos.length > 0) {
			this.appId = PubFunc.parseInt(infos[0]);
		}
		if(infos.length > 1) {
			this.channelAppId = infos[1];
		}
		if(infos.length > 2) {
			this.isSendCH1 = StrUtil.equals("1",infos[2]);
		}
	}


	public int getChannelId() {
		return channelId;
	}

	public String getSdkInfo() {
		return serverSDK;
	}

	public int getAppId() {
		if(appId <= 0) {
			return CHSDKContants.CPId;
		}
		return appId;
	}

	public String getChannelAppId() {
		if(channelAppId == null) {
			return CHSDKContants.ChannelAppId;
		}
		return channelAppId;
	}


}
