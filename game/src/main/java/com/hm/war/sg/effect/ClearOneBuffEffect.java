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
public class ClearOneBuffEffect extends BaseSkillEffect{
	private UnitBufferType buffType;
	private boolean isFuncVal;//是否按照fuc配置的层数删除
	
	public ClearOneBuffEffect(UnitBufferType buffType) {
		super(SkillEffectType.ClearBuff);
		this.buffType = buffType;
	}

	public ClearOneBuffEffect(UnitBufferType buffType, boolean isFuncVal) {
		super(SkillEffectType.ClearBuff);
		this.buffType = buffType;
		this.isFuncVal = isFuncVal;
	}



	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList, Skill skill, SkillFunction skillFunction) {
		int funcId = skillFunction.getId();
		unitList.forEach(def -> {
					List<BaseBuffer> removeList = removeBuff(unit, def, skill, skillFunction);
			List<Integer> removeFuncIds = removeList.stream()
                    .map(e -> e.getFuncId()).collect(Collectors.toList());
			frame.addEvent(new CleanEvent(def.getId(), funcId, removeFuncIds));
			
			for (BaseBuffer buffer : removeList) {
				buffer.doRemoveAction(frame, def);//删除时触发
			}
		}
		);
	}

	public List<BaseBuffer> removeBuff(Unit atk, Unit def, Skill skill, SkillFunction skillFunction) {
		if (!isFuncVal) {
			return def.getUnitBuffs().removeBuffer(buffType);
		}
		int delNum = (int) skillFunction.getFunctionValue(atk, def, skill);
		return def.getUnitBuffs().removeBuffer(buffType, delNum);
	}
}