package com.hm.servercontainer;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdCodeFilterIpWhiteContainer {
	private List<String> whiteIps = Lists.newArrayList();
	
	public void loadWhiteIps(List<String> ips){
		this.whiteIps = ips;
	}

	public List<String> getWhiteIps() {
		return whiteIps;
	}
	
}
