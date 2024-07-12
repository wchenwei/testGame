package com.hm.action.build.vo;

public class OpenTankFactoryVO {
	private int blockId;
	private int speed;//每分钟募兵数
	private int limit;//兵营容量
	private int extensionTimes;//队列扩建次数
	private int overTimes;//加时次数
	private int cost;//每个tanke消耗的石油数量
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getExtensionTimes() {
		return extensionTimes;
	}
	public void setExtensionTimes(int extensionTimes) {
		this.extensionTimes = extensionTimes;
	}
	public int getOverTimes() {
		return overTimes;
	}
	public void setOverTimes(int overTimes) {
		this.overTimes = overTimes;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getBlockId() {
		return blockId;
	}
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	

}
