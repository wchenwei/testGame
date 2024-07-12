package com.hm.enums;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 *          分享领取奖励枚举
 * @date 2021/1/8 11:04
 */
public enum ShareEnum {
    ADD_DESKTOP_REWARD(1001,"添加桌面赠礼,只可领取一次"){
        @Override
        public List<Items> getItem() {
            return super.getByType(CommonValueType.ShareDeskReward);
        }
    },
    WX_GZYXQ(1002,"微信关注游戏圈"){
        @Override
        public List<Items> getItem() {
            return super.getByType(CommonValueType.GZYXQ);
        }
    },
    DY_Sidebar(1003, "抖音侧边栏"){
        @Override
        public List<Items> getItem() {
            return super.getByType(CommonValueType.DYSideBar);
        }
    },
//    WX_GZGZH(1003,"微信关注公众号"){
//        @Override
//        public List<Items> getItem() {
//            return super.getByType(CommonValueType.ShareDeskReward);
//        }
//    },
//    ADD_FLOATING_WINDOW_REWARD(67,"添加浮窗赠礼,只可领取一次"){
//        @Override
//        public void sendMail(Player player) {
//            super.send(player, MailConfigEnum.ADD_FLOATING_WINDOW_REWARD);
//        }
//    },
//    COLLECT_REWARD(69,"收藏赠礼,只可领取一次"){
//        @Override
//        public void sendMail(Player player) {
//            super.send(player, MailConfigEnum.COLLECT_REWARD);
//        }
//    },
//    AUTHORIZED_GIFTS(70,"白泽授权好礼,只可领取一次"){
//        @Override
//        public void sendMail(Player player) {
//            super.send(player, MailConfigEnum.AUTHORIZED_GIFTS);
//        }
//    },
    ;

    private ShareEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    private int type;
    private String desc;

    public int getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }

    public static ShareEnum getShareByType(int type){
        for (ShareEnum shareEnum : ShareEnum.values()) {
            if (shareEnum.getType() == type) {
                return shareEnum;
            }
        }
        return null;
    }

    /**
     * @description
     *          是否可以领取
     * @param player
     * @return boolean
     * @author wyp
     * @date 2021/1/9 11:32
     */
    public boolean checkCanRevice(Player player, StatisticsType statisticsType){
        long lifeStatistics = player.getPlayerStatistics().getLifeStatistics(statisticsType);
        if(lifeStatistics > 0){
            return false;
        }
        return true;
    }

    /**
     * @description
     *      添加事件统计
     * @param player
     * @return void
     * @author wyp
     * @date 2021/1/9 11:31
     */
    public void addReward(Player player, StatisticsType statisticsType){
        player.getPlayerStatistics().addLifeStatistics(statisticsType);
    }

    /**
     * @description
     *          获取奖励物品
     * @return java.util.List<com.hm.model.item.Items>
     * @author wyp
     * @date 2021/1/9 11:31
     */
    public List<Items> getItem(){
        return null;
    };
    /**
     * @description
     *      发送邮件
     * @param player
     * @return void
     * @author wyp
     * @date 2021/1/9 15:05
     */
    public void sendMail(Player player){};


    private List<Items> getByType(CommonValueType commonValueType){
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        List<Items> listItem = commValueConfig.getListItem(commonValueType.getType());
        return listItem;
    }

}
