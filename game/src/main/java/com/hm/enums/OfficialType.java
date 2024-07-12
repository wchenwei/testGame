package com.hm.enums;

/**
 * 
 * @Description: 国家官职类型
 * @author siyunlong  
 * @date 2017年10月9日 下午5:24:30 
 * @version V1.0
 */
public enum OfficialType {
	//总统，司令，参谋长，将军
	//此处的type，对应的，camp_power配置文件中的id，修改清注意
	JSZ(1,2,"总军士长"),
	CM(2,2,"总参谋"),
	SL(3,3,"总司令"),
	CZG(4,1,"财政官"),
	QBG(5,1,"情报官"),
	HQG(6,1,"后勤官"),
	ZAG(7,1,"治安官"),
	SFG(8,1,"司法官"),
	JCG(9,1,"检查官"),
	MSZ(10,1,"秘书长"),
	NYG(11,1,"农业官"),
	JYG(12,1,"教育官"),
	WSG(13,1,"秘书长"),
	JTG(14,1,"交通官"),
	MYG(15,1,"贸易官"),
	SB(0,0,"士兵"),
	;
	
	private OfficialType(int type,int lv, String desc) {
		this.type = type;
		this.lv = lv;
		this.desc = desc;
	}
	
	public static OfficialType getOfficialType(int type) {
		for (OfficialType buildType : OfficialType.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}
	//是否是阵营管理者
	public static boolean isManager(int type){
		return type==JSZ.getType()||type==CM.getType()||type==SL.getType();
	}
	
	private int type;
	private int lv;//官员等级
	private String desc;
	
	public int getType() {
		return type;
	}

	public int getLv() {
		return lv;
	}
	
}
