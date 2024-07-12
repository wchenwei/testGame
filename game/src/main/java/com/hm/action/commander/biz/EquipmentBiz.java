package com.hm.action.commander.biz;

import com.hm.action.item.ItemBiz;
import com.hm.config.EquipmentConfig;
import com.hm.config.excel.templaextra.PlayerArmCircleExtraTemplate;
import com.hm.config.excel.templaextra.PlayerArmStoneTemplate;
import com.hm.enums.EquCircleType;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.item.Items;
import com.hm.model.player.Equipment;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.Arrays;


@Biz
public class EquipmentBiz implements IObserver{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private EquipmentConfig equipmentConfig;


	//更换石头
	public void changeStone(Player player, int stoneId,boolean backOld) {
		PlayerArmStoneTemplate template = equipmentConfig.getStone(stoneId);
		Equipment equ = player.playerEquip().getEquipment(template.getArm_pos());
		//找出id最小的index
		int index = equ.findMineStoneIndex();
		int oldStoneId = equ.getStone()[index];//部位原先的石头id
		equ.changeStone(stoneId,index);

		player.playerEquip().SetChanged();
		if(backOld && oldStoneId>0){
			itemBiz.addItem(player, new Items(oldStoneId, 1, ItemType.STONE),LogType.ChangeStone);
		}
	}
	//更换石头
	public void changeStone(Player player, int id, int stoneId, int index) {
		Equipment equ = player.playerEquip().getEquipment(id);
		int oldStoneId = equ.getStone()[index];//部位原先的石头id
		player.playerEquip().changeStone(id,stoneId,index);
		if(oldStoneId>0){
			itemBiz.addItem(player, new Items(oldStoneId, 1, ItemType.STONE),LogType.ChangeStone);
		}
	}


	//装备提供的回血速度加成	
	public double getEquipHpCircle(Player player){
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mounts)){
			return 0;
		}
		double rate = 0;
		for(EquCircleType type:EquCircleType.values()){
			int lv = player.playerEquip().getCircleLv(type);
			if(lv>0){
				PlayerArmCircleExtraTemplate template = equipmentConfig.getCircle(type, lv);
				rate += template==null?0:template.getHp_recover();
			}
		}
		return rate;
	}
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
		
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		int type = Integer.parseInt(argv[0].toString());
		if(type==PlayerFunctionType.Equip.getType()){
			player.playerEquip().initEqu();
		}
	}

	//计算最新的大师光环等级
	public boolean calEquipCircle(Player player, EquCircleType type) {
		int circleLv = equipmentConfig.getCircleLv(player,type);
		if(circleLv == player.playerEquip().getCircleLv(type)) {
			return false;
		}
		player.playerEquip().changeCircleLv(type,circleLv);
		return true;
	}
	

	public boolean allStoneIsMaxLv(Player player) {
		for (Equipment equs : player.playerEquip().getEqus()) {
			for (int stoneId : equs.getStone()) {
				if(stoneId <= 0) {
					return false;
				}
				PlayerArmStoneTemplate template = equipmentConfig.getStone(stoneId);
				if(template == null || template.getNext_id() > 0) {
					return false;
				}
			}
		}
		return true;
	}

	// 获取各个部位石头的平均等级
	public double [] getStoneAvgLvs(Player player){
		return Arrays.stream(player.playerEquip().getEqus()).mapToDouble(e ->  Arrays.stream(e.getStone()).map(id ->{
			PlayerArmStoneTemplate template = equipmentConfig.getStone(id);
			return template==null?0:template.getLevel();
		}).average().getAsDouble()).toArray();
	}

	// 获得各个部队的强化等级
	public double [] getEquipStrengthLvs(Player player){
		return Arrays.stream(player.playerEquip().getEqus()).mapToDouble(e -> e.getStrengthenLv()).toArray();
	}
}
