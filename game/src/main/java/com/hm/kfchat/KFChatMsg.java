package com.hm.kfchat;

import com.hm.libcore.util.GameIdUtils;
import com.hm.libcore.util.sensitiveWord.sensitive.SysWordSensitiveUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * 聊天内容
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/3 10:25
 */
@Data
@NoArgsConstructor
public class KFChatMsg {
    private transient String id;//消息唯一id
    private transient long pid;//玩家id
    @Indexed
    private transient int gid;//房间id
    private int type;
    public String content;
    public long time;
    @Transient
    private KFPlayerInfo playerInfo;

    public KFChatMsg(IKFPlayer player, String content) {
        this.id = GameIdUtils.nextStrId();
        this.pid = player.getPlayerId();
        this.gid = player.getGroupId();
        this.content = content;
        this.time = System.currentTimeMillis();
        buildPlayerInfo();
    }

    public void buildPlayerInfo() {
        this.playerInfo = new KFPlayerInfo(pid);
    }

    public void setType(int type) {
        this.type = type;
        replaceSensitiveWord();
    }

    public void replaceSensitiveWord() {
        if (type == 0) {
            content = SysWordSensitiveUtil.getInstance().replaceSensitiveWord(content, "*");
        }
    }
}
