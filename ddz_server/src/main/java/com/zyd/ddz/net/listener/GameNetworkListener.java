package com.zyd.ddz.net.listener;

import com.alibaba.fastjson.JSON;
import xyz.noark.core.annotation.Component;
import xyz.noark.core.network.NetworkListener;
import xyz.noark.core.network.NetworkPacket;
import xyz.noark.core.network.Session;
import xyz.noark.core.network.SessionManager;
import xyz.noark.network.WebSocketSession;

import static xyz.noark.log.LogHelper.logger;

/**
 * @author zyd
 * @date 2023/4/15 11:01
 */
@Component
public class GameNetworkListener implements NetworkListener {
    @Override
    public void channelActive(Session session) {
        logger.info("{} 客户端连接成功.......", session.getIp());
    }

    @Override
    public void channelInactive(Session session) {
        logger.info("{}, 断开连接", session.getPlayerId());
        SessionManager.removeSession(session);
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
