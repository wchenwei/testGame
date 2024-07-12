package com.hm.action.guild.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.temlate.GuildDonateTemplateImpl;
import com.hm.config.excel.temlate.GuildTecTemplateImpl;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.GuildTechnology;
import com.hm.model.guild.tactics.AbstractCityTactics;
import com.hm.model.guild.tactics.ArtillerySupportTactics;
import com.hm.model.guild.tactics.RoadblockTatics;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.util.StringUtil;
import org.apache.commons.lang.ArrayUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: GuildTechBiz. <br/>  
 * Function: 部落科技相关. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2019年1月9日 下午4:05:54 <br/>  
 * @author zxj  
 * @version
 */
@Biz
public class GuildTechBiz extends NormalBroadcastAdapter {
	@Resource
	private GuildConfig guildConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildBiz guildBiz;

	
	/**
	 * donate:(部落捐献). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * @author zxj  
	 * @param guild
	 * @param player
	 * @param donate
	 */
	public List<Items> donate(Guild guild, Player player, GuildDonateTemplateImpl donate) {
		List<Items> reward = donate.getItemsReward();
		//给玩家增加奖励信息
		itemBiz.addItem(player, reward, LogType.GuildDonateReward);
		//增加玩家当日的捐献数据
		guild.getGuildMembers().addDayContr(player, donate.getContr());
		//增加捐献次数
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.GuildGold);
		this.guildAddExp(player, guild, donate.getGuild_exp());
		return reward;
	}
	
	public void guildAddExp(Player player, Guild guild, int exp) {

		//增加部落经验，已经更新等级
		guild.guildLevelInfo().addExp(exp);
		int nextLv = guildConfig.getGuildLv(guild.guildLevelInfo().getExp());
		int lv = guild.guildLevelInfo().getLv();
		if(nextLv>lv) {
			//处理部落科技点数
			for(int i=lv+1; i<=nextLv; i++) {
				guild.getGuildInfo().addTecPoints(guildConfig.getGuildLvPoint(i));
			}
			guild.guildLevelInfo().updateGuildLv(nextLv);
			//更新部落等级
			player.notifyObservers(ObservableEnum.GuildUpdateLv, guild);
		}
		guild.broadMemberGuildUpdate();

	}
	
	public void guildAddExp(Player player, int exp) {
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if(guild==null){
			return;
		}
		this.guildAddExp(player, guild, exp);
	}
	

	//根据部落获取科技家成template
	private Map<GuildTecTemplateImpl, Double> getTechTemplate(Guild guild, GuildTecFunEnum guildTecGun, FightType effectEnum) {
		Map<GuildTecTemplateImpl, Double> mapGuildTec = Maps.newHashMap();
		if(null==guild) {
			return mapGuildTec;
		}
		guild.guildTechnology().getTechnologys().forEach((tech, lv)->{
			GuildTecTemplateImpl guildTec = guildConfig.getGuilTec(tech);
			if(guildTec.getTec_func()==guildTecGun.getType() &&
				(guildTec.getEffective()==effectEnum.getType()
				||guildTec.getEffective()==FightType.All.getType())) {
				mapGuildTec.put(guildTec, guildTec.getTecValue(lv));
			}
		});
		return mapGuildTec;
	}
	
	//获取战斗属性加成
	public Map<Integer,Double> getFightAttrRate(Player player, FightType effectEnum) {
		Map<Integer,Double> rateMap = Maps.newHashMap();
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if(effectEnum == null || guild == null) {
			return rateMap;
		}
		getTechTemplate(guild, GuildTecFunEnum.UnitAttr, effectEnum)
		.entrySet().forEach(entry -> {
			int attrId = entry.getKey().getTec_value();
			rateMap.put(attrId, rateMap.getOrDefault(attrId, 0d)+entry.getValue());
		});
		return rateMap;
	}
	//获取坦克技能，永久生效的会自动加上
	public Map<Integer,Integer> getGuildTechSkills(Guild guild,Player player,int cityId,FightType fightType) {
		Map<Integer,Integer> skillLvs = Maps.newHashMap();
		if(guild == null || cityId == 0) {
			return skillLvs;
		}
		getTechTemplate(guild, GuildTecFunEnum.SkillLv, fightType)
		.entrySet().forEach(entry -> {
			int skillId = entry.getKey().getTec_value();
			int lv = new Double(entry.getValue()).intValue();
			skillLvs.put(skillId, lv);
		});
		//查看是否有炮火支援技能
		RoadblockTatics roadblockTatics = (RoadblockTatics)getGuildCityTactics(player, GuildTacticsType.Roadblock, cityId);
		if(roadblockTatics != null) {
			skillLvs.put(roadblockTatics.getSkillId(), 1);
		}
		return skillLvs;
	}
	
	//查看是否有炮火支援技能
	public int getArtillerySupportSkillId(Player player,int cityId) {
		ArtillerySupportTactics artillerySupportTactics = (ArtillerySupportTactics)getGuildCityTactics(player, GuildTacticsType.ArtillerySupport, cityId);
		return artillerySupportTactics != null ?artillerySupportTactics.getSkillId():0;
	}
	
	public double getGuildTecAdd(Player player, GuildTecFunEnum guildTecGun) {
		if(player.getGuildId() <= 0) {
			return 0;
		}
		Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
		if(guild == null) {
			return 0;
		}
		return getGuildTecAdd(guild,guildTecGun);
	}

	//
	public double getGuildTecAdd(Guild guild, GuildTecFunEnum guildTecGun) {
		GuildTechnology guildTechnology = guild.guildTechnology();
		for (GuildTecTemplateImpl template : guildConfig.getGTecTemplates(guildTecGun)) {
			int lv = guildTechnology.getTecLvById(template.getId());
			if(lv > 0) {
				return template.getTecValue(lv);
			}
		}
		return 0;
	}
	public GuildTecTemplateImpl getGuildTecTemplate(Guild guild, GuildTecFunEnum guildTecGun) {
		return guildConfig.getGTecTemplates(guildTecGun).get(0);
	}

	
	/**
	 * 获取部落对城池的战术
	 * @param player
	 * @param type
	 * @param cityId
	 * @return
	 */
	public AbstractCityTactics getGuildCityTactics(Player player,GuildTacticsType type,int cityId) {
		Guild guild = guildBiz.getGuild(player);
		if(guild == null) {
			return null;
		}
		return guild.getGuildTactics().getGuildTactics(type,cityId);
	}
	
	/**
	 * reduceTecPoints:(校验科技点数，如果通过则直接减去). <br/>  
	 * @author zxj  
	 * @param guild
	 * @param tecPointCost
	 * @return  使用说明
	 */
	public boolean reduceTecPoints(Guild guild, int tecPointCost) {
		return guild.getGuildInfo().reduceTecPoints(tecPointCost);
	}

	/**
	 * resetTech:(重置科技). <br/>  
	 * @author zxj  
	 * @param guild  使用说明
	 */
	public void resetTech(Guild guild) {
		//获取点数
		int points = guildConfig.getGuildAllPoint(guild.guildLevelInfo().getLv());
		//清除科技信息
		guild.guildTechnology().cleanTec();
		//重置科技点数
		guild.getGuildInfo().resetPoints(points);
	}
	/**
	 * isCanUpdate:(判断部落科技是否能升级). <br/>  
	 * @author zxj  
	 * @return  使用说明
	 */
	public boolean isCanUpdate(Guild guild, int techId) {
		ConcurrentHashMap<Integer, Integer> mapTech = guild.guildTechnology().getTechnologys();
		GuildTecTemplateImpl tempTec = guildConfig.getGuilTec(techId);
		//默认解锁
		if(tempTec.getDefault_lock()==0) {
			return true;
		}
		for(int key :mapTech.keySet()) {
			GuildTecTemplateImpl guildTec = guildConfig.getGuilTec(key);
			//判断后置科技是否包含要判断的科技id
			int[] techArr = StringUtil.strToIntArray(guildTec.getNext_tec(), ",");
			if(ArrayUtils.contains(techArr, techId) && mapTech.get(key)==guildTec.getMax_level()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 玩家登陆时发送战术信息
	 * @param player
	 */
	public void sendPlayerGuildTactics(Player player) {
		Guild guild = guildBiz.getGuild(player);
		if(guild != null) {
			List<BaseGuildTacticsVo> voList = guild.getGuildTactics().createGuildTacticsVo();
			if(CollUtil.isNotEmpty(voList)) {
				JsonMsg msg = new JsonMsg(MessageComm.S2C_Guild_TacticsUpdate);
				msg.addProperty("guildTacticsList", voList);
				player.sendMsg(msg);
			}
		}
	}
	
	/**
	 * 战术数据发生变化时更新
	 * @param guild
	 */
	public void boradGuildTacticsUpdate(Guild guild) {
		if(guild != null) {
			List<BaseGuildTacticsVo> voList = guild.getGuildTactics().createGuildTacticsVo();
			JsonMsg msg = new JsonMsg(MessageComm.S2C_Guild_TacticsUpdate);
			msg.addProperty("guildTacticsList", voList);
			guildBiz.broadGuildMember(guild, msg);
		}
	}
}








