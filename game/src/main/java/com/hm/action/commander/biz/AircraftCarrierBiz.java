package com.hm.action.commander.biz;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.AircraftCarrierConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.CommonValueType;
import com.hm.enums.PlayerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Aircraft;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerAircraftCarrier;
import com.hm.model.tank.TankAttr;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.PlayerAirFormation;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Biz
public class AircraftCarrierBiz implements IObserver{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private AircraftCarrierConfig aircraftCarrierConfig;
	@Resource
	private CommValueConfig commValueConfig;
	
	
	//是否可以消耗飞机对某飞机进行升星
	public boolean isCanStarUp(Player player,int id,List<Aircraft> aircrafts) {
		if(aircrafts.stream().anyMatch(t ->player.playerAircraftCarrier().isAircraftUp(t.getUid())||t.getId()!=id)) {
			return false;
		}
		return true;
	}
	

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock,this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftCarrierEit,this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftCarrierLvUp,this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AircraftCarrierBuild,this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum){
			case FunctionUnlock:
				if((int)argv[0] == PlayerFunctionType.AircraftCarrier.getType()) {
					player.notifyObservers(ObservableEnum.AircraftCarrierLvUp);
				}
				break;
			case AircraftCarrierEit:
				checkUpdateAirFormation(player);
				break;
			case AircraftCarrierLvUp:
			case AircraftCarrierBuild:
				checkIsland(player);
				break;
		}
	}
	
	private void checkUpdateAirFormation(Player player) {
		PlayerAirFormation formation = PlayerAirFormation.getOrCreate(player.getId());
		formation.checkUpdateAirFormation(player.playerAircraftCarrier().getAircraftList());
		sendPlayerAirFormation(player);
	}


	/**
	 * 计算航母属性
	 * @param player
	 * @return
	 */
	public TankAttr calAircraftCarrierAttr(Player player){
		TankAttr tankAttr = new TankAttr();
		if (player.getPlayerFunction().isOpenFunction(PlayerFunctionType.AircraftCarrier)){
			PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
			CvLevelTemplateImpl cvLevelTemplate = aircraftCarrierConfig.getCvLevelTemplate(playerAircraftCarrier.getLv());
			if (cvLevelTemplate != null){
				tankAttr.addAttr(cvLevelTemplate.getTankAttrMap());
			}
			//计算航母-舰岛属性加成
			TankAttr islandAttr = aircraftCarrierConfig.getIslandAttr(playerAircraftCarrier.getIsland());
			if(null!=islandAttr) {
				tankAttr.addAttr(islandAttr);
			}
		}
		return tankAttr;
	}

	/**
	 * 计算动力舱属性
	 * @param player
	 * @return
	 */
	public Map<Integer, TankAttr> calAircraftCarrierEngineAttr(Player player){
		Map<Integer, TankAttr> tankAttrMap = Maps.newHashMap();
		if (player.getPlayerFunction().isOpenFunction(PlayerFunctionType.AircraftCarrier)){
			PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
			CvEngineLevelTemplateImpl cvEngineLevelTemplate = aircraftCarrierConfig.getCvEngineLevelTemplate(playerAircraftCarrier.getEngineLv());
			if (cvEngineLevelTemplate != null){
				tankAttrMap = cvEngineLevelTemplate.getTotalTankTypeAttrMap();
			}
		}
		return tankAttrMap;
	}




	public List<Items> luckDraw(Player player,int id,int type, int count) {
		int luck = player.playerAircraft().getLuck(id);
		int recruitCount = player.playerAircraft().getRecruitCount(id);
		double rate = player.playerAircraft().getRate(id);
		List<Items> rewards = Lists.newArrayList();
		CvDrawConfigTemplateImpl drwaTemplate = aircraftCarrierConfig.getDrawTemplate(id);
		double aircraftDrawLuckAdd = commValueConfig.getDoubleCommValue(CommonValueType.AircraftDrawLuckAdd);
		double aircraftDrawLuckReduce = commValueConfig.getDoubleCommValue(CommonValueType.AircraftDrawLuckReduce);
		int maxLuckSmall = 10;//10次必出紫色或橙色战机
		int maxLuckBig = drwaTemplate.getLucky_value();//幸运值
		
		for(int i=0;i<count;i++) {
			recruitCount++;
			luck++;
			//幸运值满或者概率计算中满足出现史诗飞机
			if(luck>=maxLuckBig||(rate>0&&Math.random()<=rate)) {
				//随机出一个史诗飞机
				rewards.add(randomSSAir(player,id));
				//幸运值清空
				luck =0;
				//概率清空
				rate -= aircraftDrawLuckReduce;
				continue;
			}
			if(recruitCount>=maxLuckSmall) {
				//随机出一个传奇飞机
				rewards.add(randomSmallLuckAir(player,id));
				recruitCount = 0;
				continue;
			}
			//随机一个普通奖励
			Items reward = randomNormalReward(player, id, type);
			if(aircraftCarrierConfig.isSSAir(reward.getItemType(),reward.getId())) {
				//幸运值清空
				luck =0;
				//概率清空
				rate -= aircraftDrawLuckReduce;
			}
			rewards.add(reward);
			rate += aircraftDrawLuckAdd;
		}
		//额外奖励道具
		rewards.add(aircraftCarrierConfig.randomLibReward(type==0?drwaTemplate.getExtra_reward_once():drwaTemplate.getExtra_reward_ten(), player.playerLevel().getLv()));
		player.playerAircraft().setLuck(id,luck);
		player.playerAircraft().setRecruitCount(id,recruitCount);
		player.playerAircraft().setRate(id,rate);
		return ItemUtils.checkItemList(rewards);
	}

	//随机一个SS飞机
	private Items randomSSAir(Player player,int id) {
		CvDrawConfigTemplateImpl template = aircraftCarrierConfig.getDrawTemplate(id);
		int libId = template.getLibrary_sscar();
		return aircraftCarrierConfig.randomLibReward(libId, player.playerLevel().getLv());
	}
	
	//随机一个紫或橙飞机
	private Items randomSmallLuckAir(Player player,int id) {
		CvDrawConfigTemplateImpl template = aircraftCarrierConfig.getDrawTemplate(id);
		int libId = template.randomSmallLuckLib();
		return aircraftCarrierConfig.randomLibReward(libId, player.playerLevel().getLv());
	}
	//随机一个普通奖励
	private Items randomNormalReward(Player player,int id,int type) {
		CvDrawConfigTemplateImpl template = aircraftCarrierConfig.getDrawTemplate(id);
		int libId = type==0?template.randomOnceLib():template.randomTenLib();
		return aircraftCarrierConfig.randomLibReward(libId, player.playerLevel().getLv());
	}
	//获取分解奖励
	public List<Items> getDecomposeReward(Player player, List<String> uids) {
		List<Items> rewards = Lists.newArrayList();
		uids.forEach(uid ->{
			Aircraft aircraft = player.playerAircraft().getAircraft(uid);
			if(aircraft!=null&&!player.playerAircraftCarrier().isAircraftUp(uid)){
				ItemBattleplaneTemplateImpl template = aircraftCarrierConfig.getAirTemplate(aircraft.getId());
				if(template!=null) {
					rewards.addAll(template.getDecompses(aircraft.getStar()));
				}
			}
		});
		return ItemUtils.mergeItemList(rewards);
	}
	//分解
	public List<Integer> decompose(Player player, List<String> uids) {
		List<Integer> ids = Lists.newArrayList();
		uids.forEach(uid->{
			Aircraft aircraft = player.playerAircraft().getAircraft(uid);
			if(aircraft!=null&&!player.playerAircraftCarrier().isAircraftUp(uid)){
				player.playerAircraft().decomposeAircraft(uid);
				ids.add(aircraft.getId());
			}
		});
		return ids;
	}

	//校验编队合法性
	public boolean checkAirFormation(Player player, int id, List<String> uids) {
		List<Integer> types = Lists.newArrayList();
		for(String uid:uids) {
			Aircraft aircraft = player.playerAircraft().getAircraft(uid);
			if(aircraft==null||!player.playerAircraftCarrier().isAircraftUp(uid)){
				return false;
			}
			ItemBattleplaneTemplateImpl template = aircraftCarrierConfig.getAirTemplate(aircraft.getId());
			if(template==null||types.contains(template.getType())) {
				return false;
			}
			types.add(template.getType());
		}
		return true;
	}
	
	
	//获取飞行编队唯一id
	public static String getAirFormationId(long playerId,int id) {
		return playerId+"_"+id;
	}
	
	public void sendPlayerAirFormation(Player player) {
		PlayerAirFormation formation = PlayerAirFormation.getOrCreate(player.getId());
		player.sendMsg(MessageComm.S2C_Aircraft_GetAirFormation, formation);
	}

	public TankAttr getAirAttr(Player player) {
		TankAttr tankAttr = new TankAttr();
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.AircraftCarrier)) {
			return null;
		}
		player.playerAircraftCarrier().getAircraftList().forEach(t ->{
			Aircraft aircraft = player.playerAircraft().getAircraft(t);
			if(aircraft!=null){
				BattleplaneStarTemplateImpl template = aircraftCarrierConfig.getStarTemplate(aircraft.getId(), aircraft.getStar());
				tankAttr.addAttr(template.getAttrMap());
			}
		});;
		return tankAttr;
	}

	//检查飞机位置
	public boolean checkAircraftPosition(Player player, CvLevelTemplateImpl cvLevelTemplate, String troop) {
		String[] aircraft = troop.split(",");
		for (int i = 0; i < aircraft.length; i++) {
			int index = Integer.parseInt(aircraft[i].split(":")[0]);
			if (index > cvLevelTemplate.getUnlock_num()){
				return false;
			}
		}
		return true;
	}

	// 上阵
	public void upAircraft2Carrier(Player player, String troop) {
		String[] aircraft = troop.split(",");
		for (int i = 0; i < aircraft.length; i++) {
			String [] s = aircraft[i].split(":");
			int index = Integer.parseInt(s[0]);
			String uid = s.length > 1 ? s[1] : "";
            if (StrUtil.isNotBlank(uid)) {
                player.notifyObservers(ObservableEnum.AircraftCarrierEitEvent, index, uid, player.playerAircraftCarrier().getAircraftList().get(index - 1));
            }
			player.playerAircraftCarrier().upAircraft(index, uid);
		}
	}
	// 飞机唯一类型检查
	public boolean checkAirplaneTypeUnique(Player player, String troop) {
		List<String> aircraftList = getNewAirUids(player,troop);
		List<Integer> airTypes = aircraftList.stream()
				.map(uid -> player.playerAircraft().getAircraft(uid))
				.filter(Objects::nonNull)
				.map(e -> aircraftCarrierConfig.getAirTemplate(e.getId()).getType())
				.collect(Collectors.toList());
		List<Integer> distAirTypes = airTypes.stream().distinct().collect(Collectors.toList());
		return airTypes.size() == distAirTypes.size();
	}

	private List<String> getNewAirUids(Player player, String troop){
		PlayerAircraftCarrier playerAircraftCarrier = player.playerAircraftCarrier();
		String[] cloneAirs = ObjectUtil.clone( playerAircraftCarrier.getAircrafts());
		String[] aircraft = troop.split(",");
		for (int i = 0; i < aircraft.length; i++) {
			String [] s = aircraft[i].split(":");
			int index = Integer.parseInt(s[0]);
			String uid = s.length > 1 ? s[1] : "";
			cloneAirs[index-1] = uid;
		}
		return Arrays.stream(cloneAirs).collect(Collectors.toList());
	}

	/**
	 * 判断需要开启航母-舰岛
	 * @param player
	 */
	public void checkIsland(Player player) {
		PlayerAircraftCarrier airCraft = player.playerAircraftCarrier();
		int lv = airCraft.getLv();
		int engineLv = airCraft.getEngineLv();
		Map<Integer, Integer> openIsland = aircraftCarrierConfig.getOpenIsland(lv, engineLv, airCraft.getIsland());
		airCraft.addIsland(openIsland);
	}

}
