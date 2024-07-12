package com.hm.action.warMake;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.WarMakesConfig;
import com.hm.config.excel.temlate.KfSeasonPassGiftTemplate;
import com.hm.config.excel.temlate.RechargePriceNewTemplate;
import com.hm.config.excel.templaextra.KfSeasonPassTemplateImpl;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.enums.WarMakesEnum;
import com.hm.model.activity.kfseason.KFSeasonActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerWarMakes;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 *          玩家战令
 * @date 2021/4/10 21:01
 */
@Biz
public class WarMakesBiz implements IObserver {

    @Resource
    private RechargeConfig rechargeConfig;
    @Resource
    private WarMakesConfig warMakesConfig;
    @Resource
    private PlayerBiz playerBiz;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch(observableEnum) {
            case Recharge :
                int rechargeId = (int)argv[0];
                int giftId = (int)argv[3];
                doRecharge(player, rechargeId, giftId);
                break;
            default:
                break;
        }
    }

    /**
     * @description
     *          领取经验
     * @param player
     * @param experience  经验值
     * @return void
     * @date 2021/4/10 21:44
     */
    public void addExperience(Player player, int experience){
        this.checkWarMakes(player);
        player.playerWarMakes().addExperience(experience);
    }


    public List<Items> receiveReward(Player player, int type, int id){
        PlayerWarMakes playerWarMakes = player.playerWarMakes();
        int stage = playerWarMakes.getStage();
        WarMakesEnum warMakesEnum = WarMakesEnum.getByType(type);
        if(warMakesEnum == null){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return Lists.newArrayList();
        }
        KfSeasonPassTemplateImpl kfSeasonPassTemplate = warMakesConfig.getKfSeasonPassTemplate(stage, id);
        if(kfSeasonPassTemplate == null){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return Lists.newArrayList();
        }
        if(playerWarMakes.getLevel() < kfSeasonPassTemplate.getPass_level()){
            player.sendErrorMsg(SysConstant.WarMakes_Lv_Not_Enough);
            return Lists.newArrayList();
        }
        if(!playerWarMakes.isCanReceive(warMakesEnum, id)){
            return Lists.newArrayList();
        }
        playerWarMakes.addReceive(type, id);
        return warMakesEnum.getRewardByType(kfSeasonPassTemplate);
    }


    public List<Items> receiveAll(Player player){
        PlayerWarMakes playerWarMakes = player.playerWarMakes();
        // 玩家等级
        List<KfSeasonPassTemplateImpl> kfSeasonPassTemplates = warMakesConfig.receiveAll(playerWarMakes.getStage(), playerWarMakes.getLevel());
        if(CollUtil.isEmpty(kfSeasonPassTemplates)){
            return Lists.newArrayList();
        }
        // 所有可领取的 id集合
        List<Integer> idList = kfSeasonPassTemplates.stream().map(KfSeasonPassTemplateImpl::getId).collect(Collectors.toList());
        List<Items> list = Lists.newArrayList();
        for(WarMakesEnum warMakesEnum: WarMakesEnum.values()){
            if(!warMakesEnum.typeIsCanReceive(playerWarMakes)){
                continue;
            }
            List<Items> collect = kfSeasonPassTemplates.stream()
                            .filter(e -> playerWarMakes.isCanReceive(warMakesEnum, e.getId()))
                            .flatMap(e -> warMakesEnum.getRewardByType(e).stream()).collect(Collectors.toList());
            list.addAll(collect);
            playerWarMakes.addAllReceive(warMakesEnum.getType(), Lists.newArrayList(idList));
        }
        // 奖励合并
        List<Items> items = ItemUtils.mergeItemList(list);
        return items;
    }

    private void doRecharge(Player player, int rechargeId, int giftId) {
        RechargePriceNewTemplate rechargeTemplate = rechargeConfig.getTemplate(rechargeId);
        RechargeGiftTempImpl giftTemplate = rechargeConfig.getRechargeGift(giftId);
        if(null==rechargeTemplate || null==giftTemplate) {
            return;
        }
        KFSeasonActivity activity = (KFSeasonActivity)ActivityServerContainer.of(player.getServerId()).getAbstractActivity(ActivityType.KFSeason);
        if(activity == null || !activity.isOpen()) {
            return;
        }
        KfSeasonPassGiftTemplate seasonPassGiftTemplate = warMakesConfig.getGiftTemplate(giftId);
        if(seasonPassGiftTemplate == null){
            return;
        }
        // 防止 在线后赛季开启 直接购买被重置的问题
        this.checkWarMakes(player);

        PlayerWarMakes playerWarMakes = player.playerWarMakes();
        Integer id = seasonPassGiftTemplate.getId();
        if(id == 3 && playerWarMakes.getExperienceBuyTimes() < seasonPassGiftTemplate.getLimit_buy()){
            // 经验书
            playerWarMakes.addExperienceBuyTimes();
        } else if (id < 3) {
            // 购买的 战令类型
            playerWarMakes.addBuyType(seasonPassGiftTemplate.getId());
        }
        // 增加经验
        playerBiz.addWarMakes(player, seasonPassGiftTemplate.getPass_exp_add(), LogType.WarMakes.value(id));
        playerWarMakes.SetChanged();
    }


    /**
     * @description
     *          每天第一次登陆赛季 期数校验
     * @param player
     * @return void
     * @date 2021/4/14 21:39
     */
    public void checkWarMakes(Player player){
        KFSeasonActivity activity = (KFSeasonActivity)ActivityServerContainer.of(player).getAbstractActivity(ActivityType.KFSeason);
        if(activity == null || !activity.isOpen()) {
            return;
        }
       player.playerWarMakes().checkWarMakes(activity.getSeasonId());
    }
}
