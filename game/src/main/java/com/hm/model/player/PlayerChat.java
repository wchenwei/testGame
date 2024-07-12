package com.hm.model.player;

import com.google.common.collect.Sets;
import com.hm.libcore.msg.JsonMsg;

import java.util.LinkedHashSet;


/**
 * 
 * ClassName: PlayerMail. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2017年12月18日 下午4:28:10 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class PlayerChat extends PlayerDataContext{
	private LinkedHashSet<Long> blackSet = Sets.newLinkedHashSet();  // 黑名单

	
	public void addBlack(long id){
		blackSet.add(id);
		SetChanged();
	}
	
	public void delBlack(long id){
		blackSet.remove(id);
		SetChanged();
	}


	
	
	public LinkedHashSet<Long> getBlackSet() {
		return blackSet;
	}


	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("blackList",blackSet);
	}
	
	
}
