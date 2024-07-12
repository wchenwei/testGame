package com.hm.action.tank.biz;

import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.StarUnlockTemplateImpl;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.war.sg.setting.TankSetting;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * tank图纸研发
 */
@Service
public class PaperResearchBiz {

    @Resource
    private TankConfig tankConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private CommValueConfig commValueConfig;

    // 研发次数是否达到最大值
    public boolean isResearchMax(Player player, Tank tank){
        StarUnlockTemplateImpl tankStarTemplate = tankConfig.getTankStarTemplate(tank.getStar());
        int maxTimes = tankStarTemplate.getTame_num();
        int count = player.playerPaperResearch().getTankResearchCount(tank.getId());
        return count >= maxTimes;
    }

    //坦克研发
    public Items tankResearch(Player player, int tankId,int count){
        TankSetting tankSetting = tankConfig.getTankSetting(tankId);
        Items reward = new Items(tankSetting.getPaper_id(), count, ItemType.PAPER);
        itemBiz.addItem(player, reward, LogType.TankResearch.value(tankId));
        player.notifyObservers(ObservableEnum.TankResearch, reward.getId(), tankId);
        return reward;
    }

}
