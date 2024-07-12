package com.hm.action.overallWar.biz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.overallWar.vo.OverallWarOpponentVo;
import com.hm.action.serverData.ServerDataBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.action.troop.client.ClientTroop;
import com.hm.action.troop.client.ClientTroopGroup;
import com.hm.config.OverallWarConfig;
import com.hm.config.excel.PlayerLevelConfig;
import com.hm.config.excel.templaextra.OverallWarRewardTemplate;
import com.hm.config.excel.templaextra.OverallWarTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.db.WarResultUtils;
import com.hm.enums.RankType;
import com.hm.enums.ServerFunctionType;
import com.hm.enums.WarResultType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardBiz;
import com.hm.model.fight.FightProxy;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.war.BattleRecord;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.server.GameServerManager;
import com.hm.util.MathUtils;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.troop.NpcTroop;
import com.hm.war.sg.troop.PlayerTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
@Slf4j
@Biz
public class OverallWarBiz implements IObserver{
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private OverallWarConfig overallWarConfig;
	@Resource
	private PlayerLevelConfig playerLevelConfig;
	@Resource
	private ServerDataBiz serverDataBiz;
	@Resource
	private  LeaderboardBiz leaderboardBiz;
	
	//全面战争开启星期
	public final static List<Integer> OpenWeeks = Lists.newArrayList(2,4,6);

	//检查上阵坦克状态
	public boolean checkTroopState(Player player,int type,List<TankArmy> tankArmys){
		ConcurrentHashMap<Integer, List<TankArmy>> map = player.playerOverallWar().getMyTanks();
		//除该队以外的所有队的坦克
		List<TankArmy> allTanks = Lists.newArrayList();
		for(Map.Entry<Integer, List<TankArmy>> entry:map.entrySet()){
			if(entry.getKey()!=type){
				allTanks.addAll(entry.getValue());
			}
		}
		for (TankArmy tankArmy : tankArmys) {
			//当前部队都不包含此坦克
			Tank tank = player.playerTank().getTank(tankArmy.getId());
			if(tank==null){
				//玩家没有该坦克
				return false;
			}
			//当前部队都不包含此坦克
			if(allTanks.stream().anyMatch(e -> e.getId()==tankArmy.getId())) {
				return false;
			}
			
		}
		return true;
	}
	/**
	 * @param player
	 * @param troops  0:101;0:102#1,1:103;0:201#1,1:202,2:203#1;0:301,1:302,2:303,3:404#1;
	 */
	public boolean checkTroopStateAll(Player player, JsonMsg msg) {
		//所有的上阵坦克
		List<Integer> tanks = Lists.newArrayList();
		String[] troopStrs = msg.getString("troops").split(";");
		//必须上满5支部队
		if(troopStrs.length!=5){
			return false;
		}
		ClientTroopGroup clientTroopGroup = ClientTroopGroup.buildFull(player, msg);
		
		List<ClientTroop> troops = clientTroopGroup.getTroopList();
		for(int i=1;i<=troops.size();i++) {
			List<TankArmy> tankArmys = troops.get(i-1).getArmyList();
			if(!checkTankArmy(player,i, tankArmys,tanks)){
				return false;
			}
		}
		return true;
	}
	
