package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.battle.vo.TankSimple;
import com.hm.model.tank.Tank;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerExpedition extends PlayerDataContext {
	private int id;
	private transient Expedition expedition;
	private int resetCont;
	private int historyId;
	private boolean state;//全通标识
	//已经匹配的对手
	private transient ArrayList<Long> playerIds = Lists.newArrayList();
	//玩家自己的坦克
	private transient ConcurrentHashMap<Integer, TankSimple> map = new ConcurrentHashMap<Integer, TankSimple>();
	//已领箱子的id
	private ArrayList<Integer> receiveIds = Lists.newArrayList();
	private int buyBuffCount;//购买buff的次数
	private int fightNum;//攻击次数

	//生成对手
	public void createOpponent(Expedition expedition){
		this.historyId = Math.max(id, historyId);
		this.id++;
		if(this.id>15){
			this.id = 1;
		}
		this.expedition = expedition;
		SetChanged();
	}

	public void setId(int id) {
		this.id = id;
		SetChanged();
	}

	public int getCurId() {
		return this.id;
	}
	
	public int getResetCont() {
		return resetCont;
	}

	public Expedition getExpedition() {
		return expedition;
	}

	public ConcurrentHashMap<Integer, TankSimple> getMyTankMap() {
		return map;
	}
	public int getHistoryId() {
		return historyId;
	}

	public int getBuyBuffCount() {
		return buyBuffCount;
	}

	public boolean isState() {
		return state&&this.id==15;
	}

	public boolean isContains(long playerId){
		return playerIds.contains(playerId);
	}
	

	public void updateMyTank(TankSimple tank){
		this.map.put(tank.getId(), tank);
		SetChanged();
	}
	public void updateEnemy(int id,long hp,long mp){
		this.expedition.updateTank(id,hp,mp);
		SetChanged();
	}
	//获取我方坦克信息
	public TankSimple getTank(int tankId){
		return map.get(tankId);
	}
	//获取敌方坦克信息
	public Tank getEnemyTank(int tankId){
		return expedition.getTank(tankId);
	}
	public void receiveBox(int id) {
		receiveIds.add(id);
		SetChanged();
	}
	public boolean isReceiveBox(int id) {
		return receiveIds.contains(id);
	}
	
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerExpedition", this);
	}
	//重置远征
	public void reset(boolean isFree) {
		this.id = 0;
		if(!isFree) {
			this.resetCont++;
		}
		this.receiveIds.clear();
		this.map.clear();
		this.playerIds.clear();
		this.state =false;
		this.buyBuffCount = 0;
		this.fightNum = 0;
		SetChanged();
	}
	//重置数据
	public void resetData(){
		//只清空重置次数，其余数据由玩家手动重置
		this.resetCont=0;
		SetChanged();
	}

	public void addOpponent(long opponentId) {
		this.playerIds.add(opponentId);
		SetChanged();
	}
	//是否能重置
	public boolean isCanReset(){
		return this.id>1||!this.map.isEmpty();
	}

	public void clearance() {
		this.state = true;
		SetChanged();
	}

	public void buyBuff() {
		this.buyBuffCount++;
		SetChanged();
	}

	public void addFightNum() {
		this.fightNum ++;
		SetChanged();
	}
	
}
