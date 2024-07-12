package com.hm.action.levelEvent.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.battle.vo.TankSimple;
import com.hm.action.item.ItemBiz;
import com.hm.action.levelEvent.vo.LevelEventLimit;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.GameConstants;
import com.hm.config.LevelEventConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.temlate.MissionEventBuffTemplate;
import com.hm.config.excel.temlate.MissionLevelWarTemplate;
import com.hm.config.excel.templaextra.LevelEventBuffGroupTemplate;
import com.hm.config.excel.templaextra.LevelEventLinkTemplate;
import com.hm.config.excel.templaextra.LevelEventNewTemplate;
import com.hm.config.excel.templaextra.LevelEventRewardTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.EventBuffType;
import com.hm.enums.EventSupportType;
import com.hm.enums.MailConfigEnum;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import com.hm.model.item.Items;
import com.hm.model.player.LevelEvent;
import com.hm.model.player.LevelEventNew;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class LevelEventBiz implements IObserver{
	@Resource
	private LevelEventConfig levelEventConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private MailConfig mailConfig;
	//检查上传结果是否合法
	public boolean checkResult(Player player,int id,String result){
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
		if(template==null){
			return false;
		}
		int warId = template.getWar_id();
		//检查我方的出战的坦克是否有阵亡或不存在的
		List<String> myTanks = StringUtil.splitStr2StrList(result, ",");
		for(String tankStr: myTanks){
			int tankId = Integer.parseInt(tankStr.split(":")[0]);
			Tank tank = player.playerTank().getTank(tankId);
			if(tank==null){//玩家没有该tank
				return false;
			}
			TankSimple tankSimple = player.playerEvent().getLevelEvent(warId).getTank(tankId);
			if(tankSimple!=null&&tankSimple.isDeath()){
				return false;
			}
		}
		List<Integer> tankIds = myTanks.stream().map(t ->Integer.parseInt(t.split(":")[0])).collect(Collectors.toList());
		//检查出战坦克是否满足关卡限制条件
		for(LevelEventLimit limit:template.getLimits()){
			if(!limit.checkResult(player, tankIds)){
				return false;
			}
		}
		return true;
	}

	@Override
	public void registObserverEnum() {
		//TODO 等级事件本次版本不升级
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
                       Object... argv) {
		//TODO 等级事件本次版本不升级
		levelEventConfig.checkLevelEventOpen(player);

		//新的等级事件
		levelEventConfig.checkLevelEventNewOpen(player);
	}

	public void doResult(Player player,int warId, int id, int star, String result) {
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		levelEvent.updateMyTank(id,result);
		player.playerEvent().SetChanged();
	}

	//把部队派遣到这个点
	public void dispatch(Player player, LevelEventNew levelEvent, int id, boolean isAir) {
		int nowId = levelEvent.getNowId();
		int lineId = levelEventConfig.getLine(nowId,id);
		//占领这条线
		if (!isAir) {
			levelEvent.occupyLine(lineId);
		}
		//部队到达该点
		levelEvent.dispatch(id);
        //TODO 派遣的时候不再检查事件，只在首次占领据点时检查
        //checkPointEvent(player,levelEvent,id);
		player.playerEvent().SetChanged();
	}
	//检查事件点触发buff事件
	public void checkPointEvent(Player player,LevelEventNew levelEvent, int id) {
		//获取点的配置
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
        if (template == null || (template.getType() != 4)) {
			return;
		}
		//根据点的类型触发buff
        int buffGroupId = template.randomBuffGroupId();
		//给本事件添加buff
        if (buffGroupId > 0) {
            //根据buffGroupId取出buff
            LevelEventBuffGroupTemplate buffGroupTemplate = levelEventConfig.getBuffGroup(buffGroupId);
            if (buffGroupTemplate == null) {
                return;
            }
            int[] events = buffGroupTemplate.getEvents();
            levelEvent.createEvents(events);
			player.playerEvent().SetChanged();
		}
	}

	//支援
	public void support(Player player, int warId, int type, int id) {
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		EventSupportType supportType = EventSupportType.getType(type);
		switch(supportType) {
            case AirSupport:
                if (!isInWarId(id, warId)) {
                    return;
                }
                //部队直接空降到该点
                levelEvent.dispatch(id);
                checkPointEvent(player, levelEvent, id);
                break;
            case ArtillerySupport:
                if (!isInWarId(id, warId)) {
                    return;
                }
                //直接占据该点
                levelEvent.clearance(id);
                break;
            case EngineerSupport:
                //判断该线是否是本战役的线
                LevelEventLinkTemplate template = levelEventConfig.getLine(id);
                if (template == null || template.getWar_id() != warId) {
                    return;
                }
                //占领该线路(不增加步数)
                levelEvent.occupyLine(id);
                break;
		}
		levelEvent.addSupportCount();
		player.playerEvent().SetChanged();
	}

	public void doBuffEffect(Player player, LevelEventNew levelEvent,List<Integer> tankIds) {
//		int buffId = levelEvent.getBuffId();
//		if(buffId<=0) {
//			return;
//		}
//		MissionEventBuffTemplate template = levelEventConfig.getBuff(buffId);
//		if(template==null) {
//			return;
//		}
//		List<TankSimple> onTanks = levelEvent.getTanks().stream().filter(t->tankIds.contains(t)).collect(Collectors.toList());
//		EventBuffType buffType = EventBuffType.getType(buffId);
//		switch(buffType){
//		case Recovery:
//			double recoveryRate = Double.parseDouble(template.getPara());
//			onTanks.stream().filter(t ->t.getHp()>0).forEach(t ->{
//				double maxHp = player.playerTank().getTank(t.getId()).getMaxHp();
//				t.setHp((long)Math.min(maxHp, t.getHp()+maxHp*recoveryRate));
//			});
//			player.playerEvent().SetChanged();
//			break;
//		case ReduceHp:
//			double reduceRate = Double.parseDouble(template.getPara());
//			onTanks.stream().filter(t ->t.getHp()>0).forEach(t ->{
//				double maxHp = player.playerTank().getTank(t.getId()).getMaxHp();
//				t.setHp((long)Math.max(0, t.getHp()-maxHp*reduceRate));
//			});
//			player.playerEvent().SetChanged();
//			break;
//		case Revive:
//			int luckTankId = onTanks.stream().filter(t ->t.getHp()<=0).map(t ->t.getId()).findAny().orElse(0);
//			if(luckTankId>0) {
//				levelEvent.updateMyTank(new TankSimple(luckTankId));
//				levelEvent.clearBuff();
//				player.playerEvent().SetChanged();
//			}
//			break;
//		case DestroyTank:
//			//已死亡坦克
//			List<Integer> deathTankIds = onTanks.stream().filter(t ->t.getHp()<=0).map(t ->t.getId()).collect(Collectors.toList());
//			Tank luckTank = player.playerTank().getTankList().stream().filter(t ->!deathTankIds.contains(t.getId())).sorted(Comparator.comparing(Tank::getCombat).reversed()).limit(5).findAny().orElse(null);
//			if(luckTank!=null) {
//				//让这个坦克死亡
//				levelEvent.updateMyTank(new TankSimple(luckTank.getId(), 0, 0));
//			}
//			levelEvent.clearBuff();
//			player.playerEvent().SetChanged();
//			break;
//		}
	}
	//获取上传结果中的tankId
	public List<Integer> getTankIds(String result) {
		List<String> myTanks = StringUtil.splitStr2StrList(result, ",");
		return myTanks.stream().map(t ->Integer.parseInt(t.split(":")[0])).collect(Collectors.toList());
	}

	//根据Buff获取上阵坦克数量上线
	public int getOnTankNum(int buffId) {
		if(buffId<=0) {
			return GameConstants.OnTankDefaultNum;
		}
		EventBuffType type = EventBuffType.getType(buffId);
//		switch(type) {
//		case Expand:
//			return GameConstants.OnTankDefaultNum+1;
//		case UnExpand:
//			return GameConstants.OnTankDefaultNum-1;
//		}
		return GameConstants.OnTankDefaultNum;
	}

	public long getSupportCost(int count) {
		List<Integer> costs = StringUtil.splitStr2IntegerList(commValueConfig.getStrValue(CommonValueType.LevelEventSupporCost), ":");
		return costs.get(Math.min(count-1, costs.size()-1));
	}


    public void checkLevelEventCal(Player player) {
		if(!player.playerLevelEvent().isCal()) {
			//对老的事件进行结算
			List<Items> rewards = getReturnReward(player);
			if(!CollUtil.isEmpty(rewards)) {
				//发邮件
				MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.LevelEventReturn);
				mailBiz.sendSysMail(player, mailTemplate, rewards);
			}
			player.playerLevelEvent().setCal(true);
            player.playerLevelEvent().setCalNew(true);
		}
	}

	public List<Items> getReturnReward(Player player){
		List<LevelEvent> events = player.playerLevelEvent().getLevelEvents();
		List<Items> rewards = Lists.newArrayList();
		for(LevelEvent event:events) {
			rewards.addAll(getLevelEventReward(player,event));
		}
		return ItemBiz.createItemList(rewards);
	}

	public List<Items> getLevelEventReward(Player player,LevelEvent event) {
		int lv = player.playerLevel().getLv();
		List<Items> rewards = Lists.newArrayList();
        if (event.isEndOld()) {
            return rewards;
        }
        MissionLevelWarTemplate warTemplate = levelEventConfig.getLevelEventWar(event.getWarId());
        if (lv >= warTemplate.getLevel_need()) {
            //先把本关的剩余奖励计算
            List<Integer> allRewardIds = levelEventConfig.getRewardByWarId(event.getWarId());
            allRewardIds.removeAll(event.getReceivedIds());
            for (int id : allRewardIds) {
                System.out.println(player.getId() + "--------------" + event.getWarId() + "-------------" + id);
                LevelEventRewardTemplate template = levelEventConfig.getReward(id);
                if (template != null) {
                    rewards.addAll(template.getRewards());
                }
            }
        }
        //再把后续星级对应的关卡的奖励结算掉
        int nextWarId = warTemplate.getNext_id();
        MissionLevelWarTemplate nextWarTemplate = levelEventConfig.getLevelEventWar(nextWarId);
        int i = 0;
        while (nextWarId > 0 && lv >= nextWarTemplate.getLevel_need() && i < 9) {
            i++;
            System.out.println("获得本关的所有奖励--------------" + nextWarTemplate.getWar_id());
            //获得本关的所有奖励
            rewards.addAll(levelEventConfig.getAllRewards(nextWarId));
            nextWarId = nextWarTemplate.getNext_id();
            nextWarTemplate = levelEventConfig.getLevelEventWar(nextWarId);
            if (nextWarTemplate == null) {
                break;
            }
        }
        return rewards;
    }

    public List<Items> getLevelEventRewardNew(Player player, LevelEvent event) {
        int lv = player.playerLevel().getLv();
        List<Items> rewards = Lists.newArrayList();
        if (event.isEndOld()) {
            //如果本来就是已经完成的则不再结算
            return rewards;
        }
        if (event.isEnd()) {
            System.out.println(player.getId() + "--------------" + event.getWarId() + "结算错误，重新结算");
            MissionLevelWarTemplate warTemplate = levelEventConfig.getLevelEventWar(event.getWarId());
            if (lv >= warTemplate.getLevel_need()) {
                //先把本关的剩余奖励计算
                List<Integer> allRewardIds = levelEventConfig.getRewardByWarId(event.getWarId());
                allRewardIds.removeAll(event.getReceivedIds());
                for (int id : allRewardIds) {
                    LevelEventRewardTemplate template = levelEventConfig.getReward(id);
                    if (template != null) {
                        rewards.addAll(template.getRewards());
                    }
                }
            }
            //再把后续星级对应的关卡的奖励结算掉
            int nextWarId = warTemplate.getNext_id();
            MissionLevelWarTemplate nextWarTemplate = levelEventConfig.getLevelEventWar(nextWarId);
            int i = 0;
            while (nextWarId > 0 && lv >= nextWarTemplate.getLevel_need() && i < 9) {
                i++;
                //获得本关的所有奖励
                rewards.addAll(levelEventConfig.getAllRewards(nextWarId));
                nextWarId = nextWarTemplate.getNext_id();
                nextWarTemplate = levelEventConfig.getLevelEventWar(nextWarId);
                if (nextWarTemplate == null) {
                    break;
                }
            }
            System.out.println(player.getId() + "--------------" + event.getWarId() + "结算错误，重新结算" + GSONUtils.ToJSONString(rewards));
            return rewards;

        }
        return rewards;

    }

    public boolean isInWarId(int id, int warId) {
		//检查该点是否是本战役的点
		LevelEventNewTemplate template = levelEventConfig.getLevelEventNew(id);
		if(template==null||template.getWar_id()!=warId) {
			return false;
		}
		return true;
	}
	//检查支援的参数是否合法
	public boolean checkSuppor(Player player, int warId, int type, int id) {
		LevelEventNew levelEvent = player.playerEvent().getLevelEvent(warId);
		EventSupportType supportType = EventSupportType.getType(type);
		switch(supportType) {
            case AirSupport:
                if (!isInWarId(id, warId)) {
                    return false;
                }
                return levelEvent.getNowId() != id;
            case ArtillerySupport:
                if (!isInWarId(id, warId)) {
                    return false;
                }
                return !levelEvent.getIds().contains(id);
            case EngineerSupport:
                //判断该线是否是本战役的线
                LevelEventLinkTemplate template = levelEventConfig.getLine(id);
                if (template == null || template.getWar_id() != warId) {
                    return false;
                }
                return !levelEvent.getLines().contains(id);
		}
		return true;
	}

    public void choiceEvent(Player player, LevelEventNew levelEvent, int choice, int point) {
        int eventId = levelEvent.getEvents()[choice];
        if (eventId <= 0) {
            return;
        }
        MissionEventBuffTemplate template = levelEventConfig.getBuff(eventId);
        if (template == null) {
            return;
        }
        EventBuffType buffType = EventBuffType.getType(template.getType());
        List<TankSimple> onTanks = levelEvent.getTanks();
        switch (buffType) {
            case Recovery:
                int needNum = Integer.parseInt(template.getPara().split(",")[0]);
                double recoveryRate = Double.parseDouble(template.getPara().split(",")[1]);
                onTanks.stream().filter(t -> t.getHp() > 0).sorted(Comparator.comparing(TankSimple::getHp)).limit(needNum).forEach(t -> {
                    double maxHp = player.playerTank().getTank(t.getId()).getMaxHp();
                    t.setHp((long) Math.min(maxHp, t.getHp() + maxHp * recoveryRate));
                });
                player.playerEvent().SetChanged();
                break;
            case Airdrop:
                //空投
				LevelEventNewTemplate pointTemplate = levelEventConfig.getLevelEventNew(point);
				if (pointTemplate != null && pointTemplate.getWar_id() == levelEvent.getWarId()) {
					dispatch(player, levelEvent, point, true);
					player.playerEvent().SetChanged();
				}
                break;
            case Revive:
                int num = Integer.parseInt(template.getPara().split(",")[0]);
                double rate = Double.parseDouble(template.getPara().split(",")[1]);
                List<Integer> luckIds = onTanks.stream().filter(t -> t.getHp() == 0).map(t -> player.playerTank().getTank(t.getId())).sorted(Comparator.comparing(Tank::getCombat).reversed()).map(t -> t.getId()).limit(num).collect(Collectors.toList());
                luckIds.forEach(t -> {
                    long hp = (long) player.playerTank().getTank(t).getMaxHp();
                    levelEvent.updateMyTank(new TankSimple(t, hp, 0));
                });
                player.playerEvent().SetChanged();
                break;
            case Skill:
                levelEvent.addBuff(eventId);
				player.playerEvent().SetChanged();
                break;
        }
        levelEvent.choice(choice);

    }


    public void checkLevelEventCalNew(Player player) {
        //如果老的结算是错误的
        if (player.playerLevelEvent().isCal() && !player.playerLevelEvent().isCalNew()) {
            //如果已经结算过，则检查玩家是否有事件没有完全结算
            List<Items> rewards = Lists.newArrayList();
            for (LevelEvent event : player.playerLevelEvent().getLevelEvents()) {
                rewards.addAll(getLevelEventRewardNew(player, event));
            }
            if (!CollUtil.isEmpty(rewards)) {
                //发邮件
                MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.LevelEventReturnCheck);
                mailBiz.sendSysMail(player, mailTemplate, ItemBiz.createItemList(rewards));
            }
            player.playerLevelEvent().setCalNew(true);
        }
    }
}
