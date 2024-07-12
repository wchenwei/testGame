package com.hm.enums;

import java.util.Arrays;


public enum OverallWarTroopType {
	V1(1, "1v1",0,1),
	V2(2, "2v2",1,3),
	V3(3,"3v3",3,6),
	V4(4,"4v4",6,10),
	V5(5,"5v5",10,15),
	;

	private OverallWarTroopType(int type, String desc,int start,int end) {
		this.type = type;
		this.desc = desc;
		this.start = start;
		this.end = end;
	}
	
	private int type;
	private String desc;
	private int start;
	private int end;

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

	public int getStart() {
		return start;
	}


	public int getEnd() {
		return end;
	}

	public static OverallWarTroopType getTroopType(int type) {
		return Arrays.stream(OverallWarTroopType.values()).filter(t -> t.getType()==type).findFirst().orElse(null);
	}

	
}
