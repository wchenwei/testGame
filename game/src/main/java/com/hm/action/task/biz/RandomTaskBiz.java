package com.hm.action.task.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.config.CityConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.RandomTaskConfig;
import com.hm.config.excel.templaextra.RandomTaskConfigTemplateImpl;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.RandomTaskType;
import com.hm.enums.WorldType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.task.Random.BaseRandomTask;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.NumericalConstant;
import com.hm.util.RandomUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-12
 *
 * @author Administrator
 */
@Biz
public class RandomTaskBiz implements IObserver {
    @Resource
    private RandomTaskConfig randomTaskConfig;
    @Resource
    private CityConfig cityConfig;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);

    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        if (observableEnum == ObservableEnum.FunctionUnlock) {
            int functionId = (int) argv[0];
            if (functionId == PlayerFunctionType.RandTask.getType()) {
                refreshTask(player);
            }
        }
    }

    /**
     * 每次登录检查
     *
     * @param player
     */
    public void doLoginRandomEventCheck(Player player) {
        if (isCanAutoRefresh(player)) {
            refreshTask(player);
        }
    }

    public boolean haveTroopInCity(Player player, int cityId) {
        return TroopServerContainer.of(player)
                .getWorldTroopByPlayer(player).stream().anyMatch(e -> e.getCityId() == cityId);
    }

    /**
     * 用户当日完成民情事件是否达到最大数量
     *
     * @param player
     * @return
     */
    public boolean isMaxCount(BasePlayer player) {
        return player.playerRandomTask().getCount() >= NumericalConstant.TaskMaxCount;
    }

    /**
     * 随机获取一个已解锁的city id
     *
     * @param player
     * @return
     */
    public int choiceCity(BasePlayer player) {
        // 城市、阵营归属信息
        // cityId,guildId
        Map<Integer, Integer> cityMap = WorldServerContainer.of(player.getServerId()).getWorldCityBelongGuild(WorldType.Normal);
        // 该玩家解锁的最大城市id
        int maxOpenCityId = player.playerMission().getOpenCity();
        // 过滤掉未解锁城市
        cityMap.keySet().removeIf(cityId -> cityId > maxOpenCityId);

        int playerGuildId = player.getGuildId();
        List<Integer> cityList0;
        List<Integer> cityList1 = Lists.newArrayList();
        List<Integer> cityList2 = Lists.newArrayList();
        // 0:该玩家所在阵营占领的城市
        cityList0 = cityMap.entrySet().stream().filter(entry -> entry.getValue().equals(playerGuildId)).map(Map.Entry::getKey).collect(Collectors.toList());
        // 所属阵营没有任何领地时（有领地但未解锁，也视作没有领地	）用巴黎
        if (cityList0.isEmpty()) {
            cityList0.add(GameConstants.ParisId);
        }

        cityMap.keySet().removeIf(cityList0::contains);

        // 1: cityList0 相邻的 距离为1的所有city id
        for (Integer cityId : cityList0) {
            cityList1.addAll(getLinkCities(cityId, cityMap.keySet()));
        }
        // 去重
        cityList1 = CollUtil.distinct(cityList1);
        cityMap.keySet().removeIf(cityList1::contains);

        // 2: cityList1 相邻的 距离为1 or 2的所有city id
        for (Integer cityId : cityList1) {
            Collection<Integer> linkCities = getLinkCities(cityId, cityMap.keySet());
            // 有一半机会找两层link city
            if (RandomUtils.randomInt(100) < 50) {
                for (Integer linkCity : linkCities) {
                    cityList2.addAll(getLinkCities(linkCity, cityMap.keySet()));
                }
            }
            cityList2.addAll(linkCities);
        }
        // 去重
        cityList2 = CollUtil.distinct(cityList2);

        List<Integer> allAvailableCities = Lists.newArrayList();
        allAvailableCities.addAll(cityList0);
        allAvailableCities.addAll(cityList1);
        allAvailableCities.addAll(cityList2);
        allAvailableCities = CollUtil.distinct(allAvailableCities);

        return RandomUtils.randomEle(allAvailableCities);
    }

    private Collection<Integer> getLinkCities(int selfCityId, Collection<Integer> availableCities) {
        List<Integer> linkCityIds = cityConfig.getCityById(selfCityId).getLinkCityIds();
        return CollUtil.intersection(availableCities, linkCityIds);
    }

    /**
     * 刷出一个新task
     *
     * @param player
     * @return
     */
    public boolean refreshTask(Player player) {
        if (isMaxCount(player)) {
            return false;
        }

        RandomTaskConfigTemplateImpl cfg = randomTaskConfig.pickARandomTask();
        if (cfg == null) {
            return false;
        }

        RandomTaskType taskType = RandomTaskType.id2Type(cfg.getId());
        BaseRandomTask randomTask = taskType.getRandomTask();
        randomTask.setCityId(choiceCity(player));

        player.playerRandomTask().setTask(randomTask);
        return true;
    }

    /**
     * 刷出一个指定类型的任务
     *
     * @param player
     * @param taskType
     * @param cityId
     * @return
     */
    public boolean refreshTask(Player player, RandomTaskType taskType, int cityId) {
        if (isMaxCount(player)) {
            return false;
        }

        BaseRandomTask randomTask = taskType.getRandomTask();
        randomTask.setCityId(cityId);

        player.playerRandomTask().setTask(randomTask);
        return true;
    }

    public boolean isCanAutoRefresh(Player player) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task == null) {
            return false;
        }
        if (task.isCanAutoRefresh()) {
            return true;
        }
        return false;
    }

    public void doReward(Player player) {
        BaseRandomTask task = player.playerRandomTask().getTask();
        if (task == null) {
            return;
        }
        //发奖励
        task.doReward(player);
        //发亲密度
        player.playerRandomTask().addCityValue(task.getCityId(), randomTaskConfig.getIntimacy(task.getType()));
        player.notifyObservers(ObservableEnum.RandomTaskComplete);
    }
}
