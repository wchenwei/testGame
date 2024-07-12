package com.hm.action.build.biz;/*package com.hm.action.build.biz;

import java.util.List;

import javax.annotation.Resource;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.TechConfig;
import com.hm.config.excel.temlate.TecTemplateImpl;
import com.hm.config.excel.temlate.TecTypeTemplate;
import com.hm.enums.BuildType;
import com.hm.enums.TankType;
import com.hm.enums.TechIdType;
import com.hm.model.baseinterface.BaseBuild;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.tech.Tech;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

*//**
 * Description:
 * User: yang xb
 * Date: 2018-04-17
 *//*
@Biz
public class TechCentreBiz implements IObserver {
    @Resource
    private TechConfig techConfig;

    *//**
     * 该科技是否解锁
     *
     * @param player
     * @param uid
     * @return
     *//*
    public boolean isOpen(Player player, long uid) {
        TecTemplateImpl cfg = techConfig.getTecTemplate(uid);
        if (cfg == null) {
            return false;
        }

        // 该科技已经达到等级上限
        int maxTechLv = techConfig.getMaxTechLv(cfg.getTec_id());
        if (maxTechLv <= cfg.getTec_lv()) { //已经达到等级上限
            return false;
        }

        // 主角等级限制
        int playerLv = player.playerLevel().getLv();
        if (playerLv < cfg.getPlayer_level()) {
            return false;
        }

        // 科技建筑等级限制
        BaseBuild build = player.getBuildByBuildType(BuildType.TechCentre);
        int buildLv = build.getLv();
        if (buildLv < cfg.getTecbuild_level()) {
            return false;
        }

        // 所需科技当前是否完成
        TecTemplateImpl need = techConfig.getTecTemplate(cfg.getTec_id_pre(), cfg.getTec_id_lv());
        if (need != null && !isTechUnlock(player, need.getId())) {
            return false;
        }

        if (!checkTechTypeLimit(player, cfg.getTec_id())) {
            return false;
        }

        if (getUnlockLvMax(player, cfg.getTec_id()) != cfg.getTec_lv()) {
            return false;
        }
        return true;
    }

    private boolean isTechUnlock(Player player, int techUid) {
        TecTemplateImpl cfg = techConfig.getTecTemplate(techUid);
        if (cfg != null) {
            return player.getTechCentre().isTechUnlock(cfg.getTec_id(), cfg.getTec_lv());
        }

        return false;
    }

    *//**
     * 检测tec_type excel 限制条件
     *
     * @param player
     * @param techId
     * @return
     *//*
    private boolean checkTechTypeLimit(Player player, int techId) {
        TecTypeTemplate tecTypeCfg = techConfig.getTecTypeTemplate(techId);
        if (tecTypeCfg == null) {
            return false;
        }

        BaseBuild build = player.getBuildByBuildType(BuildType.TechCentre);
        int buildLv = build.getLv();
        // 科技开启对科技建筑的等级限制
        if (buildLv < tecTypeCfg.getUnlock_lv()) {
            return false;
        }

        // 前置条件，必须先解锁某个科技
        if (tecTypeCfg.getUnlock_tec() > 0) {
            Integer preTechUid = tecTypeCfg.getUnlock_tec();
            if (!isTechUnlock(player, preTechUid)) {
                return false;
            }
        }
        return true;
    }

    *//**
     * 是否触发科技升级
     *
     * @param player
     * @param techId
     * @return
     *//*
    private boolean checkLvUp(Player player, int techId) {
        Tech tech = player.getTechCentre().getTech(techId);
        if (tech == null) {
            return false;
        }

        TecTemplateImpl cfg = techConfig.getTecTemplate(techId, tech.getLv());
        if (cfg == null) {
            return false;
        }

        if (cfg.getUp_stage() > tech.getStage()) {
            return false;
        }

        if (tech.getLv() >= techConfig.getMaxTechLv(techId)) {
            return false;
        }
        return true;
    }

    *//**
     * 完成一次某个科技
     *
     * @param player
     * @param techId
     * @param lv
     *//*
    public void finishTech(Player player, int techId, int lv) {
        player.getTechCentre().incStage(techId, lv);
        if (checkLvUp(player, techId)) {
            player.getTechCentre().techLvUp(techId);
        }
    }

    *//**
     * 获取该科技目前达到的等级
     *
     * @param player
     * @param techId
     * @return
     *//*
    public int getUnlockLvMax(BasePlayer player, int techId) {
        return player.getTechCentre().getUnlockLvMax(techId);
    }

    *//**
     * 获取科技加成
     *
     * @param techIdType 科技id enum
     * @return
     *//*
    public float getTechEffect(BasePlayer player, TechIdType techIdType) {
    	if(player == null) {
    		return 0f;
    	}
        Tech tech = player.getTechCentre().getTech(techIdType.getType());
        if (tech != null) {
            TecTemplateImpl template = techConfig.getTecTemplate(techIdType.getType(), tech.getLv());
            if (template != null) {
                return template.getTec_effect();
            }
        }

        return 0f;
    }

    *//**
     * 自动解锁科技
     *
     * @param player
     *//*
    public void autoUnlockTech(Player player) {
        BaseBuild build = player.getBuildByBuildType(BuildType.TechCentre);
        if (build == null || build.isLock()) {
            return;
        }

        // 获取所有科技类型
        List<TecTypeTemplate> list = techConfig.getTecTypeTemplateList();
        for (TecTypeTemplate t : list) {
            Integer unlockLv = t.getLv_base(); //开启后默认等级
            Integer techId = t.getId();
            if (!checkTechTypeLimit(player, techId)) {//不满足解锁默认等级条件
                continue;
            }

            int unlockLvMax = getUnlockLvMax(player, techId);
            if (unlockLvMax > unlockLv) {    //用户当前科技等级已经大于默认解锁等级
                continue;
            }

            if (unlockLvMax < unlockLv) {
                player.getTechCentre().setStage(techId, unlockLv, 0);
                continue;
            }

            Tech tech = player.getTechCentre().getTech(techId);
            if (tech == null) {
                player.getTechCentre().setStage(techId, unlockLv, 0);
            }
        }
    }
    
    *//**
     * 计算坦克攻击加成
     *//*
    public int calTankAddAtk(Player player,int tankType) {
    	TechIdType techIdType = null;
    	if(tankType == TankType.ARMOR.getType()) {
    		techIdType = TechIdType.ArmorAtk;
    	}else if(tankType == TankType.TANK.getType()) {
    		techIdType = TechIdType.TankAtk;
    	}else if(tankType == TankType.ROCKET.getType()) {
    		techIdType = TechIdType.RocketAtk;
    	}
    	if(techIdType == null) {
    		return 0;
    	}
    	return (int)getTechEffect(player, techIdType);
    }
    
    

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.TechnologLevelUp, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        if (observableEnum == ObservableEnum.TechnologLevelUp) {
            int techId = (int) argv[0];
            if (techId > 0) {
                Integer uid = techConfig.getTecTemplate(techId, getUnlockLvMax(player, techId)).getId();
                List<TecTypeTemplate> list = techConfig.getTecTypeTemplateList();
                // 有前置科技解锁条件的技能
                if (list.stream().anyMatch(t -> t.getUnlock_tec().equals(uid))) {
                    autoUnlockTech(player);
                }
            }
        }
    }
}
*/