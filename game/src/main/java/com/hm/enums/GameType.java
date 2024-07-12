package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.model.player.BaseSamllGame;
import com.hm.model.smallgame.SmallGame2;
import com.hm.model.smallgame.SmallGame3;
import com.hm.model.smallgame.TankFightGame;

public enum GameType {
    TankFight(1, "坦克大战") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new TankFightGame(this.getType());
        }
    },
    SmallGame2(2, "抢滩登陆") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new SmallGame2(this.getType());
        }
    },
    SmallGame3(3, "h5小游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new SmallGame3(this.getType());
        }
    },

    SmallGame4(4, "第4个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame5(5, "第5个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame6(6, "第6个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame7(7, "第7个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame8(8, "第8个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame9(9, "第9个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame10(10, "第10个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame11(11, "第11个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame12(12, "第12个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame13(13, "第13个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame14(14, "第14个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },
    SmallGame15(15, "第15个游戏") {
        @Override
        public BaseSamllGame getPlayerGame() {
            return new BaseSamllGame(this.getType());
        }
    },

    ;


    private GameType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static GameType getGameType(int type) {
        return Lists.newArrayList(GameType.values()).stream().filter(t -> t.getType() == type).findFirst().orElse(null);
    }

    public abstract BaseSamllGame getPlayerGame();
}
