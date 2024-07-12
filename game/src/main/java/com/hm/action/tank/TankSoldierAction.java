package com.hm.action.tank;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.MagicReformBiz;
import com.hm.action.tank.biz.TankSoldierBiz;
import com.hm.config.MagicReformConfig;
import com.hm.config.PlayerFunctionConfig;
import com.hm.config.TankSoldierConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.FunctionUnlockTemplate;
import com.hm.config.excel.templaextra.TankSoldierLevelTemplate;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankRareType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankSoldier;
import com.hm.observer.ObservableEnum;
import com.hm.redis.troop.TroopRedisUtils;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.setting.TankSetting;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
/**
 * @Date 2020年7月7日09:42:28
 * @author xjt
 *
 */
@Action
public class TankSoldierAction extends AbstractPlayerAction {
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
	@Resource
	private TankSoldierConfig tankSoldierConfig;
	
	/**
	 * 绑定奇兵
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_Tank_Soldier_Bind)
	public void bind(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		int bindId = msg.getInt("bindId");//绑定坦克id
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		TankSetting bindSetting = tankConfig.getTankSetting(bindId);
		if(tankSetting==null||bindSetting==null||tankSetting.getRare()!=TankRareType.SSSR.getType()||bindSetting.getRare()!=TankRareType.SSR.getType()){
			return;
		}
		Tank tank = player.playerTank().getTank(tankId);
		Tank bindTank = player.playerTank().getTank(bindId);
		if(tank == null||bindTank==null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		FunctionUnlockTemplate template = playerFunctionConfig.getFunctionTemplate(PlayerFunctionType.TankSoldier.getType());
		if(template==null||tank.getLv()<template.getLevel()){
			player.sendErrorMsg(SysConstant.TANK_LV_LIMIT);
			return;
		}
		if(!CollUtil.isEmpty(TroopRedisUtils.checkPlayerTank(player.getId(), tankId))){
			//该双S坦克出现在某个上阵编队中，请下阵再进行奇兵绑定
			return;
		};
		int subTankId = tank.getTankSoldier()==null?0:tank.getTankSoldier().getSubTankId();
		if(subTankId>0) {
			tankSoldierBiz.unBind(player, tankId);
			player.notifyObservers(ObservableEnum.SoldierUnBind, tankId,subTankId);
		}
		//绑定奇兵
		tankSoldierBiz.bind(player, tankId, bindId);
		player.notifyObservers(ObservableEnum.SoldierBind, tankId,bindId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Tank_Soldier_Bind, tankId);
	}
	
	/**
	 * 打开绑定界面
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_Tank_Soldier_OpenChoice)
	public void openChoice(Player player, JsonMsg msg) {
		List<Integer> tankIds = player.playerTank().getTankByRare(TankRareType.SSR.getType());
		player.sendMsg(MessageComm.S2C_Tank_Soldier_OpenChoice,TroopRedisUtils.checkPlayerTank(player.getId(), tankIds));
	}
	
	/**
	 * 奇兵解绑
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_Tank_Soldier_UnBind)
	public void unBind(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(tankSetting.getRare()!=TankRareType.SSSR.getType()){
			return;
		}
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		TankSoldier tankSoldier = tank.getTankSoldier();
		if(tankSoldier==null) {
			return;
		}
		int subTankId = tank.getTankSoldier().getSubTankId();
		tankSoldierBiz.unBind(player, tankId);
		player.notifyObservers(ObservableEnum.SoldierUnBind, tankId,subTankId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Tank_Soldier_Bind, tankId);
	}
	
	/**
	 * 奇兵升级
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_Tank_Soldier_LvUp)
	public void LvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(tankSetting.getRare()!=TankRareType.SSSR.getType()){
			return;
		}
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		if(tank.getTankSoldier().getSubTankId()<=0){
			//没有绑定奇兵
			return;
		}
		int maxLv = tankSoldierConfig.getMaxLv();
		if(tank.getTankSoldier().getLv()>=maxLv){
			//已到达最大等级
			return;
		}
		TankSoldierLevelTemplate template = tankSoldierConfig.getLvTemplate(tank.getTankSoldier().getLv());
		if(template==null){
			return;
		}
		List<Items> costs = template.getCosts();
		if(!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.TankSoldierLvUp)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		tank.getTankSoldier().lvUp();
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.SoldierLvUp, tankId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Tank_Soldier_LvUp, tankId);
		
	}
}
