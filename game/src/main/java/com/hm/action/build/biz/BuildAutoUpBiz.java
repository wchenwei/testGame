package com.hm.action.build.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.item.ItemBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.config.BuildConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.templaextra.BuildUpTemplate;
import com.hm.enums.BuildQueueType;
import com.hm.enums.LogType;
import com.hm.enums.QueueType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.Player;
import com.hm.model.queue.AbstractQueue;
import com.hm.model.queue.BuildUpQueue;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
/**
 * 
 * @author xjt
 * @date 2018年3月8日15:22:37
 *
 */
@Biz
public class BuildAutoUpBiz implements IObserver{
	@Resource
	private BuildBiz buildBiz;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private QueueBiz queueBiz;
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);		
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		buildBiz.startAutoBuild(player);
	}
	/**
     * 登录时检查建筑自动升级
     * @param player
     * @param session
     */
    public void autoBuildUpLevel(Player player) {
    	Date outDate = player.playerBaseInfo().getLastOffLineDate();//上次离线时间
    	if(outDate == null || !player.playerBuild().inAutoTime(outDate.getTime())) {
    		return;
    	}
    	//playerBiz.checkPlayerQueue(player);
    	List<BuildUpQueue> runningQueueList = getRunningBuild(player);//获取登录时应该完成的建造队列
    	if(runningQueueList==null){
    		return;
    	}
    	queueBiz.checkPlayerQueue(player);//将能够完成的队列完成
    	for(BuildUpQueue queue :runningQueueList){
    		BuildVO buildVo = getAutoLevelUp(player);//获取最优建造队列
    		if(buildVo!=null){
    			if(!player.playerBuild().inAutoTime(outDate.getTime())){
    				return;
    			}
    			//开始自动建造
    			autoUpBuild(player, buildVo, queue.getBuildQueueType(), queue.getEndTime());
    		}
    	}
    	//第一遍检查结束，新队列产生
    	
    	//获取最先完成的队列
    	BuildUpQueue fastQueue = getFastCompleteQueue(player);
    	while(fastQueue!=null){
    		if(!fastQueue.isOver()){
    			return;
    		}
    		long endTime = fastQueue.getEndTime();
    		player.playerQueue().queueComplete(fastQueue.getId());
    		if(player.playerBuild().inAutoTime(endTime)){
    			BuildVO vo = getAutoLevelUp(player);
    			if(vo==null){
    				return;
    			}
    			autoUpBuild(player, vo, fastQueue.getBuildQueueType(), fastQueue.getEndTime());
    			fastQueue = getFastCompleteQueue(player);
    		}
    	}
    }
    /**
     * 自动升级建筑
     * @param player
     * @param buildQueueType 占用的队列类型
     * @param startTime 开始时间
     */
    public BuildUpQueue autoUpBuild(Player player,BuildVO buildVo,int buildQueueType,long startTime) {
    	BuildUpTemplate temp = buildConfig.getTemplateByType(buildVo.getBuildType(), buildVo.getLv());
		//检查并且扣除资源
		if(!itemBiz.checkItemEnoughAndSpend(player, temp.getCosts(), LogType.BuildLvUp.value(buildVo.getBuildType()+"_"+buildVo.getLv()))) {
			return null;
		}
		if(buildVo.getLv()==0){
			//是建造，则随机一个空位把建筑放上去
			//随机一个空位
			player.playerBuild().createBuild(buildBiz.getEmptyBlockId(player), buildVo.getId(), 0);
		}
		//升级建筑时间 = 升级读表时间/ (1 - 科技加成%)
		long totalTime = buildBiz.getBuildSecond(player, buildVo.getBuildType(), buildVo.getLv());
		BuildUpQueue queue = new BuildUpQueue(player);
		queue.setBuildId(buildVo.getId());
		queue.setBuildQueueType(buildQueueType);
		queue.setEndTime(startTime+totalTime*GameConstants.SECOND);
		queue.setTotalTime((int)totalTime);
		player.playerQueue().addQueue(queue);
		player.notifyObservers(ObservableEnum.BuildLevelUpStart, buildVo.getId(),queue.getId());
		return queue;
    }
	/**
     * 获取最优的可升级建筑
     * @param player
     * @return
     */
    public BuildVO getAutoLevelUp(Player player) {
    	BuildQueueType freeQueueType = player.getFreeBuildQueue();//获取空闲建筑队列
    	if(freeQueueType==BuildQueueType.None){//无可用队列
    		return null;
    	}
    	//先查看玩家有没有达到建造条件但是没有没有建造的建筑
	    List<BuildVO> buildVos = buildBiz.createNoBuildVos(player);
	    if(CollUtil.isEmpty(buildVos)){
	    	buildVos = buildBiz.createPlayerBuildVos(player);////玩家已有的建筑模板
	    }
	    //排除不满足升级条件和资源不足的建筑
	    buildVos = screenAutoBuildVO(player,buildVos);
    	if(buildVos.isEmpty()) {//如果第一次没有适用建筑则将所有建筑放进来再刷选一遍
    		buildVos = buildBiz.createPlayerBuildVos(player);
    		if(screenAutoBuildVO(player,buildVos).isEmpty()){
    			return null;
    		}
    	}
    	//按照等级升序排序
        Comparator<BuildVO> byLv = Comparator.comparing(BuildVO::getLv);
        //按照消耗资源升序排序
        Comparator<BuildVO> byCost = Comparator.comparing(BuildVO::getCost);
        //按等级和消耗资源升序排序
        buildVos.sort(byLv.thenComparing(byCost));
    	return buildVos.get(0);
    }
    //筛选自动建造模板，去除条件不足的
    private List<BuildVO> screenAutoBuildVO(Player player,List<BuildVO> buildVos){
    	//排除不满足升级条件和资源不足的建筑
	    for(int i=buildVos.size()-1;i>=0;i--){
	    	BuildVO buildvo = buildVos.get(i);
	    	if(!buildBiz.isCanlvUp(player, buildvo.getBuildType(),buildvo.getLv())){//排除不满足升级条件的
	    		buildVos.remove(i);
	    		continue;
	    	}
	    	BuildUpTemplate buildTemplate = buildConfig.getTemplateByType(buildvo.getBuildType(), buildvo.getLv());
	    	if(!itemBiz.checkItemEnough(player, buildTemplate.getCosts())){//排除资源不足的
	    		buildVos.remove(i);
	    		continue;
	    	}
	    	//排除正在升级的
	    	BuildUpQueue queue = player.playerQueue().getQueueByBlockIdAndQueueType(buildvo.getId(), QueueType.BuildUp);
	    	if(queue!=null){
	    		buildVos.remove(i);
	    		continue;
	    	}
	    	//排除没有在升级但是该建筑上有正在进行的队列的
	    	List<AbstractQueue> queues = player.playerQueue().getQueueByBuildId(buildvo.getId());
	    	if(!queues.isEmpty()){//有正在进行的队列
    			buildVos.remove(i);
    			continue;
	    	}
	    	
	    }
	    return buildVos;
    }
    /**
     * 登录获取已经完成升级的建筑队列
     * @param playerBuilds
     * @return
     */
    private List<BuildUpQueue> getRunningBuild(Player player) {
    	List<BuildUpQueue> runningQueue = Lists.newArrayList();
    	List<AbstractQueue> queueList = player.playerQueue().getQueueByType(QueueType.BuildUp);
    	for(AbstractQueue queue : queueList){
    		if(queue.isOver()){
    			runningQueue.add((BuildUpQueue)queue);
    		}
    	}
		if(runningQueue.isEmpty()) {
			return null;
		}
		Collections.sort(runningQueue, new Comparator<BuildUpQueue>() {
			@Override
			public int compare(BuildUpQueue o1, BuildUpQueue o2) {
				return (int) (o1.getEndTime() - o2.getEndTime());
			}
		});
		return runningQueue;
    }
    /**
     * 获取最先完成的队列
     * @param player
     * @return
     */
    private BuildUpQueue getFastCompleteQueue(Player player) {
    	List<BuildUpQueue> list = getRunningBuild(player);
    	if(list.isEmpty()){
    		return null;
    	}
		return list.get(0);
    }
    
}
