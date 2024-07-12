package com.hm.model.guild;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.GuildRewardTimesType;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;
import lombok.Data;

import java.util.List;
import java.util.Map;
/**
 * ClassName: GuildCityReward. <br/>  
 * Function: 部落城池奖励明细信息. <br/>  
 * date: 2019年1月18日 下午5:13:00 <br/>  
 * @author zxj  
 * @version
 */
@Data
public class GuildCityReward {
	//部落科技加成（GuildRewardTimesType，加成数量）
	private Map<Integer, Double> rewardExtra = Maps.newHashMap();
	//额外的资源产出
	private List<Items> itemsList = Lists.newArrayList();
	
	public GuildCityReward() {}
	
	//根据军需箱id，生成一个奖励信息
	public GuildCityReward(double cityResBox) {
		Items item = new Items(new Double(cityResBox).intValue(), 1, ItemType.ITEM.getType());
		this.itemsList.add(item);
	}
	//添加奖励次数
	public void addRewardExtra(GuildRewardTimesType type, double value) {
//		if(value<0) {
//			value = 0d;
//		}
		this.rewardExtra.put(type.getType(), this.rewardExtra.getOrDefault(type.getType(), 0.0)+value);
	}
	//根据数据，返回指定类型的加成
	public long getRewardNum(GuildRewardTimesType type, long value) {
		return new Double(value * rewardExtra.getOrDefault(type.getType(), 0.0)).longValue();
	}
	//根据数据，返回所有科技的加成值
	public long getRewardNum(long value) {
		double tempReward = 0;
		for(double v: rewardExtra.values()) {
			tempReward +=v;
		};
		return new Double(value * tempReward).longValue();
	}
}


