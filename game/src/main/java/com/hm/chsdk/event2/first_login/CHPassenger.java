package com.hm.chsdk.event2.first_login;

import cn.hutool.core.util.StrUtil;
import com.hm.model.passenger.Passenger;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerPassenger;
import com.hm.model.tank.Tank;

import java.util.Arrays;

public class CHPassenger {
    public int id;//id

    public Integer pd1;
    public Integer pl1;
    public Integer ps1;

    public Integer pd2;
    public Integer pl2;
    public Integer ps2;

    public Integer pd3;
    public Integer pl3;
    public Integer ps3;

    public Integer pd4;
    public Integer pl4;
    public Integer ps4;

    public Integer pd5;
    public Integer pl5;
    public Integer ps5;

    public Integer pd6;
    public Integer pl6;
    public Integer ps6;

    public CHPassenger(Tank tank, Player player) {
        String[] passengers = tank.getTankPassenger().getpassengers();
        PlayerPassenger playerPassenger = player.playerPassenger();
        Passenger passenger = playerPassenger.getPassenger(passengers[0]);
        if(passenger != null) {
            this.pd1 = passenger.getId();
            this.pl1 = passenger.getLv();
            this.ps1 = passenger.getStar();
        }

        passenger = playerPassenger.getPassenger(passengers[1]);
        if(passenger != null) {
            this.pd2 = passenger.getId();
            this.pl2 = passenger.getLv();
            this.ps2 = passenger.getStar();
        }

        passenger = playerPassenger.getPassenger(passengers[2]);
        if(passenger != null) {
            this.pd3 = passenger.getId();
            this.pl3 = passenger.getLv();
            this.ps3 = passenger.getStar();
        }

        passenger = playerPassenger.getPassenger(passengers[3]);
        if(passenger != null) {
            this.pd4 = passenger.getId();
            this.pl4 = passenger.getLv();
            this.ps4 = passenger.getStar();
        }

        passenger = playerPassenger.getPassenger(passengers[4]);
        if(passenger != null) {
            this.pd5 = passenger.getId();
            this.pl5 = passenger.getLv();
            this.ps5 = passenger.getStar();
        }

        passenger = playerPassenger.getPassenger(passengers[5]);
        if(passenger != null) {
            this.pd6 = passenger.getId();
            this.pl6 = passenger.getLv();
            this.ps6 = passenger.getStar();
        }
        this.id = tank.getId();
    }

    public boolean allIsNull(String[] passengers) {
        return Arrays.stream(passengers).allMatch(e -> StrUtil.isEmpty(e));
    }
}
