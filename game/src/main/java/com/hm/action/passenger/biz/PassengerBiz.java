package com.hm.action.passenger.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.config.PassengerConfig;
import com.hm.config.excel.templaextra.TankPassengerTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.model.item.Items;
import com.hm.model.passenger.Passenger;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.observer.ObservableEnum;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
@Biz
public class PassengerBiz {
	@Resource
	private TankBiz tankBiz;
	@Resource
	private PassengerConfig passengerConfig;
	@Resource
	private ItemBiz itemBiz;
	/**
	 * 合成乘员
	 * @param player
	 * @param passengerId
	 * @param num
	 */
	public void compose(Player player, int passengerId, int num) {
		for(int i=1;i<=num;i++){
			player.playerPassenger().addPassenger(new Passenger(passengerId,player.getServerId()));
		}
	}

	/**
     * 替换、上阵
     *
     * @param player
     * @param parts
     * @param tank
     */
    public void up(Player player, Passenger passenger, Tank tank,int index) {
    	//该坦克该位置原本的乘员
		String oldUid = tank.getPassengerUid(index);
		Passenger oldPassenger = player.playerPassenger().getPassenger(oldUid);
		if(eqPassenger(player, tank.getId(), passenger.getUid(), true)){
           if (oldPassenger != null) {
        	   oldPassenger.setTankId(0); //从tank上面转入背包
           }
           passenger.setTankId(tank.getId());
           player.playerPassenger().SetChanged();
	    }
		
    }
    
