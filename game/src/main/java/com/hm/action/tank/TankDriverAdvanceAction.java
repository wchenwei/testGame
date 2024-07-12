package com.hm.action.tank;

import com.alibaba.fastjson.JSON;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankDriverAdvanceBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankDriverAdvanceConfig;
import com.hm.config.excel.TankFettersConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zxj
 * date:2020年3月4日10:06:23
 * desc:坦克车长的，军职 
 */
@Action
public class TankDriverAdvanceAction extends AbstractPlayerAction{
	@Resource
	private TankDriverAdvanceBiz driverAdvanceBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private TankDriverAdvanceConfig advanceConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private TankFettersConfig tankFettersConfig;
	/**
	 * 军职升级
	 * @param player
	 * @param msg
	 * #msg:340329,tankId=8,chooseId=1
	 */
	@MsgMethod ( MessageComm.C2S_TankDriverAd)
	public void lvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		int chooseId = msg.getInt("chooseId"); // 选择技能的id
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankDriverAd)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		Tank tank = player.playerTank().getTank(tankId);
		int check=driverAdvanceBiz.check(player, tank, chooseId);
		if(check>0) {
			player.sendErrorMsg(check);
			return;
		}
		int maxLv = tank.getDriver().getAdvanceMaxLv();
		//消耗资源
		List<Items> costItems = advanceConfig.getAdvanceCostByLv(maxLv);
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.TankDriverLvup.value(tankId))) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		tank.getDriver().advanceLvup(chooseId);
		int minLv = driverAdvanceBiz.getMinLv(player, tankId);
		//广播羁绊变化//1级没有羁绊加成，所有要判断大于
		if(minLv>maxLv) {
			player.notifyObservers(ObservableEnum.TankDriverAdChangeFetter, tankFettersConfig.getTankFetter(tankId));
		}
		player.notifyObservers(ObservableEnum.TankDriverAdChange, tank.getId());

		String s = JSON.toJSONString(tank.getDriver().getAdvance());
		player.notifyObservers(ObservableEnum.CHTankDriverAdvanceLvUp, tankId, chooseId, s, costItems);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankDriverAd,true);
	}
	
	/**
	 * 军职退伍
	 * @param player
	 * @param msg
	 * #msg:340331,tankId=8
	 */
	@MsgMethod ( MessageComm.C2S_TankDriverAdQuit)
	public void quit(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		Tank tank = player.playerTank().getTank(tankId);
		if(null==tank || tank.getDriver().getAdvanceMaxLv()<=0) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		//消耗资源
		List<Items> listItem = commValueConfig.getListItem(CommonValueType.TankDriverAd);
		if(!itemBiz.checkItemEnoughAndSpend(player, listItem, LogType.TankDriverQuit)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		//获取军职羁绊的最小等级
		int minLv = driverAdvanceBiz.getMinLv(player, tankId);
		
		List<Items> costs = ItemBiz.createItemList(advanceConfig.getAdvanceCostAll(tank.getDriver().getAdvanceMaxLv()));
		tank.getDriver().cleanAdvance(tankId);
		itemBiz.addItem(player, costs, LogType.TankDriverQuit);
		//如果最小等级大于0，则需要广播羁绊变化
		if(minLv>0){
			player.notifyObservers(ObservableEnum.TankDriverAdChangeFetter, tankFettersConfig.getTankFetter(tankId));
		}
		player.notifyObservers(ObservableEnum.TankDriverAdChange, tankId);
		// 军职重铸，打点类型3
		Items items = listItem.get(0);
		player.notifyObservers(ObservableEnum.CHTankRecast, tank.getId(), 3, items.getId(), itemBiz.getItemTotal(player, items.getItemType(),items.getId()), costs);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankDriverAdQuit,costs);
	}
}




