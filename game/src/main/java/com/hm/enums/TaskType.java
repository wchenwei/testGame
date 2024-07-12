
package com.hm.enums;
/**
 * Title: BuildType.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年4月29日 上午10:51:55
 * @version 1.0
 */
public enum TaskType {

//	1 指挥中心（默认1级解锁）
//	2 坦克工厂1（默认1级解锁）
//	3 坦克工厂2（默认13级解锁）
//	4 部落（默认5级解锁）
//	5 科技中心（默认3级解锁）
//	6 仓库1（默认9级解锁）
//	7 仓库2（默认17级解锁）
//	8 装置车间（默认10级解锁）
//	9 军事学院（默认20级解锁）
//	10 水晶工厂（默认7级解锁）
//	11 改造车间（默认15级解锁）
//	12 配件工厂（默认12级解锁）
//	13 作战中心（默认20级解锁）
//	14 铁矿
//	15 石油
//	16 铅矿
//	17 钛矿


	
	BUILDING(1,"基地建设"),
	PLAYER(2,"角色任务"),
	RESOURCE(3,"资源任务");
	private TaskType(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	
	private String desc;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}

