/**  
 * Project Name:SLG_GameHot.  
 * File Name:tank.java  
 * Package Name:com.hm.model.tank  
 * Date:2018年3月5日下午2:20:31  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.model.tank;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.DriverTemplate;
import com.hm.enums.*;
import com.hm.war.sg.setting.TankSetting;
import com.rits.cloning.Cloner;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
public class Tank{
	private int id; 
	private int lv; // 等级
	private int reLv; //突破等级
	private long exp; //累计经验 
	private int star; //星级
	private int starNode;// 星级节点
	private long combat; //战力

	private Map<Integer, Integer> skillMap = Maps.newHashMap(); //坦克技能<id,lv>
	private Map<Integer, Double> totalAttrMap = Maps.newHashMap(); //坦克总属性
	private int state; //状态TankState
	private long curHp = -1;//当前血量 -1表示区当前最大血量
	private Driver driver;
	private int[] friendStars = new int[3];//坦克羁绊等级
	private int fetters = 0;//坦克羁绊星级
	private TankTech tankTech = new TankTech();
	private int evolveStar;	//进化星级，高级星级
	private TankSpecial tankSpecial = new TankSpecial();	//专精
	private TankPassenger tankPassenger = new TankPassenger();
	//private String passengers[] = new String[]{"","","","","",""}; //乘员
	private TankMagicReform tankMagicReform = new TankMagicReform();//魔改
	private TankStrength tankStrength = new TankStrength();//强化
	private TankSoldier tankSoldier;//奇兵
	private int mainTank;//绑定的主战坦克(本坦克作为奇兵绑定在mainTank上)
	@Transient
	private transient long mp;
	
	private TankJingzhu tankJingzhu = new TankJingzhu();	//坦克精铸信息
	private TankControl control = new TankControl();//
	
	public Tank(){
		
	}
	public void addStar(){
		this.star++;
		this.starNode = 0;
	}

	public void addStarNode(){
		this.starNode ++;
	}

	public void initSkill(List<Integer> skillList){
		this.skillMap = skillList.stream().collect(Collectors.toMap(x->x,x->0,(key1, key2) -> key2,HashMap::new));
	}

	public void skillLvUp(int skillId,int lv){
		this.skillMap.replace(skillId, lv);
	}
	//获取解锁的技能id，按等级从小到大排列
	public List<Integer> getUnlockSkillIds(){
		return skillMap.entrySet().stream().filter(entry ->entry.getValue()>0)
		        .sorted(Map.Entry.comparingByValue()).map(entry ->entry.getKey())
		        .collect(Collectors.toList());
	}
	public int getSkillLv(int skillId){
		return this.skillMap.getOrDefault(skillId,0);
	}
	public boolean isSkillUnLock(int skillId){
		return getSkillLv(skillId)>0;
	}

	
	public void reform(){
		this.reLv++;
	}
	public double getTotalAttr(TankAttrType attrType){
		return totalAttrMap.getOrDefault(attrType.getType(), 0d);
	}
	
	public void setState(TankStateType state) {
		this.state = state.getType();
	}
	public int getOil(){
        return TankOilUtils.getTankOil(this.combat);
	}
	
	public void initDriver(DriverTemplate temp) {
		this.driver = new Driver(temp.getId());
	}

	public int getSkillMaxLv(){
		return skillMap.values().stream().max(Integer::compare).get();
	}


	public int getFriendStar(int index){
		return friendStars[index-1];
	}
	public void addFriendStars(int index) {
		this.friendStars[index-1]++; 
	}

	public TankVo createTankVo(){
		return new TankVo(this);
	}
	
	public Tank clone(){
		Tank clone = Cloner.standard().deepClone(this);
		clone.setCurHp((int)getTotalAttr(TankAttrType.HP));
		return clone;
	}
	public void addEvolveStar() {
		this.evolveStar++; 
	}
	
	public void resetFriendStars() {
		friendStars = new int[3];
	}
	public void addFetters() {
		this.fetters+=1;
	}
	
	public String getPassengerUid(int index){
		return tankPassenger.getpassengers()[index-1];
	}
	
	public void addPassenger(String passengerUid,int pos){
		tankPassenger.getpassengers()[pos-1] = passengerUid; 
	}
	
	public void delPassenger(int pos){
		tankPassenger.getpassengers()[pos-1] = ""; 
	}
	
	public void calSuitSkills(List<Integer> skillIds){
		tankPassenger.setSkillIds(skillIds); 
	}
	
	public double getMaxHp(){
		return getTotalAttr(TankAttrType.HP);
	}
	public void magicReform(int skill) {
		if(skill>0){//说明是普通技能
			this.tankMagicReform.reform(skill, MagicSkillType.Normal);
			return;
		}
		//魔改大招
		//从tanksetting中获取坦克的魔改技能
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		TankSetting tankSetting = tankConfig.getTankSetting(this.id);
		int bigSkillId = tankSetting.getMagic_reform_bigskill();
		int superBigSkillId = tankSetting.getSuper_reform_bigskill();
		int magicLv = this.tankMagicReform.getReformLv();
		//一阶魔改等级上限
		int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
		boolean flag = magicLv<reformLvLimit;//是否是一阶大招
		this.tankMagicReform.reform(flag?bigSkillId:superBigSkillId,flag?MagicSkillType.BigSkill:MagicSkillType.SuperBigSkill);
		return;

	}
	public void strength(int id) {
		tankStrength.strength(id);
	}
	public void bindMainTank(int tankId) {
		this.mainTank = tankId;
	}
	public void bindTank(int bindId) {
		if(this.tankSoldier==null) {
			this.tankSoldier = new TankSoldier();
		}
		this.tankSoldier.bindTank(bindId);
	}

	public void lvUp(){
		this.lv ++;
	}
	//获取坦克某个模块的养成等级
	public int getLv(TankSoldierHalosType type) {
		switch(type){
		case EvolveCircle:
			return this.getEvolveStar();
		case masteryCircle:
			return this.getTankSpecial().getLv();
		case MagicCircle:
			return this.getTankMagicReform().getReformLv();
		}
		return 0;
	}
	public void unBindMainTank() {
		this.mainTank = 0;
	}
	
	/**
	 * 获取奇兵的坦克id 如果没有返回0
	 * @return
	 */
	public int getSSSStrangeTankId() {
		if(tankSoldier==null) {
			return 0;
		}
		return tankSoldier.getSubTankId();
	}

	public TankControl getTankControl() {
		return control;
	}

	public void elementLvUp(int index) {
		this.control.getPos(index).lvUp();
	}
}
  
