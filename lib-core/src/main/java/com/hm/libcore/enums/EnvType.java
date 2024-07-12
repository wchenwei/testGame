package com.hm.libcore.enums;


import cn.hutool.core.net.NetUtil;
import com.hm.libcore.ok.HttpRequestMap;
import com.hm.libcore.ok.HttpUtil;
import lombok.Getter;

@Getter
public enum EnvType {
	Inner(0,"内网") {
		@Override
		public String getOutIp() {
			return NetUtil.localIps().stream().filter(e -> e.startsWith("192.168")).findFirst().get();
		}

		@Override
		public String getInnerIp() {
			return NetUtil.localIps().stream().filter(e -> e.startsWith("192.168")).findFirst().get();
		}
	},
	TX(1,"tx") {
		@Override
		public String getOutIp() {
			return HttpUtil.httpGet(HttpRequestMap.create("http://metadata.tencentyun.com/meta-data/public-ipv4"));
		}

		@Override
		public String getInnerIp() {
			return HttpUtil.httpGet(HttpRequestMap.create("http://metadata.tencentyun.com/latest/meta-data/local-ipv4"));
		}
	},
	;

	/**
	 * @param type
	 * @param desc
	 */
	private EnvType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	
	private int type;
	private String desc;

	public abstract String getOutIp();
	public abstract String getInnerIp();

	public static EnvType getType(int type) {
		for (EnvType buildType : EnvType.values()) {
			if (buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
}
