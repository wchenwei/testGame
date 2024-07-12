package com.hm.war.sg.buff;

public enum BuffKind {
	None(0,"none"),
	AttrChange(1,"属性改变"),
	Buff(2,"buff类"),
	DeBuff(3,"DeBuff"),
	;
	
	private BuffKind(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	private int type;
	
	private String desc;
	
	public static BuffKind getKind(int type) {
		for (BuffKind buildType : BuffKind.values()) {
			if(buildType.getType() == type) {
				return buildType;
			}
		}
		return null;
	}

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
