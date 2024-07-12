package com.hm.chsdk.event2;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class CHKeyVal {
    public Object id;
    public Object v;

    public CHKeyVal(Object id, Object v) {
        this.id = id;
        this.v = v;
    }
}
