package com.hm.action.wx;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WXSubInfo {
    private long playerId;
    private int serverId;
    /**
     * @see WXSubsType#getType()
     */
    private int subId;
    private Long endTime;

    public boolean canSend() {
        return endTime <= System.currentTimeMillis();
    }
}