	public void createToops(Player player,JsonMsg msg){
		Map<Integer,ClientTroop> map = Maps.newConcurrentMap();
		ClientTroopGroup clientTroopGroup = ClientTroopGroup.buildFull(player, msg);
		List<ClientTroop> troops = clientTroopGroup.getTroopList();
		for(int i=1;i<=troops.size();i++) {
			map.put(i, troops.get(i-1));
		}
		player.playerOverallWar().changeTroops(map);
	}
	/**
	 * 判断单支部队是否合法
	 * @param player
	 * @param type
	 * @param tankArmys
	 * @return
	 */
	private boolean checkTankArmy(Player player,int type,List<TankArmy> tankArmys,List<Integer> tankIds){
		//判断该类型的坦克数量
		if(tankArmys.size()!=type){
			return false;
		}
		List<Integer> pos =Lists.newArrayList();
		for(TankArmy tankArmy:tankArmys){
			Tank tank = player.playerTank().getTank(tankArmy.getId());
			//玩家没有该坦克或是奇兵不能上阵
			if(tank==null||tank.getMainTank()>0){
				return false;
			}
			//站位不合法
			if(tankArmy.getIndex()>8||tankArmy.getIndex()<0){
				return false;
			}
			//坦克重复上阵或位置重复
			if(tankIds.contains(tankArmy.getId())||pos.contains(tankArmy.getIndex())){
				return false;
			}
			tankIds.add(tankArmy.getId());
			pos.add(tankArmy.getIndex());
		}
		return true;
	}
	/**
	 * TODO 第一版匹配规则 废弃
	 * @param player
	 * @return
	 */
	public OverallWarOpponentVo matchOpponent(Player player) {
		int score = player.playerOverallWar().getScore();
		if(score<70){//小于70分从NPC处随机
			int npcId = overallWarConfig.randomNpc();
			String opponentId = "npc_"+npcId;
			player.playerOverallWar().matchOpponent(opponentId);
			return new OverallWarOpponentVo(opponentId);
		}
		final long playerId = player.getId();
		int diff = 0;
		List<Integer> userIds = HdLeaderboardsService.getInstance().getUserIdsThanScore(player.getServerId(),RankType.OverallWarRank.getRankName(),score, score)
				.stream().filter(t -> playerId != t).collect(Collectors.toList());
		while(userIds.isEmpty()){
			diff+=10;
			int maxScore = score+diff;
			int minScore = score-diff;
			if(minScore<0){//如果最小分数到0分还没有
				userIds.clear();
				break;
			}
			userIds = HdLeaderboardsService.getInstance().getUserIdsThanScore(player.getServerId(),RankType.OverallWarRank.getRankName(),minScore, maxScore)
					.stream().filter(t -> playerId != t).collect(Collectors.toList());
		}
		if(!userIds.isEmpty()){
			int opponentId = userIds.get(MathUtils.random(0, userIds.size()));
			player.playerOverallWar().matchOpponent(opponentId+"");
			Player opponent = PlayerUtils.getPlayer(opponentId);
			return new OverallWarOpponentVo(opponent);
		}
		String randomNpc = "npc_"+overallWarConfig.randomNpc();
		player.playerOverallWar().matchOpponent(randomNpc);
		return  new OverallWarOpponentVo(randomNpc);
	}
	
	/**
	 * 第三版匹配规则 
	 * @param xjt
	 * @date 2019年7月17日09:27:45
	 * @return
	 */
	public OverallWarOpponentVo matchOpponentNew2(Player player) {
		//获取50级以上的所有玩家
        List<Integer> userIds = HdLeaderboardsService.getInstance().getUserIdsThanScore(player.getServerId(),RankType.Combat.getRankName(),0, 10000000000l);
        //所有大于50级的玩家排行
        List<Integer> result = userIds.stream().collect(Collectors.toList());
        //把玩家分为5挡
        Map<Integer,List<Integer>> grades = overallWarConfig.createGrades(result);
        //本次玩家从哪一档取对手
        int opponentGrade = player.playerOverallWar().getOpponentGrade();
        if(opponentGrade<=0){
            //玩家当前处于的档位
            int grade = getGrade(grades, player.getId());
            log.error(player.getId()+"确定自己的档位:"+grade);
            player.playerOverallWar().calMyGrade(grade);
            //第一次匹配玩家中的最大档
            opponentGrade=overallWarConfig.getMaxGrade();
            player.playerOverallWar().setOpponentGrade(opponentGrade);
        }
        log.error(player.getId()+"对手档位:"+opponentGrade);
        if(opponentGrade<=overallWarConfig.getMaxGrade()&&!CollectionUtil.isEmpty(grades.get(opponentGrade))){
			//获取对手
			int opponentId = getOpponent(player, opponentGrade, grades);
			int count =0;
			Player opponent = PlayerUtils.getPlayer(opponentId);
			while((opponent==null||!opponent.playerOverallWar().isOpenOverallWar())&&count<30){
				grades.get(opponentGrade).remove(new Integer(opponentId));
				opponentId = getOpponent(player, opponentGrade, grades);
				opponent = PlayerUtils.getPlayer(opponentId);
				count++;
			}
			if(opponent!=null&&opponent.playerOverallWar().isOpenOverallWar()){
				player.playerOverallWar().matchOpponent(opponentId+"");
                log.error(player.getId()+"匹配对手:"+opponent.getId());
				return new OverallWarOpponentVo(opponent);
			}
		}
		String randomNpc = "npc_"+overallWarConfig.randomNpc();
        log.error(player.getId()+"未匹配到对手，随机npc:"+randomNpc);
		player.playerOverallWar().matchOpponent(randomNpc);
		return new OverallWarOpponentVo(randomNpc);
	}

