package com.hm.action.cityworld.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.CityConfig;
import com.hm.config.city.CityAreaTemplate;
import com.hm.enums.WorldType;
import com.hm.model.guild.Guild;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WorldAreaBiz {
	@Resource
	private CityConfig cityConfig;
	/**
	 * 计算每个城池受到岛屿buff的产量加成
	 * @param guild
	 * @param cityList 部落所有城池列表
	 * @return
	 */
	public Map<Integer,Double> calCityAreaBuff(Guild guild,List<Integer> guildCityList) {
		Map<Integer,Double> cityMap = Maps.newHashMap();
		//当前部落占的所有岛屿区域
		List<Integer> areaList = getGuildAreas(guildCityList);
		for (int areaId : areaList) {
			log.error(guild.getServerId()+"岛屿buff:"+areaId+"->"+guild.getId());
			CityAreaTemplate areaTemplate = cityConfig.getCityAreaTemplate(areaId);
			areaTemplate.getCityList().forEach(e -> cityMap.put(e.getId(),areaTemplate.getAdd()));
		}
		return cityMap;
	}


	//占领全部城的岛屿
	public List<Integer> getGuildAreas(List<Integer> guildCityList){
		List<Integer> areaList = Lists.newArrayList();
		for (CityAreaTemplate areaTemplate : cityConfig.getWorldConfig(WorldType.Normal).getAreaMap().values()) {
			//判断所有城池是否归属一个阵营
			if(areaTemplate.getCityList().stream().allMatch(e -> CollUtil.contains(guildCityList,e.getId()))) {
				areaList.add(areaTemplate.getId());
			}
		}
		return areaList;
	}
}
