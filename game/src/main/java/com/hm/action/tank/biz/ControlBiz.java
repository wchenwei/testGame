package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.ControlConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.templaextra.CentralControlLabExtraTemplate;
import com.hm.config.excel.templaextra.ItemElementExtraTemplate;
import com.hm.enums.*;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.*;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.sysConstant.ItemConstant;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.skillnew.Skill;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class ControlBiz implements IObserver {
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private CommValueConfig commValueConfig;
    @Resource
    private ControlConfig controlConfig;
    @Resource
    private TankSkillConfig tankSkillConfig;

    public void changeStone(Player player, int tankId, int elementId, int index) {
        Tank tank = player.playerTank().getTank(tankId);
        int oldElementId = tank.getControl().getPos()[index].getElementId();
        tank.getControl().changeElement(index,elementId);
        player.playerTank().SetChanged();
        if(oldElementId>0){
            itemBiz.addItem(player, new Items(oldElementId, 1, ItemType.Element), LogType.ChangeElement.value(tankId));
        }

    }

    public ElementPos[] initPos() {
        ElementPos[] pos = new ElementPos[9];
        List<Integer> colors = StringUtil.splitStr2IntegerList(commValueConfig.getStrValue(CommonValueType.ControlPosColorInit),",");
        for(int i=0;i<colors.size();i++){
            pos[i] = new ElementPos(colors.get(i));
        }
        return pos;
    }

    /**
     * 石头升级的消耗
     * @param id
     * @param index
     * @return
     */
//    public List<Items> getElementLvUpCost(Player player, int tankId, int index){
//        Tank tank = player.playerTank().getTank(tankId);
//        int elementId = tank.getControl().getPos(index).getElementId();//该部位的元件id
//        List<Items> cost = Lists.newArrayList();
//        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
//        cost.addAll(template.getLvUpCost());
//        //升级需要消耗背包里两个一样的石头，把槽位上的石头升级
//        getElementLvUpCost(player, elementId, 2, cost);
//        return cost;
//    }

    /**
     *
     * @param player
     * @param stoneId 需要的石头id
     * @param num 需要的数量
     * @param cost
     * @return
     */
//    public List<Items> getElementLvUpCost(Player player,int elementId,long needNum,List<Items> cost){
//        ItemElementExtraTemplate template = controlConfig.getElement(elementId);
//        long haveNum = player.playerStone().getItemCount(elementId);//拥有的该石头数量
//        long oweNum = needNum - haveNum;//缺的石头数
//        //不缺了
//        if(oweNum<=0){
//            cost.add(new Items(elementId,needNum,ItemType.STONE));
//        }else{//还缺
//            //先把拥有的加进去
//            if(haveNum>0){
//                cost.add(new Items(elementId,haveNum,ItemType.STONE));
//            }
//            if(template.getLevel()==1){//如果换算成1级石头还缺的话不再换算
//                cost.clear();
//            }else{
//                //先把所需的其它资源加进去
//                ItemElementExtraTemplate beforeTemplate = controlConfig.getElement(template.getBefore_id());
//                cost.addAll(beforeTemplate.getLvUpCost(oweNum));
//                getElementLvUpCost(player,template.getBefore_id(),oweNum*3,cost);
//            }
//        }
//        return cost;
//    }

    public boolean checkUpAll(Player player, TankControl control, String ups){
        for(String str:ups.split(",")){
            int index = Integer.parseInt(str.split(":")[0]);
            int elementId = Integer.parseInt(str.split(":")[1]);
            ElementPos pos = control.getPos(index);
            if(pos.getElementId()!=0){//如果该位置上没有元件
                return false;
            }
            ItemElementExtraTemplate template = controlConfig.getElement(elementId);
            if(template==null||template.getColor()!=pos.getColor()){//颜色不匹配
                return false;
            }
        }
        return true;
    }
    //计算大技能和小技能
    public void calControlSkill(Player player,int tankId){
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return;
        }
        TankControl tankControl = tank.getControl();
        ElementSkill[] skills = new ElementSkill[]{null,null,null};
        //小技能
        //获得1:2:3,4:5:6,7:8:9的元件类型组合和等级
        for(int i=0;i<3;i++){
            List<ElementPos> s = Lists.newArrayList(tankControl.getPos()[3*i+0],tankControl.getPos()[3*i+1],tankControl.getPos()[3*i+2]);
            List<ItemElementExtraTemplate> templates = s.stream().map(t->{
                ItemElementExtraTemplate template = controlConfig.getElement(t.getElementId());
                return template;
            }).collect(Collectors.toList());
            if(templates.stream().anyMatch(t ->t==null)){
                continue;
            }
            String colors = StringUtil.list2Str(templates.stream().map(t->t.getColor()).collect(Collectors.toList()),":");
            int skillId = controlConfig.getSkillByColors(colors);
            int skillLv = templates.stream().mapToInt(t->t.getLevel()).min().orElse(0);
            if(skillId>0){
                skills[i] = new ElementSkill(skillId,skillLv);
            }
        }
        tankControl.setSkills(skills);
        //计算战意技
        List<ElementPos> bigSkillPos = Lists.newArrayList(tankControl.getPos()[0],tankControl.getPos()[3],tankControl.getPos()[6]);
        List<ItemElementExtraTemplate> templates = bigSkillPos.stream().map(t->{
            ItemElementExtraTemplate template = controlConfig.getElement(t.getElementId());
            return template;
        }).collect(Collectors.toList());
        //战意技激活的孔位各颜色石头数量
        List<Integer> bigColors = Lists.newArrayList();
        for(int i=1;i<=3;i++){
            //*色石头数量
            int color = i;
            bigColors.add((int)templates.stream().filter(t->t!=null&&t.getColor().intValue()==color).count());
        }
        String colorNums = StringUtil.list2Str(bigColors,":");
        int skillId = controlConfig.getBigSkillByColors(colorNums);
        int power = getPower(player,tankId);
        int skillLv = controlConfig.getBigSkillLv(power);
        ElementSkill bigSkill = new ElementSkill(skillId,skillLv);
        tankControl.setBigSkill(bigSkill);
        player.playerTank().SetChanged();
    }
    //计算坦克中控系统元件提供的经验
    public int getPower(Player player,int tankId){
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return 0;
        }
        TankControl tankControl = tank.getTankControl();
        return Arrays.stream(tankControl.getPos()).mapToInt(t->{
            ItemElementExtraTemplate template = controlConfig.getElement(t.getElementId());
            return template==null?0:template.getElement_exp();
        }).sum();
    }
    //获取中控系统给坦克附加的属性
    public TankAttr getControlTankAttr(Player player, int tankId){
        TankAttr tankAttr = new TankAttr();
        Tank tank = player.playerTank().getTank(tankId);
        if(tank==null){
            return tankAttr;
        }
        TankControl tankControl = tank.getTankControl();
        Arrays.stream(tankControl.getPos()).filter(t->t!=null).forEach(t ->{
            ItemElementExtraTemplate template = controlConfig.getElement(t.getElementId());
            if(template!=null){
                tankAttr.addAttr(template.getAttrMap());
            }
        });
        return tankAttr;
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock,this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        int type = (int)argv[0];
        if(type== PlayerFunctionType.Control.getType()){
            //初始化中控室
            initControl(player);
        }
    }

    public void initControl(Player player){
        CentralControlLabExtraTemplate template = controlConfig.getLabInit();
        if(template!=null){
            player.playerElement().addLib(new ControlLab(template.getId(),template.getLimit()));
        }
    }

    public void checkLab(Player player, int id) {
        ControlLab lab = player.playerElement().getLab(id);
        CentralControlLabExtraTemplate template = controlConfig.getLab(id);
        if(lab==null||template==null){
            return;
        }
        if(lab.getNum()<=0){
            //生成下一个中控室
            int nextId = template.randomNextId();
            if(nextId>0){
                CentralControlLabExtraTemplate nextTemplate = controlConfig.getLab(nextId);
                player.playerElement().addLib(new ControlLab(nextId,nextTemplate.getLimit()));
            }else{
                //清除所有中控室并重新初始化
                player.playerElement().clearLab();
                initControl(player);
            }
        }
    }

    public void transfer(Player player, int tankId, int desTankId) {
        Tank tank = player.playerTank().getTank(tankId);
        Tank desTank = player.playerTank().getTank(desTankId);
        TankControl control = new TankControl(tank.getControl().getPos(),tank.getControl().getSkills(),tank.getControl().getBigSkill());
        TankControl desControl = new TankControl(desTank.getControl().getPos(),desTank.getControl().getSkills(),desTank.getControl().getBigSkill());

        player.playerTank().changeControl(tankId, desControl);
        player.playerTank().changeControl(desTankId, control);
    }

    public boolean isCanLvUp(int lv,String ids){
        int num = 0;//换算成1级的数量
        for(int id:StringUtil.splitStr2IntegerList(ids,",")){
            ItemElementExtraTemplate template = controlConfig.getElement(id);
            if(template!=null){
                num+=Math.pow(3, template.getLevel()-1);
            }
        }
        return num==Math.pow(3,lv-1)*2;
    }


    public List<Items> getCost(int nowId, int nextId, String ids) {
        ItemElementExtraTemplate nowTemplate = controlConfig.getElement(nowId);
        ItemElementExtraTemplate nextTemplate = controlConfig.getElement(nextId);
        long cash = nextTemplate.getCost_total() - nowTemplate.getCost_total();
        List<Items> costs = Lists.newArrayList();
        for(int id:StringUtil.splitStr2IntegerList(ids,",")){
            ItemElementExtraTemplate template = controlConfig.getElement(id);
            if(template!=null){
                costs.add(new Items(template.getId(),1,ItemType.Element));
                cash -= template.getCost_total();
            }
        }
        costs.add(new Items(PlayerAssetEnum.Cash.getTypeId(),cash,ItemType.CURRENCY));
        return ItemBiz.createItemList(costs);
    }

    public List<Skill> getControlSkill(Tank tank) {
        List<Skill> skills = Lists.newArrayList();
        //战意技
        ElementSkill skill = tank.getControl().getBigSkill();
        if (skill != null) {
            int bigSkillId = controlConfig.getBigSkillId(skill.getSkillId(), skill.getLv());
            if (bigSkillId > 0) {
                SkillSetting setting = tankSkillConfig.getSkillSetting(bigSkillId);
                skills.add(new Skill(1, tankSkillConfig.getSkillSetting(bigSkillId)));
            }
        }
        Arrays.stream(tank.getControl().getSkills()).filter(t -> t != null).forEach(t -> {
            skills.add(new Skill(t.getLv(), tankSkillConfig.getSkillSetting(t.getSkillId())));
        });
        return skills;
    }

    public void luckAll(Player player, List<Items> rewardAll) {
        ControlLab nowLab = player.playerElement().getNowLab();
        CentralControlLabExtraTemplate template = controlConfig.getLab(nowLab.getId());
        //拥有令牌的个数
        int haveNum = (int) player.playerBag().getItemCount(ItemConstant.Control_Research_Order);
        if (haveNum <= 0) {
            return;
        }
        int count = Math.min(haveNum, nowLab.getNum());
        List<Items> costs = ItemUtils.calItemRateReward(commValueConfig.getListItem(CommonValueType.ControlPosColorResearch), count);
        if (!itemBiz.checkItemEnoughAndSpend(player, costs, LogType.ElementResearch)) {
            return;
        }
        List<Items> rewards = Lists.newArrayList();
        for (int i = 1; i <= count; i++) {
            List<Items> reward = template.getRandomRewards();
            rewards.addAll(reward);
            rewardAll.addAll(reward);
        }
        //减次数
        if (!player.playerElement().reduce(nowLab.getId(), count)) {
            return;
        }
        checkLab(player, nowLab.getId());
        luckAll(player, rewardAll);
    }


    public static void main(String[] args){
        int lv = 4;
        System.out.println(Math.pow(3, lv-1));
    }
}
