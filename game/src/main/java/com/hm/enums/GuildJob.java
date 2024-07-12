package com.hm.enums;

public enum GuildJob {
	NONE(0,"无职位"),
	LEGATUS(1,"副部落长"),
	PRESIDENT(2,"部落长"),
	;
	
	private GuildJob(int type,String desc){
		this.type = type;
		this.desc = desc;
	}
	private int type;
	private String desc;
	
	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}	
	
	public static GuildJob getType(int type) {
		for (GuildJob temp : GuildJob.values()) {
			if(type == temp.getType()) return temp; 
		}
		return null;
	}
	public static String getJobName(int type) {
		GuildJob job = getType(type);
		if(job != null) {
			return job.getDesc();
		}
		return NONE.getDesc();
	}
	/**
	 * 是否是官员
	 * @param type
	 * @return
	 */
	public static boolean isOfficer(int type){
		return type==LEGATUS.getType();
	}
	/**
	 *是否是部落长
	 */
	public static boolean isPersident(int type){
		return type==PRESIDENT.getType();
	}
	/**
	 *是否是部落管理者
	 */
	public static boolean isManager(int type){
		return isOfficer(type)||isPersident(type);
	}
}