    /**
     * 替换、上阵
     *
     * @param player
     * @param parts
     * @param tank
     */
    public List<String> up(Player player, List<String> passengers, Tank tank) {
    	List<String> failList = Lists.newArrayList();
    	for(String uid:passengers){
    		Passenger passenger = player.playerPassenger().getPassenger(uid);
    		if(passenger==null||passenger.getTankId()!=0){
    			failList.add(uid);
    			continue;
    		}
    		TankPassengerTemplate template = passengerConfig.getPassenger(passenger.getId());
    		int index = template.getPosition();//通过配置获取该乘员对应的位置
    		//该坦克该位置原本的乘员
    		String oldUid = tank.getPassengerUid(index);
    		Passenger oldPassenger = player.playerPassenger().getPassenger(oldUid);
    		if(eqPassenger(player, tank.getId(), passenger.getUid(), true)){
               if (oldPassenger != null) {
            	   oldPassenger.setTankId(0); //从tank上面转入背包
               }
               passenger.setTankId(tank.getId());
               player.playerPassenger().SetChanged();
    	    }
    	}
    	return failList;
		
    }
    /**
     * 下阵
     * @param player
     * @param passenger
     */
    public void down(Player player, Passenger passenger) {
    	if(eqPassenger(player, passenger.getTankId(), passenger.getUid(), false)){
            passenger.setTankId(0);
            player.playerPassenger().SetChanged();
 	    }
	}
    /**
     * 
     * @param player
     * @param tankId
     * @param passengerUid 乘员唯一id
     * @param eq true:上阵  false:下阵
     */
	public boolean eqPassenger(Player player,int tankId,String passengerUid,boolean eq){
    	Passenger passenger=player.playerPassenger().getPassenger(passengerUid);
    	if(passenger==null){
    		return false;
    	}
    	int pos = passengerConfig.getPassenger(passenger.getId()).getPosition();
		if(eq){
			player.playerTank().addTankPassenger(tankId, passengerUid, pos);
		}else{
			player.playerTank().delTankPassenger(tankId, pos);
		}
		player.notifyObservers(ObservableEnum.Passenger_Eq, tankId,passenger.getId(),eq);
		return true; 
    }
    //升级消耗
	public List<Items> getLvUpCost(Player player, Passenger passenger,int count) {
		int startLv = passenger.getLv();
		int endLv = startLv+count;
		return itemBiz.createItemList(passengerConfig.getLvUpCost(passenger.getId(), startLv, endLv));
	}
	//培养消耗
	public List<Items> getCultureCost(Passenger passenger) {
		int lockCount = passenger.getLock().size();
		//根据上锁的数量得出消耗
		List<Items> costs = passengerConfig.getCultureCost(passenger.getId());
		return ItemUtils.calItemRateReward(costs, lockCount+1);
	}
	//升星消耗
	public List<Items> getStarUpCost(Passenger passenger) {
		List<Items> costs = passengerConfig.getStarUpCost(passenger.getId(), passenger.getStar());
		return costs;
	}
	//计算坦克的乘员套装光环
	public void calPassengerCircle(Player player, int tankId) {
		Tank tank = player.playerTank().getTank(tankId);
		String[] passengers = tank.getTankPassenger().getpassengers();
		Map<Integer,Integer> suitMap = Maps.newConcurrentMap();
		Arrays.stream(passengers).forEach(uid ->{
			Passenger passenger = player.playerPassenger().getPassenger(uid);
			if(passenger!=null){
				TankPassengerTemplate template = passengerConfig.getPassenger(passenger.getId());
				int suitType = template.getSuit_type();
				int count = suitMap.getOrDefault(suitType, 0);
				suitMap.put(suitType, count+1);
			}
		});
		List<Integer> skillIds = Lists.newArrayList();
		for(Map.Entry<Integer, Integer> entry:suitMap.entrySet()){
			if(entry.getValue()>=2){
				skillIds.addAll(passengerConfig.getSuitAttriIds(entry.getKey(), entry.getValue()));
			}
		}
		tank.getTankPassenger().calSkillIds(skillIds);
		player.playerTank().SetChanged();
	}
	//获取坦克中乘员的属性
	public TankAttr getPassengerAttr(Player player,Tank tank) {
		TankAttr tankAttr = new TankAttr();
		//乘员本身属性(升级，升星，培养)
		for(String uid:tank.getTankPassenger().getpassengers()){
			Passenger passenger = player.playerPassenger().getPassenger(uid);
			if(passenger!=null){
				tankAttr.addAttr(passengerConfig.getPassengerAttri(passenger));
			}
		}
		//乘员套装属性
		tankAttr.addAttr(passengerConfig.getSuitAttri(tank.getTankPassenger().getSkillIds()));
		return tankAttr;
	}
	//获取坦克乘员套装中的技能
	public List<Integer> getTankPassengerSkillIds(Player player,Tank tank) {
		return passengerConfig.getSuitSkillIds(tank.getTankPassenger().getSkillIds());
	}
	//乘员退役获得的材料
	public List<Items> getRetireReward(Player player, List<String> uids) {
		List<Items> rewards = Lists.newArrayList();
		uids.forEach(uid ->{
			Passenger passenger = player.playerPassenger().getPassenger(uid);
			if(passenger!=null&&passenger.getTankId()==0){
				rewards.addAll(passengerConfig.getRetireReward(passenger));
			}
		});
		//不返还钞票
		return ItemUtils.filterItemList(itemBiz.createItemList(rewards), ItemType.CURRENCY, PlayerAssetEnum.Cash.getTypeId());
	}
	//乘员退役
	public void retire(Player player, List<String> uids) {
		uids.forEach(uid->{
			Passenger passenger = player.playerPassenger().getPassenger(uid);
			if(passenger!=null&&passenger.getTankId()==0){
				player.playerPassenger().delPassenger(uid);
			}
		});
	}
	//升级
	public boolean lvUp(Player player, Passenger passenger, int type) {
		int lv = passenger.getLv();
		int count = type==0?1:10;
		int maxLv = passengerConfig.getMaxLv(passenger.getId());
		//要提升到的等级
		int destLv =  Math.min(maxLv,lv+count);
		for(int i=destLv;i>lv;i--){
			List<Items> cost = passengerConfig.getLvUpCost(passenger.getId(),lv, i);
			if(itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Passenger_LvUp.value(passenger.getId()))){
				//强化
				passenger.lvUp(i-lv);
				player.playerPassenger().SetChanged();
				return true;
			}
		}
		return false;
	}
	
    
}
