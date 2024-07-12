/**  
 * Project Name:SLG_GameHot.
 * File Name:PlayerStateTime.java  
 * Package Name:com.hm.model.player  
 * Date:2018年7月19日下午4:40:56  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.model.player;

/**  
 * ClassName: PlayerStateTime. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年7月19日 下午4:40:56 <br/>  
 *  
 * @author zxj  
 * @version   
 */
public class PlayerStateTime {
	private long number; //次数
	private long time;	//时间
	
	public PlayerStateTime(long number, long time) {
		this.number = number;
		this.time = time;
	}
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
