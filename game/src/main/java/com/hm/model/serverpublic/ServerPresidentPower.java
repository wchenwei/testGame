package com.hm.model.serverpublic;

import cn.hutool.core.date.DateUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.model.serverpublic.serverpower.CenterCity;
import com.hm.model.serverpublic.serverpower.MilitaryPolicy;
import com.hm.model.serverpublic.serverpower.Punish;

import java.util.Date;
import java.util.List;


public class ServerPresidentPower extends ServerPublicContext {
	//中心城市
	private CenterCity centerCity;
	//军事政策
	private MilitaryPolicy militaryPolicy;
	//制裁
	private Punish punish;
	
	public MilitaryPolicy getMilitaryPolicy(){
		return militaryPolicy;
	}
	
	public Punish getPunish() {
		return punish;
	}

	//是否中心城市正在生效中
	public boolean haveCenterCity() {
		if(centerCity==null){
			return false;
		}
		return centerCity.getEndTime()>System.currentTimeMillis();
	}
	//设置中心城
	public void setCenterCity(List<Integer> cityIds){
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int hour = commValueConfig.getCommValue(CommonValueType.PresidentPower_CenterCity_Time);
		//距离24点不足3个小时则结束时间按24点算
		this.centerCity = new CenterCity(cityIds,Math.min(System.currentTimeMillis()+hour*GameConstants.HOUR,DateUtil.endOfDay(new Date()).getTime()));
		SetChanged();
	}
	//是否军事政策正在生效中
	public boolean haveMilitaryPolicy() {
		if(militaryPolicy==null){
			return false;
		}
		return militaryPolicy.getEndTime()>System.currentTimeMillis();
	}
	
	//颁布军事政策
	public void promulgateMilitaryPolicy(long expLimit){
		if(militaryPolicy==null){
			militaryPolicy = new MilitaryPolicy();
		}
		militaryPolicy.military(expLimit);
		SetChanged();
	}
	
	//是否可以制裁
	public boolean isCanPunish(){
		if(punish==null){
			return true;
		}
		return !punish.getPunishs().values().stream().anyMatch(t ->t>=System.currentTimeMillis());
	}
	
	public void punish(int id) {
		if(punish==null){
			punish = new Punish();
		}
		punish.addPunish(id);
		SetChanged();
	}
	public void clearPunish() {
		if(this.punish != null){
			this.punish.clearPunish();
			SetChanged();
		};
	}
	//是否是中心城
	public boolean isCenterCity(int cityId){
		if(centerCity==null||!centerCity.isInTime()){
			return false;
		}
		return centerCity.getCenterCityIds().contains(cityId);
	}
	
	public long getExpLimit(){
		if(militaryPolicy==null||!militaryPolicy.isInTime()){
			return 0;
		}
		return militaryPolicy.getExpLimit();
	}
	
	public void clearCenterCity(){
		if(this.centerCity==null){
			return;
		}
		this.centerCity.clearData();
		SetChanged();
	}
	
	public void clearData() {
		if(this.centerCity!=null)this.centerCity.clearData();
		if(this.militaryPolicy!=null) this.militaryPolicy.clearData();
		if(this.punish!=null) this.punish.clearPunish();
		SetChanged();
	}
	public boolean isBePunished(long playerId) {
		if(this.punish==null){
			return false;
		}
		return punish.isBePunished(playerId);
	}
}
