package com.hm.action.tank;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.tank.biz.TankJingzhuBiz;
import com.hm.config.excel.TankJingzhuConfig;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.StringUtil;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
/**
 * 坦克精铸处理
 * @ClassName:  TankJingzhuAction   
 * @Description:
 * @author: zxj
 * @date:   2020年7月8日 下午4:54:41
 */
@Action
public class TankJingzhuAction extends AbstractPlayerAction{
	
	@Resource
	private TankBiz tankBiz; 
	@Resource
	private TankJingzhuBiz tankJingzhuBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private TankJingzhuConfig tankJingzhuConfig;
	
	/**
	 * 随机精铸属性
	 * @Title: randomAttr   
	 * @Description: 
	 * @param player
	 * @param msg      
	 * @return: void      
	 * @throws
	 * /msg:340343,tankId=51,position=1,type=1
	 */
	@MsgMethod ( MessageComm.C2S_TankJingzhu_Random)
	public void randomAttr(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		int position = msg.getInt("position"); // 选择的位置，从1开始
		//精铸类型，0普通精铸，1高级精铸；（现在没有普通精铸，只有高级精铸）
		int type = msg.getInt("type");
		Tank tank = player.playerTank().getTank(tankId);
		int check = this.commonCheck(tank, player);
		if(check>0) {
			player.sendErrorMsg(check);
			return;
		}
		//消耗资源
		Items cost = tankJingzhuBiz.getRandomCost(tank, position, type);
		if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.TankJingZhuRandomCost)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}

		String oldWashIds = Ints.join(",", tank.getTankJingzhu().getWahsBean(position).getWashIdsArr());

		boolean addResult = tankJingzhuBiz.addWash(position, type, tank);
		if(!addResult) {
			player.sendErrorMsg(SysConstant.OPEAR_FAIL);
			return;
		}
		String newWashIds = Ints.join(",", tank.getTankJingzhu().getWahsBean(position).getWashIdsArr());
		String lockStr = Joiner.on(",").join(tank.getTankJingzhu().getWahsBean(position).getLockWashIds());

		player.notifyObservers(ObservableEnum.TankJingzhuAttrChange, tank.getId());
		player.notifyObservers(ObservableEnum.CHTankJingzhu, tankId, position, oldWashIds, newWashIds, lockStr,
				cost.getCount(), itemBiz.getItemTotal(player, cost.getItemType(), cost.getId()));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankJingzhu_Random,true);
	}
	
	/**
	 * 锁定，或者解锁精铸属性
	 * @Title: lockUnlock   
	 * @Description: 
	 * @param player
	 * @param msg      
	 * @return: void      
	 * @throws
	 * /msg:340347,tankId=51,position=1,type=0,index=2
	 */
	@MsgMethod ( MessageComm.C2S_TankJingzhu_LockUnlock)
	public void lockUnlock(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		int position = msg.getInt("position"); // 选择的位置，从1开始
		int index = msg.getInt("index"); // 位置，从0开始
		//操作类型，0锁定，1解锁
		int type = msg.getInt("type");
		if(index>2 || index<0) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		
		Tank tank = player.playerTank().getTank(tankId);
		int check = this.commonCheck(tank, player);
		if(check>0) {
			player.sendErrorMsg(check);
			return;
		}
		//锁定或解锁
		boolean result = tankJingzhuBiz.lockOrUnlock(tank, type, position, index);
		if(!result) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		player.playerTank().SetChanged();
		player.sendMsg(MessageComm.S2C_TankUpdate, Lists.newArrayList(tank));
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankJingzhu_LockUnlock,true);
	}
	
	/**
	 * 精铸星级升级
	 * @Title: lvUp   
	 * @Description: 
	 * @param player
	 * @param msg      
	 * @return: void      
	 * @throws
	 * /msg:340345,tankId=51,position=1,partsIds=4101
	 */
	@MsgMethod ( MessageComm.C2S_TankJingzhu_Lvup)
	public void lvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		int position = msg.getInt("position"); // 选择的位置，从1开始
		String partsIds = msg.getString("partsIds"); // 坦克配件id，多个逗号分隔(,)
		Tank tank = player.playerTank().getTank(tankId);
		int check = this.commonCheck(tank, player);
		if(check>0) {
			player.sendErrorMsg(check);
			return;
		}
		//判断满级
		if(tankJingzhuBiz.isMaxLv(tank, position)) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		// 记录消耗前数量
		List<Long> s = StringUtil.splitStr2IntegerList(partsIds, ",").stream().mapToLong(e -> player.playerTankpiece().getItemCount(e)).boxed().collect(Collectors.toList());
		String cntStr = Joiner.on(",").join(s);
		//判断是否有配件，配件经验
		long exp = tankJingzhuBiz.getPartsIdsAndSpeedExp(player, partsIds);
		if(exp<=0) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		int beforeLv = tankJingzhuBiz.getStartLv(tank, position);
		tank.getTankJingzhu().addExp(exp, position, tankJingzhuConfig.getMaxExp());
		int endLv = tankJingzhuBiz.getStartLv(tank, position);
		//有等级变化，就有战力变化
		if(endLv>beforeLv) {
			player.notifyObservers(ObservableEnum.TankJingzhuLvup, tank.getId());
		}else {
			player.playerTank().SetChanged();
			List<Tank> tankList = Lists.newArrayList(tank);
			player.sendMsg(MessageComm.S2C_TankUpdate, tankList);
		}
		player.notifyObservers(ObservableEnum.CHTankJingzhuLvup, tankId, position, beforeLv, endLv,
				partsIds, cntStr, tank.getTankJingzhu().getWahsBean(position).getExp());
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankJingzhu_Lvup,true);
	}
	
	//校验数据
	private int commonCheck(Tank tank, Player player) {
		if(null==tank) {
			return SysConstant.PARAM_ERROR;
		}
		//判断品质，5或者5以上的就可以随机
		int quality = tankBiz.getTankReformQuality(player, tank);
		if(quality<5) {
			return SysConstant.Function_Lock;
		}
		return -1;
	}
	
}






