package com.hm.observer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.hm.model.player.Player;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 观察模式处理中心，负责注册观察者和被观察对象更新时，通知观察者 ClassName:ObserverRouter <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2017年9月19日 上午11:25:51 <br/>
 * 
 * @author zigm
 * @version 1.1
 * @since
 */
@Slf4j
public class ObserverRouter {
	private static final ObserverRouter instance = new ObserverRouter();

	public static ObserverRouter getInstance() {
		return instance;
	}

	private final ListMultimap<ObservableEnum, SortObserver> observers;// 观察者集合

	private ObserverRouter() {
		observers = ArrayListMultimap.create();
	}

	public void registObserver(ObservableEnum observableEnum, IObserver observer) {
		registObserver(observableEnum, observer, 100000);
	}
	
	/**
	 * 注册监听事件
	 * @param observableEnum 监听事件枚举
	 * @param observer 监听者
	 * @param sortValue 排序值 自定义优先级不要重复!
	 */
	public void registObserver(ObservableEnum observableEnum, IObserver observer, int sortValue) {
		if (observer == null || observableEnum == null) {
			return;
		}
		addObservable(observableEnum, observer, sortValue);
	}

	private void addObservable(ObservableEnum observableEnum, IObserver observer, int sortValue) {
		List<SortObserver> list = observers.get(observableEnum);
		SortObserver sortObserver = list.stream().filter(t -> t.getObserver() == observer).findAny().orElse(null);
		if (sortObserver == null) {
			//没有注册过该信号的该IObserver
			observers.put(observableEnum, new SortObserver(observer, sortValue));
			sortObserver(observableEnum);
			return;
		}
		if (sortValue >= sortObserver.getSort()) {//注册过且新注册的order比已有的大则不再注册
			return;
		}
		sortObserver.setSort(sortValue);
		sortObserver(observableEnum);
		return;
	}

	private void sortObserver(ObservableEnum observableEnum) {
		List<SortObserver> oList = observers.get(observableEnum);
		Collections.sort(oList, (SortObserver a,SortObserver b)->(a.getSort()-b.getSort()));
	}

	public static void notifyObservers(ObservableEnum observableEnum, Object... argv) {
		getInstance().notifyObservers(observableEnum, null, argv);
	}

	/**
	 * 通知所有观察者被观察对象发生改变 notifyObservers:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * 
	 * @author zigm
	 * @param observableEnum
	 * @param player
	 * @param argv
	 *            使用说明
	 *
	 */
	public void notifyObservers(ObservableEnum observableEnum, Player player, Object... argv) {
		if(player != null && player.playerTemp().isCloneLogin()) {
			return;
		}
		if (!observableEnum.chkArgv(argv)) {
			log.error("传递的观察者参数错误 " + observableEnum.getEnumId());
			return;
		}
		for (SortObserver observer : observers.get(observableEnum)) {
			try {
				observer.getObserver().invoke(observableEnum, player, argv);
			} catch (Exception e) {
				log.error("传递的观察者参数错误 " + observableEnum.getEnumId(), e);
			}
		}
	}
}
