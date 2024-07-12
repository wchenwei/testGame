package com.hm.action.tank;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.tank.biz.ControlBiz;
import com.hm.config.ControlConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.CentralControlLabExtraTemplate;
import com.hm.config.excel.templaextra.ItemElementExtraTemplate;
import com.hm.enums.*;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.ControlLab;
import com.hm.model.tank.ElementPos;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankControl;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.setting.TankSetting;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Action
public class ControlAction extends AbstractPlayerAction {
    @Resource
    private ControlConfig controlConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private ControlBiz controlBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private TankConfig tankConfig;
    @Resource
    private LogBiz logBiz;

//    @MsgMethod(MessageComm.C2S_Control_ElementCompose)
//    public void compose(Player player, JsonMsg msg){
//        int elementId = msg.getInt("elementId");//将3个1级宝石合成1个2级宝石，此处stoneId是1级宝石的id
//        int type = msg.getInt("type");//0-合成 1-一键合成
//        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
//        if(template==null){
//            return ;
//        }
//        int nextId = template.getNext_id();
//        if(nextId==0){
//            //已到最高级
//            return;
//        }
//        ItemElementExtraTemplate nextTemplate = controlConfig.getElement(nextId);
//        //拥有的本级数量
//        long count = player.playerStone().getItemCount(elementId);
//        long num = type==0?1:count/3;
//        List<Items> cost = nextTemplate.getComposeCost(num);
//        if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.ComposeElement.value(template.getNext_id()))){
//            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
//            return;
//        }
//        //生成宝石
//        Items item = new Items(nextId,num,ItemType.Element);
//        itemBiz.addItem(player, item, LogType.ComposeElement);
//        player.sendUserUpdateMsg();
//        player.sendMsg(MessageComm.S2C_Control_ElementCompose);
//    }

    /**
     * 更换元件
     */
    @MsgMethod(MessageComm.C2S_Control_ElementChange)
    public void changeElement(Player player, JsonMsg msg){
        int tankId = msg.getInt("tankId");
        int index = msg.getInt("index");//位置
        int elementId = msg.getInt("elementId");//更换的元件id（id==0表示卸下）

        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return;
        }
        TankControl control = tank.getTankControl();
        ElementPos pos = control.getPos(index);
        if(pos.getElementId()==elementId){//如果要更换的元件和原元件相同则直接返回结果
            player.sendMsg(MessageComm.S2C_Control_ElementChange, index);
            return;
        }
        //该石头能否装备在该位置上(id==0为卸下该位置上的宝石，不需要检查部位条件)
        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
        if(elementId>0&&template==null){
            return;
        }
        if(elementId>0&&pos.getColor()!=template.getColor()){
            //空位颜色和元件颜色不符
            return;
        }
        List<Items> costs = commValueConfig.getListItem(CommonValueType.ControlPosColorDismantle);
        if(elementId==0&&!itemBiz.checkItemEnoughAndSpend(player,costs,LogType.ChangeElement)){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return ;
        }
        //没有该元件
        if(elementId>0&&!itemBiz.checkItemEnoughAndSpend(player, new Items(elementId,1,ItemType.Element),LogType.ChangeElement)){
            player.sendErrorMsg(SysConstant.ITEM_HAVENOT);
            return;
        }
        int oldElementId = tank.getControl().getPos()[index].getElementId();
        controlBiz.changeStone(player,tankId,elementId,index);
        //发出更换元件信号，信号会引起坦克属性和战力的变化
        player.notifyObservers(ObservableEnum.ChangeElement, tankId);
        player.notifyObservers(ObservableEnum.CHTankCtrlChange, tankId, index, oldElementId, elementId);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_ElementChange, elementId == 0 ? -1 : index);
    }

    /**
     * 元件升级
     */
