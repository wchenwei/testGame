package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.action.mission.vo.MissionFightTemp;
import com.hm.config.GameConstants;
import com.hm.enums.WorldType;
import com.hm.libcore.msg.JsonMsg;
import lombok.Data;

import java.util.Map;

/**
 * @Description: 玩家临时信息
 * @author siyunlong  
 * @date 2018年11月5日 下午7:25:01 
 * @version V1.0
 */
@Data
public class PlayerTemp extends PlayerDataContext{
	private long lastMsgTime;//此玩家最后一次发生消息的时间
	private int curCityId;//当前所处的cityId
	private WorldType worldType;//当前所在世界
	private long lastPvpTime;
	private boolean cloneLogin;//是否是Gm克隆登录玩家账号
    private boolean isGm;
	
	private Map<String,String> clientMap = Maps.newConcurrentMap();
	private long lastCHHeartTime;
	//战役临时信息
	private MissionFightTemp missionFightTemp;
	//跨服服务器id
	private KFPServerUrl kfpServerUrl;

	
	public boolean isCanPvp() {
		return System.currentTimeMillis() > lastPvpTime;
	}
	public void updatePvpTime() {
		this.lastPvpTime = System.currentTimeMillis()+GameConstants.SECOND;
	}
	

	public boolean isWorldView() {
		return this.worldType != null;
	}


    public void clearCloneLogin() {
        this.cloneLogin = false;
        SetChanged();
    }
	public void initClientParm(JsonMsg msg) {
		try {
			buildCleintParm(msg, "dn");
			buildCleintParm(msg, "mac");
			buildCleintParm(msg, "cimei");
			buildCleintParm(msg, "ai");
			buildCleintParm(msg, "oaid");
			buildCleintParm(msg, "idfa");
			buildCleintParm(msg, "mt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void buildCleintParm(JsonMsg msg,String key) {
		if(msg.containsKey(key)) {
			this.clientMap.put(key, msg.getString(key));
		}
	}
	public String getClientParm(String key,String defaultValue) {
		return this.clientMap.getOrDefault(key, defaultValue);
	}

	public void leaveWorld() {
		this.curCityId = 0;
		this.worldType = null;
	}

	public void doLoginOut() {
		this.curCityId = 0;
		this.worldType = null;
		this.kfpServerUrl = null;
	}
}
