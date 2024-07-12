package com.hm.war.sg.skilltrigger;

import cn.hutool.core.collection.CollUtil;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 普攻目标一列的敌军数据量触发
 * @author siyunlong  
 * @date 2018年10月9日 下午7:48:59 
 * @version V1.0
 */
public class NormalAtkColDefCountTrigger extends BaseTriggerSkill{
	private int count;

	public NormalAtkColDefCountTrigger() {
		super(SkillTriggerType.NormalAtkColDefCountTrigger);
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		List<Unit> defList = atk.getAtkEngine().getCurTrueAtkTargets();
		if(CollUtil.isEmpty(defList)) {
			return false;
		}
		Unit defUnit = defList.get(0);
		List<Unit> tempList = ChoiceUtils.listUnitsByIndexs(ChoiceUtils.getColsByIndex(defUnit.getIndex()), defUnit.getMyGroup().getLifeUnit());
		return tempList.size() == this.count;
	}
	
	public void init(String parms,int lv) {
		this.count = Integer.parseInt(parms);
	}
}
