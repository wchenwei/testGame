package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.BaseBuffer;
import com.hm.war.sg.buff.UnitBufferType;
import com.hm.war.sg.event.CleanEvent;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;
/**
 * @Description: 清除指定类型的buff
 * @author siyunlong  
 * @date 2018年10月12日 下午3:12:04 
 * @version V1.0
 */
public class ClearMarkBuffEffect extends BaseSkillEffect{
	
	public ClearMarkBuffEffect() {
		super(SkillEffectType.ClearBuff);
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		int funcId = skillFunction.getId();
		int sendId = unit.getId();
		unitList.forEach(def -> {
			List<BaseBuffer> removeList = def.getUnitBuffs().removeSendBuff(UnitBufferType.MarkBuff,sendId);
			List<Integer> removeFuncIds = removeList.stream()
					.map(e -> e.getFuncId()).collect(Collectors.toList());
			frame.addEvent(new CleanEvent(def.getId(), funcId, removeFuncIds));
			}
		);
	}
	
}