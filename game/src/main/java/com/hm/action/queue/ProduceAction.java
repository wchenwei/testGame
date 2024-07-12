package com.hm.action.queue;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.config.BuildConfig;
import com.hm.config.excel.templaextra.BuildUpTemplate;
import com.hm.enums.BuildType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.queue.ProduceQueue;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
@Action
public class ProduceAction extends AbstractPlayerAction{
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private BuildConfig buildConfig;
	 /**
     * 生产
     *
     * @param player
     * @param msg
     */
	@MsgMethod(MessageComm.C2S_Build_Product)
    public void compose(Player player, JsonMsg msg) {
    	int index = msg.getInt("index");
        int buildId = msg.getInt("buildId");
        int num = msg.getInt("num");
        int buildType = buildConfig.getBuildType(buildId);
        int lv = player.playerBuild().getBuildLv(buildId);
        if(lv<=0){//还没有该建筑
        	return;
        }
        //不是生产建筑
        if(!BuildType.isProduct(buildType)){
        	return;
        }
        // 有正在进行中的了
        if (!player.playerQueue().getQueueByBuildId(buildId).isEmpty()) {
            return;
        }
        BuildUpTemplate template = buildConfig.getBuildUpTemplate(buildType, lv);
        if(index<=0||index>template.getProducts().size()||num>template.getProductLimit(index)){
        	player.sendErrorMsg(SysConstant.PARAM_ERROR);
        	return;
        }
        
        List<Items> cost = template.getProductCost(index);
        Items product = template.getProduct(index,num);
        if (!itemBiz.checkItemEnoughAndSpend(player, ItemUtils.calItemRateReward(cost, num), LogType.Queue_Produce)) { // 道具不足
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        ProduceQueue queue = new ProduceQueue(player, buildId,index,num,product ,template.getProductTime(index));
        player.playerQueue().addQueue(queue);
        //触发观察者事件
        player.notifyObservers(ObservableEnum.ProduceStart, buildId,product);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Build_Product);
    }
}
