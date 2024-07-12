package com.hm.action.player.biz;

import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.annotation.Broadcast;
import com.hm.config.MissionConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.PlayerLevelConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.config.excel.templaextra.PlayerLevelExtraTemplate;
import com.hm.config.excel.templaextra.PlayerLvCompensateTemplate;
import com.hm.enums.LogType;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@Biz
public class PlayerLevelBiz extends NormalBroadcastAdapter {
	@Resource
	private PlayerLevelConfig playerLevelConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MailBiz mailBiz;
	@Resource
    private MailConfig mailConfig;
	@Resource
	private MissionConfig missionConfig;
	@Resource
	private PlayerBiz playerBiz;

//	@Broadcast({ObservableEnum.FBFightWin,ObservableEnum.LOGIN})
//	public void doMissionWin(ObservableEnum observableEnum, Player player, Object... argv) {
//		int missionId = player.playerMission().getFbId();
//		MissionExtraTemplate missionTemplate = missionConfig.getMission(missionId);
//		if(missionTemplate.getUnlock_level() > player.playerLevel().getLv()) {
//			//改变玩家等级
//			playerBiz.notifyLvUp(player,player.playerLevel().getLv(),missionTemplate.getUnlock_level());
//		}
//	}


	public void checkLvReward(Player player){
		int lv = player.playerLevel().getLv();
		int rewardLv = player.playerLevel().getRewardLv();
		if(rewardLv<lv){
			for(int i = rewardLv+1;i<=lv;i++){
				PlayerLevelExtraTemplate template = playerLevelConfig.getPlayerLevelTemplate(i);
				if(template!=null){
					itemBiz.addItem(player, template.getLevelRewards(), LogType.LevelTarget.value(i));
					itemBiz.addItem(player, template.getBigRewards(), LogType.LevelTarget.value(i));
				}
			}
			player.playerLevel().syncRewardLv();
		}
	}
	
	/**
	 * 发放等级补偿
	 * @param player
	 */
	public void checkPlayerLvCompensate(Player player) {
		if(player.playerLevel().isSendReward()) {
			return;
		}
		player.playerLevel().setSendReward(true);
		PlayerLvCompensateTemplate lvCompensateTemplate = playerLevelConfig.getPlayerLvCompensateTemplate(player.playerLevel().getLv());
		if(lvCompensateTemplate == null) {
			log.error(player.getId()+"等级补偿不足:"+player.playerLevel().getLv());
			return;
		}
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.LvCompensate);
		if(mailTemplate == null) {
			return;
		}
		log.error(player.getId()+"等级补偿奖励:"+player.playerLevel().getLv());
		mailBiz.sendSysMail(player, mailTemplate, lvCompensateTemplate.getItemList());
	}
	

	
}
