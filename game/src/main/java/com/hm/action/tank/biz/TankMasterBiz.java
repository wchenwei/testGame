package com.hm.action.tank.biz;

import com.google.common.collect.Maps;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.TankMasterTemplateImpl;
import com.hm.enums.TankAttrType;
import com.hm.model.player.Player;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class TankMasterBiz {
    @Resource
    private TankConfig tankConfig;

    public Map<TankAttrType, Double> getPlayerTankMasterAttr(Player player){
        int totalStar = player.playerTankMaster().getTotalStar();
        TankMasterTemplateImpl template = tankConfig.getTankMasterTemplate(totalStar);
        if (template != null){
            return template.getAttAttrMap();
        }
        return Maps.newHashMap();
    }

}
