package com.hm.action.build.vo;

public class OpenBuildVO {
	private int blockId;
	
	private int buildType;
	
	private int buildLv;
	
	private int costGold;//立刻完成需要的金币
	
	private int lastSecond;//升级剩余时间

	public int getBlockId() {
		return blockId;
	}

	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}

	public int getBuildType() {
		return buildType;
	}

	public void setBuildType(int buildType) {
		this.buildType = buildType;
	}

	public int getBuildLv() {
		return buildLv;
	}

	public void setBuildLv(int buildLv) {
		this.buildLv = buildLv;
	}

	public int getCostGold() {
		return costGold;
	}

	public void setCostGold(int costGold) {
		this.costGold = costGold;
	}

	public int getLastSecond() {
		return lastSecond;
	}

	public void setLastSecond(int lastSecond) {
		this.lastSecond = lastSecond;
	}
	
	

}
