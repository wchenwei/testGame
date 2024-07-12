package com.hm.config.excel;
import com.google.common.collect.*;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ArenaRankGoldExTemplate;
import com.hm.config.excel.templaextra.ArenaRefreshExTemplate;
import com.hm.config.excel.templaextra.EnemyArenaExTemplate;
import com.hm.model.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.common.collect.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞技场配置
 * ClassName: ArenaConfig. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月17日 下午2:12:21 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
@Slf4j
@Config
public class ArenaConfig extends ExcleConfig{
	private Map<Integer, EnemyArenaExTemplate> arenaNpcMap = Maps.newHashMap(); 
	private List<ArenaRefreshExTemplate> refreshList = Lists.newArrayList(); 
	private List<ArenaRankGoldExTemplate> rankRewardList = Lists.newArrayList();
	// level:List<id>
	private ListMultimap<Integer, Integer> trumpNpcBaseMap = ArrayListMultimap.create();

	@Override
	public void loadConfig() {
		loadArenaNpc();
		loadArenaRefresh();
		loadArenaReward();
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(EnemyArenaExTemplate.class,ArenaRefreshExTemplate.class,ArenaRankGoldExTemplate.class);
	}
	
	public void loadArenaNpc() {
		Map<Integer, EnemyArenaExTemplate> arenaNpcMap = Maps.newHashMap();
        List<EnemyArenaExTemplate> templateList= JSONUtil.fromJson(getJson(EnemyArenaExTemplate.class), new TypeReference<ArrayList<EnemyArenaExTemplate>>() {});
        templateList.forEach(temp->{
        	temp.init();
        	arenaNpcMap.put(temp.getId(), temp);
        });
        
        this.arenaNpcMap = ImmutableMap.copyOf(arenaNpcMap);

		// level:List<id>
		ListMultimap<Integer, Integer> tempMap = ArrayListMultimap.create();
		for (EnemyArenaExTemplate template : arenaNpcMap.values()) {
			if (template.getType() != 3) {
				continue;
			}
			tempMap.put(template.getLevel(), template.getId());
		}

		trumpNpcBaseMap = ImmutableListMultimap.copyOf(tempMap);
		log.info("竞技场NPC配置加载完成");
	}
	
	public void loadArenaRefresh(){
		List<ArenaRefreshExTemplate> refreshList= JSONUtil.fromJson(getJson(ArenaRefreshExTemplate.class), new TypeReference<ArrayList<ArenaRefreshExTemplate>>() {});
		refreshList.forEach(t->t.init());
		this.refreshList = ImmutableList.copyOf(refreshList);
		log.info("竞技场刷新配置加载完成");
	}
	
	public void loadArenaReward(){
		List<ArenaRankGoldExTemplate> rankRewardList = JSONUtil.fromJson(getJson(ArenaRankGoldExTemplate.class), new TypeReference<ArrayList<ArenaRankGoldExTemplate>>() {});
		rankRewardList.forEach(t->t.init());
		this.rankRewardList = ImmutableList.copyOf(rankRewardList);
		log.info("竞技场最高排名奖励配置加载完成");
	}
	
	public EnemyArenaExTemplate getArenaNpc(int id){
		return arenaNpcMap.getOrDefault(id, null);
	}

	/**
	 * 根据npc 等级，随机一个npc id
	 *
	 * @param level
	 * @return -1 如果没找到的话
	 */
	public int getTrumpNpcInfo(int level) {
		List<Integer> npcIdList = trumpNpcBaseMap.get(level);
		if (npcIdList.isEmpty()) {
			return -1;
		}
		return RandomUtil.randomEle(npcIdList);
	}

	public List<Integer> getEnemyNpcIdList(int level) {
		return trumpNpcBaseMap.get(level);
	}
	/**
     *
	 * @param type 策划定义2:竞技场,3:王牌演习npc
	 * @return
	 */
	public int getArenaNpcSize(int type){
		return (int) arenaNpcMap.values().stream().filter(c -> c.getType() == type).count();
	}

	public List<ArenaRefreshExTemplate> getRefreshList() {
		return refreshList;
	}

	public ArenaRefreshExTemplate getLastArenaRefreshTemplate(){
		return refreshList.get(refreshList.size()-1);
	}

	public List<ArenaRankGoldExTemplate> getRankRewardList() {
		return rankRewardList;
	}

	/***
	 * 合并排名奖励(包括minRank,不包括maxRank)
	 *
	 * @author yanpeng 
	 * @param minRank
	 * @param maxRank
	 * @return  
	 *
	 */
	public List<Items> getRankReward(long minRank,long maxRank){
		List<Items> rewardList = Lists.newArrayList(); 
		
		Optional<ArenaRankGoldExTemplate> opt = rankRewardList.stream()
				.filter(t -> t.getRank()>=minRank).findFirst();
		if(!opt.isPresent()){
			return rewardList; 
		}
		
		int minId = opt.get().getId(); 
		int maxId = rankRewardList.get(rankRewardList.size()-1).getId(); 
		
		opt = rankRewardList.stream().filter(t -> t.getRank()>=maxRank).findFirst();
		if(opt.isPresent()){
			maxId = opt.get().getId();
		}
		
		Table<Integer, Integer, Items> itemTable = HashBasedTable.create();
		for(int id =minId;id<maxId;id++){
			List<Items> rewards = rankRewardList.get(id).getItemList(); 
			rewards.forEach(items -> {
				if(itemTable.contains(items.getItemType(), items.getId())) {
					Items reward = items.clone();
					reward.addCount(itemTable.get(reward.getItemType(), reward.getId()).getCount());
					itemTable.put(items.getItemType(), items.getId(), reward);
				}else {
					itemTable.put(items.getItemType(), items.getId(), items);
				}
			});
		}
		rewardList = Lists.newArrayList(itemTable.values());
		return rewardList; 
	}
	
	
}
