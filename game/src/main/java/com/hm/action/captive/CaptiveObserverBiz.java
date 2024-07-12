package com.hm.action.captive;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.CaptiveConfig;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;

/**
 * @Description: tank俘虏
 * @author siyunlong  
 * @date 2020年7月1日 下午5:38:12 
 * @version V1.0
 */
@Slf4j
@Biz
public class CaptiveObserverBiz implements IObserver{

	@Resource
	private CaptiveConfig captiveConfig;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
	}
	
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		int functionId = Integer.parseInt(argv[0].toString());
		if(functionId==PlayerFunctionType.TankCaptive.getType()){
			player.playerCaptive().setLv(1);
			int researcher = captiveConfig.getDefResearcher();
			player.playerCaptive().changeResearcher(researcher);
			player.playerCaptive().addResearcher(researcher,-1);
		}
	}
}
