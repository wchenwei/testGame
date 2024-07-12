package com.hm.model.tank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlLab {
    private int id;
    private int num;//剩余研发次数

    public void reduce(int count) {
        this.num= Math.max(0,this.num-count);
    }
}
