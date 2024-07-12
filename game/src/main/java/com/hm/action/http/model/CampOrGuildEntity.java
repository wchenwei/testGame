package com.hm.action.http.model;

import com.hm.model.guild.Guild;
import lombok.Data;

import java.io.Serializable;

@Data
public class CampOrGuildEntity implements Serializable {
    private int id;
    private int type;//1阵营；2部落
    private String notice = "";
    public CampOrGuildEntity(){}


    public CampOrGuildEntity(Guild guild) {
        this.id = guild.getId();
        this.type=2;
        this.notice = guild.getGuildInfo().getNotice();
    }
}
