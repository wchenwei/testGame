package com.hm.war.sg.effect;

import com.hm.enums.TankAttrType;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.AtkAddType;
import com.hm.war.sg.bear.AttrBear;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillFunction;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * 普攻时给自己额外增加某个属性
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author siyunlong  
 * @date 2019年9月6日 下午2:03:16 
 * @version V1.0
 */
public class AddAttrBufferForNormalAtkEffect extends BaseAttrBufferEffect{
	private int atkAddType;//普攻某属性
	private boolean isHave;//有某属性触发/没有某属性触发
	
	public AddAttrBufferForNormalAtkEffect(TankAttrType attrType,AtkAddType atkAddType,boolean isHave) {
		super(attrType);
		this.atkAddType = atkAddType.getType();
		this.isHave = isHave;
	}
	
	/**
	 * 攻击时触发
	 */
	@Override
	public void doAtkHurtBear(Frame frame, Unit atk, Unit def, HurtBear hurtBear, Skill skill, SkillFunction skillFunction){
		boolean haveType = hurtBear.getAddType() == atkAddType;
		if(haveType != isHave) {
			return;
		}
		//只给自己触发
		double addAttr = getAttrValue(atk,def,skill, skillFunction);
		long effectFrame = hurtBear.getEffectFrame();
		atk.addBear(frame,new AttrBear(atk.getId(), effectFrame, attrType, addAttr, skillFunction));
	}
	
	//不实现
	@Override
	public void doEffect(Frame frame, Unit unit, List<Unit> unitList,Skill skill, SkillFunction skillFunction) {
		
	}
	//不实现
	public void doDefHurtBear(Frame frame, Unit unit,List<Unit> unitList,HurtBear hurtBear,Skill skill, SkillFunction skillFunction) {
		
	}
	
}
