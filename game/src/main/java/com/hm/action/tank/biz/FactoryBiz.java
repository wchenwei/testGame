package com.hm.action.tank.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.TankPaperTemplate;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankRareType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.RandomUtils;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-16
 */
@Biz
public class FactoryBiz implements IObserver {
    // 客栈格子数
    private static final int COUNT = 3;
    // 每日绘制最大次数
    private static final int MAXCOUNT = 100;
    @Resource
    private TankConfig tankConfig;
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private DropConfig dropConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private ActivityBiz activityBiz;


    /**
     * 拥有图纸的或者已经获得的坦克
     *
     * @param player
     * @return
     */
    private Collection<Integer> tankSource(BasePlayer player) {
        // 玩家拥有的tank
        List<Tank> tankList = player.playerTank().getTankList();
        List<Integer> tankIdList = tankList.stream().map(Tank::getId).collect(Collectors.toList());

        // 玩家拥有的碎片
        Set<Integer> papers = player.playerTankPaper().getData().keySet();
        List<Integer> paper2tankIdList = papers.stream().map(paper -> tankConfig.getTankPaperTemplate(paper)).
                filter(Objects::nonNull).map(TankPaperTemplate::getTank_id).collect(Collectors.toList());

        Set<Integer> collect = Stream.of(tankIdList, paper2tankIdList).flatMap(Collection::stream).collect(Collectors.toSet());
        // 过滤掉部分品阶tank
        List<Integer> ban = player.playerTankFactory().getBanRareTypeList();
        if (CollUtil.isNotEmpty(ban)) {
            List<Integer> result = Lists.newArrayList();
            for (Integer tankId : collect) {
                TankRareType type = TankRareType.getType(tankConfig.getTankSetting(tankId).getRare());
                if (!ban.contains(type.getType())) {
                    result.add(tankId);
                }
            }
            return result;
        }
        return collect;
    }

    /**
     * 换一批操作
     * 点击“换一批”按钮，则会从刷新列表中随机未锁定数量的坦克并替换
     *
     * @param player
     * @param exclude
     * @param count
     * @return
     */
    public Collection<Integer> randomTank(BasePlayer player, Collection<Integer> exclude, int count) {
        player.playerTankFactory().setPassIntro(true);
        Collection<Integer> integers = tankSource(player);
        if (!CollUtil.isEmpty(exclude)) {
            integers.removeAll(exclude);
        }

        // 策划新需求,每次随机tank可重复
        return RandomUtils.randomRepeatableEleList(Lists.newArrayList(integers), count);
    }


    public boolean lock(BasePlayer player, int index) {
        return player.playerTankFactory().lock(index);
    }

    public boolean unLock(BasePlayer player, int index) {
        return player.playerTankFactory().unLock(index);
    }



    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        int funcId = Integer.parseInt(argv[0].toString());
        if (funcId != PlayerFunctionType.TankFactory.getType()) {
            return;
        }
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankFactory)) {
            return;
        }
        if (player.playerTankFactory().isNull()) {
            player.playerTankFactory().reset();
        }
    }
}









