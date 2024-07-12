package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active1001GiftTemplate;
import com.hm.config.excel.temlate.Active1001PassGiftTemplate;
import com.hm.config.excel.temlate.Active1001PassLevelTemplate;
import com.hm.config.excel.templaextra.Active121CircleTemplateImpl;
import com.hm.config.excel.templaextra.Active121LoginTemplateImpl;
import com.hm.config.excel.templaextra.Active121PassTemplateImpl;
import com.hm.config.excel.templaextra.Active121StageImpl;
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
@Config ( "Active121Config" )
public class Active121Config extends ExcleConfig{
	//
    private Map<Integer, Active121CircleTemplateImpl> circleMap = Maps.newConcurrentMap();
    //期数，计费点id，礼包信息
    private Table<Integer, Integer, Active1001GiftTemplate> giftTable = HashBasedTable.create();
    private Map<Integer,Active121LoginTemplateImpl> loginMap = Maps.newConcurrentMap();
    private Map<Pair<Integer, Integer>, WeightMeta<Integer>> randomMap = Maps.newConcurrentMap();
    private Map<Integer,Active121StageImpl> stageMap = Maps.newConcurrentMap();

    private Map<Integer, Active1001PassGiftTemplate> giftTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, Active121PassTemplateImpl> passTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, Active1001PassLevelTemplate> passLevelMap = Maps.newConcurrentMap();

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
        return getConfigName(Active121CircleTemplateImpl.class,
                Active1001GiftTemplate.class,
                Active121LoginTemplateImpl.class,
                Active121StageImpl.class,
                Active121PassTemplateImpl.class,
                Active1001PassGiftTemplate.class,
                Active1001PassLevelTemplate.class);
	}

	private void loadCircleConfig() {
        List<Active121CircleTemplateImpl> list = JSONUtil.fromJson(getJson(Active121CircleTemplateImpl.class), new TypeReference<List<Active121CircleTemplateImpl>>() {});
        list.forEach(e->{
        	e.init();
        });
        Map<Integer, Active121CircleTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active121CircleTemplateImpl::getId, Function.identity()));
        circleMap = ImmutableMap.copyOf(tempMap);
        
        Map<Pair<Integer, Integer>, List<Active121CircleTemplateImpl>> map = list.stream().collect(Collectors.groupingBy(Active121CircleTemplateImpl::genGroupKey));
        Map<Pair<Integer, Integer>, WeightMeta<Integer>> randomMapTemp = Maps.newConcurrentMap();
        for (Map.Entry<Pair<Integer, Integer>, List<Active121CircleTemplateImpl>> entry : map.entrySet()) {
            // id:weight
            Map<Integer, Integer> wm = entry.getValue().stream().collect(Collectors.toMap(Active121CircleTemplateImpl::getId, Active121CircleTemplateImpl::getWeight));
            WeightMeta<Integer> weightMeta = RandomUtils.buildWeightMeta(wm);
            randomMapTemp.put(entry.getKey(), weightMeta);
        }
        randomMap = ImmutableMap.copyOf(randomMapTemp);   
    }
	private void loadGiftConfig() {
        List<Active1001GiftTemplate> list = JSONUtil.fromJson(getJson(Active1001GiftTemplate.class), new TypeReference<List<Active1001GiftTemplate>>() {});
        Table<Integer, Integer, Active1001GiftTemplate> tempGiftTable = HashBasedTable.create();
        list.forEach(e->{
            tempGiftTable.put(e.getStage(), e.getRecharge_gift_id(), e);
        });
        giftTable = ImmutableTable.copyOf(tempGiftTable);
    }
	private void loadLoginConfig() {
        List<Active121LoginTemplateImpl> list = JSONUtil.fromJson(getJson(Active121LoginTemplateImpl.class), new TypeReference<List<Active121LoginTemplateImpl>>() {});
        list.forEach(e->{
        	e.init();
        });
        Map<Integer, Active121LoginTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active121LoginTemplateImpl::getId, Function.identity()));
        loginMap = ImmutableMap.copyOf(tempMap);
    }
    private void loadStageConfig() {
        List<Active121StageImpl> list = JSONUtil.fromJson(getJson(Active121StageImpl.class), new TypeReference<List<Active121StageImpl>>() {});
        list.forEach(e->{
            e.init();
        });
        Map<Integer, Active121StageImpl> tempMap = list.stream().collect(Collectors.toMap(Active121StageImpl::getId, Function.identity()));
        stageMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadGiftTemplate() {
        List<Active1001PassGiftTemplate> list = JSONUtil.fromJson(getJson(Active1001PassGiftTemplate.class), new TypeReference<List<Active1001PassGiftTemplate>>() {});
        Map<Integer, Active1001PassGiftTemplate> collect = list.stream().collect(Collectors.toMap(Active1001PassGiftTemplate::getRecharge_gift_id, Function.identity()));
        giftTemplateMap = ImmutableMap.copyOf(collect);
    }

    private void loadPassTemplateMap() {
        List<Active121PassTemplateImpl> list = JSONUtil.fromJson(getJson(Active121PassTemplateImpl.class), new TypeReference<List<Active121PassTemplateImpl>>() {});
        list.forEach(e->{
            e.init();
        });
        Map<Integer, Active121PassTemplateImpl> collect = list.stream().collect(Collectors.toMap(Active121PassTemplateImpl::getId, Function.identity()));
        passTemplateMap = ImmutableMap.copyOf(collect);
    }

    private void loadPassLevel() {
        List<Active1001PassLevelTemplate> list = JSONUtil.fromJson(getJson(Active1001PassLevelTemplate.class), new TypeReference<List<Active1001PassLevelTemplate>>(){});
        Map<Integer,Active1001PassLevelTemplate> giftMap = list.stream().collect(Collectors.toMap(Active1001PassLevelTemplate::getId, e->e));
        this.passLevelMap = ImmutableMap.copyOf(giftMap);
    }
	
	//根据玩家等级和天数找出对应的template
  	public Active121LoginTemplateImpl getLoginTemp(int playerLv, int day, int stage) {
        Optional<Active121LoginTemplateImpl> optional = loginMap.values().stream()
                .filter(t -> playerLv >= t.getLv_down()
                        && playerLv <= t.getLv_up()
                        && day == t.getDay()
                        && stage == t.getStage()).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    public Active121LoginTemplateImpl getLoginTemp(int id) {
        return loginMap.get(id);
    }

    public Active121CircleTemplateImpl getRandomCycle(int serverLv) {
        for (Pair<Integer, Integer> pair : randomMap.keySet()) {
            if (pair.getFirst() <= serverLv && serverLv <= pair.getSecond()) {
                Integer id = randomMap.get(pair).random();
                return circleMap.getOrDefault(id, null);
            }
        }
        return null;
    }

	public Active1001GiftTemplate getGiftid(Integer id, int stage) {
		return giftTable.get(stage, id);
	}

	public Active121StageImpl getStageMsg(int stage) {
	    return this.stageMap.getOrDefault(stage, null);
    }

    public RankType getRankType(int stage, int day) {
        Active121StageImpl stageTemplate = this.getStageMsg(stage);
        return RankType.getTypeByIndex(stageTemplate.getRankType(day));
    }

    /**
     * 获取单个的，节日礼包奖励
     * @param stage
     * @param id
     * @return
     */
    public Active121PassTemplateImpl getPassTemplate(int stage, int id) {
        Active121PassTemplateImpl passTemplate = passTemplateMap.values().stream()
                .filter(e -> e.getStage() == stage && e.getId() == id).findFirst().orElse(null);
        return passTemplate;
    }

    public List<Active121PassTemplateImpl> receiveAllPassTemp(int stage, int level){
        List<Active121PassTemplateImpl> collect = passTemplateMap.values().stream()
                .filter(e ->e.getStage() == stage && level >= e.getPass_level()).collect(Collectors.toList());
        return collect;
    }

    public Active1001PassGiftTemplate getPassGiftTemplate(int giftId) {
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





