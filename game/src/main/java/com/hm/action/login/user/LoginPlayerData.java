package com.hm.action.login.user;

import com.hm.model.player.BasePlayer;
import com.hm.redis.type.RedisTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginPlayerData {
    private long id;//玩家id
    private int lv;
    private String name;
    private long time;

    public LoginPlayerData(BasePlayer player) {
        setId(player.getId());
        this.name = player.getName();
        this.lv = player.playerCommander().getMilitaryLv();
        this.time = player.playerBaseInfo().getLastLoginDate().getTime();
    }

    public static void update(BasePlayer player) {
        RedisTypeEnum.LoginPlayerData.putObject(getId(player), new LoginPlayerData(player));
    }

//    public static void del(BasePlayer player) {
//        RedisTypeEnum.LoginPlayerData.delHKey(getId(player));
//    }

    private static String getId(BasePlayer player){
        return player.getUid() + "_" + player.getCreateServerId();
    }

}
