package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.PassengerConfig;
import com.hm.model.passenger.Passenger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerPassenger extends PlayerBagBase  {
	private ConcurrentHashMap<String, Passenger> passengerMap = new ConcurrentHashMap<>(); //乘员

	//获得乘员
	public void addPassenger(Passenger passenger) {
		this.passengerMap.put(passenger.getUid(), passenger);
		SetChanged();
	}
	
	public Passenger getPassenger(String uid) {
		if(StrUtil.isEmpty(uid)) {
			return null;
		}
		return passengerMap.get(uid);
	}
	
	public boolean isLock(String uid, int index){
		Passenger passenger = getPassenger(uid);
		return passenger.isLock(index);
	}
	
	public void lock(String uid, int index) {
		Passenger passenger = getPassenger(uid);
		if(passenger==null){
			return;
		}
		passenger.lock(index);
		SetChanged();
	}
	public void unLock(String uid, int index) {
		Passenger passenger = getPassenger(uid);
		if(passenger==null){
			return;
		}
		passenger.unLock(index);
		SetChanged();
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerPassenger", this);
	}

	public void culture(String uid) {
		Passenger passenger = getPassenger(uid);
		if(passenger==null){
			return;
		}
		PassengerConfig passengerConfig = SpringUtil.getBean(PassengerConfig.class);
		//根据配置随机出未锁定的技能
		List<Integer> unlocks = passenger.getUnLocks();
		List<Integer> skillIds = passengerConfig.createSkills(passenger.getId(), unlocks.size());
		for(int i=0;i<unlocks.size();i++){
			passenger.createSkill(unlocks.get(i), skillIds.get(i));
		}
		SetChanged();
	}

	public void delPassenger(String uid) {
		this.passengerMap.remove(uid);
		SetChanged();
	}


	


}
