package com.hm.action.recharge;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.commander.biz.CommanderBiz;
import com.hm.action.giftpack.GiftPackBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.task.biz.DailyTaskBiz;
import com.hm.cache.RechargeOrderCacheManager;
import com.hm.config.excel.*;
import com.hm.config.excel.temlate.RechargePriceNewTemplate;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerRecharge;
import com.hm.model.recharge.RechargeLogWarn;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.bluevip.QQBlueVip;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@Biz
public class RechargeBiz implements IObserver {
	@Resource
	private RechargeConfig rechargeConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GiftPackageConfig giftPackageConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private LogBiz logBiz;
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
	private CommanderBiz commanderBiz; 
	@Resource
	private CmqBiz cmqBiz; 
	@Resource
	private GiftPackBiz giftPackBiz;
	@Resource
	private DailyTaskBiz dailyTaskBiz;
	@Resource
	private LoginBiz loginBiz;



	
	/**
	 * 玩家充值成功奖励
	 * @param player
	 * @param @rechargeId
	 * @return
	 */
	public boolean rewardPlayer(Player player,int giftId) {
		RechargeGiftTempImpl rechargeGift = rechargeConfig.getRechargeGift(giftId);
		if(rechargeGift == null) {
			return false;
		}
		RechargePriceNewTemplate template = rechargeConfig.getTemplate(rechargeGift.getRecharge_id());
		if(template == null) {
			return false;
		}
		rewardRecharge(player, template,template.getPrice(), 0, rechargeGift, null);

		//logRechargeLog(player, template.getPrice(), PayType.Normal.getCode(), Maps.newHashMap(), rechargeGift, 1001);

		return true;
	}
	
	//领取月卡/周卡
	public List<Items> receiveDayCard(Player player,int rechargeType){
		PlayerRecharge playerRecharge = player.getPlayerRecharge();
		playerRecharge.reardKaType(rechargeType);
		RechargeGiftTempImpl template = rechargeConfig.getTemplateByType(rechargeType);
		List<Items> rewards = giftPackageConfig.rewardGiftList(template.getMonth_gift());
		itemBiz.addItem(player, rewards, LogType.DayCard.value(rechargeType));
		return rewards;
	}
	
