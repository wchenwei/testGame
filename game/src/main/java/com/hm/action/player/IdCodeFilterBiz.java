package com.hm.action.player;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.action.http.biz.HttpBiz;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.json.JSONUtil;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.IdCodeFilterIpWhiteContainer;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.idcode.IdCodeInfo;
import com.hm.servercontainer.idcode.IdCodeItemContainer;

import javax.annotation.Resource;
import java.util.ArrayList;

@Biz
public class IdCodeFilterBiz implements IObserver{
	@Resource
	private HttpBiz httpBiz;
	@Resource
	private IdCodeFilterIpWhiteContainer idCodeFilterIpWhiteContainer;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		loadIpWhites();
	}
	
	public void loadIpWhites(){
		if(DateUtil.thisHour(true) > 0) {
			String ips = httpBiz.getIpWhite();
			if(StrUtil.isBlank(ips)){
				return ;
			}
			idCodeFilterIpWhiteContainer.loadWhiteIps(JSONUtil.fromJson(ips, new TypeReference<ArrayList<String>>(){}));
		}
	}
	
	
	public void changeIdCode(Player player,String idCode){
		IdCodeItemContainer  container =  IdCodeContainer.of(player);
		//更改内存中的
		IdCodeInfo idCodeInfo = container.getIdCodeInfo(player.getIdCode(), player.getCreateServerId());
		//从原有的删除
		if(idCodeInfo!=null){
			idCodeInfo.delId(player.getId());
			//保存进数据库
			idCodeInfo.saveDB();
		}
		player.setIdCode(idCode);
		//添加新的
		container.bindIdCode(player.getIdCode(), player.getCreateServerId(), player.getId());
		//把新的唯一码信息发向统计服进行更新
		player.notifyObservers(ObservableEnum.BindIdCode, player.getIdCode());
		player.sendUserUpdateMsg();
	}

}
