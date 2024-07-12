package com.hm.action.kfseason;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.kfseason.kftask.PlayerKFTaskBiz;
import com.hm.action.kfseason.vo.SeasonServerRankVo;
import com.hm.action.kfseason.vo.SeasonTopVo;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.KfConfig;
import com.hm.config.excel.templaextra.KfSeasonShopTemplateImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfSeasonShop.KfSeasonShopActivity;
import com.hm.model.activity.kfSeasonShop.KfSeasonShopValue;
import com.hm.model.activity.kfseason.server.KFSeasonUtil;
import com.hm.model.activity.kfseason.server.KfSeasonServerUtils;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Action
public class KfSeasonAction extends AbstractPlayerAction{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private ItemBiz itemBiz;

	@Resource
    private KfConfig kfConfig;
	@Resource
	private PlayerKFTaskBiz playerKFTaskBiz;
	
	@MsgMethod(MessageComm.C2S_KFSeasonRank)
    public void getSeasonRank(Player player, JsonMsg msg) {
		int groupId = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerKefuData().getSeasonGroupId();
		List<SeasonServerRankVo> serverRankList = KfSeasonServerUtils.getSortServerList(groupId)
				.stream().map(e -> new SeasonServerRankVo(e)).collect(Collectors.toList());
		
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KFSeasonRank);
		serverMsg.addProperty("serverRankList", serverRankList);
		LeaderboardInfo myRankData = HdLeaderboardsService.getInstance().getLeaderboardInfo(player, RankType.SeasionPlayerScore);
		if(myRankData != null) { 
			serverMsg.addProperty("playerScore", (int)myRankData.getScore());
		}else{
			serverMsg.addProperty("playerScore", 0);
		}
		player.sendMsg(serverMsg);
	}
	
	
	@MsgMethod(MessageComm.C2S_KFSeasonTop)
    public void getSeasonTop(Player player, JsonMsg msg) {
		int seasonId = msg.getInt("seasonId");//赛季id
		seasonId = Math.min(seasonId, KFSeasonUtil.getCurSeason().getId()-1);
		if(seasonId <= 0) {
			return;
		}
		int groupId = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerKefuData().getSeasonGroupId();
		SeasonTopVo topSeason = new SeasonTopVo(groupId, seasonId);
		
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KFSeasonTop);
		serverMsg.addProperty("topSeason", topSeason);
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_KFSeason_Buy)
    public void buy(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		KfSeasonShopActivity activity = (KfSeasonShopActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFSeasonShop);
		if(activity==null||activity.isClose()) {
			return;
		}
		KfSeasonShopTemplateImpl template = kfConfig.getShopTemplate(id);
		if(template==null) {
			return;
		}
		KfSeasonShopValue value = (KfSeasonShopValue)player.getPlayerActivity().getPlayerActivityValue(ActivityType.KFSeasonShop);
		if(!value.isCanBuy(player, id)) {
			return;
		}
		if(!itemBiz.checkItemEnoughAndSpend(player, template.getPrices(), LogType.ActivityShop.value(ActivityType.KFSeasonShop.getType()))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		};
		value.buy(player, id);
		player.getPlayerActivity().SetChanged();
		List<Items> rewards = template.getRewards();
		itemBiz.addItem(player, rewards, LogType.ActivityShop.value(ActivityType.KFSeasonShop.getType()));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_KFSeason_Buy,rewards);
		
	}
	
	
	
	
	
	
}
