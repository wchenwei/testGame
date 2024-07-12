package com.hm.action.build.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.action.build.vo.BuildingLvUpVO;
import com.hm.action.build.vo.OpenBuildVO;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.player.biz.PlayerDynamicDataBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.action.shop.biz.ShopBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.BuildConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.BuildingUnlockTemplate;
import com.hm.config.excel.templaextra.BuildUpTemplate;
import com.hm.enums.*;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.queue.BuildUpQueue;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.rmi.LogServerRmiHandler;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xjt
 * @date 2020年2月24日10:41:46
 * @version 1.0
 */
@Biz
public class BuildBiz implements IObserver{
	@Resource
	private BuildBiz buildBiz;
	@Resource
	private LogServerRmiHandler logServerRmiHandler;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private ShopBiz shopBiz;
	@Resource
	private VipBiz vipBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private QueueBiz queueBiz;
	@Resource
	private PlayerDynamicDataBiz playerDynamicDataBiz;
	@Resource
	private BuildAutoUpBiz buildAutoUpBiz;
	

    @Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.BuildLvUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.AddRes, this);
	}
	
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		switch(observableEnum){
		case FunctionUnlock:
			int functionId = (int) argv[0];
	        if(functionId==PlayerFunctionType.BuildUnlock.getType()){
	        	checkBuildOpen(player);
	        }
			break;
		case BuildLvUp:
			int buildId = (int)argv[0];
			if(buildId == GameConstants.CommandCentreBuildId){
				checkAutoBuild(player);
			}
			break;
		case PlayerLevelUp:
			checkAutoBuild(player);
			break;
		case AddRes:
			int id = (int) argv[0];
			if(id==CurrencyKind.Crystal.getIndex()){
				checkAutoBuild(player);
			}
			break;
		}
        
        
	}

	//解锁默认开启建筑
	private void checkBuildOpen(Player player) {
		List<BuildingUnlockTemplate> builds = buildConfig.getBuilds().stream().filter(t ->t.getOpen_base()==1&&t.getLocation()>0).collect(Collectors.toList());
		if(player.playerBuild().getCenterLv()>0) {
			//已解锁过不用再解锁
			return;
		}
		builds.forEach(t ->{
			player.playerBuild().createBuild(t.getLocation(),t.getId(), t.getLevel());
			player.notifyObservers(ObservableEnum.BuildLvUp, t.getId());
		});
		List<Items> initRewards = commValueConfig.getListItem(CommonValueType.BuildInitRes);
		itemBiz.addItem(player, initRewards, LogType.BuildInitRes);
		playerDynamicDataBiz.calPlayerDynamicData(player);
		
	}

	/**
	 * 验证资源是否足够升级或建筑
	 * 
	 * @param blockId
	 *            建筑唯一id
	 * @param lv
	 *            下一等级
	 * @param player
	 *            玩家对象
	 * @param isCost
	 *            是否消耗资源.true=扣除资源 false=只检查不扣资源
	 * @return
	 */
	public boolean checkBuildLvUp(int buildType, int lv, Player player, boolean isCost) {
		BuildUpTemplate template = buildConfig.getTemplateByType(buildType, lv);
		if (template == null) {
			return false;
		}
		return itemBiz.checkItemEnoughAndSpend(player, template.getCosts(), LogType.BuildLvUp.value(buildType+"_"+lv));
	}

	
	/**
	 * 验证资源是否足够升级或建筑
	 * 
	 * @param buildLvUpTemplate
	 * @param lv
	 * @param player
	 * @return
	 */
	public boolean checkBuildLvUpCost(int buildType, int lv, Player player) {
		return checkBuildLvUp(buildType, lv, player, true);
	}

	/**
	 * 计算建筑升级资源不足所需的金币
	 * 
	 * @param buildLvUpTemplate
	 * @param lv
	 * @param player
	 * @return
	 */
	/*public int getBuildLvUpGoldCost(int buildType, int lv, Player player) {
		BuildUpTemplate temp = buildConfig.getTemplateByType(buildType, lv);
		if(temp==null){
			return 0;
		}
		//每分钟2钻石,时间直接减去vip的免费加速时间
		int timeDiamonds = (int) (Math.ceil(MathUtils.div(getBuildSecond(player, buildType, lv)-vipBiz.getVipPow(player, VipPowType.BuildSpeed),60))*commValueConfig.getCommValue(CommonValueType.BuildUpSpeedCost));
		if(timeDiamonds<0){
			timeDiamonds=0;
		}
		int resDiamonds = 0;
		for (Map.Entry<CurrencyKind, Long> entry : temp.getCostResMap().entrySet()) {
			// 计算不足的资源量
			double diff = entry.getValue() - player.playerCurrency().get(entry.getKey());
			if (diff > 0) {
				//转化单价：所需资源量/（征收一次的量/商店购买价格）
				int diamonds = (int) Math.ceil(MathUtils.div(diff, shopBiz.getResByDiamonds(player, entry.getKey())));
				resDiamonds += diamonds;
			}
		}
		return resDiamonds+timeDiamonds;
	}*/
	/**
	 * 立刻升级建筑消耗资源
	 * 
	 * @param buildLvUpTemplate
	 * @param lv
	 * @param feGold
	 * @param leadGold
	 * @param oilGold
	 * @param player
	 *            日志分为消耗金币和不消耗金币
	 */
	/*public void reduceBuildLvUpResource(int buildType, int lv, Player player) {
		BuildUpTemplate temp = buildConfig.getTemplateByType(buildType, lv);
		for (Map.Entry<CurrencyKind, Long> entry : temp.getCostResMap().entrySet()) {
			CurrencyKind kind = entry.getKey();
			// 计算不足的资源量
			long diff = entry.getValue() - player.playerCurrency().get(kind);
			// 实际消耗的资源量，如果资源不足则消耗金币，金币已经消耗，只需将玩家该种资源全部消耗
			long cost = diff > 0 ? player.playerCurrency().get(kind) : entry.getValue();
			playerBiz.spendPlayerCurrency(player, entry.getKey(), cost,LogType.BuildImmediately);
		}
	}*/

	public BuildingLvUpVO createBuildingLvUpVO(int type, int time, int allTime, int templateId, int blockId,
			Player player) {
		BuildingLvUpVO buildingLvUpVO = new BuildingLvUpVO();
		buildingLvUpVO.setType(type);
		buildingLvUpVO.setTime(time);
		buildingLvUpVO.setAllTime(allTime);
		buildingLvUpVO.setTemplateId(templateId);
		buildingLvUpVO.setBlockId(blockId);
		return buildingLvUpVO;
	}

	
	/**
	 * 
	 * @param blockId
	 * @param i
	 * @param player
	 */
	public void buildLvUpStart(int buildId, int buildType,BuildQueueType buildQueueType, int lv, Player player) {
		long totalTime = getBuildSecond(player, buildType, lv);
		BuildUpQueue buildQueue = new BuildUpQueue(player,buildId,totalTime);
		buildQueue.setBuildQueueType(buildQueueType.getType());
		player.playerQueue().addQueue(buildQueue);
	}

	//获取建造时间(秒)
	public long getBuildSecond(BasePlayer player,int buildType,int lv){
		BuildUpTemplate template = buildConfig.getTemplateByType(buildType, lv);
		//升级建筑时间 = 升级读表时间* (1 - 科技减少%)
		//double speedRate = techCenterBiz.getTechEffect(player, TechIdType.BuildSpeed)
		double speedRate = 0;
		long buildSecond =MathUtils.mul(template.getTime(), 1-speedRate);
		return buildSecond;
	}
	/**
     * 创建打开建筑模板
     * @param build
     * @param buildLvUpTemplate
     * @param player
     */
	public OpenBuildVO createOpenBuildVO(int id,
			int blockId, Player player) {
        //int costGold = getBuildLvUpGoldCost(blockId,build.getLv()+1,player);
        OpenBuildVO vo = new OpenBuildVO();
        /*vo.setBlockId(build.getBlockId());
        vo.setBuildLv(build.getLv());
        vo.setBuildType(build.getType());
        vo.setLastSecond(build.getLastSecond());
        vo.setCostGold(costGold);*/
        return vo;
	}

	/**
	 *判断建筑是否能够升级（只判断等级上限） 
	 */
	public boolean isCanlvUp(BasePlayer player, int buildType, int lv) {
		BuildUpTemplate nextTemp = buildConfig.getTemplateByType(buildType, lv+1);
		if(nextTemp==null){//已达到最高等级
			return false;
		}
		BuildUpTemplate temp = buildConfig.getTemplateByType(buildType, lv);
		if(temp==null){
			return false;
		}
		int centreLv = player.playerBuild().getCenterLv();
		return centreLv>=temp.getLv_mbuilding()&&player.playerLevel().getLv()>=temp.getLv_player();
	} 
	
	//开始自动建造(激活自动建造或激活商用队列时调用)
	public void startAutoBuild(Player player) {
		if(player.playerBuild().inAutoTime()){
			//获取空闲队列
			BuildQueueType buildQueueType = player.getFreeBuildQueue();
			while(buildQueueType!=BuildQueueType.None){//如果有空闲队列则创建队列开始自动建造
				//获取最优升级队列
				//找出最应该升级的建筑
				BuildVO buildVo = buildAutoUpBiz.getAutoLevelUp(player);
				//创建自動建造隊列
				if(buildVo==null){
					return;
				}
				buildAutoUpBiz.autoUpBuild(player, buildVo, buildQueueType.getType(), System.currentTimeMillis());
				buildQueueType = player.getFreeBuildQueue();
			}
		}
		
	}
	
	/**
	 * TODO 解锁全部建筑，提供给客户端测试，后续需要删除
	 */
	/*public void unlockAllBuild(Player player){
		List<UnlockBuildVO> unlockBuildList = Lists.newArrayList();
		//判断是否有建筑解锁
		for(BuildingUnlockTemplate template :buildUnlockConfig.getBuildUnlockMap().values()){
			buildUnlock(player, template.getLocation(),unlockBuildList);//解锁建筑，该方法已经过滤掉已经解锁的建筑
		}
		if(unlockBuildList.size() > 0) {
			player.sendMsg(MessageComm.S2C_UnlockBuild,unlockBuildList);
		}
	}*/
	
	/**
	 * 获取玩家buildType类型建筑的等级
	 * @param buildType
	 * @return
	 */
	public List<Integer> getLvByBuildType(Player player,int buildType){
		List<Integer> lvs = Lists.newArrayList();
		BuildConfig buildConfig = SpringUtil.getBean(BuildConfig.class);
		for(Map.Entry<Integer, Integer> entry :player.playerBuild().getBuilds().entrySet()){
			int id = entry.getKey();
			if(buildType==buildConfig.getBuildType(id)&&player.playerBuild().getBuildLv(id)>0){
				lvs.add(entry.getValue());
			}
		}
		return lvs;
	}

	public void builLvUp(BasePlayer player, int buildId) {
		player.playerBuild().lvUp(buildId);
	}
	
	public long getResCollect(Player player){
		Map<Integer,Integer> lvs = player.playerBuild().getBuildByBuildType(BuildType.CrystalMine.getType());
		long time = player.playerBuild().getProductTime();
		return (long)lvs.values().stream().mapToDouble(lv->{
			BuildUpTemplate template = buildConfig.getBuildUpTemplate(BuildType.CrystalMine.getType(), lv);
			double speed = template.getByAddType(AttributeType.Crystal_Speed);
			double limit = template.getByAddType(AttributeType.Crystal_Product_Limit);
			return Math.min(speed*time, limit);
		}).sum();
	}
	//返还建造资源
	public List<Items> getBuildQueueRe(int buildId,int lv) {
		int buildType = buildConfig.getBuildType(buildId);
		BuildUpTemplate temp = buildConfig.getTemplateByType(buildType, lv);
		List<Items> returnRes = ItemUtils.calItemRateReward( temp.getCosts(), 0.5);
		return returnRes;
	}
	
	//创建所有已有建筑模板
	public List<BuildVO> createPlayerBuildVos(BasePlayer player){
		List<BuildVO> vos = Lists.newArrayList();
		for(Map.Entry<Integer, Integer> entry:player.playerBuild().getBuilds().entrySet()){
			BuildingUnlockTemplate template = buildConfig.getBuild(entry.getKey());
			BuildUpTemplate upTemplate = buildConfig.getBuildUpTemplate(template.getBuild_type(), entry.getValue());
			if(template!=null&&upTemplate!=null){
				vos.add(new BuildVO(entry.getKey(),template.getBuild_type(),entry.getValue(),upTemplate.getCostCrystal()));
			}
		}
		return vos;
	}
	
	//创建所有可以建造但是还没有建造的建筑模板
	public List<BuildVO> createNoBuildVos(BasePlayer player){
		List<BuildVO> vos = Lists.newArrayList();
		List<Integer> buildIds = buildConfig.getCanBuildIds(player);
		for(int id:buildIds){
			BuildingUnlockTemplate template = buildConfig.getBuild(id);
			BuildUpTemplate upTemplate = buildConfig.getBuildUpTemplate(template.getBuild_type(), 0);
			if(template!=null&&upTemplate!=null){
				vos.add(new BuildVO(id, template.getBuild_type(),0,upTemplate.getCostCrystal()));
			}
		}
		return vos;
	}
	
	public static void main(String[] args) {
		System.err.println(TimeUtils.getTimeString(1578306529560l));
	}

	public int getEmptyBlockId(Player player) {
		for(int i=1;i<28;i++){
			if(!player.playerBuild().getBlocks().keySet().contains(i)){
				return i;
			}
		}
		return -1;
	}
	/**
	 * 检查是否可以自动建造
	 * @param player
	 */
	public void checkAutoBuild(Player player) {
		if(player.playerBuild().inAutoTime()){
			buildBiz.startAutoBuild(player);
		}
	}

}
