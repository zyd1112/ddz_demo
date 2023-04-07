package com.zyd.ddz;

import xyz.noark.game.Noark;

/**
 * @author zyd
 * @date 2022/12/26 12:37
 */
public class GameServerApplication {
    public static void main(String[] args) {
        Noark.run(GameServerBootstrap.class, args);
    }
}
