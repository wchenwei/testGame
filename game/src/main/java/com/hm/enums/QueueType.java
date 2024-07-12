package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;

public enum QueueType {
	BuildUp(0,"建筑升级队列"),
	Produce(1,"生产队列"),
	Arms(2,"武器生产队列"),
	;
	
	private QueueType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	
	
	private String desc;

	public int getType() {
		return type;
	}

	//获取队列加速每分钟花费的钻石数
	public int getCost() {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		return 1;
		//return commValueConfig.getCommValue(CommonValueType.getCommonValueTypeByQueueType(type));
	}


	public String getDesc() {
		return desc;
	}

	public static boolean isBuildUp(QueueType queueType) {
		return queueType==BuildUp;
	}
	//是否可以使用钻石加速
	public static boolean isCanSpeedUp(QueueType queueType){
		return queueType == QueueType.BuildUp;
	}
	//是否可以使用道具加速
	public static boolean isCanSpeedUpUserItem(QueueType queueType){
		return queueType == QueueType.BuildUp;
	}
	//是否可以帮助
	public static boolean isCanHelp(QueueType queueType){
		return queueType==QueueType.BuildUp;
	}

	public static boolean isCanCancel(QueueType queueType) {
		return queueType==QueueType.BuildUp||queueType==QueueType.Produce;
	}
	
}
