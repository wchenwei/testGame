
package com.hm.model.player;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.TitleConfig;
import com.hm.config.excel.templaextra.TitleTemplate;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.tank.TankAttr;
import com.hm.observer.ObservableEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTitle extends PlayerDataContext {
	//正在使用的角色id
	private int usingTitleId;
	//不包含系统称号
	private transient ConcurrentHashMap<Integer,Title> titleMap = new ConcurrentHashMap<Integer, Title>();
	//每个称号获取的次数
	@Getter
	private HashMap<Integer,Integer> countMap = Maps.newHashMap();

	public void doLoginForTitle() {
		checkTitle();
		if(CollUtil.isEmpty(countMap) && CollUtil.isNotEmpty(titleMap)) {
			for (Title title : titleMap.values()) {
				addTitleGetForCount(title.getTitleId());
			}
			// 全服称号获取次数
			ServerData serverData = ServerDataManager.getIntance().getServerData(this.Context().getServerId());
			List<Integer> listTitle = serverData.getTitleData().getUserTitle(this.Context().getId());
			listTitle.forEach(this::addTitleGetForCount);
		}
	}

	public void addTitleGetForCount(int titleId) {
		this.countMap.put(titleId,this.countMap.getOrDefault(titleId,0)+1);
		SetChanged();
	}

	public int getUsingTitleId() {
		return usingTitleId;
	}

	/**
	 * 是否拥有该称号
	 * @param id
	 * @return
	 */
	public boolean haveTitle(int id){
		checkTitle();
		if(this.titleMap.containsKey(id)) {
			return true;
		}
		//验证是否是全服称号
		ServerData serverData = ServerDataManager.getIntance().getServerData(this.Context().getServerId());
    	List<Integer> listTitle = serverData.getTitleData().getUserTitle(this.Context().getId());
		return listTitle.contains(id);
	}
	
	/**
	 *解锁称号 
	 */
	public void addPlayerTitle(int id){
		if(id<=0){
			return;
		}
		addPlayerTitle(new Title(id));
	}
	
	public void addPlayerTitle(Title title){
		titleMap.put(title.getTitleId(), title);
		addTitleGetForCount(title.getTitleId());//记录获得次数
		SetChanged();
	}
	/**
	 * 使用称号
	 */
	public void useTitle(int id){
		this.usingTitleId=id;
		SetChanged();
	}
	/**
	 *检查称号是否过期
	 */
	public void checkTitle() {
		//先不用检查,当前没有限时称号
		for (Title title : titleMap.values()) {
			if(!title.isValid()){
				this.titleMap.remove(title.getTitleId());
				if(title.getTitleId()==usingTitleId){//如果正在使用此称号则将正在使用的称号置为0
					this.usingTitleId=0;
					super.Context().notifyObservers(ObservableEnum.ChangeTitle);
				}
				SetChanged();
			}
		}
	}
	
	/**
	 * 删除称号
	 * @param titleId
	 * @return
	 */
	public boolean removeTitle(int titleId) {
		if(this.titleMap.remove(titleId) != null) {
			SetChanged();
		}
		if(titleId == this.usingTitleId) {
			this.usingTitleId = 0;
			SetChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerTitle", this);
	}
	/**
	 * 获取当前用户称号的属性加成
	 * @return
	 */
	public TankAttr getTankAttr() {
		if(usingTitleId>0) {
			TitleConfig titleConfig = SpringUtil.getBean(TitleConfig.class);
			TitleTemplate title =titleConfig.getTitleTemplate(usingTitleId);
			if(title != null) {
				return title.getTankAttr();
			}
		}
		return new TankAttr();
	}
	
	public Map<Integer,Title> getPlayerTitleMap() {
		checkTitle();
		return Maps.newHashMap(titleMap);
	}

    public Map<Integer, Title> getPlayerTitleMapNoCheck() {
        return Maps.newHashMap(titleMap);
    }

}
