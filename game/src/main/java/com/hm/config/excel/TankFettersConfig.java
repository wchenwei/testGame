package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.FettersImpl;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
@Config
public class TankFettersConfig extends ExcleConfig{
	private Map<Integer, FettersImpl> tankFettersMap = Maps.newHashMap(); //羁绊
	private Map<Integer, FettersImpl> tankFettersStrMap = Maps.newHashMap(); //羁绊
	
	
	@Override
	public void loadConfig() {
		loadTankFetters();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(FettersImpl.class);
	}

	/**
	 * loadTankFetters:(加载坦克羁绊). <br/>  
	 * @author zxj    使用说明
	 */
	private void loadTankFetters() {
		Map<Integer, FettersImpl> tempFettersMap = Maps.newHashMap();
		Map<Integer, FettersImpl> tempFettersStrMap = Maps.newHashMap();
        List<FettersImpl> templateList= JSONUtil.fromJson(getJson(FettersImpl.class), new TypeReference<ArrayList<FettersImpl>>() {});
        templateList.forEach(e -> e.init());
        tempFettersMap = templateList.stream()
				.collect(Collectors.toMap(FettersImpl::getId, Function.identity()));
        for(FettersImpl tempFetters :templateList) {
        	tempFetters.getTankList().forEach(e->{
        		if(e>0) {
        			tempFettersStrMap.put(e, tempFetters);
        		}
        	});
        }
		this.tankFettersMap = ImmutableMap.copyOf(tempFettersMap);
		this.tankFettersStrMap = ImmutableMap.copyOf(tempFettersStrMap);
		log.info("羁绊配置加载完成");
	}
	
	public FettersImpl getFettersById(int id) {
		return this.tankFettersMap.get(id);
	}
	
	public FettersImpl getFetterByTankId(int tankId) {
		return tankFettersStrMap.get(tankId);
	}
	
	public List<Integer> getTankFetter(int tankId) {
		return getFetterByTankId(tankId).getTankList();
	}
	
	public List<FettersImpl> getFetterByIds(List<Integer> tankIds) {
		return tankIds.stream().map(e->tankFettersStrMap.get(e))
				.filter(Objects::nonNull)
				.distinct()
				.filter(e->{
					List<Integer> listIds = e.getTankList();
					return listIds.stream().allMatch(m->tankIds.contains(m));
				}).collect(Collectors.toList());
	}
}




