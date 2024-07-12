
package com.hm.action.login.vo;
/**
 * Title: InitLoginVO.java
 * Description:初始游戏返回VO
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年4月24日 上午9:50:07
 * @version 1.0
 */
public class InitLoginVO {

	private long playerId;//玩家ID
	
	private int state;//状态 0为请求创建角色 1为请求加载角色
	
	
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}

