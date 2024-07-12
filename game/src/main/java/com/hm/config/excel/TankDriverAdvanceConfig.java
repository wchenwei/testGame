package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.DriverAdvanceShowImpl;
import com.hm.config.excel.templaextra.DriverAdvanceTemplateImpl;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: TankDriverAdvanceConfig. <br/>  
 * Function: 坦克车长，军职. <br/>  
 * date: 2020年3月4日15:48:40
 * @author zxj  
 * @version
 */
@Config
public class TankDriverAdvanceConfig extends ExcleConfig{
	//id,tankdriveradvance
	private Map<Integer, DriverAdvanceTemplateImpl> tankAdvance = Maps.newHashMap();
	
	//三选一属性加成
	private Map<Integer, DriverAdvanceShowImpl> tankAdvanceShow = Maps.newHashMap();
	
	@Override
	public void loadConfig() {
		//坦克军长，军职
		List<DriverAdvanceTemplateImpl> tankAdvanceList = loadDriverAdvanceFile();
		Map<Integer, DriverAdvanceTemplateImpl> tankSpecTemp = Maps.newHashMap();
		tankAdvanceList.stream().forEach(e -> e.init());
		tankSpecTemp = tankAdvanceList.stream()
				.collect(Collectors.toMap(DriverAdvanceTemplateImpl::getId, Function.identity()));
		tankAdvance = ImmutableMap.copyOf(tankSpecTemp);
		
		
		
		//坦克军长，军职-军职三选一属性加成
		Map<Integer, DriverAdvanceShowImpl> tanktankAdvanceShowTemp = Maps.newHashMap();
		List<DriverAdvanceShowImpl> tankAdvanceShowList = loadDriverAdvanceShowFile();
		tankAdvanceShowList.stream().forEach(e->{
			e.init();
		});
		tanktankAdvanceShowTemp = tankAdvanceShowList.stream()
				.collect(Collectors.toMap(DriverAdvanceShowImpl::getId, Function.identity()));
		tankAdvanceShow = ImmutableMap.copyOf(tanktankAdvanceShowTemp);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(DriverAdvanceTemplateImpl.class, DriverAdvanceShowImpl.class);
	}

	private List<DriverAdvanceShowImpl> loadDriverAdvanceShowFile() {
		return JSONUtil.fromJson(getJson(DriverAdvanceShowImpl.class), new TypeReference<ArrayList<DriverAdvanceShowImpl>>(){});
	}
	
	private List<DriverAdvanceTemplateImpl> loadDriverAdvanceFile() {
		return JSONUtil.fromJson(getJson(DriverAdvanceTemplateImpl.class), new TypeReference<ArrayList<DriverAdvanceTemplateImpl>>(){});
	}
	
	public DriverAdvanceTemplateImpl getTannkAdvance(int lv) {
		return tankAdvance.getOrDefault(lv, null);
	}
	
	public DriverAdvanceShowImpl getTannkAdvanceShow(int id) {
		return tankAdvanceShow.getOrDefault(id, null);
	}
	
	public List<Items> getAdvanceCostByLv(int lv) {
		return tankAdvance.get(lv).getLvCost();
	}

	public List<Items> getAdvanceCostAll(int advanceMaxLv) {
		List<Items> lvCost = Lists.newArrayList();
		for(int i=1;i<advanceMaxLv; i++) {
			lvCost.addAll(tankAdvance.get(i).getLvCost());
		}
		return lvCost;
	}
	
	//获取羁绊属性
	public TankAttr getAttrFetterAttr(int minLv){
		TankAttr tankAttr = new TankAttr();
		tankAdvance.keySet().forEach(e->{
			if(e<=minLv) {
				DriverAdvanceTemplateImpl advanceTemplat = tankAdvance.get(e);
				tankAttr.addAttr(advanceTemplat.getFetterAttrMap());
			}
		});
		return tankAttr; 
	}
}




