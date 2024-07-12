package com.hm.model.war;

import com.hm.war.sg.UnitGroup;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FightTroopRecord {
	private long playerId;
	private String name;
	private int lv;
	private String icon;
	private int frameIcon;
	private long combat;
	private String flag;
	private Integer flagId;
	
	public FightTroopRecord(UnitGroup unitGroup) {
		this.playerId = unitGroup.getPlayerId();
		this.name = unitGroup.getName();
		this.lv = unitGroup.getLv();
		this.icon = unitGroup.getIcon();
		this.frameIcon = unitGroup.getFrameIcon();
		this.combat = unitGroup.getCombat();
		this.flag = unitGroup.getFlag();
	}
	
	public void resetCombat(long combat) {
		this.combat = Math.max(this.combat, combat);
	}
}
