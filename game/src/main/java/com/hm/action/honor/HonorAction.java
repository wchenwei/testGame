package com.hm.action.honor;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.HonorConfig;
import com.hm.config.excel.templaextra.HonorLineTemplate;
import com.hm.enums.LogType;
import com.hm.enums.PlayerRewardMode;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Action
public class HonorAction extends AbstractPlayerAction{
	@Resource
    private HonorConfig honorConfig;
	@Resource
    private ItemBiz itemBiz;
	@Resource
    private ActivityEffectBiz activityEffectBiz;
	
	@MsgMethod(MessageComm.C2S_RewardHonorLine)
    public void rewardHonorLine(Player player, JsonMsg msg) {
		//领取所有
		List<HonorLineTemplate> templateList = honorConfig.getCanHonorLineTemplate(player);
		if(templateList.isEmpty()) {
			return;
		}
		List<Items> itemList = templateList.stream()
				.flatMap(e -> e.getRewardList().stream()).collect(Collectors.toList());
		List<Integer> idList = templateList.stream().filter(t-> !CollUtil.isEmpty(t.getRewardList())).map(e -> e.getId()).collect(Collectors.toList());
		player.playerHonor().addRewardIndex(idList);

		itemList = itemBiz.createItemList(itemList);
		
		itemBiz.addItem(player, itemList, LogType.HonorLineReward);//发放奖励

		player.notifyObservers(ObservableEnum.HonorReceive,idList);
		player.sendUserUpdateMsg();

		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_RewardHonorLine);
		serverMsg.addProperty("itemList", itemList);
		player.sendMsg(serverMsg);
	}
}
