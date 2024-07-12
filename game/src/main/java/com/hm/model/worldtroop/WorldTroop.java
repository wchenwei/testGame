package com.hm.model.worldtroop;

import com.hm.action.troop.client.ClientTroop;
import com.hm.actor.OnceQueueTaskType;
import com.hm.config.GameConstants;
import com.hm.enums.TroopPosition;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.actor.once.IOnceTask;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.war.sg.troop.PlayerTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
@RedisMapperType(type = MapperType.STRING_HASH)
public class WorldTroop extends BaseEntityMapper<String> implements IOnceTask {
	private long playerId;
	private int state;//TroopState
	private int cityId;//当前所在城市
    public long lastChangeTime;//上次部队变化时间
	private int worldType;
	
	private TroopArmy troopArmy;
	private TroopWay troopWay;
	private TroopInfo troopInfo;
	private int troopPosition;//部队位置

	@Transient
	private transient TroopTemp troopTemp;
	@Transient
	private transient ReentrantLock lock = new ReentrantLock(true);
	
	public TroopArmy getTroopArmy() {
		if(troopArmy == null) troopArmy = new TroopArmy();
		this.troopArmy.LateInit(this);
		return this.troopArmy;
	}
	public TroopWay getTroopWay() {
		if(troopWay == null) troopWay = new TroopWay();
		this.troopWay.LateInit(this);
		return this.troopWay;
	}
	public TroopInfo getTroopInfo() {
		if(troopInfo == null) troopInfo = new TroopInfo();
		this.troopInfo.LateInit(this);
		return this.troopInfo;
	}
	public TroopTemp getTroopTemp() {
		if(troopTemp == null) troopTemp = new TroopTemp();
		return this.troopTemp;
	}
	
	public void changeState(TroopState state) {
		this.state = state.getType();
	}


	public boolean isSky() {
		return this.cityId == GameConstants.PeaceId;//TODO 后边要改
	}

	public void setWorldType(WorldType worldType) {
		this.worldType = worldType.getType();
	}
	
	public void changePosition(TroopPosition troopPoition) {
		this.troopPosition = troopPoition.getType();
	}
	
	public void createLoadPlayer(Player player) {
		setServerId(player.getServerId());
		setId(player.getId()+"_"+PrimaryKeyWeb.getPrimaryKey("WorldTroop", player.getServerId()));
		this.playerId = player.getId();
		this.lastChangeTime = System.currentTimeMillis();
		setCityId(0);
		getTroopInfo().createLoadPlayer(player);
	}
	
	public PlayerTroop buildPlayerTroop() {
		PlayerTroop playerTroop = new PlayerTroop(getPlayerId(),getId());
		List<TankArmy> tankList = getTroopArmy().getTankList();
		tankList.forEach(e -> e.clearUnitRetain());
		playerTroop.setTankList(tankList);
		playerTroop.setAdvanceSkillTime(getTroopTemp().getAdvanceSkillTime());
		playerTroop.setFormationId(getTroopInfo().getFormationId());
		return playerTroop;
	}

	public PlayerTroop buildPvePlayerTroop() {
		PlayerTroop playerTroop = new PlayerTroop(getPlayerId(),getId());
		List<TankArmy> tankList = getTroopArmy().getCloneTankList();
		tankList.forEach(e ->{
			e.clearUnitRetain();
			e.setFullHp();
		});
		playerTroop.setTankList(tankList);
		playerTroop.setAdvanceSkillTime(getTroopTemp().getAdvanceSkillTime());
		playerTroop.setFormationId(getTroopInfo().getFormationId());
		return playerTroop;
	}

	public boolean lockTroop() {
		try {
			return this.lock.tryLock(1, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public void unlockTroop() {
		try {
			if(this.lock.isHeldByCurrentThread()) {
				this.lock.unlock();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void loadClientTroop(ClientTroop clientTroop) {
		this.getTroopArmy().changeTankList(clientTroop.getArmyList());
		this.getTroopInfo().bindFormation(clientTroop.getAircraftId());
	}

	public void saveDB() {
		//加入队列保存
		OnceQueueTaskType.TroopDB.putTask(this);
	}

	@Override
	public void doOnceTask() {
		this.lastChangeTime = System.currentTimeMillis();
		RedisMapperUtil.update(this);
	}

	public long getTroopCombat(Player player) {
		return getTroopArmy().getTankList().stream().map(e -> player.playerTank().getTank(e.getId()))
				.filter(Objects::nonNull).mapToLong(e -> e.getCombat()).sum();
	}

	public boolean isKFWorldTroop() {
		KFPServerUrl kfpServerUrl = getTroopTemp().getKfpServerUrl();
		return kfpServerUrl != null;
	}

	public int getKFServerId() {
		KFPServerUrl kfpServerUrl = getTroopTemp().getKfpServerUrl();
		if(kfpServerUrl != null && kfpServerUrl.getType() == 1) {
			return kfpServerUrl.getServerId();
		}
		return 0;
	}

}
