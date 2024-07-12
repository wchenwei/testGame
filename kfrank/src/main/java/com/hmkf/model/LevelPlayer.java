package com.hmkf.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LevelPlayer extends KFPlayerDataContext {
    private int levelType;//用于变化段位之后的展示
    private int beforeLvType;//之前的类型id

    private int maxLevelType = 1;

    private transient String rankId;//在哪一个排行里


    public int getLevelType() {
        return levelType;
    }

    public void setLevelType(String rankId,int levelType) {
        this.beforeLvType = this.levelType;
        this.rankId = rankId;
        this.maxLevelType = Math.max(levelType, this.levelType);
        this.levelType = levelType;
        SetChanged();
    }


    public boolean clearBeforeLvType() {
        if(this.beforeLvType > 0) {
            this.beforeLvType = 0;
            SetChanged();
            return true;
        }
        return false;
    }
}
