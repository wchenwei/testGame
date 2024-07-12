package com.hm.model.tank;

import com.hm.enums.TankAttrType;
import lombok.Data;

@Data
public class TankVo {
	private int id; 
	private int lv; // 等级
	private int reLv; //改造等级(品质)
	private long exp; //累计经验 
	private int star; //星级
	private long combat;//战力
	private int evolveStar;//进化星级
	private double hp;
	private double maxHp;
	private int tankSpecialType;
	//需要用到才需赋值
	private int index;
	
	public TankVo() {
		super();
	}
	public TankVo(Tank tank) {
		super();
		this.id = tank.getId();
		this.lv = tank.getLv();
		this.reLv = tank.getReLv();
		this.exp = tank.getExp();
		this.star = tank.getStar();
		this.combat = tank.getCombat();
		this.evolveStar = tank.getEvolveStar();
		this.hp = tank.getCurHp();
		this.maxHp = tank.getTotalAttr(TankAttrType.HP);
		this.tankSpecialType = tank.getTankSpecial().getType();
	}
}
