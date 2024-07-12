package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.*;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.config.CityConfig;
import com.hm.config.excel.temlate.NpcTankTemplate;
import com.hm.config.excel.temlate.TankBaseTemplate;
import com.hm.config.excel.templaextra.CarTemplate;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.config.excel.templaextra.PvpNpcTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.NpcConfType;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.annotation.ConfigLoadIndex;
import com.hm.model.cityworld.CityNpc;
import com.hm.model.cityworld.WorldCity;
import com.hm.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@Config
@ConfigLoadIndex(100)
public class NpcConfig extends ExcleConfig{
	@Resource
    private CityConfig cityConfig;
	@Resource
    private WorldBiz worldBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private CommanderConfig commanderConfig;
	
	private Map<Integer,PvpNpcTemplate> npcMap = Maps.newConcurrentMap();
	//key:等級 
	private Map<Integer,NpcTroopTemplate> npcTroopMap = Maps.newConcurrentMap();
	//key:等级  key:类型  value:
	private Table<Integer,Integer,List<NpcTroopTemplate>> npcTroopTables = HashBasedTable.create();
	
	private Map<Integer,NpcTankTemplate> npcTank = Maps.newConcurrentMap();
	private int maxLv;
	
	@Override
	public void loadConfig() {
		loadPvpNpcConfig();
		loadNpcTroopConfig();
		loadNpcTankConfig();
		log.info("加载npc成功");
	}

	private void loadNpcTankConfig() {
		Map<Integer,NpcTankTemplate> npcTankTemp = json2ImmutableMap(NpcTankTemplate::getId, NpcTankTemplate.class);
		this.npcTank = ImmutableMap.copyOf(npcTankTemp);
	}

	private void loadNpcTroopConfig() {
		Map<Integer,NpcTroopTemplate> enemyMap = Maps.newHashMap();
		Table<Integer,Integer,List<NpcTroopTemplate>> npcTroopTables = HashBasedTable.create();
		Map<Integer, CarTemplate> carMap = commanderConfig.getCarMap();
		for (NpcTroopTemplate template : json2List(NpcTroopTemplate.class)) {
			template.init(carMap);
			List<NpcTroopTemplate> templateList = npcTroopTables.get(template.getLevel(), template.getType());
			if(templateList == null) {
				templateList = Lists.newArrayList();
				npcTroopTables.put(template.getLevel(), template.getType(), templateList);
			}
			templateList.add(template);
			enemyMap.put(template.getId(), template);
		}
		this.npcTroopTables = ImmutableTable.copyOf(npcTroopTables);
		this.npcTroopMap = ImmutableMap.copyOf(enemyMap);
		this.maxLv = this.npcTroopMap.values().stream().mapToInt(e -> e.getLevel()).max().getAsInt();
	}

	private void loadPvpNpcConfig() {
		Map<Integer,PvpNpcTemplate> npcMap = json2Map(PvpNpcTemplate::getId, PvpNpcTemplate.class);
		int[] unlockSkillLvs = commValueConfig.getConvertObj(CommonValueType.TankSkillUnlockLvs);

		Map<Integer, TankBaseTemplate> tankBaseMap = json2ImmutableMap(TankBaseTemplate::getId, TankBaseTemplate.class);
		npcMap.values().forEach(e -> {
			TankBaseTemplate tankTemplate = tankBaseMap.get(e.getModel());
			e.init(unlockSkillLvs, tankTemplate);
		});
		this.npcMap = ImmutableMap.copyOf(npcMap);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(NpcTroopTemplate.class,PvpNpcTemplate.class, NpcTankTemplate.class);
	}
	
	public NpcTroopTemplate getNpcTroopTemplate(int id) {
		return this.npcTroopMap.get(id);
	}
	public PvpNpcTemplate getPvpNpcTemplate(int id) {
		return this.npcMap.get(id);
	}
	
	public List<PvpNpcTemplate> getPvpNpcTemplateList(int[] ids) {
		List<PvpNpcTemplate> list = Lists.newArrayList();
		for(int i=0; i<ids.length; i++) {
			list.add(getPvpNpcTemplate(ids[i]));
		}
		return list;
	}

	/**
	 * 随机等级npc个数
	 * @param lv
	 * @param count
	 * @return
	 */
//	public List<Integer> randomNpcList(int lv,int count) {
//		final int tempLv = Math.min(lv, this.maxLv);
//		List<Integer> tempList = this.npcTroopMap.values().stream()
//				.filter(e -> e.getLevel() == tempLv).mapToInt(e -> e.getId()).boxed().collect(Collectors.toList());
//		return RandomUtils.randomRepeatableEleList(tempList, count);
//	}
	
