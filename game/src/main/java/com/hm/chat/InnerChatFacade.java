package com.hm.chat;

import cn.hutool.core.util.RandomUtil;
import com.hm.message.ChatMessageComm;
import com.hm.libcore.annotation.Facder;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.enums.CommonValueType;
import com.hm.model.chat.RedMsg;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.observer.IObservable;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

@Facder("InnerChatFacade")
public class InnerChatFacade implements IObserver,IObservable{
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
	private CommValueConfig commValueConfig;
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.NotChat, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerQuit, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildDel, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.JoinCamp, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		if(player == null){
			return; 
		}
		switch(observableEnum) {
			case GuildPlayerAdd :{
				Guild guild = (Guild) argv[0];
				joinGuild(player.getServerId(), player.getId(), guild.getId());
				break;
			}
			case GuildPlayerQuit : {
				Guild guild = (Guild) argv[0];
				quitGuild(player.getServerId(), player.getId(), guild.getId());
				break;
			}
			case GuildDel : {
				Guild guild = (Guild) argv[0];
				delGuild(guild.getServerId(), guild.getId());
				break;
			}
			case NotChat : {
				gagPlayer(player.getServerId(), player.getId());
				break;
			}
		}
	}

	public void sendSysMsg(Player player,String content, ChatRoomType roomType) {
		sendSysMsg(player,content,roomType,ChatMsgType.Normal,null);
	}

	/**
	 * 发送系统消息
	 *
	 * @author yanpeng 
	 * @param content  
	 *
	 */
	public void sendSysMsg(long playerId, int serverId, int guildId, String content, ChatRoomType roomType, ChatMsgType msgType, String extend){
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_SendChatMsg);
		msg.addProperty("playerId", playerId);
		msg.addProperty("serverId", serverId);
		msg.addProperty("guildId", guildId);
		msg.addProperty("content", content);
		msg.addProperty("msgType", msgType.getType());
		msg.addProperty("roomType", roomType.getType());
		msg.addProperty("extend", extend);
		ChatRpcUtils.sendMsg(msg);
	}
	public void sendSysMsg(Player player,String content, ChatRoomType roomType,ChatMsgType msgType,String extend) {
		sendSysMsg(player.getId(),player.getServerId(),player.getGuildId(),content,roomType,msgType,extend);
	}

	private void sendSysRedPackageMsg(int serverId,int guildId,String title,String content, String extend){
		sendSysMsg(0,serverId,guildId,content,ChatRoomType.Guild,ChatMsgType.RedPacket,extend);
	}
	
	/**
	 * 玩家加入部落
	 *
	 * @author yanpeng 
	 * @param serverId
	 * @param playerId  
	 *
	 */
	public void joinGuild(int serverId,long playerId,int guildId){
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_JoinGuild);
		msg.addProperty("playerId", playerId);
		msg.addProperty("serverId", serverId);
		msg.addProperty("guildId", guildId);
		ChatRpcUtils.sendMsg(msg);
	}
	
	/**
	 * 玩家退出部落
	 *
	 * @author yanpeng 
	 * @param serverId
	 * @param playerId
	 * @param guildId  
	 *
	 */
	public void quitGuild(int serverId,long playerId,int guildId){
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_QuitGuild);
		msg.addProperty("playerId", playerId);
		msg.addProperty("serverId", serverId);
		msg.addProperty("guildId", guildId);
		ChatRpcUtils.sendMsg(msg);
	}

	/**
	 * 解散部落
	 *
	 * @author yanpeng 
	 * @param serverId
	 * @param guildId  
	 *
	 */
	public void delGuild(int serverId,int guildId){
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_DelGuild);
		msg.addProperty("guildId", guildId);
		msg.addProperty("serverId", serverId);
		ChatRpcUtils.sendMsg(msg);
	}
	
	/**
	 * 禁言玩家
	 */
	public void gagPlayer(int serverId,long playerId){
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_GagPlayer);
		msg.addProperty("playerId", playerId);
		msg.addProperty("serverId", serverId);
		ChatRpcUtils.sendMsg(msg);
	}
	
	/**
	 * 保存并发送红包
	 *
	 * @author yanpeng 
	 * @param player  
	 *
	 */
	public void saveAndSendRedPacket(Player player){
		String id = PrimaryKeyWeb.getPrimaryKey(RedMsg.class.getSimpleName(),player.getServerId());
		RedMsg redMsg = new RedMsg(id,player); 
		redMsg.initRedPacket(randomRedPacket()); 
		redMsg.saveDB(player.getServerId()); 
		String content = String.format(langeConfig.getValue("red_packet_txt"),
				player.getName());
		sendSysMsg(player.getId(),player.getServerId(),player.getGuildId(),content,ChatRoomType.Guild,ChatMsgType.RedPacket,id);

	}
	
	public void saveAndSendSystemRedPacket(int serverId,int campId,int total,int count,String title,String content){
		String id = PrimaryKeyWeb.getPrimaryKey(RedMsg.class.getSimpleName(),serverId);
		RedMsg redMsg = new RedMsg(id, 0, 1, "系统");
		redMsg.initRedPacket(randomRedPacket(total,count)); 
		redMsg.saveDB(serverId); 
		sendSysRedPackageMsg(serverId, campId, title,content, id);
	}
	
	/**
	 * 随机分配红包
	 *
	 * @author yanpeng 
	 * @return  
	 *
	 */
	public int[] randomRedPacket(){
		int total = commValueConfig.getCommValue(CommonValueType.RedPacketTotal);
		int count = commValueConfig.getCommValue(CommonValueType.RedPacketCount);
		return randomRedPacket(total, count);
	}
	
	public int[] randomRedPacket(int total,int count) {
		double minRate = commValueConfig.getCommValue(CommonValueType.RedPacketMin)/100d; 
		//double maxRate = commValueConfig.getCommValue(CommonValueType.RedPacketMax)/100d; 
		int[] diamons = new int[count];
		int left = total; //剩余金额
		for(int i=0;i<count-1;i++){
			int min = Math.max(1,(int)Math.floor(left * minRate)); 
			double maxRate = 2d/(count-i+1);
			int max = Math.max(1,(int)Math.floor(left * maxRate));
			int random = 1;
			if(min >= max){
				random = min;
			}else{
				random = RandomUtil.randomInt(min, max);
			}
			
			diamons[i] = random; 
			left -= random; 
		}
		diamons[count-1] = left; 
		return diamons;
	}
	
	/**
	 * 清空聊天记录
	 * @param serverId
	 * @param chatMsgType
	 */
	public void clearChat(int serverId,ChatMsgType chatMsgType) {
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_ClearChat);
		msg.addProperty("serverId", serverId);
		msg.addProperty("chatMsgType", chatMsgType.getType());
		ChatRpcUtils.sendMsg(msg);
	}

	/**
	 * 发送前线战争组队邀请
	 * @param serverId
	 * @param chatMsgType
	 */
	public void sendFrontBattleTroop(Player player,ChatRoomType roomType,String troopId,int missionId) {
		sendSysMsg(player,"",roomType,ChatMsgType.Build_Troop,troopId+"_"+missionId);
	}
	
	@Override
	public void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv) {
		ObserverRouter.getInstance().notifyObservers( observableEnum, player, argv );
	}
	
	
	
		
	
}
