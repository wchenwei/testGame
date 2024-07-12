package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;



/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家指挥官
 * @date 2024/4/12 14:20
 */
public class PlayerCommander extends PlayerDataContext{
	private int militaryLv = 0; //军衔等级，10个阶段升一级，初始1级
	private int militaryClickCount; //当前等级点击次数，升级清零


	private int carLv = 0; //座驾等级，20个阶段升一级,初始1级
	private int carClickCount; //当前等级点击次数，升级清零

	private int superWeaponLv; //超武星级,0级未购买，购买之后默认1级
	private int superWeaponUpgrade;//超武等级

	private PlayerMilitaryProject militaryProject = new PlayerMilitaryProject();
	
	public int getMilitaryLv() {
		return Math.max(1,militaryLv);
	}
	public int getCarLv() {
		return carLv;
	}

	public void addCarLv(){
		this.carLv++; 
		this.carClickCount = 0; 
		SetChanged();
	}

	public void addMilitaryLv(){
		this.militaryLv ++; 
		this.militaryClickCount = 0; 
		SetChanged();
	}

	public void setMilitaryLv(int militaryLv) {
		this.militaryLv = militaryLv;
		SetChanged();
	}

	public void setCarLv(int carLv) {
		this.carLv = carLv;
		SetChanged();
	}

	public int getMilitaryClickCount() {
		return militaryClickCount;
	}


	public int getCarClickCount() {
		return carClickCount;
	}

	public void addCarClickCount() {
		this.carClickCount++;
		SetChanged();
	}
	
	public void addMilitaryClickCount(){
		this.militaryClickCount++; 
		SetChanged();
	}


	public int getSuperWeaponLv() {
		return superWeaponLv;
	}
	
	public int getSuperWeaponUpgrade() {
		return superWeaponUpgrade;
	}


	public void addSuperWeaponLv() {
		this.superWeaponLv ++; 
		SetChanged();
	}
	
	public void addSuperWeaponUpgrade() {
		this.superWeaponUpgrade ++; 
		SetChanged();
	}

	public void buySuperWeapon(){
		if(this.superWeaponLv >= 1) {
			return;
		}
		this.superWeaponLv = 1; 
		SetChanged(); 
	}
	
	public PlayerMilitaryProject getMilitaryProject() {
		return militaryProject;
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerCommander", this);
	}
}