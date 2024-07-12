package com.hm.action.player.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.config.GameConstants;
import com.hm.config.GameRandomNameConfig;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.player.PlayerDataItemContainer;
import com.hm.servercontainer.player.PlayerDataServerContainer;
import com.hm.util.RandomUtils;

import javax.annotation.Resource;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2023/3/13 14:32
 */
@Biz
public class PlayerNameBiz implements IObserver {
    @Resource
    private GameRandomNameConfig randomNameConfig;


    /**
     * 检查名字是否重复
     * @param serverId
     * @param name
     * @return
     */
    public boolean checkNameRepeat(int serverId, String name){
        PlayerDataItemContainer playerDataItemContainer = PlayerDataServerContainer.of(serverId);
        if (playerDataItemContainer == null){
            return false;
        }
        return playerDataItemContainer.checkNameRepeat(name);
    }

    /**
     * 根据服务器获取玩家唯一名字
     * @param serverId
     * @return
     */
    public String randomPlayerName(int serverId){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < GameConstants.PLAYER_NAME_RANDOM_MAX; i++) {
            sb.setLength(0);
            String pre = RandomUtils.randomEle(randomNameConfig.getPrefix());
            String suf = RandomUtils.randomEle(randomNameConfig.getSuffix());
            sb.append(pre).append(suf);
            if (!checkNameRepeat(serverId, sb.toString()))
                return sb.toString();
        }
        //多次都找不到不重名的玩家
        for (int i = 1; i < GameConstants.PLAYER_NAME_RANDOM_MAX; i++) {
            sb.append(i);
            if (!checkNameRepeat(serverId, sb.toString()))
                return sb.toString();
            sb.setLength(0);
        }
        return System.currentTimeMillis() +"";
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeName, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerCreate, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        PlayerDataItemContainer playerDataItemContainer = PlayerDataServerContainer.of(player);
        if (playerDataItemContainer == null) {
            return;
        }
        if (observableEnum == ObservableEnum.PlayerCreate) {
            playerDataItemContainer.updateName(null, player.getName());
        } else {
            String oldName = (String) argv[0];
            playerDataItemContainer.updateName(oldName, player.getName());
        }

    }
}
