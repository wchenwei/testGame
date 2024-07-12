package com.hm.model.chat;  

import com.hm.model.player.SimplePlayerVo;

/**
 * 玩家红包
 * ClassName: PlayerRedPacket. <br/>    
 * date: 2018年7月18日 下午3:22:37 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class PlayerRedPacket extends SimplePlayerVo{
	private int num; //领取数量 
	private long openTime; //领取时间 
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public long getOpenTime() {
		return openTime;
	}
	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}
	
}
  
