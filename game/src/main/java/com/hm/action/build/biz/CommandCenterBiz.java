package com.hm.action.build.biz;//package com.hm.action.build.biz;
//
//import java.util.Map;
//
//import javax.annotation.Resource;
//
//import com.google.common.collect.Maps;
//import com.hm.libcore.annotation.Biz;
//import com.hm.action.activity.ActivityEffectBiz;
//import com.hm.action.build.vo.OpenComandCenterVO;
//import com.hm.action.player.PlayerBiz;
//import com.hm.action.season.biz.SeasonBiz;
//import com.hm.config.excel.SeasonConfig;
//import com.hm.config.excel.WorkerConfig;
//import com.hm.config.excel.temlate.WorkerTemplate;
//import com.hm.enums.CDType;
//import com.hm.enums.LogType;
//import com.hm.enums.ResAddType;
//import com.hm.enums.SeasonEffectType;
//import com.hm.enums.TechIdType;
//import com.hm.enums.WorkerType;
//import com.hm.model.baseinterface.mine.ElecMineCenter;
//import com.hm.model.baseinterface.mine.FerrumMineCenter;
//import com.hm.model.baseinterface.mine.GoldMineCenter;
//import com.hm.model.baseinterface.mine.OilWellMineCenter;
//import com.hm.model.cd.CdData;
//import com.hm.model.player.Player;
//import com.hm.model.player.CurrencyKind;
//import com.hm.model.worker.WorkerData;
//
///**
// * 
// * @author xjt
// * @date 2018年3月9日15:55:15
// *
// */
//@Biz
//public class CommandCenterBiz {
//	@Resource
//	private WorkerConfig workerConfig;
//	@Resource
//	private PlayerBiz playerBiz;
//	@Resource
//	private TechCentreBiz techCentreBiz;
//	@Resource
//	private ActivityEffectBiz activityEffectBiz;
//	@Resource
//	private SeasonConfig seasonConfig;
//	@Resource
//	private SeasonBiz seasonBiz;
//	
//	//创建打开指挥中心返回模板
//	public OpenComandCenterVO createOpenComandCenterVO(Player player){
//		OpenComandCenterVO vo = new OpenComandCenterVO();
//		Map<CurrencyKind,Double> seasonAddMap = getSeasonAddMap();
//		Map<CurrencyKind,Double> activityAddMap = getResActivityAddMap();
//		Map<CurrencyKind,Double> techAddMap = getResTechAddMap(player);
//		vo.setSeasonAddMap(seasonAddMap);
//		vo.setActivityAdd(activityAddMap);
//		vo.setTechnologyAdd(techAddMap);
//		vo.setBaseRes(getBaseResMap(player));//基础资源产量
//		vo.setWorkerAdd(getWorkerAdd(player));
//		return vo;
//	}
//	public Map<CurrencyKind,Double> getWorkerAdd(Player player){
//		WorkerData workerData = player.getPlayerWorkers().getWorkerData(WorkerType.NZG);
//		Map<CurrencyKind,Double> workerAdd = Maps.newConcurrentMap();
//		if(workerData==null){
//			return workerAdd;
//		}
//		WorkerTemplate workTemplate = workerConfig.getWorkerTemplate(workerData.getWorkerId());
//		if(workerAdd==null){
//			return workerAdd;
//		}
//		double add = workTemplate.getBuff_add();
//		workerAdd.put(CurrencyKind.Oil, workTemplate.getOli()==1?add:0);
//		workerAdd.put(CurrencyKind.PaperMoney, workTemplate.getFe()==1?add:0);
//		workerAdd.put(CurrencyKind.Elec, workTemplate.getElec()==1?add:0);
//		workerAdd.put(CurrencyKind.Gold, workTemplate.getGold()==1?add:0);
//		return workerAdd;
//		
//	}
//	//获取资源矿的基础产量
//	public Map<CurrencyKind,Integer> getBaseResMap(Player player){
//		Map<CurrencyKind,Integer> baseResmap = Maps.newConcurrentMap();//资源基础产量
//		FerrumMineCenter ferrumMineCenter = player.ferrumMineCenter();
//		baseResmap.put(ferrumMineCenter.getResType(), ferrumMineCenter.getBaseResYiled());
//		OilWellMineCenter oilWellMineCenter = player.oilWellMineCenter();
//		baseResmap.put(oilWellMineCenter.getResType(), oilWellMineCenter.getBaseResYiled());
//		GoldMineCenter goldMineCenter = player.goldMineCenter();
//		baseResmap.put(goldMineCenter.getResType(), goldMineCenter.getBaseResYiled());
//		ElecMineCenter elecMineCenter = player.elecMineCenter();
//		baseResmap.put(elecMineCenter.getResType(), elecMineCenter.getBaseResYiled());
//		return  baseResmap;
//	}
//	//获取季节加成
//	public Map<CurrencyKind,Double> getSeasonAddMap(){
//		Map<CurrencyKind,Double> seasonAddMap = Maps.newConcurrentMap();
//		seasonAddMap.put(CurrencyKind.PaperMoney, seasonBiz.getSeasonEffect(SeasonEffectType.PaperMoney_Collect));
//		seasonAddMap.put(CurrencyKind.Oil, seasonBiz.getSeasonEffect(SeasonEffectType.Oil_Collect));
//		seasonAddMap.put(CurrencyKind.Elec, seasonBiz.getSeasonEffect(SeasonEffectType.Elec_Collect));
//		seasonAddMap.put(CurrencyKind.Gold, seasonBiz.getSeasonEffect(SeasonEffectType.Gold_Collect));
//		return  seasonAddMap;
//	}
//	//获取活动资源加成
//	public Map<CurrencyKind,Double> getResActivityAddMap(){
//		Map<CurrencyKind,Double> activityAddMap = Maps.newConcurrentMap();
//		double resAdd = activityEffectBiz.getWorldResEffect();
//		activityAddMap.put(CurrencyKind.PaperMoney, resAdd);
//		activityAddMap.put(CurrencyKind.Oil, resAdd);
//		activityAddMap.put(CurrencyKind.Elec, resAdd);
//		activityAddMap.put(CurrencyKind.Gold, resAdd);
//		return  activityAddMap;
//	}
//	//获取科技对资源的加成
//	public Map<CurrencyKind,Double> getResTechAddMap(Player player){
//		Map<CurrencyKind,Double> techAddMap =Maps.newConcurrentMap();
//		techAddMap.put(CurrencyKind.PaperMoney, (double)techCentreBiz.getTechEffect(player, TechIdType.Ferrum));
//		techAddMap.put(CurrencyKind.Oil, (double)techCentreBiz.getTechEffect(player, TechIdType.Oil));
//		techAddMap.put(CurrencyKind.Elec, (double)techCentreBiz.getTechEffect(player, TechIdType.Elec));
//		techAddMap.put(CurrencyKind.Gold, (double)techCentreBiz.getTechEffect(player, TechIdType.Gold));
//		return techAddMap;
//	}
//	//获取资源的活动加成
//	public double getActivityAddByRes(CurrencyKind kind){
//		Map<CurrencyKind,Double> map = getResActivityAddMap();
//		if(map.containsKey(kind)){
//			return map.get(kind);
//		}
//		return 0;
//	}
//	public Map<CurrencyKind,Double> getResAdd(Player player,ResAddType addtype){
//		int type = addtype.getType();
//		switch (type) {
//		case 1:
//			return getSeasonAddMap();
//		case 2:
//			return getWorkerAdd(player);
//		case 3:
//			return getResActivityAddMap();
//		case 4:
//			return getResTechAddMap(player);
//		}
//		return Maps.newConcurrentMap();
//	}
//	//获取资源的某种加成
//	public double getTechAddByRes(Player player,CurrencyKind kind,ResAddType type){
//		Map<CurrencyKind,Double> map = getResAdd(player,type);
//		if(map.containsKey(kind)){
//			return map.get(kind);
//		}
//		return 0;
//	}
//	//收矿
//	public Map<CurrencyKind,Long> collectionRes(Player player,int type,boolean isDouble) {
//		CdData cdData = player.getPlayerCDs().getCdDataByCdType(CDType.SK);
//		int collectCount = type==1?cdData.getCount():1;
//		Map<CurrencyKind,Long> map = getResYiled(player,collectCount);
//		if(isDouble) {
//			for (Map.Entry<CurrencyKind,Long> entry : map.entrySet()) {
//				map.put(entry.getKey(), entry.getValue()*2);
//			}
//		}
//		playerBiz.addPlayerCurrency(player, map, LogType.Collection);
//		player.getPlayerCDs().touchCdEvent(CDType.SK,type);//减当前次数
//		return map;
//	}
//	//获取各种资源收取一次的量
//	public Map<CurrencyKind,Long> getResYiled(Player player){
//		return getResYiled(player,1);
//	}
//	
//	/**
//	 * 获取各种资源征收量
//	 * @param player
//	 * @param count 征收次数
//	 * @return
//	 */
//	public Map<CurrencyKind,Long> getResYiled(Player player,int count){
//		Map<CurrencyKind,Integer> baseResMap = getBaseResMap(player);//资源基础产量
//		Map<CurrencyKind,Long> map = Maps.newConcurrentMap();
//		for(Map.Entry<CurrencyKind, Integer> entry: baseResMap.entrySet()){
//			CurrencyKind kind = entry.getKey();
//			int num = entry.getValue();
//			double techAdd = getTechAddByRes(player,kind,ResAddType.technology);
//			double seasonAdd = getTechAddByRes(player,kind,ResAddType.Season);
//			double activityAdd = getTechAddByRes(player,kind,ResAddType.Activity);
//			double workerAdd = getTechAddByRes(player,kind,ResAddType.Worker);
//			num = (int) (num*count*(1+techAdd+seasonAdd+activityAdd+workerAdd));
//			map.put(kind, (long)num);
//		}
//		return map;
//	}
//	
//	/**
//	 * 获取征收一次资源的数量
//	 * @param player 玩家
//	 * @param kind 资源类型
//	 * @return
//	 */
//	public long getCollectionResValue(Player player,CurrencyKind kind) {
//		Map<CurrencyKind,Long> resMap = getResYiled(player);
//		if(resMap.containsKey(kind)) {
//			return resMap.get(kind);
//		}
//		return 0;
//	}
//}
