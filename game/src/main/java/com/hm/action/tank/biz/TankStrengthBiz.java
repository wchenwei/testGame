package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.BuildConfig;
import com.hm.config.GameConstants;
import com.hm.config.MagicReformConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.BuildingTrainingGroundTemplate;
import com.hm.config.excel.templaextra.TankStrengthTaskTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.TrainTank;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.model.tank.TankStrength;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 坦克强化biz（基地内坦克养成）
 * @author xjt
 * @date 2020年2月27日11:26:41
 *
 */
@Biz
public class TankStrengthBiz implements IObserver{
	@Resource
	private MagicReformConfig magicReformConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private ItemBiz itemBiz;



	public boolean isCanBreach(TankStrength tankStrength, int type, int breachLv) {
		//突破所需满足的条件
		Map<Integer,Integer> needMap = buildConfig.getBreakNeed(type, breachLv);
		for(Map.Entry<Integer, Integer> entry: needMap.entrySet()) {
			if(tankStrength.getStrengthNum(entry.getKey())<entry.getValue()) {
				return false;
			}
		}
		return true;
	}


	/**
	 * 
	 * @param id
	 * @param state
	 * @param sweep -false不是扫荡 -true
	 * @return
	 */
	public Map<Integer, TrainTank> createNpc(int id,int state,boolean sweep) {
		Map<Integer,TrainTank> map = Maps.newConcurrentMap();
		BuildingTrainingGroundTemplate template = buildConfig.getTrain(id);
		String[] str = sweep?template.getNpc_onekey().split(","):template.getNpc().split(",");
		int npcId = 1;
		for(String s:str) {
			int type = Integer.parseInt(s.split(":")[0]);
			int num = Integer.parseInt(s.split(":")[1]);
			//根据id和type随机出npc奖励
			for(int i=npcId;i<npcId+num;i++) {
				List<Items> rewards = buildConfig.getNpcRewards(id,type,state);
				map.put(i, new TrainTank(i, type, rewards));
			}
			npcId += num; 
		}
		
		return map;
	}



	public List<Items> getTrainReward(Player player, String npc) {
		Map<Integer, TrainTank> map = player.playerTrain().getNpcMap();
		List<Integer> npcIds = StringUtil.splitStr2IntegerList(npc, ",");
		List<Items> rewards = map.values().stream().filter(t ->npcIds.contains(t.getId())).flatMap(t ->t.getRewards().stream()).collect(Collectors.toList());
		return ItemBiz.createItemList(rewards);
	}
	
	public List<Items> getTrainReward(Player player,Map<Integer,TrainTank> tankMap){
		List<Items> rewards = tankMap.values().stream().flatMap(t ->t.getRewards().stream()).collect(Collectors.toList());
		return ItemBiz.createItemList(rewards);
	}


	/**
	 * 获取坦克强加加成的属性
	 * @param player
	 * @param tank
	 * @return
	 */
	public TankAttr getStrengthAttr(Player player, Tank tank) {
		int lv = player.playerBuild().getBuildLv(GameConstants.TrainCentreId);
		if(lv<=0) {
			return null;
		}
		try {
			TankAttr tankAttr = new TankAttr();
			TankStrength tankStrength = tank.getTankStrength();
			tankStrength.getStrengthMaps().forEach((k,v) ->{
				tankAttr.addAttr(buildConfig.getStrengthAttr(k,v));
			});
			return tankAttr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TankAttr getTaskAttr(Player player) {
		int lv = player.playerBuild().getBuildLv(GameConstants.TrainCentreId);
		if(lv<=0) {
			return null;
		}
		TankAttr tankAttr = new TankAttr();
		player.playerTank().getTaskIds().forEach(t ->{
			TankStrengthTaskTemplate template =  buildConfig.getTask(t);
			if(template!=null) {
				tankAttr.addAttr(template.getAttrMap());
			}
		});
		return tankAttr;
	}
	/**
	 * 获取强化了>=num次的坦克数量
	 * @param num
	 * @return
	 */
	public int getTankStrengthNum(Player player,int num) {
		return (int)player.playerTank().getTankList().stream().filter(t ->t.getTankStrength().getStrengthCount()>=num).count();
	}
	/**
	 * 检查成就是否发生变化
	 * @param player
	 * @return
	 */
	public boolean checkTask(Player player) {
		List<Integer> ids = buildConfig.getTasks().values().stream().filter(t ->getTankStrengthNum(player, t.getStrengthNum())>=t.getNeedNum()).map(t ->t.getTask_id()).collect(Collectors.toList());
		List<Integer> oldTaskIds = player.playerTank().getTaskIds();
		List<Integer> template = Lists.newArrayList(ids);
		template.removeAll(oldTaskIds);
		if(template.size()>0) {
			player.playerTank().changeTaskIds(ids);
			return true;
		}
		return false;
	}



	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankStrength, this);
	}



	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		if(checkTask(player)) {
			player.notifyObservers(ObservableEnum.TankStrengthTaskChange);
		}
		
	}



}
