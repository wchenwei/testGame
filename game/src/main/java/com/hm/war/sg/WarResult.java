package com.hm.war.sg;

import com.google.common.collect.Lists;
import com.hm.action.guildwar.WarConstants;
import com.hm.enums.WarResultType;
import com.hm.model.item.Items;
import lombok.Data;

import java.util.List;

@Data
public class WarResult {
	private UnitGroup atk;//进攻方
	private UnitGroup def;//防御方
	//帧列表
	private List<Frame> frameList = Lists.newArrayList();
	//是否是攻击方胜利
	private boolean isAtkWin;
	//战斗总帧数
    private int moveFrame = WarConstants.WarMoveFrame;//0-30
	private int battleFrame;//30-100
    private int calFrame = WarConstants.WarCalFrame;//100-130
	private int atkWinNum;//攻击方胜利次数
	private int defWinNum;//防御方胜利次数
	private long startFightTime;//开始时间
	private long endFightTime;//结束时间
	private int warResultType = WarResultType.Normal.getType();//战斗报告类型
	private List<Items> showRewardList;//
	private int frameTime = WarConstants.FrameTime;//每帧多少毫秒
	
	public WarResult(UnitGroup atk, UnitGroup def) {
		this.atk = atk;
		this.def = def;
	}

	public void addFrame(Frame frame) {
		if(!frame.isEmptyEvent()) {
			this.frameList.add(frame);
		}
	}
	public UnitGroup getWinUnitGroup() {
		return isAtkWin ? atk:def;
	}
	public UnitGroup getLoseUnitGroup() {
		return isAtkWin ? def:atk;
	}
	
	public void syncWinNum(WarResult lastWarResult) {
		if(lastWarResult == null) {
			return;
		}
		this.atkWinNum = lastWarResult.atkWinNum;
		this.defWinNum = lastWarResult.defWinNum;
	}
	
	public void calWinNum() {
		if(this.isAtkWin) {
			this.atkWinNum ++;
		}else{
			this.defWinNum ++;
		}
	}
	
	public int getBattleFrame() {
		return this.battleFrame;
	}
	public long getTotalFrame() {
		return this.moveFrame + battleFrame + calFrame;
	}
	
	public void calFightTime(int setFrameTime) {
		if(setFrameTime > 0) {
			this.frameTime = setFrameTime;
		}
		this.startFightTime = System.currentTimeMillis();
		//晚2秒结束，用于控制客户端弹出结算框
		this.endFightTime = System.currentTimeMillis()+getTotalFrame()*frameTime;
	}
	
	public void setWarResultType(WarResultType warResultType) {
		this.warResultType = warResultType.getType();
	}
	
	public boolean isOver() {
		return System.currentTimeMillis() > this.endFightTime;
	}
	/**
	 * 已经进行帧数
	 * @return
	 */
	public long getRunningFrame() {
		return (System.currentTimeMillis() - startFightTime)/frameTime;
	}
}
