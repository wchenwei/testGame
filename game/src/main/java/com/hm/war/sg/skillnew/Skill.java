package com.hm.war.sg.skillnew;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.war.sg.Frame;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.event.SkillReleaseEvent;
import com.hm.war.sg.setting.FunctionSetting;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.skillchoice.BaseSkillChoice;
import com.hm.war.sg.skilltrigger.BaseTriggerSkill;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Skill {
	private int id;
	private int lv;
	private long lastFrame;//上次出手时间
	
	private SkillType skillType;//技能类型
	private long cd;//技能cd
	private long preFrame;//前摇时间
	private long flyFrame;//技能飞行时间
	private long triggerNum;//触发次数
	private long maxTriggerNum;//最大触发次数
	private List<BaseTriggerSkill> skillTriggerConditions;
	private BaseSkillChoice skillChoice;//目标选择
	private List<SkillFunction> functionList = Lists.newArrayList();//功能效果列表
	private long endSkillFrame = -1;//技能失效截止帧  -1为永久
	
	public Skill(int lv, SkillSetting setting) {
		this.lv = lv;
		this.id = setting.getId();
		this.skillType = setting.getSkillType();
		this.cd = setting.getSkill_cool();
		this.preFrame = setting.getCast_time();
		this.flyFrame = setting.getFly_time();
		this.skillChoice = setting.getSkillChoice();
		this.maxTriggerNum = setting.getFrequency();
		reloadSkillForLv(setting);
	}
	public void reloadSkillForLv(SkillSetting setting) {
		List<FunctionSetting> fstList = setting.getFunctionSettingList();
		this.functionList = fstList.stream().map(e -> new SkillFunction(lv,e)).collect(Collectors.toList());
		this.functionList.forEach(e -> e.setSkill(this));
		this.skillTriggerConditions = setting.createTriggerCondition(lv);
	}
	
	public boolean checkCanSkill(long curFrame) {
		//判断触发次数
		if(maxTriggerNum > 0 && this.triggerNum >= maxTriggerNum) {
			return false;
		}
		return curFrame >= getCd()+lastFrame;
	}
	
	public void addTriggerNum() {
		this.triggerNum ++;
	}
	
	public boolean isCanTrigger(Frame frame, Unit atk, Unit def) {
		//判断触发次数
		if(frame != null && !checkCanSkill(frame.getId())) {
			return false;
		}
		for (BaseTriggerSkill triggerConditions : skillTriggerConditions) {
			if(!triggerConditions.isCanTriggerFrame(frame,atk, def)) {
				return false;
			}
		}
		if(frame != null) {
			setLastFrame(frame.getId());
		}
		return true;
	}
	
	
	/**
	 * 主动释放技能
	 * @param frame
	 * @param atk
	 * @param atkGroup
	 * @param defGroup
	 */
	public void doActiveSkill(Frame frame, Unit atk, UnitGroup atkGroup,UnitGroup defGroup) {
		//======================释放技能=====================================
		List<Unit> unitList = skillChoice.choiceTargets(atk, atkGroup, defGroup);
		addTriggerNum();//增加触发次数
		if(unitList.isEmpty()) {
			return;
		}
		List<Integer> targets = unitList.stream().map(e -> e.getId()).collect(Collectors.toList());
		frame.addEvent(new SkillReleaseEvent(atk, this.id, targets,flyFrame));
		//处理效果
		functionList.forEach(func -> {
			BaseSkillChoice funcChoice = func.getSkillChoice();
 			if(funcChoice != null) {//重定向技能目标
				func.doEffect(frame, atk, funcChoice.choiceTargets(atk, atkGroup, defGroup),this);
			}else{
				func.doEffect(frame, atk, unitList,this);
			}
		});
	}


	public void doActiveSkillForTargetList(Frame frame, Unit atk, UnitGroup atkGroup, UnitGroup defGroup, List<Unit> unitList) {
		addTriggerNum();//增加触发次数
		List<Integer> targets = unitList.stream().map(e -> e.getId()).collect(Collectors.toList());
		frame.addEvent(new SkillReleaseEvent(atk, this.id, targets,flyFrame));
		//处理效果
		functionList.forEach(func -> {
			func.doEffect(frame, atk, unitList,this);
		});
	}
	
	/**
	 * 找出此技能是否适合队友玩家
	 * @param atk
	 * @param belong
	 * @param atkGroup
	 * @param defGroup
	 * @return
	 */
	public boolean isFitUnit(Unit friend, Unit belong, UnitGroup atkGroup,UnitGroup defGroup) {
		if(belong.getId() == friend.getId()) {
			return true;
		}
		List<Unit> unitList = skillChoice.choiceTargets(belong, atkGroup, defGroup);
		return unitList.stream().filter(e -> e.getId() == friend.getId()).findFirst().orElse(null) != null;
	}
	
	/**
	 * 触发攻击被动技能
	 * @param frame
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doPassiveAtkSkill(Frame frame,Unit atk, Unit def, HurtBear hurtBear) {
		if(isCanTrigger(frame,atk, def)) {
			addTriggerNum();//增加触发次数
			functionList.forEach(func -> func.doPassiveAtkEffect(frame, atk, def, hurtBear,this));
		}
	}
	
	/**
	 * 触发防御被动技能
	 * @param frame
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doPassiveDefSkill(Frame frame,Unit atk, Unit def, HurtBear hurtBear) {
		if(isCanTrigger(frame,def, atk)) {
			addTriggerNum();//增加触发次数
			List<Unit> unitList = skillChoice.choiceTargets(def, def.getMyGroup(), def.getDefGroup());
			if(CollUtil.isEmpty(unitList)) {
				return;
			}
			functionList.forEach(func -> func.doPassiveDefEffect(frame, def, unitList, hurtBear,this));
		}
	}
	
	/**
	 * 触发属性技能
	 * @param frame
	 * @param atk
	 * @param def
	 * @param hurtBear
	 */
	public void doAttrSkill(Unit atk, UnitAttr unitAttr) {
		if(isCanTrigger(null,atk, atk)) {
			addTriggerNum();//增加触发次数
			functionList.forEach(func -> func.doAttrSkill(atk,unitAttr, this));
		}
	}
	
	/**
	 * 对特定的人释放技能
	 * @param frame
	 * @param unit
	 * @param def
	 */
	public void doReleaseSkillForUnit(Frame frame,Unit unit,Unit def) {
		//释放技能
		setLastFrame(frame.getId());
		frame.addEvent(new SkillReleaseEvent(unit, getId(), Lists.newArrayList(def.getId()),getFlyFrame()));
		addTriggerNum();//增加触发次数
		//复活
		functionList.forEach(func -> {
			BaseSkillChoice funcChoice = func.getSkillChoice();
 			if(funcChoice != null) {//重定向技能目标
				func.doEffect(frame, unit, funcChoice.choiceTargets(unit, unit.getMyGroup(), unit.getDefGroup()),this);
			}else{
				func.doEffect(frame, unit, Lists.newArrayList(def),this);
			}
		});
	}
}
