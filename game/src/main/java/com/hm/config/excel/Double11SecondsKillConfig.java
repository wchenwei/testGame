package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveShoppingGiftTemplateImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 *          双十一限时秒杀
 * @date 2020/10/14 19:53
 */
@Config
public class Double11SecondsKillConfig extends ExcleConfig{

    /**
     * 计费点:天数:object
     */
    private Table<Integer,Integer, List<ActiveShoppingGiftTemplateImpl>> dataTable = HashBasedTable.create();

    private List<ActiveShoppingGiftTemplateImpl> list = Lists.newArrayList();

    @Override
    public void loadConfig() {
//        loadActiveShoppingGiftTemplate();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveShoppingGiftTemplateImpl.class);
    }

    private void loadActiveShoppingGiftTemplate(){
        List<ActiveShoppingGiftTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveShoppingGiftTemplateImpl.class), new TypeReference<List<ActiveShoppingGiftTemplateImpl>>() {});
        Table<Integer,Integer, List<ActiveShoppingGiftTemplateImpl>> data = HashBasedTable.create();
        Table<Integer,Integer, List<Integer>> idsTable = HashBasedTable.create();
        list.forEach(e -> {
            e.init();
            Integer recharge_gift = e.getRecharge_gift();
            Integer days = e.getDays();
            List<ActiveShoppingGiftTemplateImpl> entryList = data.contains(recharge_gift, days) ? data.get(recharge_gift, days) : Lists.newArrayList();
            entryList.add(e);
            data.put(recharge_gift, days, entryList);
        });
        this.dataTable = ImmutableTable.copyOf(data);
        this.list = ImmutableList.copyOf(list);
    }

    public ActiveShoppingGiftTemplateImpl getActiveShoppingGiftTemplate(int rechargeId, int days, int serverLv, int type){
        ActiveShoppingGiftTemplateImpl activeShoppingGiftTemplate = dataTable.get(rechargeId, days).stream().filter(e -> e.isFit(days, serverLv, type)).findFirst().orElse(null);
        return activeShoppingGiftTemplate;
    }

    public ActiveShoppingGiftTemplateImpl getById(int id, int days, int serverLv, int type){
       return this.list.stream().filter(e->e.isFit(days, serverLv, type) && e.getId()==id).findFirst().orElse(null);
    }

    public boolean containRechargeId(int rechargeId) {
        Set<Integer> integers = dataTable.rowKeySet();
        return integers.contains(rechargeId);
    }

    public List<ActiveShoppingGiftTemplateImpl> getListByDays(int days,int type){
        return this.list.stream().filter(e->e.isFitDays(days,type)).collect(Collectors.toList());
    }
}
