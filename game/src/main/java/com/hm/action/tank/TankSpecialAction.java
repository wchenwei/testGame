package com.hm.action.tank;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankSpecialBiz;
import com.hm.config.PlayerFunctionConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankSpecialityConfig;
import com.hm.config.excel.temlate.FunctionUnlockTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.setting.TankSetting;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName: TankSpecialAction. <br/>  
 * Function: 坦克专精. <br/>  
 * date: 2019年7月11日 上午10:16:07 <br/>  
 * @author zxj  
 * @version
 */
@Action
public class TankSpecialAction extends AbstractPlayerAction {
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private TankSpecialityConfig tankSpecConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private TankSpecialBiz tankSpecialBiz;
	
	/**
	 * lvUp:(专精升级). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod ( MessageComm.C2S_SpecialLvup)
	public void lvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("id");
		int upType = msg.getInt("type"); //0 单次  1 一键专精
		Tank tank = player.playerTank().getTank(tankId);
		int check=this.check(player, tank);
		if(check>0) {
			player.sendErrorMsg(check);
			return;
		}
		if (tankSpecialBiz.lvUpdate(player, tank, upType)){
			int lv = tank.getTankSpecial().getLv();
			int type = tank.getTankSpecial().getType();
			player.notifyObservers(ObservableEnum.TankSpecialLvUp, tank.getId(), type, lv);
			player.playerTank().SetChanged();
			player.sendUserUpdateMsg();
			player.sendMsg(MessageComm.S2C_SpecialLvup);
		}
	}
	
	/**
	 * change:(改变专精). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod ( MessageComm.C2S_SpecialChange)
	public void change(Player player, JsonMsg msg) {
		int tankId = msg.getInt("id");
		int SpecialType = msg.getInt("type");
		
		
		Tank tank = player.playerTank().getTank(tankId);
		int check=this.check(player, tank);
		if(check>0) {
			player.sendErrorMsg(check);
			return;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		
		if(!tankSetting.containsSpecial(SpecialType) || tank.getTankSpecial().getType()==SpecialType) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		//判断师傅是同一天，同一天，就要扣资源
		if(tank.getTankSpecial().getTimes()>0) {
			List<Items> listItems = commValueConfig.getListItem(CommonValueType.TankMasterChange);
			if(!itemBiz.checkItemEnoughAndSpend(player, listItems, LogType.TankSpecChangeCost)) {
				player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
				return;
			}
		}
		//切换专精
		tank.getTankSpecial().setType(SpecialType);
		player.notifyObservers(ObservableEnum.TankSpecialChange, tank.getId());
		player.playerTank().SetChanged();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_SpecialChange);
	}
	/**
	 * check:(校验参数合法性). <br/>  
	 * @author zxj  
	 * @param player
	 * @param tank
	 * @return  使用说明
	 */
	private int check(Player player, Tank tank) {
		if(tank == null) {
			return SysConstant.TANK_NOT_EXIST;
		}
		PlayerFunctionConfig playerFunctionConfig = SpringUtil.getBean(PlayerFunctionConfig.class);
		FunctionUnlockTemplate template = playerFunctionConfig.getFunctionTemplate(PlayerFunctionType.TankSpecial.getType());
		if(null==template || player.playerLevel().getLv()<template.getLevel()) {
			return SysConstant.PARAM_ERROR;
		}
		return 0;
	}
}




