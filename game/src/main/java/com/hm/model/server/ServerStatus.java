/**
 * 
 */
package com.hm.model.server;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.util.dns.InetAddressUtils;
import com.hm.libcore.util.dns.ParseDomainName;

import java.util.Map;


/**
 * Title: ServerStatus.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2015年10月19日 上午9:30:09
 * @version 1.0
 */
public class ServerStatus {
	private int serverId;
	private int status;
	private int hotType;
	private String ip_white_list;
	private Map<String,WhiteList> mapWhiteList = Maps.newHashMap();
	private String version;
	
	public Map<String, WhiteList> getMapWhiteList() {
		return mapWhiteList;
	}
	public void setMapWhiteList(Map<String, WhiteList> mapWhiteList) {
		this.mapWhiteList=mapWhiteList;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version=version;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getIp_white_list() {
		return ip_white_list;
	}
	
	public int getHotType() {
		return hotType;
	}
	public void setHotType(int hotType) {
		this.hotType = hotType;
	}
	public void setIp_white_list(String ip_white_list) {
		mapWhiteList.clear();
		if(StrUtil.isNotEmpty(ip_white_list)){
			String[] wl = ip_white_list.split(",");
			for(String iwl : wl){
				String[] host = iwl.split(":");
				//modify by zqh 2017.8.28
				if ( host.length>=2 ) {
					WhiteList wlInfo = new WhiteList();
					wlInfo.setHost(host[0]);
					wlInfo.setType(Integer.parseInt(host[1]));
					if(wlInfo.getType() == 1) {
						mapWhiteList.put(wlInfo.getHost(),wlInfo);
					}else {
						//域名
						if(!InetAddressUtils.isIPv4Address(wlInfo.getHost()) && !InetAddressUtils.isIPv6Address(wlInfo.getHost())){
							ParseDomainName parseDomainName = new ParseDomainName(wlInfo.getHost());
							String wlHost = parseDomainName.getServerIP().getHostAddress();
							mapWhiteList.put(wlHost,wlInfo);
						}
					}
				}
			}
		}
		this.ip_white_list = ip_white_list;
	}
	
	/**  
	 * isInWhiteList:获取白名单状态
	 * @author zqh  
	 * @param host
	 * @return  使用说明
	 */
	public boolean isInWhiteList(String host) {
		if(null == this.mapWhiteList.get(host)) {
			return false;
		}
		return true;
	}
	
	/**  
	 * isInWhiteList:获取白名单状态
	 * @author zqh  
	 * @param host
	 * @return  使用说明
	 */
	public boolean isInWhite(String host) {
		if(this.ip_white_list.contains(host)){
			return true;
		}
		return false;
	}
	
}
