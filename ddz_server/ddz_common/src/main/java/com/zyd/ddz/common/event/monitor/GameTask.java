package com.zyd.ddz.common.event.monitor;

import xyz.noark.log.LogHelper;

import java.util.concurrent.TimeUnit;

public abstract class GameTask implements Runnable {

    @Override
    public void run() {
        try {
            doAction();
        }catch (Exception e){
            LogHelper.logger.info("{} 任务执行错误", e.getMessage());
        }
    }

    public abstract void doAction();

    protected abstract long getInitialDelay();

    protected abstract long getDelay();

    protected abstract TimeUnit getUnit();
}
