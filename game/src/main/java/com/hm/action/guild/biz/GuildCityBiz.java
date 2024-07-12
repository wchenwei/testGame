package com.hm.action.guild.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.hm.action.cityworld.biz.WorldAreaBiz;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.annotation.Broadcast;
import com.hm.config.CityConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.container.PlayerContainer;
import com.hm.enums.WorldType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.camp.CityRewardShow;
import com.hm.model.camp.SimpleCityRewardShow;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Biz
public class GuildCityBiz extends NormalBroadcastAdapter {
    @Resource
    private GuildBiz guildBiz;
    @Resource
    private GuildWorldBiz guildWorldBiz;
    @Resource
    private GuildConfig guildConfig;
    @Resource
    private WorldBiz worldBiz;
    @Resource
    private CityConfig cityConfig;
    @Resource
    private WorldAreaBiz worldAreaBiz;


    @Broadcast(ObservableEnum.GuildDel)
    public void doGuildQuit(ObservableEnum observableEnum, Player player, Object... argv) {
        Guild guild = (Guild) argv[0];
        for (WorldCity worldCity : getGuildCityList(guild.getServerId(), guild.getId())) {
            worldCity.getCityBelong().setGuildId(0);
            worldCity.saveDB();
            worldBiz.broadWorldCityUpdate(worldCity);//广播城池变化
        }
    }


    public List<CityRewardShow> getCityRewardShow(Player player, Guild guild) {
        long lastCityTime = player.playerGuild().getLastCityTime();
        //有部落获取部落的城池奖励
        return guild.getGuildReward().getCityRewardShow(lastCityTime);
    }

    //获取军团占领的城池列表
    public List<WorldCity> getGuildCityList(int serverId,int guildId) {
        return WorldServerContainer.of(serverId).getWorldCitys(WorldType.Normal)
                .stream().filter(e -> e.getBelongGuildId() == guildId)
                .collect(Collectors.toList());
    }

    //军团占领的城池列表达到上限
    public boolean guildCityIsMax(Player player) {
        int guildId = player.getGuildId();
        if(guildId <= 0) {
            return true;
        }
        int cityNum = getPlayerGuildCityMaxNum(player);
        return WorldServerContainer.of(player).getWorldCitys(WorldType.Normal)
                .stream().filter(e -> e.getBelongGuildId() == guildId)
                .count() >= cityNum;
    }

    // 玩家所在军团可拥有的最大城市数量
    public int getPlayerGuildCityMaxNum(Player player){
        Guild guild = guildBiz.getGuild(player);
        if (guild == null){
            return 0;
        }
        return guildConfig.getGuildCityNum(guild.guildLevelInfo().getLv());
    }

    /**
     * 计算阵营每小时时间城池产出奖励
     * @param serverId
     */
    public void doGuildCityHour(int serverId) {
        ArrayListMultimap<Integer, Integer> guildMap = ArrayListMultimap.create();
        for (WorldCity worldCity : WorldServerContainer.of(serverId).getWorldCitys(WorldType.Normal)) {
            if (worldCity.getBelongGuildId() > 0) {
                guildMap.put(worldCity.getBelongGuildId(), worldCity.getId());
            }
        }
        for (Guild guild : GuildContainer.of(serverId).getAllGuild()) {
            try {
                List<Integer> cityList = guildMap.get(guild.getId());
                //添加部落
                if (CollUtil.isNotEmpty(cityList)) {
                    guildWorldBiz.doGuildCityReward(guild, cityList);
                    guild.saveDB();
                    broadGuildCityHaveReward(guild);
                }
            } catch (Exception e) {
                log.error(guild.getId() + "部落奖励出错", e);
            }
        }
    }

    public SimpleCityRewardShow getSimpleCityRewardShow(Player player, Guild guild){
        SimpleCityRewardShow rewardShow = new SimpleCityRewardShow();
        long lastCityTime = player.playerGuild().getLastCityTime();
        if(guild == null) {
            Lists.newArrayList();
        }
        //取部落的城池奖励
        List<Items> rewards = guild.getGuildReward().getCityRewardShow(lastCityTime).stream()
                .flatMap(e -> e.getAllItems().stream()).collect(Collectors.toList());

        List<Integer> cityIds = WorldServerContainer.of(player).getWorldCitys(WorldType.Normal)
                .stream().filter(e -> e.getBelongGuildId() == guild.getId())
                .map(DBEntity::getId).collect(Collectors.toList());

        rewardShow.setAreas(worldAreaBiz.getGuildAreas(cityIds));
        rewardShow.setCityNum(cityConfig.calCityTypeNum(cityIds));
        rewardShow.setRewards(ItemUtils.mergeItemList(rewards));
        return rewardShow;
    }

    public void broadGuildCityHaveReward(Guild guild){
        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Broad_Guild_HaveCityReward);
        PlayerContainer.broadPlayerByGuild(guild.getId(), serverMsg);
    }

    public boolean haveGuildWorldCityReward(Player player, Guild guild) {
        if(guild == null) {
            return false;
        }
        long lastCityTime = player.playerGuild().getLastCityTime();
        //有军团获取军团的城池奖励
        return guild.getGuildReward().havePlayerCityReward(lastCityTime);
    }
}
