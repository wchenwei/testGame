/**  
 * Project Name:SLG_GameHot.
 * File Name:NoticeBiz.java  
 * Package Name:com.hm.action.notice  
 * Date:2018年4月3日上午11:21:39  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */

package com.hm.action.notice;

import cn.hutool.core.util.StrUtil;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.item.ItemNameBiz;
import com.hm.chat.ChatRoomType;
import com.hm.chat.InnerChatFacade;
import com.hm.config.CityConfig;
import com.hm.config.ControlConfig;
import com.hm.config.EquipmentConfig;
import com.hm.config.excel.*;
import com.hm.config.excel.templaextra.*;
import com.hm.container.PlayerContainer;
import com.hm.enums.AtkDefType;
import com.hm.enums.LogType;
import com.hm.enums.NoticeMsgType;
import com.hm.enums.TankRareType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.AbstractGuildTactics;
import com.hm.model.item.Items;
import com.hm.model.notice.NewNotice;
import com.hm.model.player.Aircraft;
import com.hm.model.player.CaptiveTankInfo;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.redis.ServerNameCache;
import com.hm.redis.util.RedisUtil;
import com.hm.util.PubFunc;
import com.hm.util.StringUtil;
import com.hm.war.sg.setting.TankSetting;
import com.rits.cloning.Cloner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class NoticeBiz implements IObserver{
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
	private InnerChatFacade innerChatFacade; 
	@Resource
	private NoticeConfig noticeConfig; 
	@Resource
	private TankConfig tankConfig; 
	@Resource
	private CommanderConfig commanderConfig; 
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private EquipmentConfig equipmentConfig;
	@Resource
	private AgentConfig agentConfig;
	@Resource
	private ItemNameBiz itemNameBiz;
	@Resource
	private TankDriverAdvanceConfig advanceConfig;
	@Resource
	private AircraftCarrierConfig aircraftCarrierConfig;
	@Resource
	private FishConfig fishConfig;
	@Resource
	private Act2063Config act2063Config;
	
	@Override
	public void registObserverEnum() {
		for (ObservableEnum observableEnum : noticeConfig.getAllObservableEnum()) {
			registObserver(observableEnum);
		}
	}
	
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		NoticeTemplate noticeTemplate = noticeConfig.getNoticeTemplate(observableEnum);
		if(noticeTemplate == null) {
			return;
		}
		String content = null;
		int serverId = 0;
		switch (observableEnum) {
			case TankAdd:
				content = doTankAdd(player, (int)argv[0], noticeTemplate);
				break;
			case AddAircraftCarrier:
				content = doAircraftCarrierAdd(player, (int)argv[0], noticeTemplate);
				break;
			case TankStarUp:
				content = doTankStarUp(player, (int)argv[0], noticeTemplate);
				break;
			case AircraftStarUp:
				content = doAircraftStarUp(player, (String)argv[0], noticeTemplate);
				break;
			case DriverLv:
				content = doDriverLv(player, (int)argv[0], noticeTemplate);
				break;
			case MilitaryLvChange:
				content = doMilitaryLv(player, (boolean)argv[0],noticeTemplate);
				break;
			case WorldBossStart:
			case WorldBossStartHot:
			case KfScoreStart5:
			case KfScoreStart:
			case KfExpeditionOccWin:
			case KfExpeditionOccFail:
			case WorldBuildTaskRelease:
			case WorldBuildOpen:
			case SpringActivityBagEnd:
			case SpringActivityBagReward:
			case KFWzStart:
			case BerlinWarOpen:
			case KfScuffleStart:
			case KfScuffleAirportReset:
			case KFScoreGNpcPre5:
			case KFScoreGNpcOut:
			case KFScoreScoreChangePre5:
			case KFScoreScoreChange:
			case GuildWarStart:
			case GuildWarEnd:
			case GuildWarHerald:
				serverId = PubFunc.parseInt(argv[0].toString());
				content = langeConfig.getValue(noticeTemplate.getKey());
				break;
			case PlayerCityOccupy:
				AtkDefType warType = (AtkDefType)argv[0];
				int cityId = (int) argv[1];
				content = this.getCityOccupyContent(player, warType, cityId, noticeTemplate);
				break;
			case GuildChangeMainCity:
				int oldCityId = (int) argv[0];
				int newCityId = (int) argv[1];
				content = getGuildChangeMainCity(player, noticeTemplate, oldCityId, newCityId);
				break;
			case Vow:
				int multiple = (int) argv[0];
				content = getVowRateContent(player, noticeTemplate, multiple);
				break;
			case AddEquip:
				int itemId = (int) argv[0];
				content = getAddEquidMsg(player, noticeTemplate, itemId);
				break;
			case UseTactics:
				content = getUseUseTacticsMsg(player, noticeTemplate, (AbstractGuildTactics) argv[0]);
				break;
			case PveRebelOccupyCity:
				content = getPveContent(player, noticeTemplate,(WorldCity) argv[0]);
				break;
			case TankDrawShuffleLucky:
			case KillGhostBoss:
				content = getKillGhostBoss(player, noticeTemplate);
				break;
			case TankEvolveStarUp:
				content = getTankEvolveStarUp(player, noticeTemplate, (int)argv[0]);
				break;
			case KfManorOcc:
				serverId = (int)argv[2];
				content = getKfManorOcc(noticeTemplate, (String)argv[0], (String)argv[1]);
				break;
			case WorldBossKill:
				String playerName = argv[0].toString();
				content = getWorldBossKillContent(player, noticeTemplate, playerName);
				break;
			case FindBlackMainCity:
				content = getFindBlackMainCity(player, (WorldCity)argv[0], noticeTemplate);
				break;
			case KfExpeditionOccAtk:
			case KfExpeditionOccDef:
				serverId = PubFunc.parseInt(argv[0].toString());
				cityId = PubFunc.parseInt(argv[1].toString());
				content = getKfExpedetion(noticeTemplate, cityId);
				break;
			case Power_CenterCity:
				content = getPowerCenterCity(player, argv, noticeTemplate);
				break;
			case Power_Policy:
				content = getPowerPolicy(player, argv, noticeTemplate);
				break;
			case Power_Punish:
				content = getPowerPunish(player, argv, noticeTemplate);
				break;
			case Agent:
				int agentId = (int) argv[0];
				if (argv[1].toString().equals("active")) {
					content = getAgentContent(player, agentId, noticeTemplate);
				}
				break;
			case EggGetTrueItem:
				serverId = (int)argv[2];
				content = getEggGetTrueItem(noticeTemplate, (String)argv[0], (String)argv[1]);
				break;
			case SantaClausForecast:
			case SantaClausAppear:
				serverId = (int)argv[0];
				Items bigGift = (Items)argv[2];
				CityTemplate cityTemplate = cityConfig.getCityById((int)argv[1]);
				String cityName = langeConfig.getValue(cityTemplate.getName());
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), cityName,itemNameBiz.getItemName(bigGift),bigGift.getCount());
				break;
			case SantaClausBigGift:
				Items reward = (Items)argv[0];
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), player.getName(),itemNameBiz.getItemName(reward),reward.getCount());
				break;
			case YearGetItems:
			case Activity202GetItems:
				serverId = (int)argv[3];
				content = getYearGetItems(noticeTemplate, (String)argv[0], (String)argv[1], (int)argv[2]);
				break;
			case KFScoreMainCityLose:
				serverId = Integer.parseInt(argv[0].toString());
				content = getKFscoreCityLose(noticeTemplate, Integer.parseInt(argv[1].toString()));
				break;
			case SpringActivityKillBoss:
				int bossLv = PubFunc.parseInt(argv[0].toString());
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), player.getName(), bossLv);
				break;
			case DoubleNinthActivityKillBoss:
				// id-->chongyang_palce_2-->岳阳楼
				int nameId = PubFunc.parseInt(argv[0].toString());
				String languageId = langeConfig.getValue(String.format("chongyang_palce_%d", nameId));
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), player.getName(), langeConfig.getValue(languageId));
				break;
			case ChSmShopReward:
				String itemName = langeConfig.getValue((String)argv[0]);
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), player.getName(), itemName);
				break;
			case TankDriverAdChange:
				int tankId = Integer.parseInt(argv[0].toString());
				content = getTankDriverAdChange(player, tankId, noticeTemplate);
				break;
			case OneDollarPrize:
				serverId = Integer.parseInt(argv[0].toString());
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), argv[1].toString(), argv[2].toString());
				break;
			case OneDollarAllIn:
				serverId = Integer.parseInt(argv[0].toString());
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), argv[1].toString(), argv[2].toString());
				break;
			case KFWzCenter:
				serverId = Integer.parseInt(argv[0].toString());
				content = getKfServerName(noticeTemplate, (String)argv[1]);
				break;
			case KfBuildHunterFind:
				serverId = Integer.parseInt(argv[0].toString());
				content = getKfBuildHunterFind(noticeTemplate, (String)argv[1],(String)argv[2]);
				break;
			case TankCaptive:
				content = getCaptiveTankContent(player, noticeTemplate, (CaptiveTankInfo) argv[0]);
				break;
			case Act97GetItems:
				serverId = (int)argv[2];
				content = getAct97GetItems(noticeTemplate, (String)argv[0], (String)argv[1]);
				break;
			case KfScuffleOccupyMine:
				serverId = Integer.parseInt(argv[0].toString());
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), argv[1].toString(), argv[2].toString());
				break;
			case KfScuffleAirportBossKill:
				serverId = Integer.parseInt(argv[0].toString());
				content = String.format(langeConfig.getValue(noticeTemplate.getKey()), argv[1].toString());
				break;
			case ElementLvUp:
				content = getElementLvUp(player, noticeTemplate, (int) argv[1]);
				break;
			case AddElement:
                content = getElementAdd(player, noticeTemplate, (int) argv[0], (long) argv[1], (LogType) argv[2]);
				break;
			case FishRecord:
				content = getFishRecordContent(player, noticeTemplate, (int) argv[0]);
				break;
			case Act2063Record:
				content = getActFishContent(player, noticeTemplate, (int) argv[0]);
				break;
		}
		if(StrUtil.isEmpty(content)) {
			return;
		}
		NewNotice notice = new NewNotice();
		notice.setContent(content);
		notice.setRank(noticeTemplate.getRank());
		notice.setMaxTimes(noticeTemplate.getMaxtimes());
		notice.setTimes(1);
		if(player != null) {
			notice.setServerId(player.getServerId());
		}else{
			notice.setServerId(serverId);
		}
		long playerId = player != null ? player.getId():0;
		sendNoticeAndChat(playerId, notice, ChatRoomType.getType(noticeTemplate.getChat_channel()), NoticeMsgType.getType(noticeTemplate.getDisplay_type()));
	}


	private String getFishRecordContent(Player player, NoticeTemplate noticeTemplate, int fishId) {
		ActiveFishTemplateImpl template = fishConfig.getFishTemplateById(fishId);
		if(template == null || template.getShow() != 1){
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String fishName = langeConfig.getValue(template.getName());
		return String.format(content, player.getName(), fishName);
	}

	private String getActFishContent(Player player, NoticeTemplate noticeTemplate, int fishId) {
		ActiveFishFcTemplateImpl template = act2063Config.getFishTemplateById(fishId);
		if(template == null || template.getShow() != 1){
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String fishName = langeConfig.getValue(template.getName());
		return String.format(content, player.getName(), fishName);
	}

    private String getElementAdd(Player player, NoticeTemplate noticeTemplate, int elementId, long num, LogType logType) {
        if (logType.getCode() != LogType.ElementResearch.getCode()) {
            return "";
        }
		ControlConfig controlConfig = SpringUtil.getBean(ControlConfig.class);
		ItemElementExtraTemplate template = controlConfig.getElement(elementId);
		if (template.getLevel() < Integer.parseInt(noticeTemplate.getParms())) {
			return "";
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
        return String.format(content, player.getName(), num, template.getLevel(), getElementName(template.getColor()));
	}

	private String getElementLvUp(Player player, NoticeTemplate noticeTemplate, int elementId) {
		ControlConfig controlConfig = SpringUtil.getBean(ControlConfig.class);
		ItemElementExtraTemplate template = controlConfig.getElement(elementId);
		if (template.getLevel() < Integer.parseInt(noticeTemplate.getParms())) {
			return "";
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, player.getName(), getElementName(template.getColor()), template.getLevel());
	}

	private String getElementName(int color) {
		switch (color) {
			case 1:
				return "火控";
			case 2:
				return "装甲";
			case 3:
				return "传感";
		}
		return "";
	}


	private String getTankDriverAdChange(Player player, int tankId, NoticeTemplate noticeTemplate) {
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		Tank tank = player.playerTank().getTank(tankId);
		
		if(tankSetting == null || null==tank) {
			return null;
		}
		int maxLv = tank.getDriver().getAdvanceMaxLv();
		DriverAdvanceTemplateImpl advance = advanceConfig.getTannkAdvance(maxLv);
		if(advance == null) {
			return null;
		}
		String fetterName = langeConfig.getValue(advance.getName());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String tankName = langeConfig.getValue(tankSetting.getName());
		return String.format(content, player.getName(), tankName, fetterName);
	}

	private String getAgentContent(Player player, int agentId, NoticeTemplate noticeTemplate) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		AgentBaseTemplateImpl cfg = agentConfig.getBaseCfg(agentId);
		String agentName;
		if (cfg != null) {
			agentName = langeConfig.getValue(cfg.getName());
		} else {
			agentName = String.valueOf(agentId);
		}
		return String.format(content, player.getName(), agentName);
	}

	//大总统%s将%s、%s、%s指定为中心城市，产出双倍
	private String getPowerCenterCity(Player player, Object[] argv,
			NoticeTemplate noticeTemplate) {
		List<Integer> cityIds = (List<Integer>) argv[0];
		String content = langeConfig.getValue(noticeTemplate.getKey());
		List<String> cityNames = cityIds.stream().map(cityId ->{
			CityTemplate cityTemplate = cityConfig.getCityById(cityId);
			return langeConfig.getValue(cityTemplate.getName());
		}).collect(Collectors.toList());
		return String.format(content, player.getName(),cityNames.get(0),cityNames.get(1),cityNames.get(2));
	}
	
	//大总统%s已颁布军事政策，战车损毁获得额外经验
	private String getPowerPolicy(Player player, Object[] argv,
			NoticeTemplate noticeTemplate) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, player.getName());
	}
		
	//大总统%s制裁了指挥官%s，以儆效尤
	private String getPowerPunish(Player player, Object[] argv,
			NoticeTemplate noticeTemplate) {
		int id = Integer.parseInt(argv[0].toString());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, player.getName(),RedisUtil.getPlayerRedisData(id).getName());
	}

	private String getTankEvolveStarUp(Player player, NoticeTemplate noticeTemplate, int tankId) {
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		Tank tank = player.playerTank().getTank(tankId);
		if(tankSetting == null || null==tank) {
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String tankName = langeConfig.getValue(tankSetting.getName());
		int evolveStar = tank.getEvolveStar();
		//5星以下广播为红星，5-10星广播为彩星,1星以上为太阳
		return String.format(content, player.getName(), tankName, getEvolveName(evolveStar));
	}

	public String getEvolveName(int evolveStar){
		int star = evolveStar % 5 != 0 ? evolveStar % 5 : 5 ;
		if (evolveStar > 10){
			return star + "太阳";
		} else if (evolveStar > 5){
			return star + "彩星";
		}else {
			return star + "红星";
		}
	}
	
	public String getAirStarName(int star){
		int relstar = star % 5 != 0 ? star % 5 : 5 ;
		if (star > 15){
			return relstar + "太阳";
		} else if (star > 10){
			return relstar + "彩星";
		}else if(star>5){
			return relstar + "红星";
		}
		return relstar+"星";
	}
	
	private String getKfManorOcc(NoticeTemplate noticeTemplate, String serverName,String manorName) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String tankName = langeConfig.getValue(manorName);
		
		return String.format(content, serverName, tankName);
	}
	private String getKfServerName(NoticeTemplate noticeTemplate, String serverName) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, serverName);
	}
	
	private String getKfBuildHunterFind(NoticeTemplate noticeTemplate, String playerName,String val) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, playerName,val);
	}
	
	private String getEggGetTrueItem(NoticeTemplate noticeTemplate, String playerName,String ItemName) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, playerName, ItemName);
	}
	private String getKfExpedetion(NoticeTemplate noticeTemplate, int cityId) {
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		String cityName = langeConfig.getValue(cityTemplate.getName());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, cityName);
	}

	private String getAddEquidMsg(Player player, NoticeTemplate noticeTemplate, int itemId) {
		PlayerArmExtraTemplate template = equipmentConfig.getEquTemplate(itemId);
		String content = langeConfig.getValue(noticeTemplate.getKey());
		if(template.getQuality()==5) {
			return String.format(content, player.getName(), langeConfig.getValue(template.getName()), "");
		}else if(template.getQuality()==6) {
			return String.format(content, player.getName(), "", langeConfig.getValue(template.getName()));
		}else {
			return null;
		}
	}
	
	private String getUseUseTacticsMsg(Player player, NoticeTemplate noticeTemplate,AbstractGuildTactics guildTactics) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String tacticsName = guildTactics.getType().getDesc();
		Guild guild = guildBiz.getGuild(player);
		if(guild == null) {
			return null;
		} 
		return String.format(content, guild.getGuildInfo().getGuildName(), tacticsName);
	}
	
	//部落迁城事件
	private String getGuildChangeMainCity(Player player, NoticeTemplate noticeTemplate, int oldCityId, int newCityId) {
		CityTemplate cityTemplate = cityConfig.getCityById(newCityId);
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String cityName = langeConfig.getValue(cityTemplate.getName());
		return String.format(content, player.getName(), cityName);
	}

	//处理
	private String doTankAdd(Player player,int tankId,NoticeTemplate noticeTemplate) {
		int minLv = Integer.parseInt(noticeTemplate.getParms());
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(tankSetting == null || tankSetting.getRare() < minLv) {
			return null;
		}
		TankRareType tankRareType = TankRareType.getType(tankSetting.getRare());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String tankName = langeConfig.getValue(tankSetting.getName());
		
		//恭喜%s获得强力%s级坦克%s，称霸天下指日可待！
		return String.format(content, player.getName(), tankRareType.getDesc(),tankName);
	}
	//获得飞机
	private String doAircraftCarrierAdd(Player player,int id,NoticeTemplate noticeTemplate) {
		int minLv = Integer.parseInt(noticeTemplate.getParms());
		ItemBattleplaneTemplateImpl template = aircraftCarrierConfig.getAirTemplate(id);
		if(template == null || template.getQuality() < minLv) {
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String airName = langeConfig.getValue(template.getName());
		
		//恭喜%s获得强力飞机%s，称霸天下指日可待！
		return String.format(content, player.getName(),airName);
	}
	
	//处理
	private String doAircraftStarUp(Player player,String uid,NoticeTemplate noticeTemplate) {
		Aircraft aircraft = player.playerAircraft().getAircraft(uid);
		if(aircraft==null) {
			return null;
		}
		
		int minLv = Integer.parseInt(noticeTemplate.getParms());
		ItemBattleplaneTemplateImpl template = aircraftCarrierConfig.getAirTemplate(aircraft.getId());
		if(template == null || template.getQuality() < minLv) {
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String airName = langeConfig.getValue(template.getName());
		
//		String[] starName = {"","一","二","三","四","五","六"};
		//恭喜%s将%s升到了%s星，实力大增！
		return String.format(content, player.getName(), airName,getAirStarName(aircraft.getStar()));
	}
	
	private String doTankStarUp(Player player,int tankId,NoticeTemplate noticeTemplate) {
		int minLv = Integer.parseInt(noticeTemplate.getParms());
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		Tank tank = player.playerTank().getTank(tankId);
		if(tankSetting == null || tank == null || tank.getStar() < minLv) {
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String tankName = langeConfig.getValue(tankSetting.getName());
		
//		String[] starName = {"","一","二","三","四","五","六"};
		//恭喜%s将%s升到了%s星，实力大增！
		return String.format(content, player.getName(), tankName,tank.getStar());
	}

	
	//
	private String doDriverLv(Player player,int tankId,NoticeTemplate noticeTemplate) {
		int minLv = Integer.parseInt(noticeTemplate.getParms());
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		Tank tank = player.playerTank().getTank(tankId);
		if(tankSetting == null || tank == null || tank.getDriver().getLv() != minLv) {
			return null;
		}
		DriverTemplate DriverTemplate = tankConfig.getDriverTemplate(tankId);
		if(DriverTemplate == null) {
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String driverName = langeConfig.getValue(DriverTemplate.getName());
		
		//恭喜%s将名将%s升到了%s级，如虎添翼！
		return String.format(content, player.getName(), driverName,tank.getDriver().getLv());
	}
	private String doMilitaryLv(Player player,boolean isChangeLv,NoticeTemplate noticeTemplate) {
		if(!isChangeLv) {
			return null;
		}
		List<Integer> lvList = StringUtil.splitStr2IntegerList(noticeTemplate.getParms(), ",");
		int playerMiLv = player.playerCommander().getMilitaryLv();
		if(!lvList.contains(playerMiLv)) {
			return null;
		}
		MilitaryExtraTemplate militaryExtraTemplate = commanderConfig.getMilitaryExtraTemplate(playerMiLv);
		if(militaryExtraTemplate == null) {
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String name = langeConfig.getValue(militaryExtraTemplate.getName());
		
		//表彰！%s为世界和平做了杰出贡献，荣升%s军衔
		return String.format(content, player.getName(), name);
	}

	
	private String getCityOccupyContent(Player player, AtkDefType warType, int cityId, NoticeTemplate noticeTemplate) {
		if(warType!=AtkDefType.ATK) {
			return null;
		}
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		String cityName = langeConfig.getValue(cityTemplate.getName());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		//号外！%s刚刚击败%s，取得了竞技场第%s名！
		return String.format(content, player.getName(),cityName);
	}
	
	private String getPveContent(Player player,NoticeTemplate noticeTemplate,WorldCity worldCity) {
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		String cityName = langeConfig.getValue(cityTemplate.getName());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, cityName);
	}
	
	private String getKillGhostBoss(Player player,NoticeTemplate noticeTemplate) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, player.getName());
	}


	private String getVowRateContent(Player player ,NoticeTemplate noticeTemplate,int multiple){
		//5倍以下不发送广播
		if(multiple<5){
			return null;
		}
		String content = langeConfig.getValue(noticeTemplate.getKey());
		//喜讯！%s在踏青活动中获得了%s倍增益，堪比欧皇！
		return String.format(content, player.getName(),multiple);
	}
	
	private String getWorldBossKillContent(Player player ,NoticeTemplate noticeTemplate,String name){
		String content = langeConfig.getValue(noticeTemplate.getKey());
		//世界boss击杀广播
		return String.format(content, player.getName(),name);
	}
	
	private String getFindBlackMainCity(Player player,WorldCity worldCity, NoticeTemplate noticeTemplate) {
		CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
		String cityName = langeConfig.getValue(cityTemplate.getName());
		String content = langeConfig.getValue(noticeTemplate.getKey());
		//号外！%s刚刚击败%s，取得了竞技场第%s名！
		return String.format(content, player.getName(),cityName);
	}
	
	private String getYearGetItems(NoticeTemplate noticeTemplate, String playerName, String itemName, int times) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, playerName, itemName, times);
	}
	
	private String getKFscoreCityLose(NoticeTemplate noticeTemplate, int toServerId) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		String serverName = ServerNameCache.getServerName(toServerId);
		return String.format(content, serverName);
	}
	private String getOpenBerlinBossContent(Player player, NoticeTemplate noticeTemplate) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, player.getName());
	}

	private String getCaptiveTankContent(Player player, NoticeTemplate noticeTemplate, CaptiveTankInfo captiveTankInfo) {
		if (captiveTankInfo == null || !captiveTankInfo.isPlayerTank()){
			return null;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(captiveTankInfo.getTankId());
		if(tankSetting == null) {
			return null;
		}
		String tankName = langeConfig.getValue(tankSetting.getName());
		return String.format(langeConfig.getValue(noticeTemplate.getKey()), player.getName(), captiveTankInfo.getName(), tankName);
	}
	
	private String getAct97GetItems(NoticeTemplate noticeTemplate, String playerName, String itemName) {
		String content = langeConfig.getValue(noticeTemplate.getKey());
		return String.format(content, playerName, itemName);
	}

	//需要发送到广播中，需要发送聊天
	public void sendNoticeAndChat(long playerId, NewNotice notice, ChatRoomType roomType, NoticeMsgType noticeMsgType){
		if(null!=notice && !StringUtils.isEmpty(notice.getContent())) {
			//给在线用户发送广播信息
			switch(noticeMsgType) {
				case Sys:
					this.sendToOnlineUser(notice, MessageComm.S2C_Player_Notice);
					break;
				case Notice:
					this.sendToOnlineUser(notice, MessageComm.S2C_SendSysNotice);
					break;
				default:
					break;
			}
			
			//发送到聊天服
			if(null!=roomType) {
				this.sendNoticeToChat(playerId, notice, roomType);
			}
		}
	}
	
	//需要发送到广播中，需要发送聊天
	public void sendSysNoticeAndChat(long playerId, NewNotice notice){
		if(null!=notice && !StringUtils.isEmpty(notice.getContent())) {
			Cloner cloner = new Cloner();
			NewNotice tempNotice = cloner.deepClone(notice);
			tempNotice.setMaxTimes(1);
			//给在线用户发送广播信息
			this.sendToOnlineUser(tempNotice, MessageComm.S2C_SendSysNotice);
			//发送到聊天服
			if(notice.getTimes() == 1) {//只有第一次发送到聊天服务器
				this.sendNoticeToChat(playerId, tempNotice,ChatRoomType.System);
			}
		}
	}
	//给聊天服发送信息
	public void sendNoticeToChat(long playerId, NewNotice notice,ChatRoomType roomType) {
		innerChatFacade.sendSysMsg(playerId, notice.getServerId(),0,notice.getContent(), roomType,ChatMsgType.Notice,null);
	}
	//给在线用户发送广播信息
	public void sendToOnlineUser(NewNotice notice, int messageType) {
		JsonMsg msg = JsonMsg.create(messageType);
        msg.addProperty("notice", notice);
        PlayerContainer.broadPlayer(notice.getServerId(), msg);
	}


}















