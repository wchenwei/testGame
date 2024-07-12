package com.hm.model.serverpublic;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-28
 *
 * @author Administrator
 */
public class ServerMergeData extends ServerPublicContext {
    /**
     * 合服时间
     */
    private long date;
    /**
     * 合服后服务器平均等级
     */
    private int avgLv;
    //和服次数
    private int mergeTimes;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
        SetChanged();
    }

    public int getAvgLv() {
        return avgLv;
    }

    public void setAvgLv(int avgLv) {
        this.avgLv = avgLv;
        SetChanged();
    }

	public int getMergeTimes() {
		return mergeTimes;
	}

	public void setMergeTimes(int mergeTimes) {
		this.mergeTimes = mergeTimes;
		SetChanged();
	}
	public void addMergeTimes() {
		this.mergeTimes ++;
		SetChanged();
	}
    
}
