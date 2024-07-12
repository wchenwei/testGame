package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;
import com.hm.model.task.Random.BaseRandomTask;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:地图民情
 * User: yang xb
 * Date: 2019-04-12
 *
 * @author Administrator
 */
@Data
public class PlayerRandomTask extends PlayerDataContext {
    /**
     * 完成次数
     */
    private int count;
    private BaseRandomTask task;

    //每个城市的亲密度 key:城市id  value:城市亲密度
    private ConcurrentHashMap<Integer, Integer> cityMap = new ConcurrentHashMap<>();

    public void incCount() {
        count++;
        SetChanged();
    }

    /**
     * 增加城池亲密度
     *
     * @param cityId
     * @param v
     */
    public void addCityValue(int cityId, int v) {
        this.cityMap.put(cityId, getCityValue(cityId) + v);
        SetChanged();
    }

    /**
     * 获取城市亲密度
     *
     * @param cityId
     * @return
     */
    public int getCityValue(int cityId) {
        return this.cityMap.getOrDefault(cityId, 0);
    }

    /**
     * 重置次数
     */
    public void resetDay() {
        if (count != 0) {
            count = 0;
            SetChanged();
        }
    }

    public void setTask(BaseRandomTask task) {
        this.task = task;
        SetChanged();
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerRandomTask", this);
    }
}
