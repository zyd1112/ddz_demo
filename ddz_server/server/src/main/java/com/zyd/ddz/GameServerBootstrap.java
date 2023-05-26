package com.zyd.ddz;

import com.zyd.ddz.common.proto.JsonCodec;
import xyz.noark.core.network.PacketCodec;
import xyz.noark.game.bootstrap.BaseServerBootstrap;

/**
 * @author zyd
 * @date 2022/12/26 12:38
 */
public class GameServerBootstrap extends BaseServerBootstrap {
    @Override
    protected String getServerName() {
        return "zyd_斗地主";
    }

    @Override
    protected String bannerFileName() {
        return "banner.txt";
    }

    @Override
    protected PacketCodec getPacketCodec() {
        return new JsonCodec();
    }
}
