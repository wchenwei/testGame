package com.hm.action.fish;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.FishConfig;
import com.hm.config.excel.templaextra.ActiveFishTemplateImpl;
import com.hm.enums.LogType;
import com.hm.model.fish.FishVO;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import javax.annotation.Resource;

/**
 * @introduce: 类注释
 * @author: wyp
 * @DATE: 2023/11/1
 **/
@Biz
public class FishBiz{
    @Resource
    private FishConfig fishConfig;
    @Resource
    private ItemBiz itemBiz;

    public void failFish(Player player){
        // 修改连胜次数
        player.playerFish().clearContinuousPerfect();
        // 清空双倍积分
        player.playerFish().setDoubleScore(0);
    }

    public FishVO successFish(Player player, JsonMsg msg, int randomFishId){
        ActiveFishTemplateImpl fishTemplate = fishConfig.getFishTemplateById(randomFishId);
        if(fishTemplate == null){
            return null;
        }
        FishVO fishVO = new FishVO(player, fishTemplate);
        // 连击
        boolean perfect = msg.getBoolean("perfect");
        player.playerFish().updateRecordAndPerfect(fishVO, perfect);
        // 添加经验
        this.addExp(player, fishTemplate);
        // 添加商店积分
        itemBiz.addItem(player, fishVO.getList(), LogType.Fish);
        player.notifyObservers(ObservableEnum.FishRecord, randomFishId);
        return fishVO;
    }

    public void addExp(Player player, ActiveFishTemplateImpl fishTemplate){
        int expTotal = player.playerFish().addExp(fishTemplate.getExp());
        int fishLevel = fishConfig.getGoFishLevel(expTotal);
        if(fishLevel <= 0){
            return;
        }
        player.playerFish().setLv(fishLevel);
    }
}
