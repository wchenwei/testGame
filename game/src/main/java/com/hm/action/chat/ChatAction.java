package com.hm.action.chat;

 import com.google.common.collect.Lists;
 import com.hm.action.AbstractPlayerAction;
 import com.hm.action.item.ItemBiz;
 import com.hm.action.personalchat.biz.FriendBiz;
 import com.hm.chat.ChatRpcUtils;
 import com.hm.db.PlayerUtils;
 import com.hm.enums.ItemType;
 import com.hm.enums.LogType;
 import com.hm.enums.PlayerAssetEnum;
 import com.hm.message.ChatMessageComm;
 import com.hm.libcore.annotation.MsgMethod;
 import com.hm.libcore.msg.JsonMsg;
 import com.hm.libcore.springredis.util.RedisMapperUtil;
 import com.hm.message.MessageComm;
 import com.hm.model.chat.RedMsg;
 import com.hm.model.guild.Guild;
 import com.hm.model.item.Items;
 import com.hm.model.player.Player;
 import com.hm.redis.PlayerRedisData;
 import com.hm.redis.util.RedisUtil;
 import com.hm.servercontainer.guild.GuildContainer;
 import com.hm.sysConstant.SysConstant;
 import com.hm.libcore.annotation.Action;

 import javax.annotation.Resource;
 import java.util.LinkedHashSet;
 import java.util.List;
 import java.util.concurrent.locks.ReentrantLock;
 import java.util.stream.Collectors;


@Action
public class ChatAction extends AbstractPlayerAction {
	@Resource
	private ItemBiz itemBiz;
    @Resource
    private FriendBiz friendBiz;

	private ReentrantLock lock = new ReentrantLock();

	/**
	 * 添加删除黑名单
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod(MessageComm.C2S_SetBlack)
	public void setBlack(Player player,JsonMsg msg){
		long playerId = msg.getLong("playerId");
		int type = msg.getInt("type"); //1添加2删除
		if(playerId == player.getId()){
			player.sendErrorMsg(SysConstant.BLACK_ADD_FAIL);
			return;
		}
		Player blackPlayer = PlayerUtils.getPlayer(playerId);
		if(blackPlayer == null){
			player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
			return; 
		}
		if(type == 1){
			player.playerChat().addBlack(playerId);
            //如果是好友则删除
            if (player.playerFriend().getFriendVal(playerId) != null) {
                friendBiz.delFriend(player, playerId);
                Player friendPlayer = PlayerUtils.getPlayer(playerId);
                friendBiz.delFriend(friendPlayer, player.getId());
            }
		}else if(type ==2){
			player.playerChat().delBlack(playerId);
		}
		player.sendUserUpdateMsg();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_SetBlack);
		serverMsg.addProperty("playerId",playerId); 
		serverMsg.addProperty("name", blackPlayer.getName());
		serverMsg.addProperty("type", type);
		player.sendMsg(serverMsg);
	}
	
	/**
	 * 打开黑名单
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod(MessageComm.C2S_OpenBlack)
	public void openBlack(Player player,JsonMsg msg){
		List<PlayerBlackVO> playerBlackVOList = Lists.newArrayList(); 
		LinkedHashSet<Long> blackSet = player.playerChat().getBlackSet();
		if(blackSet.size() >0){
			List<PlayerRedisData> playerList = RedisUtil.getListPlayer(blackSet.stream().collect(Collectors.toList()));
			playerList.forEach(p->{
				PlayerBlackVO blackVO = new PlayerBlackVO(p.getId());
				blackVO.setPlayerInfo(p);
				if(p.getGuildId()>0){
					Guild guild = GuildContainer.of(player).getGuild(p.getGuildId());
					if (guild != null){
						blackVO.setGuildInfo(guild);
					}
				}
				playerBlackVOList.add(blackVO);
			});
		}
		player.sendMsg(MessageComm.S2C_OpenBlack,playerBlackVOList);
	}
	
	@MsgMethod(MessageComm.C2S_OpenRedPacket)
	public void openRedPacket(Player player,JsonMsg msg){
		String redId = msg.getString("redId");//红包id
		RedMsg redMsg = RedisMapperUtil.queryOne(player.getServerId(), redId, RedMsg.class);
		if(redMsg == null){
			player.sendErrorMsg(SysConstant.RedMsg_Not_Exist);
			return;
		}
		try{
			lock.lock(); 
			int count = redMsg.openRedPacket(player);
			if(count >0){
				Items item = new Items(PlayerAssetEnum.SysGold.getTypeId(), count, ItemType.CURRENCY.getType());
				itemBiz.addItem(player, item, LogType.RedPacket);
				player.sendUserUpdateMsg();
			}
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_OpenRedPacket);
			serverMsg.addProperty("redPacket", redMsg);
			player.sendMsg(serverMsg);
		}finally{
			lock.unlock();
		}
	}
}
  
