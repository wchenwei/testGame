package com.hm.action.titile;

import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.TitleConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.templaextra.TitleTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerTitleType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.Title;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Biz
public class TitleBiz implements IObserver{
	@Resource
    private TitleConfig titleConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	
	@Override
	public void registObserverEnum() {
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {

	}

	public void addHead(Player player) {
		List<Items> listItem = commValueConfig.getListItem(CommonValueType.PRESIDENT_HEAD_ICON);
		itemBiz.addItem(player, listItem, LogType.UnlockIcon.value("PRESIDENT"));
	}
	
	/**
	 * 替换系统称号
	 * @param serverId
	 * @param titleType
	 * @param newPlayerId
	 */
	public void replacePlayerTitle(int serverId,PlayerTitleType titleType,long newPlayerId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		long tarPlayerId = serverData.getTitleData().getTitlePlayer(titleType.getType());
		Player tarPlayer = null;
		//当前拥有该称号的用户
		if(tarPlayerId>0 && null!=(tarPlayer = PlayerUtils.getPlayer(tarPlayerId))) {
			if(tarPlayer.getServerId() == serverId) {
				//判断用户是否正在使用此称号
				boolean result = tarPlayer.playerTitle().removeTitle(titleType.getType());
				if(result ) {
					tarPlayer.notifyObservers(ObservableEnum.ChangeTitle);
					tarPlayer.sendUserUpdateMsg();
				}
			}
		}
		serverData.getTitleData().addTitleData(newPlayerId, titleType);
		serverData.save();
	}
	
	/**
	 * addTitle:(把称号添加给用户，同时把拥有称号的用户给去除称号). <br/>  
	 * @author zxj  
	 * @param player
	 * @param titleType  使用说明
	 *
	 */
	public void addTitle(Player player,PlayerTitleType titleType) {
		if(null==titleType || player == null) {
			return;
		}
		TitleTemplate titleTemplate = titleConfig.getTitleTemplate(titleType.getType());
		if(titleTemplate == null) {
			return;
		}
		if(titleTemplate.isSystemOnly()) {
			//如果是系统唯一称号,替换玩家称号
			replacePlayerTitle(player.getServerId(), titleType,player.getId());
			//判断用户是否佩戴称号，没佩戴的话，给用户佩戴上
			autoUsingTitle(player, titleType);
			//添加唯一称号获取次数
			player.playerTitle().addTitleGetForCount(titleType.getType());
		}else{
			//可以重复的称号，添加到玩家
			player.playerTitle().addPlayerTitle(titleType.getType());
			//判断用户是否佩戴称号，没佩戴的话，给用户佩戴上
			if(autoUsingTitle(player, titleType)) {
				return;
			}
		}
		player.sendUserUpdateMsg();
	}
	public void addTitle(Player player,Title title) {
		player.playerTitle().addPlayerTitle(title);
		//判断用户是否佩戴称号，没佩戴的话，给用户佩戴上
		if(autoUsingTitle(player, PlayerTitleType.getType(title.getTitleId()))) {
			return;
		}
		player.sendUserUpdateMsg();
	}
	
	/**
	 * 自动使用称号
	 * @param player
	 * @return
	 */
	public boolean autoUsingTitle(Player player,PlayerTitleType titleType) {
		int usingTitle = player.playerTitle().getUsingTitleId();
		if(usingTitle<=0) {
	    	player.playerTitle().useTitle(titleType.getType());
	    	player.notifyObservers(ObservableEnum.ChangeTitle);
	    	player.sendUserUpdateMsg();
	    	return true;
		}
		return false;
	}
	
	/**
	 * 给用户发送称号列表
	 * @param player
	 */
	public void sendTitleList(Player player) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		Map<Integer,Title> titleMap = player.playerTitle().getPlayerTitleMap();
		for (int titleId : serverData.getTitleData().getUserTitle(player.getId())) {
			titleMap.put(titleId, new Title(titleId,-1));
		}
    	player.sendMsg(MessageComm.S2C_Get_Title, titleMap);
	}
}
