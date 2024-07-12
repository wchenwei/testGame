package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active6yearGiftTemplate;
import com.hm.config.excel.temlate.Active6yearPassGiftTemplate;
import com.hm.config.excel.temlate.Active6yearPassLevelTemplate;
import com.hm.config.excel.temlate.Active6yearRechargeDoubleTemplate;
import com.hm.config.excel.templaextra.Active6yearCircleTemplateImpl;
import com.hm.config.excel.templaextra.Active6yearLoginTemplateImpl;
import com.hm.config.excel.templaextra.Active6yearPassTemplateImpl;
import com.hm.config.excel.templaextra.Active6yearStageImpl;
import com.hm.enums.RankType;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;
import org.springframework.data.util.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config ( "Active97Config" )
public class Active97Config extends ExcleConfig{
	//
    private Map<Integer,Active6yearCircleTemplateImpl> circleMap = Maps.newConcurrentMap();
    //期数，计费点id，礼包信息
    private Table<Integer, Integer, Active6yearGiftTemplate> giftTable = HashBasedTable.create();
    private Map<Integer,Active6yearLoginTemplateImpl> loginMap = Maps.newConcurrentMap();
    private Map<Pair<Integer, Integer>, WeightMeta<Integer>> randomMap = Maps.newConcurrentMap();
    private Map<Integer,Active6yearStageImpl> stageMap = Maps.newConcurrentMap();

    private Map<Integer, Active6yearPassGiftTemplate> giftTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, Active6yearPassTemplateImpl> passTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, Active6yearPassLevelTemplate> passLevelMap = Maps.newConcurrentMap();

	@Override
	public void loadConfig() {
//		loadCircleConfig();
//		loadGiftConfig();
//		loadLoginConfig();
//        loadStageConfig();
//        loadGiftTemplate();
//        loadPassTemplateMap();
//        loadPassLevel();
	}

	@Override
	public List<String> getDownloadFile() {
        return getConfigName(Active6yearCircleTemplateImpl.class,
                Active6yearGiftTemplate.class,
                Active6yearLoginTemplateImpl.class,
                Active6yearRechargeDoubleTemplate.class,
                Active6yearStageImpl.class,
                Active6yearPassTemplateImpl.class,
                Active6yearPassGiftTemplate.class,
                Active6yearPassLevelTemplate.class);
	}

