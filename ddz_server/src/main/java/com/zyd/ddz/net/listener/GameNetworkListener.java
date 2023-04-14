package com.zyd.ddz.net.listener;

import com.alibaba.fastjson.JSON;
import xyz.noark.core.annotation.Component;
import xyz.noark.core.network.NetworkListener;
import xyz.noark.core.network.NetworkPacket;
import xyz.noark.core.network.Session;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/15 11:01
 */
@Component
public class GameNetworkListener implements NetworkListener {
    @Override
    public void channelActive(Session session) {
        session.send(1, JSON.toJSONString("heart"));
        logger.info("发送心跳包");
    }

    @Override
    public void channelInactive(Session session) {

    }

    @Override
    public boolean handleDuplicatePacket(Session session, NetworkPacket networkPacket) {
        return false;
    }

    @Override
    public boolean handleChecksumFail(Session session, NetworkPacket networkPacket) {
        return false;
    }

    @Override
    public void handleDeprecatedPacket(Session session, NetworkPacket networkPacket) {

    }

    @Override
    public boolean handlePacketWarning(Session session, int i, int i1, int i2) {
        return false;
    }

    @Override
    public void handleException(Session session, NetworkPacket networkPacket, Throwable throwable) {

    }
}