	public int randomNpcId(int lv) {
		final int tempLv = Math.min(lv, this.maxLv);
		List<Integer> tempList = this.npcTroopMap.values().stream()
				.filter(e -> e.getLevel() == tempLv).mapToInt(e -> e.getId()).boxed().collect(Collectors.toList());
		return RandomUtils.randomEle(tempList);
	}
	
	public List<Integer> randomNpcList(WorldCity worldCity,int count) {
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		CityNpc cityNpc = worldCity.getCityNpc();
		int npcLv = worldBiz.getCityNpcLv(worldCity);
		int tempLv = Math.max(cityTemplate.getNpcLv(), npcLv);
		tempLv = Math.min(npcLv, this.maxLv);
		int npcIndex = cityNpc.getNpcIndex();
		List<Integer> resultMap = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			List<NpcTroopTemplate> npcList = this.npcTroopTables.get(tempLv, cityTemplate.getNpcType(npcIndex));
			if(CollUtil.isEmpty(npcList)) {
				resultMap.add(randomNpcId(tempLv));
			}else{
				resultMap.add(RandomUtils.randomEle(npcList).getId());
			}
			npcIndex ++;
		}
		cityNpc.setNpcIndex(npcIndex);
		return resultMap;
	}
	
	//随机柏林npc
	public List<Integer> randomBerlinNpcList(WorldCity worldCity,NpcConfType type,int count) {
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		int npcLv = worldBiz.getCityNpcLv(worldCity)+10;
		int tempLv = Math.max(cityTemplate.getNpcLv(), npcLv);
		tempLv = Math.min(npcLv, this.maxLv);
		List<Integer> resultMap = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			List<NpcTroopTemplate> npcList = this.npcTroopTables.get(tempLv, type.getType());
			if(CollUtil.isNotEmpty(npcList)) {
				resultMap.add(RandomUtils.randomEle(npcList).getId());
			}
		}
		return resultMap;
	}
	
	public List<Integer> randomNpcList(NpcConfType type,int count) {
		List<NpcTroopTemplate> tempList = this.npcTroopTables.column(type.getType()).values().stream().flatMap(e -> e.stream()).collect(Collectors.toList());
		return RandomUtils.randomEleList(tempList, count).stream().map(e -> e.getId()).collect(Collectors.toList());
	}
	
	public NpcTankTemplate getNpcTank(int id) {
		return npcTank.get(id);
	}
	
	public List<Integer> randomNpcList(NpcConfType type,int lv,int count) {
	    lv = Math.min(lv,getMaxLv(type));
		List<NpcTroopTemplate> templateList = this.npcTroopTables.get(lv, type.getType());
		if(CollUtil.isEmpty(templateList)) {
			templateList = this.npcTroopTables.column(type.getType()).values().stream().flatMap(e -> e.stream()).collect(Collectors.toList());
		}
		List<Integer> npcList = Lists.newArrayList();
		if(CollUtil.isEmpty(templateList)) {
			return npcList;
		}
		for (int i = 0; i < count; i++) {
			npcList.add(RandomUtils.randomEle(templateList).getId());
		}
		return npcList;
	}

	public List<Integer> randomNpcList(int cityId,int count,int npcIndex,int npcLv) {
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		return randomNpcList(cityTemplate, count, npcIndex, npcLv);
	}
	public List<Integer> randomNpcList(CityTemplate cityTemplate,int count,int npcIndex,int npcLv) {
		npcLv = Math.min(npcLv, maxLv);
		List<Integer> resultMap = Lists.newArrayList();
		for (int i = 0; i < count; i++) {
			List<NpcTroopTemplate> npcList = this.npcTroopTables.get(npcLv, cityTemplate.getNpcType(npcIndex));
			if(CollUtil.isEmpty(npcList)) {
				resultMap.add(randomNpcId(npcLv));
			}else{
				resultMap.add(RandomUtils.randomEle(npcList).getId());
			}
			npcIndex ++;
		}
		return resultMap;
	}
	
	public int getMaxLv() {
		return maxLv;
	}

	public int getMaxLv(NpcConfType type){
        return this.npcTroopTables.column(type.getType()).values().stream().flatMap(t->t.stream()).mapToInt(t->t.getLevel()).max().orElse(0);
    }

	public List<NpcTroopTemplate> getNpcTemplate(int npcLv, int type) {
		return this.npcTroopTables.get(npcLv, type);
	}

}
