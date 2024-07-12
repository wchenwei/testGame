package com.hm.action.voucher;

import cn.hutool.json.JSONObject;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.voucher.ApplyResult;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.http.biz.HttpBiz;
import com.hm.action.item.ItemBiz;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class PlayerVoucherAction extends AbstractPlayerAction{
	@Resource
	private HttpBiz httpBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CmqBiz cmqBiz;
	/**
	 * 兑换
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_Voucher)
	public void applyVoucher(Player player,JsonMsg msg){
		try {
			String voucherId = msg.getString("voucherId");
			int version = player.playerBaseInfo().getVersion();
			JSONObject result = httpBiz.applyVoucher(voucherId,player.getChannelId(), version,player.getPlayerVipInfo().getVipLv(),player.playerVoucher().getAppliedLog());
			ApplyResult applyResult = ApplyResult.getApplyResultByStr(result.getStr("result"));
			List<Items> rewards = ItemUtils.jsonToItems(result.getJSONArray("rewards"));
			String voucherSetName = result.getStr("voucherSetName");
			switch (applyResult) {
				case Ok:
					//增加兑换记录
					player.playerVoucher().addAppliedLog(voucherSetName);
					//发放物品
					itemBiz.addItem(player, rewards, LogType.Voucher.value(voucherId+"_"+voucherSetName));
					player.sendUserUpdateMsg();
					player.sendMsg(MessageComm.S2C_Voucher, ItemConstant.filterHideItems(rewards));
					
					cmqBiz.sendVoucher(player, voucherId, voucherSetName, rewards);
					
					return;
				case VoucherExpired://对换码过期
					player.sendErrorMsg(SysConstant.Voucher_Expired);
					return;
				case VoucherAlreadyUsed://已使用过
					player.sendErrorMsg(SysConstant.Voucher_AlreadyUsed);
					return;
				case VoucherInvalid://兑换码异常
					player.sendErrorMsg(SysConstant.Voucher_Invalid);
					return;
				case NotSameChannel://渠道不同
					player.sendErrorMsg(SysConstant.Voucher_ChannelLimit);
					return;
				case VipLimit://兑换码异常
					player.sendErrorMsg(SysConstant.Voucher_VipLimit);
					return;
					
			}
		} catch (Exception e) {
			player.sendErrorMsg(SysConstant.Network_Anomaly);
			return;
		}
	}
}
