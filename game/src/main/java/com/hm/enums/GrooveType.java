
package com.hm.enums;


/**
 * 
 * ClassName: TechnologyType. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2017年10月23日 上午11:49:10 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public enum GrooveType {
	BATTLE(1,"战斗槽位"),
	GATHER(2,"采集槽位"),
	DEF(3,"防守槽位"),
	;
	
	
	
	private GrooveType(int type,String desc){
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
	
	public static GrooveType getType(int type){
		for (GrooveType kind : GrooveType.values()) {
			if(type == kind.getType()) return kind; 
		}
		return null;
	}
}

