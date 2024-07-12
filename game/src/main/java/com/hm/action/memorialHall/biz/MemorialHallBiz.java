package com.hm.action.memorialHall.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.MemorialHallConfig;
import com.hm.config.excel.templaextra.MemorialChapterTemplate;
import com.hm.config.excel.templaextra.MemorialWallTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.player.MemorialHallChapter;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

@Biz
public class MemorialHallBiz implements IObserver{
	@Resource
    private MemorialHallConfig memorialHallConfig;
	@Resource
    private ItemBiz itemBiz;
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		if((int)argv[0]==PlayerFunctionType.MemorialHall.getType()){
			unlockMemorialHall(player);
		}
	}
	
	public void unlockMemorialHall(Player player) {
		//解锁第一章
		player.playerMemorialHall().unlockChapters(1);
		player.playerMemorialHall().setMarkLv(1);
	}
	
	/**
	 * 一键展出
	 * @param player
	 * @param chapterId
	 */
	public boolean onekeyShowPhoto(Player player,int chapterId) {
		MemorialHallChapter memorialHallChapter = player.playerMemorialHall().getMemorialHallChapter(chapterId);
		if(memorialHallChapter == null) {
			return false;
		}
		MemorialChapterTemplate chapterTemplate = memorialHallConfig.getMemorialChapterTemplate(chapterId);
		if(chapterTemplate == null) {
			return false;
		}
		boolean isChange = false;
		for (MemorialWallTemplate template : chapterTemplate.getWallList()) {
			//检查此章节的每个照片墙的照片数量
			if(doAutoShowPhotoForMemorialWallTemplate(player, template, memorialHallChapter)) {
				isChange = true;
			}
		}
		if(!isChange) {
			return false;
		}
		player.playerMemorialHall().SetChanged();
		doPlayerNextChapter(player);
		return true;
	}
	
	/**
	 * 处理单个照片墙的升级
	 * @param player
	 * @param template
	 * @param hallChapter
	 * @return
	 */
	public boolean doAutoShowPhotoForMemorialWallTemplate(Player player,MemorialWallTemplate template,MemorialHallChapter hallChapter) {
		//我的照片数量
		long myPhotoNum = player.playerMemorialHall().getPhotoNum(template.getTank_photo_id());
		if(myPhotoNum <= 0) {
			return false;
		}
		//可以贴几张照片
		long addNum = Math.min(template.getCost() - hallChapter.getPohotoNum(template.getWallIndex()), myPhotoNum);
		if(addNum <= 0) {
			return false;
		}
		//贴照片
		if(itemBiz.checkItemEnoughAndSpend(player, template.getTank_photo_id(), addNum, ItemType.TankPhoto, LogType.PhotoWallLv.value(template.getChapter()+""))) {
			hallChapter.addWall(template.getWallIndex(),addNum);
			player.notifyObservers(ObservableEnum.CHShowPhoto, template.getTank_photo_id(), addNum);
			return true;
		}
		return false;
	}
	
	/**
	 * 检查是否开启下一个章节
	 * @param player
	 */
	public void doPlayerNextChapter(Player player) {
		MemorialChapterTemplate[] chapters = memorialHallConfig.getChapters();
		for (int i = 1; i < chapters.length; i++) {
			MemorialChapterTemplate nextChapter = chapters[i];
			//处理是否开启
			MemorialHallChapter memorialHallChapter = player.playerMemorialHall().getMemorialHallChapter(nextChapter.getId());
			if(memorialHallChapter == null) {
				MemorialHallChapter preHallChapter = player.playerMemorialHall().getMemorialHallChapter(nextChapter.getId()-1);
				if(preHallChapter != null && preHallChapter.getTotalPohoto() >= nextChapter.getUnlock()) {
					player.playerMemorialHall().unlockChapters(nextChapter.getId());
				}
				return;
			}
		}
	}
	
}
