package com.hm.observer;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hm.annotation.Broadcast;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
public abstract class BaseBroadcastAdapter implements IObserver {

    private MethodAccess access;
    private ListMultimap<ObservableEnum, SortMethodObserver> msgMethodMap = ArrayListMultimap.create();

    public MethodAccess getAccess() {
        return access;
    }

    public ListMultimap<ObservableEnum, SortMethodObserver> getMsgMethodMap() {
        return msgMethodMap;
    }

    public void setMsgMethodMap(ListMultimap<ObservableEnum, SortMethodObserver> msgMethodMap) {
        this.msgMethodMap = msgMethodMap;
    }

    public void registerMsg(ObservableEnum obs, IObserver observer, int index, int order) {
        ObserverRouter.getInstance().registObserver(obs, observer, order);
    }

    public void sortObserver(ObservableEnum observableEnum) {
        List<SortMethodObserver> oList = msgMethodMap.get(observableEnum);
        Collections.sort(oList, (SortMethodObserver a, SortMethodObserver b) -> (a.getSort() - b.getSort()));
    }

    @Override
    public void registObserverEnum() {
        initBroadcastAccess();
    }

    public abstract int getMethodIndex(MethodAccess access, String methodName);

    private void initBroadcastAccess() {
        this.access = MethodAccess.get(getClass());
        for (Method method : getClass().getDeclaredMethods()) {
            Broadcast broadcast = method.getAnnotation(Broadcast.class);
            if (broadcast != null) {
                ObservableEnum[] os = broadcast.value();
                for (int i = 0; i < broadcast.value().length; i++) {
                    int index = getMethodIndex(access, method.getName());
                    if (index < 0) {
                        continue;
                    }
                    ObservableEnum obs = os[i];
                    //没有填写的默认10000
                    int order = i > broadcast.order().length - 1 ? 10000 : broadcast.order()[i];
                    order = order < 0 ? 10000 : order;
                    msgMethodMap.put(obs, new SortMethodObserver(index, method.getName(), order));
                    registerMsg(obs, this, index, order);
                }
            }
        }
        //統一进行排序
        msgMethodMap.keySet().stream().forEach(t -> sortObserver(t));
    }

}
