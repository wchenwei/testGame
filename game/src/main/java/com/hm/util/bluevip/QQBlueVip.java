package com.hm.util.bluevip;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * qq蓝砖信息
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/8/26 9:20
 */
@Data
@NoArgsConstructor
public class QQBlueVip {
    private int blueVipLv;
    // 记录新手礼包、以及等级礼包
    private int isBlueVip;
    private int isBlueYearVip;
    private int isSuperBlueVip;

    public QQBlueVip(int blueVipLv, int isBlueVip, int isBlueYearVip, int isSuperBlueVip) {
        this.blueVipLv = blueVipLv;
        this.isBlueVip = isBlueVip;
        this.isBlueYearVip = isBlueYearVip;
        this.isSuperBlueVip = isSuperBlueVip;
    }

    public boolean checkBlueVip() {
        return isBlueVip == 1 || isSuperBlueVip == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QQBlueVip qqBlueVip = (QQBlueVip) o;
        return blueVipLv == qqBlueVip.blueVipLv &&
                isBlueVip == qqBlueVip.isBlueVip &&
                isBlueYearVip == qqBlueVip.isBlueYearVip &&
                isSuperBlueVip == qqBlueVip.isSuperBlueVip;
    }

    public boolean isSame(QQBlueVip temp) {
        return this.equals(temp);
    }
}
