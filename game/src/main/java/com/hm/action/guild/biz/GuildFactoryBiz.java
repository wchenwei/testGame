package com.hm.action.guild.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.guild.vo.ArmsVo;
import com.hm.annotation.Broadcast;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildFactoryConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.GuildFactoryBuildExtraTemplate;
import com.hm.config.excel.templaextra.GuildFactoryWeaponUpgradeExtraTemplate;
import com.hm.config.excel.templaextra.ItemGuildWeaponExtraTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.libcore.annotation.Biz;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.player.*;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.centreArms.CentreArmsContainer;
import com.hm.servercontainer.centreArms.CentreArmsItemContainer;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * @desc 部落军工厂biz
 * @author xjt
 * @date 2020年4月2日10:31:58
 */
@Biz
public class GuildFactoryBiz extends NormalBroadcastAdapter {
	
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private CommValueConfig commValueConfig;

	@Resource
	private GuildFactoryConfig guildFactoryConfig;
	@Resource
	private TankConfig tankConfig;

	
	public void build(Player player, Guild guild, int type, int count){
		GuildFactoryBuildExtraTemplate template = guildFactoryConfig.getBuildTemplate(type);
		int exp = template.getExp_add() * count;//同时增加经验
		int parts = template.getPart_add() * count;//同时增加配件
		guild.guildFactory().addExp(exp);
		guild.guildFactory().addParts(parts);
		GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(player.getId());
		if(guildPlayer!=null){
			guildPlayer.build(type, count);
		}
		//记录建设日志
		guild.guildFactory().addBuildRecord(player,type, count);
	}

    public int produce(Player player, Guild guild) {
		guild.guildFactory().addProsperity(commValueConfig.getCommValue(CommonValueType.GuildFactoryScore));
        int index = guild.guildFactory().checkProsperity();
		//记录建设日志
		guild.guildFactory().addProduceRecord(player);
        return index;
	}
	
	public void sendArmsPosChange(BasePlayer player){
		CentreArms centreArms = CentreArmsContainer.of(player).getCentreArms(player.getId());
		player.sendMsg(MessageComm.S2C_GuildFactory_ArmsPosChange,centreArms);
	}
	//获取玩家武器属性加成(全体加成)
	public TankAttr getArmsAllAttr(Player player) {
		TankAttr tankAttr = new TankAttr();
		CentreArmsItemContainer centreArmsItemContainer  = CentreArmsContainer.of(player);
		if(centreArmsItemContainer == null) {
			return null;
		}
		CentreArms centreArms = centreArmsItemContainer.getCentreArms(player.getId());
		if(centreArms==null){
			return null;
		}
		for(ArmsPosition armsPosition : centreArms.getArms()){
			if(armsPosition==null){
				continue;
			}
			Arms arms = armsPosition.getArms();
			if(armsPosition!=null&&armsPosition.getState()==2&&arms!=null){
				GuildFactoryWeaponUpgradeExtraTemplate template = guildFactoryConfig.getWeaponUpgradeTemplate(arms.getId(), arms.getLv());
				ItemGuildWeaponExtraTemplate weaponTemplate = guildFactoryConfig.getWeapon(arms.getId());
				if(template!=null&&weaponTemplate!=null&weaponTemplate.getType()==GameConstants.Arms_All_Type){
					tankAttr.addAttr(template.getAttrMap());
				}
			}
		}
		return tankAttr;
	}
	//把玩家放在部落强化位上的装备强行卸下
	public void armsStrengthDown(Guild guild, Player player) {
		int exp = commValueConfig.getCommValue(CommonValueType.GuildFactoryExpOnce);
		ArmsVo[] armVos = guild.guildFactory().getArms();
		for(int i=0;i<armVos.length;i++){
			ArmsVo vo = armVos[i];
			if(vo!=null&&vo.getPlayerId()==player.getId()){
				if(System.currentTimeMillis()>=vo.getEndTime()){
					Arms arms = player.playerArms().getArms(vo.getUid());
					boolean lvUp = arms.addExp(exp);
					if(lvUp){
						//发出信号更新玩家属性
						player.notifyObservers(ObservableEnum.ArmsChange);
					}
				}
				//从部落强化位上拿下来
				guild.guildFactory().strengthDown(i);
			}
		}
		
	}

