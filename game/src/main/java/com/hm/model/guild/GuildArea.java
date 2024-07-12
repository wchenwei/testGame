package com.hm.model.guild;

import com.hm.libcore.msg.JsonMsg;

public class GuildArea extends GuildComponent{
    private int cityCount;

    public void addCityCount(){
        this.cityCount ++;
        SetChanged();
    }

    public void reduceCount(){
        this.cityCount = Math.max(0, cityCount-1);
        SetChanged();
    }

    @Override
    public void fillMsg(JsonMsg msg, boolean ignoreChange) {
        msg.addProperty("guildArea", this);
    }
}
