package com.hm.war.sg.unit;

import com.hm.config.excel.templaextra.PvpNpcTemplate;
import com.hm.model.tank.Tank;
import com.hm.war.sg.UnitType;
import com.hm.war.sg.setting.TankSetting;
import lombok.Data;

@Data
public class UnitInfo {
	int unitType;//战斗单元类型  UnitType
	String name;
	int lv;
	int reform;
	int star; //星级
	int evolveStar;//进化星级
	int driveMaxLv;//军职的最大级
	int npcId;//如果是npc,存储npcId
	
	public UnitInfo() {
		super();
	}
	public UnitInfo(Tank tank,TankSetting setting) {
		this.unitType = UnitType.PlayerTank.getType();
		this.name = setting.getName();
		this.lv = tank.getLv();
		this.reform = tank.getReLv();
		this.star = tank.getStar();
		this.evolveStar = tank.getEvolveStar();
		this.driveMaxLv = tank.getDriver() != null ? tank.getDriver().getAdvanceMaxLv():0;
	}
	
	public UnitInfo(PvpNpcTemplate npc) {
		this.unitType = UnitType.PvpNpc.getType();
		this.name = npc.getName();
		this.lv = npc.getLevel();
		this.reform = npc.getReform();
		this.star = npc.getStar();
		this.npcId = npc.getId();
	}
	
	public int getNpcId() {
		return this.npcId;
	}

	public boolean isNpc(){
		return this.npcId > 0;
	}
}
