package com.hm.action.build.biz;/*package com.hm.action.build.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.WallConfig;
import com.hm.config.excel.temlate.WallConfigTemplate;
import com.hm.config.excel.temlate.WallHeadTemplate;
import com.hm.config.excel.temlate.WallNpcBuffTemplate;
import com.hm.config.excel.temlate.WallNpcTemplate;
import com.hm.enums.*;
import com.hm.model.player.Player;
import com.hm.model.tank.FortressTank;
import com.hm.model.war3.WarFightAttr;
import com.hm.model.war3.bean.WarUnit;
import com.hm.util.MathUtils;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;

import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Biz
public class FortressBiz {
    @Resource
    private WallConfig wallConfig;
    @Resource
    private TechCentreBiz techCentreBiz;
    @Resource
    private CommValueConfig commValueConfig;


    public boolean canFortress(Player player) {
        // 城防队列是否满
        int lv = player.getFortress().getLv();
        int size = player.getFortress().getRecruitList().size();
        return size < lv;
    }

    *//**
     * 招募
     *
     * @param player
     *//*
    public void fortress(Player player) {
        FortressTank tank = createTank(player);
        if (tank == null) {
            return;
        }

        // 添加城防坦克
        player.getFortress().recruitTank(tank);
    }

    *//**
     * 获取招募需要的钻石数量
     *
     * @param player
     * @return
     *//*
    public long calcConsume(Player player) {
        // 花费钻石可以将剩余时间直接减为0，消耗的钻石数量=剩余分钟数（向上取整）
        long t = player.getPlayerCDs().getNextCdSecond(CDType.CF) * GameConstants.SECOND;

        double div = MathUtils.div(Math.max(t, 0), GameConstants.MINUTE);

        // 策划配置的倍率
        int value = commValueConfig.getCommValue(CommonValueType.FortressSpeedCost);
        return (long) Math.ceil(div) * value;
    }

    *//**
     * 根据城防等级随机产生一个防守坦克
     *
     * @param player
     * @return
     *//*
    private FortressTank createTank(Player player) {
        int wallLv = player.getFortress().getLv();
        WallConfigTemplate t = wallConfig.getWallConfigTemplate(wallLv);
        if (t == null) {
            log.error("FortressTank.createTank, wall level " + wallLv + " config error!");
            return null;
        }

        FortressTank tank = new FortressTank();
        Random random = new Random();

        // 等级
        int lv = RandomUtils.randomInt(t.getNpc_lv1(), t.getNpc_lv2() + 1);
        tank.setLv(lv);

        // 品质
        tank.setQuality(t.getQuality_initial());

        //招募城防军有机率达到上限
        float techEffect = techCentreBiz.getTechEffect(player, TechIdType.Fortress);
        if (RandomUtils.randomDouble() < techEffect) {
            tank.setLv(t.getNpc_lv2());
            tank.setQuality(t.getQuality_limit());
        }

        // 头像
        int[] ints = StringUtil.strToIntArray(t.getHead(), ":");
        if (ints.length == 2) {
            int b = ints[0], e = ints[1];
            int headId = RandomUtils.randomInt(b, e + 1);
            tank.setHeadId(headId);
        }

        return tank;
    }

    *//**
     * 强化
     *
     * @param player
     * @param uid
     *//*
    public boolean strengthen(Player player, long uid) {
        int wallLv = player.getFortress().getLv();
        FortressTank tank = getTank(player, uid);
        if (tank == null) {
            log.error("FortressTank.strengthen uid = " + uid);
            return false;
        }

        WallConfigTemplate config = wallConfig.getWallConfigTemplate(wallLv);
        int lv_limit = config.getLv_limit(), quality_limit = config.getQuality_limit();
        int lv = tank.getLv(), quality = tank.getQuality();

        Map<EStrengthen, Integer> rand = new Strengthen(lv_limit - lv, quality_limit - quality).rand();
        // 强化满了
        if (rand.isEmpty()) {
            return false;
        }

        EStrengthen meta = RandomUtils.buildWeightMeta(rand).random();
        if (meta.equals(EStrengthen.QUA)) {
            tank.setQuality(tank.getQuality() + 1);
        } else {
            tank.setLv(tank.getLv() + meta.getValue());
        }

        player.getFortress().SetChanged();
        return true;
    }

    *//**
     * 通过uid,查找招募来的tank
     *
     * @param player
     * @param uid
     * @return
     *//*
    public FortressTank getTank(Player player, long uid) {
        return player.getFortress().getRecruitList()
                .stream().filter(t -> t.getUid() == uid).findFirst().orElse(null);
    }

    *//**
     * 获取城防招募npc战斗单位
     *
     * @param player
     * @return
     *//*
    public List<WarUnit> getRecruitWarUnit(Player player) {
        List<WarUnit> listWarUnit = Lists.newArrayList();
        List<FortressTank> tanks = player.getFortress().getRecruitList();
        for (int i=0; i<tanks.size(); i++) {
        	FortressTank tank = tanks.get(i);
            int lv = tank.getLv(), quality = tank.getQuality(), headId = tank.getHeadId();
            WallNpcTemplate npcCfg = wallConfig.getWallNpcTemplate(lv);
            WallNpcBuffTemplate queCfg = wallConfig.getWallNpcBuffTemplate(quality);
            WallHeadTemplate headCfg = wallConfig.getWallHeadTemplate(headId);

            WarFightAttr warFightAttr = getWarFightAttr(npcCfg, queCfg);

            WarUnit warUnit = new WarUnit(tank.getHeadId(), npcCfg.getLevel_npc(), 0,player.getName(),
                    headCfg.getType_head(), headCfg.getTank_id(), warFightAttr, UserType.FORTRESS, i, player.playerBaseInfo().getIcon());
            warUnit.setQuality(quality);//设置品质

            listWarUnit.add(warUnit);
        }
        return listWarUnit;
    }

    private WarFightAttr getWarFightAttr(WallNpcTemplate npcCfg, WallNpcBuffTemplate queCfg) {
        WarFightAttr warFightAttr = new WarFightAttr();
        Map<WarUnitAttrType, Double> attrMap = Maps.newHashMap();
        attrMap.put(WarUnitAttrType.ATK, Double.valueOf(npcCfg.getAtk() * queCfg.getAtk_add()));
        attrMap.put(WarUnitAttrType.DEF, Double.valueOf(npcCfg.getDef() * queCfg.getDef_add()));
        attrMap.put(WarUnitAttrType.HP, Double.valueOf(npcCfg.getHp() * queCfg.getHp_add()));
        attrMap.put(WarUnitAttrType.HIT, Double.valueOf(npcCfg.getHit()));
        attrMap.put(WarUnitAttrType.DODGE, Double.valueOf(npcCfg.getDodge()));
        attrMap.put(WarUnitAttrType.CRIT, Double.valueOf(npcCfg.getCrit()));
        attrMap.put(WarUnitAttrType.CritDef, Double.valueOf(npcCfg.getArmor()));
        attrMap.put(WarUnitAttrType.CRITTIMES, Double.valueOf(npcCfg.getCrit_rate()));
        attrMap.put(WarUnitAttrType.QATK, Double.valueOf(npcCfg.getAtk1()));
        attrMap.put(WarUnitAttrType.QDEF, Double.valueOf(npcCfg.getDef1()));
        attrMap.put(WarUnitAttrType.CATK, Double.valueOf(npcCfg.getAtk2()));
        attrMap.put(WarUnitAttrType.CDEF, Double.valueOf(npcCfg.getDef2()));
        attrMap.put(WarUnitAttrType.HPREAL, Double.valueOf(npcCfg.getHp() * queCfg.getHp_add()));
        attrMap.put(WarUnitAttrType.ROWS, Double.valueOf(npcCfg.getRows()));
        warFightAttr.addAttr(attrMap);
        return warFightAttr;
    }

    private enum EStrengthen {
        LV_1(1, 20),
        LV_2(2, 20),
        LV_3(3, 15),
        LV_4(4, 15),
        LV_5(5, 15),
        QUA(6, 15);

        private int value;
        private int weight; //权重

        EStrengthen(int value, int weight) {
            this.value = value;
            this.weight = weight;
        }

        public int getValue() {
            return value;
        }

        public int getWeight() {
            return weight;
        }
    }


    *//**
     * 可以强化的等级、品质区间
     *//*
    private class Strengthen {
        private int lv; //等级
        private int qua;//品质

        public Map<EStrengthen, Integer> rand() {
            HashMap<EStrengthen, Integer> map = new HashMap<>();
            if (qua > 0) {
                map.put(EStrengthen.QUA, EStrengthen.QUA.getWeight());
            }

            for (EStrengthen e : EStrengthen.values()) {
                if (e == EStrengthen.QUA) {
                    continue;
                }

                if (e.getValue() <= lv) {
                    map.put(e, e.getWeight());
                }
            }

            return map;
        }

        public Strengthen(int lv, int qua) {
            this.lv = lv;
            this.qua = qua;
        }
    }


    *//**
     * getForTressArmySize:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     *
     * @param player
     * @return 使用说明
     * @author zxj
     *//*
    public int getForTressArmySize(Player player) {
        List<WarUnit> listWarUnit = this.getRecruitWarUnit(player);
        int army = 0;
        for (WarUnit unit : listWarUnit) {
            army += unit.getHpReal();
        }
        return army;
    }

    *//**
     * 获取玩家最大可驻防部队数量
     *
     * @param player
     * @return
     *//*
    public int getFortressMaxGarrisonNum(Player player) {
    	WallConfigTemplate template = wallConfig.getWallConfigTemplate(player.getFortress().getLv());
    	if(template != null) {
    		return template.getTank_defence();
    	}
    	return 0;
    }
}*/