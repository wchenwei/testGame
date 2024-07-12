package com.hm.action.memorialHall;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.memorialHall.biz.MemorialHallBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.excel.MemorialHallConfig;
import com.hm.config.excel.templaextra.MemorialChapterTemplate;
import com.hm.config.excel.templaextra.PhotoTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.MemorialHallChapter;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Action
public class MemorialHallAction extends AbstractPlayerAction{
	@Resource
    private MemorialHallBiz memorialHallBiz;
	@Resource
    private MemorialHallConfig memorialHallConfig;
	@Resource
    private PlayerBiz playerBiz;
	@Resource
	private ItemBiz itemBiz;
	
	@MsgMethod(MessageComm.C2S_ShowPhoto)
    public void showPhoto(Player player, JsonMsg msg) {
		int chapterId = msg.getInt("chapterId");
		boolean isSuc = memorialHallBiz.onekeyShowPhoto(player, chapterId);
		if(!isSuc) {
			return;
		}
		player.notifyObservers(ObservableEnum.MemorialHallChapterPhotoAdd, chapterId);
		player.sendUserUpdateMsg();
		
		player.sendMsg(MessageComm.S2C_ShowPhoto);
	}
	
	@MsgMethod(MessageComm.C2S_ChapterLvUp)
	public void chapterLvUp(Player player, JsonMsg msg) {
		int chapterId = msg.getInt("chapterId");
		MemorialHallChapter memorialHallChapter = player.playerMemorialHall().getMemorialHallChapter(chapterId);
		if(memorialHallChapter == null) {
			return;
		}
		MemorialChapterTemplate chapterTemplate = memorialHallConfig.getMemorialChapterTemplate(chapterId);
		if(chapterTemplate == null) {
			return;
		}
		int curLv = memorialHallChapter.getLv();
		int maxLv = chapterTemplate.getMaxLv(memorialHallChapter.getTotalPohoto());
		if(curLv >= maxLv) {
			return;
		}
		memorialHallChapter.setLv(curLv+1);
		int newLv = memorialHallChapter.getLv();
		
		player.playerMemorialHall().addMarkVal(chapterTemplate.getMarkvalue(newLv));
		//计算最新等级
		player.playerMemorialHall().setMarkLv(memorialHallConfig.calNewLv(player.playerMemorialHall().getMarkVal()));
		
		player.notifyObservers(ObservableEnum.MemorialHallChapterLv, chapterId);
		player.notifyObservers(ObservableEnum.CHMemorialChapterLv, chapterId, curLv, newLv, memorialHallChapter.getTotalPohoto());

		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_ChapterLvUp);
		serverMsg.addProperty("chapterId", chapterId);
		serverMsg.addProperty("newLv", newLv);
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod(MessageComm.C2S_RecoveryPhoto)
	public void recoveryPhoto(Player player, JsonMsg msg) {
		String photosIds = msg.getString("photosIds");//照片id
		List<Items> itemList = StringUtil.splitStr2IntegerList(photosIds, ",").stream().map(t -> new Items(t,1,ItemType.TankPhoto)).collect(Collectors.toList());
		if(!itemBiz.checkItemEnoughAndSpend(player, itemList, LogType.PhotoRecovery)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		List<Items> rewardList = Lists.newArrayList();
		for (Items items : itemList) {
			PhotoTemplate template = memorialHallConfig.getPhotoTemplate(items.getId());
			if(template != null) {
				rewardList.addAll(template.getItemList());
			}
		}
		rewardList = itemBiz.createItemList(rewardList);
		itemBiz.addItem(player, rewardList, LogType.PhotoRecovery);
		
		player.sendUserUpdateMsg();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_RecoveryPhoto);
		serverMsg.addProperty("rewardList", rewardList);
		player.sendMsg(serverMsg);
	}
	
}
