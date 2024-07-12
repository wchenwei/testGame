package com.hm.war.sg.effect;

import com.hm.war.sg.Frame;
import com.hm.war.sg.buff.BaseBuffer;
import com.hm.war.sg.buff.BuffKind;
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
public class ClearBuffEffect extends BaseSkillEffect{
	private BuffKind buffKind;
	
	public ClearBuffEffect(BuffKind buffKind) {
		super(SkillEffectType.ClearBuff);
		this.buffKind = buffKind;
	}

	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		int funcId = skillFunction.getId();
		unitList.forEach(def -> {
			List<BaseBuffer> removeList = def.getUnitBuffs().removeBufferByKind(buffKind);
//			List<Integer> removeFuncIds = removeList.stream()
//					.mapToInt(e -> e.getFuncId()).boxed().collect(Collectors.toList());
//			frame.addEvent(new CleanEvent(def.getId(), funcId, removeFuncIds));
			clearBuff(frame,funcId,def,removeList);
		}
		);
	}

	public static void clearBuff(Frame frame,int funcId,Unit def,List<BaseBuffer> removeList) {
		List<Integer> removeFuncIds = removeList.stream()
				.mapToInt(e -> e.getFuncId()).boxed().collect(Collectors.toList());
		frame.addEvent(new CleanEvent(def.getId(), funcId, removeFuncIds));
	}

}