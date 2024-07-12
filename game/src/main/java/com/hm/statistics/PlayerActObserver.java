package com.hm.statistics;

import com.hm.action.cmq.CmqMsg;
import com.hm.libcore.annotation.Biz;
import com.hm.action.cmq.CmqBiz;
import com.hm.config.GameConstants;
import com.hm.enums.BattleType;
import com.hm.message.CmqMessageComm;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
/**
 * @ClassName: PlayerActObserver 
 * @Description: 统计用户行为的数据
 * @author zxj
 * @date 2020年1月8日 上午10:01:28 
 *
 */
@Biz
public class PlayerActObserver implements IObserver {

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddHonor, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GetSysGold, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AnswerQuestion, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.BattleFight, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankDraw, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankDrawFre, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Task_Reward, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankTechResearch, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Treasury_Collection, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TroopRevive, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MainFbFail, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PvpOneByOneLaunch, this);
		
		
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum){
			case AddHonor:
				long addHonor =(long)argv[1];
				this.sendStatistics(player, StatisticsType.Honer, addHonor);
				break;
			case GetSysGold:
				long addGold =(long)argv[0];
				this.sendStatistics(player, StatisticsType.SysGold, addGold);
				break;
			case Recharge:
				long recharge =(long)argv[1];
				this.sendStatistics(player, StatisticsType.Recharge, recharge);
				break;
			case AnswerQuestion:
				this.sendStatistics(player, StatisticsType.AnswerQuestion, 1);
				break;
			case BattleFight:
				int type = (int) argv[0];
				if(type == BattleType.ExperimentBattle.getType()) {
					this.sendStatistics(player, StatisticsType.ExperimentBattle, 1);
				}
			case TankDraw:
				this.sendStatistics(player, StatisticsType.TankDraw, 1);
				break;
			case TankDrawFre:
				this.sendStatistics(player, StatisticsType.TankDrawFre, 1);
				break;
			case Task_Reward:
				this.sendStatistics(player, StatisticsType.Task_Reward, 1);
				break;
			case TankTechResearch:
				int techTimes =(int)argv[1];
				this.sendStatistics(player, StatisticsType.TankTechResearch, techTimes);

				break;	
			case Treasury_Collection:
				this.sendStatistics(player, StatisticsType.Task_Reward, 1);
				break;
			case TroopRevive:
				long goldCost =(long)argv[1];
				if(goldCost<=0) {
					this.sendStatistics(player, StatisticsType.TroopRevive, 1);
				}else {
					this.sendStatistics(player, StatisticsType.TroopReviveGold, 1);
				}
				break;
			case MainFbFail:
				this.sendStatistics(player, StatisticsType.MainFbFail, 1);
				break;
			case PvpOneByOneLaunch:
				this.sendStatistics(player, StatisticsType.PvpOneByOneLaunch, 1);
				break;
			default:
				break;
		}
	}
	//用于统计阵营积分
	public void sendStatistics(int serverId, StatisticsType type, long num) {
		try {
			CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Statistics);
			cmqMsg.addProperty("playerId", -1);
			cmqMsg.addProperty("type", type.getType());
			cmqMsg.addProperty("num", num);
			cmqMsg.addProperty("serverid", serverId);
			cmqMsg.addProperty("channel", -1);
			cmqMsg.addProperty("createdate", System.currentTimeMillis()-GameConstants.DAY);
			CmqBiz.sendDefaultMessage(serverId, cmqMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendStatistics(Player player,StatisticsType type, long num) {
		CmqMsg cmqMsg = new CmqMsg(CmqMessageComm.S2S_Statistics);
		cmqMsg.addProperty("playerId", player.getId());
		cmqMsg.addProperty("type", type.getType());
		cmqMsg.addProperty("num", num);
		cmqMsg.addProperty("serverid", player.getServerId());
		cmqMsg.addProperty("channel", player.getChannelId());
		cmqMsg.addProperty("createdate", System.currentTimeMillis());
		CmqBiz.sendDefaultMessage(player, cmqMsg);
	}
}











