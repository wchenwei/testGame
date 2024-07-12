package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_element")
public class ItemElementTemplate {
    private Integer id;
    private String name;
    private String icon;
    private String desc;
    private Integer level;
    private Integer color;
    private String attr;
    private Long cost_total;
    private Long levelup_cost;
    private Integer next_id;
    private Integer before_id;
    private Integer element_exp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Long getCost_total() {
        return cost_total;
    }

    public void setCost_total(Long cost_total) {
        this.cost_total = cost_total;
    }

    public Long getLevelup_cost() {
        return levelup_cost;
    }

    public void setLevelup_cost(Long levelup_cost) {
        this.levelup_cost = levelup_cost;
    }

    public Integer getNext_id() {
        return next_id;
    }

    public void setNext_id(Integer next_id) {
        this.next_id = next_id;
    }

    public Integer getBefore_id() {
        return before_id;
    }

    public void setBefore_id(Integer before_id) {
        this.before_id = before_id;
    }

    public Integer getElement_exp() {
        return element_exp;
    }

    public void setElement_exp(Integer element_exp) {
        this.element_exp = element_exp;
    }
}
