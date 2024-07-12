package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active202GiftTemplate;
import com.hm.config.excel.templaextra.Active202ItemLibraryImpl;

import java.util.List;
import java.util.Map;

@Config
public class Activity202Config extends ExcleConfig {

	//新年礼包，购买（rechargeid，obj）
    private Map<Integer, Active202GiftTemplate> act202GiftMap = Maps.newConcurrentMap();
	
	//新年礼包，解锁奖池
    private Map<Integer, Active202ItemLibraryImpl> act202LibraryMap = Maps.newConcurrentMap();
    private List<Active202ItemLibraryImpl> act202LibraryList = Lists.newArrayList();

    
    public Active202GiftTemplate getAct202Gift(int rechargeId) {
    	return act202GiftMap.getOrDefault(rechargeId, null);
    }
    
    public Active202ItemLibraryImpl getAct202Library(int id) {
    	return act202LibraryMap.getOrDefault(id, null);
    }
    
	@Override
	public void loadConfig() {
	/*	Map<Integer, Active202GiftTemplate> tempAct202Gift = Maps.newConcurrentMap();
        List<Active202GiftTemplate> tempList1 = JSONUtil.fromJson(getJson(Active202GiftTemplate.class), new TypeReference<List<Active202GiftTemplate>>() {
        });
        tempList1.forEach(e -> {
        	tempAct202Gift.put(e.getRecharge_id(), e);
        });
        act202GiftMap = ImmutableMap.copyOf(tempAct202Gift);
        
		Map<Integer, Active202ItemLibraryImpl> tempAct202YearLibrary = Maps.newConcurrentMap();
		List<Active202ItemLibraryImpl> tempAct202LibraryList = Lists.newArrayList();
        List<Active202ItemLibraryImpl> tempList3 = JSONUtil.fromJson(getJson(Active202ItemLibraryImpl.class), new TypeReference<List<Active202ItemLibraryImpl>>() {
        });
        tempList3.forEach(e -> {
        	e.init();
        	tempAct202YearLibrary.put(e.getId(), e);
        	tempAct202LibraryList.add(e);
        });
        act202LibraryMap = ImmutableMap.copyOf(tempAct202YearLibrary);
        act202LibraryList = ImmutableList.copyOf(tempAct202LibraryList);*/
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(Active202GiftTemplate.class,
				Active202ItemLibraryImpl.class
        );
	}

	
	public List<Integer> getUnlock(int score, int lv) {
		List<Integer> result = Lists.newArrayList();
		act202LibraryList.forEach(e->{
			if(e.getPlayer_lv_down()<=lv && e.getPlayer_lv_up()>=lv 
					&& e.getPoints()>0 && score>=e.getPoints()) {
				result.add(e.getId());
			}
		});
		return result;
	}
	
	public List<Integer> getLock(int lv) {
		List<Integer> result = Lists.newArrayList();
		act202LibraryList.forEach(e->{
			if(e.getPlayer_lv_down()<=lv && e.getPlayer_lv_up()>=lv && e.getPoints()>0 ) {
				result.add(e.getId());
			}
		});
		return result;
	}
	//校验奖励是否在奖励池呢
	public boolean checkCellReward(List<Integer> rewardList, int lv, List<Integer> list) {
		for(int temp: rewardList) {
			Active202ItemLibraryImpl library = act202LibraryMap.get(temp);
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
		act202LibraryList.forEach(e->{
			if(e.getPoints()==0 && e.getPlayer_lv_down()<=lv && e.getPlayer_lv_up()>=lv) {
				result.add(e.getId());
			}
		});
		return result;
	}
	
	public List<Active202ItemLibraryImpl> getItem(List<Integer> cardList) {
		List<Active202ItemLibraryImpl> result = Lists.newArrayList();
		act202LibraryList.forEach(e->{
			if(cardList.contains(e.getId())) {
				result.add(e);
			}
		});
		return result;
	}
}