	public List<WarResult> fight(Player player) {
		Map<Integer, ClientTroop> atkMap = player.playerOverallWar().getClientTroops();
		List<WarResult> results = Lists.newArrayList();
		Map<Integer,ClientTroop> defTroops = Maps.newConcurrentMap();
		OverallWarTemplate template = null;
		Player opponent = null;
		if(!player.playerOverallWar().isNpc()){
			opponent = PlayerUtils.getPlayer(player.playerOverallWar().getOpponentIntId());
			defTroops = opponent.playerOverallWar().getClientTroops();
		}else{
			template = overallWarConfig.getNpc(player.playerOverallWar().getNpcId());
			defTroops = template.getDefTroops();
		}
		int winNum = 0;
		int failNum = 0;
		for(Map.Entry<Integer, ClientTroop> entry : atkMap.entrySet()){
			int type = entry.getKey();
			PlayerTroop atkTroop = new PlayerTroop(player.getId(), "atk"+type);
			atkTroop.loadClientTroop(entry.getValue());
			WarResult warResult = null;
			if(player.playerOverallWar().isNpc()){
				NpcTroop troop = new NpcTroop("def"+(type));
				troop.loadNpcInfo(defTroops.get(type).getArmyList(), template.getId(), template.getName(), template.getLevel(),
						template.getHead_icon(), template.getHead_frame(),template.getCombats().get(type-1));
				warResult = new FightProxy().doFight(atkTroop, troop);
			}else{
				PlayerTroop defTroop = new PlayerTroop(player.playerOverallWar().getOpponentIntId(), "def"+type);
				defTroop.loadClientTroop(defTroops.get(type));
				warResult = new FightProxy().doFight(atkTroop, defTroop);
			}
			results.add(warResult);
			warResult.setWarResultType(WarResultType.OverallWar);
			BattleRecord record = new BattleRecord(player.getServerId(), warResult);
			record.loadParam(type+"");
			WarResultUtils.saveOrUpdate(record);
			//保存战报
			player.playerOverallWar().addRecord(record.getId());
			winNum = warResult.isAtkWin()?winNum+1:winNum;
			failNum = warResult.isAtkWin()?failNum:failNum+1;
			if(winNum>=3||failNum>=3){
				break;
			}
		}
		return results;
	}
	//结算
	public int doResult(Player player, boolean result) {
		//结算防御方
		//TODO 新的结算方法不再结算防守方只结算进攻方
		/*if(!player.playerOverallWar().isNpc()){
			Player opponent = PlayerUtils.getPlayer(player.playerOverallWar().getOpponentIntId());
			if(player!=null){
				opponent.playerOverallWar().calDefResult(result);
			}
		}*/
		//结算进攻方
		return player.playerOverallWar().fight(result);
	}
	//全面战争是否开启
	public boolean isRunning() {
		//周二，四，六晚18点30-19点30开启
		if(OpenWeeks.contains(DateUtil.getCsWeek())){
			int startHour = 0;
			int endHour = 23;
			Date now = new Date();
			int hour = DateUtil.hour(now, true);
			int minute = DateUtil.minute(now);
			if(hour>=startHour&&hour<endHour){
				return true;
			}
		}
		return false;
	}
	