	public Map<Integer, TankAttr> getArmsAttr(Player player) {
		Map<Integer,TankAttr> typeMap = Maps.newConcurrentMap();
		CentreArmsItemContainer centreArmsItemContainer  = CentreArmsContainer.of(player);
		if(centreArmsItemContainer == null) {
			return typeMap;
		}
		CentreArms centreArms = centreArmsItemContainer.getCentreArms(player.getId());
		if(centreArms==null){
			return typeMap;
		}
		for(ArmsPosition armsPosition : centreArms.getArms()){
			if(armsPosition==null){
				continue;
			}
			Arms arms = armsPosition.getArms();
			if(armsPosition!=null&&armsPosition.getState()==2&&arms!=null){
				GuildFactoryWeaponUpgradeExtraTemplate template = guildFactoryConfig.getWeaponUpgradeTemplate(arms.getId(), arms.getLv());
				ItemGuildWeaponExtraTemplate weaponTemplate = guildFactoryConfig.getWeapon(arms.getId());
				if(template!=null&&weaponTemplate!=null&weaponTemplate.getType()!=GameConstants.Arms_All_Type){
					TankAttr tankAttr = new TankAttr();
					tankAttr.addAttr(template.getAttrMap());
					typeMap.put(weaponTemplate.getType(), tankAttr);
				}
			}
		}
		return typeMap;
	}

	public void checkPlayerArmsOpen(Player player) {
		Guild guild = guildBiz.getGuild(player);
		if(guild!=null){
			checkPlayerArmsOpen(player, guild);
		}
	}

	public void checkPlayerArmsOpen(Player player,Guild guild){
		if(player.playerArms().getState()==0&&isGuildFactoryUnlock(guild)){
			player.playerArms().setState(1);
		}
	}

	public List<Integer> getArmsSkillIds(Player player,
			Tank tank) {
		List<Integer> skillIds = Lists.newArrayList();
		try {
			CentreArmsItemContainer centreArmsItemContainer = CentreArmsContainer.of(player);
			if(centreArmsItemContainer == null) {
				return skillIds;
			}
			CentreArms  container = centreArmsItemContainer.getCentreArms(player.getId());
			if(container==null){
				return skillIds;
			}
			TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
			if(tankSetting==null){
				return skillIds;
			}
			//本坦克对应的武器
			ArmsPosition armsType = container.getArmsByType(tankSetting.getType());
			if(armsType!=null){
				ItemGuildWeaponExtraTemplate template1 = guildFactoryConfig.getWeapon(armsType.getArms().getId());
				if(template1!=null){
					skillIds.add(template1.getSkill_id());
				}
			}
			//给全体坦克加技能的武器
			ArmsPosition armsAll = container.getArmsByType(GameConstants.Arms_All_Type);
			if(armsAll!=null){
				ItemGuildWeaponExtraTemplate template2 = guildFactoryConfig.getWeapon(armsAll.getArms().getId());
				if(template2!=null){
					skillIds.add(template2.getSkill_id());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return skillIds;
	}

	public void clearArms(Player player) {
		CentreArms centreArms = CentreArmsContainer.of(player).getCentreArms(player.getId());
		if(centreArms!=null){
			ArmsPosition[] armsPositions = centreArms.getArms();
			for(int i=0;i<armsPositions.length;i++){
				if(armsPositions[i]!=null&&armsPositions[i].getArms()!=null){
					centreArms.down(i);
				}
			}
			centreArms.save();
			player.notifyObservers(ObservableEnum.ArmsChange);
			sendArmsPosChange(player);
		}
		for(Arms arms:player.playerArms().getAllArms()){
			if(arms.getPos()>=0){
				arms.setPos(-1);
				player.playerArms().SetChanged();
			}
		}
	}

	public boolean isGuildFactoryUnlock(Guild guild){
		int unlockLv = commValueConfig.getCommValue(CommonValueType.GuildFactoryUnlockLv);
		return guild.guildLevelInfo().getLv() >= unlockLv;
	}


	@Broadcast(ObservableEnum.GuildUpdateLv)
	public void doGuildUpdateLv(ObservableEnum observableEnum, Player player, Object... argv){
		Guild guild = (Guild) argv[0];
		if(isGuildFactoryUnlock(guild)){
			//如果部落等级大于5级则全部落乘员解锁武器
			for(GuildPlayer guildPlayer:guild.getGuildMembers().getGuildMembers()){
				Player p = PlayerUtils.getPlayer(guildPlayer.getPlayerId());
				if(p!=null){
					checkPlayerArmsOpen(p, guild);
					if(p.playerArms().Changed()){
						p.sendUserUpdateMsg();
					}
				}
			}
		}
	}
}
