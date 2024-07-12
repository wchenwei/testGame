package com.hm.model.serverpublic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.PlayerTitleType;
import com.hm.model.player.BasePlayer;

import java.util.List;
import java.util.Map;

/**
 * ClassName: TitleData. <br/>  
 * Function: 服务器的称号. <br/>  
 *  
 * @author zxj  
 * @version
 */
public class TitleData extends ServerPublicContext {
	private Map<Integer, Long> titleData = Maps.newHashMap();
	
	public void addTitleData(BasePlayer player, int titleId) {
		titleData.put(titleId, player.getId());
		SetChanged();
	}
	public void addTitleData(long playerId,PlayerTitleType titleType) {
		titleData.put(titleType.getType(), playerId);
		SetChanged();
	}
	
	public void removeTitle(PlayerTitleType titleType) {
		this.titleData.remove(titleType.getType());
		SetChanged();
	}
	
	/**
	 * 获取用户的所有称号
	 * @param id
	 * @return
	 */
	public List<Integer> getUserTitle(long id) {
		List<Integer> listTitle = Lists.newArrayList();
		this.titleData.keySet().forEach(key->{
			if(titleData.get(key)==id) {
				listTitle.add(key);
			}
		});
		return listTitle;
	}

	public long getTitlePlayer(int titleId) {
		return this.titleData.getOrDefault(titleId, 0L);
	}
	
	public Map<Integer, Long> getAllTitle() {
		return this.titleData;
	}
	
	public void doMergeServerForTitle() {
		this.titleData.remove(PlayerTitleType.PRESIDENT.getType());
		this.titleData.remove(PlayerTitleType.COMMANDER.getType());
		SetChanged();
	}
}




