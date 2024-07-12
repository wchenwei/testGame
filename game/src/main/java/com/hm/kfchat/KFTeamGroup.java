package com.hm.kfchat;

import com.hm.enums.KfType;
import com.hm.libcore.mongodb.MongoUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

/**
 * 跨服对应的组
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/8 14:28
 */
public class KFTeamGroup {
    @Id
    private String id;
    private int type;
    private int groupId;
    private int serverId;
    private String dbName;

    public KFTeamGroup(IKFPlayer player) {
        this.id = player.getTeamId() + "_" + KFChatRoomManager.getInstance().getKfType().getType();
        this.type = KFChatRoomManager.getInstance().getKfType().getType();
        this.groupId = player.getGroupId();
        this.serverId = player.getTeamId();
        this.dbName = KFChatRoomManager.getInstance().getChatMsgDB().getDBName();
    }

    public void saveDB() {
        MongoUtils.getLoginMongodDB().save(this);
    }


    public static void deleteType(KfType type) {
        Query query = Query.query(Criteria.where("type").is(type.getType()));
        MongoUtils.getLoginMongodDB().getMongoTemplate().remove(query, KFTeamGroup.class);
    }
}
