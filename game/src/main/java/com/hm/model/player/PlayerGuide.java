package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @Description: 新手引导
 * @author siyunlong  
 * @date 2018年12月8日 下午2:10:30 
 * @version V1.0
 */
@Getter
public class PlayerGuide extends PlayerDataContext{
	private String mainId;
	private int subId;
	private boolean skip;
	private ConcurrentHashMap<Integer,Integer> speGuide = new ConcurrentHashMap<Integer, Integer>();
	
	public void changeGuide(String mainId,int subId) {
		this.mainId = mainId;
		this.subId = subId;
		SetChanged();
	}
	public void skipGuide(){
		this.skip = true;
		SetChanged();
	}
	public void changeSpeGuide(int id,int value){
		this.speGuide.put(id, value);
		SetChanged();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerGuide", this);
	}
}
