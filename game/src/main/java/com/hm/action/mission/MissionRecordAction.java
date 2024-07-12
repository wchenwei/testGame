package com.hm.action.mission;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.mission.biz.MissionRecordBiz;
import com.hm.action.mission.vo.MissionRecordTopVo;
import com.hm.config.MissionConfig;
import com.hm.enums.BattlePveRecordType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.servercontainer.mission.MissionRecord;
import com.hm.servercontainer.mission.MissionTopRecord;
import com.hm.servercontainer.record.PveMRecord;
import com.hm.servercontainer.world.WorldItemContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.StringUtil;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Action
public class MissionRecordAction extends AbstractPlayerAction{

	@Resource
	private MissionConfig missionConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MissionRecordBiz missionRecordBiz;


	@MsgMethod(MessageComm.C2S_PlayerFb_Record)
	public void playerFbRecord(Player player,JsonMsg msg){
		String id = msg.getString("id");
		
		MissionRecord record = WorldServerContainer.of(player).getMissionRecord(id);
		MissionTopRecord firstRecord = record.getFirstRecord();
		MissionTopRecord minCombatRecord = record.getMinCombatRecord();
		
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_PlayerFb_Record);
		serverMsg.addProperty("id", id);
		if(firstRecord != null) {
			try {
				serverMsg.addProperty("firstRecord", new MissionRecordTopVo(firstRecord));
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		if(minCombatRecord != null) {
			try {
				serverMsg.addProperty("minCombatRecord", new MissionRecordTopVo(minCombatRecord));
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		player.sendMsg(serverMsg);
	}

	@MsgMethod(MessageComm.C2S_Battle_Record)
	public void battleRecord(Player player,JsonMsg msg){
		List<String> id = StringUtil.splitStr2StrList(msg.getString("ids"),",");

		WorldItemContainer container = WorldServerContainer.of(player);
		List<PveMRecord> recordList = id.stream().map(e -> container.getPveMRecord(e))
				.filter(Objects::nonNull).collect(Collectors.toList());

		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Battle_Record);
		serverMsg.addProperty("recordList", recordList);
		player.sendMsg(serverMsg);
	}

	@MsgMethod(MessageComm.C2S_Battle_RecordReward)
	public void battleRecordReward(Player player,JsonMsg msg){
		String id = msg.getString("id");

		PveMRecord record = WorldServerContainer.of(player).getPveMRecord(id);
		if(record == null) {
			player.sendErrorMsg(SysConstant.Record_Not_Exist);
			return;
		}
		if(player.playerRecord().haveId(id)) {
			player.sendErrorMsg(SysConstant.Record_Reward_Received);
			return;
		}
		BattlePveRecordType recordType = BattlePveRecordType.get(record.getType());
		if(recordType == null) {
			player.sendErrorMsg(SysConstant.Record_Not_Exist);
			return;
		}
		List<Items> itemsList = recordType.getRecordItems(record.getMid());
		if(CollUtil.isEmpty(itemsList)) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		//记录
		player.playerRecord().addId(id);
		itemBiz.addItem(player,itemsList, LogType.BattleReward.value(id));
		player.sendUserUpdateMsg();

		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Battle_RecordReward);
		serverMsg.addProperty("id", id);
		serverMsg.addProperty("itemsList", itemsList);
		player.sendMsg(serverMsg);
	}
}
