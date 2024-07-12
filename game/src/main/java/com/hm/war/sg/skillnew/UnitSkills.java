package com.hm.war.sg.skillnew;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.excel.TankSkillConfig;
import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.buff.SkillPreBuff;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.SkillPreEvent;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.skillchoice.BaseSkillChoice;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UnitSkills {
	//技能列表
	protected Map<Integer,Skill> skillMap = Maps.newConcurrentMap();
	protected ListMultimap<SkillType, Skill> typeSkillMap = ArrayListMultimap.create();
	private transient Unit unit;
	
	public UnitSkills(Unit unit) {
		this.unit = unit;
	}
	public UnitSkills() {}
	
	//检查临时技能
	public void checkExtraSkill(Frame frame) {
		for (Map.Entry<Integer,Skill> entry : skillMap.entrySet()) {
			long endFrame = entry.getValue().getEndSkillFrame();
			if(endFrame > 0 && endFrame < frame.getId()) {
				removeSkill(entry.getKey());
			}
		}
	}
	
	public void addSkill(Skill skill) {
		this.skillMap.put(skill.getId(), skill);
		this.typeSkillMap.put(skill.getSkillType(), skill);
	}
	public void addSkillForRun(Skill skill) {
		this.skillMap.put(skill.getId(), skill);

		List<Skill> skillList = Lists.newArrayList(this.typeSkillMap.get(skill.getSkillType()));
		skillList.add(skill);
		this.typeSkillMap.removeAll(skill.getSkillType());
		this.typeSkillMap.putAll(skill.getSkillType(),skillList);
	}

	public void addSkill(List<Skill> skillList) {
		skillList.stream().forEach(e -> addSkill(e));
	}
	
	public void buildSkill(TankSkillConfig tankSkillConfig,Map<Integer, Integer> tempMap) {
		for (Map.Entry<Integer, Integer> entry : tempMap.entrySet()) {
			int skillId = entry.getKey();int skillLv= entry.getValue();
			if(skillLv > 0) {
				SkillSetting skillSetting = tankSkillConfig.getSkillSetting(skillId);
				if(skillSetting != null) {
					addSkill(new Skill(skillLv, skillSetting));
				}else{
					System.out.println("战斗技能不存在:"+skillId);
				}
			}
		}
	}
	
	/**
	 * 释放主动技能
	 * @param frame
	 * @param atk
	 * @param myGroup
	 * @param defGroup
	 * @param skillId
	 */
	public void doActiveSkill(Frame frame,Unit atk,UnitGroup myGroup,UnitGroup defGroup,int skillId) {
		Skill skill = this.skillMap.get(skillId);
		if(skill != null) {
			skill.doActiveSkill(frame, atk, myGroup, defGroup);
			if(skill.getSkillType() == SkillType.ActiveSkill) {
				//释放大招触发额外技能
				doReleaseSkillForType(frame, SkillType.ReleaseBigSkill);
				//释放大招触发敌方的技能检查
				for (Unit def : defGroup.getLifeUnit()) {
					def.getUnitSkills().doReleaseSkillForType(frame, SkillType.ReleaseBigSkillForDef);
				}
			}
		}
	}
	
	/**
	 * 大招前摇
	 * @param frame
	 * @param atk
	 * @return
	 */
	public boolean doPreActiveSkill(Frame frame,Unit atk) {
		if(!atk.getMpEngine().isMpFull()) {
			return false;
		}
		List<Skill> activeSkills = getSkillListByType(frame,SkillType.ActiveSkill);
		if(CollUtil.isNotEmpty(activeSkills)) {
			Skill skill = activeSkills.get(0);
			int skillId = skill.getId();
			long curFrame = frame.getId();
			atk.getMpEngine().clearMp();//清空蓝量
			
			atk.getUnitBuffs().setSkillPre(new SkillPreBuff(skill.getPreFrame()+curFrame, skillId));
			frame.addEvent(new SkillPreEvent(atk, skillId,skill.getPreFrame()));
			return true;
		}
		return false;
	}
	
	/**
	 * 释放触发的小技能
	 * @return
	 */
	public boolean isPreSmallTriggerSkill(Frame frame,Unit atk) {
		if(atk.getUnitBuffs().haveBuff(UnitBufferType.SmallSkillSilentBuff)) {
			return false;
		}
		List<Skill> smallSkills = getSkillListByType(frame,SkillType.TriggerSkill)
				//过滤出能释放的
				.stream().filter(e -> e.checkCanSkill(frame.getId()))
				.collect(Collectors.toList());
		Collections.shuffle(smallSkills);
		for (Skill skill : smallSkills) {
			if(skill.isCanTrigger(frame,atk, null)) {
				int skillId = skill.getId();
				long curFrame = frame.getId();
				//释放技能
				atk.getUnitBuffs().setSkillPre(new SkillPreBuff(skill.getPreFrame()+curFrame, skillId));
				frame.addEvent(new SkillPreEvent(atk, skillId,skill.getPreFrame()));
				return true;
			}
		}
		return false;
	}
	
	public List<Skill> getSkillListByType(Frame frame,SkillType type) {
		if(!this.unit.getMyGroup().isCanReleaseSkill()) {
			return Lists.newArrayList();//所有技能失效
		}
		return this.typeSkillMap.get(type);
	}

	
	/**
	 * 释放指定类型的技能
	 * @param frame
	 * @param skillType
	 */
	public void doReleaseSkillForType(Frame frame,SkillType skillType) {
		List<Skill> skillList = getSkillListByType(frame,skillType);
		for (int i = skillList.size()-1; i >= 0; i--) {
			Skill skill = skillList.get(i);
			if(skill != null) {
				if(skill.isCanTrigger(frame, this.unit, null)) {
					skill.doActiveSkill(frame, unit, unit.getMyGroup(), unit.getDefGroup());
				}
			}else{
				System.out.println("skill is null:"+unit.getSetting().getTankId());
			}
		}
	}
	
	/**
	 * 删除此坦克的光环技能
	 * @param myGroup
	 * @param defGroup
	 */
	public void doUnitDeath(Frame frame,Unit unit) {
		unit.getMyGroup().getUnits().forEach(e -> e.getUnitBuffs().removeBufferByUnit(frame,unit.getId()));
		unit.getDefGroup().getUnits().forEach(e -> e.getUnitBuffs().removeBufferByUnit(frame,unit.getId()));
		
		//触发死亡技能
		unit.getUnitSkills().doReleaseSkillForType(frame, SkillType.SelfDeathSkill);
	}
	
	/**
	 * 计算血量影响的属性技能 ps:血量低于50% 增加100攻击
	 * @return
	 */
	public UnitAttr calHpUnitAttr() {
		UnitAttr unitAttr = new UnitAttr();
		getSkillListByType(null,SkillType.AttrSkill).forEach(e -> e.doAttrSkill(unit,unitAttr));
		return unitAttr;
	}
	
	/**
	 * 触发复活技能
	 * @return
	 */
	public boolean triggerReviveSkill(Frame frame,Unit deathUnit) {
		if(!this.unit.isCanRealseSkill()) {
			return false;//是否可以释放技能
		}
		Skill skill = getSkillListByType(frame,SkillType.Revive).stream()
				.filter(e -> e.checkCanSkill(frame.getId()))
				.filter(e -> e.isCanTrigger(frame, this.unit, deathUnit))
				.findFirst().orElse(null);
		if(skill != null) {
			BaseSkillChoice skillChoice = skill.getSkillChoice();
			if(skillChoice == null) {
				return false;
			}
			List<Unit> unitList = skillChoice.choiceTargets(this.unit, this.unit.getMyGroup(), this.unit.getDefGroup());
			if(unitList.contains(deathUnit)) {
				skill.doReleaseSkillForUnit(frame, unit, deathUnit);
				//自己死亡复活触发
				deathUnit.getUnitSkills().doReleaseSkillForType(frame, SkillType.DeathLifeSelf);
				return true;
			}
		}
		return false;
	}

    public List<Integer> getAllSkillIds() {
        return Lists.newArrayList(this.skillMap.keySet());
	}
	/**
	 * 检查是否删除技能
	 * @param skillId
	 * @param delSkillId
	 */
	public void checkRepaceSkillAndDel(SkillSetting skillSetting,int delSkillId) {
		int skillId = skillSetting.getId();
		Skill newSkill = this.skillMap.get(skillId);
		Skill delSkill = this.skillMap.get(delSkillId);
		if(newSkill == null || delSkill == null) {
			return;
		}
		removeSkill(delSkillId);
		newSkill.setLv(Math.max(newSkill.getLv(), delSkill.getLv()));
		newSkill.reloadSkillForLv(skillSetting);
	}

	public boolean removeSkill(int skillId) {
		Skill skill = this.skillMap.remove(skillId);
		if (skill != null) {
			List<Skill> skillList = Lists.newArrayList(this.typeSkillMap.get(skill.getSkillType()));
			skillList.remove(skill);

			this.typeSkillMap.removeAll(skill.getSkillType());
			this.typeSkillMap.putAll(skill.getSkillType(),skillList);
			return true;
		}
		return false;
	}
}
