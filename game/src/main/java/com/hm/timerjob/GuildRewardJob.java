package com.hm.timerjob;

import com.hm.action.cityworld.biz.ResetWorldBiz;
import com.hm.action.guild.biz.GuildCityBiz;
import com.hm.action.guild.biz.GuildWorldBiz;
import com.hm.server.GameServerManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 
 * ClassName: GuildRewardJob. <br/>  
 * Function: 部落城池福利发送. <br/>
 * date: 2018年11月17日 下午4:32:05 <br/>  
 *  
 * @author zxj  
 * @version
 */
@Slf4j
@Service
public class GuildRewardJob {
	@Resource
	private GuildWorldBiz guildWorldBiz;

	@Resource
	private ResetWorldBiz resetWorldBiz;
	@Resource
	private GuildCityBiz guildCityBiz;
	

	//每天早上八点到晚上23点
	@Scheduled(cron="0 0 8-23 * * ?")  
	//@Scheduled(cron="0/50 * * * * ?")
	public void doGuildReward() {
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
//			Map<Integer, Guild> guildMap = GuildContainer.of(serverId).getDataMap();
//			if(null==guildMap || guildMap.size()==0) {
//				return;
//			}
//			
//			guildMap.values().forEach(guild -> guildWorldBiz.sendCityHourReward(guild));
			try {
				guildCityBiz.doGuildCityHour(serverId);
			} catch (Exception e) {
				log.error(serverId+"阵营奖励出错",e);
			}
			try {
				resetWorldBiz.doResetWorld(serverId);
			} catch (Exception e) {
				log.error(serverId+"重置世界出错",e);
			}
		});
	}
	
	/*@Scheduled(cron="0/10 * * * * ?")  
	public void doGuildReward() {
		System.out.println("=====================");
		guildWorldBiz.sendReward();
	}*/
}











