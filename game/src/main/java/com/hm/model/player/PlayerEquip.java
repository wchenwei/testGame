package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.config.EquipmentConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.templaextra.PlayerArmExtraTemplate;
import com.hm.enums.EquCircleType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;
/**
 * 装备系统
 * @author xjt
 *
 */
@Getter
public class PlayerEquip extends PlayerBagBase{
	//部位
	private Equipment[] equs = new Equipment[8];
	//光环
	private Map<Integer,Integer> circleMap = Maps.newHashMap();



	public void initEqu(){
		for(int i=0;i< GameConstants.EquipMaxPos;i++){
			Equipment equ = this.equs[i];
			if(equ==null){
				this.equs[i]=new Equipment(i+1);
			}
		}
		SetChanged();
	}
	public Equipment getEquipment(int id){
		Equipment equ = equs[id-1];
		if(equ==null){
			initEqu();
			equ = equs[id-1];
		}
		return equ;
	}
	

	/**
	 * 更换装备
	 * @param id 部位id
	 * @param equId 装备id
	 */
	public void changeEqu(int id,PlayerArmExtraTemplate template){
		Equipment equ = getEquipment(id);
		if(equ==null){
			return ;
		}
		equ.changeEui(template);
		SetChanged();
	}

	/**
	 * 更换石头
	 * @param id 部位
	 * @param stoneId 更换的石头id
	 * @param index 孔位
	 */
	public void changeStone(int id, int stoneId, int index) {
		Equipment equ = getEquipment(id);
		if(equ==null){
			return ;
		}
		equ.changeStone(stoneId,index);
		SetChanged();
	}

	//获取装备品质>=quality的个数
	public int getSuitNum(int quality){
		return (int)Stream.of(equs).filter(t->t.getEquId()>0).filter(t -> {
			return t.getQuality()>=quality;
		}).count();
	}

	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("playerEquip", this);
	}



	public void changeCircleLv(EquCircleType circleType,int newLv) {
		this.circleMap.put(circleType.getType(),newLv);
		SetChanged();
	}

	public int getCircleLv(EquCircleType circleType) {
		return this.circleMap.getOrDefault(circleType.getType(),0);
	}

	//获取装备品质
	public int[] getEquQuality() {
		int[] quas = new int[this.equs.length];
		for (int i = 0; i < quas.length; i++) {
			if(this.equs[i] != null) {
				quas[i] = this.equs[i].getQuality();
			}
		}
		return quas;
	}
}
