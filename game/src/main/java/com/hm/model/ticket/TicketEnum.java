package com.hm.model.ticket;

import com.hm.config.excel.temlate.ItemTemplate;

/**
 * @author wyp
 * @description
 * @date 2020/10/23 17:13
 */
public enum TicketEnum {
    DiscountTicket(68,"折扣券"){
        @Override
        public Ticket getTicket(ItemTemplate itemTemplate) {
            return new DiscountTicket(itemTemplate);
        }
    },
    DeductionTicket(69,"抵扣券"){
        @Override
        public Ticket getTicket(ItemTemplate itemTemplate) {
            return new DeductionTicket(itemTemplate);
        }
    },

    ;

    private int type; // 与 SubItemType 中类型相对照

    private String desc;

    private TicketEnum(int type,String desc){
        this.type = type;
        this.desc = desc;
    }

    public static TicketEnum getTicketEnum(int type) {
        for (TicketEnum buildType : TicketEnum.values()) {
            if (buildType.getType() == type) {
                return buildType;
            }
        }
        return null;
    }

    public abstract Ticket getTicket(ItemTemplate itemTemplate);

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
