package com.hm.config.excel;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.RandomTaskIntimacyTemplate;
import com.hm.config.excel.temlate.RandomTaskLibrary1Template;
import com.hm.config.excel.temlate.RandomTaskLibrary2Template;
import com.hm.config.excel.templaextra.RandomTaskConfigTemplateImpl;
import com.hm.config.excel.templaextra.RandomTaskIntimacyTemplateImpl;
import com.hm.config.excel.templaextra.RandomTaskRewardTemplateImpl;
import com.hm.model.item.Items;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-11
 *
 * @author Administrator
 */
@Config
public class RandomTaskConfig extends ExcleConfig {
    /**
     * random task type 8's id list
     * random_task_library1.xlsx's id
     */
    private List<Integer> type8List = Lists.newArrayList();
    /**
     * random task type 9's id list
     * random_task_library2.xlsx's id
     */
    private List<Integer> type9List = Lists.newArrayList();
    /**
     * id : object
     */
    private Map<Integer, RandomTaskConfigTemplateImpl> randomTaskConfigTemplateMap = Maps.newConcurrentMap();
    /**
     * RandomTaskConfigTemplateImpl::getId
     * 随机任务类型用
     */
    private WeightMeta<Integer> weightMeta;

    /**
     * random task type:object
     */
    private ListMultimap<Integer, RandomTaskRewardTemplateImpl> rewardTemplateMap = ArrayListMultimap.create();
    
    private List<RandomTaskIntimacyTemplateImpl> randomTaskBuffAttrList = Lists.newArrayList();

    @Override
    public void loadConfig() {
        List<RandomTaskLibrary1Template> t8 = JSONUtil.fromJson(getJson(RandomTaskLibrary1Template.class), new TypeReference<List<RandomTaskLibrary1Template>>() {
        });

        type8List = ImmutableList.copyOf(t8.stream().map(RandomTaskLibrary1Template::getId).collect(Collectors.toList()));

        List<RandomTaskLibrary2Template> t9 = JSONUtil.fromJson(getJson(RandomTaskLibrary2Template.class), new TypeReference<List<RandomTaskLibrary2Template>>() {
        });

        type9List = ImmutableList.copyOf(t9.stream().map(RandomTaskLibrary2Template::getId).collect(Collectors.toList()));

        List<RandomTaskConfigTemplateImpl> list = JSONUtil.fromJson(getJson(RandomTaskConfigTemplateImpl.class), new TypeReference<List<RandomTaskConfigTemplateImpl>>() {
        });

        Map<Integer, RandomTaskConfigTemplateImpl> tm = Maps.newConcurrentMap();

        // RandomTaskConfigTemplateImpl::getId, RandomTaskConfigTemplateImpl::getWeight
        Map<Integer, Integer> twm = Maps.newConcurrentMap();

        list.forEach(t -> {
            t.init();
            tm.put(t.getId(), t);
            twm.put(t.getId(), t.getWeight());
        });

        randomTaskConfigTemplateMap = ImmutableMap.copyOf(tm);
        weightMeta = RandomUtils.buildWeightMeta(twm);

        List<RandomTaskRewardTemplateImpl> reward = JSONUtil.fromJson(getJson(RandomTaskRewardTemplateImpl.class), new TypeReference<List<RandomTaskRewardTemplateImpl>>() {
        });

        ListMultimap<Integer, RandomTaskRewardTemplateImpl> tMap = ArrayListMultimap.create();
        reward.forEach(t -> {
            t.init();
            tMap.put(t.getTask_type(), t);
        });

        rewardTemplateMap = ImmutableListMultimap.copyOf(tMap);
        
        loadTaskBuffAttr();//加载属性加成
    }
    
    /**
     * 随机一个type 8 的id
     *
     * @return
     */
    public int random8() {
        return RandomUtil.randomEle(type8List);
    }

    /**
     * 随机一个type 9 的id
     *
     * @return
     */
    public int random9() {
        return RandomUtil.randomEle(type9List);
    }

    /**
     * 随机获取一个任务类型的配置基础信息
     *
     * @return
     */
    public RandomTaskConfigTemplateImpl pickARandomTask() {
        return randomTaskConfigTemplateMap.get(weightMeta.random());
    }
    public RandomTaskConfigTemplateImpl getRandomTaskConfigTemplateImpl(int type) {
        return randomTaskConfigTemplateMap.getOrDefault(type, null);
    }
    public int getIntimacy(int type){
    	RandomTaskConfigTemplateImpl template = getRandomTaskConfigTemplateImpl(type);
    	if(template==null){
    		return 0;
    	}
    	return template.getIntimacy_add();
    }

    /**
     * 获取奖励
     *
     * @param taskType 任务类型
     * @param playerLv 用户等级
     * @return
     */
    public List<Items> getRandomTaskRewardTemplateImpl(int taskType, int playerLv) {
        List<RandomTaskRewardTemplateImpl> list = rewardTemplateMap.get(taskType);
        Optional<RandomTaskRewardTemplateImpl> any = list.stream().
                filter(t -> t.getLevel_limit_down() <= playerLv && playerLv <= t.getLevel_limit_up()).findAny();
        return any.map(RandomTaskRewardTemplateImpl::getRewardItems).orElseGet(Lists::newArrayList);
    }
    
    @Override
    public List<String> getDownloadFile() {
        return getConfigName(RandomTaskConfigTemplateImpl.class, RandomTaskLibrary1Template.class, RandomTaskLibrary2Template.class,
                RandomTaskRewardTemplateImpl.class,RandomTaskIntimacyTemplate.class);
    }
    
    public void loadTaskBuffAttr() {
    	List<RandomTaskIntimacyTemplateImpl> tempList = JSONUtil.fromJson(getJson(RandomTaskIntimacyTemplateImpl.class), new TypeReference<List<RandomTaskIntimacyTemplateImpl>>() {});
    	tempList.forEach(e -> e.init());
    	this.randomTaskBuffAttrList = ImmutableList.copyOf(tempList);
    }
    /**
     * 获取亲密度属性加成
     * @param score
     * @return
     */
    public Map<Integer,Double> getEventBuffAttr(int score) {
    	RandomTaskIntimacyTemplateImpl template = null;
    	for (RandomTaskIntimacyTemplateImpl temp : randomTaskBuffAttrList) {
			if(score >= temp.getIntimacy()) {
				template = temp;
			}else{
				break;
			}
		}
    	if(template != null) {
    		return template.getAttrMap();
    	}
    	return new HashMap<>();
    }
}
