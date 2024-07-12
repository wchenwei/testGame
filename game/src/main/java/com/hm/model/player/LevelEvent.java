package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.battle.vo.TankSimple;
import com.hm.config.LevelEventConfig;
import com.hm.config.excel.templaextra.LevelEventRewardTemplate;
import com.hm.config.excel.templaextra.LevelEventTemplate;
import com.hm.enums.LevelEventRewardType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LevelEvent {
	private int warId;
	private List<Integer> ids = Lists.newArrayList();//本次已经打过的点
	private List<Integer> historyIds = Lists.newArrayList();//历史打过的点
	private int nowId;//当前所在地点id
	private Map<Integer,Integer> starMap = Maps.newConcurrentMap();
	private int resetCount;//重置次数
	//玩家自己的坦克
	private ConcurrentHashMap<Integer, TankSimple> myTanks = new ConcurrentHashMap<Integer, TankSimple>();
	private List<Integer> receivedIds = Lists.newArrayList();
	
	
	public LevelEvent() {
		super();
	}

	public LevelEvent(int warId) {
		super();
		this.warId = warId;
	}

	public int getNowId() {
		return nowId;
	}
	
	public int getResetCount() {
		return resetCount;
	}

	public int getWarId() {
		return warId;
	}

	public TankSimple getTank(int id){
		return myTanks.get(id);
	}
	public void clearance(int id,int star,String result){
		this.nowId = id;
		this.ids.add(id);
		if(!this.historyIds.contains(id)){
			this.historyIds.add(id);
		}
		updateStar(id, star);
	}
	public void updateStar(int id,int star){
		int oldStar = starMap.getOrDefault(id, 0);
		if(star>oldStar){
			starMap.put(id, star);
		}
	}
	public boolean isFighted(int id){
		return this.historyIds.contains(id);
	}
	//更新自己的坦克信息
	public void updateMyTank(int id,String result){
		List<String> myTanks = StringUtil.splitStr2StrList(result, ",");
		//同步自己的血量
		myTanks.forEach(tankStr ->{
			List<Integer> list = StringUtil.splitStr2IntegerList(tankStr, ":");
			updateMyTank(new TankSimple(list.get(0),list.get(1),list.get(2)));
		});
	}
	private void updateMyTank(TankSimple tankSimple) {
		this.myTanks.put(tankSimple.getId(), tankSimple);
	}
	//是否能攻打该关
	public boolean isCanFight(int id) {
		if(ids.contains(id)){
			return false;
		}
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		if(nowId==0){//如果还未攻打则判断是否是第一关
			LevelEventTemplate template = levelEventConfig.getLevelEvent(id);
			int startId = levelEventConfig.getStartId(template.getWar_id());
			return id == startId;
		}
		//部队所在地和该关卡是否连通
		LevelEventTemplate template = levelEventConfig.getLevelEvent(nowId);
		if(template==null){
			return false;
		}
		return template.isNextId(id);
	}
	public void reset() {
		this.nowId = 0;
		this.resetCount ++;
		this.ids.clear();
		this.myTanks.clear();
	}
	public int getStarTotal(){
		return this.starMap.values().stream().mapToInt(Integer::intValue).sum();
	}
	//是否满足领取条件
	public boolean canReceive(int id) {
		if(this.receivedIds.contains(id)){
			//已经领取过
			return false;
		}
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		LevelEventRewardTemplate template = levelEventConfig.getReward(id);
		if(template==null){
			return false;
		}
		//根据配置获取奖励类型
		LevelEventRewardType rewardType = LevelEventRewardType.getType(template.getRewardType());
		int needNum = template.getNeedNum();
		switch(rewardType){
			case TargetReward:
				//根据配置获取该事件的目标点id
				return this.historyIds.contains(needNum);
			case StarFullReward:
				//根据配置获取该事件的满星数
				return getStarTotal()>=needNum;
			case ProgressReward:
				return this.historyIds.size()>=needNum;
		}
		return false;
	}
	public void receive(int id) {
		if(!this.receivedIds.contains(id)){
			this.receivedIds.add(id);
		}
	}
	public boolean isEnd() {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
        List<Integer> allRewardIds = levelEventConfig.getNewRewardByWarId(this.warId);
		List<Integer> temp = Lists.newArrayList(allRewardIds);
		for(int i=receivedIds.size()-1;i>=0;i--){
			int id = receivedIds.get(i);
			if(temp.contains(id)){
				temp.remove(new Integer(id));
			}
		}
		return temp.isEmpty();
	}

    public boolean isEndOld() {
        LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
        List<Integer> allRewardIds = levelEventConfig.getRewardByWarId(this.warId);
        List<Integer> temp = Lists.newArrayList(allRewardIds);
        for (int i = receivedIds.size() - 1; i >= 0; i--) {
            int id = receivedIds.get(i);
            if (temp.contains(id)) {
                temp.remove(new Integer(id));
            }
        }
        return temp.isEmpty();
    }

	public List<Integer> getReceivedIds() {
		return receivedIds;
	}
	
	
}
