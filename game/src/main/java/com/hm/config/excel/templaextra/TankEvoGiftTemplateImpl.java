package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankEvolutionaryGiftTemplate;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName TankEvoGiftTemplateImpl
 * @Deacription 坦克超进化礼包
 * @Author zxj
 * @Date 2021/9/14 9:23
 * @Version 1.0
 **/
@FileConfig("tank_evolutionary_gift")
public class TankEvoGiftTemplateImpl extends TankEvolutionaryGiftTemplate {
    private List<Integer> rechargeId = Lists.newArrayList();
    private Map<Integer, Integer> rechargeBuyTimes = Maps.newHashMap();
    public void init(){
        String[] tempDataArr = this.getRecharge_gift_id().split(",");
        for(String tempFor : tempDataArr) {
            if(StringUtils.isEmpty(tempFor)) {
                continue;
            }
            String[] tempArr = tempFor.split(":");
            rechargeId.add(Integer.parseInt(tempArr[0]));
            rechargeBuyTimes.put(Integer.parseInt(tempArr[0]), Integer.parseInt(tempArr[1]));
        }
    }

    public boolean containsRechargeIds(int rechargeid) {
        return rechargeId.contains(rechargeid);
    }

    public int getTimes(int rechargeid) {
        return rechargeBuyTimes.getOrDefault(rechargeid, 0);
    }
}
