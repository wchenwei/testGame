package com.hm.action.tank;

import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankTechBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: TankTechAction. <br/>  
 * Function: 坦克科技处理. <br/>  
 * date: 2019年3月6日 下午1:42:46 <br/>  
 * @author zxj  
 * @version
 */
@Slf4j
@Action
public class TankTechAction extends AbstractPlayerAction {

	@Resource
	private ItemBiz itemBiz;
	@Resource
	private TankTechBiz tankTechBiz;
	@Resource
	private CommValueConfig commValueConfig;


	
	@MsgMethod (MessageComm.C2S_TankRecast)
	public void tankRecast(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId");

		Tank tank = player.playerTank().getTank(tankId);
		if(null==tank) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}

		List<Items> spendItems = commValueConfig.getListItem(CommonValueType.TankResetCost);
		if(!itemBiz.checkItemEnoughAndSpend(player, spendItems, LogType.TankRecast.value(tankId))) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}

		//给玩家重铸资源
		List<Items> addItems = tankTechBiz.getTankResetItems(tank);
		itemBiz.addItem(player, addItems, LogType.TankRecast.value(tankId));
		tankTechBiz.doReset(player, tank);
		Items items = spendItems.get(0);
		player.notifyObservers(ObservableEnum.CHTankRecast, tank.getId(), items.getId(), itemBiz.getItemTotal(player, items.getItemType(),items.getId()), addItems);
		
		JsonMsg showMsg = JsonMsg.create(MessageComm.S2C_TankRecast);
        showMsg.addProperty("itemList", addItems);
        player.sendUserUpdateMsg();
		player.sendMsg(showMsg);
	}
	
}