//    @MsgMethod(MessageComm.C2S_Control_ElementLvUp)
//    public void stoneLvUp(Player player, JsonMsg msg){
//        int tankId = msg.getInt("tankId");//tankId
//        int index = msg.getInt("index");//位置
//        Tank tank = player.playerTank().getTank(tankId);
//        if(tank==null){
//            return;
//        }
//        TankControl control = tank.getTankControl();
//        ElementPos pos = control.getPos(index);
//        int elementId = pos.getElementId();
//        if(elementId==0){//如果该位置上没有元件
//            return;
//        }
//        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
//        if(template==null||template.getNext_id()==0){//没有该元件或已经升到最高级
//            return;
//        }
//        //升级的消耗
//        List<Items> cost = controlBiz.getElementLvUpCost(player, tankId, index);
//        if(cost.isEmpty()||!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.LevleUpStone)){
//            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
//            return;
//        }
//        //元件升级
//        tank.elementLvUp(index);
//        player.playerTank().SetChanged();
//        //发出更换石头信号，信号会引起坦克属性和战力的变化
//        player.notifyObservers(ObservableEnum.ElementLvUp, tankId);
//        player.sendUserUpdateMsg();
//        player.sendMsg(MessageComm.S2C_Control_ElementLvUp);
//    }

    /**
     * 元件升级（新的）
     */
    @MsgMethod(MessageComm.C2S_Control_ElementLvUp)
    public void lvUp(Player player, JsonMsg msg){
        int tankId = msg.getInt("tankId");//tankId
        int index = msg.getInt("index");//位置
        String ids = msg.getString("ids");//要消耗的宝石
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return;
        }
        TankControl control = tank.getTankControl();
        ElementPos pos = control.getPos(index);
        int elementId = pos.getElementId();
        if(elementId==0){//如果该位置上没有元件
            return;
        }
        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
        if(template==null||template.getNext_id()==0){//没有该元件或已经升到最高级
            return;
        }
        if(!controlBiz.isCanLvUp(template.getLevel(),ids)){
            return;
        }
        //升级的消耗
        List<Items> cost = controlBiz.getCost(template.getId(), template.getNext_id(), ids);
        if(cost.isEmpty()||!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.lvUpElement.value(String.format("%s_%s", tankId, template.getNext_id())))){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        //元件升级
        tank.elementLvUp(index);
        player.playerTank().SetChanged();
        //发出更换石头信号，信号会引起坦克属性和战力的变化
        player.notifyObservers(ObservableEnum.ElementLvUp, tankId, pos.getElementId(), elementId, cost);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_ElementLvUp,index);
    }

    /**
     * 一键上阵
     */
    @MsgMethod(MessageComm.C2S_Control_ElementUpAll)
    public void upAll(Player player, JsonMsg msg){
        int tankId = msg.getInt("tankId");//tankId
        String ups = msg.getString("ups");//1:101,2:102
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return;
        }
        TankControl control = tank.getTankControl();
        if(!controlBiz.checkUpAll(player,control,ups)){
            return;
        }
        //
        List<Items> costs = ItemBiz.createItemList(Arrays.stream(ups.split(",")).map(t -> new Items(Integer.parseInt(t.split(":")[1]), 1, ItemType.Element)).collect(Collectors.toList()));
        if (!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.ChangeElement.value(tankId))) {
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        for(String str:ups.split(",")){
            int index = Integer.parseInt(str.split(":")[0]);
            int elementId = Integer.parseInt(str.split(":")[1]);
            controlBiz.changeStone(player,tankId,elementId,index);
        }
        player.notifyObservers(ObservableEnum.ChangeElement,tankId);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_ElementUpAll, ups);
    }

    /**
     * 一键下阵
     */
    @MsgMethod(MessageComm.C2S_Control_ElementDownAll)
    public void downAll(Player player, JsonMsg msg){
        int tankId = msg.getInt("tankId");//tankId
        String ups = msg.getString("ups");//1:101,2:102
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return;
        }
        List<Items> elements = Lists.newArrayList();
        TankControl control = tank.getTankControl();
        ElementPos[] pos = control.getPos();
        //要下阵的个数
        int count = (int)Arrays.stream(pos).filter(t->t.getElementId()>0).count();
        List<Items> costs = ItemUtils.calItemRateReward(commValueConfig.getListItem(CommonValueType.ControlPosColorDismantle),count);
        //消耗
        if(!itemBiz.checkItemEnoughAndSpend(player,costs,LogType.ChangeElement.value(tankId))){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        List<Integer> ids = Lists.newArrayList();
        for(int i=0;i<pos.length;i++){
            int elementId = pos[i].getElementId();
            if(elementId>0){
                ids.add(elementId);
                elements.add(new Items(elementId,1,ItemType.Element));
                control.changeElement(i,0);
            }
        }
        logBiz.addPlayerActionLog(player, ActionType.ElementDown.getCode(), StringUtil.list2Str(ids,","));
        player.playerTank().SetChanged();
        itemBiz.addItem(player,elements,LogType.ChangeElement.value(tankId));
        //controlBiz.changeStone(player,tankId,elementId,index);
        player.notifyObservers(ObservableEnum.ChangeElement,tankId);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_ElementDownAll);
    }
    //研究
    @MsgMethod(MessageComm.C2S_Control_Lucky)
    public void luck(Player player, JsonMsg msg){
        int id = msg.getInt("id");
        int type = msg.getInt("type");
        ControlLab lab = player.playerElement().getLab(id);
        CentralControlLabExtraTemplate template = controlConfig.getLab(id);
        if(template==null){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        if(lab==null){
            //这个库还没有开放
            return;
        }
        if(lab.getNum()<=0){
            //没有次数
            return;
        }
        //拥有令牌的个数
        int haveNum = (int)player.playerBag().getItemCount(ItemConstant.Control_Research_Order);

        int count = type==0?1:Math.min(haveNum,lab.getNum());
        List<Items> costs = ItemUtils.calItemRateReward(commValueConfig.getListItem(CommonValueType.ControlPosColorResearch),count);
        if(!itemBiz.checkItemEnoughAndSpend(player,costs,LogType.ElementResearch)){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        List<Items> rewards = Lists.newArrayList();
        for(int i=1;i<=count;i++){
            rewards.addAll(template.getRandomRewards());
        }
        //减次数
        if(!player.playerElement().reduce(id,count)){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        itemBiz.addItem(player,rewards,LogType.ElementResearch);
        //检查中控室是否解锁了下一个或是否需要重置
        controlBiz.checkLab(player,id);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_Lucky,rewards);
    }

    //研究
    @MsgMethod(MessageComm.C2S_Control_LuckyAll)
    public void luckAll(Player player, JsonMsg msg) {
        List<Items> rewards = Lists.newArrayList();
        controlBiz.luckAll(player, rewards);
        List<Items> goods = ItemBiz.createItemList(rewards);
        itemBiz.addItem(player, goods, LogType.ElementResearch);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_LuckyAll, goods);
    }

    /**
     * 中控转移
     * @param session
     * @param req
     */
    @MsgMethod ( MessageComm.C2S_Control_Transfer)
    public void transfer(Player player, JsonMsg msg) {
        int tankId = msg.getInt("tankId"); // 坦克id
        int desTankId = msg.getInt("desTankId");//目标坦克id
        TankSetting tankSetting = tankConfig.getTankSetting(tankId);
        TankSetting tankSetting2 = tankConfig.getTankSetting(desTankId);
        if(tankSetting.getRare()< TankRareType.SSR.getType()||tankSetting2.getRare()<TankRareType.SSR.getType()||tankSetting.getRare()!=tankSetting.getRare()){
            return;
        }
        Tank tank = player.playerTank().getTank(tankId);
        Tank desTank = player.playerTank().getTank(desTankId);
        if(tank == null||desTank==null) {
            player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
            return;
        }
        //必须都开放中控系统
        if(tank.getLv()<115||desTank.getLv()<115) {
            return;
        }
        //至少要有一个上面装配了元件
        if (!tank.getControl().isUp() && !desTank.getControl().isUp()) {
            return;
        }
        List<Items> cost = commValueConfig.getListItem(CommonValueType.ControlPosColorTransf);
        if(!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.Controltransfer)){
            player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
            return;
        }
        controlBiz.transfer(player,tankId,desTankId);
        player.notifyObservers(ObservableEnum.ControlTransfer,Lists.newArrayList(tankId,desTankId), cost);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_Transfer);
    }



    @MsgMethod ( MessageComm. C2S_Control_ChangePos)
    public void changePos(Player player, JsonMsg msg) {
        int tankId = msg.getInt("tankId");
        int p1 = msg.getInt("p1");
        int p2 = msg.getInt("p2");
        if(p1==p2||p1<0||p1>8||p2<0||p2>8){
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null||tank.getLv()<115){
            return;
        }
        TankControl control = tank.getControl();
        ElementPos pos1 = control.getPos(p1);
        ElementPos pos2 = control.getPos(p1);
        if(pos1==null||pos2==null||(pos1.getElementId()==0&&pos2.getElementId()==0)){
            return;
        }
        tank.getControl().changePos(p1,p2);
        player.notifyObservers(ObservableEnum.ChangeElement,tankId);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Control_ChangePos);
    }
}
