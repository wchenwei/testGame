package com.hm.action.http.third;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.ChThirdConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.ActiveChSmShopTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.server.GameServerManager;
import com.hm.util.ItemUtils;
import com.hm.util.PubFunc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Slf4j
@Service("playerreward.do")
public class PlayerRewardAction {
	@Resource
	private ChThirdConfig chThirdConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	
	/**
	 * 发放草花线下神秘商店
	 * @param session
	 */
	public void sendChSmShopItem(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int itemId = Integer.parseInt(session.getParams("itemId"));
			ActiveChSmShopTemplate template = chThirdConfig.getActiveChSmShopTemplate(itemId);
			if(template == null) {
				log.error("草花线下=============找不到itemId"+playerId+"="+itemId);
				session.Write("0");
				return;
			}
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null) {
				log.error("草花线下=============找不到玩家"+playerId+"="+itemId);
				session.Write("0");
				return;
			}
			MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.ChSwShopReward);
			if(mailTemplate == null) {
				log.error("草花线下=============找不到邮件魔板"+playerId+"="+itemId);
				session.Write("0");
				return;
			}
			//发送邮件
			mailBiz.sendSysMail(player.getServerId(), playerId, mailTemplate, template.getItemList());
			
			if(template.getIs_broadcast() == 1) {
				player.notifyObservers(ObservableEnum.ChSmShopReward, template.getDesc());
			}
			log.error("草花线下=============成功"+playerId+"="+itemId);
			session.Write("1");
			return;
		} catch (Exception e) {
			log.error("草花线下=============异常",e);
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}

	private Set<String> orderList = Sets.newConcurrentHashSet();

	public void sendWxGift(HttpSession session) {
		try {
			String orderId = session.getParams("orderId");
			if(StrUtil.isEmpty(orderId) || CollUtil.contains(orderList,orderId)) {
				log.error(orderId+"=订单号已经存在");
				session.Write("0");
				return;
			}

			long playerId = Long.parseLong(session.getParams("playerId"));
			String itemId = session.getParams("itemId");
			int mailType = Integer.parseInt(session.getParams("type"));
			int num = Math.max(1,PubFunc.parseInt(session.getParams("num")));

			if(!GameServerManager.getInstance().isServerMachinePlayer(playerId)) {
				log.error("草花线下=============找不到玩家"+playerId+"="+itemId);
				session.Write("0");
				return;
			}

			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null) {
				log.error("草花线下=============找不到玩家"+playerId+"="+itemId);
				session.Write("0");
				return;
			}
			MailConfigEnum mailId = MailConfigEnum.getMailType(mailType);
			if(mailId == null) {
				log.error("草花线下=============找不到邮件魔板"+playerId+"="+itemId+"->"+mailType);
				session.Write("0");
			}
			MailTemplate mailTemplate = mailConfig.getMailTemplate(mailId);
			if(mailTemplate == null) {
				log.error("草花线下=============找不到邮件魔板"+playerId+"="+itemId+"->"+mailType);
				session.Write("0");
				return;
			}
			orderList.add(orderId);
			List<Items> itemList = ItemUtils.str2DefaultItemList(itemId);
			//发送邮件
			mailBiz.sendSysMail(player,mailTemplate,itemList);

			log.error(orderId+"草花线下WX=============成功"+playerId+"="+itemId);
			session.Write("1");
			return;
		} catch (Exception e) {
			log.error("草花线下=============异常",e);
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
}
