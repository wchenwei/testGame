package com.hm.enums;
/**
 * ClassName: GuildRewardTimesType. <br/>  
 * Function: 部落科技对资源加成类型. <br/>  
 * date: 2019年1月30日 上午9:33:58 <br/>  
 * @author zxj  
 * @version
 */
public enum GuildRewardTimesType {
	RewardDouble(1, "城市产出倍数"), 
	RewardAdd(2, "科技加成"),
	CreditAdd(4, "城市产出功勋加成"),
	RewardDoublePer(5, "城市产出倍数的概率"), 
	;

	private int type;
	@SuppressWarnings("unused")
	private String desc;

	private GuildRewardTimesType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public int getType() {
		return this.type;
	}

	public static GuildRewardTimesType getTimesType(int type)
	  {
	    for (GuildRewardTimesType temp : GuildRewardTimesType.values()) {
	      if (type == temp.getType()) {
	        return temp;
	      }
	    }
	    return null;
	  }
}
