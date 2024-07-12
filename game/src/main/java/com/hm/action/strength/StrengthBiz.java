package com.hm.action.strength;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.strength.StrengthConfig;
import com.hm.config.strength.excel.*;
import com.hm.enums.*;
import com.hm.log.LogBiz;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.strength.*;
import com.hm.model.tank.TankAttr;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Biz
@Slf4j
public class StrengthBiz{
    @Resource
    private StrengthConfig strengthConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private TankConfig tankConfig;
    @Resource
    private LogBiz logBiz;
    // id = x* COEFFICIENT +y
    private static final int COEFFICIENT = 100;

    public boolean operation(Player player, StrengthStore store, StrengthModel strengthModel, JsonMsg msg){
        boolean operationUp = msg.getBoolean("operationUp");
        if(!operationUp){
            return this.operationDown(player, store, strengthModel);
        }
        return this.operationUp(player, store, strengthModel, msg);
    }

    /**
     * @param player
     * @param store
     * @param update 是否更新 属性
     */
    public void attrUpdate(Player player, StrengthStore store, boolean update){
        if(update && CollUtil.isNotEmpty(store.getList())){
            store.setAttrList(store.getList());
            this.notifyObserve(player, store, ObservableEnum.StrengthAttrChange);
        }
        store.setList(null);
    }

    public boolean attr(Player player, StrengthStore store, String costUid){
        StrengthStore costStore = player.playerStrength().getStore(costUid);
        if(costStore == null || costStore.isLock()){
            return false;
        }
        if(player.playerStrength().checkStoreUsed(costStore)){
            // 在其他地方上阵中
            return false;
        }
        BlockPartsTemplateImpl blockPartsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
        if(blockPartsTemplate == null){
            return false;
        }
        BlockPartsTemplateImpl costTemplate = strengthConfig.getBlockPartsTemplate(costStore.getId());
        if(!blockPartsTemplate.checkStarAndGrid(costTemplate)){
            // 不满足 精炼条件
            return false;
        }
        List<Integer> randomAttr = strengthConfig.getRandomAttr(blockPartsTemplate);
        store.setList(randomAttr);
        this.costStore(player, Lists.newArrayList(costStore), "attr");
        return true;
    }

    public StrengthStore lvUp(Player player, StrengthStore store, BlockPartsTemplate partsTemplate, String costIds){
        List<StrengthStore> costList = Lists.newArrayList();
        int expSum = StringUtil.splitStr2StrList(costIds, ",").stream().mapToInt(uid -> {
            StrengthStore costStore = player.playerStrength().getStore(uid);
            if (costStore == null || costStore.isLock()) {
                return 0;
            }
            if (player.playerStrength().checkStoreUsed(costStore)) {
                // 正在使用中
                return 0;
            }
            BlockPartsTemplate blockPartsTemplate = strengthConfig.getBlockPartsTemplate(costStore.getId());
            if(blockPartsTemplate == null){
                return 0;
            }
            costList.add(costStore);
            return blockPartsTemplate.getExp();
        }).sum();
        if(expSum <= 0){
            return store;
        }
        int[] ints = strengthConfig.calLv(store, partsTemplate, expSum + store.getExp());
        store.setLv(ints[0]);
        store.setExp(ints[1]);
        this.costStore(player, costList, "lv");
        return store;
    }