	public OverallWarOpponentVo createOpponentVo(Player player){
		OverallWarOpponentVo  vo = null;
		if(StrUtil.isBlank(player.playerOverallWar().getOpponentId())){
			return null;
		}
		if(player.playerOverallWar().isNpc()){
			vo = new OverallWarOpponentVo(player.playerOverallWar().getOpponentId());
		}else{
			Player opponent = PlayerUtils.getPlayer(player.playerOverallWar().getOpponentIntId());
			if(opponent!=null){
				vo = new OverallWarOpponentVo(opponent);
			}
		}
		return vo;
	}
	
	//是否符合领奖条件
	public boolean isCanReceive(Player player, int id) {
		OverallWarRewardTemplate template = overallWarConfig.getReward(id);
		//1为连胜奖励 2为胜场奖励
		int num = template.getType()==1?player.playerOverallWar().getMaxContinueWin():player.playerOverallWar().getWinNum();
		return num>=template.getNumber();
	}

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.ServerFunction, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp,this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
		//ObserverRouter.getInstance().registObserver(ObservableEnum.MinuteEvent, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		switch(observableEnum){
			case ServerFunction:
				int serverId = Integer.parseInt(argv[0].toString());
				int type = Integer.parseInt(argv[1].toString());
				if(type==ServerFunctionType.OverallWar.getType()){
					overallWarOpen(observableEnum, serverId, argv);
				}
				break;
			case TankAdd:
			case PlayerLevelUp:
				player.playerOverallWar().checkOverallWar();
				break;
//			case MinuteEvent:
//				sendReward(argv);
//				break;
		}
	}
	private void sendReward(Object[] argv) {
		if(!OpenWeeks.contains(DateUtil.getCsWeek())){
			return;
		}
		int minute = Integer.parseInt(argv[0].toString());
		int hour = DateUtil.hour(new Date(), true);
		if(hour!=19){
			return;
		}
		if(minute!=30){
			return;
		}
		GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
			try {
				leaderboardBiz.doRankReward(serverId, RankType.OverallWarRank);
			} catch (Exception e) {
				log.error("全面战争发放排行奖励出错:"+serverId);
			}
		});
	}
	//全面战争开放
	private void overallWarOpen(ObservableEnum observableEnum, int  serverId,
			Object... argv){
		for(Player p:PlayerContainer.getOnlinePlayersByServerId(serverId)){
			if(isCanOpenOverallWar(p)){
				p.playerOverallWar().checkOverallWar();
				p.sendUserUpdateMsg();
			}
		}
	}
	public boolean isCanOpenOverallWar(Player player){
		return player.playerLevel().getLv()>=50&&player.playerTank().getTankList().size()>=15;
	}
	/**
	 * 判断今天是否开启全面战争
	 * @param serverId
	 * @return
	 */
	public boolean isOverallWarOpenDay(int serverId,int week){
		if(serverDataBiz.isServerFunctionOpen(serverId, ServerFunctionType.OverallWar)) {
			return OpenWeeks.contains(week);
		}
		return false;
	}
	//获取玩家所在档位
	public int getGrade(Map<Integer,List<Integer>> map ,long playerId){
		for(Map.Entry<Integer, List<Integer>> entry: map.entrySet()){
			if(entry.getValue().contains(playerId)){
				return entry.getKey();
			}
		}
		return 0;
	}
	//获得对手
	public int getOpponent(Player player,int grade,Map<Integer,List<Integer>> grades){
		List<Integer> userIds = grades.get(grade).stream().filter(t -> t.intValue() != player.getId()).collect(Collectors.toList());
		if(!CollectionUtil.isEmpty(userIds)){
			return userIds.get(MathUtils.random(0, userIds.size()));
		}
		return -1;
	}

}
