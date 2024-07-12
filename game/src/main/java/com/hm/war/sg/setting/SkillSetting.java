package com.hm.war.sg.setting;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.temlate.SkillBaseTemplate;
import com.hm.war.sg.skillchoice.BaseSkillChoice;
import com.hm.war.sg.skillchoice.SkillChoiceType;
import com.hm.war.sg.skillnew.SkillType;
import com.hm.war.sg.skilltrigger.BaseTriggerSkill;
import com.hm.war.sg.skilltrigger.SkillTriggerType;

import java.util.List;

@FileConfig("skill_base")
public class SkillSetting extends SkillBaseTemplate{
	private SkillType skillType;//技能类型
	private BaseSkillChoice skillChoice;//目标选择
	
	private List<FunctionSetting> functionSettingList = Lists.newArrayList();
	
	public void init(TankSkillConfig config) {
		this.skillType = SkillType.getSkillType(getSkill_type());
		
		for (int functionId : StringUtil.splitStr2IntegerList(getSkill_function(), ",")) {
			FunctionSetting fs = config.getFunctionSetting(functionId);
			if(fs != null) {
				functionSettingList.add(fs);
			}else{
				System.err.println("FunctionSetting未找到!:"+functionId);
			}
		}
		initSkillChoice();
		createTriggerCondition(1);//测试
	}
	
	public void initSkillChoice() {
		String skillTarget = getSkill_target();
		String[] infos = skillTarget.split("#");
		this.skillChoice = SkillChoiceType.getType(Integer.parseInt(infos[0])).buildChoice();
		if(infos.length>1) {
			this.skillChoice.loadParm(infos[1]);
		}
	}

	public SkillType getSkillType() {
		return skillType;
	}
	
	public BaseSkillChoice getSkillChoice() {
		return skillChoice;
	}

	public List<FunctionSetting> getFunctionSettingList() {
		return functionSettingList;
	}
	
	/**
	 * 生成触发条件
	 * @param lv
	 * @return
	 */
	public List<BaseTriggerSkill> createTriggerCondition(int lv) {
		List<BaseTriggerSkill> triggerList = Lists.newArrayList();
		for (String triggerStr : getSkill_trigger().split(",")) {
			int type = Integer.parseInt(triggerStr.split("#")[0]);
			SkillTriggerType skillTriggerType = SkillTriggerType.getType(type);
			if(skillTriggerType == null) {
				System.out.println("triggerStr type:"+type+" is null skillId:"+getId());
				continue;
			}
			BaseTriggerSkill trigger = skillTriggerType.buildTriggerCon();
			if(trigger.getType() != SkillTriggerType.None) {
				String parms = triggerStr.split("#")[1];
				trigger.init(parms, lv);
			}
			triggerList.add(trigger);
		}
		return triggerList;
	}
}
