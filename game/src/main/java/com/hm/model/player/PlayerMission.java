package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.enums.BattleType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.observer.ObservableEnum;
import lombok.Getter;

import java.util.List;

/**
 * @Description: 玩家副本数据
 * @author xjt  
 * @date 2018年10月9日16:33:54 
 * @version V1.0
 */
public class PlayerMission extends PlayerDataContext{
	//当前fb进度
	private int missionId=101;

	private List<Integer> receiveGuideIds = Lists.newArrayList();
	private int guideId;
	@Getter
	private int deathWave = -1;//是否处于关卡挂机
	//战斗加速截止时间
	private long gveSpeedTime;

	public int getFbId() {
		return Math.min(MissionConfig.MainMissionMaxId,missionId);
	}

	public int getMissionId() {
		return this.missionId;
	}

	public int getGuideId() {
		return guideId;
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerMission", this);
	}
	
	//是否可以攻打
	public boolean isCanFight(int missionId) {
		//正常通关或攻打军械库
		return this.missionId >= missionId || isCanFightBandits(missionId);
	}
	//通关副本
	public void clearance(int missionId,long clearanceCombat) {
		MissionConfig missionConfig= SpringUtil.getBean(MissionConfig.class);
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		if(this.missionId==missionId && template.getNext_mission() > 0){
			this.missionId = template.getNext_mission();
			super.Context().notifyObservers(ObservableEnum.ClearnceMission,BattleType.MainBattle.getType(), missionId,clearanceCombat,1);
			SetChanged();
		}
		//开启城市
		if(template.getUnlock_city()>0){
			super.Context().notifyObservers(ObservableEnum.RecaptureCity, template.getUnlock_city());
		}
		super.Context().notifyObservers(ObservableEnum.FightMission, missionId,clearanceCombat,"");
	}
	//获取副本的章节数
	public int getChapterByMissionId(int missionId){
		return missionId/100;
	}
	//不包含扩展的世界地图
	public int getOpenCity(){
		return Math.min(getChapterByMissionId(this.missionId)-1,GameConstants.MaxWorldCityId);
	}
	//包含扩展的世界地图
	public int getRelOpenCity(){
		return getChapterByMissionId(this.missionId)-1;
	}
	//是否可以攻打匪徒关卡
	public boolean isCanFightBandits(int missionId){
		MissionConfig missionConfig = SpringUtil.getBean(MissionConfig.class);
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		if(template==null){
			return false;
		}
		if(getRelOpenCity()<template.getCity()){//城市还未开放
			return false;
		}
		return true;
	}

	public void setMissionId(int missionId) {
		this.missionId = missionId;
		SetChanged();
	}
	//城市是否已经解锁
	public boolean isUnlockCity(int cityId){
		return getOpenCity()>=cityId;
	}


	//是否已领取奖励
	public boolean isReceiveGuide(int id){
		return receiveGuideIds.contains(id);
	}
	public void receiveCityGuide(int id) {
		if(!isReceiveGuide(id)){
			this.receiveGuideIds.add(id);
			SetChanged();
		}
	}
	//gm命令使用方法
	public void checkArmory(int cityId){
//		if(!this.armoryMap.containsKey(cityId)){
//			this.armoryMap.put(cityId, 1);
//			SetChanged();
//		}
	}
	//同步章节信息
	public void syncGuideInfo(int cityId) {
		this.guideId = cityId;
		SetChanged();
	}



	public void setDeathWave(int deathWave) {
		this.deathWave = deathWave;
		SetChanged();
	}

	public boolean isOnHookFight() {
		return this.deathWave >= 0;
	}

	public void addGveSpeedTime(long addSecond) {
		this.gveSpeedTime = Math.max(System.currentTimeMillis(),this.gveSpeedTime)
				+ addSecond*GameConstants.SECOND;
		SetChanged();
	}
}
