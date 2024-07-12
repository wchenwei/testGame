package com.hm.war.sg.skilltrigger;

import com.hm.war.sg.Frame;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 场上存活的tank数量
 * @author siyunlong  
 * @date 2020年12月16日 下午4:33:34 
 * @version V1.0
 */
public class LifeUnitTrigger extends BaseTriggerSkill{
	private int num;
	
	private boolean isGte;//是否>=
	private boolean isAtk;
	private boolean containSelf;

	public LifeUnitTrigger(SkillTriggerType type,boolean isAtk,boolean isGte,boolean containSelf) {
		super(type);
		this.isGte = isGte;
		this.isAtk = isAtk;
		this.containSelf = containSelf;
	}
	
	@Override
	public boolean isCanTriggerFrame(Frame frame, Unit atk, Unit def, Object...args) {
		List<Unit> unitList = isAtk?atk.getMyGroup().getUnits():atk.getDefGroup().getUnits();
		long lifeNum = 0;
		if(isAtk && !containSelf) {
			lifeNum = unitList.stream().filter(e -> atk.getId() != e.getId()).filter(e -> !e.isDeath())
					.count();
		}else{
			lifeNum = unitList.stream().filter(e -> !e.isDeath()).count();
		}
		if(isGte) {
			return lifeNum >= num;
		}else{
			return lifeNum < num;
		}
	}
	
	public void init(String parms,int lv) {
		this.num = Integer.parseInt(parms);
	}
}
