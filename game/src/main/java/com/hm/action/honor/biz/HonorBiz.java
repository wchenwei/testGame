package com.hm.action.honor.biz;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.HonorConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.HonorLineTemplate;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class HonorBiz {
	@Resource
    private HonorConfig honorConfig;
	@Resource
    private ItemBiz itemBiz;
	@Resource
    private MailBiz mailBiz;

	
	public boolean doPlayerReset(Player player) {
		if(player.playerHonor().getTotalHonor() <= 0) {
			return false;
		}
		//把之前没有领取的发邮件
		List<HonorLineTemplate> templateList = honorConfig.getCanHonorLineTemplate(player);
		if(CollUtil.isNotEmpty(templateList)) {
			List<Items> itemList = templateList.stream()
					.flatMap(e -> e.getRewardList().stream()).collect(Collectors.toList());
			if(CollUtil.isNotEmpty(itemList)) {
				itemList = itemBiz.createItemList(itemList);
				//发邮件
				mailBiz.sendSysMail(player, MailConfigEnum.HonorReward, itemList);
			}
		}
		player.playerHonor().resetDay();
		return true;
	}



}
