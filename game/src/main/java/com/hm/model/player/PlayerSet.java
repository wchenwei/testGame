/**  
 * Project Name:SLG_GameHot.
 * File Name:PlayerSet.java  
 * Package Name:com.hm.model.player  
 * Date:2018年6月28日上午10:30:17  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.PushSetConfig;
import com.hm.enums.UserSetTypeEnum;

import java.util.List;
import java.util.Map;

/**  
 * ClassName: PlayerSet. <br/>  
 * Function: 玩家的设置信息. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年6月28日 上午10:30:17 <br/>  
 *  
 * @author zxj  
 * @version   
 */
public class PlayerSet extends PlayerDataContext{
	//玩家设置配置
	//Integer:对应UserSetTypeEnum;String:状态;0,关闭；1:开启
	private Map<Integer, Integer> playerSet = Maps.newHashMap();


	public void setState(int type, int state) {
		playerSet.put(type, state);
		this.SetChanged();
	}
	
	public int getState(UserSetTypeEnum userSet) {
		return this.playerSet.getOrDefault(userSet.getType(), 0);
	}
	
	public boolean isClose(UserSetTypeEnum userSet) {
		//默认为打开，没有此配置的时候，为开启；1是开启，默认拒绝。0是关闭，不拒绝
		if(!playerSet.containsKey(userSet.getType()) || playerSet.get(userSet.getType())==PushSetConfig.CLOSE) {
			return true;
		}
		return false;
	}
	
	public void initCreateSetting(List<Integer> defaultList) {
		for (int type : defaultList) {
			this.playerSet.put(type, 1);
			SetChanged();
		}
	}




	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("playerSet", playerSet);
	}
}







