package com.hm.action.tank.biz;

import cn.hutool.core.collection.CollectionUtil;
import com.hm.enums.CommonValueType;
import com.hm.libcore.annotation.Biz;
import com.hm.config.PartsConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankJingzhuConfig;
import com.hm.config.excel.templaextra.PartsExtraTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.model.tank.TankJingzhuWashBean;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.List;
@Slf4j
@Biz
public class TankJingzhuBiz {
	@Resource
	private TankJingzhuConfig tankJingzhuConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private PartsConfig partsConfig;
	//最大的，属性格子的数量
	private static final int SIZE =3;
	private static final int MAXLOCKSIZE =3;
	

	/**
	 * 获取随机的消耗信息
	 * @Title: getRandomCost   
	 * @Description: 
	 * @param tank
	 * @param position
	 * @param type
	 * @return      
	 * @return: Items      
	 * @throws
	 */
	public Items getRandomCost(Tank tank, int position, int type) {
		int lockSize = tank.getTankJingzhu().getLockSize(position);
		List<Items> itemsList = commValueConfig.getConvertObj(CommonValueType.TankJingzhuCost);
		Items items = itemsList.get(lockSize);
		return items;
	}

	/**
	 * 随机属性，并添加到坦克
	 * @Title: addWash   
	 * @Description: 
	 * @param position
	 * @param type
	 * @param tank
	 * @return      
	 * @return: boolean      
	 * @throws
	 */
	public boolean addWash(int position, int type, Tank tank) {
		TankJingzhuWashBean washBean = tank.getTankJingzhu().getWahsBean(position);
		int[] washIds = washBean.getWashIdsArr();
		List<Integer> lockWashIds = washBean.getLockWashIds();
		for(int index=0; index<washIds.length; index++) {
			int tempId = washIds[index];
			if(lockWashIds.contains(index)) {
				continue;
			}
			int resultId = tankJingzhuConfig.getRandomWashId(position, type, tempId, commValueConfig);
			washIds[index]=resultId;
		}
		tank.getTankJingzhu().addWash(position, washBean);
		return true;
	}
	/**
	 * 锁定或者解锁属性格子
	 * @Title: lockOrUnlock   
	 * @Description: 
	 * @param tank
	 * @param type， 0:锁定，1:解锁
	 * @param position，位置
	 * @param index，锁定的index
	 * @return      
	 * @return: boolean      
	 * @throws
	 */
	public boolean lockOrUnlock(Tank tank, int type, int position, int index) {
		if(0==type) {
			return tank.getTankJingzhu().lock(position, index, MAXLOCKSIZE);
		}else {
			return tank.getTankJingzhu().unlock(position, index, MAXLOCKSIZE);
		}
	}

	/**
	 * 获取当前位置的等级
	 * @Title: getStartLv   
	 * @Description: 
	 * @param tank
	 * @param position
	 * @return      
	 * @return: int      
	 * @throws
	 */
	public int getStartLv(Tank tank, int position) {
		long exp = tank.getTankJingzhu().getWahsBean(position).getExp();
		return tankJingzhuConfig.getStartLv(exp);
	}

	/**
	 * 获取消耗配件的经验
	 * @Title: getPartsIdsAndSpeedExp   
	 * @Description: 
	 * @param player
	 * @param partsIds
	 * @return      
	 * @return: long      
	 * @throws
	 */
	public long getPartsIdsAndSpeedExp(Player player, String partsIds) {
		List<Integer> listParts = StringUtil.splitStr2IntegerList(partsIds, ",");
		if(CollectionUtil.isEmpty(listParts)) {
			return 0;
		}
		long exp = listParts.stream().mapToLong(d->{
			long tempCount = player.playerTankpiece().getItemCount(d);
			PartsExtraTemplate tempPart =partsConfig.getParts(d);
			player.playerTankpiece().spendItem(d, tempCount);
			if(tempCount>0 && tempPart!=null) {
				return tempCount*tempPart.getParts_exp();
			}
			return 0l;
		}).sum();
		return exp;
	}
	
	public float getAttrAddTimes(TankJingzhuWashBean washBean) {
		int lv = tankJingzhuConfig.getStartLv(washBean.getExp());
		return tankJingzhuConfig.getJingzhuStart(lv);
	}
	
	//获取玩家精铸属性加成
	public TankAttr getJingzhuAllAttr(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr();
		
		List<TankJingzhuWashBean> listWash = tank.getTankJingzhu().getWash();
		listWash.forEach(d->{
			int[] washIds = d.getWashIdsArr();
			float addTimes = getAttrAddTimes(d);
			tankAttr.addAttr(tankJingzhuConfig.getTankAttr(washIds, addTimes));
		});
		return tankAttr;
	}

	public boolean isMaxLv(Tank tank, int position) {
		int lv = getStartLv(tank, position);
		return tankJingzhuConfig.getMaxLv()==lv;
	}

}






