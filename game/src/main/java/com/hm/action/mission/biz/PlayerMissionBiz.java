package com.hm.action.mission.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityEffectBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.mission.vo.MissionResultVo;
import com.hm.action.player.PlayerBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.MissionConfig;
import com.hm.config.excel.templaextra.CityGuideTemplate;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.config.excel.templaextra.MissionExtraTemplate;
import com.hm.enums.BattleType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerRewardMode;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.annotation.Biz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerStatistics;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;
@Biz
public class PlayerMissionBiz implements IObserver{
	@Resource
	private MissionConfig missionConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private TankBiz tankBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	
	public MissionResultVo fight(Player player, int missionId,List<Integer> tankIdList){
		//校验结果
		MissionExtraTemplate template = missionConfig.getMission(missionId);

		List<Items> missionRewards = null;
		if(!player.playerMission().isOnHookFight() && missionId >= player.playerMission().getMissionId()) {//通关
			long clearanceCombat = tankBiz.getTankCombat(player, tankIdList);
			player.playerMission().clearance(missionId,clearanceCombat);
			//战斗胜利的广播
			player.notifyObservers(ObservableEnum.FBFightWin, missionId, tankIdList);
			//发放奖励
			missionRewards = template.getMissRewardList();
			itemBiz.addItem(player,missionRewards, LogType.Mission.value(missionId));
		}

		MissionResultVo vo = new MissionResultVo(player.playerTank().createTankVos(tankIdList),player.playerLevel().getLv(),player.playerLevel().getLvExp(),0,0);
		vo.setRewards(missionRewards);
		return vo;
	}
	//扫荡
	public List<Items> sweep(Player player, int missionId,int sweepCount){
		//发放奖励
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		if(template==null){
			return Lists.newArrayList();
		}
		List<Items> rewards = template.getDropItemList(sweepCount);
		itemBiz.addItem(player, rewards, LogType.SweepFb.value(missionId));
		player.sendUserUpdateMsg();
		return rewards;
	}

	
	//城市章节奖励
	private void cityGuideReward(ObservableEnum observableEnum, Player player,
			Object[] argv) {
		int cityId = Integer.parseInt(argv[0].toString());
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		if(cityTemplate==null){
			return;
		}
		int chapterId = cityTemplate.getChapter();
		CityGuideTemplate cityGuideTemplate = cityConfig.getCityGuide(chapterId);
		if(cityGuideTemplate==null){
			return;
		}
		List<Items> rewards = Lists.newArrayList();
		if(cityGuideTemplate.getRewardCityId() == cityId){//解锁城市等于需要发放章节奖励的城市
			rewards = cityGuideTemplate.getRewards();
		}
		player.sendMsg(MessageComm.S2C_CityGuide_Reward, rewards);
	}
	//城市解放奖励
	private void cityOpenReward(ObservableEnum observableEnum, Player player,
			Object[] argv){
		int cityId = Integer.parseInt(argv[0].toString());
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		if(cityTemplate==null){
			return;
		}
		List<Items> rewards = cityTemplate.getReleaseRewards();
		itemBiz.addItem(player, rewards, LogType.CityOpen.value(cityId));
	}
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.RecaptureCity, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ClearnceMission, this);

	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		if(observableEnum == ObservableEnum.RecaptureCity){
			//城市解放奖励
			cityOpenReward(observableEnum, player, argv);
			//城市章节奖励
			cityGuideReward(observableEnum,player,argv);
		}else if(observableEnum == ObservableEnum.ClearnceMission) {
			updateMaxMissionId(player, (int)argv[0], (int)argv[1], (int)argv[3]);
		}
	}
	//领取城市章节奖励
	public List<Items> receiveCityGuide(Player player,int id) {
		CityGuideTemplate template = cityConfig.getCityGuide(id);
		List<Items> rewards = template.getRewards();
		itemBiz.addItem(player, template.getRewards(), LogType.CityGuide.value(id));
		player.playerMission().receiveCityGuide(id);
		return rewards;
	}
	//校验战力
	public boolean checkCombat(Player player,List<Integer> tankIdList,int missionId) {
		long maxCombat = tankBiz.getTankMaxCombat(player, tankIdList);
		MissionExtraTemplate template = missionConfig.getMission(missionId);
		if(template==null){
			return false;
		}
		return maxCombat>=template.getBottom_power();
	}
	
	/**
	 * 获取玩家最大解锁城市
	 * @param serverId
	 * @return
	 */
	public int getPlayerMaxCity(int serverId) {
		ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(serverId).getServerStatistics();
		int maxMissionId = serverStatistics.getMaxMissionId();
		MissionExtraTemplate template = missionConfig.getMainMission(maxMissionId);
        if (template == null) {
            return 0;
        }
		return Math.min(template.getCity()-1,GameConstants.MaxWorldCityId);
	}
	public int getPlayerMaxMissionId(int serverId) {
		List<LeaderboardInfo> tempList = HdLeaderboardsService.getInstance().getGameRank(RankType.PlayerMainBattle, serverId, Lists.newArrayList(1));
		if(CollUtil.isNotEmpty(tempList)) {
			return (int)tempList.get(0).getScore();
		}
		return 0;
	}
	
	private void updateMaxMissionId(Player player,int battleType,int missionId,int result) {
		if(result == 0) {
			return;
		}
		if(battleType == BattleType.MainBattle.getType()) {
			ServerStatistics serverStatistics = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerStatistics();
			if(serverStatistics.getMaxMissionId() < missionId) {
				serverStatistics.setMaxMissionId(missionId);
				serverStatistics.save();
			}
		}
	}
}
