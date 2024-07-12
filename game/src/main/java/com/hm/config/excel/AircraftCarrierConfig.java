package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.CvEngineLevelTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.TankAttr;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 航母
 * @author: chenwei
 * @create: 2020-12-15 11:42
 **/
@Slf4j
@Config
public class AircraftCarrierConfig extends ExcleConfig{
    private Map<Integer, CvEngineLevelTemplateImpl> engineLevelTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, CvLevelTemplateImpl> levelTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, CvDrawConfigTemplateImpl> drawMap = Maps.newConcurrentMap();
    private Map<Integer,ItemBattleplaneTemplateImpl> airMap = Maps.newConcurrentMap();
    private Map<Integer,CvDrawLibraryTemplateImpl> libMap = Maps.newConcurrentMap();
    private Table<Integer,Integer,BattleplaneStarTemplateImpl> starTables = HashBasedTable.create();
    private Map<Integer,Integer> starMaxs = Maps.newConcurrentMap();
    private int maxLv;
    private int maxEngineLv;

    private Map<Integer, CvIslandTemplateImpl> islandMap = Maps.newConcurrentMap();
    private Table<Integer,Integer,CvIslandTemplateImpl> islandTables = HashBasedTable.create();

    @Override
    public void loadConfig() {
        loadEngineLevel();
        loadLevel();
        loadDrawConfig();
        loadAirConfig();
        loadLibConfig();
        loadStarConfig();
        loadIslandConfig();
    }

    private void loadEngineLevel() {
        try {
            List<CvEngineLevelTemplateImpl> templateList = JSONUtil.fromJson(getJson(CvEngineLevelTemplateImpl.class),new TypeReference<List<CvEngineLevelTemplateImpl>>(){});
            templateList.forEach(CvEngineLevelTemplateImpl::init);
            templateList.forEach(e -> e.initAttr(templateList));
            engineLevelTemplateMap = templateList.stream().collect(Collectors.toMap(CvEngineLevelTemplate::getId, Function.identity()));
            maxEngineLv = engineLevelTemplateMap.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
        } catch (Exception e) {
            log.error("航母动力舱加载失败", e);
        }
    }

    private void loadLevel() {
        try {
            List<CvLevelTemplateImpl> templateList = JSONUtil.fromJson(getJson(CvLevelTemplateImpl.class),new TypeReference<List<CvLevelTemplateImpl>>(){});
            templateList.forEach(e ->{
                e.init();
            });
            levelTemplateMap = templateList.stream().collect(Collectors.toMap(CvLevelTemplateImpl::getId, Function.identity()));
            maxLv = levelTemplateMap.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();
        } catch (Exception e) {
            log.error("航母等级加载失败", e);
        }
    }
    
    private void loadDrawConfig() {
        try {
            List<CvDrawConfigTemplateImpl> templateList = JSONUtil.fromJson(getJson(CvDrawConfigTemplateImpl.class),new TypeReference<List<CvDrawConfigTemplateImpl>>(){});
            templateList.forEach(e ->{
                e.init();
            });
            drawMap = templateList.stream().collect(Collectors.toMap(CvDrawConfigTemplateImpl::getId, Function.identity()));
        } catch (Exception e) {
            log.error("战机十连配置加载失败", e);
        }
    }
    
    private void loadAirConfig() {
        try {
            List<ItemBattleplaneTemplateImpl> templateList = JSONUtil.fromJson(getJson(ItemBattleplaneTemplateImpl.class),new TypeReference<List<ItemBattleplaneTemplateImpl>>(){});
            templateList.forEach(e ->{
                e.init();
            });
            airMap = templateList.stream().collect(Collectors.toMap(ItemBattleplaneTemplateImpl::getId, Function.identity()));
        } catch (Exception e) {
            log.error("战机配置加载失败", e);
        }
    }
    
    private void loadLibConfig() {
        try {
            List<CvDrawLibraryTemplateImpl> templateList = JSONUtil.fromJson(getJson(CvDrawLibraryTemplateImpl.class),new TypeReference<List<CvDrawLibraryTemplateImpl>>(){});
            templateList.forEach(e ->{
                e.init();
            });
            libMap = templateList.stream().collect(Collectors.toMap(CvDrawLibraryTemplateImpl::getId, Function.identity()));
        } catch (Exception e) {
            log.error("战机随机lib配置加载失败", e);
        }
    }
    
    private void loadStarConfig() {
        try {
            List<BattleplaneStarTemplateImpl> templateList = JSONUtil.fromJson(getJson(BattleplaneStarTemplateImpl.class),new TypeReference<List<BattleplaneStarTemplateImpl>>(){});
            Table<Integer,Integer,BattleplaneStarTemplateImpl> tables = HashBasedTable.create();
            Map<Integer,Integer> map = Maps.newConcurrentMap();
            templateList.forEach(e ->{
                e.init();
                tables.put(e.getBattleplane_id(), e.getStar(), e);
                int star = map.getOrDefault(e.getBattleplane_id(), 1);
                map.put(e.getBattleplane_id(), Math.max(e.getStar(), star));
            });
            starTables = ImmutableTable.copyOf(tables);
            starMaxs = ImmutableMap.copyOf(map);
        } catch (Exception e) {
            log.error("战机升星配置加载失败", e);
        }
    }

