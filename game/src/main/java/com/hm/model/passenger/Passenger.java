package com.hm.model.passenger;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.PassengerConfig;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {
	private String uid; //唯一id
    private int id;  //乘员id
    private int lv=1;
    private int tankId;//所属tankId
    private int[] skills = new int[3];//技能id
    //锁定的技能
    private List<Integer> lock = Lists.newArrayList();
    private int star=0;//星级
    
    public Passenger(int id,int serverId) {
        this.id = id;
        uid = serverId+"_"+PrimaryKeyWeb.getPrimaryKey("Passenger",serverId);
        PassengerConfig passengerConfig = SpringUtil.getBean(PassengerConfig.class);
        //随机出一个技能
        skills[0] = passengerConfig.createSkills(id, 1).get(0);
    }

	public void lvUp(int count) {
		PassengerConfig passengerConfig = SpringUtil.getBean(PassengerConfig.class);
		this.lv= Math.min(passengerConfig.getMaxLv(id), this.lv+count);
	}
	public boolean isLock(int index){
		return lock.contains(index);
	}
	
	public void lock(int index) {
		this.lock.add(index);
	}
	
	public void unLock(int index) {
		this.lock.remove(new Integer(index));
	}
	public List<Integer> getUnLocks(){
		List<Integer> unLocks = Lists.newArrayList(1,2,3);
		unLocks.removeAll(this.lock);
		return unLocks;
	}
	
	public void createSkill(int index,int skillId){
		this.skills[index-1] = skillId;
	}

	public void starUp() {
		PassengerConfig passengerConfig = SpringUtil.getBean(PassengerConfig.class);
		int maxStar = passengerConfig.getStarMax(id);
		this.star= Math.min(maxStar, this.star+1);
	}
}
