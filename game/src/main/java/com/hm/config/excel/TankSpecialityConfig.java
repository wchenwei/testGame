package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.TankSpecNameTemplateImpl;
import com.hm.config.excel.templaextra.TankSpecTempImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * ClassName: TankSpecialityConfig. <br/>  
 * Function: 坦克专精. <br/>  
 * date: 2019年7月15日 上午10:39:07 <br/>  
 * @author zxj  
 * @version
 */
@Config
public class TankSpecialityConfig extends ExcleConfig{
	//id,tankSpec
	private Map<Integer, TankSpecTempImpl> tankSpecMap = Maps.newHashMap();
	//tree, level, tankSpec
	private Table<Integer, Integer, TankSpecTempImpl> tankSpecTable = HashBasedTable.create();
	
	//坦克专精名字配置信息 id,TankSpecNameTemplateImpl
	private Map<Integer, TankSpecNameTemplateImpl> tankSpecName = Maps.newHashMap();
	
	@Override
	public void loadConfig() {
		List<TankSpecTempImpl> tankSpecList = loadSpecFile();
		Map<Integer, TankSpecTempImpl> tankSpecTemp = Maps.newHashMap();
		tankSpecTemp = tankSpecList.stream()
				.collect(Collectors.toMap(TankSpecTempImpl::getId, Function.identity()));
		Table<Integer, Integer, TankSpecTempImpl> tempTable = HashBasedTable.create();
		for (TankSpecTempImpl spec : tankSpecList) {
			tempTable.put(spec.getTree(), spec.getLevel(), spec);
		}
		tankSpecList.stream().forEach(e -> e.init(tempTable));
		tankSpecList.stream().forEach(e -> e.initRecasetItems(tempTable));
		//坦克专精的，分类，等级等信息
		tankSpecMap = ImmutableMap.copyOf(tankSpecTemp);
		tankSpecTable = ImmutableTable.copyOf(tempTable);
		
		//坦克专精名字信息，主要用于开启后的属性加成
		Map<Integer, TankSpecNameTemplateImpl> tankSpecNameTemp = Maps.newHashMap();
		List<TankSpecNameTemplateImpl> tankSpecNameList = loadSpecNameFile();
		tankSpecNameList.stream().forEach(e->{
			e.init();
		});
		tankSpecNameTemp = tankSpecNameList.stream()
				.collect(Collectors.toMap(TankSpecNameTemplateImpl::getId, Function.identity()));
		tankSpecName = ImmutableMap.copyOf(tankSpecNameTemp);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(TankSpecTempImpl.class, TankSpecNameTemplateImpl.class);
	}

	private List<TankSpecTempImpl> loadSpecFile() {
		return JSONUtil.fromJson(getJson(TankSpecTempImpl.class), new TypeReference<ArrayList<TankSpecTempImpl>>(){});
	}
	
	private List<TankSpecNameTemplateImpl> loadSpecNameFile() {
		return JSONUtil.fromJson(getJson(TankSpecNameTemplateImpl.class), new TypeReference<ArrayList<TankSpecNameTemplateImpl>>(){});
	}
	
	
	public TankSpecTempImpl getTankSpecById(int id) {
		return tankSpecMap.getOrDefault(id, null);
	}
	
	public TankSpecNameTemplateImpl getTankSpecNameById(int id) {
		return tankSpecName.getOrDefault(id, null);
	}
	
	public TankSpecTempImpl getTankSpecByTree(int tree, int level) {
		return tankSpecTable.get(tree, level);
	}
}




