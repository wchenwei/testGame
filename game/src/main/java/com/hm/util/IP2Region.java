package com.hm.util;

import cn.hutool.core.net.NetUtil;

public class IP2Region {
	public static void main(String[] args) {
		System.err.println(NetUtil.ipv4ToLong("192.144.238.206"));
		System.err.println(NetUtil.longToIpv4(3232235872L));
	}
}
