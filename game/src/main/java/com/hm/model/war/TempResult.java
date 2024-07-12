package com.hm.model.war;

import com.hm.enums.WarResultType;
import com.hm.war.sg.WarResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TempResult {
	private FightTroopRecord atk;//进攻方
	private FightTroopRecord def;//防御方
	//是否是攻击方胜利
	private boolean isAtkWin;
	private long startFightTime;//开始时间
	private long endFightTime;//结束时间
	private int warResultType = WarResultType.Normal.getType();//战斗报告类型
	
	public TempResult(WarResult result) {
		this.atk = new FightTroopRecord(result.getAtk());
		this.def = new FightTroopRecord(result.getDef());
		this.warResultType = result.getWarResultType();
		this.isAtkWin = result.isAtkWin();
		this.startFightTime = result.getStartFightTime();
		this.endFightTime = result.getEndFightTime();
	}
}
