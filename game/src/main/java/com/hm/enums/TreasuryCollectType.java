
package com.hm.enums;


/**
 * 
 * ClassName: TreasuryCollectType. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年10月10日16:22:00 <br/>  
 *  
 * @author xjt  
 * @version
 */
public enum TreasuryCollectType {
	CashLevy(1,"征收钞票"),
	OilLevy(2,"征收石油"),
	;
	
	
	
	private TreasuryCollectType(int type,String desc){
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
	
	public static TreasuryCollectType getType(int index){
		for (TreasuryCollectType type : TreasuryCollectType.values()) {
			if(index == type.getType()) return type; 
		}
		return null;
	}
	
}

