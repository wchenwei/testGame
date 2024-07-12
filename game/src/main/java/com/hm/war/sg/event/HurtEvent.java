package com.hm.war.sg.event;

import com.hm.war.sg.unit.Unit;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HurtEvent extends Event{
	private long hurt;//
	private int addType;//附加类型 暴击 闪避 
	private long hp;//当前hp
	private double mp;//当前mp

	private int atkId;//攻击方坦克位置id
	private long skillId;//所受技能id 如果=0 就是普通攻击 >0 是技能攻击
	private int funcId;
	private int atkTimes;//普攻伤害次数
	
	public HurtEvent(Unit unit, long hurt, int addType, int atkId, long skillId, int funcId, int atkTimes) {
		super(unit.getId(), EventType.Hurt);
		this.hurt = hurt;
		this.addType = addType;
		this.hp = Math.max(unit.getHpEngine().getHp(), 0);
		this.mp = unit.getMpEngine().getMp();
		this.atkId = atkId;
		this.skillId = skillId;
		this.funcId = funcId;
		this.atkTimes = atkTimes;
	}
	
	@Override
	public String toString() {
        String content = "收到伤害:%s,攻击者%s,skillId:%s,funcId:%s,剩余血量:%s,addType:%s";
        return String.format(content, this.hurt, this.atkId, skillId, funcId, this.hp, this.addType);
	}
	@Override
	public void showHp() {
		System.err.println("hurt:"+getId()+"="+this.hp+ ":"+this.hurt);
	}
}
