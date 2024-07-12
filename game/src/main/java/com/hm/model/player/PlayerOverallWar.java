package com.hm.model.player;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.OverallWarConfig;
import com.hm.config.excel.temlate.TotalWarMatchNewTemplate;
import com.hm.enums.OverallWarTroopType;
import com.hm.enums.RankType;
import com.hm.enums.ServerFunctionType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardBiz;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerFunction;
import com.hm.model.tank.Tank;
import com.hm.war.sg.troop.TankArmy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerOverallWar extends PlayerDataContext {
	//防守阵容
	private ConcurrentHashMap<Integer, List<TankArmy>> myTanks = new ConcurrentHashMap<Integer, List<TankArmy>>();
	
	private ConcurrentHashMap<Integer,Integer> airFormationIds = new ConcurrentHashMap<Integer, Integer>();
	//最大连胜次数
	private int maxContinueWin;
	//连胜次数
	private int continueWin;
	//分数
	private int score;
	//战斗次数
	private int fightNum;
	private int winNum;
	private List<Integer> rewardIds = Lists.newArrayList();
	
	private String opponentId;
	private int opponentGrade;//对手档位
	private int myGrade;//我本次的档位
	private LinkedList<String> recordList = Lists.newLinkedList();
	
	public String getOpponentId() {
		return opponentId;
	}
	
	public boolean isNpc(){
		return StrUtil.isNotBlank(this.opponentId)&&opponentId.startsWith("npc_");
	}
	public int getOpponentIntId(){
		return Integer.parseInt(opponentId);
	}
	public int getNpcId(){
		return Integer.parseInt(opponentId.split("_")[1]);
	}
	public int getMaxContinueWin() {
		return maxContinueWin;
	}

	public LinkedList<String> getRecordList() {
		return recordList;
	}

	public int getContinueWin() {
		return continueWin;
	}

	public int getScore() {
		return score;
	}

	public int getOpponentGrade() {
		return opponentGrade;
	}

	public int getFightNum() {
		return fightNum;
	}

	public ConcurrentHashMap<Integer, List<TankArmy>> getMyTanks(){
		return myTanks;
	}
	
	
	public ConcurrentHashMap<Integer, Integer> getAirFormationIds() {
		return airFormationIds;
	}
	
	public int getAirFormationId(int type) {
		return airFormationIds.getOrDefault(type, 0);
	}

	public Map<Integer,ClientTroop> getClientTroops(){
		Map<Integer,ClientTroop> map = Maps.newConcurrentMap();
		for(Map.Entry<Integer, List<TankArmy>> entry: myTanks.entrySet()) {
			map.put(entry.getKey(), ClientTroop.build(entry.getValue(),getAirFormationId(entry.getKey())));
		}
		return map;
	}
	
	public int getWinNum() {
		return winNum;
	}

	public void setOpponentGrade(int opponentGrade) {
		this.opponentGrade = opponentGrade;
	}

	public void checkOverallWar(){
		ServerData serverData = ServerDataManager.getIntance().getServerData(super.Context().getServerId());
		ServerFunction serverFunction = serverData.getServerFunction();
		if(!serverFunction.isServerUnlock(ServerFunctionType.OverallWar.getType())){
			return;
		}
		if(!myTanks.isEmpty()){
			return;
		}
		if(super.Context().playerLevel().getLv()<50||super.Context().playerTank.getTankList().size()<15){
			return;
		}
		TankBiz tankBiz = SpringUtil.getBean(TankBiz.class);
		//取出战力前15的坦克
		List<Tank> tanks = tankBiz.getTankListTopCombat(super.Context(), 15);
		for(OverallWarTroopType type:OverallWarTroopType.values()){
			List<Tank> thisTanks = tanks.subList(type.getStart(), type.getEnd());
			//生成站位
			myTanks.put(type.getType(), tankBiz.createTankPos(thisTanks));
		}
		SetChanged();
		//向排行榜中插入数据
		//HdLeaderboardsService.getInstance().updatePlayerRankOverride(super.Context(), RankType.OverallWarRank, score);
	}
	
	
	public void changeTroop(int type, ArrayList<TankArmy> armyList) {
		this.myTanks.put(type, armyList);
		SetChanged();
	}
	public void changeTroop(Map<Integer,List<TankArmy>> troopMap){
		this.myTanks.putAll(troopMap);
		SetChanged();
	}
	public void changeAirFormationIds(Map<Integer,Integer> airFormationIds) {
		this.airFormationIds.putAll(airFormationIds);
		SetChanged();
	}

	public List<TankArmy> getTroop(int type) {
		return myTanks.get(type);
	}
	public void matchOpponent(String opponentId) {
		this.opponentId = opponentId;
		SetChanged();
	}
	public void calMyGrade(int grade) {
		this.myGrade = grade;
		SetChanged();
	}
	public int getMyGrade() {
		return myGrade;
	}

	//战斗结算
	public int fight(boolean result) {
		this.continueWin = result?this.continueWin+1:0;
		this.maxContinueWin = Math.max(this.continueWin, this.maxContinueWin);
		this.winNum = result?this.winNum+1:this.winNum;
		OverallWarConfig overallWarConfig = SpringUtil.getBean(OverallWarConfig.class);
		TotalWarMatchNewTemplate template = overallWarConfig.getMathchNew(this.opponentGrade);
		//胜利则获得对手档次对应积分+(连胜次数-1)*10
		int scoreAdd = result?template==null?20:template.getScore()+10*(continueWin-1):0;
		this.score += scoreAdd;
		//挑战胜利则分数+10+(连胜次数+1)
		//this.score = result?this.score+10+(continueWin-1):this.score-10;
		//清空上一局匹配信息
		this.opponentId = "";
		this.fightNum++;
		LeaderboardBiz leaderboardBiz = SpringUtil.getBean(LeaderboardBiz.class);
		if(result){
            double finalScore = leaderboardBiz.getFinalScore(this.score, super.Context().getPlayerDynamicData().getCombat()/1000, 100000000, 8,true);
            HdLeaderboardsService.getInstance().updatePlayerRankOverride(super.Context(), RankType.OverallWarRank, finalScore);
        }
		//生成下一局对手档次
		calNextOpponentGrade(result);
		SetChanged();
		return scoreAdd;
	}
	
	public void calNextOpponentGrade(boolean result){
		OverallWarConfig overallWarConfig = SpringUtil.getBean(OverallWarConfig.class);
		if(!result){
			//如果战斗失败，玩家处于》=6挡则从最低npc中选择，反之从最低玩家档次哦中选择
			this.opponentGrade = this.opponentGrade>=6?overallWarConfig.getMaxNpcGrade():overallWarConfig.getMaxGrade();
			System.err.println(super.Context().getId()+"获取下一关对手档位:"+opponentGrade);
			return;
		}
		//如果战斗胜利，低于自己所在档位，10%概率在当前档位中继续随机对手，90%概率从上一个档位中随机对手（第一档的玩家胜利从当前档位中随机对手）；等于或高于自己档位，30%概率在当前档位中继续随机对手，70%概率从上一个档位中随机对手（第一档的玩家胜利从当前档位中随机对手）
		/*int rate = opponentGrade>myGrade?10:30;
		int random = MathUtils.random(1, 101);
		if(random>=rate){
			//取当前挡的上一档
			this.opponentGrade = Math.max(1, this.opponentGrade-1);
		}*/
		
		//如果胜利则升档
		this.opponentGrade = Math.max(1, this.opponentGrade-1);
	}
	/**
	 * 防御战结算
	 * @param result true-进攻方胜利
	 */
	public void calDefResult(boolean result) {
		this.score = result?this.score-10:this.score+10;
		HdLeaderboardsService.getInstance().updatePlayerRankOverride(super.Context(), RankType.OverallWarRank, this.score);
		SetChanged();
	}
	public void addRecord(String recordId){
		if(!recordList.contains(recordId)){
			recordList.addFirst(recordId);
			if(recordList.size() >10){
				recordList.removeLast();
			}
			SetChanged();
		}
		
	}
	public void resetDay(){
		this.winNum = 0;
		this.maxContinueWin = 0;
		this.continueWin = 0;
		this.rewardIds.clear();
		this.score = 0;
		this.opponentId = "";
		this.opponentGrade = 0;
		this.fightNum = 0;
		SetChanged();
	}

	public boolean isReceive(int id) {
		return rewardIds.contains(id);
	}

	public void receive(int id) {
		if(!isReceive(id)){
			this.rewardIds.add(id);
			SetChanged();
		}
	}
	
	public boolean isOpenOverallWar(){
		return !this.myTanks.isEmpty();
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerOverallWar", this);
	}

	public void changeTroops(Map<Integer, ClientTroop> map) {
		Map<Integer,Integer> airFormationIds = Maps.newConcurrentMap();
		Map<Integer,List<TankArmy>> troops = Maps.newConcurrentMap();
		for(Map.Entry<Integer, ClientTroop>entry:map.entrySet()) {
			airFormationIds.put(entry.getKey(), entry.getValue().getAircraftId());
			troops.put(entry.getKey(), entry.getValue().getArmyList());
		}
		changeTroop(troops);
		changeAirFormationIds(airFormationIds);
	}

}
