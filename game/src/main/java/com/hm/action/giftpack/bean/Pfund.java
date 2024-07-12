package com.hm.action.giftpack.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Pfund {
    private int id;//1-关卡基金 2-头衔基金
    private int status;//是否购买计费了 =1代表已经购买了

    //0-普通的奖励领取到多少等级  1-计费的奖励领取到多少等级
    private int[] vals = new int[2];//领取到哪个id了

    public Pfund(int id) {
        this.id = id;
    }

    public boolean isBuyRecharge() {
        return this.status == 1;
    }

}
