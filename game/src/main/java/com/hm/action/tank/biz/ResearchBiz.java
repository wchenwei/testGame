package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.vo.ItemsVo;
import com.hm.annotation.Broadcast;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankLuckConfig;
import com.hm.config.excel.temlate.LuckyBaseTemplate;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import com.hm.war.sg.setting.TankSetting;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description:
 * User: yang xb
 * Date: 2018-10-15
 */
@Slf4j
@Biz
public class ResearchBiz extends NormalBroadcastAdapter {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private TankConfig tankConfig;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private TankLuckConfig tankLuckConfig;

    public static final int SeniorType = 1;

    /**
     * 捕猎
     * @param player
     * @param researchType
     * @return
     */
    public List<ItemsVo> seniorCustom(Player player, TankResearchType researchType) {
        if (!checkAndSpendItems(player, researchType)){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return null;
        }
        int count = researchType.getCount();
        List<Items> itemsListRaw = IntStream.range(0, count).mapToObj(i -> roundOne(player)).collect(Collectors.toList());

        List<ItemsVo> itemsListVo = this.changeToPiece(itemsListRaw, player);
        List<Items> itemsList = ItemBiz.createItemList(voToItems(itemsListVo));
        itemBiz.addItem(player, itemsList, LogType.TankSeniorSingle.value(researchType.getType()));
        player.notifyObservers(ObservableEnum.TankTechResearch, researchType.getType(), count);
               return itemsListVo;
    }

    private boolean checkAndSpendItems(Player player, TankResearchType researchType) {
        if (researchType == TankResearchType.Ad){
            return true;
        }
        if (researchType == TankResearchType.One){// 单抽 检查使用有免费次数
            int freeTimes = commValueConfig.getCommValue(CommonValueType.TankResearchFreeTimes);
            // 免费次数
            if (player.playerResearchTank().getFreeTimes() < freeTimes){
                if (player.playerResearchTank().getFreeEndTime() <= System.currentTimeMillis()){
                    int cdSecond = commValueConfig.getCommValue(CommonValueType.TankResearchFreeTimesCd);
                    player.playerResearchTank().addFreeTimes(cdSecond);
                    return true;
                }
            }
        }
        List<Items> totalCost = calCostItems(player, researchType);
        return itemBiz.checkItemEnoughAndSpend(player, totalCost, LogType.TankSeniorSingle.value(researchType.getType()));
    }

    private List<Items> calCostItems(Player player, TankResearchType researchType) {
        List<Items> totalCost = Lists.newArrayList();
        int count = researchType.getCount();
        List<Items> freeItems = commValueConfig.getListItem(CommonValueType.TankResearchFreeItem);
        Items items = freeItems.get(0);
        // 玩家身上的道具数量
        long itemTotal = itemBiz.getItemTotal(player, items.getItemType(), items.getId());
        // 本次消耗的数量
        long costTotal = Math.min(count, itemTotal);
        totalCost.addAll(ItemUtils.calItemRateReward(freeItems, costTotal));
        if (count > costTotal){// 道具数量不足补金砖
            int price = commValueConfig.getCommValue(CommonValueType.LuckyItemCost);
            long goldCount = price * (count - costTotal);
            totalCost.add(PlayerAssetEnum.Gold.buildItems(goldCount));
        }
        return totalCost;
    }

    private Items roundOne(Player player) {
        player.playerResearchTank().incSeniorTimes();
        LuckyBaseTemplate luckyBaseTemplate = getLuckyBaseTemplate(player);
        // 检查增加权重
        checkAndRestAddWeight(player, luckyBaseTemplate);
        // 是一个心愿池子 并且玩家有心愿
        if (luckyBaseTemplate.getWish_pond() > 0 && player.playerResearchTank().haveWish()){
            double rate = commValueConfig.getDoubleCommValue(CommonValueType.LuckyWishRate);
            if (RandomUtils.randomIsRate(rate)){// 心愿达成
                return new Items(player.playerResearchTank().getWishTankId(), 1, ItemType.TANK);
            }
        }
        return tankLuckConfig.randItems(luckyBaseTemplate.getId());
    }

    //判断获取到的坦克，用户是否已经获取，如果获取则转换成碎片
    private List<ItemsVo> changeToPiece(List<Items> itemsList, Player player) {
        List<ItemsVo> itemsVo = Lists.newArrayList();
        itemsList.forEach(item -> {
            if (ItemType.TANK != item.getEnumItemType()) {
                itemsVo.add(new ItemsVo(item, false));
            } else {
                if (player.playerTank().isHaveTank(item.getId())) {// 已经拥有该tank
                    itemsVo.add(item2ItemsVo(item));
                    // 这批奖励里已经加过一辆该tank
                } else if (itemsVo.stream().anyMatch(v -> v.getEnumItemType() == ItemType.TANK && v.getId() == item.getId())) {
                    //转为坦克图纸
                    itemsVo.add(item2ItemsVo(item));
                } else {
                    itemsVo.add(new ItemsVo(item, false));
                }
            }
        });
        return itemsVo;
    }

    private ItemsVo item2ItemsVo(Items item) {
        //转为坦克图纸
        TankSetting template = tankConfig.getTankSetting(item.getId());
        int paperCount = tankConfig.getTankStarTemplate(template.getStar()).getRepeat_paper();
        int paperId = template.getPaper_id();
        return new ItemsVo(new Items(paperId, paperCount, ItemType.PAPER), true);
    }

    public List<Items> voToItems(List<ItemsVo> itemsList) {
        List<Items> items = Lists.newArrayList();
        itemsList.forEach(item -> {
            items.add(item);
        });
        return items;
    }


    private LuckyBaseTemplate getLuckyBaseTemplate(Player player) {
        int mustTankTimes = commValueConfig.getCommValue(CommonValueType.LuckyMustSTankTimes);
        // 额外增加的权重
        Map<Integer, Integer> addPondWeight = player.playerResearchTank().getAddWeightMap();
        // 是否必得tank
        boolean bTank = player.playerResearchTank().getSeniorTimes() >= mustTankTimes;

        LuckPondType pondType = LuckPondType.Normal;
        if(bTank) {
            player.playerResearchTank().resetSeniorTimes();
            pondType = LuckPondType.TenMust;
        }
        return tankLuckConfig.randomLuckTemplate(pondType, addPondWeight);
    }

    public void checkAndRestAddWeight(Player player, LuckyBaseTemplate template){
        List<LuckyBaseTemplate> templates = tankLuckConfig.getAddWeightTemplates();
        for (LuckyBaseTemplate luckyBaseTemplate : templates) {
            if (luckyBaseTemplate.getWeight_add() > 0){
                int pondId = luckyBaseTemplate.getId();
                if (pondId != template.getId()){
                    // x次不出 增加权重
                    if (player.playerResearchTank().getNotPondTimes(pondId) >= luckyBaseTemplate.getWeight_add_base()){
                        player.playerResearchTank().addNotPondWeight(pondId, luckyBaseTemplate.getWeight_add());
                    }
                    player.playerResearchTank().addNotPondTimes(pondId);
                } else {
                    // 得到后将权重重置
                    player.playerResearchTank().clearNotPondWeight(pondId);
                }

            }
        }
    }


    @Broadcast(ObservableEnum.TankTechResearch)
    public void doStatistic(ObservableEnum observableEnum, Player player, Object... argv) {
        int count = (int) argv[1];
        player.getPlayerStatistics().addLifeStatistics(StatisticsType.TankSeniorResearch, count);
        player.getPlayerStatistics().addTodayStatistics(StatisticsType.TankSeniorResearch, count);
    }
}







