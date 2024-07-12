package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.Active81TempImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: Active81SearchConfig. <br/>  
 * Function: 81活动. <br/>  
 * date: 2019年6月24日 下午2:58:05 <br/>  
 *  
 * @author zxj  
 * @version
 */
@Config ( "Active81SearchConfig" )
public class Active81SearchConfig extends ExcleConfig{
	//id,template
	Map<Integer, Active81TempImpl> searchMap = Maps.newConcurrentMap();
	//Refresh_limit, template
	Map<Integer, Active81TempImpl> searchLimit = Maps.newConcurrentMap();
	//81活动中，保底的次数
	private int maxCycle = 0;
	//金砖刷新，资源权重
	Map<Active81TempImpl, Integer> goldMap = Maps.newHashMap();
	//免费刷新，权重
	Map<Active81TempImpl, Integer> freeMap = Maps.newHashMap();
	
	@Override
	public void loadConfig() {
//		loadActiveConsumeGold();
	}
	@Override
	public List<String> getDownloadFile() {
		return getConfigName(Active81TempImpl.class);
	}
	private void loadActiveConsumeGold() {
		Map<Integer, Active81TempImpl> tempSearchMap = Maps.newConcurrentMap();
		Map<Integer, Active81TempImpl> tempLimitMap = Maps.newConcurrentMap();
		for(Active81TempImpl template :JSONUtil.fromJson(getJson(Active81TempImpl.class),
				new TypeReference<ArrayList<Active81TempImpl>>() {
				})) {
			template.init();
			tempSearchMap.put(template.getId(), template);
			if(template.getRefresh_limit()>0) {
				tempLimitMap.put(template.getRefresh_limit(), template);
			}
			if(template.getWeight_gold()>0) {
				goldMap.put(template, template.getWeight_gold());
			}
			if(template.getWeight_free()>0) {
				freeMap.put(template, template.getWeight_free());
			}
		}
		this.searchMap = ImmutableMap.copyOf(tempSearchMap);
		this.searchLimit = ImmutableMap.copyOf(tempLimitMap);
		
		maxCycle = this.searchMap.values().stream().mapToInt(s->s.getRefresh_limit()).max().getAsInt();
	}
	
	public Active81TempImpl getSearch(int id) {
		return this.searchMap.getOrDefault(id, null);
	}
	
	public Active81TempImpl getLimit(int times) {
		if(times>0) {
			int tempCycle = times%maxCycle;
			return this.searchLimit.getOrDefault(tempCycle==0?maxCycle:tempCycle, null);
		}
		return null;
	}
	
	/**
	 * getRandomWeight:(获取的权重信息). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * @author zxj  
	 * @param type	类型，1：免费权重；2：收费权重
	 * @return  使用说明
	 *
	 */
	public Map<Active81TempImpl, Integer> getRandomWeight(int type) {
        return type==2?goldMap:freeMap;
    }
	
	public int getMaxCycle() {
		return maxCycle;
	}
}



