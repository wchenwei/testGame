package com.hm.observer;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 普通信号处理类(类似于之前实现IObserver)的处理类,方法请添加注解@Broadcast,注解参数参考注解
 *
 * @author xjt
 * @Date 2020年10月13日14:20:31
 */
@Slf4j
public abstract class NormalBroadcastAdapter extends BaseBroadcastAdapter {

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        //处理信号
        List<SortMethodObserver> sortBroadcastObserver = this.getMsgMethodMap().get(observableEnum);
        for (SortMethodObserver o : sortBroadcastObserver) {
            try {
                this.getAccess().invoke(this, o.getIndex(), observableEnum, player, argv);
            } catch (Exception e) {
                log.error((player == null ? 0 : player.getId()) + "处理观察者:" + observableEnum.getEnumId() + ",方法:" + o.getMethodName() + "出错！", e);
            }
        }
    }

    @Override
    public int getMethodIndex(MethodAccess access, String methodName) {
        try {
            return access.getIndex(methodName, ObservableEnum.class, Player.class, Object[].class);
        } catch (Exception e) {
            log.error("注冊信号出错,参数错误！class:" + this.getClass().getSimpleName() + ",methodName:" + methodName, e);
            return -1;
        }
    }


}
