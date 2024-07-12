package com.hm.action.recharge;

import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.util.GameIdUtils;
import com.hm.model.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@NoArgsConstructor
@Document(collection = "PayMsg")
public class PayMsg {
    private String id;
    private String channelid;
    private String productid;
    private String userid;
    private String uid;
    private String serverid;
    private int paytype;
    private Date createDate;

    public PayMsg(Player player, String productid) {
        this.id = player.getId()+"_"+ GameIdUtils.nextStrId();
        this.productid = productid;
        this.userid = player.getId()+"";
        this.serverid = player.getServerId()+"";
        this.channelid = player.getChannelId()+"";
        this.uid = player.getUid()+"";
        this.paytype = 1;
        this.createDate = new Date();
    }
    public void saveDB() {
        MongoUtils.getPayMongoDB().save(this);
    }
}
