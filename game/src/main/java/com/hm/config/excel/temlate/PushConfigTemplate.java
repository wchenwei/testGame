package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("push_config")
public class PushConfigTemplate {
	private Integer channelId;
	private Integer secretId;
	private String accessKey;

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Integer getSecretId() {
		return secretId;
	}

	public void setSecretId(Integer secretId) {
		this.secretId = secretId;
	}
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
}
