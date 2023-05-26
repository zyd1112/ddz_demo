package com.zyd.ddz.common.event.monitor;

import xyz.noark.core.thread.MonitorThreadPool;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author zyd
 * @date 2023/5/26 15:52
 */
public abstract class Monitor implements MonitorThreadPool {

    public void addScheduleService(GameTask task) {
        ((ScheduledExecutorService) getMonitorService()).scheduleWithFixedDelay(task, task.getInitialDelay(), task.getDelay(), task.getUnit());
    }

}
