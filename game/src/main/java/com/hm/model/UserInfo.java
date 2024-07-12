package com.hm.model;

import java.util.LinkedList;

public class UserInfo {
    // 最近登录
    private LinkedList<Integer> laterLoginServerList;


    public LinkedList<Integer> getLaterLoginServerList() {
        return laterLoginServerList;
    }

    public void setLaterLoginServerList(Integer lastLoginServerId) {
        if (laterLoginServerList != null) {
            if (laterLoginServerList.contains(lastLoginServerId)) {
                laterLoginServerList.remove(lastLoginServerId);
                laterLoginServerList.addLast(lastLoginServerId);
            } else {
                laterLoginServerList.addLast(lastLoginServerId);
            }
            if (laterLoginServerList.size() > 4) {
                laterLoginServerList.removeFirst();
            }
        } else {
            laterLoginServerList = new LinkedList<>();
            laterLoginServerList.add(lastLoginServerId);
        }
    }

    public void resetLaterLoginServer(LinkedList<Integer> serverIds) {
        this.laterLoginServerList = serverIds;
    }
}
