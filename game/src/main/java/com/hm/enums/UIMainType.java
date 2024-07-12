
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
public enum UIMainType {
	Troop(1,"部队"),
	Bag(2,"背包"),
	Camp(3,"阵营"),
	War(4,"战役"),
	Mail(5,"邮件"),
	Bsg(6,"兵书阁"),
	Task(7,"任务"),
	Arena(8,"竞技场"),
	Female(9,"女将"),
	Friend(10,"好友"),
	KuaFu(11,"跨服战"),
	Sweapon(12,"超武"),
	Rank(13,"排行榜"),
	Notice(14,"系统公告"),
	Setting(15,"设置"),
	Customer(16,"联系客服"),
	Login(17,"返回登陆"),
	World(18,"世界"),
	More(19,"更多"),
	;
	
	
	
	private UIMainType(int type,String desc){
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
	
	public static UIMainType getType(int index){
		for (UIMainType type : UIMainType.values()) {
			if(index == type.getType()) return type; 
		}
		return null;
	}
	
}

