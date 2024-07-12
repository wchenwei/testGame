package com.hm.model.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.battle.vo.TankSimple;
import com.hm.config.LevelEventConfig;
import com.hm.config.excel.templaextra.LevelEventNewTemplate;
import com.hm.config.excel.templaextra.LevelEventRewardNewTemplate;
import com.hm.enums.LevelEventTargetType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LevelEventNew {
	private int warId;
	private List<Integer> ids = Lists.newArrayList();//本次已经打过的点
	private List<Integer> historyIds = Lists.newArrayList();//历史打过的点
	private List<Integer> lines = Lists.newArrayList();//已经占领的路线
	private int nowId;//当前所在地点id
	private int steps;//步数
	private int limitSteps;//历史占领所有线的最小步数
	private Map<Integer,Integer> starMap = Maps.newConcurrentMap();
	private int resetCount;//重置次数
	//玩家自己的坦克
	private ConcurrentHashMap<Integer, TankSimple> myTanks = new ConcurrentHashMap<Integer, TankSimple>();
	private List<Integer> receivedIds = Lists.newArrayList();
	private int buffId;//buff
	private int supportCount;//支援次数
	private int[] events = new int[]{0, 0, 0};
	private int choice = -1;//选择的事件
	
	
	public LevelEventNew() {
		super();
	}

	public LevelEventNew(int warId) {
		super();
		this.warId = warId;
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		//把玩家当前点设置为起点
		//TODO 不设置起点
		//this.nowId = levelEventConfig.getNewStartId(warId);
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
	
	public List<TankSimple> getTanks(){
		return Lists.newArrayList(myTanks.values());
	}
	
	public void clearance(int id){
		this.ids.add(id);
		if(!this.historyIds.contains(id)){
			this.historyIds.add(id);
		}
		//updateStar(id, star);
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
	public void updateMyTank(TankSimple tankSimple) {
		this.myTanks.put(tankSimple.getId(), tankSimple);
	}
	//是否能攻打该关
	public boolean isCanFight(int id) {
		if(ids.contains(id)){
			return false;
		}
		if(nowId==0){
			return false;
		}
		//部队是否站在该点上或者是玩家所在点的下一个点
		if (nowId == id) {
			return true;
		}
		;
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		//部队所在地和该关卡是否连通
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(nowId);
		if (template == null) {
			return false;
		}
		return template.isNextId(id);
	}
	public void reset() {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		//把玩家当前点设置为起点
		//TODO 重置把起点设置为0
		//this.nowId = levelEventConfig.getNewStartId(warId);
		this.nowId = 0;
		this.resetCount ++;
		this.ids.clear();
		this.myTanks.clear();
		this.lines.clear();
		this.steps = 0;
		this.buffId = 0;
		this.events = new int[]{0, 0, 0};
		this.choice = -1;
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
		LevelEventRewardNewTemplate template = levelEventConfig.getRewardNew(id);
		if(template==null){
			return false;
		}
		//根据配置获取奖励类型
		LevelEventTargetType targetType = LevelEventTargetType.getType(template.getRewardType());
		int needNum = template.getNeedNum();
		switch(targetType){
			case TargetReward:
				//根据配置获取该事件的目标点id
				return this.historyIds.contains(needNum);
			case LimitStep:
				//根据配置获取全部路线数
				//int allLineNum = levelEventConfig.getAllLineNum(template.getWar_id());
				//X步内占领全部路线
				//return this.limitSteps<=needNum&&lines.size()>=allLineNum;
				return this.limitSteps > 0 && this.limitSteps <= needNum;
			case ProgressReward:
				int allCityNum = levelEventConfig.getAllCityNum(template.getWar_id());
				return this.historyIds.size()>=allCityNum;
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
	//占领这条线
	public void occupyLine(int id) {
		if(id>0&&!this.lines.contains(id)) {
			this.lines.add(id);
			
		}
	}
	//检查所有线是否都被占领
	public boolean checkAllLine() {
		LevelEventConfig levelEventConfig = SpringUtil.getBean(LevelEventConfig.class);
		int lineNum = levelEventConfig.getAllLineNum(this.warId);
		return this.lines.size()>=lineNum;
	}

	//派遣到该点
	public void dispatch(int id) {
		this.nowId = id;
	}
	//增加步数
	public void addSteps() {
		this.steps++;
		if(checkAllLine()) {
			this.limitSteps = this.limitSteps==0?this.steps:Math.min(this.steps, this.limitSteps);
		}
	}

	public int getSteps() {
		return steps;
	}

	public void addBuff(int id) {
		this.buffId = id;
	}

	public int getBuffId() {
		return buffId;
	}

	public void addSupportCount() {
		this.supportCount++;
	}

	public int getSupportCount() {
		return supportCount;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public List<Integer> getLines() {
		return lines;
	}

	public void clearBuff() {
		this.buffId = 0;
	}

	public void createEvents(int[] events) {
		this.events = events;
		this.choice = -1;
	}

	public int[] getEvents() {
		return events;
	}

	//是否有进行中的事件还未结束
	public boolean isEventRunning() {
		return Arrays.stream(events).allMatch(t -> t > 0) && choice < 0;
	}

	public void choice(int choice) {
		this.choice = choice;
	}
}
