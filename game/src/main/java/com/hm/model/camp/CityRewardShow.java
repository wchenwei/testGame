package com.hm.model.camp;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.CityConfig;
import com.hm.model.item.Items;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 城池奖励
 * @author siyunlong  
 * @date 2019年4月28日 下午2:46:33 
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class CityRewardShow {
	private transient long time;
	private int hour;
	//资源产出 每个城市都要给
	private List<Items> itemsList = Lists.newArrayList();
	private int[] cityNum;//每个类型城市的数量
	//额外城池产出
	private ExtraCityReward extraCityReward;
	
	public CityRewardShow(int hour,long time,List<Integer> cityList) {
		this.hour = hour;
		//计算每个
		CityConfig cityConfig = SpringUtil.getBean(CityConfig.class);
		this.cityNum = cityConfig.calCityTypeNum(cityList);
		this.time = time;
	}
	
	public boolean isFit(long lastTime) {
		return this.time > lastTime;
	}
	
	public List<Items> getAllItems() {
		if(extraCityReward != null) {
			List<Items> tempList = Lists.newArrayList(itemsList);
			tempList.addAll(extraCityReward.getItemList());
			return tempList;
		}
		return itemsList;
	}
}
