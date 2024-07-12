package com.hm.model.guild;

import com.hm.model.camp.CityRewardShow;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * ClassName: GuildReward. <br/>  
 * Function: 部落产出. <br/>  
 * date: 2019年1月18日 上午10:22:46 <br/>  
 * @author zxj  
 * @version
 */
public class GuildReward extends GuildComponent {
	private CityRewardShow[] cityRewardList = new CityRewardShow[24];
	
	public void addCityReward(CityRewardShow cityReward) {
		this.cityRewardList[cityReward.getHour()] = cityReward;
		SetChanged();
	}

	public List<CityRewardShow> getCityRewardShow(long lastTime) {
		return Arrays.stream(cityRewardList).filter(Objects::nonNull)
				.filter(e -> e.isFit(lastTime))
				.collect(Collectors.toList());
	}

	public boolean havePlayerCityReward(long lastTime) {
		return Arrays.stream(cityRewardList).filter(Objects::nonNull)
				.anyMatch(e -> e.isFit(lastTime));
	}

}




