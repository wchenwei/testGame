package com.hm.action.kf;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.SensitiveWordUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.build.biz.RobMathUtils;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.kfbuild.CrystalHunterLog;
import com.hm.action.kf.kfbuild.HunterRedisUtils;
import com.hm.action.kf.kfexpedition.KfExpeditionBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.DropConfig;
import com.hm.enums.*;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfactivity.KFServerInfoUtil;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.kf.AbstractKfData;
import com.hm.model.serverpublic.kf.KFSportsData;
import com.hm.redis.RedisLockTool;
import com.hm.redis.ServerNameCache;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.PubFunc;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Slf4j
@Action
public class KfPlayerAction extends AbstractPlayerAction{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
    private DropConfig dropConfig;
	@Resource
    private ItemBiz itemBiz;
	@Resource
    private KfExpeditionBiz kfExpeditionBiz;

	
	@MsgMethod(MessageComm.C2S_KfSportsWorship)
    public void kfSportsWorship(Player player, JsonMsg msg) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		AbstractKfData kfData = serverData.getServerKfData().getKfData(KfType.Sports);
		if(kfData == null) {
			return;
		}
		KFSportsData sportsData = (KFSportsData)kfData;
		if(!sportsData.haveWinPlayer()) {
			return;
		}
		if(player.getPlayerStatistics().getTodayStatistics(StatisticsType.KfSportsWorship) > 0) {
			return;
		}
		List<Items> itemList = dropConfig.randomItem(commValueConfig.getCommValue(CommonValueType.KfSportsWorshipDrop));
		if(itemList.isEmpty()) {
			return;
		}
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.KfSportsWorship);
		itemBiz.addItem(player, itemList, LogType.KfSportsWorship);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KfSportsWorship);
		serverMsg.addProperty("itemList", itemList);
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_KfExpeditionServerList)
    public void expeditionServerList(Player player, JsonMsg msg) {
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KfExpeditionServerList);
		serverMsg.addProperty("serverList", kfExpeditionBiz.buildExpeditionServerVo(player.getServerId()));
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_KfExpeditionDeclare)
    public void expeditionDeclare(Player player, JsonMsg msg) {
		int targetId = msg.getInt("targetId");
		if(player.getServerId() == targetId) {
			return;
		}
		if(!kfExpeditionBiz.isCanDeclare(player)) {
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_ConditionsNot);
			return;//不满足宣战条件
		}
		KfExpeditionActivity expeditionActivity = (KfExpeditionActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KfExpeditionActivity);
		boolean activityIsOpen = expeditionActivity != null && expeditionActivity.isOpen();
		if(!activityIsOpen) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;//活动未开放
		}
		String lockKey = "ExpeditionDeclare";
		String requestId = RedisLockTool.getRequestId();
		String declaration = msg.getString("declaration");
		if(StrUtil.isEmpty(declaration)) {
			declaration = "堵上我服的荣誉，此战必胜";
		}else{
			declaration = SensitiveWordUtil.replaceSensitiveWord(declaration, "*");
		}
        log.error(player.getServerId() + "跨服远征宣战");

		if(!RedisLockTool.tryGetDistributedLock(lockKey, requestId, 10000)) {
			player.sendErrorMsg(SysConstant.KfExpeditionDeclare_SystemBusy);
			return;
		}
		try {
			if(targetId > 0) {
				kfExpeditionBiz.declareFixedServer(player, targetId, declaration);
			}else{
				kfExpeditionBiz.declareRandomServer(player, declaration);
			}
		} finally {
			RedisLockTool.releaseDistributedLock(lockKey, requestId);
		}
		
	}
	
	
	@MsgMethod(MessageComm.C2S_ServerName)
    public void getServerName(Player player, JsonMsg msg) {
		try {
			List<Integer> ids = StringUtil.splitStr2IntegerList(msg.getString("ids"), ",");
			Map<Integer,String> nameMap = ServerNameCache.getServerName(ids);
			
			JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_ServerName);
			serverMsg.addProperty("nameMap", nameMap);
			player.sendMsg(serverMsg);
		} catch (Exception e) {
		}
	}
	
	

	@MsgMethod(MessageComm.C2S_KfBuildHunterLog)
	public void getBuildHunterLog(Player player, JsonMsg msg) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_KfBuildHunterLog);
		List<CrystalHunterLog> logList = HunterRedisUtils.getAllCrystalHunterLog(serverData.getGameServerType().getId());
		serverMsg.addProperty("logList", logList);
		int cryHunterNum = RobMathUtils.getTodayMaxHunterNum() - RobMathUtils.getTodayNum(logList);
		serverMsg.addProperty("cryHunterNum", Math.max(0, cryHunterNum));
		player.sendMsg(serverMsg);
	}


	@MsgMethod(MessageComm.C2S_KfUrl)
	public void getKFUrl(Player player, JsonMsg msg) {
		String url = msg.getString("url");
		player.sendMsg(MessageComm.S2C_KfUrl, KFServerInfoUtil.getOutUrl(url));
	}
}
