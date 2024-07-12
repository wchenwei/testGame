package com.hm.action.http.model;

import com.hm.enums.ActivityType;

import java.util.Date;

/**
 * Description:
 * User: yang xb
 * Date: 2018-05-17
 */
public class ActivityEntity {
    private Integer id;
    private Integer activityId;
    private Integer serverId;
    private Date viewTime;
    private Date startTime;
    private Date clearingTime;
    private Date endTime;
    private Integer status;
    private String extend;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Date getViewTime() {
        return viewTime;
    }

    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getClearingTime() {
        return clearingTime;
    }

    public void setClearingTime(Date clearingTime) {
        this.clearingTime = clearingTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ActivityType getActivityType() {
        return ActivityType.getActivityType(activityId);
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
