package com.hm.action.passenger;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.passenger.biz.PassengerBiz;
import com.hm.config.PassengerConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.TankCrewPieceTemplate;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.passenger.Passenger;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.setting.TankSetting;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class PassengerAction extends AbstractPlayerAction {
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private PassengerBiz passengerBiz;
	@Resource
	private PassengerConfig passengerConfig;
	@Resource
	private TankConfig tankConfig;
	
	/**
	 * 合成
	 */
	@MsgMethod(MessageComm.C2S_Passenger_Compose)
    public void compose(Player player, JsonMsg msg) {
		//碎片id
		int pieceId = msg.getInt("pieceId");
		//合成数量
		int num = msg.getInt("num");
		TankCrewPieceTemplate template = passengerConfig.getPassengerPieceTemplate(pieceId);
		if(template==null){
			//该碎片不存在
			return;
		}
		//碎片id对应的乘员id
		int passengerId = template.getCrew();
		Items cost = passengerConfig.getComposeCost(passengerId,num);
		if(cost==null){
			return;
		}
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Passenger_Compose)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		passengerBiz.compose(player,passengerId,num);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_Compose, num);
	}
	
	/**
	 * 上阵,替换
	 */
	@MsgMethod(MessageComm.C2S_Passenger_Up)
    public void passengerUp(Player player, JsonMsg msg) {
		String msgString = msg.getString("uids");
		List<String> uids = StringUtil.splitStr2StrList(msgString, ",");
		//坦克id
		int tankId = msg.getInt("tankId");
		Tank tank = player.playerTank().getTank(tankId);
		if(tank==null){
			return;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		//只有SS及以上坦克才能装备乘员
		if(tankSetting==null||tankSetting.getRare()<4){
			return;
		}
        List<String> failList = passengerBiz.up(player, uids, tank);
		player.notifyObservers(ObservableEnum.CHPassengerUp, tankId, msgString);
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_Up,failList);
	}
	
	/**
	 * 下阵
	 */
	@MsgMethod(MessageComm.C2S_Passenger_Down)
    public void passengerDown(Player player, JsonMsg msg) {
		String uid = msg.getString("uid");
		Passenger passenger = player.playerPassenger().getPassenger(uid);
        if (passenger == null || passenger.getTankId() == 0) {
        	//没有该乘员或该乘员没有上阵
            return;
        }
        int tankId = passenger.getTankId();
        passengerBiz.down(player, passenger);
        passengerBiz.calPassengerCircle(player,tankId);
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_Down);
	}
	
	/**
	 * 升级
	 */
	@MsgMethod(MessageComm.C2S_Passenger_LevelUp)
    public void C2S_Passenger_LevelUp(Player player, JsonMsg msg) {
		String uid = msg.getString("uid");
		Passenger passenger = player.playerPassenger().getPassenger(uid);
        if (passenger == null) {
            return;
        }
        int maxLv = passengerConfig.getMaxLv(passenger.getId());
		int oldLv = passenger.getLv();
		if(oldLv >=maxLv){
        	return;
        }
        //0-升级一次 1-升级10次
        int type = msg.getInt("type");
        passengerBiz.lvUp(player,passenger,type);
        player.notifyObservers(ObservableEnum.Passenger_LvUp, passenger.getTankId(),
				passenger.getId(), oldLv, passenger.getLv());
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_LevelUp);
	}
	/**
	 * 锁定
	 */
	@MsgMethod(MessageComm.C2S_Passenger_Lock)
    public void C2S_Passenger_Lock(Player player, JsonMsg msg) {
		String uid = msg.getString("uid");
		int index = msg.getInt("index");
		Passenger passenger = player.playerPassenger().getPassenger(uid);
		if(passenger.getLock().size()>=2){
			return;
		}
		if(index>3||player.playerPassenger().isLock(uid, index)){
			//已经锁定
			return;
		}
		player.playerPassenger().lock(uid,index);
        player.playerPassenger().SetChanged();
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_Lock);
	}
	
	/**
	 * 解锁
	 */
	@MsgMethod(MessageComm.C2S_Passenger_UnLock)
    public void C2S_Passenger_unLock(Player player, JsonMsg msg) {
		String uid = msg.getString("uid");
		int index = msg.getInt("index");
		if(!player.playerPassenger().isLock(uid, index)){
			//已经锁定
			return;
		}
		player.playerPassenger().unLock(uid,index);
        player.playerPassenger().SetChanged();
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_UnLock);
	}
	
	/**
	 * 培养
	 */
	@MsgMethod(MessageComm.C2S_Passenger_Culture)
    public void culture(Player player, JsonMsg msg) {
		String uid = msg.getString("uid");
		Passenger passenger = player.playerPassenger().getPassenger(uid);
        if (passenger == null) {
            return;
        }
        List<Items> cost = passengerBiz.getCultureCost(passenger);
        if(CollectionUtil.isEmpty(cost)){
        	return;
        }
        if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Passenger_Culture.value(passenger.getId()))){
        	return;
        }
		String oldStr = JSON.toJSONString(passenger);
		player.playerPassenger().culture(uid);
		String newStr = JSON.toJSONString(passenger);
        player.notifyObservers(ObservableEnum.Passenger_Culture, passenger.getTankId(), oldStr, newStr, cost);
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_Culture);
	}
	
	/**
	 * 升星
	 */
	@MsgMethod(MessageComm.C2S_Passenger_StarUp)
    public void starUp(Player player, JsonMsg msg) {
		String uid = msg.getString("uid");
		Passenger passenger = player.playerPassenger().getPassenger(uid);
        if (passenger == null) {
            return;
        }
        //根据乘员获取该品质最大星级
        int maxStar = passengerConfig.getStarMax(passenger.getId());
		int oldStar = passenger.getStar();
		if(oldStar >=maxStar){
        	return;
        }
        List<Items> cost = passengerBiz.getStarUpCost(passenger);
        if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Passenger_Culture.value(passenger.getId()))){
        	return;
        }
        passenger.starUp();
        player.notifyObservers(ObservableEnum.Passenger_StarUp, passenger.getTankId(), passenger.getId(), oldStar, passenger.getStar(), cost);
        player.playerPassenger().SetChanged();
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_StarUp);
	}
	
	/**
	 * 退役
	 */
	@MsgMethod(MessageComm.C2S_Passenger_Retire)
    public void retire(Player player, JsonMsg msg) {
		List<String> uids = StringUtil.splitStr2StrList(msg.getString("uids"), ",");
		List<Items> rewards = passengerBiz.getRetireReward(player,uids);
		//删除乘员
		passengerBiz.retire(player,uids);
		itemBiz.addItem(player, rewards, LogType.Passenger_Retire.value(msg.getString("uids")));
		player.notifyObservers(ObservableEnum.CHPassengerRetire, uids);
        player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Passenger_Retire,rewards);
	}
	
}
