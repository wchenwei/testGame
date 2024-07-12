package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.tank.biz.FactoryBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.tank.FactoryNode;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-16
 */
public class PlayerTankFactory extends PlayerDataContext {
    // 客栈格子数
    private static final int COUNT = 3;
    private static final int MAXLOCKEDCOUNT = 2;
    private FactoryNode[] nodes = new FactoryNode[COUNT];

    // 是否完成引导
    private transient boolean passIntro;

    /**
     * 禁用品阶 (TankRareType::getType)
     */
    private List<Integer> banRareTypeList = Lists.newArrayList();

    public FactoryNode[] getNodes() {
        return nodes;
    }

    public boolean isNull() {
        return Arrays.stream(nodes).noneMatch(Objects::nonNull);
    }

    @Override
    public void initData() {

    }

    @Override
    public void fillMsg(JsonMsg msg) {
        if (!isOpen(Context())) {
            return;
        }
        if (isNull()) {
            reset();
        }
        msg.addProperty("PlayerTankFactory", this);
    }

    private boolean isOpen(BasePlayer player) {
        return player != null && player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankFactory);
    }

    /**
     * 每天0点重置
     */
    public void reset() {
        if (!isOpen(Context())) {
            return;
        }

        FactoryBiz bean = SpringUtil.getBean(FactoryBiz.class);
        if (bean == null) {
            return;
        }

        // 每天把禁用的状态清空
        banRareTypeList.clear();

        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        if (commValueConfig == null) {
            return;
        }

        Collection<Integer> tank;
        if (passIntro) {
            BasePlayer player = Context();
            tank = bean.randomTank(player, null, COUNT);
        } else {
            // 没通过引导时使用策划配置的坦克列表
            int[] initTankList = commValueConfig.getCommonValueByInts(CommonValueType.TankFactoryIntro);
            tank = Arrays.stream(initTankList).boxed().collect(Collectors.toList());
        }

        if (tank.size() != COUNT) {
            return;
        }
        int i = 0;
        for (Integer tankId : tank) {
            FactoryNode node = new FactoryNode();
            node.setTankId(tankId);
            nodes[i++] = node;
        }
        SetChanged();
    }

    /**
     * 锁定状态tank数量
     *
     * @return
     */
    public int lockedCount() {
        return (int) Arrays.stream(nodes).filter(FactoryNode::isLocked).count();
    }

    public boolean lock(int index) {
        if (lockedCount() >= MAXLOCKEDCOUNT) {
            return false;
        }

        if (0 <= index && index < COUNT) {
            FactoryNode node = nodes[index];
            if (null != node && !node.isLocked()) {
                node.setLocked(true);
                SetChanged();
                return true;
            }
        }
        return false;
    }

    public boolean isPassIntro() {
        return passIntro;
    }

    public void setPassIntro(boolean passIntro) {
        if (this.passIntro != passIntro) {
            this.passIntro = passIntro;
            SetChanged();
        }
    }

    public boolean unLock(int index) {
        if (0 <= index && index < COUNT) {
            FactoryNode node = nodes[index];
            if (null != node && node.isLocked()) {
                node.setLocked(false);
                SetChanged();
                return true;
            }
        }
        return false;
    }

    public List<Integer> getBanRareTypeList() {
        return banRareTypeList;
    }
}
