package com.hm.war.sg.effect;

import com.hm.config.excel.TankSkillConfig;
import com.hm.war.sg.Frame;
import com.hm.war.sg.SettingManager;
import com.hm.war.sg.event.AddExtraSkillEvent;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 添加额外技能
 * @author siyunlong  
 * @date 2018年12月22日 下午2:51:08 
 * @version V1.0
 */
public class AddExtraSkillEffect extends BaseSkillEffect{

	public AddExtraSkillEffect() {
		super(SkillEffectType.AddExtraSkill);
	}
	
	@Override
	public void doEffect(Frame frame, Unit atk, List<Unit> unitList, Skill skill, SkillFunction skillFunction){
		int skillId = getSkillId(atk, atk, skill, skillFunction);
		TankSkillConfig tankSkillConfig = SettingManager.getInstance().getTankSkillConfig();
		SkillSetting skillSetting = tankSkillConfig.getSkillSetting(skillId);
		long endSkillFrame = frame.getId() + skillFunction.getContinuedFrame();
		if(skillSetting == null) {
			return;
		}
		for (int i = 0; i < unitList.size(); i++) {
			Unit def = unitList.get(i);
			Skill newSkill = new Skill(skill.getLv(), skillSetting);
			newSkill.setEndSkillFrame(endSkillFrame);
			def.getUnitSkills().addSkillForRun(newSkill);
			//添加事件
			frame.addEvent(new AddExtraSkillEvent(def.getId(), skillId, skillFunction.getContinuedFrame(), skillFunction.getId()));
		}
	}
	
	private int getSkillId(Unit atk,Unit def,Skill skill,SkillFunction skillFunction) {
		return (int)skillFunction.getFunctionValue(atk,def,skill);
	}
}
