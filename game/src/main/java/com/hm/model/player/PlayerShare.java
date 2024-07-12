package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.ShareConfig;
import com.hm.config.excel.templaextra.ShareTemplate;

import java.util.Map;

/**
 * 用户分享数据
 */
public class PlayerShare extends PlayerDataContext {
    /**
     * @see ShareTemplate#getId()  --> timestamp
     */
    private Map<Integer, Long> lastTimeMap = Maps.newConcurrentMap();
    /**
     * @see ShareTemplate#getId()  --> times
     */
    private Map<Integer, Integer> recMap = Maps.newConcurrentMap();


    /**
     * @param id
     */
    public void addCd(int id) {
        lastTimeMap.put(id, System.currentTimeMillis());
    }

    /**
     * 校验cd，ture--> ok
     *
     * @param id
     * @return
     */
    public boolean checkCd(int id, int cdSecond) {
        if (cdSecond <= 0) {
            return true;
        }
        Long old = lastTimeMap.getOrDefault(id, 0L);
        return (old + cdSecond * GameConstants.SECOND) < System.currentTimeMillis();
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("playerShare", this);
    }

    public void dayReset() {
        ShareConfig shareConfig = SpringUtil.getBean(ShareConfig.class);
        boolean b = recMap.keySet().removeIf(
                id -> {
                    ShareTemplate t = shareConfig.getTemplate(id);
                    return t.isCanReset() || t.getShareType().canDel(Context());
                }
        );
        if (b) {
            SetChanged();
        }
    }

    public void addRec(int id) {
        recMap.merge(id, 1, Integer::sum);
    }

    public int getShareCnt(int id) {
        return recMap.getOrDefault(id, 0);
    }
}
