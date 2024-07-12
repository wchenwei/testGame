package com.hm.action.player;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.enums.LogType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 马甲包测试接口
 * @author siyunlong  
 * @date 2019年11月19日 下午9:41:56 
 * @version V1.0
 */
@Slf4j
@Action
public class PlayerTestAction extends AbstractPlayerAction{
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private ItemBiz itemBiz;
	
	/**
	 * 测试扣除金币
	 * @param player
	 * @param msg
	 */
	@MsgMethod(2374)
	public void receiveLvReward(Player player, JsonMsg msg){
		if(player.getServerId() != 999) {
			return;
		}
		int spend = msg.getInt("spend");
		if(playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold,spend, null)) {
			player.sendUserUpdateMsg();
		}
		player.sendMsg(2375);
	}
	
	@MsgMethod(2376)
	public void testItems(Player player, JsonMsg msg){
		if(player.getServerId() != 999) {
			return;
		}
		List<Items> spendItems = ItemUtils.str2ItemList(msg.getString("itemSpend"), ",", ":");
		List<Items> addItems = ItemUtils.str2ItemList(msg.getString("itemAdd"), ",", ":");
		if(!itemBiz.checkItemEnoughAndSpend(player, spendItems, LogType.None)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		itemBiz.addItem(player, addItems, LogType.None);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(2377);
		serverMsg.addProperty("addItems", addItems);
		player.sendMsg(serverMsg);
	}
	
	
	
	@MsgMethod(2382)
	public void logAction(Player player, JsonMsg msg){
		String info = msg.getString("info");
		log.error(player.getId()+"行为日志:"+info);
	}
}
