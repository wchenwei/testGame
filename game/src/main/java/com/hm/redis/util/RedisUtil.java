package com.hm.redis.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.guild.Guild;
import com.hm.model.player.BasePlayer;
import com.hm.redis.InviteInfoData;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.ServerCampOfficialData;
import com.hm.redis.data.GuildRedisData;
import com.hm.redis.type.RedisTypeEnum;

import java.util.List;
//redis的工具类。（简单存取，可以用RankEnum, TypeEnum操作）
public class RedisUtil {
	public static void updateRedisPlayer(PlayerRedisData playerRedisData) {
		RedisTypeEnum.Player.put(playerRedisData.getId(), JSON.toJSONString(playerRedisData, SerializerFeature.NotWriteDefaultValue));
	}
	//把player存到redis缓存中
	public static void updateRedisPlayer(BasePlayer player) {
		RedisTypeEnum.Player.put(player.getId(), JSON.toJSONString(new PlayerRedisData(player), SerializerFeature.NotWriteDefaultValue));
	}
	public static void updateRedisGuild(Guild guild) {
		RedisTypeEnum.Guild.put(guild.getId(), JSON.toJSONString(new GuildRedisData(guild)));
	}
	public static void updateRedisInviteCode(BasePlayer player){
		RedisTypeEnum.InviteCode.put(player.getInviteCode(), JSON.toJSONString(player.getId()));
	}
	public static void updateRedisBindPhone(BasePlayer player,String phone){
		RedisTypeEnum.BindPhone.put(player.getUid(), JSON.toJSONString(phone));
	}
	//玩家信息
	public static PlayerRedisData getPlayerRedisData(long playerId) {
		return GSONUtils.FromJSONString(RedisTypeEnum.Player.get(String.valueOf(playerId)), PlayerRedisData.class);
	}
	//玩家信息是否存在
	public static boolean isExitPlayer(long playerId){
		return RedisTypeEnum.Player.isExit(String.valueOf(playerId));
	}
	public static InviteInfoData getInviteInfoData(long playerId){
		return GSONUtils.FromJSONString(RedisTypeEnum.InviteInfo.get(String.valueOf(playerId)), InviteInfoData.class);
	}
	public static void updateInviteInfo(long id,InviteInfoData inviteInfoData) {
		RedisTypeEnum.InviteInfo.put(id, JSON.toJSONString(inviteInfoData, SerializerFeature.NotWriteDefaultValue));
	}
	//部落信息
	public static GuildRedisData getGuildRedisData(int guildId) {
		return GSONUtils.FromJSONString(RedisTypeEnum.Guild.get(String.valueOf(guildId)), GuildRedisData.class);
	}
	
	public static long getPlayerIdByInviteCode(String code){
		String result = RedisTypeEnum.InviteCode.get(code);
		if(StrUtil.isBlank(result)){
			return -1;
		}
		return Integer.parseInt(result);
	}
	//根据用户uid获取用户手机号
	public static String getPhoneByUid(String uid){
		String result = RedisTypeEnum.BindPhone.get(uid);
		if(StrUtil.isBlank(result)){
			return "-1";
		}
		return result;
	}
	
	public static List<PlayerRedisData> getListPlayer(List<Long> ids) {
		List<String> listObj = RedisTypeEnum.Player.getListObj(RedisTypeEnum.Player, ids);
		List<PlayerRedisData> listPlayer = Lists.newArrayList();
		if(null!=listObj && listObj.size()>0) {
			listObj.forEach(obj->{
				if(null!=obj) {
					listPlayer.add(JSON.toJavaObject(JSONObject.parseObject(obj), PlayerRedisData.class));
				}
			});
		}
		return listPlayer;
	}
	
	public static List<GuildRedisData> getListGuild(List<Long> ids) {
		List<String> listObj = RedisTypeEnum.Player.getListObj(RedisTypeEnum.Guild, ids);
		List<GuildRedisData> listGuild = Lists.newArrayList();
		if(null!=listObj && listObj.size()>0) {
			listObj.forEach(obj->{
				listGuild.add(JSON.toJavaObject(JSONObject.parseObject(obj), GuildRedisData.class));
			});
		}
		return listGuild;
	}
	public static String getServerName(int serverId) {
		String result = RedisTypeEnum.ServerName.get(String.valueOf(serverId));
		if(StrUtil.isBlank(result)){
			return "未知-"+serverId;
		}
		return result;
	}

	public static void updateCampOfficial(int serverId, ServerCampOfficialData data){
		RedisTypeEnum.CampOfficial.put(serverId, JSON.toJSONString(data, SerializerFeature.NotWriteDefaultValue));
	}

	public static ServerCampOfficialData getCampOfficial(int serverId){
		return GSONUtils.FromJSONString(RedisTypeEnum.CampOfficial.get(String.valueOf(serverId)), ServerCampOfficialData.class);
	}
}
