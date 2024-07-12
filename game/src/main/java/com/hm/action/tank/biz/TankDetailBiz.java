package com.hm.action.tank.biz;

import com.google.common.collect.Maps;
import com.hm.config.EquipmentConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankFettersConfig;
import com.hm.config.excel.templaextra.FettersImpl;
import com.hm.config.excel.templaextra.PlayerArmCircleExtraTemplate;
import com.hm.config.excel.templaextra.StarLevelTemplateImpl;
import com.hm.enums.EquCircleType;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.model.tank.Driver;
import com.hm.model.tank.Tank;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author chenwei
 * @Date 2024/6/24
 * @Description:
 */
@Biz
public class TankDetailBiz {
    @Resource
    private TankConfig tankConfig;
    @Resource
    private TankAttrBiz tankAttrBiz;
    @Resource
    private EquipmentConfig equipmentConfig;
    @Resource
    private TankFettersConfig tankFettersConfig;

    // 强化属性
    public Map<Integer, Double> calEquStrengthAttr(Player player){
        Map<Integer,Double> attrMap = Maps.newConcurrentMap();
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Equip)){
            return attrMap;
        }
        for(int i = 1;i<=player.playerEquip().getEqus().length;i++){
            Equipment equ = player.playerEquip().getEquipment(i);
            //装备本身的属性
            Map<TankAttrType,Double> equAttrmap = equipmentConfig.getEquAttr(equ.getEquId());
            equAttrmap.forEach((key,value)->attrMap.merge(key.getType(), value, (x,y)->(x+y)));
            //强化的属性
            Map<TankAttrType,Double> strengthenAttrMap = equipmentConfig.getStrengthenAttr(equ.getId(),equ.getStrengthenLv());
            strengthenAttrMap.forEach((key,value)->attrMap.merge(key.getType(), value, (x,y)->(x+y)));
        }
        //光环属性
        int lv = player.playerEquip().getCircleLv(EquCircleType.StrengthenCircle);
        if (lv > 0){
            PlayerArmCircleExtraTemplate circletemplate = equipmentConfig.getCircle(EquCircleType.StrengthenCircle, lv);
            circletemplate.getAttrMap().forEach((key,value)->attrMap.merge(key.getType(), value, (x,y)->(x+y)));
        }
        return attrMap;
    }

    // 宝石属性
    public Map<Integer, Double> calStoneAttr(Player player){
        Map<Integer,Double> attrMap = Maps.newConcurrentMap();
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Equip)){
            return attrMap;
        }
        for(int i = 1;i<=player.playerEquip().getEqus().length;i++){
            Equipment equ = player.playerEquip().getEquipment(i);
            //石头的属性
            for (int stoneId : equ.getStone()) {
                if(stoneId > 0) {
                    Map<TankAttrType,Double> stoneAttrMap = equipmentConfig.getStoneAttr(stoneId);
                    stoneAttrMap.forEach((key,value)->attrMap.merge(key.getType(), value, (x,y)->(x+y)));
                }
            }
        }
        //光环属性
        int lv = player.playerEquip().getCircleLv(EquCircleType.StoneCircle);
        if (lv > 0){
            PlayerArmCircleExtraTemplate circletemplate = equipmentConfig.getCircle(EquCircleType.StoneCircle, lv);
            circletemplate.getAttrMap().forEach((key,value)->attrMap.merge(key.getType(), value, (x,y)->(x+y)));
        }
        return attrMap;
    }


    public boolean isActiveTankFetters(Player player, int tankId){
        Tank tank = player.playerTank().getTank(tankId);
        if (tank == null){
            return false;
        }
        List<WorldTroop> worldTroops = TroopServerContainer.of(player).getWorldTroopByPlayer(player);
        WorldTroop worldTroop = worldTroops.stream().filter(e -> e.getTroopArmy().isContainTankId(tankId)).findFirst().orElse(null);
        if (worldTroop == null){
            return false;
        }
        TankSetting tankSetting = tankConfig.getTankSetting(tankId);

        FettersImpl fetters = tankFettersConfig.getFettersById(tankSetting.getFetters());
        if(fetters == null) {
            return false;
        }
        List<Integer> tankIdList = worldTroop.getTroopArmy().getTankIdList();
        return tankIdList.containsAll(fetters.getTankList());
    }

}
