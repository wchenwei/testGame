package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_season_pass_task")
public class KfSeasonPassTaskTemplate {
    private Integer task_id;
    private Integer kf_type;
    private Integer task_type;
    private String task_icon;
    private String task_name;
    private String task_sec;
    private String task_finish;
    private String task_reward;
    private Integer pass_exp;
    private Integer goto_ui;
    private Integer last_task;

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Integer getKf_type() {
        return kf_type;
    }

    public void setKf_type(Integer kf_type) {
        this.kf_type = kf_type;
    }

    public Integer getTask_type() {
        return task_type;
    }

    public void setTask_type(Integer task_type) {
        this.task_type = task_type;
    }

    public String getTask_icon() {
        return task_icon;
    }

    public void setTask_icon(String task_icon) {
        this.task_icon = task_icon;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_sec() {
        return task_sec;
    }

    public void setTask_sec(String task_sec) {
        this.task_sec = task_sec;
    }

    public String getTask_finish() {
        return task_finish;
    }

    public void setTask_finish(String task_finish) {
        this.task_finish = task_finish;
    }

    public String getTask_reward() {
        return task_reward;
    }

    public void setTask_reward(String task_reward) {
        this.task_reward = task_reward;
    }

    public Integer getPass_exp() {
        return pass_exp;
    }

    public void setPass_exp(Integer pass_exp) {
        this.pass_exp = pass_exp;
    }

    public Integer getGoto_ui() {
        return goto_ui;
    }

    public void setGoto_ui(Integer goto_ui) {
        this.goto_ui = goto_ui;
    }

    public Integer getLast_task() {
        return last_task;
    }

    public void setLast_task(Integer last_task) {
        this.last_task = last_task;
    }
}
