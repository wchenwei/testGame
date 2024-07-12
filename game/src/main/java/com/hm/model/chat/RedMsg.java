package com.hm.model.chat;

import com.google.common.collect.Lists;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.Player;
import lombok.Data;

import java.util.List;


/**
 * 红包
 * ClassName: RedMsg. <br/>    
 * date: 2018年7月18日 下午3:14:33 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
@Data
@RedisMapperType(type = MapperType.STRING_HASH)
public class RedMsg extends BaseEntityMapper<String> {
	private long playerId; //玩家id
	private String icon; //玩家icon
	private String name; //玩家名字
	private long sendTime; //发送时间
	private int total; //红包金额
	private int remain; //未领取的数量
	private List<PlayerRedPacket> redPacketList = Lists.newArrayList(); //红包详情
	
	public RedMsg(){
		
	}
	
	public RedMsg(String id, long playerId, int icon, String name) {
		setId(id);
		this.playerId = playerId;
		this.icon = icon+"";
		this.name = name;
	}

	public RedMsg(String id,Player player){
		setId(id);
		this.playerId = player.getId(); 
		this.name = player.getName(); 
		this.icon = player.playerHead().getIcon();
		this.sendTime = System.currentTimeMillis(); 
	}
	
	public void initRedPacket(int[] diamons){
		this.remain = diamons.length; 
		for(int i=0;i<diamons.length;i++){
			PlayerRedPacket playerRedPacket = new PlayerRedPacket(); 
			int num = diamons[i];
			playerRedPacket.setNum(num);
			this.redPacketList.add(playerRedPacket);
			this.total += num;
		}
	}
	
	//领取(打开)红包
	public int openRedPacket(Player player) {
		int count = 0; 
		if(this.remain > 0) {
			for(PlayerRedPacket redPacket :this.redPacketList) {
				if(redPacket.getPlayerId() == 0) {
					redPacket.load(player);
					redPacket.setOpenTime(System.currentTimeMillis());
					count = redPacket.getNum(); 
					this.remain -- ;
					saveDB(player.getServerId());
					break;
				}else if(redPacket.getPlayerId() == player.getId()) {
					break;
				}
			}
		}
		return count; 
	}

	public void saveDB(int serverId) {
		setServerId(serverId);
		RedisMapperUtil.update(this);
	}
}
  
