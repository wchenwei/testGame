package com.hm.model.cityworld;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.vo.SMovePlayerVo;
import com.hm.actor.OnceQueueTaskType;
import com.hm.config.GameConstants;
import com.hm.enums.AtkDefType;
import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.actor.once.IOnceTask;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.cityworld.troop.BaseCityFightTroop;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.util.RandomUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 
 * @Description: 世界城池
 * @author siyunlong  
 * @date 2018年10月20日 上午10:17:26 
 * @version V1.0
 */
@RedisMapperType(type = MapperType.STRING_HASH)
public class WorldCity extends BaseEntityMapper<Integer> implements IOnceTask,IWorldCity {
	private CityBelong cityBelong;//城镇归属
	private CityTroop atkCityTroop;
	private CityTroop defCityTroop;
	
	private CityNpc cityNpc;
	private CityStatus cityStatus;

	@Transient
	private transient CityFight cityFight;
	@Transient
	@Getter@Setter
	private transient boolean isPortCity;
	
	@Transient
	private transient ReentrantLock lock = new ReentrantLock(true);
	
	public CityBelong getCityBelong() {
		if(cityBelong == null) cityBelong = new CityBelong();
		this.cityBelong.LateInit(this);
		return this.cityBelong;
	}
	public CityTroop getAtkCityTroop() {
		if(atkCityTroop == null) atkCityTroop = new CityTroop();
		this.atkCityTroop.LateInit(this);
		return this.atkCityTroop;
	}
	public CityTroop getDefCityTroop() {
		if(defCityTroop == null) defCityTroop = new CityTroop();
		this.defCityTroop.LateInit(this);
		return this.defCityTroop;
	}
	public CityNpc getCityNpc() {
		if(cityNpc == null) cityNpc = new CityNpc();
		this.cityNpc.LateInit(this);
		return this.cityNpc;
	}
	public CityFight getCityFight() {
		if(cityFight == null) cityFight = new CityFight();
		this.cityFight.LateInit(this);
		return this.cityFight;
	}
	public CityStatus getCityStatus() {
		if(cityStatus == null) cityStatus = new CityStatus();
		this.cityStatus.LateInit(this);
		return this.cityStatus;
	}

	public int getBelongGuildId() {
		return getCityBelong().getGuildId();
	}

	public boolean isPlayerCity() {
		return getBelongGuildId() > 0;
	}
	
	public CityTroop getCityTroop(int type) {
		return type == AtkDefType.ATK.getType() ? getAtkCityTroop():getDefCityTroop();
	}
	
	/**
	 * 添加部队
	 * @param player
	 * @param worldTroop
	 */
	public void addTroop(Player player,WorldTroop worldTroop,boolean isFriendGuild) {
		try {
			lockWrite();
			worldTroop.setCityId(this.getId());
			if(hasTroop(worldTroop.getId())) {
				return;
			}
			WorldBiz worldBiz = SpringUtil.getBean(WorldBiz.class);
			//是否联盟
			if(isFriendGuild) {
				if(hasFight()) {
					TroopState troopState = worldBiz.checkTroopIsFightState(this, AtkDefType.DEF)?TroopState.Fight:TroopState.FightWait;
					worldTroop.changeState(troopState);
				}else{
					worldTroop.changeState(TroopState.None);
				}
				getDefCityTroop().addTroop(worldTroop);
			}else{
				TroopState troopState = worldBiz.checkTroopIsFightState(this, AtkDefType.ATK)?TroopState.Fight:TroopState.FightWait;
				worldTroop.changeState(troopState);
				getAtkCityTroop().addTroop(worldTroop);
			}
		} finally {
			unlockWrite();
		}
	}
	
	//删除部队
	public boolean removeTroop(String troopId) {
		try {
			lockWrite();
			return this.getAtkCityTroop().removeTroop(troopId) || this.getDefCityTroop().removeTroop(troopId);
		} finally {
			unlockWrite();
		}
	}
	
	public boolean removeTroopAndSave(String troopId) {
		if(getId() == GameConstants.PeaceId) {
			return true;
		}
		boolean isSuc = removeTroop(troopId);
		if(isSuc) saveDB();
		return isSuc;
	}
	
	public boolean hasTroop(String troopId) {
		return this.getAtkCityTroop().hasTroop(troopId) || this.getDefCityTroop().hasTroop(troopId);
	}
	
	//此城是否有战斗
	public boolean hasFight() {
		WorldBiz worldBiz = SpringUtil.getBean(WorldBiz.class);
		return worldBiz.worldCityHasFight(this);
	}
	
	//根据id获取部队
	public BaseCityFightTroop getCityFightTroop(String troopId) {
		BaseCityFightTroop temp = this.getAtkCityTroop().getCityFightTroop(troopId);
		if(temp != null) {
			return temp;
		}
		return this.getDefCityTroop().getCityFightTroop(troopId);
	}
	
	//获取攻守双方部队数量
	public int getTroopSize(AtkDefType type) {
		return getCityTroop(type.getType()).getTroopSize();
	}
	
	public AtkDefType getAtkDefType(String troopId) {
		if(this.getAtkCityTroop().hasTroop(troopId)) {
			return AtkDefType.ATK;
		}
		if(this.getDefCityTroop().hasTroop(troopId)) {
			return AtkDefType.DEF;
		}
		return null;
	}
	
	//攻占城后清空数据
	public void clearData() {
		getCityFight().clearFightData();
	}
	
	public WorldType getWorldType() {
		return WorldType.getTypeByCityId(this.getId());
	}

	

	public boolean lockWrite() {
		try {
			return this.lock.tryLock(GameConstants.CityLockSecond, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public void unlockWrite() {
		try {
			if(this.lock.isHeldByCurrentThread()) {
				this.lock.unlock();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveDB() {
		//加入队列保存
		OnceQueueTaskType.BuildDB.putTask(this);
	}

	@Override
	public void doOnceTask() {
		RedisMapperUtil.update(this);
	}

	public boolean haveAtkCityTroop() {
		return CollUtil.isNotEmpty(getAtkCityTroop().getTroopList());
	}


	public List<SMovePlayerVo> getDefTroopForMoveVo() {
		return CollUtil.sub(getDefCityTroop().getTroopList(),0,GameConstants.CityMaxShowTroop)
				.stream().map(e -> e.createSMovePlayerVo(this)).collect(Collectors.toList());
	}

	@Override
	public int getWorldCityId() {
		return getId();
	}
}
