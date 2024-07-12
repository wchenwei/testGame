/**  
 * Project Name:SLG_Game.  
 * File Name:PlayerNoticeData.java  
 * Package Name:com.hm.model.notice  
 * Date:2017年9月22日下午2:42:06  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.model.notice;

import com.hm.libcore.db.mongo.DBEntity;

/**  
 * 发送公告，玩家达成的数量
 * ClassName:PlayerNoticeData <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason:   TODO ADD REASON. <br/>  
 * Date:     2017年9月22日 下午2:42:06 <br/>  
 * @author   zigm  
 * @version  1.1  
 * @since    
 */
public class PlayerNoticeData extends DBEntity{
	
	
	private long playerId;
	
	private int count;
	
	private int send;//是否已发送
	
	private int enumType;//

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSend() {
		return send;
	}

	public void setSend(int send) {
		this.send = send;
	}

	public int getEnumType() {
		return enumType;
	}

	public void setEnumType(int enumType) {
		this.enumType = enumType;
	}
}
  
