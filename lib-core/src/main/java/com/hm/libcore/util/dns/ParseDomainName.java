package com.hm.libcore.util.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Title: ParseDomainName.java 
 * Description:DNS解析类
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * 
 * @author 叶亮
 * @date 2015年3月31日 下午2:17:28
 * @version 1.0
 */

public class ParseDomainName {
	InetAddress myServer = null;
	InetAddress myIPaddress = null;
	String domainName = null;

	public ParseDomainName(String domainName) {
		this.domainName = domainName;
	}

	public InetAddress getServerIP() {
		try {
			myServer = InetAddress.getByName(domainName);
		} catch (UnknownHostException e) {
		}
		return (myServer);
	}

	// 取得LOCALHOST的IP地址
	public InetAddress getMyIP() {
		try {
			myIPaddress = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
		}
		return (myIPaddress);
	}
}
