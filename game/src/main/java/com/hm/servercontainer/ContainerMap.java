package com.hm.servercontainer;

import com.google.common.collect.Maps;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.util.function.GofReturnFunction1;

import java.util.Collection;
import java.util.Map;

/**
 * 容器map
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/12/17 16:27
 */
public class ContainerMap<T extends ItemContainer> {
    private GofReturnFunction1<T, Integer> func;
    private Map<Integer, T> dataMap = Maps.newConcurrentMap();

    public ContainerMap(GofReturnFunction1<T, Integer> func) {
        this.func = func;
    }

    public void putAndNewServerContainer(int serverId) {
        T container = func.apply(serverId);
        container.initContainer();
        putItem(serverId, container);
    }

    public T getItemContainer(int serverId) {
        T t = dataMap.get(serverId);
        if (t == null) {
            System.out.println(serverId + " 不存在ItemContainer");
        }
        return t;
    }

    public T getItemContainer(DBEntity entity) {
        return getItemContainer(entity.getServerId());
    }

    public void putItem(int serverId, T t) {
        this.dataMap.put(serverId, t);
    }

    public void removeItem(int serverId) {
        this.dataMap.remove(serverId);
    }

    public Collection<T> getAllContainer() {
        return this.dataMap.values();
    }

    public void clear() {
        this.dataMap.clear();
    }
}
