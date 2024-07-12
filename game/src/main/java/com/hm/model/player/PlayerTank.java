package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.TankConfig;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankControl;
import com.hm.model.tank.TankMagicReform;
import com.hm.model.tank.TankVo;
import com.hm.war.sg.setting.TankSetting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 玩家坦克
 * ClassName: PlayerTank. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2017年12月18日 下午4:28:10 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class PlayerTank extends PlayerDataContext{
	private ConcurrentHashMap<Integer, Tank> tankMap = new ConcurrentHashMap<>(); //玩家坦克(id,坦克)
	//坦克成就数据
	private List<Integer> taskIds = Lists.newArrayList();
	
	/**
	 * 添加坦克
	 *
	 * @author yanpeng 
	 * @param tank  
	 *
	 */
	public void addTank(Tank tank) {
		this.tankMap.put(tank.getId(), tank);
		SetChanged();
	}
	
	
	/**
	 * 获取坦克
	 *
	 * @author yanpeng 
	 * @param tankId
	 * @return  
	 *
	 */
	public Tank getTank(int tankId){
		return this.tankMap.get(tankId);
	}
	
	public boolean isHaveTank(int tankId){
		return this.tankMap.containsKey(tankId);
	}
	
	public List<Tank> getTankList(){
		return Lists.newArrayList(this.tankMap.values());
	}
	public List<Tank> getTankByIds(List<Integer> ids){
		return ids.stream().map(t -> tankMap.get(t)).collect(Collectors.toList());
	}
	public List<TankVo> createTankVos(List<Integer> ids){
		return ids.stream().map(t -> tankMap.get(t).createTankVo()).collect(Collectors.toList());
	}
	
	public long getTankTotalCombat(){
		return tankMap.values().stream().mapToLong(Tank::getCombat).sum(); 
	}
	
	public long getTankCombatIds(int[] tankIds){
		return Arrays.stream(tankIds).mapToLong(e->{
			return tankMap.get(e).getCombat();
		}).sum();
	}

	public long getTankCombatIds(List<Integer> tankIds){
		return tankIds.stream().mapToLong(e->tankMap.get(e).getCombat()).sum();
	}
	
	public long getTankTotalOil(List<Integer> tankIds){
		return tankIds.stream().mapToLong(t->tankMap.get(t).getOil()).sum(); 
	}
	public int getMaxCombatTankId(List<Integer> tankIds){
		Tank tank = tankIds.stream().map(t ->getTank(t)).max(Comparator.comparingLong(Tank::getCombat)).get();
		return tank.getId();
	}
	//更改坦克的当前血量
	public void changeHp(int tankId, long hp) {
		Tank tank = getTank(tankId);
		if(tank==null){
			return;
		}
		tank.setCurHp(hp);
		SetChanged();
	}
	
	public List<Integer> getTankIdList(){
		return Lists.newArrayList(tankMap.keySet()); 
	}
	public List<Integer> getTaskIds(){
		return taskIds;
	}
	
	public void changeTaskIds(List<Integer> taskIds){
		this.taskIds = taskIds;
		SetChanged();
	}
	
	/**
	 * 装备乘员
	 *
	 * @author xjt 
	 * @param tankId
	 * @param partsId  
	 *
	 */
	public void addTankPassenger(int tankId,String passengerId,int pos){
		Tank tank = getTank(tankId);
		if(tank != null){
			tank.addPassenger(passengerId,pos);
			SetChanged();
		}
	}
	/**
	 * 卸载乘员
	 *
	 * @author yanpeng 
	 * @param tankId
	 * @param partsId
	 * @param pos  
	 *
	 */
	public void delTankPassenger(int tankId,int pos){
		Tank tank = tankMap.get(tankId);
		if(tank != null){
			tank.delPassenger(pos);
			SetChanged();
		}
	}
	
	public void resetDay() {
		getTankList().forEach(t->{
			t.getTankSpecial().resetTimes();
		});
		SetChanged();
	}


	public void magicReform(int tankId, int skill) {
		Tank tank = tankMap.get(tankId);
		if(tank != null){
			tank.magicReform(skill);
			SetChanged();
		}
	}
	public void changeMagic(int tankId, TankMagicReform tankMagicReform) {
		Tank tank = tankMap.get(tankId);
		if(tank != null){
			tank.setTankMagicReform(tankMagicReform);
			SetChanged();
		}
	}

	public void changeControl(int tankId, TankControl tankControl) {
		Tank tank = tankMap.get(tankId);
		if(tank != null){
			tank.setControl(tankControl);
			SetChanged();
		}
	}


	public void strength(int tankId,int id) {
		Tank tank = tankMap.get(tankId);
		if(tank != null){
			tank.strength(id);
			SetChanged();
		}
	}


	public List<Integer> getTankByRare(int type) {
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		return getTankIdList().stream().filter(t->{
			TankSetting setting = tankConfig.getTankSetting(t);
			return setting.getRare()==type;
		}).collect(Collectors.toList());
	}

	public List<Integer> getTankByStar(int star) {
		return tankMap.values().stream().filter(t -> t.getStar() == star).map(Tank::getId).collect(Collectors.toList());
	}

	public List<Integer> getTankByLv(int lv) {
		return tankMap.values().stream().filter(t -> t.getLv() >= lv).map(Tank::getId).collect(Collectors.toList());
	}

}