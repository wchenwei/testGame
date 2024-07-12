package com.hm.model.serverpublic;

/**
 * Description:王牌竞技场数据
 * User: yang xb
 * Date: 2019-04-08
 *
 * @author Administrator
 */
public class ServerTrumpArenaData extends ServerPublicContext {
    /**
     * 期数
     */
    private int stage;
    private int serverLv;
    /**
     * 当前参与期数唯一id
     */
    private String stageMark;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
        SetChanged();
    }

    public int getServerLv() {
        return serverLv;
    }

    public void setServerLv(int serverLv) {
        this.serverLv = serverLv;
        SetChanged();
    }

    public String getStageMark() {
        return stageMark;
    }

    public void setStageMark(String stageMark) {
        this.stageMark = stageMark;
        SetChanged();
    }
}

