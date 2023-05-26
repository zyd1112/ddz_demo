package com.zyd.ddz.common.event.monitor;

import xyz.noark.core.thread.NamedThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author zyd
 * @date 2023/5/26 15:50
 */
public class MailCheckMonitor extends Monitor {
    private final ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(2, new NamedThreadFactory("mail-check-monitor", false));

    @Override
    public ExecutorService getMonitorService() {
        return this.scheduledExecutor;
    }
}
