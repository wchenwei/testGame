package com.hm.action.task.observer;

import com.google.common.collect.Lists;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.Objects;

/**
 * 基础任务处理类
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/11 16:53
 */
public abstract class BaseTaskObserver implements IObserver {

    @Override
    public void registObserverEnum() {
        getObserver().stream()
                .filter(Objects::nonNull)
                .forEach(observableEnum->{registObserver(observableEnum);});
    }

    /**
     * @description
     *          获取需要注册的 信号集合
     * @return java.util.List<java.lang.Integer>
     * @date 2021/9/7 13:49
     */
    public List<ObservableEnum> getObserver(){
        return Lists.newArrayList();
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player sender, Object... argv) {
        doPlayerTask(sender, observableEnum, argv);
    }

    public abstract void doPlayerTask(Player player, ObservableEnum observableEnum, Object... argv);
}
