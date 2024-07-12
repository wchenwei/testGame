package com.hm.action.guild.biz;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.action.cityworld.biz.WorldAreaBiz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.biz.WorldCityBiz;
import com.hm.action.guild.util.GuildFinanceCache;
import com.hm.libcore.language.LanguageVo;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.model.camp.CityReward;
import com.hm.model.camp.CityRewardShow;
import com.hm.model.camp.ExtraCityReward;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.FinanceTatics;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.ItemConstant;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GuildWorldBiz {
	@Resource
	private CityConfig cityConfig;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
    private GuildTechBiz guildTechBiz;
	@Resource
    private MailBiz mailBiz;
	@Resource
    private LanguageCnTemplateConfig languageCnTemplateConfig;
	@Resource
	private WorldAreaBiz worldAreaBiz;
	@Resource
	private WorldCityBiz worldCityBiz;

	/**
	 * 发送攻占城池奖励
	 * @param guild
	 * @param
	 */
	public void sendOccupyCityReward(Guild guild,int cityId) {
		FinanceTatics financeTatics = (FinanceTatics)guild.getGuildTactics().getGuildTactics(GuildTacticsType.FinanceTatics,cityId);
		if(financeTatics != null) {
			Items rewardItems = financeTatics.getItems();//发送奖励
			doMailOccupyCityReward(guild, cityId, rewardItems,financeTatics.getStartDate());
			guild.getGuildTactics().removeGuildTactics(financeTatics);
			if(guild.getGuildTactics().isEmpty()) {
				guild.saveDB();
				guildTechBiz.boradGuildTacticsUpdate(guild);//广播次数变化
			}
		}
	}
	
	public void doMailOccupyCityReward(Guild guild,int cityId,Items rewardItems,String startDate) {
		//今日可以领取金融战奖励的玩家id
        //金融战开始的日期
		Set<Player> joinPlayers = guild.getGuildMembers().getGuildMembers().stream()
				.filter(e -> GuildFinanceCache.todayCanFinance(e.getPlayerId(),startDate))
				.map(e -> PlayerUtils.getPlayer(e.getPlayerId()))
				.collect(Collectors.toSet());
		if (CollUtil.isEmpty(joinPlayers)) {
			return;
		}
		//添加金融战领取次数
		for (Player joinPlayer : joinPlayers) {
			System.out.println(joinPlayer.getId() + "领取金融战奖励" + cityId+"_"+startDate);
			GuildFinanceCache.addFinanceLog(joinPlayer.getId(),startDate);
		}
		//发送奖励
		Set<Long> receivers = joinPlayers.stream()
				.map(e -> e.getId()).collect(Collectors.toSet());
		String cityName = languageCnTemplateConfig.getValue(cityConfig.getCityById(cityId).getName());
		mailBiz.sendSysMail(guild.getServerId(), receivers, MailConfigEnum.FinanceTatics, Lists.newArrayList(rewardItems), LanguageVo.createStr(cityName));
		guildTechBiz.boradGuildTacticsUpdate(guild);
	}

	
	public int getMoveMainCitySpendGold(Guild guild,boolean isQianDu) {
		if(isQianDu) {
			return commValueConfig.getCommValue(CommonValueType.GuildMoveMainCity);
		}
		return 0;
	}
	
	private List<Integer> getExtraRewardCityList(Guild guild) {
		//获取额外城市加成次数（科技：205 有贸易商人的时候）
		double cityAdd = guildTechBiz.getGuildTecAdd(guild, GuildTecFunEnum.CityResHour);
		if(cityAdd>0) {
			//随机选出城市
			int randomNum = new Double(cityAdd).intValue();
			List<Integer> cityIds = cityConfig.getAllCityIds(WorldType.Normal).stream().filter(f->cityConfig.getCityById(f).getCity_type()!=3).collect(Collectors.toList());
			return RandomUtils.randomEleList(cityIds, randomNum);
		}
		return Lists.newArrayList();
	}
	
	public void doGuildCityReward(Guild guild,List<Integer> cityList) {
		CityReward cityReward = new CityReward(DateUtil.thisHour(true),cityList);
		//部落内玩家贸易商人随机城池奖励一样
		List<Integer> extraRewardCityList = getExtraRewardCityList(guild);
		cityReward.setExtraCityList(extraRewardCityList);

		double resourceAdd = guildTechBiz.getGuildTecAdd(guild, GuildTecFunEnum.WarResource);
		double rewardHour = guildTechBiz.getGuildTecAdd(guild, GuildTecFunEnum.CityResDoub);
		double cityResBox = guildTechBiz.getGuildTecAdd(guild, GuildTecFunEnum.CityResBox);
		//是否可领取二级城双倍奖励
		boolean checkDoubleReward = checkDouble(guild.guildTechnology().getRewardDate(), rewardHour);
		if(checkDoubleReward) {
			cityReward.addRewardRate(GuildRewardTimesType.RewardDouble, 1);
			guild.guildTechnology().resetRewardDate();
		}
		
		cityReward.addRewardRate(GuildRewardTimesType.RewardDoublePer, guildTechBiz.getGuildTecAdd(guild, GuildTecFunEnum.ResDoubChance));
		
		//额外奖励
		if(cityResBox > 0) {
			Items item = new Items(new Double(cityResBox).intValue(), cityList.size(), ItemType.ITEM.getType());
			cityReward.addExtraItems(item);
		}
		if(resourceAdd > 0) {
			cityReward.addRewardRate(GuildRewardTimesType.RewardAdd, resourceAdd);
		}
		double techCreditAdd = guildTechBiz.getGuildTecAdd(guild, GuildTecFunEnum.Credit);
		if(techCreditAdd>0) {
			cityReward.addRewardRate(GuildRewardTimesType.CreditAdd, techCreditAdd);
		}
		guild.getGuildReward().addCityReward(buildCityRewardShow(guild,cityReward));
	}
	
	public CityRewardShow buildCityRewardShow(Guild guild, CityReward cityReward) {
		List<Items> returnItem = Lists.newArrayList();
		double baseRate = cityReward.getRewardRate(GuildRewardTimesType.RewardAdd);
		double doubleRate = cityReward.getRewardRate(GuildRewardTimesType.RewardDouble);
		double doubleRatePer = cityReward.getRewardRate(GuildRewardTimesType.RewardDoublePer);
		List<Integer> cityList = Lists.newArrayList(cityReward.getCityList());
		Map<Integer,Double> areaBuffMap = worldAreaBiz.calCityAreaBuff(guild,cityList);

		
		for (int cityId : cityList) {
			CityTemplate cityTemplate = cityConfig.getCityById(cityId);
			List<Items> cityItemList = Lists.newArrayList();
			cityItemList.addAll(cityTemplate.getResourceItem());//基础奖励
			if(commValueConfig.isCanSpecialReward(cityReward.getHour())) {
				cityItemList.addAll(cityTemplate.getSpecialItem());//额外奖励
			}
			
			double doubleCityRate = cityTemplate.isBigCity()?doubleRate:0d;

			double doubleCityRatePer = (cityTemplate.isBigCity() && doubleRatePer>Math.random())?1:0d;
			//增加岛屿buff
			doubleCityRatePer += areaBuffMap.getOrDefault(cityId,0d);

			//添加城池奖励加成，钞票有所的加成都有效
			for (Items item : cityItemList) {
				//功勋增加固定的数量
				if(item.getItemType() == ItemType.CURRENCY.getType() && PlayerAssetEnum.Credit.getTypeId()==item.getId()) {
					item.addCount(new Double(cityReward.getRewardRate(GuildRewardTimesType.CreditAdd)).longValue());
				}
				if(item.getEnumItemType()==ItemType.ITEM && item.getId() == ItemConstant.Meat_Id) {
					//钞票跟着奖励倍数走
					item.addCountRate(baseRate + doubleCityRate + doubleCityRatePer);
				}else {
					item.addCountRate(doubleCityRate + doubleCityRatePer);
				}
			}
			returnItem.addAll(cityItemList);
		}
		returnItem.addAll(cityReward.getItemsList());//随机宝箱

		CityRewardShow cityRewardShow = cityReward.buildCityRewardShow();
		cityRewardShow.setItemsList(ItemUtils.mergeItemList(returnItem));
		//计算额外城池奖励
		List<Integer> extraCityList = cityReward.getExtraCityList();
		if(CollUtil.isNotEmpty(extraCityList)) {
			cityRewardShow.setExtraCityReward(calExtraCityReward(cityReward.getHour(),extraCityList));
		}
		return cityRewardShow;
	}
	
	/**
	 * 计算额外城池的基础奖励
	 * @param cityList
	 * @return
	 */
	private ExtraCityReward calExtraCityReward(int hour,List<Integer> cityList) {
		List<Items> cityItemList = Lists.newArrayList();
		for (int cityId : cityList) {
			CityTemplate cityTemplate = cityConfig.getCityById(cityId);
			cityItemList.addAll(cityTemplate.getResourceItem());//基础奖励
			if(commValueConfig.isCanSpecialReward(hour)) {
				cityItemList.addAll(cityTemplate.getSpecialItem());//额外奖励
			}
		}
		return new ExtraCityReward(ItemUtils.mergeItemList(cityItemList), cityConfig.calCityTypeNum(cityList));
	}
	
	//校验是否可以双倍
	public boolean checkDouble(long rewardDate, double rewardHour) {
		if(rewardHour>0) {
			//第一次时间为空。判断上次时间，距离当前时间是否大于双倍奖励发放时间间隔
			Date startDate = new Date(rewardDate);
			Date endDate = new Date();
			if(0==rewardDate 
					|| DateUtil.hour(endDate, true)-DateUtil.hour(startDate, true)>=rewardHour
					|| endDate.getTime()-rewardDate>GameConstants.HOUR) {
				return true;
			}
		}
		return false;
	}
	
	//校验是否可以双倍(根据双倍的概率)
	public boolean checkDoubleChance(double chance) {
		return RandomUtils.randomIsRate(chance);
	}
	
	//清理部落领地信息
	public void clearGuildArea(Player player,Guild guild) {
		WorldServerContainer.of(guild).getWorldCitys(WorldType.Normal).stream()
			.filter(e -> e.getBelongGuildId() == guild.getId())
			.forEach(worldCity -> {
				worldCityBiz.giveUpCity(player,worldCity);
			});
	}

	public void guildWinWorldCity(Player player, int winGuildId, WorldCity worldCity) {
		//设置胜利阵营
		worldCity.getCityBelong().setGuildId(winGuildId);
		if(winGuildId > 0 && player != null) {
			Guild guild = guildBiz.getGuild(player);
			if(guild != null) {
				worldCity.getCityBelong().setGuildId(guild.getId());
				sendOccupyCityReward(guild, worldCity.getId());
			}else{
				worldCity.getCityBelong().setGuildId(0);
			}
		}
	}
}
