package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.ActiveYearCardTemplate;
import com.hm.config.excel.temlate.ActiveYearGiftTemplate;
import com.hm.config.excel.templaextra.ActiveYearItemLibraryImpl;
import com.hm.config.excel.templaextra.ActiveYearProgressTemplateImpl;
import com.hm.config.excel.templaextra.ActiveYearSignTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.task.ActivityTask;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
public class Activity69Config extends ExcleConfig {
	
	//新年礼包，购买（rechargeid，obj）
    private Map<Integer, ActiveYearGiftTemplate> newYearGiftMap = Maps.newConcurrentMap();
	
	//新年礼包，解锁特权卡
    private Map<Integer, ActiveYearCardTemplate> newYearCardMap = Maps.newConcurrentMap();
    private List<ActiveYearCardTemplate> newYearCardList = Lists.newArrayList();
    
	//新年礼包，解锁奖池
    private Map<Integer, ActiveYearItemLibraryImpl> newYearLibraryMap = Maps.newConcurrentMap();
    private List<ActiveYearItemLibraryImpl> newYearLibraryList = Lists.newArrayList();

	private Map<Integer, ActiveYearSignTemplateImpl> loginMap = Maps.newConcurrentMap();

	private List<ActiveYearProgressTemplateImpl> yearProgress = Lists.newArrayList();

    public ActiveYearGiftTemplate getYearGift(int rechargeId) {
    	return newYearGiftMap.getOrDefault(rechargeId, null);
    }
    
    public ActiveYearCardTemplate getYearCard(int id) {
    	return newYearCardMap.getOrDefault(id, null);
    }
    
    public ActiveYearItemLibraryImpl getLibrary(int id) {
    	return newYearLibraryMap.getOrDefault(id, null);
    }
    
	@Override
	public void loadConfig() {
    	/*this.loadYearSignConfig();
    	this.loadYearProgressConfig();

		Map<Integer, ActiveYearGiftTemplate> tempnewYearGift = Maps.newConcurrentMap();
        List<ActiveYearGiftTemplate> tempList1 = JSONUtil.fromJson(getJson(ActiveYearGiftTemplate.class), new TypeReference<List<ActiveYearGiftTemplate>>() {
        });
        tempList1.forEach(e -> {
        	tempnewYearGift.put(e.getRecharge_id(), e);
        });
        newYearGiftMap = ImmutableMap.copyOf(tempnewYearGift);
        
        List<ActiveYearCardTemplate> tempYearCardLibrary = Lists.newArrayList();
		Map<Integer, ActiveYearCardTemplate> tempnewYearCard = Maps.newConcurrentMap();
        List<ActiveYearCardTemplate> tempList2 = JSONUtil.fromJson(getJson(ActiveYearCardTemplate.class), new TypeReference<List<ActiveYearCardTemplate>>() {
        });
        tempList2.forEach(e -> {
        	tempnewYearCard.put(e.getId(), e);
        	tempYearCardLibrary.add(e);
        });
        newYearCardMap = ImmutableMap.copyOf(tempnewYearCard);
        newYearCardList = ImmutableList.copyOf(tempYearCardLibrary);
        
		Map<Integer, ActiveYearItemLibraryImpl> tempnewYearLibrary = Maps.newConcurrentMap();
		List<ActiveYearItemLibraryImpl> tempYearLibraryList = Lists.newArrayList();
        List<ActiveYearItemLibraryImpl> tempList3 = JSONUtil.fromJson(getJson(ActiveYearItemLibraryImpl.class), new TypeReference<List<ActiveYearItemLibraryImpl>>() {
        });
        tempList3.forEach(e -> {
        	e.init();
        	tempnewYearLibrary.put(e.getId(), e);
        	tempYearLibraryList.add(e);
        });
        newYearLibraryMap = ImmutableMap.copyOf(tempnewYearLibrary);
        newYearLibraryList = ImmutableList.copyOf(tempYearLibraryList);*/
	}



	@Override
	public List<String> getDownloadFile() {
		return getConfigName(ActiveYearGiftTemplate.class,
				ActiveYearCardTemplate.class,
				ActiveYearItemLibraryImpl.class,
				ActiveYearSignTemplateImpl.class,
				ActiveYearProgressTemplateImpl.class
        );
	}

