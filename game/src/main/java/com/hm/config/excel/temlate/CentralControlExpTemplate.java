package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("central_control_exp")
public class CentralControlExpTemplate {
    private Integer id;
    private Integer element_exp;
    private Integer element_exp_total;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getElement_exp() {
        return element_exp;
    }

    public void setElement_exp(Integer element_exp) {
        this.element_exp = element_exp;
    }

    public Integer getElement_exp_total() {
        return element_exp_total;
    }

    public void setElement_exp_total(Integer element_exp_total) {
        this.element_exp_total = element_exp_total;
    }
}
