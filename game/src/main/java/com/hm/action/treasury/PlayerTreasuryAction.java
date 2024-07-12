package com.hm.action.treasury;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.treasury.biz.TreasuryBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.MathUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

@Action
public class PlayerTreasuryAction extends AbstractPlayerAction{
	@Resource
	private TreasuryBiz treasuryBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;

	@MsgMethod(MessageComm.C2S_Treasury_Collection)
	public void collect(Player player,JsonMsg msg){
		// 0: free, 1: cost
		int type = msg.getInt("type");
		// 付费征收
		if (type == 1) {
			int price = treasuryBiz.calcCollectPrice(player);
			if (price < 0) {
				// 没征收次数 & 前端要拦下
				player.sendErrorMsg(SysConstant.PARAM_ERROR);
				return;
			} else if (price > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, price, LogType.TreasuryLevy)) {
				player.sendErrorMsg(SysConstant.PLAYER_DIAMOND_NOT);
				return;
			}

			List<Items> items = treasuryBiz.cacCostReward(player);
			itemBiz.addItem(player, items, LogType.TreasuryLevy.value(type));
			player.playerTreasury().incCostTimes();
			player.notifyObservers(ObservableEnum.TreasuryCollect, type);
			player.sendUserUpdateMsg();
			player.sendMsg(MessageComm.S2C_Treasury_Collection, items);
		} else {
			long lastTime = player.playerTreasury().getLastTime();
			int minInterval = commValueConfig.getCommValue(CommonValueType.TreasuryMinInterval);
			int maxInterval = commValueConfig.getCommValue(CommonValueType.TreasuryMaxInterval);
			//订阅vip
			if(player.getPlayerRecharge().haveSubscribeVip()) {
				maxInterval = commValueConfig.getCommValue(CommonValueType.SubVipTreasuryMax);
			}
			long now = System.currentTimeMillis();
			int interval = (int) MathUtils.div(now - lastTime, GameConstants.MINUTE);
			if (interval < minInterval) {
				// 收取间隔时间太短 & 前端要拦下
				player.sendErrorMsg(SysConstant.PARAM_ERROR);
				return;
			}

			// 收取间隔有时间上限
			interval = Math.min(interval, maxInterval);
			List<Items> items = treasuryBiz.cacReward(player, interval);
			itemBiz.addItem(player, items, LogType.TreasuryLevy.value(interval));
			player.playerTreasury().setLastTime(now);
			player.notifyObservers(ObservableEnum.TreasuryCollect, type);
			player.sendUserUpdateMsg();
			player.sendMsg(MessageComm.S2C_Treasury_Collection, items);
		}
	}
}