	public List<Integer> getUnlock(int score, int lv) {
		List<Integer> result = Lists.newArrayList();
		newYearLibraryList.forEach(e->{
			if(e.getPlayer_lv_down()<=lv && e.getPlayer_lv_up()>=lv 
					&& e.getPoints()>0 && score>=e.getPoints()) {
				result.add(e.getId());
			}
		});
		return result;
	}
	
	public List<Integer> getLock(int lv) {
		List<Integer> result = Lists.newArrayList();
		newYearLibraryList.forEach(e->{
			if(e.getPlayer_lv_down()<=lv && e.getPlayer_lv_up()>=lv && e.getPoints()>0 ) {
				result.add(e.getId());
			}
		});
		return result;
	}
	//校验奖励是否在奖励池呢
	public boolean checkCellReward(List<Integer> rewardList, int lv, List<Integer> list) {
		for(int temp: rewardList) {
			ActiveYearItemLibraryImpl library = newYearLibraryMap.get(temp);
			if(library.getPlayer_lv_down()>lv || library.getPlayer_lv_up()<lv) {
				return false;
			}
			if(library.getPoints()>0 && !list.contains(library.getId())) {
				return false;
			}
		}
		return true;
	}

	public List<Integer> getDefaultUnlock(int lv) {
		List<Integer> result = Lists.newArrayList();
		newYearLibraryList.forEach(e->{
			if(e.getPoints()==0 && e.getPlayer_lv_down()<=lv && e.getPlayer_lv_up()>=lv) {
				result.add(e.getId());
			}
		});
		return result;
	}
	
	public List<ActiveYearItemLibraryImpl> getYearItem(List<Integer> cardList) {
		List<ActiveYearItemLibraryImpl> result = Lists.newArrayList();
		newYearLibraryList.forEach(e->{
			if(cardList.contains(e.getId())) {
				result.add(e);
			}
		});
		return result;
	}
	
	//判断元旦任务是否已经完成
	private boolean isOpen(ActiveYearCardTemplate yearCard, Map<Integer, ActivityTask> tasks) {
		int tempYearCardId = yearCard.getCard_limit();
		int times = 0; 	
		while(null!=newYearCardMap.get(tempYearCardId)) {
			ActiveYearCardTemplate tempYearCard = newYearCardMap.get(tempYearCardId);
			ActivityTask task2 = tasks.get(Integer.parseInt(tempYearCard.getItem_unlock()));
			if(null!=task2 && task2.isComplete()) {
				if(tempYearCard.getCard_limit()!=0) {
					tempYearCardId = tempYearCard.getCard_limit();
				}else {
					return true;
				}
			}else {
				return false;
			}
			//防止死循环
			times++;
			if(times>100) {
				return false;
			}
		}
		return false;
	}

	private void loadYearProgressConfig() {
		List<ActiveYearProgressTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveYearProgressTemplateImpl.class), new TypeReference<List<ActiveYearProgressTemplateImpl>>() {});
		list.forEach(e->{
			e.init();
		});
		yearProgress = ImmutableList.copyOf(list);
	}

	private void loadYearSignConfig() {
		List<ActiveYearSignTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveYearSignTemplateImpl.class), new TypeReference<List<ActiveYearSignTemplateImpl>>() {});
		list.forEach(e->{
			e.init();
		});
		Map<Integer, ActiveYearSignTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveYearSignTemplateImpl::getId, Function.identity()));
		loginMap = ImmutableMap.copyOf(tempMap);
	}

	//根据玩家等级和天数找出对应的template
	public ActiveYearSignTemplateImpl getLoginTemp(int playerLv, int day) {
		ActiveYearSignTemplateImpl activeYearSignTemplate = loginMap.values().stream().filter(t ->t.checkLv(playerLv) && day==t.getDay()).findFirst().orElse(null);
		return activeYearSignTemplate;
	}

	public ActiveYearProgressTemplateImpl getYearProgress(int playerLv, int id){
		ActiveYearProgressTemplateImpl activeYearProgressTemplate = yearProgress.stream().filter(e -> e.getId() == id && e.isFit(playerLv)).findFirst().orElse(null);
		return activeYearProgressTemplate;
	}
}
