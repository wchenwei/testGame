package com.hm.war.sg.skillnew;

import com.google.common.collect.Lists;
import com.hm.war.sg.Frame;
import com.hm.war.sg.SettingManager;
import com.hm.war.sg.UnitAtkAction;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.buff.BuffKind;
import com.hm.war.sg.cal.BaseCalFormula;
import com.hm.war.sg.cal.CalFormulaType;
import com.hm.war.sg.effect.BaseSkillEffect;
import com.hm.war.sg.effect.EffectBuilderFactory;
import com.hm.war.sg.setting.FunctionSetting;
import com.hm.war.sg.skillchoice.BaseSkillChoice;
import com.hm.war.sg.skilltrigger.BaseTriggerSkill;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitAttr;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SkillFunction {
	private int id;
	private int lv;//当前等级
	private long continuedFrame;//持续帧
	private long effectFrame;//生效帧
	private long maxAttBuffOvers;//最大属性buff叠加次数
	private int intervalFrame = 1;//频率
	private int formulaType;//计算代入类型
	private int delayFrame;//延迟帧
	private BaseCalFormula calFormula;
	private BuffKind buffKind;
	private int delBuffType;//0-不删除  1-释放者死亡后删除
	private String extra_param;
	private int effectId;
	
	private BaseTriggerSkill functionTriggerCondition;//触发条件
	private BaseTriggerSkill buffTriggerCondition;//如果是buff的功能，此buff的生效条件
	private BaseSkillEffect skillEffect;
	private BaseSkillChoice skillChoice;//目标选择
	private List<ParmValue> parmList = Lists.newArrayList();
	private Skill skill;
	
	public SkillFunction(int lv,FunctionSetting setting) {
		this.id = setting.getId();
		this.lv = lv;
		this.buffKind = setting.getBuffKind();
		this.formulaType = setting.getFormula();
		this.functionTriggerCondition = setting.createTriggerCondition(lv);
		this.buffTriggerCondition = setting.createBuffTriggerCondition();
		this.continuedFrame = setting.getContinued_frame();
		this.effectFrame = setting.getFunction_time();
		this.intervalFrame = setting.getFrequency();
		this.maxAttBuffOvers = setting.getBuff_layers();
		this.calFormula = CalFormulaType.getType(setting.getCal_type()).buildFormula();
		this.skillEffect = EffectBuilderFactory.create(setting.getFunction_effect());
		this.effectId = setting.getFunction_effect();
		this.parmList = setting.getParmList();
		this.skillChoice = setting.getSkillChoice();
		this.delayFrame = setting.getDelay();
		this.delBuffType = setting.getCleanbuff();
		this.extra_param = setting.getExtra_param();
	}
	
	public boolean isCanTrigger(Frame frame, Unit atk, Unit def) {
		return !haveTriggerCondition() || functionTriggerCondition.isCanTriggerFrame(frame,atk, def);
	}
	
	public boolean haveTriggerCondition() {
		return functionTriggerCondition != null;
	}
	
	public void doEffect(Frame frame, Unit atk, List<Unit> unitList,Skill skill) {
		//计算过滤之后的技能目标列表
		if(haveTriggerCondition()) {
			List<Unit> defList = unitList.stream().filter(e -> isCanTrigger(frame,atk, e)).collect(Collectors.toList());
			skillEffect.doEffect(frame, atk, defList,skill,this);
		}else{
			skillEffect.doEffect(frame, atk, unitList,skill,this);
		}
	}
	
	public void doPassiveAtkEffect(Frame frame,Unit atk, Unit def, HurtBear hurtBear,Skill skill) {
		if(isCanTrigger(frame,atk, def)) {
			skillEffect.doAtkHurtBear(frame, atk, def, hurtBear, skill, this);
		}
	}
	
	public void doPassiveDefEffect(Frame frame,Unit def, List<Unit> unitList, HurtBear hurtBear,Skill skill) {
		if (skillChoice != null) {//如果function技能选择目标有配置 就按照function的目标选择
			unitList = skillChoice.choiceTargets(def, def.getMyGroup(), def.getDefGroup());
		}
		if(haveTriggerCondition()) {
			List<Unit> defList = unitList.stream().filter(e -> isCanTrigger(frame,def, e)).collect(Collectors.toList());
			skillEffect.doDefHurtBear(frame, def, defList, hurtBear, skill, this);
		}else{
			skillEffect.doDefHurtBear(frame, def, unitList, hurtBear, skill, this);
		}
	}
	
	public void doAttrSkill(Unit atk, UnitAttr unitAttr, Skill skill) {
		skillEffect.doCalAttr(atk, unitAttr, skill, this);
	}

	public double getFunctionValue(Unit atk,Unit def,Skill skill,Object...args) {
		return calFormula.calFormula(atk,def,skill, this,args);
	}
	
	public long getEffectFrame() {
		return effectFrame;
	}
	
	//按照距离计算飞行时间
	public long getEffectFrame(Unit atk,Unit def) {
		//四舍五入 取int
		return Math.round(effectFrame*SettingManager.getInstance().getFlyFrameRate(atk,def));
	}
	
	
	public double calFormula(Unit atk, Unit def, double value) {
		switch (this.formulaType) {
			case 0: return value;
			case 1: return UnitAtkAction.calSkillHurt(atk, def, value);
			case 2: return UnitAtkAction.calCure(atk, def, value);
			case 3: return UnitAtkAction.calBuff(atk, def, value);
			case 4: return UnitAtkAction.calSkillHurt(atk, def, value,false);
            case 5:
                return UnitAtkAction.calSkillHurt(atk, def, value, true, false);

		} 
		return value;
	}
	
	public static void main(String[] args) {
		double v = 5*0.7;
		System.err.println(Math.round(v));
	}
}