	private void loadCircleConfig() {
        List<Active6yearCircleTemplateImpl> list = JSONUtil.fromJson(getJson(Active6yearCircleTemplateImpl.class), new TypeReference<List<Active6yearCircleTemplateImpl>>() {});
        list.forEach(e->{
        	e.init();
        });
        Map<Integer, Active6yearCircleTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active6yearCircleTemplateImpl::getId, Function.identity()));
        circleMap = ImmutableMap.copyOf(tempMap);
        
        Map<Pair<Integer, Integer>, List<Active6yearCircleTemplateImpl>> map = list.stream().collect(Collectors.groupingBy(Active6yearCircleTemplateImpl::genGroupKey));
        Map<Pair<Integer, Integer>, WeightMeta<Integer>> randomMapTemp = Maps.newConcurrentMap();
        for (Map.Entry<Pair<Integer, Integer>, List<Active6yearCircleTemplateImpl>> entry : map.entrySet()) {
            // id:weight
            Map<Integer, Integer> wm = entry.getValue().stream().collect(Collectors.toMap(Active6yearCircleTemplateImpl::getId, Active6yearCircleTemplateImpl::getWeight));
            WeightMeta<Integer> weightMeta = RandomUtils.buildWeightMeta(wm);
            randomMapTemp.put(entry.getKey(), weightMeta);
        }
        randomMap = ImmutableMap.copyOf(randomMapTemp);   
    }
	private void loadGiftConfig() {
        List<Active6yearGiftTemplate> list = JSONUtil.fromJson(getJson(Active6yearGiftTemplate.class), new TypeReference<List<Active6yearGiftTemplate>>() {});
        Table<Integer, Integer, Active6yearGiftTemplate> tempGiftTable = HashBasedTable.create();
        list.forEach(e->{
            tempGiftTable.put(e.getStage(), e.getRecharge_gift_id(), e);
        });
        giftTable = ImmutableTable.copyOf(tempGiftTable);
    }
	private void loadLoginConfig() {
        List<Active6yearLoginTemplateImpl> list = JSONUtil.fromJson(getJson(Active6yearLoginTemplateImpl.class), new TypeReference<List<Active6yearLoginTemplateImpl>>() {});
        list.forEach(e->{
        	e.init();
        });
        Map<Integer, Active6yearLoginTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active6yearLoginTemplateImpl::getId, Function.identity()));
        loginMap = ImmutableMap.copyOf(tempMap);
    }
    private void loadStageConfig() {
        List<Active6yearStageImpl> list = JSONUtil.fromJson(getJson(Active6yearStageImpl.class), new TypeReference<List<Active6yearStageImpl>>() {});
        list.forEach(e->{
            e.init();
        });
        Map<Integer, Active6yearStageImpl> tempMap = list.stream().collect(Collectors.toMap(Active6yearStageImpl::getId, Function.identity()));
        stageMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadGiftTemplate() {
        List<Active6yearPassGiftTemplate> list = JSONUtil.fromJson(getJson(Active6yearPassGiftTemplate.class), new TypeReference<List<Active6yearPassGiftTemplate>>() {});
        Map<Integer, Active6yearPassGiftTemplate> collect = list.stream().collect(Collectors.toMap(Active6yearPassGiftTemplate::getRecharge_gift_id, Function.identity()));
        giftTemplateMap = ImmutableMap.copyOf(collect);
    }

    private void loadPassTemplateMap() {
        List<Active6yearPassTemplateImpl> list = JSONUtil.fromJson(getJson(Active6yearPassTemplateImpl.class), new TypeReference<List<Active6yearPassTemplateImpl>>() {});
        list.forEach(e->{
            e.init();
        });
        Map<Integer, Active6yearPassTemplateImpl> collect = list.stream().collect(Collectors.toMap(Active6yearPassTemplateImpl::getId, Function.identity()));
        passTemplateMap = ImmutableMap.copyOf(collect);
    }

    private void loadPassLevel() {
        List<Active6yearPassLevelTemplate> list = JSONUtil.fromJson(getJson(Active6yearPassLevelTemplate.class), new TypeReference<List<Active6yearPassLevelTemplate>>(){});
        Map<Integer,Active6yearPassLevelTemplate> giftMap = list.stream().collect(Collectors.toMap(Active6yearPassLevelTemplate::getId, e->e));
        this.passLevelMap = ImmutableMap.copyOf(giftMap);
    }
	
	//根据玩家等级和天数找出对应的template
  	public Active6yearLoginTemplateImpl getLoginTemp(int playerLv, int day, int stage) {
        Optional<Active6yearLoginTemplateImpl> optional = loginMap.values().stream()
                .filter(t -> playerLv >= t.getLv_down()
                        && playerLv <= t.getLv_up()
                        && day == t.getDay()
                        && stage == t.getStage()).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Active6yearLoginTemplateImpl getLoginTemp(int id) {
        return loginMap.get(id);
    }

    public Active6yearCircleTemplateImpl getRandomCycle(int serverLv) {
        for (Pair<Integer, Integer> pair : randomMap.keySet()) {
            if (pair.getFirst() <= serverLv && serverLv <= pair.getSecond()) {
                Integer id = randomMap.get(pair).random();
                return circleMap.getOrDefault(id, null);
            }
        }
        return null;
    }

	public Active6yearGiftTemplate getGiftid(Integer id, int stage) {
		return giftTable.get(stage, id);
	}

	public Active6yearStageImpl getStageMsg(int stage) {
	    return this.stageMap.getOrDefault(stage, null);
    }

    public RankType getRankType(int stage, int day) {
        Active6yearStageImpl stageTemplate = this.getStageMsg(stage);
        return RankType.getTypeByIndex(stageTemplate.getRankType(day));
    }

    /**
     * 获取单个的，节日礼包奖励
     * @param stage
     * @param id
     * @return
     */
    public Active6yearPassTemplateImpl getPassTemplate(int stage, int id) {
        Active6yearPassTemplateImpl passTemplate = passTemplateMap.values().stream()
                .filter(e -> e.getStage() == stage && e.getId() == id).findFirst().orElse(null);
        return passTemplate;
    }

    public List<Active6yearPassTemplateImpl> receiveAllPassTemp(int stage, int level){
        List<Active6yearPassTemplateImpl> collect = passTemplateMap.values().stream()
                .filter(e ->e.getStage() == stage && level >= e.getPass_level()).collect(Collectors.toList());
        return collect;
    }

    public Active6yearPassGiftTemplate getPassGiftTemplate(int giftId) {
        return giftTemplateMap.getOrDefault(giftId, null);
    }

    /**
     * 根据经验，后去等级信息
     */
    public int getPassLv(int exp) {
        List<Integer> ids = passLevelMap.values().stream().filter(e->exp>=e.getFlowers_total()).map(e->e.getId()).collect(Collectors.toList());
        return Collections.max(ids);
    }
}





