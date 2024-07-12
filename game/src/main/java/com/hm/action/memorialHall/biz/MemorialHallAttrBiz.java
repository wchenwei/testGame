package com.hm.action.memorialHall.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.MemorialHallConfig;
import com.hm.config.excel.templaextra.*;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankAttrType;
import com.hm.model.player.MemorialHallChapter;
import com.hm.model.player.Player;
import com.hm.model.tank.TankAttr;
import com.hm.util.PubFunc;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Biz
public class MemorialHallAttrBiz{
	@Resource
    private MemorialHallConfig memorialHallConfig;
	@Resource
    private ItemBiz itemBiz;
	
	/**
	 * 计算纪念馆属性
	 * @param player
	 * @return
	 */
	public TankAttr calHallAttrMap(Player player) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.MemorialHall)){
			return null;
		}
		TankAttr resultAttr = new TankAttr();
		Map<Integer, MemorialHallChapter> chapterMap = player.playerMemorialHall().getHallChapterMap();
		for (Map.Entry<Integer, MemorialHallChapter> entry : chapterMap.entrySet()) {
			//循环所有纪念馆
			int[] photos = entry.getValue().getPhotos();
			for (int i = 0; i < photos.length; i++) {
				if(photos[i] > 0) {
					//每个纪念馆中的每面墙
					MemorialWallTemplate wallTemplate = memorialHallConfig.getMemorialWallTemplate(entry.getKey(), i+1);
					if(wallTemplate != null) {
						TankAttr tankAttr = wallTemplate.getTankAttr(photos[i]);
						if(tankAttr != null) {
							resultAttr.addAttr(tankAttr);
						}
					}
				}
			}
			//计算章节等级
			int chapterLv = entry.getValue().getLv();
			if(chapterLv > 0) {
				MemorialChapterTemplate chapterTemplate = memorialHallConfig.getMemorialChapterTemplate(entry.getKey());
				if(chapterTemplate != null) {
					resultAttr.addAttr(chapterTemplate.getTankAttr(chapterLv));
				}
			}
		}
		return resultAttr;
	}
	
	/**
	 * 计算羁绊加成
	 * @param player
	 * @param attrMap
	 * @return
	 */
	public Map<TankAttrType, Double> calPlayerFetterAttr(Player player,Map<TankAttrType, Double> attrMap) {
		int markLv = player.playerMemorialHall().getMarkLv();
		if(markLv > 0) {
			MemorialLvTemplate memorialLvTemplate = memorialHallConfig.getMemorialLvTemplate(markLv);
			if(memorialLvTemplate != null) {
				Map<TankAttrType, Double> resultMap = Maps.newHashMap(attrMap);
				for (Map.Entry<TankAttrType, Double> entry : resultMap.entrySet()) {
					resultMap.put(entry.getKey(), entry.getValue()*(1+memorialLvTemplate.getAttri()));
				}
				return resultMap;
			}
		}
		return attrMap;
	}
	
	/**
	 * 根据羁绊计算每个坦克额外增加属性
	 * @param player
	 * @param fettersSkills
	 * @return
	 */
	public Map<Integer,Map<Integer,Double>> calFetterBuffAttr(Player player,List<FettersImpl> fettersSkills) {
		Map<Integer,Map<Integer,Double>> tankAttMap = Maps.newHashMap();
		if(CollUtil.isEmpty(fettersSkills)) {
			return tankAttMap;
		}
		int markLv = player.playerMemorialHall().getMarkLv();
		if(markLv <= 0) {
			return tankAttMap;
		}
		MemorialLvTemplate memorialLvTemplate = memorialHallConfig.getMemorialLvTemplate(markLv);
		if(memorialLvTemplate != null) {
			int buffLv = PubFunc.parseInt(memorialLvTemplate.getCircle());
			for (FettersImpl fettersImpl : fettersSkills) {
				MemorialBuffTemplate buffTemplate = memorialHallConfig.getMemorialBuffTemplate(fettersImpl.getQuality(), buffLv);
				if(buffTemplate != null) {
					List<Integer> tankIds = fettersImpl.getTankList();
					if(tankIds.size() >= 2) {
						for (int tankId : tankIds) {
							tankAttMap.put(tankId, buffTemplate.getAttrMap());
						}
					}
				}
			}
		}
		return tankAttMap;
	}
}
