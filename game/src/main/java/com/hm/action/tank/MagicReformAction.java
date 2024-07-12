package com.hm.action.tank;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.MagicReformBiz;
import com.hm.action.tank.biz.TankSoldierBiz;
import com.hm.config.MagicReformConfig;
import com.hm.config.PlayerFunctionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.FunctionUnlockTemplate;
import com.hm.enums.*;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankMagicReform;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.setting.TankSetting;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class MagicReformAction extends AbstractPlayerAction {
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MagicReformBiz magicReformBiz;
	@Resource
	private MagicReformConfig magicReformConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private PlayerFunctionConfig playerFunctionConfig;
	@Resource
	private TankSoldierBiz tankSoldierBiz;
	/**
	 * 魔改
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_Tank_MagicReform)
	public void lvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(tankSetting.getRare()<TankRareType.SSR.getType()){
			return;
		}
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		FunctionUnlockTemplate template = playerFunctionConfig.getFunctionTemplate(PlayerFunctionType.MagicReform.getType());
		if(template==null||tank.getLv()<template.getLevel()){
			player.sendErrorMsg(SysConstant.TANK_LV_LIMIT);
			return;
		}

		int reformLv = tank.getTankMagicReform().getReformLv();
		if(tankSetting.getMagic_reform_bigskill()<=0){
		    //没有开放魔改
		    return;
        }
		if(tankSetting.getXianding_type()>0){
			template = playerFunctionConfig.getFunctionTemplate(PlayerFunctionType.XianDingMagic.getType());
			if(template==null || tank.getLv() < template.getLevel()){
				player.sendErrorMsg(SysConstant.TANK_LV_LIMIT);
				return;
			}
		}
		//一阶魔改等级上限
		int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
		int superNeedLv =commValueConfig.getCommValue(CommonValueType.SuperMagicReformOpenLv);//二阶魔改要求的坦克等级
		if(reformLv>=reformLvLimit&&(tankSetting.getSuper_reform_bigskill()<=0||tank.getLv()<superNeedLv)){
			return;
		}

        if(tankSetting.getSuper_reform_bigskill()<=0&&reformLv>=reformLvLimit){
            //没有开放二阶魔改
            return;
        }
        int superReformLimit = commValueConfig.getCommValue(CommonValueType.SuperMagicReformLimit);
        if(reformLv>=superReformLimit){
            return;
        }
        List<Items> cost = magicReformBiz.getMagicReformCost(tank);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.TankMagicReform.value(tankId))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		int skill = magicReformBiz.reform(player, tank);
		int oldSkillLv = tank.getTankMagicReform().getSkillLv(skill);
		player.playerTank().magicReform(tankId,skill);
		int newSkillLv = tank.getTankMagicReform().getSkillLv(skill);
		player.notifyObservers(ObservableEnum.MagicReform, tankId, reformLv, skill, oldSkillLv, newSkillLv, cost);
		player.sendUserUpdateMsg();
		JsonMsg returnMsg = JsonMsg.create(MessageComm.S2C_Tank_MagicReform);
		returnMsg.addProperty("tankId", tankId);
		returnMsg.addProperty("skill", skill);
		player.sendMsg(returnMsg);
	}
	
	/**
	 * 魔改转移
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_Tank_MagicReform_Transfer)
	public void transfer(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		int desTankId = msg.getInt("desTankId");//目标坦克id
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		TankSetting tankSetting2 = tankConfig.getTankSetting(desTankId);
		if(tankSetting.getRare()<TankRareType.SSR.getType()||tankSetting2.getRare()<TankRareType.SSR.getType()||tankSetting.getRare()!=tankSetting.getRare()){
			return;
		}
        if(tankSetting.getMagic_reform_bigskill()<=0||tankSetting2.getMagic_reform_bigskill()<=0){
            //没有开放魔改
            return;
        }
        //限定和非限定之间不能转移
        if(!((tankSetting.getXianding_type()==0&&tankSetting2.getXianding_type()==0)||(tankSetting.getXianding_type()>0&&tankSetting2.getXianding_type()>0))){
            return;
        }
		Tank tank = player.playerTank().getTank(tankId);
		Tank desTank = player.playerTank().getTank(desTankId);
		if(tank == null||desTank==null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		if(tank.getTankMagicReform().getReformLv()<=0&&desTank.getTankMagicReform().getReformLv()<=0) {
            return;
        }
		if(!magicReformBiz.isCantransfer(Lists.newArrayList(tank,desTank))){
		    return;
        }

		FunctionUnlockTemplate template = playerFunctionConfig.getFunctionTemplate(PlayerFunctionType.MagicReform.getType());
		if(tank.getLv()<template.getLevel()||desTank.getLv()<template.getLevel()){
			player.sendErrorMsg(SysConstant.TANK_LV_LIMIT);
			return;
		}
		List<Items> cost = commValueConfig.getListItem(CommonValueType.MagicReformTransferCost);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.TankMagicReformTransfer)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		magicReformBiz.transfer(player,tankId,desTankId);
		player.notifyObservers(ObservableEnum.MagicReformTransfer,Lists.newArrayList(tankId,desTankId), cost);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Tank_MagicReform_Transfer);
	}


	@MsgMethod ( MessageComm.C2S_Tank_MagicReform_Reset)
	public void reset(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		Tank tank = player.playerTank().getTank(tankId);
		int magicLv = tank.getLv(TankSoldierHalosType.MagicCircle);
		if(magicLv <= 0){
			// 没有魔改过
			return;
		}
		List<Items> costItem = commValueConfig.getListItem(CommonValueType.MagicReformResetCost);
		if(!itemBiz.checkItemEnoughAndSpend(player, costItem, LogType.TankMagicReformReset.value(tankId))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		List<Items> list = magicReformBiz.resetMagicReward(tank);
		itemBiz.addItem(player, list, LogType.TankMagicReformReset.value(tankId));
		tank.setTankMagicReform(new TankMagicReform());
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.MagicReformRest,Lists.newArrayList(tankId));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Tank_MagicReform_Reset,list);
	}
}
