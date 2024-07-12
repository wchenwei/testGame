package com.hm.servercontainer.guild;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.model.guild.Guild;
import com.hm.servercontainer.ItemContainer;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
@Slf4j
public class GuildItemContainer extends ItemContainer{
	private Map<Integer, Guild> dataMap = Maps.newConcurrentMap();
	private Map<Integer, Guild> kfGuildMap = Maps.newConcurrentMap();
	
	public GuildItemContainer(int serverId) {
		super(serverId);
	}
	@Override
	public void initContainer() {
		try {
			log.info( "加载部落信息开始:" +this.getServerId());
			getGuild().forEach(guild -> {
				this.addGuild(guild);
			});
			log.info( "加载部落信息结束:" +this.getServerId() );
		} catch (Exception e) {
			log.error("部落信息加载出错:"+this.getServerId(), e);
		}
	}
	
	private List<Guild> getGuild() {
		List<Guild> list = MongoUtils.getMongodDB(this.getServerId()).queryAll(Guild.class);
		return list;
	}
	
	public Guild getGuild(int guildId) {
		if(guildId <= 0) {
			return null;
		}
		Guild guild = this.dataMap.get(guildId);
		if(guild != null) {
			return guild;
		}
		return this.kfGuildMap.get(guildId);
	}
	
	public void delGuild(int guildId) {
		this.dataMap.remove(guildId);
	}
	//往容器中添加部落
	public void addGuild(Guild guild) {
		this.dataMap.put(guild.getId(), guild);
	}

	public void addKFGuild(Guild guild) {
		this.kfGuildMap.put(guild.getId(), guild);
	}

	//判断部落名字是否重复
	public boolean containsGuildName(String guildName) {
		return dataMap.values().stream()
					.anyMatch(e -> StringUtil.equals(e.getGuildInfo().getGuildName(), guildName));
	}

	//判断部落旗帜是否重复
	public boolean containsGuildFlag(String flagName) {
		return dataMap.values().stream()
					.anyMatch(e -> StringUtil.equals(e.getGuildFlag().getFlagName(), flagName));
	}
	public Map<Integer, Guild> getDataMap() {
		return dataMap;
	}
	
	public List<Guild> getAllGuild() {
		return Lists.newArrayList(dataMap.values());
	}

}












