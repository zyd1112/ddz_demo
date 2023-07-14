package com.zyd.ddz.common.net.listener;

import com.zyd.zgame.common.network.NetworkEventListener;
import com.zyd.zgame.common.network.Session;
import com.zyd.zgame.common.network.SessionManager;
import io.netty.handler.timeout.IdleState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author zyd
 * @date 2023/4/15 11:01
 */
@Component
@Slf4j
public class GameNetworkListener implements NetworkEventListener {

    @Override
    public void onActive(Session session) {
        log.info("{} 客户端连接成功.......", session.getIp());
    }

    @Override
    public void onChannelInactive(Session session) {
        log.info("{}, 断开连接", session.getUid());
        SessionManager.removeSession(session);
    }

    @Override
    public void onExceptionOccur(Session var1, Throwable var2) {

    }

    @Override
    public void idle(Session var1, IdleState var2) {

    }

    @Override
    public boolean onPacketWarning(Session session, int second, int count, int threshold) {
        return false;
    }
}
