package com.hm.war.sg.setting;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.SkillFunctionTemplate;
import com.hm.war.sg.buff.BuffKind;
import com.hm.war.sg.skillchoice.BaseSkillChoice;
import com.hm.war.sg.skillchoice.SkillChoiceType;
import com.hm.war.sg.skillnew.ParmValue;
import com.hm.war.sg.skilltrigger.BaseTriggerSkill;
import com.hm.war.sg.skilltrigger.SkillTriggerType;

import java.util.List;

@FileConfig("skill_function")
public class FunctionSetting extends SkillFunctionTemplate{
	private List<ParmValue> parmList = Lists.newArrayList();
	private BaseSkillChoice skillChoice;//目标选择
	private BuffKind buffKind;
	
	public void init() {
		for (String p : getFunction_value().split("#")) {
			parmList.add(new ParmValue(p));
		}
		initSkillChoice();
		this.buffKind = BuffKind.getKind(getFunction_type());
		createBuffTriggerCondition();
		createTriggerCondition(1);
	}
	
	public void initSkillChoice() {
		String skillTarget = getFunction_target();
		if(StrUtil.isNotBlank(skillTarget)) {
			String[] infos = skillTarget.split("#");
            SkillChoiceType skillChoiceType = SkillChoiceType.getType(Integer.parseInt(infos[0]));
            if (skillChoiceType == null) {
                System.out.println(getId() + "skillChoiceType is null:" + Integer.parseInt(infos[0]));
            }
            this.skillChoice = skillChoiceType.buildChoice();
			if(infos.length>1) {
				this.skillChoice.loadParm(infos[1]);
			}
		}
	}
	
	/**
	 * 生成触发条件
	 * @param lv
	 * @return
	 */
	public BaseTriggerSkill createTriggerCondition(int lv) {
		return createBaseTriggerSkill(getFunction_trigger(),lv);
	}
	
	public BaseTriggerSkill createBuffTriggerCondition() {
		return createBaseTriggerSkill(getBuff_trigger(),1);
	}
	
	private BaseTriggerSkill createBaseTriggerSkill(String triggerStr,int lv) {
		if(StrUtil.isEmpty(triggerStr.trim())) {
			return null;
		}
		int type = Integer.parseInt(triggerStr.split("#")[0]);
		BaseTriggerSkill trigger = SkillTriggerType.getType(type).buildTriggerCon();
		if(trigger.getType() != SkillTriggerType.None) {
			String[] parmaArrays = triggerStr.split("#");
			if(parmaArrays.length < 2) {
				trigger.init(null, lv);
				return trigger;
			}
			trigger.init(parmaArrays[1], lv);
			return trigger;
		}
		return null;
	}

	public List<ParmValue> getParmList() {
		return parmList;
	}

	public BaseSkillChoice getSkillChoice() {
		return skillChoice;
	}

	public BuffKind getBuffKind() {
		return buffKind;
	}
	
	
}
