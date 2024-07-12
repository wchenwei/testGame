/**  
 * Project Name:SLG_GameHot.  
 * File Name:tank.java  
 * Package Name:com.hm.model.tank  
 * Date:2018年3月5日下午2:20:31  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.action.chat;

import com.hm.model.guild.Guild;
import com.hm.model.player.SimplePlayerVo;
import com.hm.redis.PlayerRedisData;
import lombok.Data;



/**  
 * ClassName: tank. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年3月5日 下午2:20:31 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Data
public class PlayerBlackVO extends SimplePlayerVo{
	private long id;
	private int guildFlag; 
	private String flagName; 
	
	
	public PlayerBlackVO(long id){
		this.id = id; 
	}
	public void setPlayerInfo(PlayerRedisData playerRedisData) {
		this.guildId = playerRedisData.getGuildId(); 
		this.load(playerRedisData);
	}
	
	public void setGuildInfo(Guild guild){
		if(guild != null){
			this.flagName = guild.getGuildFlag().getFlagName();
		}
		
	}
	
}
  