	/*
	 * 
	testFlag,参数定义
	TestData("测试充值",0),
	Normal("正常充值",1),
	Gm("GM充值",2),*/
	private int rewardRecharge(Player player,RechargePriceNewTemplate template,
			long rmb, int testFlag, RechargeGiftTempImpl rechargeGift,Map<String, String> params) {
		try {
			loginBiz.resetPlayerByZero(player);//检查玩家每日数据
		} catch (Exception e) {
			e.printStackTrace();
		}

		int rechargeGiftId = rechargeGift.getId();
		int rechargeId = template.getId();
		int rechargeType = rechargeGift.getType();
		//===================发放奖励===========================
		List<Items> itemList = rechargeGift.getItemList(player);
		LogType logType = LogType.Recharge.value(rechargeGift.getId());
		if(RechargeType.isDayCard(rechargeType)) {
			int days = rechargeGift.getDays();
			player.getPlayerRecharge().addCzYueka(rechargeType, days);
		}else if(rechargeType == RechargeType.GiftRecharge.getType()) {
			itemList.addAll(giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift()));
		}else if(rechargeType == RechargeType.SuperWeapon.getType()) {
			commanderBiz.buySuperWeapon(player);
		}else if(rechargeType == RechargeType.NewPlayerGift.getType()
				|| rechargeType == RechargeType.GiftPackPay.getType()
				|| rechargeType == RechargeType.MidAutumn.getType()
				|| rechargeType == RechargeType.ChristGift.getType()) {
			itemList.addAll(giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift()));
		} else if (rechargeType == RechargeType.PayEveryDay.getType() || rechargeType == RechargeType.PayEveryDayGift.getType()) {
			itemList.addAll(giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift()));
			player.getPlayerStatistics().addLifeStatistics(StatisticsType.RECHARGE_NOACCOUNT, rmb);
		} else if (rechargeType == RechargeType.Double11Acivity.getType()) {
			itemList.addAll(giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift()));
		} else if (rechargeType == RechargeType.DailyTaskGift.getType()) {
			itemList.addAll(giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift()));
			dailyTaskBiz.doRecharge(player, rechargeGift.getId());
		} else if (rechargeType == RechargeType.Double12Acivity.getType()) {
			itemList.addAll(giftPackageConfig.rewardGiftList(rechargeGift.getSpecail_gift()));

		} else if (rechargeType == RechargeType.GiftBag.getType()) {//日周月礼包
			giftPackBiz.doRechargeActivity(player,itemList, rechargeGift);
		}

		player.getPlayerRecharge().addRechargeStatistics(rechargeGift.getId());
		player.getPlayerStatistics().addLifeStatistics(StatisticsType.RECHARGE_COUNT);
		player.getPlayerStatistics().addLifeStatistics(StatisticsType.RECHARGE,rmb);
		player.getPlayerStatistics().addLifeStatistics(StatisticsType.Diamond,template.getVip_point());
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.RECHARGEDAY,template.getPrice());
		player.getPlayerStatistics().addTodayRechargeStatistics(rechargeGift);
		//添加道具
		itemBiz.addItem(player, itemList, logType);
		playerBiz.addVipExp(player, template.getVip_point(), logType);
		//触发充值事件
		player.notifyObservers(ObservableEnum.Recharge, template.getId(),rmb, testFlag, rechargeGift.getId());
		//刷新充值排行榜
		HdLeaderboardsService.getInstance().updatePlayerRankForTimeAdd(player, RankType.Recharge, getPrice(player, template));

		if(player.isOnline()) {
			//保存更新用户
			player.sendUserUpdateMsgAndNowDB();
			JsonMsg msg = JsonMsg.create(MessageComm.S2C_Recharge);
			msg.addProperty("id", rechargeId);
			msg.addProperty("rechargeGiftId", rechargeGiftId);
			msg.addProperty("itemList", itemList);
			player.sendMsg(msg);
		}else {
			RechargeLogWarn rechargeWarn = new RechargeLogWarn(rechargeId, rechargeGiftId,itemList);
			player.getPlayerRecharge().addRechargeWarn(rechargeWarn);
			player.saveNowDB();
		}
		return 1;
	}
	
	//记录充值日志
	public void logRechargeLog(Player player,long rmb,
			int paytype,Map<String, String> params, 
			RechargeGiftTempImpl rechargeGift, int payChannel) {
		//正常重置，记录日志
		if(paytype==PayType.Normal.getCode()) {
			player.getPlayerStatistics().addLifeStatistics(StatisticsType.RECHARGEReal,rmb);
			logBiz.addRechargeLog(player, rechargeGift.getId(), rmb);
			//记录日志
			cmqBiz.playerRecharge(player, rmb, paytype, rechargeGift, payChannel, params);
		}
	}

	/**  
	 * rewardPlayer:(这里用一句话描述这个方法的作用). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 *  
	 * @author zxj  
	 * @param params
	 * @return  使用说明
	 * userid rmb productid
	 */
	public boolean rewardPlayer(Map<String, String> params) {
		long playerId = Long.parseLong(params.get("userid"));
		int rmb = Integer.parseInt(params.get("rmb"));

		int giftId = Integer.parseInt(params.get("productid"));
		
		int paychannel = params.containsKey("paychannel")?Integer.parseInt(params.get("paychannel")):0;
		
		/*TestData("测试充值",0),
		Normal("正常充值",1),
		Gm("GM充值",2),*/
		//默认正常充值
		int payType = 1;
		if(StrUtil.isNotBlank(params.get("paytype"))) {
			payType = Integer.parseInt(params.get("paytype"));
		}
		//判断订单是否重复
		String orderId = params.get("orderid");
		if(RechargeOrderCacheManager.getInstance().orderIsExist(orderId)) {
			log.error("======支付服务器======支付服务器支付失败，订单已经存在：" + JSON.toJSONString(params));
			//表示已经充值过了
			return true;
		}
		
		Player player = PlayerUtils.getPlayer(playerId);
		if(null==player) {
			log.error("======支付服务器======支付服务器支付失败，用户不存在：" + JSON.toJSONString(params));
			return false;
		}
		RechargeGiftTempImpl rechargeGift = rechargeConfig.getRechargeGift(giftId);
		if(rechargeGift == null) {
			log.error("======支付服务器======支付服务器支付失败，礼包计费点不存在：" + JSON.toJSONString(params));
			return false;
		}
		RechargePriceNewTemplate template = rechargeConfig.getTemplate(rechargeGift.getRecharge_id());
		if(template == null) {
			log.error("======支付服务器======支付服务器支付失败，计费点不存在：" + JSON.toJSONString(params));
			return false;
		}
		//配置文件中的计费点单位是元，要转换成分
		int price = template.getPrice();
		//判断用户付费的钱，不小于计费点百分之五十。（ps：渠道做活动，计费点会有优惠）
		double payPercentD = 0.5;
		String payPercentStr = ServerConfig.getInstance().getPayPercent();
		try{
			if(null!=payPercentStr) {
				payPercentD = Double.parseDouble(payPercentStr);
			}
		}catch (Exception e) {
		}
		if(rmb<price*payPercentD && !ServerConfig.getInstance().isTestPay()) {
			log.error("======支付服务器======支付服务器支付失败，用户付费不足：" + JSON.toJSONString(params));
			return false;
		}
		//发送奖励
		int result = rewardRecharge(player, template,rmb, payType, rechargeGift, params);
		if(result<0) {
			return false;
		}
		//记录充值日志
		logRechargeLog(player, rmb, payType, params, rechargeGift, paychannel);
		//加入订单缓存
		RechargeOrderCacheManager.getInstance().addOrderId(orderId);
		if(payType == PayType.Normal.getCode()) {
			player.notifyObservers(ObservableEnum.RechargeSdk, params);
		}
		return true;
	}
	
	//清除充值记录提醒
	private void clearRechargeWarn(Player player) {
		player.getPlayerRecharge().clearRechargeWarn();
		player.saveDB();
	}
	//重发充值记录提醒
	public void reSendRechargeWarn(Player player) {
		List<RechargeLogWarn> listRechargeWarn = player.getPlayerRecharge().getRechargeWarnList();
		if(listRechargeWarn.isEmpty()) {
			return;
		}
		for(RechargeLogWarn rechargeWarn :listRechargeWarn) {
			JsonMsg msg = JsonMsg.create(MessageComm.S2C_Recharge);
			msg.addProperty("id", rechargeWarn.getId());
			msg.addProperty("itemList", rechargeWarn.getId());
			player.sendMsg(msg);
		}
		this.clearRechargeWarn(player);
	}
	
	@Override
	public void invoke(ObservableEnum type, Player player, Object... argv) {
		switch(type) {
			case PlayerLevelChange:
				doRebate(player);
				break;
		}
	}
	
	//处理返利活动
	private void doRebate(Player player) {

	}

	//给玩家发送返利邮件
	private void sendRechargeEmail(Player player, String result, Map<String, Object> paramMap, double rebatePercent) {
		//计算钻石数量
		long diamondAll = new Double(Math.ceil(Float.parseFloat(result)*commValueConfig.getCommValue(CommonValueType.RechargeRebateTimes)/100)).longValue();
		long diamond = new Double(diamondAll*rebatePercent).longValue();
		Items item = new Items(PlayerAssetEnum.SysGold.getTypeId(), diamond, ItemType.CURRENCY.getType());

		mailBiz.sendSysMail(player, MailConfigEnum.RechargeRebate, Lists.newArrayList(item), LanguageVo.createStr(Integer.parseInt(result)/100, player.playerLevel().getLv(), diamond));
	}
	

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelChange, this);
	}

	/**
	 * @param player
	 * @return java.lang.Integer
	 * @description 处理 对接 QQ 游戏大厅，非蓝钻用户的充值 问题
	 * @date 2021/9/17 17:45
	 */
	public static Integer getPrice(Player player, RechargePriceNewTemplate rechargeTemplate) {
		QQBlueVip blueVip = player.playerBlueVip().getBlueVip();
		Integer price = rechargeTemplate.getPrice();
		if (blueVip != null && !blueVip.checkBlueVip()) {
			Double ceil = Math.ceil(price / 0.8);
			return ceil.intValue();
		}
		return price;
	}

	/**
	 * @param player
	 * @return java.lang.Integer
	 * @description 处理 对接 QQ 游戏大厅，非蓝钻用户的充值 Vip_point 问题
	 * @date 2021/9/17 17:45
	 */
	public static Integer getVip_point(Player player, RechargePriceNewTemplate rechargeTemplate) {
		Integer vip_point = rechargeTemplate.getVip_point();
		QQBlueVip blueVip = player.playerBlueVip().getBlueVip();
		if (blueVip != null && !blueVip.checkBlueVip()) {
			Double ceil = Math.ceil(vip_point / 0.8);
			return ceil.intValue();
		}
		return vip_point;
	}
}
















