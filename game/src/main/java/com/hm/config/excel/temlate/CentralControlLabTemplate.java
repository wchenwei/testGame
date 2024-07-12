package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("central_control_lab")
public class CentralControlLabTemplate {
    private Integer id;
    private Integer library_level;
    private String library;
    private Integer limit;
    private Integer color;
    private String desc;
    private String element_level;
    private String next_lab;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLibrary_level() {
        return library_level;
    }

    public void setLibrary_level(Integer library_level) {
        this.library_level = library_level;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getElement_level() {
        return element_level;
    }

    public void setElement_level(String element_level) {
        this.element_level = element_level;
    }

    public String getNext_lab() {
        return next_lab;
    }

    public void setNext_lab(String next_lab) {
        this.next_lab = next_lab;
    }
}