    private void loadIslandConfig() {
        try {
            List<CvIslandTemplateImpl> templateList = JSONUtil.fromJson(getJson(CvIslandTemplateImpl.class),new TypeReference<List<CvIslandTemplateImpl>>(){});
            Map<Integer,CvIslandTemplateImpl> map = Maps.newConcurrentMap();
            Table<Integer,Integer,CvIslandTemplateImpl> tables = HashBasedTable.create();
            templateList.forEach(e ->{
                e.init();
                tables.put(e.getType(), e.getLevel(), e);
                map.put(e.getId(), e);
            });
            islandMap = ImmutableMap.copyOf(map);
            islandTables = ImmutableTable.copyOf(tables);
        } catch (Exception e) {
            log.error("航母舰岛加载失败", e);
        }
    }
    
    public CvDrawConfigTemplateImpl getDrawTemplate(int id) {
    	return drawMap.get(id);
    }
    
    public List<Integer> getAirIdByRare(int rare){
    	return airMap.values().stream().filter(t ->t.getQuality()==rare).map(t ->t.getId()).collect(Collectors.toList());
    }
    
    public CvDrawLibraryTemplateImpl getLibTemplate(int libId,int lv) {
    	return libMap.values().stream().filter(t ->t.isFit(libId,lv)).findFirst().orElse(null);
    }
    public ItemBattleplaneTemplateImpl getAirTemplate(int id){
    	return airMap.get(id);
    }
    public Items randomLibReward(int libId,int lv) {
    	CvDrawLibraryTemplateImpl template = getLibTemplate(libId, lv);
    	if(template==null) {
    		return new Items();
    	}
    	return template.random();
    	
    }
    
    public BattleplaneStarTemplateImpl getStarTemplate(int id,int star) {
    	return starTables.get(id, star);
    }
    public int getMaxStar(int id) {
    	return starMaxs.getOrDefault(id, 1);
    }
    

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(CvDrawConfigTemplateImpl.class,
                CvDrawLibraryTemplateImpl.class,
                CvEngineLevelTemplateImpl.class,
                CvLevelTemplateImpl.class,
                ItemBattleplaneTemplateImpl.class,
                BattleplaneStarTemplateImpl.class,
                CvIslandTemplateImpl.class);
    }

    public CvEngineLevelTemplateImpl getCvEngineLevelTemplate(int lv){
        return engineLevelTemplateMap.get(lv);
    }

    public CvLevelTemplateImpl getCvLevelTemplate(int lv){
        return levelTemplateMap.get(lv);
    }

    public int getMaxLv() {
        return maxLv;
    }

    public int getMaxEngineLv() {
        return maxEngineLv;
    }
    //是否是传奇飞机
	public boolean isSSAir(int itemType,int id) {
		if(itemType!=ItemType.Aircraft.getType()) {
			return false;
		}
		ItemBattleplaneTemplateImpl template = getAirTemplate(id);
		return template!=null&&template.getQuality()==6;
	}

    public Map<Integer, Integer> getOpenIsland(int lv, int engineLv, Map<Integer, Integer> island) {
        Map<Integer, Integer> result = Maps.newHashMap();
        Set<Integer> type = islandTables.rowKeySet();
        result = type.stream().map(e->{return islandTables.get(e, 0); })
                .filter(e->lv>=e.getUnlock_engine_cabin_lv() && engineLv>=e.getUnlock_engine_room_lv() && !island.containsKey(e.getType()))
                .collect(Collectors.toMap(CvIslandTemplateImpl::getType, CvIslandTemplateImpl::getId));
        return result;
    }

    public CvIslandTemplateImpl getIslandById(int id) {
        return islandMap.getOrDefault(id, null);
    }

    public CvIslandTemplateImpl getNextLvIsland(Integer type, Integer level) {
        if(islandTables.contains(type, level+1)) {
            return islandTables.get(type, level+1);
        }
        return null;
    }

    /**
     * 获取坦克属性加成（航母-舰岛）
     * @param island
     * @return
     */
    public TankAttr getIslandAttr(Map<Integer, Integer> island) {
        if(island.isEmpty()) {
            return null;
        }
        TankAttr attr = new TankAttr();
        island.values().stream()
                .map(e->{return islandMap.get(e);})
                .filter(Objects::nonNull)
                .forEach(e-> {attr.addAttr(e.getTankAttrMap());attr.addAttr(e.getTankAttrExtraMap());});
        return attr;
    }

    public List<CvIslandTemplateImpl> getPlayerIsland(Player player) {
        return player.playerAircraftCarrier().getIsland()
                .values().stream().map(e -> islandMap.get(e))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }
}
