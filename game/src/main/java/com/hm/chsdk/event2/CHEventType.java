package com.hm.chsdk.event2;


import lombok.Getter;

/**
 * 草花事件
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/11/14 16:02
 */
@Getter
public enum CHEventType {
    register("1", "register_conversion"),
    Login("2", "role_login"),
    Task("3", "task"),
    Activity("4", "operational_activities"),
    PVE("5", "pve_play"),
    Attack("6", "attack"),
    PVP("6", "pvp_play"),

    Cultivate("7", "cultivate"),
    Share("8", "share"),

    ;

    private CHEventType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    private String type;
    private String name;
}
