package com.hm.model.serverpublic;

import com.hm.enums.KfType;
import com.hm.model.serverpublic.kf.AbstractKfData;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 服务器跨服数据
 * @author siyunlong  
 * @date 2019年4月16日 下午8:11:59 
 * @version V1.0
 */
@Getter
public class ServerKfData extends ServerPublicContext {
	private ConcurrentHashMap<Integer,AbstractKfData> kfMap = new ConcurrentHashMap<>();
	private String kfMineIp;//资源跨服地址
	private String kfPkUrl;//跨服段位赛
	private String kfExtortUrl;//跨服征讨
	private transient String kfMinehttpIp;//资源跨服地址
	
	public void setKfData(KfType type,AbstractKfData kfData) {
		this.kfMap.put(type.getType(), kfData);
		SetChanged();
	}
	
	public void clearKfData(KfType type) {
		this.kfMap.remove(type.getType());
	}
	
	public void loadHourCheck() {
		for (AbstractKfData kfData : this.kfMap.values()) {
			if(kfData.loadHourCheck()) {
				SetChanged();
			}
		}
		if(Changed()) {
			save();
		}
	}
	
	public AbstractKfData getKfData(KfType type) {
		return kfMap.get(type.getType());
	}


	public void setKfMineIp(String kfMineIp) {
		this.kfMineIp = kfMineIp;
	}

	public String getKfMinehttpIp() {
		return kfMinehttpIp;
	}

	public void setKfMinehttpIp(String kfMinehttpIp) {
		this.kfMinehttpIp = kfMinehttpIp;
	}


	public void setKfPkUrl(String kfPkUrl) {
		this.kfPkUrl = kfPkUrl;
	}


	public void setKfExtortUrl(String kfExtortUrl) {
		this.kfExtortUrl = kfExtortUrl;
	}
}

