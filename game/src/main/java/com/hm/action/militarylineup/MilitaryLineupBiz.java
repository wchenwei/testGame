package com.hm.action.militarylineup;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.config.excel.MilitaryLineupConfig;
import com.hm.config.excel.templaextra.MilitaryLineupAssistanceTemplateImpl;
import com.hm.config.excel.templaextra.MilitaryLineupLevelTemplateImpl;
import com.hm.config.excel.templaextra.MilitaryLineupTypeTemplateImpl;
import com.hm.enums.LogType;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.player.MilitaryLineup;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.rits.cloning.Cloner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@Biz
public class MilitaryLineupBiz {
    @Resource
    private MilitaryLineupConfig militaryLineupConfig;
    @Resource
    private TankBiz tankBiz;
    @Resource
    private ItemBiz itemBiz;

    public Map<TankAttrType, Double> getAttrMap(Player player) {
        Map<Integer, MilitaryLineup> rec = player.playerMilitaryLineup().getRec();
        Map<TankAttrType, Double> map = Maps.newConcurrentMap();

        for (Map.Entry<Integer, MilitaryLineup> entry : rec.entrySet()) {
            MilitaryLineupAssistanceTemplateImpl cfg = militaryLineupConfig.getAssistanceCfg(
                    entry.getKey(), entry.getValue().getAssistanceLv());

            if (cfg == null) {
                continue;
            }
            Map<TankAttrType, Double> tmpMap = Cloner.standard().deepClone(cfg.getTankAttrMap());

            // 军阵等级加成部分
            Float attri = 0f;
            MilitaryLineupLevelTemplateImpl levelCfg = militaryLineupConfig.getLevelCfg(entry.getKey(), entry.getValue().getLv());
            if (levelCfg != null) {
                attri = levelCfg.getAttri();
            }

            if (attri > 0) {
                final double rate = 1 + attri;
                tmpMap.replaceAll((k, v) -> v * rate);
            }

            mergeMap(map, tmpMap);
        }
        return map;
    }

    /**
     * @param player
     * @param table  R:id,C:pos,V:tankId
     * @return
     */
    public boolean checkAll(Player player, Table<Integer, Integer, Integer> table) {
        // 监测各个阵型是否解锁
        for (Integer id : table.rowKeySet()) {
            if (!isUnlock(player, id)) {
                return false;
            }
        }

        // 校验各个阵型的位置
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : table.rowMap().entrySet()) {
            if (!checkPosition(entry.getKey(), entry.getValue().keySet())) {
                return false;
            }
        }

        // 所有阵型里tank不能重复
        if (table.values().size() != new HashSet<>(table.values()).size()) {
            return false;
        }

        // tank都是该玩家拥有的
        if (!player.playerTank().getTankIdList().containsAll(table.values())) {
            return false;
        }

        return true;
    }

    /**
     * @param player
     * @param table  R:id,C:pos,V:tankId
     * @return
     */
    public boolean updateAll(Player player, Table<Integer, Integer, Integer> table) {
        if (!checkAll(player, table)) {
            return false;
        }

        player.playerMilitaryLineup().getRec().values().forEach(MilitaryLineup::reset);

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : table.rowMap().entrySet()) {
            // 阵型id
            Integer id = entry.getKey();
            MilitaryLineup militaryLineup = player.playerMilitaryLineup().getMilitaryLineup(id);
            // 记录tank list
            militaryLineup.setTankMap(entry.getValue());
            long combat = tankBiz.getTankCombat(player, new ArrayList<>(entry.getValue().values()));
            // 记录战力
            militaryLineup.setAssistanceValue(combat);
            // 更新等级
            MilitaryLineupAssistanceTemplateImpl cfg = militaryLineupConfig.getAssistanceCfg(id, combat);
            militaryLineup.setAssistanceLv(cfg != null ? cfg.getLevel() : 0);
        }
        player.playerMilitaryLineup().SetChanged();
        return true;
    }

    /**
     * @param player
     * @param id
     * @param m      [pos:tank id]
     */
    public boolean update(Player player, int id, Map<Integer, Integer> m) {
        if (!isUnlock(player, id)) {
            return false;
        }

        if (!checkPosition(id, m.keySet())) {
            return false;
        }

        if (!checkTank(player, id, m.values())) {
            return false;
        }

        MilitaryLineup militaryLineup = player.playerMilitaryLineup().getMilitaryLineup(id);
        militaryLineup.setTankMap(m);
        updateAssistance(player, id);
        return true;
    }

    public boolean lvUp(Player player, int id) {
        if (!isUnlock(player, id)) {
            return false;
        }

        MilitaryLineup militaryLineup = player.playerMilitaryLineup().getMilitaryLineup(id);
        int lvNow = militaryLineup.getLv();
        MilitaryLineupLevelTemplateImpl cfg = militaryLineupConfig.getLevelCfg(id, lvNow + 1);
        if (cfg == null) {
            return false;
        }

        List<Items> costItems = cfg.getCostItems();
        if (!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.MilitaryLineUp)) {
            return false;
        }

        militaryLineup.incLv();
        player.playerMilitaryLineup().SetChanged();
        player.notifyObservers(ObservableEnum.MilitaryLineLvUp, id, lvNow, militaryLineup.getLv(), costItems);
        return true;
    }

    public boolean updateAssistance(Player player, int id) {
        if (!isUnlock(player, id)) {
            return false;
        }


        MilitaryLineup militaryLineup = player.playerMilitaryLineup().getMilitaryLineup(id);
        ArrayList<Integer> list = new ArrayList<>(militaryLineup.getTankMap().values());
        long combat = tankBiz.getTankCombat(player, list);
        MilitaryLineupAssistanceTemplateImpl cfg = militaryLineupConfig.getAssistanceCfg(id, combat);
        militaryLineup.setAssistanceValue(combat);
        militaryLineup.setAssistanceLv(cfg != null ? cfg.getLevel() : 0);
        player.playerMilitaryLineup().SetChanged();
        return true;
    }

    private boolean isUnlock(Player player, int id) {
        MilitaryLineupTypeTemplateImpl cfg = militaryLineupConfig.getTypeCfg(id);
        if (cfg == null) {
            return false;
        }

        return cfg.getLv_unclock() <= player.playerLevel().getLv();
    }

    /**
     * @param id
     * @param posList
     * @return true if all pos ok
     */
    private boolean checkPosition(int id, Collection<Integer> posList) {
        MilitaryLineupTypeTemplateImpl cfg = militaryLineupConfig.getTypeCfg(id);
        if (cfg == null) {
            return false;
        }

        return cfg.getPositions().containsAll(posList);
    }

    /**
     * @param player
     * @param id
     * @param tankList
     * @return true if not tank repeat
     */
    private boolean checkTank(Player player, int id, Collection<Integer> tankList) {
        Map<Integer, MilitaryLineup> rec = player.playerMilitaryLineup().getRec();
        return rec.keySet().stream().filter(i -> !i.equals(id)).allMatch(e -> CollUtil.intersection(tankList, rec.get(e).getTankMap().values()).isEmpty());
    }

    private void mergeMap(Map<TankAttrType, Double> base, Map<TankAttrType, Double> add) {
        add.forEach((key, value) -> base.merge(key, value, Double::sum));
    }
}