    public boolean sublimation(Player player, StrengthStore store, BlockPartsTemplate partsTemplate){
        BlockSublimeTemplateImpl template = strengthConfig.getBlockSublimeTemplateImpl(store.getSublimationTimes());
        if(template == null){
            return false;
        }
        List<Items> cost = template.getCost(partsTemplate.getGird());
        if(CollUtil.isEmpty(cost)){
            return false;
        }
        if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Strength.value("sublimation"))){
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return false;
        }
        store.upSublimation();
        this.notifyObserve(player, store, ObservableEnum.StrengthSublimationChange);
        return true;
    }

    public List<StoreVo> maxLvUpCost(Player player, StrengthStore store, BlockPartsTemplate partsTemplate){
        // 从当前级升到满级所需经验
        int maxLvUpCost = strengthConfig.maxLvUpCost(store, partsTemplate);
        // 当前经验
        int exp = store.getExp();
        int needExp = maxLvUpCost - exp;
        if(needExp <= 0){
            return Lists.newArrayList();
        }
        List<StoreVo> collect = player.playerStrength().getFreeStoreList()
                .stream()
                .map(e -> {
                    BlockPartsTemplate blockPartsTemplate = strengthConfig.getBlockPartsTemplate(e.getId());
                    if(blockPartsTemplate == null){
                        return null;
                    }
                    return new StoreVo(e, blockPartsTemplate);})
                .filter(Objects::nonNull)
                .sorted()
                .limit(commValueConfig.getCommValue(CommonValueType.Strength_LvCost_Limit))
                .collect(Collectors.toList());

        List<StoreVo> list = Lists.newArrayList();
        for(StoreVo storeVo: collect){
            list.add(storeVo);
            needExp -= storeVo.getExp();
            if(needExp <= 0){
                break;
            }
        }
        return list;
    }


    public List<StrengthStore> research(Player player, int num){
        List<StrengthStore> list = Lists.newArrayList();
        for(int i = 0; i< num; i++){
            boolean lucky = player.playerStrength().isLucky();
            BlockPartsTemplateImpl template = strengthConfig.getRandomBlockParts(lucky);
            if(template == null){
                continue;
            }
            StrengthStore store = this.addStore(player, template, "research");
            list.add(store);
            if(template.getStar() >= StrengthConfig.RARE_Star){
                // 幸运值清空
                player.playerStrength().setLuckyValue(0);
            }else {
                player.playerStrength().incLuckyValue(1);
            }
        }
        return list;
    }

    public void addItem(Player player, int itemId, long count){
        BlockPartsTemplateImpl template = strengthConfig.getBlockPartsTemplate(itemId);
        if(template == null){
            log.error("BlockPartsTemplateImpl 配件 模板不存在  id--》{}",itemId);
            return;
        }
        for(int i = 0; i< count; i++){
            // 随机 属性，添加到 用户身上
            this.addStore(player, template, "box");
        }
        player.playerStrength().SetChanged();
    }

    public List<Items> researchCost(Player player, boolean single){
        if(single){
            if(player.getPlayerCDs().checkCanCd(CDType.Strength)){
                // 拥有免费次数
                return Lists.newArrayList();
            }
            return commValueConfig.getConvertObj(CommonValueType.Strength_Single_Cost);
        }
        return commValueConfig.getConvertObj(CommonValueType.Strength_Ten_Cost);
    }


    public StrengthStore addStore(Player player, BlockPartsTemplateImpl randomBlockPart, String extra){
        // 随机属性 数据
        StrengthStore store = new StrengthStore(randomBlockPart);
        store.setUid(player.getServerId());
        List<Integer> randomAttr = strengthConfig.getRandomAttr(randomBlockPart);
        store.setAttrList(randomAttr);
        player.playerStrength().addStore(store);
        this.addLog(player, Lists.newArrayList(store), extra, true);
        return store;
    }

    /**
     * 全体类型 坦克属性加成
     * @param player
     * @return
     */
    public Map<Integer, TankAttr> getStrengthAttr(Player player) {
        Map<Integer,TankAttr> typeMap = Maps.newConcurrentMap();
        // 功能未开启
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.PlayerStrength)){
            return typeMap;
        }
        // 所有上阵的 配件
        for(StrengthModel model: player.playerStrength().getAllStrengthModel()){
            TankAttr tankAttr = new TankAttr();
            List<String> allStore = model.getAllStore();
            allStore.forEach(uid->{
                StrengthStore store = player.playerStrength().getStore(uid);
                if(store == null){
                    return;
                }
                BlockPartsTemplateImpl blockPartsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
                if(blockPartsTemplate == null){
                    return;
                }
                for (Integer attrId : store.getAttrList()) {
                    BlockAttrTemplate attrTemplate = strengthConfig.getBlockAttrTemplate(attrId);
                    if(attrTemplate == null){
                        continue;
                    }
                    // 属性加成值 = 零件占格子数*（start_value + （零件等级-1）* levelup_value + 升华等级*sublimation_value）
                    double value = blockPartsTemplate.getGird() *( attrTemplate.getStart_value() + (store.getLv() -1) * attrTemplate.getLevelup_value() + store.getSublimationTimes() * attrTemplate.getSublimation_value());
                    tankAttr.addAttr(attrTemplate.getAttr_id(), value);
                }
            });
            // 计算同类型 满星属性加成
            tankAttr.addAttr(this.calModelStar(player, model));
            typeMap.put(model.getType(), tankAttr);
        }
        return typeMap;
    }

    public void notifyObserve(Player player, StrengthStore store, ObservableEnum observableEnum){
        // 是否上阵中
        boolean used = player.playerStrength().checkStoreUsed(store);
        player.notifyObservers(observableEnum, store.getUid(), used);
    }

    private TankAttr calModelStar(Player player, StrengthModel model){
        TankAttr tankAttr = new TankAttr();
        BlockMapTemplateImpl mapTemplateImpl = strengthConfig.getBlockMapTemplateByType(model.getType());
        if(mapTemplateImpl == null){
            return tankAttr;
        }
        List<Integer> gridList = model.getGridList();
        if(!mapTemplateImpl.checkAllUsed(gridList)){
            return tankAttr;
        }
        // 该模块所有的 上阵配件最小星级
        int minStar = model.getAllStore().stream().mapToInt(uid -> {
            StrengthStore store = player.playerStrength().getStore(uid);
            BlockPartsTemplateImpl blockPartsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
            if (blockPartsTemplate == null || !Objects.equals(blockPartsTemplate.getTank_type(), mapTemplateImpl.getTank_type())) {
                // 系别不一致，则没有技能数据
                return 0;
            }
            return blockPartsTemplate.getStar() + store.getSublimationTimes();
        }).min().orElse(0);

        if(minStar <= 0){
            return tankAttr;
        }
        return mapTemplateImpl.getTankAttrByStar(minStar);
    }

    private boolean operationUp(Player player, StrengthStore store, StrengthModel strengthModel, JsonMsg msg){
        List<Integer> gridList = StringUtil.splitStr2IntegerList(msg.getString("gridIds"), ",");
        if(CollUtil.isEmpty(gridList)){
            return false;
        }
        if(!strengthModel.useStore(store.getUid()) && player.playerStrength().checkStoreUsed(store)){
            // 不在当前模块 的其他地方上阵了
            return false;
        }
        if(strengthModel.checkBlankGrid(store, gridList)){
            // 和其他格子有重合位置
            return false;
        }
        BlockMapTemplateImpl mapTemplateImpl = strengthConfig.getBlockMapTemplateByType(strengthModel.getType());
        if(!mapTemplateImpl.checkBlank(gridList)){
            // 可填充格子位置不对
            return false;
        }
        BlockPartsTemplateImpl blockPartsTemplate = strengthConfig.getBlockPartsTemplate(store.getId());
        if(blockPartsTemplate == null){
            return false;
        }
        BlockModelTemplateImpl modelTemplate = strengthConfig.getBlockModelTemplateImpl(blockPartsTemplate.getModel_id());
        if(modelTemplate == null){
            return false;
        }
        // 配件模型
        int angle = msg.getInt("angle");
        List<Integer> modelShape = modelTemplate.getModelShape(angle);
        if(!this.checkGrid(gridList, modelShape)){
            // 格子模型校验错误
            return false;
        }
        strengthModel.up(store.getUid(), new StoreInfo(store, gridList, msg));
        return true;
    }

    private boolean operationDown(Player player, StrengthStore store, StrengthModel strengthModel){
        strengthModel.down(store.getUid());
        return true;
    }

    private void costStore(Player player,List<StrengthStore> storeList, String extra){
        player.playerStrength().removeStore(storeList);
        this.addLog(player, storeList, extra, false);
    }

    private boolean checkGrid(List<Integer> gridList, List<Integer> modelShape){
        if(CollUtil.isEmpty(gridList) || CollUtil.isEmpty(modelShape)){
            return false;
        }
        if(gridList.size() != modelShape.size()){
            return false;
        }
        int x = gridList.stream().mapToInt(e -> e / COEFFICIENT).min().orElse(1) - 1;
        int y = gridList.stream().mapToInt(e -> e % COEFFICIENT).min().orElse(1) - 1;
        return modelShape.stream().map(gridId -> {
            int gridX = gridId / COEFFICIENT;
            int gridY = gridId % COEFFICIENT;
            // 平移
            return (gridX + x) * COEFFICIENT + (y + gridY);
        }).allMatch(gridList::contains);
    }

    private void addLog(Player player, List<StrengthStore> storeList, String extra, boolean isAdd){
        storeList.forEach(store -> {
            BlockPartsTemplateImpl template = strengthConfig.getBlockPartsTemplate(store.getId());
            if(template == null){
                return;
            }
            StoreLogVo logVo = new StoreLogVo(store, template, extra);
            if(isAdd){
                logBiz.addGoods(player, store.getId(), 1, ItemType.StrengthStore.getType(), LogType.Strength.value(GSONUtils.ToJSONString(logVo)));
            }else {
                logBiz.spendGoods(player, store.getId(), 1, ItemType.StrengthStore.getType(), LogType.Strength.value(GSONUtils.ToJSONString(logVo)));
            }
        });
    }
}
