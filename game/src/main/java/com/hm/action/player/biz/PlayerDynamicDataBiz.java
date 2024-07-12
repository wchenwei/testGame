package com.hm.action.player.biz;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.annotation.Biz;
import com.hm.action.build.biz.BuildBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.BuildConfig;
import com.hm.config.excel.templaextra.BuildUpTemplate;
import com.hm.enums.AttributeType;
import com.hm.enums.BuildType;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerDynamicData;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class PlayerDynamicDataBiz implements IObserver{
	@Resource
	private VipBiz vipBiz;
	@Resource
    private CombatBiz combatBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BuildConfig buildConfig;
	@Resource
	private BuildBiz buildBiz;
	
	/**
	 * 仓库上限
	       保护资源上限		
	 */
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.BuildLvUp, this);
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		calPlayerDynamicData(player);
		calPlayerResLimit(player,argv);
	}
	public void calPlayerResLimit(Player player, Object[] argv) {
		int buildId = (int)argv[0];
		int type = buildConfig.getBuildType(buildId);
		BuildType buildType = BuildType.getBuildType(type);
		if(BuildType.isEffectResLimit(type)){//对资源上限有影响
			//获取该建筑类型对哪些属性有影响
			List<AttributeType> attrTypes = AttributeType.getTypeByBuildType(buildType);
			if(CollUtil.isEmpty(attrTypes)){
				return;
			}
			//重新计算属性值
			attrTypes.forEach(t -> player.getPlayerDynamicData().calResLimit(t.getType(), getBaseAttributeByType(player,t)));
		}
	}
	/**
	 * 实时计算玩家动态数据
	 * @param player
	 */
	public void calPlayerDynamicData(Player player) {
		PlayerDynamicData dynamicData = player.getPlayerDynamicData();
	}
	
	/**
     * 获取玩家某个属性的基础值（只和等级相关，不含科技，官职，VIP等加成）
     * @param player
     * @param attributeType
     * @return
     */
    public double getBaseAttributeByType(Player player,AttributeType attributeType){
    	List<BuildType> buildTypes = AttributeType.getBuildTypeByAttribute(attributeType);
    	return buildTypes.stream().mapToDouble(buildType ->{
    		List<Integer> lvs = buildBiz.getLvByBuildType(player, buildType.getType());
    		if(CollUtil.isEmpty(lvs)){
    			return 0;
    		}
			return lvs.stream().filter(lv ->lv>0).mapToDouble(lv ->{
				BuildUpTemplate template = buildConfig.getBuildUpTemplate(buildType.getType(), lv);
				if(template==null){
					return 0;
				}
				return template.getByAddType(attributeType);
			}).sum();
    	}).sum();
	}
	
	
}
